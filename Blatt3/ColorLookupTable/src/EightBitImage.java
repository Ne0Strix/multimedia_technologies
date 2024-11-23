import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Daniel Posch
 */
class EightBitImage {
    /*bit access flags*/
    public static final int FLAG_A = 1;    // Binary 001
    public static final int FLAG_B = 2;    // Binary 010
    public static final int FLAG_C = 4;    // Binary 100

    /**
     * Nothing needs to be changed here.
     * <p>
     * 1. Read image, 2. Create 8-bit image, 3. Reconstruct image from 8-bit values, 4. Store resulting image
     *
     * @param args
     */
    public static void main(String args[]) throws IOException {
        //read image
        BufferedImage image = readImg("mandrill_512x512.png"); //TODO edit for your needs

        //create 8-bit image and reconstruct it
        image = reconstruct(create8BitImage(image));

        //write reconstructed img to filesystem
        writeImg(image, "png", "mandrill_512x512_reconstructed.png"); //TODO edit for your needs
    }

    /**
     * Converts a 24 bit image (8 bit red, 8 bit green, 8 bit blue) to an 8 bit image (3 bit red, 3 bit green, 2 bit
     * blue).
     *
     * @param image Image as BufferedImage
     * @return 2-dimensional byte array containing the 8 bit image
     */
    private static byte[][] create8BitImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte[][] data = new byte[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int rgb = image.getRGB(x, y);

                // Extract red, green, blue components (8 bits each)
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Quantize the colors to reduce bit depth
                int red3 = red >> 5;
                int green3 = green >> 5;
                int blue2 = blue >> 6;

                // Combine the bits into a single byte
                // Red 7-5
                // Green 4-2
                // Blue 1-0
                int byteValue = (red3 << 5) | (green3 << 2) | blue2;

                data[y][x] = (byte) (byteValue & 0xFF);
            }
        }

        return data;
    }

    /**
     * Converts a 8 bit image to an 24 bit image and returns the image as BufferedImage.
     *
     * @param data 2-dimensional byte array containing the 8 bit image
     * @return BufferedImage containing the reconstructed 24 bit image
     */
    private static BufferedImage reconstruct(byte[][] data) {
        int height = data.length;
        int width = data[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int byteValue = data[y][x] & 0xFF; // Convert byte to unsigned int

                // Extract the quantized components from the 8-bit value
                int red3 = (byteValue >> 5) & 0x07;    // Bits 7-5
                int green3 = (byteValue >> 2) & 0x07;  // Bits 4-2
                int blue2 = byteValue & 0x03;          // Bits 1-0

                // Expand the quantized values back to 8 bits
                int red = (red3 * 255) / 7;     // Scale from 0-7 to 0-255
                int green = (green3 * 255) / 7; // Scale from 0-7 to 0-255
                int blue = (blue2 * 255) / 3;   // Scale from 0-3 to 0-255

                // Combine the RGB components into a single 24-bit value
                int rgb = (red << 16) | (green << 8) | blue;

                // Set the pixel in the image
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

    /**
     * This methods reads an image file and returns it as BufferedImage
     *
     * @param filePath Image file to open
     * @return Image as BufferedImage
     * @see BufferedImage
     */
    private static BufferedImage readImg(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }


    /**
     * Store the given image under the given path
     *
     * @param image  Image to store
     * @param format Image format as String
     * @param path   Path to store image at
     */
    private static void writeImg(BufferedImage image, String format, String path) {
        try {
            File outputFile = new File(path);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}