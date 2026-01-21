package Models;

import java.util.ArrayList;

public class Curves {

    public static void generateHermite(Vector b0, Vector b1, Vector r0, Vector r1, ArrayList<VectorAbstract> points) {
        for (int i = 0; i <= 100; i++) {
            double u = i / 100.0;
            double h1 = 2 * Math.pow(u, 3) - 3 * Math.pow(u, 2) + 1;
            double h2 = -2 * Math.pow(u, 3) + 3 * Math.pow(u, 2);
            double h3 = Math.pow(u, 3) - 2 * Math.pow(u, 2) + u;
            double h4 = Math.pow(u, 3) - Math.pow(u, 2);

            double x = h1 * b0.getX() + h2 * b1.getX() + h3 * r0.getX() + h4 * r1.getX();
            double y = h1 * b0.getY() + h2 * b1.getY() + h3 * r0.getY() + h4 * r1.getY();

            points.add(new Vector(Math.round(x), Math.round(y), 0));//round fixed floating point errors
        }
    }

    public static void generateBezier(Vector p0, Vector p1, Vector p2, Vector p3, ArrayList<VectorAbstract> points) {
        double ax = -p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX();
        double bx = 3 * p0.getX() - 6 * p1.getX() + 3 * p2.getX();
        double cx = -3 * p0.getX() + 3 * p1.getX();
        double dx = p0.getX();

        double ay = -p0.getY() + 3 * p1.getY() - 3 * p2.getY() + p3.getY();
        double by = 3 * p0.getY() - 6 * p1.getY() + 3 * p2.getY();
        double cy = -3 * p0.getY() + 3 * p1.getY();
        double dy = p0.getY();

        for (int i = 0; i <= 100; i++) {
            double u = i / 100.0;
            double x = ax * Math.pow(u, 3) + bx * Math.pow(u, 2) + cx * u + dx;
            double y = ay * Math.pow(u, 3) + by * Math.pow(u, 2) + cy * u + dy;

            points.add(new Vector(Math.round(x), Math.round(y), 0));
        }
    }
}
