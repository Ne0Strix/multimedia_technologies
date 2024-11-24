/*
 * Simple Image Manipulations
 * Author: Klaus Schoeffmann, 2024
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private static void showBufferedImage(BufferedImage img, String title) {
        JFrame fS = new JFrame(title);
        ImageLoadingHelper imgComponent = new ImageLoadingHelper(img);
        fS.add(imgComponent);
        fS.pack();
        fS.setVisible(true);
    }

    private static void saveImageAsFile(BufferedImage img, String name) {
        //save as file
        File outputFile = new File(name + ".jpg");
        try {
            ImageIO.write(img, "jpg", outputFile);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify image");
            System.exit(1);
        }

        //create a window
        JFrame f = new JFrame(args[0]);
        addWindowCloseHandler(f);

        //load image and show it in window
        ImageLoadingHelper origImage = new ImageLoadingHelper(args[0]);
        f.add(origImage);
        f.pack();
        f.setVisible(true);

        //extract pixel data
//        Pixeldata pixeldata = new Pixeldata(origImage.img);
//
//        //-----------Exercise 1-----------
//        pixeldata.print(100,500);
//        pixeldata.print(1000,400);
//        pixeldata.print(1400,1000);
//        pixeldata.count(255, true);
//        pixeldata.count(0, false);
        //visualize BPs:
//        for (int n = 7; n >= 0; n--) {
//            BufferedImage imgBP = pixeldata.getBP(n);
//            showBufferedImage(imgBP, "BP" + n);
//        }

        //-----------Exercise 2-----------
//        BufferedImage imgVintage = pixeldata.makeVintage(255, 255, 128);
//        showBufferedImage(imgVintage, "Vintage");
//        saveImageAsFile(imgVintage, "Vintage");

        //-----------Exercise 5-----------
//        BufferedImage imgSaturized = pixeldata.makeSaturized();
//        showBufferedImage(imgSaturized, "Saturized");
//        saveImageAsFile(imgSaturized, "Saturized");

        //-----------Exercise 6-----------
//        BufferedImage imgSepia = pixeldata.makeSepia(0);
//        showBufferedImage(imgSepia, "Sepia");
//        saveImageAsFile(imgSepia, "Sepia");

        // Assignment 5
        // Create a Convolution instance with the loaded image
        Convolution convolutionProcessor = new Convolution(origImage.img);

        int[][] blurKernel = {
                { 1, 1, 1 },
                { 1, 1, 1 },
                { 1, 1, 1 }
        };

        // exercise 5.2
        int[][] boostFilter = {
                { -1, -1, -1 },
                { -1,  9, -1 },
                { -1, -1, -1 }
        };

        int[][] gaussianBlur = {
                { 1, 4, 7, 4, 1},
                { 4, 16, 26, 16, 4},
                { 7, 26, 41, 26, 7},
                { 4, 16, 26, 16, 4},
                { 1, 4, 7, 4, 1}
        };

        // exercise 5.3

        int[][] horizontalEdgeFilter = {
                { 1, 1, 1 },
                { 0, 0, 0 },
                { -1, -1, -1 }
        };

        int[][] tiltedEdgeFilter = {
                { -1, -1, 0 },
                { -1, 0, 1 },
                { 0, 1, 1 }
        };

        // exercise 5.4
        int[][] embossKernel = {
                { -7, -5, -3,  0,  3,  5,  7 },
                { -5, -3, -2,  0,  2,  3,  5 },
                { -3, -2, -1,  0,  1,  2,  3 },
                {  0,  0,  0,  1,  0,  0,  0 },
                {  3,  2,  1,  0, -1, -2, -3 },
                {  5,  3,  2,  0, -2, -3, -5 },
                {  7,  5,  3,  0, -3, -5, -7 }
        };

        int[][] smallEmbossKernel = {
                { -5, -2,  0 },
                { -1,  1,  1 },
                {  0,  2,  5 }
        };

        int[][] gaborKernel = {
                { -1, -2, -1,  0,  1,  2,  1 },
                { -2, -3, -2,  0,  2,  3,  2 },
                { -1, -2, -1,  0,  1,  2,  1 },
                {  0,  0,  0,  0,  0,  0,  0 },
                {  1,  2,  1,  0, -1, -2, -1 },
                {  2,  3,  2,  0, -2, -3, -2 },
                {  1,  2,  1,  0, -1, -2, -1 }
        };


        // Apply the convolution
        BufferedImage transformedImage = convolutionProcessor.convolution(boostFilter);

        // Display the result
        showBufferedImage(transformedImage, "Transformed Image");
        saveImageAsFile(transformedImage, "TransformedImage");
    }

    private static void addWindowCloseHandler(JFrame f) {
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}