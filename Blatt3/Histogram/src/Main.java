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
                BufferedImage image = openBufferdImage(imgPath);

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
    private static BufferedImage openBufferdImage(String filePath) {
        //TODO implement
        return null;
    }

    /**
     * This methods calculates the histogram of a given image and returns it as array.
     *
     * @param image Image to calculate histogram
     * @return Histogram as int array
     */
    private static int[] calcHistogram(BufferedImage image) {
        //TODO implement
        return new int[0];
    }

    /**
     * Calculates the grey value, where only <code>d</code>-percent of pixel have a lower value
     *
     * @param hist Histogram as int array
     * @param d    Define how many percent of pixels should have a value below the lower bound
     * @return Lower bound
     */
    private static int calcLowerBound(int[] hist, double d) {
        //TODO implement
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
        //TODO implement
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
        //TODO implement
    }

    /**
     * Store the given image under the given path
     *
     * @param image Image to store
     * @param path  Path to store image at
     */
    private static void writeImage(BufferedImage image, String path) {
        //TODO implement
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