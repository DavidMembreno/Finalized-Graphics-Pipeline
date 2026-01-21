package Models;

import java.util.ArrayList;

public class Particle {
    protected VectorAbstract position;
    protected double size;
    protected ColorAbstract color;
    protected int currentStep;

    protected ArrayList<VectorAbstract> path;

    public Particle(ArrayList<VectorAbstract> path, double size, ColorAbstract color) {
        this.path = path;
        this.size = size;
        this.color = color;
        this.currentStep = 0;
        this.position = path.get(0);
    }

    public void update() {
        if (currentStep < path.size() - 1) {
            currentStep++;
            position = path.get(currentStep);
        }
    }

    public Triangle getTriangle() {
        double x = position.getX();
        double y = position.getY();

        VectorAbstract v1 = new Vector(x, y - size, 0);
        VectorAbstract v2 = new Vector(x - size, y + size, 0);
        VectorAbstract v3 = new Vector(x + size, y + size, 0);

        v1.setColor((Color) color);
        v2.setColor((Color) color);
        v3.setColor((Color) color);

        return new Triangle(v1, v2, v3);
    }

    public boolean isFinished() {
        return currentStep >= path.size() - 1;
    }
    public VectorAbstract getPosition() {
        return position;
    }

}
