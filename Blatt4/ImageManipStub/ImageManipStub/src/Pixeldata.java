/*
 * Simple Image Manipulations
 * Author: Klaus Schoeffmann, 2024
 */

import java.awt.image.BufferedImage;

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
        // Extract original RGB values
        this.extractRGB(x, y);
        int originalR = R;
        int originalG = G;
        int originalB = B;

        // Convert to HSV
        this.RGB2HSV();

        // Store the HSV values
        float h = H;
        float s = S;
        float v = V;

        // Convert back to RGB
        this.HSV2RGB();
        int convertedR = R;
        int convertedG = G;
        int convertedB = B;

        // Print out the values
        System.out.println("Pixel at (" + x + "," + y + "):");
        System.out.println("Original RGB: " + originalR + "/" + originalG + "/" + originalB);
        System.out.println("Converted HSV: H:" + h + " S:" + s + " V:" + v);
        System.out.println("Converted Back RGB: " + convertedR + "/" + convertedG + "/" + convertedB);
        System.out.println();
    }

    public void count(int val, boolean gt) {
        int c1 = 0, c2 = 0, c3 = 0;

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
        // Convert RGB from 0-255 range to 0-1 range
        float r = R / 255.0f;
        float g = G / 255.0f;
        float b = B / 255.0f;

        // Find the maximum and minimum values among R, G, B
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        // Compute V (Value)
        V = max; // V ranges from 0 to 1

        // Compute S (Saturation)
        if (max == 0) {
            S = 0; // Avoid division by zero; if max is 0, saturation is 0
        } else {
            S = delta / max; // S ranges from 0 to 1
        }

        // Compute H (Hue)
        if (delta == 0) {
            H = 0; // Hue is undefined when delta is 0; set to 0 for practical purposes
        } else {
            if (max == r) {
                H = 60 * (((g - b) / delta) % 6);
            } else if (max == g) {
                H = 60 * (((b - r) / delta) + 2);
            } else { // max == b
                H = 60 * (((r - g) / delta) + 4);
            }

            // Ensure H is positive
            if (H < 0) {
                H += 360;
            }
        }
    }

    private int clip(float val) {
        if (val < 0)
            return 0;
        else if (val > 255)
            return 255;
        else
            return (int) val;
    }

    private void setFloatsRGB(float r, float g, float b) {
        fR = r;
        fG = g;
        fB = b;
    }

    private void HSV2RGB() {
        float h = H;
        float s = S;
        float v = V;

        // Ensure hue is within [0, 360)
        h = h % 360;
        if (h < 0) {
            h += 360;
        }

        // If saturation is zero, the color is a shade of gray
        if (s == 0) {
            // R, G, and B are all equal to V (converted to [0,255])
            int value = clip(v * 255);
            R = value;
            G = value;
            B = value;
        } else {
            float c = v * s; // Chroma
            float x = c * (1 - Math.abs((h / 60) % 2 - 1));
            float m = v - c;

            float rPrime, gPrime, bPrime;

            if (h < 60) {
                rPrime = c;
                gPrime = x;
                bPrime = 0;
            } else if (h < 120) {
                rPrime = x;
                gPrime = c;
                bPrime = 0;
            } else if (h < 180) {
                rPrime = 0;
                gPrime = c;
                bPrime = x;
            } else if (h < 240) {
                rPrime = 0;
                gPrime = x;
                bPrime = c;
            } else if (h < 300) {
                rPrime = x;
                gPrime = 0;
                bPrime = c;
            } else {
                rPrime = c;
                gPrime = 0;
                bPrime = x;
            }

            // Add m to match the lightness
            float r = (rPrime + m) * 255;
            float g = (gPrime + m) * 255;
            float b = (bPrime + m) * 255;

            // Set the intermediate float RGB values if needed
            setFloatsRGB(r, g, b);

            // Clip the values to [0,255] and convert to integers
            R = clip(r);
            G = clip(g);
            B = clip(b);
        }
    }


    public BufferedImage makeSaturized() {
        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Iterate over all pixels in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                // Extract the RGB components of the current pixel
                this.extractRGB(x, y);

                // Convert the RGB values to HSV
                this.RGB2HSV();

                // Increase the saturation by 60%, ensuring it does not exceed 1.0
                S = Math.min(S * 1.6f, 1.0f);

                // Convert the HSV values back to RGB
                this.HSV2RGB();

                // Set the new pixel value in the resulting image
                imgN.setRGB(x, y, toPixel(R, G, B));
            }
        }

        return imgN;
    }

    public BufferedImage makeSepia(float fixedHue) {
        BufferedImage imgN = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Iterate over all pixels in the image
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                // Extract the RGB components of the current pixel
                this.extractRGB(x, y);

                // Convert the RGB values to HSV
                this.RGB2HSV();

                // Fix the hue to the specified fixedHue value
                H = fixedHue;

                // Ensure hue is within [0, 360)
                H = H % 360;
                if (H < 0) {
                    H += 360;
                }

                // Convert the HSV values back to RGB
                this.HSV2RGB();

                // Set the new pixel value in the resulting image
                imgN.setRGB(x, y, toPixel(R, G, B));
            }
        }

        return imgN;
    }
}
