import lombok.Getter;
import lombok.Setter;

import lombok.AccessLevel;

public class ImageColor {
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    int r, g, b;

    public ImageColor(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ImageColor(int rgb){
        this.r = (rgb >> 16) & 0xff;
        this.g = (rgb >> 8) & 0xff;
        this.b = rgb&0xff;

        // System.out.printf("%d, %d, %d\n", r, g, b);
    }


    public int getRGB(){
        return r<<16 | g<<8 | b; //THIS MAKES IT WRONG!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }


}
