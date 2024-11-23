import java.awt.image.BufferedImage;

public class Convolution {
    private BufferedImage img;

    /**
     * Constructor that accepts a BufferedImage and saves it internally.
     *
     * @param img The image to which convolution will be applied.
     */
    public Convolution(BufferedImage img) {
        this.img = img;
        if (img.getType() != BufferedImage.TYPE_3BYTE_BGR)
            System.out.println("Warning: Image type other than TYPE_3BYTE_BGR may not be handled correctly.");
    }

    /**
     * Applies the given convolution kernel to the internally saved image and returns the result.
     *
     * @param kernel A two-dimensional convolution kernel.
     * @return A new BufferedImage after applying the convolution.
     */
    public BufferedImage convolution(int[][] kernel) {
        int kernelHeight = kernel.length;
        int kernelWidth = kernel[0].length;

        // calculate offset from centre
        int kHeight = kernelHeight / 2;
        int kWidth = kernelWidth / 2;

        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Precompute the kernel sum, used later for normalization
        int kernelSum = 0;
        for (int[] row : kernel) {
            for (int value : row) {
                kernelSum += value;
            }
        }

        // Handle the case where the kernel sum is zero (e.g., in edge detection kernels)
        if (kernelSum == 0) {
            kernelSum = 1;
        }

        // iterate over image and for each pixel
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                double sumR = 0;
                double sumG = 0;
                double sumB = 0;

                // apply the kernel
                for (int i = 0; i < kernelHeight; i++) {
                    for (int j = 0; j < kernelWidth; j++) {
                        int kernelValue = kernel[i][j];

                        // properly align kernel centre
                        int imgX = x + j - kWidth;
                        int imgY = y + i - kHeight;

                        // Check if the position is within the image boundaries
                        if (imgX >= 0 && imgX < img.getWidth() && imgY >= 0 && imgY < img.getHeight()) {
                            int pixel = img.getRGB(imgX, imgY);

                            int R = (pixel >> 16) & 0xFF;
                            int G = (pixel >> 8) & 0xFF;
                            int B = pixel & 0xFF;

                            sumR += R * kernelValue;
                            sumG += G * kernelValue;
                            sumB += B * kernelValue;
                        }
                        // If outside the image boundaries, contribute zero (zero-padding)
                    }
                }

                // Normalize the sums
                sumR /= kernelSum;
                sumG /= kernelSum;
                sumB /= kernelSum;

                // Clip the results to the valid range [0, 255]
                int r = Math.min(Math.max((int) Math.round(sumR), 0), 255);
                int g = Math.min(Math.max((int) Math.round(sumG), 0), 255);
                int b = Math.min(Math.max((int) Math.round(sumB), 0), 255);

                // Recombine the color channels into one pixel value
                int newPixel = (r << 16) | (g << 8) | b;

                result.setRGB(x, y, newPixel);
            }
        }

        return result;
    }
}
