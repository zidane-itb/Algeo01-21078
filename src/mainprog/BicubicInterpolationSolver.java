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
 * This class is used to give solution for a given bicubic interpolation problem
 *
 * @author Christian Albert Hasiholan
 * @author Fakih Anugerah Pratama
 * @author Zidane Firzatullah
 */
public class BicubicInterpolationSolver {

    @Setter(AccessLevel.PRIVATE) @Getter(AccessLevel.PRIVATE)
    Matrix fValue;

    @Setter(AccessLevel.PRIVATE) @Getter(AccessLevel.PRIVATE)
    Matrix aCoeffMatrix;

    private Matrix getXMatrix(){
        double[][] res = new double[16][16];

        int idxX, idxY = 0;

        for(int y = -1; y <= 2; y++){
            for(int x = -1; x <= 2; x++){
                idxX = 0;

                for(int j = 0; j <= 3; j++){ // j
                    for(int i = 0; i <= 3; i++){ // i of a

                        res[idxY][idxX] = Math.pow(x, i) * Math.pow(y, j);
                        
                        idxX++;
                    }
                }

                idxY++;
            }
        }

        return new Matrix(res);
    }

    private void loadFValue(){
        Scanner scanner = new Scanner(System.in);
        double[][] fVal = new double[16][1];

        for(int i = 0; i < 16; i++){
            fVal[i][0] = scanner.nextDouble();
        }

        setFValue(new Matrix(fVal));
    }

    /**
     * a procedure to load for the values needed by the solver
     * 
     * @param fVal a matrix of f values
     * @throws IOException
     * @see <code>void loadVariables()<code/>
     *
     */
    public void loadVariables(String absFilePath) throws IOException{
        Path path = Paths.get(absFilePath);

        List<String> matrixLines = Files.readAllLines(path);
        
        double[][] fVal = new double[4][4];
        int idx = 0;
        for (String line: matrixLines) {
            String[] lineS = line.split(" ");
            for(int i = 0; i < 4; i++){
                fVal[idx][i] = Double.parseDouble(lineS[i]);
            }
            
            ++idx;
        }

        setFValue(new Matrix(fVal));
        setACoeffMatrix(Matrix.getProductMatrix(getXMatrix().getInverseMatrix(), getFValue()));

        solve();
    }

    /**
     * a procedure to load for the values needed by the solver
     * 
     * @param fVal a matrix of f values
     * @see <code>void loadVariables()<code/>
     *
     */
    public void loadVariables(Matrix fVal){
        setFValue(fVal);
        
        setACoeffMatrix(Matrix.getProductMatrix(getXMatrix().getInverseMatrix(), getFValue()));
    }

    /**
     * a procedure to load for the values needed by the solver
     * 
     * @see <code>void loadVariables(Matrix fVal)<code/>
     *
     */
    public void loadVariables(){
        
        loadFValue();
        setACoeffMatrix(Matrix.getProductMatrix(getXMatrix().getInverseMatrix(), getFValue()));
    }

    /**
     * a program to solve for F, given the x and y value
     * 
     * @param x             value
     * @param y             value
     * @return        a double value representing a solution to f for given x and y
     * @see <code>void solveF()<code/>
     *
     */
    public double solveF(Double x, Double y){
        if(getACoeffMatrix() == null) {
            System.out.printf("Variabel belum diinialisasi"); 
            return 0;
        }

        double res=0;

        for (int j = 0; j <= 3; j++){
            for (int i = 0; i <= 3; i++){
                res += this.aCoeffMatrix.getMatrix()[j*4 + i][0] * Math.pow(x, i) * Math.pow(y, j);
            }
        }

        return res;
    }

    /**
     * a program to solve for F, will load for x and y value
     * 
     * 
     * @see <code>double solveF(Double x, Double y)<code/>
     *
     */
    public void solve(){
        if(getACoeffMatrix() == null)
            loadVariables();    

        Scanner scanner = new Scanner(System.in);
        double x = scanner.nextDouble();
        double y = scanner.nextDouble();

        double result = solveF(x, y);

        System.out.printf("f(%.4f, %.4f) = %.4f\n\n", x, y, result);
    }
}
