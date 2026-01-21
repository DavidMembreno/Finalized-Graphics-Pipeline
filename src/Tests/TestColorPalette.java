package Tests;

import Models.*;
import Tools.ReadWriteImage;

import java.io.IOException;

public class TestColorPalette {
    public static void main(String[] args) {
        int width = 512;
        int height = 512;
        int boxSize = 30;

        ColorAbstract[] palette = {
                new Color(1, 0, 0),       // Red
                new Color(0, 1, 0),       // Green
                new Color(0, 0.6, 1),     // Blue
                new Color(1, 1, 0),       // Yellow
                new Color(0.6, 0, 1),     // Purple
                new Color(1, 1, 1),       // White
                new Color(1, 0.5, 0),     // Orange
                new Color(0.3, 1, 0.8),   // Mint
                new Color(1, 0.3, 0.6),   // Pink
                new Color(0.2, 0.9, 0.3), // Lime Green
                new Color(0.9, 0.6, 0),   // Amber
                new Color(0.5, 1, 1),     // Light Cyan
                new Color(0.8, 0.2, 1),   // Lavender
                new Color(0.5, 0.5, 0)    // Olive
        };

        int[][][] framebuffer = new int[3][height][width];

        Shader shader = new Shader();

        int cols = 3;
        int spacingX = 80;
        int spacingY = 80;
        int offsetX = 50;
        int offsetY = 50;

        for (int i = 0; i < palette.length; i++) {
            int col = i % cols;
            int row = i / cols;

            int x = offsetX + col * spacingX;
            int y = offsetY + row * spacingY;

            VectorAbstract v1 = new Vector(x, y, 0);
            VectorAbstract v2 = new Vector(x + boxSize, y, 0);
            VectorAbstract v3 = new Vector(x + boxSize / 2.0, y + boxSize, 0);

            v1.setColor((Color) palette[i]);
            v2.setColor((Color) palette[i]);
            v3.setColor((Color) palette[i]);

            Triangle triangle = new Triangle(v1, v2, v3);
            shader.solidFill(triangle, framebuffer);
        }

        try {
            ReadWriteImage.writeImage(framebuffer, "color_palette_test.png");
            System.out.println("Wrote color_palette_test.png");
        } catch (IOException e) {
            System.err.println("Failed to write image.");
        }
    }
}
