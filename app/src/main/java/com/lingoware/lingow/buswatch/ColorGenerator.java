package com.lingoware.lingow.buswatch;

import android.graphics.Color;

/**
 * Created by lingow on 3/05/15.
 */
public class ColorGenerator {

    public static int[] generateColors(int size) {
        return generateColorSeries(2, 2.4, 2.8, 0, 2, 4, 127, 128, size);
    }

    private static double acotar(double num, double max, double min) {
        return (num > max) ? max : (num < min) ? min : num;
    }

    private static int[] generateColorSeries(double fr, double fg, double fb, double pr, double pg, double pb, double width, double center, int length) {
        int ret[] = new int[length];
        for (int i = 0; i < length; i++) {
            ret[i] = Color.argb(
                    0xFF,
                    (int) Math.round(acotar(Math.sin(fr * i + pr) * width + center, 255, 0)),
                    (int) Math.round(acotar(Math.sin(fg * i + pg) * width + center, 255, 0)),
                    (int) Math.round(acotar(Math.sin(fb * i + pb) * width + center, 255, 0))
            );
        }
        return ret;
    }
}
