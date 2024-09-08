package net.kokoricraft.holotools.utils.objects;

import com.google.common.base.Preconditions;

import java.awt.*;
import java.util.Objects;

public final class HoloColor {
    private static final int BIT_MASK = 0xff;
    private static final int DEFAULT_ALPHA = 255;

    public static final HoloColor WHITE = fromRGB(0xFFFFFF);
    public static final HoloColor SILVER = fromRGB(0xC0C0C0);
    public static final HoloColor GRAY = fromRGB(0x808080);
    public static final HoloColor BLACK = fromRGB(0x000000);
    public static final HoloColor RED = fromRGB(0xFF0000);
    public static final HoloColor MAROON = fromRGB(0x800000);
    public static final HoloColor YELLOW = fromRGB(0xFFFF00);
    public static final HoloColor OLIVE = fromRGB(0x808000);
    public static final HoloColor LIME = fromRGB(0x00FF00);
    public static final HoloColor GREEN = fromRGB(0x008000);
    public static final HoloColor AQUA = fromRGB(0x00FFFF);
    public static final HoloColor TEAL = fromRGB(0x008080);
    public static final HoloColor BLUE = fromRGB(0x0000FF);
    public static final HoloColor NAVY = fromRGB(0x000080);
    public static final HoloColor FUCHSIA = fromRGB(0xFF00FF);
    public static final HoloColor PURPLE = fromRGB(0x800080);
    public static final HoloColor ORANGE = fromRGB(0xFFA500);

    private final byte alpha;
    private final byte red;
    private final byte green;
    private final byte blue;

    public static HoloColor fromARGB(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        return new HoloColor(alpha, red, green, blue);
    }

    public static HoloColor fromRGB(int red, int green, int blue) throws IllegalArgumentException {
        return new HoloColor(DEFAULT_ALPHA, red, green, blue);
    }

    public static HoloColor fromBGR(int blue, int green, int red) throws IllegalArgumentException {
        return new HoloColor(DEFAULT_ALPHA, red, green, blue);
    }

    public static HoloColor fromRGB(int rgb) throws IllegalArgumentException {
        Preconditions.checkArgument((rgb >> 24) == 0, "Extraneous data in: %s", rgb);
        return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb & BIT_MASK);
    }

    public static HoloColor fromARGB(int argb) {
        return fromARGB(argb >> 24 & BIT_MASK, argb >> 16 & BIT_MASK, argb >> 8 & BIT_MASK, argb & BIT_MASK);
    }

    public static HoloColor fromBGR(int bgr) throws IllegalArgumentException {
        Preconditions.checkArgument((bgr >> 24) == 0, "Extrenuous data in: %s", bgr);
        return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr & BIT_MASK);
    }

    public static HoloColor fromHex(String hexColor, int opacity){
        if(hexColor == null || !hexColor.matches("#[0-9a-fA-F]{6}"))
            throw new IllegalArgumentException("Invalid hex color format. Must be #RRGGBB.");

        Color color = Color.decode(hexColor);

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        if(opacity < 0 || opacity > 255)
            throw new IllegalArgumentException("Opacity must be between 0 and 255.");

        return fromARGB(opacity, red, green, blue);
    }

    private HoloColor(int red, int green, int blue) {
        this(DEFAULT_ALPHA, red, green, blue);
    }

    private HoloColor(int alpha, int red, int green, int blue) {
        Preconditions.checkArgument(alpha >= 0 && alpha <= BIT_MASK, "Alpha[%s] is not between 0-255", alpha);
        Preconditions.checkArgument(red >= 0 && red <= BIT_MASK, "Red[%s] is not between 0-255", red);
        Preconditions.checkArgument(green >= 0 && green <= BIT_MASK, "Green[%s] is not between 0-255", green);
        Preconditions.checkArgument(blue >= 0 && blue <= BIT_MASK, "Blue[%s] is not between 0-255", blue);

        this.alpha = (byte) alpha;
        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;
    }

    public int getAlpha() {
        return BIT_MASK & alpha;
    }

    public HoloColor setAlpha(int alpha) {
        return fromARGB(alpha, getRed(), getGreen(), getBlue());
    }

    public int getRed() {
        return BIT_MASK & red;
    }

    public HoloColor setRed(int red) {
        return fromARGB(getAlpha(), red, getGreen(), getBlue());
    }

    public int getGreen() {
        return BIT_MASK & green;
    }

    public HoloColor setGreen(int green) {
        return fromARGB(getAlpha(), getRed(), green, getBlue());
    }

    public int getBlue() {
        return BIT_MASK & blue;
    }

    public HoloColor setBlue(int blue) {
        return fromARGB(getAlpha(), getRed(), getGreen(), blue);
    }

    public int asRGB() {
        return getRed() << 16 | getGreen() << 8 | getBlue();
    }

    public int asARGB() {
        return getAlpha() << 24 | getRed() << 16 | getGreen() << 8 | getBlue();
    }

    public int asBGR() {
        return getBlue() << 16 | getGreen() << 8 | getRed();
    }

    public HoloColor mixDyes(HoloColor... colors) {
        Preconditions.checkArgument(colors != null && colors.length > 0, "No colors specified");

        int totalRed = getRed();
        int totalGreen = getGreen();
        int totalBlue = getBlue();

        for (HoloColor color : colors) {
            totalRed += color.getRed();
            totalGreen += color.getGreen();
            totalBlue += color.getBlue();
        }

        int count = colors.length + 1;
        return fromRGB(totalRed / count, totalGreen / count, totalBlue / count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoloColor)) return false;
        HoloColor color = (HoloColor) o;
        return alpha == color.alpha &&
                red == color.red &&
                green == color.green &&
                blue == color.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, red, green, blue);
    }

    @Override
    public String toString() {
        return "HoloColor{" +
                "alpha=" + getAlpha() +
                ", red=" + getRed() +
                ", green=" + getGreen() +
                ", blue=" + getBlue() +
                '}';
    }
}
