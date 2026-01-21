package Models;

import java.util.Random;

public class Pathmaker {
    private int width, height;
    private Random rng;

    public Pathmaker(int width, int height) {
        this.width = width;
        this.height = height;
        this.rng = new Random();
    }

    public VectorAbstract[] generateControlPoints() {
        VectorAbstract[] points = new VectorAbstract[4];
        for (int i = 0; i < 4; i++) {
            int x = rng.nextInt(width);
            int y = rng.nextInt(height);
            points[i] = new Vector(x, y, 0); // 2D point with Z = 0
        }
        return points;
    }
    public VectorAbstract[] generateControlPointsNear(VectorAbstract anchor) {
        Random rng = new Random();

        double dir = rng.nextDouble() * 2 * Math.PI; // random direction
        double stretch = 80 + rng.nextDouble() * 40; // more consistent length

        VectorAbstract[] cps = new VectorAbstract[4];
        cps[0] = anchor;

        for (int i = 1; i < 4; i++) {
            double angle = dir + rng.nextGaussian() * 0.4;  // slight bend
            double dist = stretch * (0.7 + rng.nextDouble() * 0.6); // varied stretch
            double dx = Math.cos(angle) * dist;
            double dy = Math.sin(angle) * dist;

            cps[i] = new Vector(anchor.getX() + dx, anchor.getY() + dy, 0);
        }

        return cps;
    }


}
