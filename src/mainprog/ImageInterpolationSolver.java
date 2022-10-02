import lombok.AccessLevel;
import lombok.Getter;


import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;


/**
 * This class is used to solve for the interpolated image based on the set <code>Image</code> object.
 * 
 * @author Christian Albert Hasiholan
 * @author Fakih Anugerah Pratama
 * @author Zidane Firzatullah
 */
public class ImageInterpolationSolver {

    @Getter(AccessLevel.PROTECTED)
    Image image;

    int targetWidth;
    int targetHeight;
    

    /**
     * 
     * @param val value to clamp
     * @param a minimum value
     * @param b maximum value
     * @return clamped value
     */
    private static int Clamp(int val, int a, int b){
        if(val < a) val = a;
        else if(val > b) val = b;

        return val;
    }


    /**
     * a function to return a matrix of color values of 16 pixels around a predetermined target pixel with a position of (<code>xStart</code>,<code>yStart</code>)
     * @param xStart x position of target pixel
     * @param yStart y position of target pixel
     * @return a matrix of 16 pixels around the target
     */
    private Matrix getFValue(int xStart, int yStart){
        double[][] res = new double[16][1];

        ImageColor[] iColors = this.image.getImageColor();

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                res[4*i + j][0] = iColors[this.image.getWidth()*(Clamp(yStart + i - 2, 0, this.image.getHeight()-1)) + (Clamp(xStart + j -2, 0, this.image.getHeight()-1))].getRGB();
            }
        }

        return new Matrix(res);
    }
    
    /**
     * a procedure to solve for the interpolated image based on the <code>Image</code> set.
     */
    public void solve() {
        BicubicInterpolationSolver bi = new BicubicInterpolationSolver();
        int[] newData = new int[this.targetHeight*this.targetWidth];

        final long start = System.currentTimeMillis();
 
        System.out.println("Interpolating image... (2/4)");

        Integer[] intArr = {0, 1, 2, 3}; // 4 parallel streams
        List<Integer> list = Arrays.asList(intArr);
        list.parallelStream().forEach(e -> {
            for(int i = (e)*(this.targetWidth/intArr.length); i < (e+1)*(this.targetWidth/intArr.length); i++){
                for(int j = 0; j < this.targetHeight; j++){
                    bi.loadVariables((getFValue((int)(j/2), (int)(i/2))));

                    newData[i*this.targetWidth + j] = (int)bi.solveF(0d, 0d);
                }
            }
        });

        final long finish = System.currentTimeMillis();

        Long timeElapsed = finish - start;
        System.out.printf("\tFinished in %.2f seconds\n", timeElapsed/1000f);

        BufferedImage result = new BufferedImage(this.targetWidth, this.targetHeight, BufferedImage.TYPE_INT_RGB);
        result.setRGB(0, 0, this.targetWidth, this.targetHeight, newData, 0, this.targetWidth);

        this.image.setImage(result);
    }

    /**
     * 
     * @param img <code>Image</code> object to interpolate
     */
    public void setImage(Image img){
        this.image = img;
        this.targetWidth = 2*img.getWidth();
        this.targetHeight = 2*img.getHeight();
    }
}
