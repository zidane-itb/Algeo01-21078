import java.util.Scanner;

import static java.lang.System.*;

public class RegresiLinearBerganda {
    private static void printMatrix(double[][] matrix) {
        out.println();
        int horizontalSize = matrix[0].length;
        for (double[] doubles : matrix) {
            for (int j = 0; j < horizontalSize; ++j) {
                out.printf("%.2f ", doubles[j]);
            }
            out.println("");
        }
        out.println();
    }

    private double[][] loadRegressionData() {
        Scanner scanner = new Scanner(System.in);
        String nVariabel, nData, line;
        String[] elementArr;
        // Note : Apus scanner1,2, deklarasi variabel kalo banyak jadiin inline

        int horizontalSize = 0;
        int verticalSize = 0;
        while (true) {
            out.print("Jumlah peubah x : ");
            nVariabel = scanner.nextLine();
            out.print("Jumlah sampel : ");
            nData = scanner.nextLine();


            try {
                verticalSize = Integer.parseInt(nData);
                horizontalSize = Integer.parseInt(nVariabel)+1;
                if (horizontalSize > 2 && verticalSize > horizontalSize - 2) {
                    break;
                }
                else {
                    out.println("wrong variabel and sample number");
                }
            }
            catch (NumberFormatException exception) {
                out.println("wrong format");
            }
        }

        out.print("Input sample (" + horizontalSize + "x" + verticalSize + ") : \n" );
        double[][] matrix = new double[verticalSize][horizontalSize];

        int i = 0;
        while (i < verticalSize) {
            line = scanner.nextLine();
            elementArr = line.split(" ");

            if (elementArr.length != horizontalSize) {
                out.println("Wrong format");
                continue;
            }

            int hIdx = 0;
            try {
                for (String str : elementArr) {
                    matrix[i][hIdx] = Double.parseDouble(str);
                    ++hIdx;
                }
                i += 1;
            } catch (NumberFormatException exception) {
                out.println("wrong format");
            }
        }
        return matrix;
    }

    private static double[][] mergeMatrices(double[][] matrix, double[][] mergingAgent) {
        if (matrix.length != mergingAgent.length)
            throw new UnsupportedOperationException("Matrices have different number of rows");

        int matrixVSize = matrix.length, matrixHSize = matrix[0].length;
        double[][] mergedMatrix = new double[matrixVSize][matrixHSize + mergingAgent.length];

        for (int i = 0; i < matrixVSize; ++i) {
            for (int j = 0; j < matrixHSize; ++j) {
                mergedMatrix[i][j] = matrix[i][j];
            }

            for (int j = 0; j < mergingAgent.length; ++j) {
                mergedMatrix[i][j + matrixHSize] = mergingAgent[i][j];
            }
        }

        return mergedMatrix;
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

    private static Matrix oneMatrix(int row) {
        double[][] oneMatrix = new double[row][1];
        for (int i = 0; i < row; i++) {
            oneMatrix[i][0] = 1;
        }
        Matrix oneInMatrix = new Matrix(oneMatrix);
        return oneInMatrix;
    }
    private static double[][] createMultiLinearRegressionMatrix(double[][] matrix) {
        Matrix firstRow = oneMatrix(matrix.length);

        double[][] fullMatrix = firstRow.mergeAndCopyMatrix(matrix).getMatrix();
        double[][] transposeFullMatrix = transposeMatrix(fullMatrix);
        double[][] productFullMatrix = new double[transposeFullMatrix.length-1][fullMatrix[0].length];

        for (int i = 0; i < transposeFullMatrix.length-1; i++) {
            for (int j = 0; j < fullMatrix[0].length; j++) {
                for (int k = 0; k < transposeFullMatrix[0].length; k++) {
                    productFullMatrix[i][j] += transposeFullMatrix[i][k] * fullMatrix[k][j];
                }
            }
        }
        return productFullMatrix;
    }

    private static int checkEchelonMatrix(double[][] matrix) {
        int type = 0;
        boolean rowZero, lastZero;

        rowZero = true;
        for (int i = 0; i < matrix[0].length-1; i++) {
            if (matrix[matrix.length-1][i] != 0) {
                rowZero = false;
                break;
            }
        }
        lastZero = true;
        if (matrix[matrix.length-1][matrix[0].length-1] != 0) {
            lastZero = false;
        }

        if (!rowZero) {
            if (matrix.length < matrix[0].length-1) {
                type = 2;
            }
            else {
                type = 1;
            }
        }
        else {
            if (lastZero) {
                type = 2;
            }
            else {
                type = 3;
            }
        }
        return type;
    }

    private static void createRegressionFunction(double[][] matrix) {
        out.print("f(");
        for (int i = 1; i < matrix.length; i++) {
            if (i != matrix.length - 1) {
                out.printf("x" + i + ",");
            } else out.print("x" + i + ") = ");
        }

        boolean first = true;
        if (Math.abs(matrix[0][matrix[0].length-1]) > 0.000000000001) {
            System.out.printf("%.2f ", matrix[0][matrix[0].length - 1]);
            first = false;
        }
        for (int j = 1; j < matrix.length; j++) {
            while (matrix[j][matrix[0].length-1] == 0 && j < matrix.length-1) {
                j += 1;
            }
            if (matrix[j][matrix[0].length-1] != 0) {
                if (first) {
                    System.out.printf("%.2f x%d ", matrix[j][matrix[0].length-1], j + 1);
                    first = false;
                } else if (!first && j < matrix[0].length) {
                    System.out.printf("+ %.2f x%d ", matrix[j][matrix[0].length-1], j + 1);
                }
            }
            else continue;

        }
    }

    private static void solveRegressionFunction(double[][] regression) {
        double[][] variabel = new double[1][regression.length];

        for (int i = 1; i < regression.length; i++) {
            Scanner var = new Scanner(in);
            out.print("x" + i +" = ");
            variabel[0][i] = var.nextDouble();
        }

        out.print("f(");
        for (int i = 1; i < variabel[0].length; i++) {
            if (i != variabel[0].length - 1) {
                out.printf(variabel[0][i] + ",");
            } else out.print(variabel[0][i] + ") = ");
        }

        double result = regression[0][regression[0].length-1];

        for (int i = 1; i < regression.length; i++) {
            result += regression[i][regression[0].length-1] * variabel[0][i];
        }
        out.printf("%.2f\n", result);
    }

    public void solve() {
        double[][] dataMatrix = loadRegressionData();

        double[][] regressionMatrix = createMultiLinearRegressionMatrix(dataMatrix);
        Matrix varMatrix = new Matrix(regressionMatrix);

        double[][] varEchelonMatrix = varMatrix.getReducedEchelonMatrix().getMatrix();
        out.print("\n");

        if (checkEchelonMatrix(varEchelonMatrix) == 1) {
            createRegressionFunction(varEchelonMatrix);
            out.print("\n");
            solveRegressionFunction(varEchelonMatrix);
        }
        else if (checkEchelonMatrix(varEchelonMatrix) == 2) {
            throw new UnsupportedOperationException("Parametric/infinite solution matrix, thus no regression function");
        }
        else if (checkEchelonMatrix(varEchelonMatrix) == 3) {
            throw new UnsupportedOperationException("Matrix has no solution, thus no regression function");
        }
    }

}
