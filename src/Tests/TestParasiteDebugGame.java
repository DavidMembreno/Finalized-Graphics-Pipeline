package Tests;

import Models.*;
import Models.Color;
import Models.Vector;
import Tools.ReadWriteImage;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TestParasiteDebugGame {
    public static void main(String[] args) {
        int width = 512;
        int height = 512;
        int numParasites = 10;
        int steps = 100;
        int frameCount = 0;
        int maxFrames = 10;
        int collisionDelayFrames = 5;

        Pathmaker pathmaker = new Pathmaker(width, height);
        Shader shader = new Shader();
        Random rng = new Random();

        ArrayList<Parasite> parasites = new ArrayList<>();

        for (int i = 0; i < numParasites; i++) {
            VectorAbstract[] controlPoints = pathmaker.generateControlPoints();
            BezierCurve curve = new BezierCurve(controlPoints);
            ArrayList<VectorAbstract> path = curve.sample(steps);

            ColorAbstract color = new Color(rng.nextDouble(), rng.nextDouble(), rng.nextDouble());
            Parasite parasite = new Parasite(path, 6, color);
            parasites.add(parasite);

            System.out.println("Initialized Parasite " + i);
        }

        while (frameCount < maxFrames) {
            System.out.println("\n=== Frame " + frameCount + " ===");

            int[][][] framebuffer = new int[3][height][width];
            boolean anyAlive = false;
            int aliveCount = 0;

            // Update parasites and track positions
            for (int i = 0; i < parasites.size(); i++) {
                Parasite p = parasites.get(i);
                if (!p.isAlive()) {
                    System.out.println("Parasite " + i + " is dead.");
                    continue;
                }

                p.tick();
                if (!p.isFinished()) p.update();
                anyAlive = true;
            }

            // Collision detection after delay (using distance)
            if (frameCount >= collisionDelayFrames) {
                Set<Parasite> alreadyProcessed = new HashSet<>();

                for (int i = 0; i < parasites.size(); i++) {
                    Parasite p1 = parasites.get(i);
                    if (!p1.isAlive() || alreadyProcessed.contains(p1)) continue;

                    for (int j = i + 1; j < parasites.size(); j++) {
                        Parasite p2 = parasites.get(j);
                        if (!p2.isAlive() || alreadyProcessed.contains(p2)) continue;

                        double dx = p1.getPosition().getX() - p2.getPosition().getX();
                        double dy = p1.getPosition().getY() - p2.getPosition().getY();
                        double distSq = dx * dx + dy * dy;

                        if (distSq < 16.0) {
                            System.out.printf("Collision between Parasite %d and %d\n", i, j);
                            if (Math.random() < 0.5) {
                                p1.grow();
                                p2.die();
                                System.out.printf(" -> Parasite %d eats %d\n", i, j);
                            } else {
                                p2.grow();
                                p1.die();
                                System.out.printf(" -> Parasite %d eats %d\n", j, i);
                            }
                            alreadyProcessed.add(p1);
                            alreadyProcessed.add(p2);
                            break;
                        }
                    }
                }
            }

            // Draw debug red triangle and dot (early frames)
            if (frameCount < 5) {
                framebuffer[0][256][256] = 255;
                framebuffer[1][256][256] = 0;
                framebuffer[2][256][256] = 0;

                VectorAbstract v1 = new Vector(200, 200, 0);
                VectorAbstract v2 = new Vector(250, 300, 0);
                VectorAbstract v3 = new Vector(150, 300, 0);
                ColorAbstract red = new Color(1, 0, 0);
                v1.setColor((Color) red);
                v2.setColor((Color) red);
                v3.setColor((Color) red);
                Triangle tri = new Triangle(v1, v2, v3);
                shader.solidFill(tri, framebuffer);
            }

            // Render all alive parasites
            for (int i = 0; i < parasites.size(); i++) {
                Parasite p = parasites.get(i);
                if (p.isAlive()) {
                    Triangle tri = p.getTriangle();
                    shader.solidFill(tri, framebuffer);
                    aliveCount++;

                    VectorAbstract pos = p.getPosition();
                    System.out.printf("Parasite %d | Pos: (%.1f, %.1f) | Lifespan: %d\n",
                            i, pos.getX(), pos.getY(), p.getLifespan());

                    VectorAbstract[] verts = tri.getVertices();
                    System.out.printf("  Triangle: (%.1f, %.1f), (%.1f, %.1f), (%.1f, %.1f)\n",
                            verts[0].getX(), verts[0].getY(),
                            verts[1].getX(), verts[1].getY(),
                            verts[2].getX(), verts[2].getY());
                }
            }

            String filename = String.format("frame%03d.png", frameCount++);
            try {
                ReadWriteImage.writeImage(framebuffer, filename);
                System.out.println("Saved: " + filename + " | Alive: " + aliveCount);
            } catch (IOException e) {
                System.err.println("Failed to save frame: " + filename);
            }

            if (!anyAlive) {
                System.out.println("No parasites left alive. Ending early.");
                break;
            }
        }

        System.out.println("Debug session complete.");
    }
}
