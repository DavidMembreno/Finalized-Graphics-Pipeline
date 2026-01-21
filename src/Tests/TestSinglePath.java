package Tests;

import Models.*;
import Tools.ReadWriteImage;

import java.io.IOException;
import java.util.ArrayList;

public class TestSinglePath {
    public static void main(String[] args) {
        int width = 512;
        int height = 512;
        int steps = 100;

        // Generate control points and sample path
        Pathmaker pathmaker = new Pathmaker(width, height);
        VectorAbstract[] controlPoints = pathmaker.generateControlPoints();
        BezierCurve curve = new BezierCurve(controlPoints);
        ArrayList<VectorAbstract> path = curve.sample(steps);

        // Create particle
        Particle p = new Particle(path, 5, new Color(1, 0, 0));
        Shader shader = new Shader();

        int frameCount = 0;

        // Create a frame per step of the particle
        while (!p.isFinished()) {
            int[][][] framebuffer = new int[3][height][width]; // [RGB][y][x]

            p.update();
            Triangle tri = p.getTriangle();
            shader.solidFill(tri, framebuffer);

            String filename = String.format("frame%03d.png", frameCount++);
            try {
                ReadWriteImage.writeImage(framebuffer, filename);
                System.out.println("Wrote " + filename);
            } catch (IOException e) {
                System.err.println("Error writing image: " + e.getMessage());
            }
        }

        System.out.println("All frames generated.");
    }
}
