package com.algeo;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class is used to give a definition to Image
 *
 * @author Christian Albert Hasiholan
 * @author Fakih Anugerah Pratama
 * @author Zidane Firzatullah
 */

public class Image {

    @Getter(AccessLevel.PROTECTED)
    int width, height;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    ImageColor[] imageColor;

    private void Build(BufferedImage image){ 
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.imageColor = new ImageColor[this.width * this.height];

        for(int i = 0; i < this.height; i++){ //i as y
            for(int j = 0; j < this.width; j++){ // j as x
                imageColor[this.width*i + j] = new ImageColor(image.getRGB(j, i));
            }
        }
    }

    /**
     * 
     * @param filename filepath as name
     * @return a buffered image made from the file read
     * @throws Exception
     */
    public BufferedImage readAsBufferedImage(String filename) throws Exception{
        String basePath = "public\\images\\source\\"; 

        BufferedImage img = null; 
        try {
            img = ImageIO.read(new File(basePath + filename));
            System.out.println("Reading from " + basePath + filename + "...");
        } catch (IOException e) {
            System.out.println("Read process failed.");
        }

        return img;
    }

    /**
     * read an image file and build it as Image object
     * @param filename filepath as filename
     * @throws Exception
     */
    public void readImage(String filename)  throws UnsupportedOperationException {
        BufferedImage img = null; 
        try {
            img = ImageIO.read(new File(filename));
            System.out.println("Reading from " + filename + "...");

            if(img == null) throw new IOException();
            System.out.println("Image read. (1/4)");
            Build(img);
        } catch (IOException e) {
            
            throw new UnsupportedOperationException("Read process failed.");
        }
    }

    /**
     * set an BufferedImage as image properties of this object
     * @param img
     */
    public void setImage(BufferedImage img){
        System.out.println("Image set. (3/4)");
        Build(img);
    }

    /**
     * get BufferedImage value of this image object
     */
    public BufferedImage getBufferedImage(){
        BufferedImage res = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB); 

        System.out.println("Getting buffered image... (4/4)");

        for(int i = 0; i < this.height; i++){ //i as y
            for(int j = 0; j < this.width; j++){ // j as x
                res.setRGB(j, i, imageColor[this.width*i + j].getRGB());
            }
        }

        return res;
    }

    /**
     * 
     * @param filename filename as filepath
     * @param imgToSave bufferedimage to save as file
     * @throws Exception
     */
    public static void saveAsFile(String filename, BufferedImage imgToSave) {

        try {
            File file = new File(filename);
            ImageIO.write(imgToSave, "png", file);
            System.out.printf("Saved Image As File in " + filename + "\n\n");    
        }
        catch (FileNotFoundException e){    
            System.out.printf("The folder you're trying to access doesn't exist. Try again!\n");
        }
        catch (IOException e){
            
        }
    }
}
