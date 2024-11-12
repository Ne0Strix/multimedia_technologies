import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
class OptimizedLUT {

    /**
     * Nothing needs to be changed here. Reads the image, starts process of creating an optimized LUT and stores
     * the resulting image.
     *
     * @param args
     */
    public static void main(String args[]) {
        //read image
        BufferedImage image = readImg("mandrill_512x512.png"); // TODO fix for your needs

        //create CLUT
        CLUT clut = createLookUpTable(image);

        // Print CLUT
        clut.printCLUT();

        //use CLUT
        useClut(image, clut);

        //store image
        writeImg(image, "png", "mandrill_512x512_reconstructed_opt.png"); // TODO fix for your needs
    }

    private static CLUT createLookUpTable(BufferedImage image) {

        //1. extract and sort channels
        int[] r = new int[0];
        int[] g = new int[0];
        int[] b = new int[0];

        // Todo implement


        //2. create a new CLUT and generate the CLUT entries
        CLUT clut = new CLUT();
        createLookUpEntry(r, "", 3, clut, CLUT.Channel.R);
        createLookUpEntry(g, "", 3, clut, CLUT.Channel.G);
        createLookUpEntry(b, "", 2, clut, CLUT.Channel.B);

        return clut;
    }

    private static void createLookUpEntry(int[] array, String codeword, int bitsLeft, CLUT clut, CLUT.Channel channel) {
        // Todo implement

        //check if there are bits left for coding

        //either split again

        //or create new entry in CLUT

        //
        /**
         * Use a command like this to add a new entry to the CLUT
         * clut.addEntry(codeword, [lower bound], [upper bound], [represented color value], channel);
         *
         * See CLUT.addEntry for more information about the usage
         */
        return;
    }

    private static void useClut(BufferedImage image, CLUT clut) {
        int img_heigth = image.getHeight();
        int img_width = image.getWidth();

        int r, g, b;

        for (int i = 0; i < img_width; i++) {
            for (int k = 0; k < img_heigth; k++) {
                Color c = new Color(image.getRGB(i, k));
                r = clut.mapValue(c.getRed(), CLUT.Channel.R);
                g = clut.mapValue(c.getGreen(), CLUT.Channel.G);
                b = clut.mapValue(c.getBlue(), CLUT.Channel.B);

                c = new Color(r, g, b);
                image.setRGB(i, k, c.getRGB());
            }
        }
    }

    /**
     * This methods reads an image file and returns it as BufferedImage
     *
     * @param filePath Image file to open
     * @return Image as BufferedImage
     * @see BufferedImage
     */
    private static BufferedImage readImg(String filePath) {
        // Todo implement

        return null;
    }

    /**
     * Store the given image under the given path
     *
     * @param image  Image to store
     * @param format Image format as String
     * @param path   Path to store image at
     */
    private static void writeImg(BufferedImage image, String format, String path) {
        // Todo implement

    }
}