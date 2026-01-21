package Models;

import java.util.ArrayList;

public class Parasite extends Particle {
    private boolean alive;
    private int lifespan;
    private int maxLifespan;
    private int growthCount;
    private boolean showTrail = false;
    private boolean enableCulling = false;

    public Parasite(ArrayList<VectorAbstract> path, double size, ColorAbstract color) {
        super(path, size, color);
        this.alive = true;
        this.maxLifespan = 150; // adjustable
        this.lifespan = maxLifespan;
        this.growthCount = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        this.alive = false;
    }

    public void grow() {
        this.size += 1.5;  // growth multiplier
        this.lifespan = maxLifespan; // reset lifespan on growth
        this.growthCount++;
    }
//Tick handles the lifespan, each time we call it the life of the parasite is reduced and once it reaches 0 it dies
    public void tick() {
        lifespan--;
        if (lifespan <= 0) {
            alive = false;
        }
    }

    // Expose current grid position
    public int getGridX() {
        return (int) Math.round(position.getX());
    }

    public int getGridY() {
        return (int) Math.round(position.getY());
    }

    public int getGrowthCount() {
        return growthCount;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int frames) {
        this.lifespan = frames;
        this.maxLifespan = frames;
    }

    public void setShowTrail(boolean show) {
        this.showTrail = show;
    }

    public boolean isTrailEnabled() {
        return showTrail;
    }

    public int getTrailLength() {
        return Math.min(currentStep, path.size());
    }

    public ArrayList<VectorAbstract> getTrailPoints() {
        return new ArrayList<>(path.subList(0, getTrailLength()));
    }

    // Cull if offscreen
    public boolean isOnScreen(int width, int height) {
        double x = position.getX();
        double y = position.getY();
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    public void setEnableCulling(boolean value) {
        this.enableCulling = value;
    }

    public boolean isCullingEnabled() {
        return enableCulling;
    }
}
