
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


/**
 * This class is used to give solution for a given polynom interpolation problem
 *
 * @author Christian Albert Hasiholan
 * @author Fakih Anugerah Pratama
 * @author Zidane Firzatullah
 */
@NoArgsConstructor
public class PolynomInterpolationSolver {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private Matrix solutions;

    @Setter(AccessLevel.PRIVATE) @Getter(AccessLevel.PRIVATE)
    private int polynomDegree;
    
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private Point[] points;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private double fValueToCalculate;

    private static class SolverHelper {

        private static double[][] getXMatrix(int polynomDegree, Point[] points){
            double[][] piSolution = new double[polynomDegree+1][polynomDegree+1];
    
            for(int i = 0; i < polynomDegree + 1; i++){
                for (int j = 0; j < polynomDegree + 1; j++){
                    piSolution[i][j] = Math.pow(points[i].getX(), j);
                }
            }
    
            return piSolution;
        }
    
        private static double[][] getYMatrix(int polynomDegree, Point[] points){
            double[][] piSolution = new double[polynomDegree+1][1];
    
            for(int i = 0; i < polynomDegree + 1; i++){
                piSolution[i][0] = points[i].getY();
            }
    
            return piSolution;
        }

        private static String getLinearEquation(int polynomDegree, Matrix solutions){
            String linEq = "p" + polynomDegree + "(x) = ";
    
            for(int i = 0; i <= polynomDegree; i++){
                if(i > 0){
                    if(solutions.getMatrix()[i][0] >= 0) linEq += " + ";
                    else linEq += " - ";
                }
    
                new String();
                linEq += String.format("%.4f", Math.abs(solutions.getMatrix()[i][0]));
    
                if(i > 0) linEq += "x";
                if(i > 1) linEq += "^" + i;     
            }
    
            return linEq;
        }

        private static double f(double x, Matrix solutions){
            double result = 0;
    
            for (int i = 0; i < solutions.getMatrix().length; i++){
                result += solutions.getMatrix()[i][0] * Math.pow(x, i);
            }
    
            return result;
        }
    }

    public void loadVariables(){
        Scanner scanner = new Scanner(System.in);

        // System.out.printf("deg : ");
        int pd = scanner.nextInt();

        Point[] pnts = new Point[pd+1];
        
        for (int i = 0; i <= pd; i++){
            Double x = scanner.nextDouble(), y = scanner.nextDouble();
            pnts[i] = new Point(x, y);
        }

        // System.out.printf("x value : ");
        double fval = scanner.nextDouble();

        setPolynomDegree(pd);
        setPoints(pnts);
        setFValueToCalculate(fval);
    }

    /**
     * load matrix from a file in txt format, element in the same row is delimited by a space and rows are delimited by a new line
     *
     * @param   absFilePath an absolute path for the matrix
     */
    public void loadVariables(String absFilePath) throws IOException, UnsupportedOperationException, NumberFormatException {
        Path path = Paths.get(absFilePath);

        List<String> matrixLines = Files.readAllLines(path);

        int hSize = 2, vSize = matrixLines.size(), i = 0, j =0; //hSize will always be 2.
        // double[][] matrix = new double[vSize][hSize];
        Point points[] = new Point[vSize];

        double x, y;

        for (String line: matrixLines) {
            String[] lineS = line.split(" ");

            x = Double.parseDouble(lineS[0]); y = Double.parseDouble(lineS[1]);

            points[i] = new Point(x, y);
            ++i;
        }

        Scanner s = new Scanner(System.in);
        System.out.printf("x = ");
        double fVal =  s.nextDouble();

        setPolynomDegree(vSize-1);
        setPoints(points);
        setFValueToCalculate(fVal);
    }
    


    private Matrix solveSolutions(){
        Matrix xMatrix = new Matrix(SolverHelper.getXMatrix(getPolynomDegree(), getPoints()));
        Matrix yMatrix = new Matrix(SolverHelper.getYMatrix(getPolynomDegree(), getPoints()));

        return Matrix.getProductMatrix(xMatrix.getInverseMatrix(), yMatrix);
    }

    /**
     * a procedure for solving a polynom interpolation problem. <br/><br/>
     * this program will ask for inputs, solve for solutions, and display the solutions
     * within a single invoke
     * 
     * @see <code>BicubicInterpolationSolver<code/>
     *
     */
    public void solve(){
        // loadVariables();
        setSolutions(solveSolutions());

        // System.out.println("| matrix of a constants |");

        // System.out.println("| linEq! |");
        System.out.println(SolverHelper.getLinearEquation(getPolynomDegree(), getSolutions()));

        System.out.printf("f(" + fValueToCalculate + ") = " + String.format("%.4f\n\n\n", SolverHelper.f(getFValueToCalculate(), getSolutions())));
    }   
}
