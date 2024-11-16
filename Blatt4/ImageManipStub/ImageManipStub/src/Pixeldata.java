/*
 * Simple Image Manipulations
 * Author: Klaus Schoeffmann, 2024
 */

import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Pixeldata {
    BufferedImage img;
    int A, R, G, B;
    // for practical reasons we can use the following members to save intermediate float values for R,G,B in HSVtoRGB
    // they can be set with helper method setFloatsRGB()
    private float fR, fG, fB;
    float H, S, V;

    public Pixeldata(BufferedImage img) {
        this.img = img;
        if (img.getType() != BufferedImage.TYPE_3BYTE_BGR)
            System.out.println("Cannot handle images of type other than TYPE_3BYTE_BGR!");
    }

    private void extractRGB(int x, int y) {
        // TODO: Exercise 4.1 - explain the code in this method
        int pixel = img.getRGB(x, y);
        A = 255; //no alpha in TYPE_3BYTE_BGR
        R = (pixel >> 16) & 0xFF;
        G = (pixel >> 8) & 0xFF;
        B = pixel & 0xFF;
    }

    private int toPixel(int r, int g, int b) {
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public void print(int x, int y) {
        this.extractRGB(x,y);
        System.out.println("Pixel at (" + x + "," + y + ") = " + R + "/" + G + "/" + B + " (alpha=" + A + ")");
    }

    public void count(int val, boolean gt) {
        // TODO: Exercise 4.1 - explain the code in this method
        int c1=0, c2=0, c3=0;

        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                this.extractRGB(x, y);
                if ((gt ? R >= val : R <= val)) c1++;
                if ((gt ? G >= val : G <= val)) c2++;
                if ((gt ? B >= val : B <= val)) c3++;
            }

        System.out.println("count result for (" + val + "," + gt + "): \t1:" + c1 + "\t2: " + c2 + "\t3: " + c3);
    }

    public BufferedImage getBP(int n) {
        // TODO: Exercise 4.1 - explain the code in this method

        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                this.extractRGB(x, y);
                int r = (R & (1 << n)) << (7 - n);
                int g = (G & (1 << n)) << (7 - n);
                int b = (B & (1 << n)) << (7 - n);
                imgN.setRGB(x, y, toPixel(r, g, b));
            }
        }

        return imgN;
    }

    public BufferedImage makeVintage(int maxR, int maxG, int maxB) {
        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Iterate over all pixels in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                // Extract the RGB components of the current pixel
                this.extractRGB(x, y);

                // Apply the vintage filter by limiting the RGB values
                int r = Math.min(R, maxR);
                int g = Math.min(G, maxG);
                int b = Math.min(B, maxB);

                // Set the new pixel value in the resulting image
                imgN.setRGB(x, y, toPixel(r, g, b));
            }
        }
        return imgN;
    }

    private void RGB2HSV() {
        //TODO: Exercise 4.3: convert RGB to HSV (save result in H, S, V members).
    }

    private int clip(float val) {
        if (val < 0)
            return 0;
        else if (val > 255)
            return 255;
        else
            return (int)val;
    }

    private void setFloatsRGB(float r, float g, float b) {
        fR = r;
        fG = g;
        fB = b;
    }

    private void HSV2RGB() {
        //TODO: Exercise 4.4: convert HSV to RGB (save result in R, G, B members).
        //Note: you might want to use the clip() helper function above
    }


    public BufferedImage makeSaturized() {
        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // TODO: Exercise 4.5 - implement Saturation filter

        return  imgN;
    }

    public BufferedImage makeSepia(float HtoUse) {
        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // TODO: Exercise 4.6 - implement Sepia filter

        return  imgN;
    }



}
