package com.algeo.lib;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

/**
 * This class is used to give solution for tubes algeo-1
 *
 * @author Christian Albert Hasiholan
 * @author Fakih Anugerah Pratama
 * @author Zidane Firzatullah
 */
@Data
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Matrix {

    @Setter(AccessLevel.PRIVATE)
    private double[][] matrix;
    @Setter(AccessLevel.PRIVATE)
    private int horizontalSize;
    @Setter(AccessLevel.PRIVATE)
    private int verticalSize;

    // matrix type 0: matrix is in echelon form, 1: matrix is in reduced echelon form, 2: matrix is in inverse form, else -1.
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private int matrixType = -1;

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.verticalSize = matrix.length;
        this.horizontalSize = matrix[0].length;
    }

    private static class MatrixHelper {

        /**
         * this method is used for debugging purpose
         *
         * @param   matrix 2d double array to be printed
         */
        private static void printMatrix(double[][] matrix) {
            out.println();
            int horizontalSize = matrix[0].length;

            for (double[] doubles : matrix) {
                for (int j = 0; j < horizontalSize; ++j) {
                    out.printf("%f ", doubles[j]);
                }
                out.println();
            }
            out.println();
        }

        private static void switchRows(double[][] matrix, int row1, int row2) {
            int horizontalSize = matrix[0].length;

            for (int i = 0; i < horizontalSize; ++i) {
                double temp = matrix[row1][i];
                matrix[row1][i] = matrix[row2][i];
                matrix[row2][i] = temp;
            }
        }

        private static void multiplyRow(double[][] matrix, int row, double multiplier) {
            int horizontalSize = matrix[0].length;

            for (int i = 0; i < horizontalSize; ++i) {
                if (matrix[row][i] == 0) continue;
                matrix[row][i] *= multiplier;
            }
        }

        private static void rowAdditionRow(double[][] matrix, int targetRow, int baseRow, double rowMultiplication) {
            int horizontalSize = matrix[0].length;

            for (int i = 0; i < horizontalSize; ++i) {
                matrix[targetRow][i] += matrix[baseRow][i] * rowMultiplication;
            }
        }

        private static double[][] getEchelonMatrix(double[][] matrix) {
            int verticalSize = matrix.length, hSize = matrix[0].length, wIdx, lOneIdx, lOneFSIdx;

            for (int i = 0; i < verticalSize; ++i) {
                lOneIdx = -1; wIdx = 0;

                while (wIdx < hSize && lOneIdx == -1) {
                    if (Math.abs(matrix[i][wIdx]) >= 0.00000000001 )
                        lOneIdx = wIdx;

                    ++wIdx;
                }

                for (int j = i+1; j < verticalSize; ++j) {
                    wIdx = 0; lOneFSIdx = -1;
                    while (wIdx < hSize && lOneFSIdx == -1) {
                        if (Math.abs(matrix[i][wIdx]) >= 0.000000000001)
                            lOneFSIdx = wIdx;

                        ++wIdx;
                    }

                    if ((lOneIdx > lOneFSIdx && lOneFSIdx != -1) || (lOneIdx == -1 && lOneFSIdx != -1)) {
                        switchRows(matrix, i, j);
                        lOneIdx = lOneFSIdx;
                        break;
                    }
                }

                if (lOneIdx == -1)
                    continue;

                multiplyRow(matrix, i, 1/matrix[i][lOneIdx]);

                for (int j = i+1; j < verticalSize; ++j) {
                    if (Math.abs(matrix[j][lOneIdx]) <= 0.000000000000001) {
                        continue;
                    }

                    rowAdditionRow(matrix, j, i, matrix[j][lOneIdx] * -1);
                }
            }

            return matrix;
        }

        private static double[][] getReducedEchelonMatrix(double[][] matrix, int limit) {
            double[][] echelonMatrix = getEchelonMatrix(matrix);
            int verticalSize = matrix.length;

            for (int i = verticalSize - 1; i > 0; --i) {
                int ldOneIdx = -1;

                for (int k = 0; k < limit; ++k) {
                    if (Math.abs(1-echelonMatrix[i][k]) <= 0.001) {
                        ldOneIdx = k;
                        break;
                    }
                }

                if (ldOneIdx == -1) continue;

                for (int j = i - 1; j >= 0; --j) {
                    double multiplier = echelonMatrix[j][ldOneIdx];
                    rowAdditionRow(echelonMatrix, j, i, multiplier * -1);
                }

            }

            return echelonMatrix;
        }

        private static double[][] getInverseMatrixSpl(double[][] matrix) {
            return getInverseMatrixMaster(matrix, true);
        }

        private static double[][] getInverseMatrix(double[][] matrix)  {
            return getInverseMatrixMaster(matrix, false);
        }

        private static double[][] getInverseMatrixMaster(double[][] matrix, boolean spl) {
            double[][] rhs = new double[1][1];

            if (spl) {
                rhs = cutMatrixColumns(matrix, matrix[0].length-1, matrix[0].length-1);
                matrix = cutMatrixColumns(matrix, 0, matrix[0].length-2);
            }

            if (matrix.length != matrix[0].length) throw new UnsupportedOperationException("Matrix is not square");

            double[][] matrixAIdentity = mergeMatrices(matrix, generateIdentityMatrix(matrix.length));
            double[][] reducedEMatrix = getReducedEchelonMatrix(matrixAIdentity, matrixAIdentity.length);

            if (!isIdentityMatrix(cutMatrixColumns(reducedEMatrix, 0, reducedEMatrix[0].length-matrix.length-1)))
                throw new UnsupportedOperationException("Matrix does not have an inverse");

            return spl ? mergeMatrices(cutMatrixColumns(matrixAIdentity, matrix.length, reducedEMatrix[0].length - 1), rhs)
                    : cutMatrixColumns(matrixAIdentity, matrix.length, reducedEMatrix[0].length - 1);
        }

        private static double[][] mergeMatrices(double[][] matrix, double[][] mergingAgent) {
            if (matrix.length != mergingAgent.length)
                throw new UnsupportedOperationException("Matrices have different number of rows");

            int matrixVSize = matrix.length, matrixHSize = matrix[0].length;
            double[][] mergedMatrix = new double[matrixVSize][matrixHSize + mergingAgent[0].length];

            for (int i = 0; i < matrixVSize; ++i) {
                for (int j = 0; j < matrixHSize; ++j) {
                    mergedMatrix[i][j] = matrix[i][j];
                }

                for (int j = 0; j < mergingAgent[0].length; ++j) {
                    mergedMatrix[i][j + matrixHSize] = mergingAgent[i][j];
                }
            }

            return mergedMatrix;
        }

        private static double[][] cutMatrixColumns(double[][] matrix, int startColumn, int endColumn) {
            double[][] resultMatrix = new double[matrix.length][endColumn - startColumn + 1];

            for (int i = 0; i < matrix.length; ++i) {
                for (int j = 0; j < endColumn - startColumn + 1; ++j) {
                    resultMatrix[i][j] = matrix[i][startColumn + j];
                }
            }

            return resultMatrix;
        }

        private static double[][] multiplyMatrix(double[][] matrix1, double[][] matrix2) {
            if (matrix1[0].length != matrix2.length)
                throw new UnsupportedOperationException("Matrices could not be operated");

            double[][] resultMatrix = new double[matrix1.length][matrix2[0].length];

            for (int i = 0; i < matrix1.length; ++i) {
                for (int j = 0; j < matrix2[0].length; ++j) {
                    for (int k = 0; k < matrix1.length; ++k) {
                        resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                    }
                }
            }

            return resultMatrix;
        }

        private static boolean isIdentityMatrix(double[][] matrix) {
            for (int i = 0; i < matrix.length; ++i) {
                for (int j = 0; j < matrix[0].length; ++j) {
                    if (i == j) {
                        if (Math.abs(matrix[i][j] - 1) > 0.00001)
                            return false;
                        continue;
                    }

                    if (Math.abs(matrix[i][j]) > 0.00001)
                        return false;
                }
            }
            return true;
        }

        private static double[][] generateIdentityMatrix(int dimension) {
            double[][] identityMatrix = new double[dimension][dimension];

            for (int i = 0; i < dimension; ++i) {
                for (int j = 0; j < dimension; ++j) {
                    if (i == j) {
                        identityMatrix[i][j] = 1;
                        continue;
                    }
                    identityMatrix[i][j] = 0;
                }
            }

            return identityMatrix;
        }

        /**
         * @param matrix    matrix to infer
         * @return int      if matrix has no solution return 0, if matrix has unique solution return 1, if matrix has many
         *                  solution return 2, else -1
         */
        private static int inferSolutionType(double[][] matrix) {
            int type = -1, count = 0, lastIdx = matrix[0].length - 1;

            for (double num : matrix[matrix.length - 1]) {
                if (Math.abs(num) > 0.00001) ++count;
            }

            double lastEl = matrix[matrix.length - 1][lastIdx];

            if (count == 0 || (count > 2 && !(Math.abs(lastEl) <= 0.0001)) || (count > 1 && (Math.abs(lastEl) <= 0.0001))) type = 2;

            if (count == 1 && !(Math.abs(lastEl) <= 0.0001)) type = 0;

            if (count >= 1 && type == -1) type = 1;

            if (type == -1)
                throw new UnsupportedOperationException("error while inferring matrix's solution type");

            return type;
        }

        private static double[][] solveEchelonFormMatrix(double[][] matrix) {
            int vSize = matrix.length, hSize = matrix[0].length, lOneIdx, wIndex, sType, countS, foundS;
            double sumRow, rowResult;
            double[][] rMatrix = MatrixHelper.cutMatrixColumns(matrix, hSize - 1, hSize- 1), resultMatrix;

            sType = inferSolutionType(matrix);

            if (sType == 0) {
                throw new UnsupportedOperationException("Matrix does not have a solution");
            }

            resultMatrix = sType == 1 ? new double[1][hSize-1] : new double[vSize][hSize-1];

            if (sType == 2) {
                double[][] sResultHolder = new double[2][hSize];
                resultMatrix = copyMatrix(matrix);

                for (int i = vSize-1; i >= 0; --i) {
                    foundS = 0;
                    countS = 0; lOneIdx = -1; sumRow = 0;
                    for (int j = 0; j < hSize-1; ++j) {
                        if (sResultHolder[1][j] == 1) {
                            sumRow += matrix[i][j] * sResultHolder[0][j];
                            resultMatrix[i][j] = 0;
                            ++foundS;
                        }

                        if (Math.abs(matrix[i][j]) > 0.000001) {
                            lOneIdx = j;
                            ++countS;
                        }
                    }

                    resultMatrix[i][hSize-1] -= sumRow;

                    if (countS == 1 || (countS-foundS) == 1) {
                        sResultHolder[1][lOneIdx] = 1;
                        sResultHolder[0][lOneIdx] = rMatrix[i][0];
                    }
                }

                return resultMatrix;
            }

            for (int i = vSize -1; i >= 0; --i) {
                lOneIdx = -1; wIndex = 0; sumRow = 0;

                while (wIndex < hSize) {
                    if (Math.abs(matrix[i][wIndex] - 1) <= 0.0000001 && lOneIdx == -1)
                        lOneIdx = wIndex;

                    ++wIndex;
                }

                if (lOneIdx == -1)
                    continue;

                for (int j = lOneIdx+1; j < hSize-1; ++j) {
                    sumRow += matrix[i][j] * resultMatrix[0][j];
                }

                rowResult = (rMatrix[i][0] - sumRow) / matrix[i][lOneIdx];

                resultMatrix[0][i] = (rowResult == -0) ? 0 : rowResult;
            }

            return resultMatrix;
        }

        private static double[][] solveInverseMatrix(double[][] matrix) {
            int hSize = matrix[0].length;

            double[][] rMatrix = MatrixHelper.cutMatrixColumns(matrix, hSize - 1, hSize- 1);

            double[][] matrixVertical = MatrixHelper.multiplyMatrix(MatrixHelper.cutMatrixColumns(matrix, 0, hSize - 2), rMatrix);
            double[][] matrixResult = new double[1][matrixVertical.length];

            for (int i = 0; i < matrixVertical.length; ++i) {
                matrixResult[0][i] = matrixVertical[i][0];
            }

            return matrixResult;
        }

        private static double[][] copyMatrix(double[][] matrix) {
            int verticalSize = matrix.length, horizontalSize = matrix[0].length;
            double[][] matrixCopyR = new double[verticalSize][horizontalSize];

            for (int i = 0; i < verticalSize; ++i) {
                System.arraycopy(matrix[i], 0, matrixCopyR[i], 0, horizontalSize);
            }

            return matrixCopyR;
        }

        private static double[][] getMinorMatrix(double[][] matrix, int cofactorRow, int cofactorCol) {
            int minorRow, minorCol;
            double[][] minorMatrix = new double[matrix.length-1][matrix[0].length-1];

            minorRow = 0;
            for (int i = 0; i < matrix.length; i++) {
                minorCol = 0;
                if (i != cofactorRow) {
                    for (int j = 0; j < matrix[0].length; j++) {
                        if (j != cofactorCol) {
                            minorMatrix[minorRow][minorCol] = matrix[i][j];
                            minorCol += 1;
                        }
                    }
                    minorRow += 1;
                }
            }
            return minorMatrix;
        }

        private static double getDeterminantByCofactor(double[][] matrix) {
            double determinant;
            int i;

            if (matrix.length == 1) {
                return matrix[0][0];
            }
            else {
                int nZeroRow, nZeroCol, zeroRow, zeroCol, nZeroRowTemp, nZeroColTemp;
                nZeroRow = 0;
                nZeroCol = 0;
                zeroRow = 0;
                zeroCol = 0;
                for (int j = 1; j < matrix.length; j++) {
                    nZeroRowTemp = 0;
                    nZeroColTemp = 0;
                    for (int k = 0; k < matrix.length; k++) {
                        if (matrix[j][k] == 0) {
                            nZeroRowTemp += 1;
                        }
                        if (matrix[k][j] == 0) {
                            nZeroColTemp += 1;
                        }
                    }
                    if (nZeroRow < nZeroRowTemp) {
                        nZeroRow = nZeroRowTemp;
                        zeroRow = j;
                    }
                    if (nZeroCol < nZeroColTemp) {
                        nZeroCol = nZeroColTemp;
                        zeroCol = j;
                    }

                }

                determinant = 0;
                for (i = 0; i < matrix[0].length; i++) {
                    if (nZeroRow >= nZeroCol) {
                        determinant += Math.pow(-1, i+zeroRow) * matrix[zeroRow][i] * getDeterminantByCofactor(getMinorMatrix(matrix, zeroRow, i));
                    }
                    else {
                        determinant += Math.pow(-1, i+zeroCol) * matrix[i][zeroCol] * getDeterminantByCofactor(getMinorMatrix(matrix, i, zeroCol));
                    }
                }
                return determinant;
            }

        }

        private static void swapZeroRow(double[][] matrix, int zeroRow) {
            int i;

            for (i = zeroRow + 1; i < matrix.length; i++) {
                if (matrix[i][zeroRow] != 0) {
                    switchRows(matrix, i, zeroRow);
                    break;
                }
            }
        }

        private static double getDeterminantByRowReduction(double[][] matrix) {
            double[][] newMatrix = copyMatrix(matrix);
            double determinant = 1;

            for (int i = 0; i < newMatrix.length-1; i++) {
                if (newMatrix[i][i] == 0) {
                    swapZeroRow(newMatrix, i);
                    determinant *= -1;
                }

                for (int j = i + 1; j < newMatrix.length; j++) {
                    double multiplier = newMatrix[j][i]/newMatrix[i][i];

                    if (multiplier*newMatrix[i][i] < 0 && newMatrix[j][i] < 0) {
                        multiplier *= -1;
                    } else if (multiplier*newMatrix[i][i] > 0 && newMatrix[j][i] > 0) {
                        multiplier *= -1;
                    }

                    rowAdditionRow(newMatrix, j, i, multiplier);
                }
            }
            for (int k = 0; k < newMatrix.length; k++) {
                determinant *= newMatrix[k][k];
            }
            if (determinant == -0) {
                return 0;
            }
            else {
                return determinant;
            }
        }

        private static double[][] changeColumn(double[][] mainMatrix, double[][] rightSideMatrix, int colTarget) {
            int i;

            for (i = 0; i < mainMatrix.length; i++) {
                mainMatrix[i][colTarget] = rightSideMatrix[i][0];
            }
            return mainMatrix;
        }
        private static double[][] solveCrammerMatrix(double[][] mainMatrix, double[][] rightSideMatrix) {
            double[][] resultMatrix = new double[1][mainMatrix.length];

            for (int i = 0; i < mainMatrix.length; i++) {
                double[][] copyMainMatrix = copyMatrix(mainMatrix);
                double[][] tempMatrix = changeColumn(copyMainMatrix, rightSideMatrix, i);
                double divResult = getDeterminantByRowReduction(tempMatrix)/getDeterminantByRowReduction(mainMatrix);
                resultMatrix[0][i] = divResult;
            }


            return resultMatrix;
        }
        private static double[][] getCrammerMatrix(double[][] matrix) {
            double[][] mainMatrix = MatrixHelper.cutMatrixColumns(matrix, 0, matrix[0].length-2);
            double[][] rightSideMatrix = MatrixHelper.cutMatrixColumns(matrix,
                    matrix[0].length-1, matrix[0].length-1);

            return MatrixHelper.solveCrammerMatrix(mainMatrix, rightSideMatrix);
        }

        private static double[][] transposeMatrix(double[][] matrix) {
            double[][] resultMatrix = new double[matrix[0].length][matrix.length];

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    resultMatrix[j][i] = matrix[i][j];
                }
            }
            return resultMatrix;
        }
        private static double[][] getInverseMatrixByAdjoint(double[][] matrix) {
            double[][] newMatrix = copyMatrix(matrix);
            double[][] cofactorMatrix = new double[newMatrix.length][newMatrix[0].length];


            for (int i = 0; i < newMatrix.length; i++) {
                for (int j = 0; j < newMatrix[0].length; j++) {
                    cofactorMatrix[i][j] = (1/getDeterminantByRowReduction(matrix)) * Math.pow(-1, i+j) *
                            getDeterminantByRowReduction(getMinorMatrix(newMatrix, i, j));
                }

            }
            double[][] inverseMatrix = transposeMatrix(cofactorMatrix);

            return inverseMatrix;
        }

    }

    /**
     *
     */
    // TODO: add try catch for type parsing (parseInt and parseDouble) to avoid killing program for wrong input
    public void loadMatrixLine() {
        Scanner scanner = new Scanner(System.in);
        String line;
        String[] elementArr;

        out.println("Input matrix : ");
        while (true) {
            line = scanner.nextLine();
            elementArr = line.split(" ");

            if (elementArr.length > 2)
                out.println("wrong format");

            this.verticalSize = Integer.parseInt(elementArr[0]);
            this.horizontalSize = Integer.parseInt(elementArr[1]);  // handle this
            break;
        }

        double[][] matrix = new double[verticalSize][horizontalSize];

        for (int i = 0; i < verticalSize; ++i) {
            line = scanner.nextLine();
            elementArr = line.split(" ");

            // System.out.printf("%d, %d<\n", elementArr.length, horizontalSize);

            if (elementArr.length != horizontalSize) {
                out.println("Wrong format");
                i -= 1;
                continue;
            }

            int hIdx = 0;

            for (String str: elementArr) {
                matrix[i][hIdx] = Double.parseDouble(str);
                ++hIdx;
            }
        }

        setMatrix(matrix);
    }

    /**
     * load matrix from a file in txt format, element in the same row is delimited by a space and rows are delimited by a new line
     *
     * @param   absFilePath an absolute path for the matrix
     */
    public void loadMatrix(String absFilePath) throws IOException, UnsupportedOperationException, NumberFormatException {
        Path path = Paths.get(absFilePath);

        List<String> matrixLines = Files.readAllLines(path);

        int hSize = matrixLines.get(0).split(" ").length, vSize = matrixLines.size(), i = 0, j =0;
        double[][] matrix = new double[vSize][hSize];

        for (String line: matrixLines) {
            String[] lineS = line.split(" ");

            if (lineS.length != hSize)
                throw new UnsupportedOperationException("Matrix in file is not uniform in size.");

            for (String el: lineS) {
                matrix[i][j] = Integer.parseInt(el);
                ++j;
            }

            ++i;
            j = 0;
        }

        setMatrix(matrix);
    }

    /**
     * @param   matrix1 left operand (A) in AB format
     * @param   matrix2 right operand (B) in AB format
     * @return  new Matrix object, result of the multiplication
     */
    public static Matrix getProductMatrix(Matrix matrix1, Matrix matrix2) throws UnsupportedOperationException {
        return new Matrix(MatrixHelper.multiplyMatrix(matrix1.getMatrix(), matrix2.getMatrix()));
    }

    // TODO: add java doc
    /**
     *
     * @return
     */
    public double[][] solveMatrixCramer() {
        if (getVerticalSize() != getHorizontalSize()-1) {
            throw new UnsupportedOperationException("Wrong matrix dimension");
        }
        return MatrixHelper.getCrammerMatrix(getMatrix());
    }

    // TODO: add java doc
    /**
     *
     * @return
     */
    public double solveDeterminantByCofactor() {
        if (getVerticalSize() != getHorizontalSize()) {
            throw new UnsupportedOperationException("Matrix need to be square");
        }
        return MatrixHelper.getDeterminantByCofactor(getMatrix());
    }

    // TODO: add java doc
    /**
     *
     * @return
     */
    public double solveDeterminantByRowReduction() {
        if (getVerticalSize() != getHorizontalSize()) {
            throw new UnsupportedOperationException("Matrix need to be square");
        }
        return MatrixHelper.getDeterminantByRowReduction(getMatrix());
    }

    public double[][] solveInverseByAdjoin() {
        if (getVerticalSize() != getHorizontalSize()) {
            throw new UnsupportedOperationException("Matrix need to be square");
        }
        return MatrixHelper.getInverseMatrixByAdjoint(getMatrix());
    }

    /**
     *
     * throws UnsupportedOperationException if matrix is not in a solvable form
     *
     * @return  2D double array of solutions, with 0 row if matrix has no solution, 1 row if matrix has unique solution,
     *          x > 1 if matrix has non-unique solution (parametrics)
     */
    public double[][] solve() throws UnsupportedOperationException {
        if (matrixType != 0 && matrixType != 1) {
            String errMessage = matrixType == -1 ? "Matrix is not in a solvable form" : "Unknown matrix type";
            throw new UnsupportedOperationException(errMessage);
        }

        double[][] copyMatrix = MatrixHelper.copyMatrix(getMatrix());

        if (matrixType == 0) {
            return MatrixHelper.solveEchelonFormMatrix(copyMatrix);
        }

        /* matrixType == 1 */
        return MatrixHelper.solveInverseMatrix(copyMatrix);
    }

    /**
     * @return  Matrix object with matrix in echelon form
     */
    public Matrix getEchelonMatrix() throws UnsupportedOperationException{
        return Matrix.builder()
                .matrix(MatrixHelper.getEchelonMatrix(MatrixHelper.copyMatrix(getMatrix())))
                .verticalSize(verticalSize)
                .horizontalSize(horizontalSize)
                .matrixType(0)
                .build();
    }

    /**
     * @return  Matrix object with matrix in reduced echelon form
     */
    public Matrix getReducedEchelonMatrix() throws UnsupportedOperationException {
        return Matrix.builder()
                .matrix(MatrixHelper.getReducedEchelonMatrix(MatrixHelper.copyMatrix(getMatrix()), horizontalSize))
                .verticalSize(verticalSize)
                .horizontalSize(horizontalSize)
                .matrixType(0)
                .build();
    }

    /**
     * @return  Matrix object with the matrix state inversed
     */
    public Matrix getInverseMatrix() throws UnsupportedOperationException {
        double[][] matrixR = new double[1][1];

        if (horizontalSize != verticalSize && horizontalSize != verticalSize+1)
            throw new UnsupportedOperationException("Matrix could not be inversed");

        if (horizontalSize == verticalSize)
            matrixR = MatrixHelper.getInverseMatrix(MatrixHelper.copyMatrix(getMatrix()));

        if (horizontalSize == verticalSize + 1)
            matrixR = MatrixHelper.getInverseMatrixSpl(MatrixHelper.copyMatrix(getMatrix()));

        return Matrix.builder()
                .matrix(matrixR)
                .verticalSize(verticalSize)
                .horizontalSize(horizontalSize)
                .matrixType(1)
                .build();
    }

    /**
     * copy Matrix object
     *
     * @return  Matrix object with the same states
     */
    public Matrix copyMatrix() {
        return Matrix.builder()
                .matrix(MatrixHelper.copyMatrix(getMatrix()))
                .horizontalSize(horizontalSize)
                .verticalSize(verticalSize)
                .matrixType(matrixType)
                .build();
    }

    /**
     * copy matrix and merge the parameter matrix
     *
     * @param   mergingAgent matrix to be merged with the old matrix
     * @return  Matrix object with the matrix state merged
     */
    public Matrix mergeAndCopyMatrix(double[][] mergingAgent) throws UnsupportedOperationException{
        double[][] newMatrix = MatrixHelper.mergeMatrices(MatrixHelper.copyMatrix(getMatrix()), mergingAgent);

        return Matrix.builder()
                .matrix(newMatrix)
                .horizontalSize(newMatrix[0].length)
                .verticalSize(newMatrix.length)
                .matrixType(-1)
                .build();
    }

    /**
     *
     * @param   folderPath path to a folder
     * @param   fileName file name
     * @param   matrix  matrix to be saved
     */
    public static void saveMatrixToFile(String folderPath, String fileName, double[][] matrix) throws IOException {
        Path fiPath = Path.of(folderPath, fileName+".txt");

        if (!Files.exists(fiPath))
            Files.createFile(fiPath);

        try(BufferedWriter writer = Files.newBufferedWriter(fiPath, StandardCharsets.UTF_8)){
            StringBuilder lineString = new StringBuilder();
            for (double[] line: matrix) {
                lineString.delete(0, lineString.length());
                for (double el: line) {
                    lineString.append(String.format("%.2f", el));
                    lineString.append(" ");
                }
                lineString.append("\n");
                writer.write(lineString.toString());
            }
        }catch(IOException ex){
            throw ex;
        }
    }

}
