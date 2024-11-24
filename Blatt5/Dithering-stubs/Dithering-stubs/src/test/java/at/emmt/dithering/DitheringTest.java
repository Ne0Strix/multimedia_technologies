package at.emmt.dithering;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DitheringTest {

    private static Dithering.RGBPixel blackPixel, whitePixel, lightgrey, y127, y128, px127, px128, px1;
    @BeforeAll
    public static void init(){
        blackPixel = new Dithering.RGBPixel(0, 0, 0);
        whitePixel = new Dithering.RGBPixel(255, 255, 255);
        lightgrey = new Dithering.RGBPixel(200, 200, 200);
        y127 = new Dithering.RGBPixel(127, 128, 128);
        y128 = new Dithering.RGBPixel(129, 128, 128);
        px127 = new Dithering.RGBPixel(127, 127, 127);
        px128 = new Dithering.RGBPixel(128, 128, 128);
        px1 = new Dithering.RGBPixel(1, 1, 1);
    }
    @Test
    public void closestColor() {
        Dithering.RGBPixel[] palette = new Dithering.RGBPixel[]{
                new Dithering.RGBPixel(0, 0, 0),
                new Dithering.RGBPixel(128, 128, 128),
                new Dithering.RGBPixel(255, 255, 255)
        };

        assertAll("closestColor",
                ()->assertEquals(blackPixel, Dithering.closestColor(blackPixel, palette)),
                ()->assertEquals(whitePixel, Dithering.closestColor(whitePixel, palette)),
                ()->assertEquals(whitePixel, Dithering.closestColor(lightgrey, palette)),
                ()->assertEquals(new Dithering.RGBPixel(128, 128, 128), Dithering.closestColor(y127, palette))
        );
    }

    @Test
    public void closestColorBW() {
        assertAll("closestColorBW",
                ()->assertEquals(blackPixel, Dithering.closestColorBW(blackPixel)),
                ()->assertEquals(whitePixel, Dithering.closestColorBW(whitePixel)),
                ()->assertEquals(whitePixel, Dithering.closestColorBW(lightgrey)),
                ()->assertEquals(blackPixel, Dithering.closestColorBW(y127)),
                ()->assertEquals(whitePixel, Dithering.closestColorBW(y128))
        );

    }

    @Test
    public void RGBPixelAdd() {
        assertAll("add",
                ()->assertEquals(px128, px127.add(px1)),
                ()->assertEquals(px127, new Dithering.RGBPixel(127, 127, 127)),
                ()->assertEquals(px1, new Dithering.RGBPixel(1, 1, 1)),
                ()->assertEquals(new Dithering.RGBPixel(256, 256, 256), px128.add(px128))
        );
    }

    @Test
    public void RGBPixelSub() {
        assertAll("sub",
                ()->assertEquals(px127, px128.sub(px1)),
                ()->assertEquals(px128, new Dithering.RGBPixel(128, 128, 128)),
                ()->assertEquals(px1, new Dithering.RGBPixel(1, 1, 1)),
                ()->assertEquals(new Dithering.RGBPixel(-1, -1, -1), px127.sub(px128))
        );
    }

    @Test
    public void RGBPixelDiff() {
        assertAll("diff",
                ()->assertEquals(3, px127.diff(px128)),
                ()->assertEquals(48387, px127.diff(new Dithering.RGBPixel(0, 0, 0)))
        );
    }
}