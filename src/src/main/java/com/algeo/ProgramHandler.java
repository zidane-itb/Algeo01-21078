package com.algeo;

import com.algeo.lib.Matrix;

import java.util.Scanner;

import static java.lang.System.out;

import java.io.IOException;

public class ProgramHandler {

    ////////////////////// func to delte later!////////////////////////
    private static void onp(String txt){
        System.out.printf("\n\n\n\n~~~~~~~~~~~onprogresss!!!!!~~~~~~~~~\n\n\n");
        System.out.println(txt+"\n\n\n");
        pause();
    }

    private static void pause(){
        Scanner s = new Scanner(System.in);
        try{
            System.out.printf("||||||||||||pause|||||||||");
            s.nextLine(); //so empty string can trigger it
        } finally{}
    }
    ////////////////////// func to delte later!////////////////////////

    private static int readMode(){
        final String toDisplay =
                "Baca nilai menggunakan :\n" +
                        "1. Masukan input\n" +
                        "2. Baca file\n";

        System.out.println(toDisplay);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static void saveAsFile(double[][] matrixToSave){
        while(true){
            out.printf("Mau save matriks ini ke file? (y/n) ");
            Scanner s = new Scanner(System.in);
            String ans = s.nextLine();
            
            if (ans.equals("y") || ans.equals("Y")){

                String path, filename;
                out.println("Masukkan path folder penyimpanan file : ");
                path = s.nextLine();
                out.println("Masukkan nama file : ");
                filename = s.nextLine();

                try {
                    Matrix.saveMatrixToFile(path, filename, matrixToSave);  
                    
                    break;
                } catch(IOException e) {
                    out.printf("Masukan path salah!");
                }
            } else
            if (ans.equals("n") || ans.equals("N")){
                break;
            } 
        }
    }

    private static Matrix readMatrix(Matrix matrix){

        while(true){
            try{
                Scanner s = new Scanner(System.in);
                matrix.loadMatrix(s.nextLine());

                break;
            }
            catch (IOException e){
                out.println("Masukan nama file salah!");
            }
        }
        
        return matrix;
    }


    private static int mainMenu() {
        // handle non 1-2-3-or-4567 inputs!

        final String toDisplay =
                "Menu\n"+
                        "1. Sistem Persamaan Linier\n"+
                        "2. Determinan\n"+
                        "3. Matriks balikan\n"+
                        "4. Interpolasi Polinom\n"+
                        "5. Interpolasi Bicubic\n"+
                        "6. Regresi linier berganda\n"+
                        "7. Perbesaran Citra dengan Interpolasi Bicubic\n"+
                        "\n"+
                        "8. Keluar\n";

        System.out.println(toDisplay);
        System.out.println("Input pilihan menu :");
        Scanner scanner = new Scanner(System.in);
        String inputUser;

        int menu = 0;
        while (true) {
            inputUser = scanner.nextLine();
            try {
                menu = Integer.parseInt(inputUser);
                if (menu > 0 && menu < 9) {
                    out.println("\n");
                    break;
                }
                else System.out.println("Tidak ada menu tersebut. Input lagi pilihan menu!");
            }
            catch (NumberFormatException exception) {
                out.println("Tidak ada menu tersebut. Input lagi pilihan menu!");
            }
        }
        return menu;
    }

    private static int mainMenuSatu() {
        // handle non 1-2-3-or-4 inputs!

        final String toDisplay =
                "Sistem Persamaan Linier\n"+
                        "1. Metode eliminasi Gauss\n"+
                        "2. Metode eliminasi Gauss-Jordan\n"+
                        "3. Metode matriks balikan\n"+
                        "4. Kaidah Cramer\n";

        System.out.println(toDisplay);
        System.out.println("Input pilihan submenu :");
        Scanner scanner = new Scanner(System.in);
        String inputUser;

        int subMenu = 0;
        while (true) {
            inputUser = scanner.nextLine();
            try {
                subMenu = Integer.parseInt(inputUser);
                if (subMenu > 0 && subMenu < 5) {
                    out.println("\n");
                    break;
                }
                else System.out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
            catch (NumberFormatException exception) {
                out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
        }
        return subMenu;
    }

    private static int mainMenuDua() {
        // handle non 1-2 inputs!

        final String toDisplay =
                "Determinan\n"+
                        "1. Reduksi baris\n"+
                        "2. Ekspansi kofaktor\n";

        System.out.println(toDisplay);
        System.out.println("Input pilihan submenu :");
        Scanner scanner = new Scanner(System.in);
        String inputUser;

        int subMenu = 0;
        while (true) {
            inputUser = scanner.nextLine();
            try {
                subMenu = Integer.parseInt(inputUser);
                if (subMenu > 0 && subMenu < 3) {
                    out.println("\n");
                    break;
                }
                else System.out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
            catch (NumberFormatException exception) {
                out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
        }
        return subMenu;
    }

    private static int mainMenuTiga() {
        // handle non 1-2 inputs!

        final String toDisplay =
                "Inverse matrix\n"+
                        "1. Identity with row reduction\n"+
                        "2. Adjoint and determinant\n";

        System.out.println(toDisplay);
        System.out.println("Input pilihan submenu :");
        Scanner scanner = new Scanner(System.in);
        String inputUser;

        int subMenu = 0;
        while (true) {
            inputUser = scanner.nextLine();
            try {
                subMenu = Integer.parseInt(inputUser);
                if (subMenu > 0 && subMenu < 3) {
                    out.println("\n");
                    break;
                }
                else System.out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
            catch (NumberFormatException exception) {
                out.println("Tidak ada submenu tersebut. Input lagi pilihan submenu!");
            }
        }
        return subMenu;
    }

    //todo
    //rename symbolnya!
    public static void start() throws NumberFormatException, UnsupportedOperationException, IOException {
        while (true) {
            int selectedMenu = mainMenu();

            if (selectedMenu == 1) {
                int selectedSubMenu = mainMenuSatu();
                if (selectedSubMenu == 1) {
                    /*onp("Metode eleminasi Gauss");*/
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }


                            double[][] testMatrix = matrix.getEchelonMatrix().getMatrix();
                            if (checkMatrix(testMatrix) == 1) {
                                double[][] matrixR = matrix.getEchelonMatrix().solve();
                                printMatrix(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 2) {
                                double[][] matrixR = matrix.getEchelonMatrix().solve();
                                printEqLeft(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 3) {
                                out.print("This matrix has no solution\n");
                            }
                            break;
                        } catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                } else
                if (selectedSubMenu == 2){
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            double[][] testMatrix = matrix.getReducedEchelonMatrix().getMatrix();
                            if (checkMatrix(testMatrix) == 1) {
                                double[][] matrixR = matrix.getReducedEchelonMatrix().solve();
                                printMatrix(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 2) {
                                double[][] matrixR = matrix.getReducedEchelonMatrix().solve();
                                printEqLeft(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 3) {
                                out.print("This matrix has no solution\n");
                            }
                            break;
                        } catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                } else
                if (selectedSubMenu == 3){
                    while(true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            double[][] testMatrix = matrix.getInverseMatrix().getMatrix();
                            if (checkMatrix(testMatrix) == 1) {
                                double[][] matrixR = matrix.getInverseMatrix().solve();
                                printMatrix(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 2) {
                                double[][] matrixR = matrix.getInverseMatrix().solve();
                                printEqLeft(matrixR);
                                out.print("\n");

                                saveAsFile(matrixR);
                            } else if (checkMatrix(testMatrix) == 3) {
                                out.print("This matrix has no solution\n");
                            }
                            break;
                        } catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                } else
                if (selectedSubMenu == 4) {
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            double[][] matrixR = matrix.solveMatrixCramer();

                            printMatrix(matrixR);
                            out.print("\n");

                            saveAsFile(matrixR);
                            break;
                        }
                        catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                }
            } else
            if (selectedMenu == 2) {
                int selectedSubMenu = mainMenuDua();
                if (selectedSubMenu == 1) {
                    while (true) {
                        try {
                            Matrix matrix = new Matrix(loadMatrix());

                            double determinantResult = matrix.solveDeterminantByRowReduction();

                            out.printf("Determinan = %.2f \n", determinantResult);
                            break;
                        }
                        catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                }
                else if (selectedSubMenu == 2) {
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            double determinantResult = matrix.solveDeterminantByCofactor();

                            out.printf("Determinan = %.2f \n", determinantResult);

                            break;
                        }
                        catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                }
            } else
            if (selectedMenu == 3) {
                int selectedSubMenu = mainMenuTiga();
                if (selectedSubMenu == 1) {
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();

                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            out.print("Inverse result :");
                            printMatrixTest(matrix.getInverseMatrix().getMatrix());
                            
                            saveAsFile(matrix.getInverseMatrix().getMatrix());
                            break;
                        } catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                }
                else if (selectedSubMenu == 2) {
                    while (true) {
                        try {
                            int readMode = readMode();
                            Matrix matrix = new Matrix();
                            
                            if(readMode == 1) matrix = new Matrix(loadMatrix());
                            else if(readMode == 2) {
                                matrix = readMatrix(matrix);
                            }

                            out.print("Inverse result :");
                            printMatrixTest(matrix.solveInverseByAdjoin());

                            saveAsFile(matrix.solveInverseByAdjoin());
                            break;
                        } catch (UnsupportedOperationException exception) {
                            out.print("Try again with different matriks!\n");
                        }
                    }
                }
            } else
            if (selectedMenu == 4) {
                out.print("Interpolasi Polinom\n");
                while (true) {
                    try {
                        PolynomInterpolationSolver pi = new PolynomInterpolationSolver();
                        int readMode = readMode();

                        if ( readMode == 1 ){
                            pi.loadVariables();
                        } else
                        if ( readMode == 2 ){
                            Scanner s = new Scanner(System.in);
                            pi.loadVariables(s.nextLine());

                        } else {
                            onp("handle bad input! in interpolasi polinom");
                        }

                        pi.solve();
                        break;
                    }
                    catch (UnsupportedOperationException exception) {
                        out.print("Try again with different matriks!\n");
                    }
                }

            } else
            if (selectedMenu == 5) {
                out.print("Interpolasi Bikubik\n");
                while (true) {
                    try {
                        BicubicInterpolationSolver bi = new BicubicInterpolationSolver();
                        int readMode = readMode();

                        if ( readMode == 1 ){
                            bi.loadVariables();
                            bi.solve();
                        } else
                        if ( readMode == 2 ){
                            Scanner s = new Scanner(System.in);
                            bi.loadVariables(s.nextLine());

                        } else {
                            onp("handle bad input! in interpolasi bikubik");
                        }

                        break;
                    }
                    catch (UnsupportedOperationException exception) {
                        out.print("Try again with different matriks!\n");
                    }
                }
            } else
            if (selectedMenu == 6) {
                out.print("Regresi Linier Berganda\n");
                while (true) {
                    try {
                        RegresiLinearBerganda regression = new RegresiLinearBerganda();
                        regression.solve();
                        break;
                    }
                    catch (UnsupportedOperationException exception) {
                        out.print("Try again with different matriks!\n");
                    }
                }
            } else
            if (selectedMenu == 7) {
                out.print("Perbesaran Citra dengan Interpolasi Bicubic\n");
                while (true) {
                    try {
                        ImageInterpolationSolver ii = new ImageInterpolationSolver();
                        Image targetImage = new Image();
                        Scanner s = new Scanner(System.in);

                        out.print("Image Path : public\\images\\source\\"); //TODO change path
                        String targetPath = s.nextLine();

                        targetImage.readImage(targetPath);

                        ii.setImage(targetImage);
                        ii.solve();

                        out.print("Image Path : public\\images\\"); //TODO change path
                        targetPath = s.nextLine();
                        Image.saveAsFile(targetPath, targetImage.getBufferedImage());
                        break;
                    }
                    catch (UnsupportedOperationException e) {
                        out.print("Try again with different filepath!\n");
                    }


                }
            } else
            if (selectedMenu == 8) {
                break;
            }
        }
    }

    private static double[][] loadMatrix() {
        int verticalSize, horizontalSize;
        Scanner scanner = new Scanner(System.in);
        String line;
        String[] elementArr;

        out.println("Input matrix : ");
        while (true) {
            line = scanner.nextLine();
            elementArr = line.split(" ");

            try {
                if (elementArr.length <= 2) {
                    verticalSize = Integer.parseInt(elementArr[0]);
                    horizontalSize = Integer.parseInt(elementArr[1]);  // handle this
                    break;
                }
                else {
                    out.println("wrong format");
                }
            }
            catch (NumberFormatException | ArrayIndexOutOfBoundsException exception) {
                out.println("wrong format");
            }

        }

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
            // System.out.printf("%d, %d<\n", elementArr.length, horizontalSize);

        }
        return matrix;
    }
    private static int checkMatrix(double[][] matrix) {
        int type = 0, countOne;
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

    private static void createParametrixSolution(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            boolean first = true, needZero = false;
            for (int j = 0; j < matrix[0].length; j++) {
                while (matrix[i][j] == 0 && j < matrix[0].length-1) {
                    j += 1;
                }
                if (matrix[i][j] != 0) {
                    needZero = true;
                    if (first) {
                        System.out.printf("%.2f x%d ", matrix[i][j], j + 1);
                        first = false;
                    } else if (!first && j < matrix[0].length - 1) {
                        System.out.printf("+ %.2f x%d ", matrix[i][j], j + 1);
                    } else {
                        System.out.printf("= %.2f ", matrix[i][j]);
                        needZero = false;
                    }
                }
                else continue;

            }
            if (needZero) {
                System.out.printf("= 0.00 ");
            }
            out.print("\n");
        }
    }

    private static void printMatrix(double[][] matrix) {
        out.println();
        int horizontalSize = matrix[0].length;

        for (double[] doubles : matrix) {
            for (int j = 0; j < horizontalSize; ++j) {
                if (doubles[j] == 0) {
                    continue;
                }
                out.printf("x%d = %.2f \n", j + 1, doubles[j]);
            }
        }
        out.println();
    }

    private static void printMatrixTest(double[][] matrix) {
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
    private static void printEqLeft(double[][] matrix) {
        boolean foundInLine = true;
        int hSize = matrix[0].length, vSize = matrix.length, k;
        double el;

        for (int i = 0; i < vSize; ++i) {
            foundInLine = false;

            for (k = 0; k < hSize; ++k) {
                el = matrix[i][k];
                if (Math.abs(el) <= 0.000001) continue;

                out.printf("x%d = ", k+1);
                foundInLine =true;
                break;
            }

            if (!foundInLine)
                break;

            foundInLine = false;

            for (int j = hSize - 1; j > k; --j) {
                el = matrix[i][j];
                if (Math.abs(el) <= 0.000001) continue;

                if (j == hSize -1) {
                    out.printf("%.2f ", el);
                    foundInLine = true;
                    continue;
                }

                if (el > 0) {
                    if (foundInLine)
                        out.print("- ");

                    out.printf("%.2f x%d ", el, j+1);
                    foundInLine = true;
                    continue;
                }

                if (foundInLine)
                    out.print("+ ");

                out.printf("%.2f x%d ", el * -1, j+1);
                foundInLine = true;

            }

            if (!foundInLine) {
                out.print("0.00");
            }
            out.println();
        }

    }

}
