package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private int width;
    private int height;
    private double maxRatio;
    private TextColorSchema colorSchema = new TextColorSchemaImpl();


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();

        double imageRatio = (double) imageWidth / imageHeight;
        if (maxRatio != 0 && imageRatio < maxRatio) {
            throw new BadImageSizeException(imageRatio, maxRatio);
        }

        double sizeChangeFactor = calculateSizeChangeFactor(imageWidth, imageHeight);

        int newWidth = (int) (img.getWidth() / sizeChangeFactor);
        int newHeight = (int) (img.getHeight() / sizeChangeFactor);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        return imageToString(bwImg);
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        colorSchema = schema;
    }

    private double calculateSizeChangeFactor(int imageWidth, int imageHeight) {
        double sizeChangeFactorWidth = 1;
        double sizeChangeFactorHeight = 1;

        if (width != 0 && imageWidth > width) {
            sizeChangeFactorWidth = (double) imageWidth / width;
        }
        if (height != 0 && imageHeight > height) {
            sizeChangeFactorHeight = (double) imageHeight / height;
        }

        return Math.max(sizeChangeFactorWidth, sizeChangeFactorHeight);

    }

    private String imageToString(BufferedImage bwImg) {
        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder stringBuilder = new StringBuilder();
        for (int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = colorSchema.convert(color);
                stringBuilder
                        .append(c)
                        .append(c);
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

}
