import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class Main {

    /**
     * Main class calls calculation of histograms and methods for histogram stretching, nothing needs
     * to be changed here
     *
     * @param args
     */
    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //edit input path to your needs
                String imgPath = "akropolis_bad.png"; //TODO

                //read image from disk
                BufferedImage image = null;
                try {
                    image = openBufferdImage(imgPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //calculate histogram
                int[] hist = calcHistogram(image);

                //show histogram
                new Histogram(hist, "Histogram");

                //calculate bounds for histogram stretching
                int low = calcLowerBound(hist, 0.01);
                int up = calcUpperBound(hist, 0.01);

                //print bounds
                System.out.println("LowerBound = " + low);
                System.out.println("UpperBound = " + up);

                //copy and stretch image
                BufferedImage image_streteched = deepCopy(image);
                stretchHistogramm(image_streteched, low, up);

                //calculate histogram
                hist = calcHistogram(image_streteched);

                //show histogram of stretched image
                new Histogram(hist, "Histogram (stretched)");

                //write stretched image to disk (edit to your needs)
                writeImage(image_streteched, "akropolis_good.png"); //TODO
            }
        });
    }

    /**
     * This methods reads an image file and returns it as BufferedImage
     *
     * @param filePath Image file to open
     * @return Image as BufferedImage
     * @see BufferedImage
     */
    private static BufferedImage openBufferdImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    /**
     * This methods calculates the histogram of a given image and returns it as array.
     *
     * @param image Image to calculate histogram
     * @return Histogram as int array
     */
    private static int[] calcHistogram(BufferedImage image) {
        int[] histogram = new int[256]; // For grayscale, 256 possible intensity values

        // Loop through each pixel in the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Get the grayscale value of the pixel (0-255)
                int color = new Color(image.getRGB(x, y)).getRed(); // assuming grayscale, red = green = blue
                histogram[color]++;
            }
        }

        return histogram;
    }

    /**
     * Calculates the grey value, where only <code>d</code>-percent of pixel have a lower value
     *
     * @param hist Histogram as int array
     * @param d    Define how many percent of pixels should have a value below the lower bound
     * @return Lower bound
     */
    private static int calcLowerBound(int[] hist, double d) {
        int totalPixels = 0;
        for (int count : hist) {
            totalPixels += count;
        }

        int targetPixels = (int) (totalPixels * d);
        int cumulativePixels = 0;
        for (int i = 0; i < hist.length; i++) {
            cumulativePixels += hist[i];
            if (cumulativePixels >= targetPixels) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Calculates the grey value, where only <code>d</code>-percent of pixel have a higher value
     *
     * @param hist Histogram as int array
     * @param d    Define how many percent of pixels should have a value above the upper bound
     * @return Upper bound
     */
    private static int calcUpperBound(int[] hist, double d) {
        int totalPixels = 0;
        for (int count : hist) {
            totalPixels += count;
        }

        int targetPixels = (int) (totalPixels * d); // Corrected line
        int cumulativePixels = 0;
        for (int i = hist.length - 1; i >= 0; i--) {
            cumulativePixels += hist[i];
            if (cumulativePixels >= targetPixels) {
                return i;
            }
        }

        return 255;
    }

    /**
     * Stretches the histogram of the given image
     *
     * @param image Image, where the histogram should be stretched
     * @param low   Lower bound
     * @param high  Upper bound
     */
    private static void stretchHistogramm(BufferedImage image, int low, int high) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y) & 0xFF; // Get the grayscale value

                // Calculate the new pixel value using the linear transformation
                int newPixel = (int) ((pixel - low) * 255.0 / (high - low));

                // Ensure the new pixel value is within the valid range (0-255)
                newPixel = Math.max(0, Math.min(255, newPixel));

                // Set the new pixel value
                image.setRGB(x, y, (newPixel << 16) | (newPixel << 8) | newPixel);
            }
        }
    }

    /**
     * Store the given image under the given path
     *
     * @param image Image to store
     * @param path  Path to store image at
     */
    private static void writeImage(BufferedImage image, String path) {
        try {
            File outputFile = new File(path);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a deep copy of the given BufferedImage, nothing needs to be changed here
     *
     * @param bi Image to clone
     * @return Cloned image
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        if (bi == null)
            return null;

        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}