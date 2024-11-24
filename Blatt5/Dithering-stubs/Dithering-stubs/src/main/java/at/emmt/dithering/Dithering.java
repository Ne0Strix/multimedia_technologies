package at.emmt.dithering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class Dithering {
    /**
     * palette will be used only for exercise 4.2
     */
    public static final RGBPixel[] palette = new RGBPixel[]
            {
                    new RGBPixel(0, 0, 0),
                    new RGBPixel(0, 0, 255),
                    new RGBPixel(0, 255, 0),
                    new RGBPixel(0, 255, 255),
                    new RGBPixel(255, 0, 0),
                    new RGBPixel(255, 0, 255),
                    new RGBPixel(255, 255, 0),
                    new RGBPixel(255, 255, 255)
            };

    public static void main(String args[]) {
        BufferedImage image = readImg("mandrill_512x512.png"); // TODO edit to your needs
        BufferedImage image_bw = deepCopy(image);
        BufferedImage image_color = deepCopy(image);
        ;

        image_bw = bw_dither(image_bw);
        image_color = color_dither(image_color);

        writeImg(image_bw, "png", "dither_bw.png"); // TODO edit to your needs
        writeImg(image_color, "png", "dither_color.png"); // TODO edit to your needs
    }

    /**
     * @param image the input image
     * @return the dithered black and white image
     */
    public static BufferedImage bw_dither(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // For each y from top to bottom
        for (int y = 0; y < height; y++) {
            // For each x from left to right
            for (int x = 0; x < width; x++) {
                // oldpixel := pixel[y][x]
                RGBPixel oldPixel = new RGBPixel(image.getRGB(x, y));
                // newpixel := find_closest_palette_color(oldpixel)
                RGBPixel newPixel = closestColorBW(oldPixel);
                // pixel[y][x] := newpixel
                image.setRGB(x, y, newPixel.toRGB());
                // quant_error := oldpixel - newpixel
                RGBPixel quant_error = oldPixel.sub(newPixel);

                // Distribute the quant_error
                // pixel[y][x+1] := pixel[y][x+1] + 7/16 * quant_error
                if (x + 1 < width) {
                    RGBPixel neighbor = new RGBPixel(image.getRGB(x + 1, y));
                    RGBPixel errorAdjusted = neighbor.add(quant_error.mul(7.0 / 16.0));
                    image.setRGB(x + 1, y, errorAdjusted.toColor().getRGB());
                }
                // pixel[y+1][x] := pixel[y+1][x] + 5/16 * quant_error
                if (y + 1 < height) {
                    RGBPixel neighbor = new RGBPixel(image.getRGB(x, y + 1));
                    RGBPixel errorAdjusted = neighbor.add(quant_error.mul(5.0 / 16.0));
                    image.setRGB(x, y + 1, errorAdjusted.toColor().getRGB());
                }
                // pixel[y+1][x-1] := pixel[y+1][x-1] + 3/16 * quant_error
                if (x - 1 >= 0 && y + 1 < height) {
                    RGBPixel neighbor = new RGBPixel(image.getRGB(x - 1, y + 1));
                    RGBPixel errorAdjusted = neighbor.add(quant_error.mul(3.0 / 16.0));
                    image.setRGB(x - 1, y + 1, errorAdjusted.toColor().getRGB());
                }
                // pixel[y+1][x+1] := pixel[y+1][x+1] + 1/16 * quant_error
                if (x + 1 < width && y + 1 < height) {
                    RGBPixel neighbor = new RGBPixel(image.getRGB(x + 1, y + 1));
                    RGBPixel errorAdjusted = neighbor.add(quant_error.mul(1.0 / 16.0));
                    image.setRGB(x + 1, y + 1, errorAdjusted.toColor().getRGB());
                }
            }
        }

        return image;
    }

    /**
     * @param image the input image
     * @return the dithered 8bit color image using the static palette
     */
    public static BufferedImage color_dither(BufferedImage image) {
        //TODO IMPLEMENT FOR EXCERCISE 5.6

        return image;
    }

    /**
     * @param color input color
     * @return the closest outputcolor. (Can only be black or white!)
     */
    public static RGBPixel closestColorBW(RGBPixel color) {
        double Y = 0.299 * color.r + 0.587 * color.g + 0.114 * color.b;
        if (Y >= 128) {
            return new RGBPixel(255, 255, 255); // White
        } else {
            return new RGBPixel(0, 0, 0); // Black
        }
    }

    /**
     * @param c       the input color
     * @param palette the palette to use
     * @return the closest color of the palette compared to c
     */
    public static RGBPixel closestColor(RGBPixel c, RGBPixel[] palette) {
        //TODO IMPLEMENT FOR EXCERCISE 5.6

        return null;
    }

    /**
     * The Class RGBPixel is a helper class to ease the calculation with colors.
     */
    static class RGBPixel {
        int r, g, b;

        public RGBPixel(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        public RGBPixel(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public RGBPixel add(RGBPixel o) {
            int newR = this.r + o.r;
            int newG = this.g + o.g;
            int newB = this.b + o.b;
            return new RGBPixel(newR, newG, newB);
        }

        public RGBPixel sub(RGBPixel o) {
            int newR = this.r - o.r;
            int newG = this.g - o.g;
            int newB = this.b - o.b;
            return new RGBPixel(newR, newG, newB);
        }

        public RGBPixel mul(double d) {
            return new RGBPixel((int) (d * r), (int) (d * g), (int) (d * b));
        }

        public int toRGB() {
            return toColor().getRGB();
        }

        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }

        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }

        public int diff(RGBPixel o) {
            //TODO IMPLEMENT FOR EXCERCISE 5.6

            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof RGBPixel)) {
                return false;
            }
            return this.r == ((RGBPixel) obj).r && this.g == ((RGBPixel) obj).g && this.b == ((RGBPixel) obj).b;
        }
    }

    private static BufferedImage readImg(String filePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("Could not load Image\n");
            System.exit(-1);
        }
        return image;
    }

    private static void writeImg(BufferedImage image, String format, String path) {
        try {
            ImageIO.write(image, format, new File(path));
        } catch (IOException e) {
            System.out.println("Could not save Image\n");
            System.exit(-1);
        }
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}