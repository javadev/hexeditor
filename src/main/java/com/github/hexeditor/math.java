package com.github.hexeditor;

class math {

    public static double nextUp(double var0) {
        if (!Double.isNaN(var0) && var0 != Double.POSITIVE_INFINITY) {
            var0 += 0.0D;
            return Double.longBitsToDouble(
                    Double.doubleToRawLongBits(var0) + (var0 >= 0.0D ? 1L : -1L));
        } else {
            return var0;
        }
    }

    public static float nextUp(float var0) {
        if (!Float.isNaN(var0) && var0 != Float.POSITIVE_INFINITY) {
            var0 += 0.0F;
            return Float.intBitsToFloat(Float.floatToRawIntBits(var0) + (var0 >= 0.0F ? 1 : -1));
        } else {
            return var0;
        }
    }

    public static double nextDown(double var0) {
        return !Double.isNaN(var0) && var0 != Double.NEGATIVE_INFINITY
                ? (var0 == 0.0D
                        ? -4.9E-324D
                        : Double.longBitsToDouble(
                                Double.doubleToRawLongBits(var0) + (var0 > 0.0D ? -1L : 1L)))
                : var0;
    }

    public static float nextDown(float var0) {
        return !Float.isNaN(var0) && var0 != Float.NEGATIVE_INFINITY
                ? (var0 == 0.0F
                        ? -1.4E-45F
                        : Float.intBitsToFloat(
                                Float.floatToRawIntBits(var0) + (var0 > 0.0F ? -1 : 1)))
                : var0;
    }
}
