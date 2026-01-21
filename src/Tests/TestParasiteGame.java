package Tests;

import Models.*;
import Models.Color;
import Tools.ReadWriteImage;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TestParasiteGame {
    public static void main(String[] args) {
        int width = 512;
        int height = 512;
        int numParasites = 100;
        int steps = 150; // path length
        int frameCount = 0;

        Pathmaker pathmaker = new Pathmaker(width, height);
        Shader shader = new Shader();
        Random rng = new Random();

        ArrayList<Parasite> parasites = new ArrayList<>();

        // Initialize parasites
        for (int i = 0; i < numParasites; i++) {
            VectorAbstract[] controlPoints = pathmaker.generateControlPoints();
            BezierCurve curve = new BezierCurve(controlPoints);
            ArrayList<VectorAbstract> path = curve.sample(steps);

            double r = 0.5 + 0.5 * rng.nextDouble();
            double g = 0.5 + 0.5 * rng.nextDouble();
            double b = 0.5 + 0.5 * rng.nextDouble();

            ColorAbstract color = new Color(r, g, b);
            Parasite parasite = new Parasite(path, 4, color);
            parasites.add(parasite);
        }

        boolean anyAlive = true;
        while (anyAlive) {
            anyAlive = false;

            // Map of grid positions to parasites for collision detection
            Map<Point, List<Parasite>> positionMap = new HashMap<>();

            // Update and track positions
            for (Parasite p : parasites) {
                if (!p.isAlive()) continue;

                p.tick(); // age
                if (!p.isFinished()) p.update();

                int x = p.getGridX();
                int y = p.getGridY();
                Point pos = new Point(x, y);

                positionMap.computeIfAbsent(pos, k -> new ArrayList<>()).add(p);
            }

            // Handle collisions
            for (List<Parasite> occupants : positionMap.values()) {
                if (occupants.size() <= 1) continue;

                // Randomly pick one survivor
                Collections.shuffle(occupants, rng);
                Parasite winner = occupants.get(0);
                winner.grow();

                // Kill others
                for (int i = 1; i < occupants.size(); i++) {
                    occupants.get(i).die();
                }
            }

            // Render
            int[][][] framebuffer = new int[3][height][width];

            int aliveCount = 0;
            for (Parasite p : parasites) {
                if (p.isAlive()) {
                    Triangle tri = p.getTriangle();
                    shader.solidFill(tri, framebuffer);
                    anyAlive = true;
                    aliveCount++;
                }
            }

            // Save image
            String filename = String.format("frame%03d.png", frameCount++);
            try {
                ReadWriteImage.writeImage(framebuffer, filename);
                System.out.println("Frame " + frameCount + " | Alive: " + aliveCount + " | Saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error writing frame: " + filename);
            }
        }

        System.out.println("Simulation ended.");
    }
}
