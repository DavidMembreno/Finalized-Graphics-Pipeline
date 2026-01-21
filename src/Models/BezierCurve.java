package Models;

import java.util.ArrayList;

public class BezierCurve {
    private VectorAbstract[] cp;

    public BezierCurve(VectorAbstract[] controlPoints)//BezierCurve method takes in a  list of type VectorAbstract for the control points (always must be a length of 4)
    {
        if (controlPoints.length != 4)
            throw new IllegalArgumentException("Must be 4 control points for cubic Bézier");
        this.cp = controlPoints;
    }

    public VectorAbstract getPoint(double t) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        return cp[0].mult(uuu)
                .add(cp[1].mult(3 * uu * t))
                .add(cp[2].mult(3 * u * tt))
                .add(cp[3].mult(ttt));
    }

    public ArrayList<VectorAbstract> sample(int steps) {
        ArrayList<VectorAbstract> path = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            path.add(getPoint(t));
        }
        return path;
    }
}
