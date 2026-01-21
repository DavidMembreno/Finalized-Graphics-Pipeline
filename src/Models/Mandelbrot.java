package Models;

import Tools.ReadWriteImage;

import java.io.IOException;
import java.util.ArrayList;

public class Mandelbrot
{
    private final int width = 800;
    private final int height = 800;
    private final int maxIter = 255;
    private int[][] frameBuffer = new int[height][width];

    public Mandelbrot()
    {}

    public Mandelbrot generate()
    {
        double realMin = -2.5, realMax = 1.5, imagMin = -2.0, imagMax = 2.0; //Just setting the bounds, we can change the coordinate to shrink them in

        int i;
        int j;

        for(i = 0; i < height; i++)
        {
            for(j = 0; j < width; j++)
            {
                double real = realMin + (i / (double) width) * (realMax - realMin);
                double imag = imagMax - (j / (double) height) * (imagMax - imagMin); // invert y
// Map pixel (i, j) to a complex number c:
// - i/width gives a value from 0 to 1 across the image horizontally
// - j/height gives a value from 0 to 1 vertically
// - Multiply by (max - min) to scale it to the complex plane’s range
// - For imag: subtract from imagMax to flip image vertically (so top = max)
                ComplexNumber c = new ComplexNumber(real, imag);
                ComplexNumber z = new ComplexNumber(0, 0);

                int count = 0;
                while (z.mag() <= 2.0 && count < maxIter) {
                    z = z.mult(z).add(c);
                    count++;
                }

                frameBuffer[i][j] = count;
            }
    }


        return this;
    }



    public Mandelbrot renderLUT() {
        int[][][] image = new int[3][height][width];  // [RGB][y][x]

        // Create Bezier curves for each color channel
        ArrayList<VectorAbstract> red = new ArrayList<>();
        ArrayList<VectorAbstract> green = new ArrayList<>();
        ArrayList<VectorAbstract> blue = new ArrayList<>();

        Curves.generateBezier(
                new Vector(0, 128, 0),
                new Vector(128, 64, 0),
                new Vector(192, 32, 0),
                new Vector(255, 0, 0),
                red
        );


        Curves.generateBezier(
                new Vector(0, 0, 0),
                new Vector(128, 64, 0),
                new Vector(192, 200, 0),
                new Vector(255, 255, 0),
                green
        );


        Curves.generateBezier(
                new Vector(0, 255, 0),
                new Vector(64, 128, 0),
                new Vector(192, 32, 0),
                new Vector(255, 255, 0),
                blue
        );






        int[][] LUT = new int[256][3];

        for (int i = 0; i < red.size(); i++) {
            VectorAbstract v = red.get(i);
            int x = (int) v.getX();
            if (x >= 0 && x < 256) {
                LUT[x][0] = (int) v.getY();
            }
        }

        for (int i = 0; i < green.size(); i++) {
            VectorAbstract v = green.get(i);
            int x = (int) v.getX();
            if (x >= 0 && x < 256) {
                LUT[x][1] = (int) v.getY();
            }
        }

        for (int i = 0; i < blue.size(); i++) {
            VectorAbstract v = blue.get(i);
            int x = (int) v.getX();
            if (x >= 0 && x < 256) {
                LUT[x][2] = (int) v.getY();
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = Math.min(255, frameBuffer[y][x]);

                image[0][y][x] = LUT[value][0]; // Red
                image[1][y][x] = LUT[value][1]; // Green
                image[2][y][x] = LUT[value][2]; // Blue
            }
        }


        try {
            ReadWriteImage.writeImage(image, "LUTmandelbrot.png");
            System.out.println("Mandelbrot LUT image saved using ReadWriteImage.");
        } catch (IOException e) {
            System.err.println("Failed to save image: " + e.getMessage());
        }

        return this;
    }

    public Mandelbrot render()
    {
        int[][][] image = new int[3][height][width];  // [RGB][y][x]

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = frameBuffer[y][x];

                image[0][y][x] = value; // Red
                image[1][y][x] = value; // Green
                image[2][y][x] = value; // Blue
            }
        }

        try {
            ReadWriteImage.writeImage(image, "mandelbrot.png");
            System.out.println("Mandelbrot image saved using ReadWriteImage.");
        } catch (IOException e) {
            System.err.println("Failed to save image: " + e.getMessage());
        }

        return this;
    }
}
