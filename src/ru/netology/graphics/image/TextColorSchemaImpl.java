package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema{
    private final char[] chars = new char[]{'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
    private final double MAX_OCTET = 255;

    @Override
    public char convert(int color) {
        int index = (int) (color / MAX_OCTET * (chars.length - 1));
        return chars[index];
    }
}
