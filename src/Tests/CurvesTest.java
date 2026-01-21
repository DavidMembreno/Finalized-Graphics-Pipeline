package Tests;
import Models.*;
import Tools.ReadWriteImage;

import java.io.IOException;
import java.util.ArrayList;

public class CurvesTest {
    public static void main(String[] args) {
        int[][][] framebuffer = new int[3][800][800]; // RGB framebuffer

        ScanConvertLine scan = new ScanConvertLine();

        ArrayList<VectorAbstract> hermitePoints = new ArrayList<>();
        ArrayList<VectorAbstract> bezierPoints = new ArrayList<>();

        // Hermite setup: b0, b1 = endpoints; r0, r1 = tangent vectors
        Curves.generateHermite(
                new Vector(100, 500, 0),//start
                new Vector(400, 100, 0),//end
                new Vector(150, 500, 0),//tangent at start
                new Vector(150, -300, 0),//tangent at end
                hermitePoints
        );

        // Bezier setup: p0 & p3 = endpoints, p1 & p2 = control points
        Curves.generateBezier(
                new Vector(100, 500, 0),//start
                new Vector(200, 100, 0),//knot 1
                new Vector(200, 300, 0),//knot 2
                new Vector(400, 100, 0),//end
                bezierPoints
        );

        // Render Hermite curve (red)
        for (int i = 0; i < hermitePoints.size() - 1; i++) {
            VectorAbstract a = hermitePoints.get(i);
            VectorAbstract b = hermitePoints.get(i + 1);
            scan.bresenham((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY(), new Color(1, 0, 0), new Color(1, 0, 0), framebuffer);
        }

        // Render Bezier curve (green)
        for (int i = 0; i < bezierPoints.size() - 1; i++) {
            VectorAbstract a = bezierPoints.get(i);
            VectorAbstract b = bezierPoints.get(i + 1);
            scan.bresenham((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY(), new Color(0, 1, 0), new Color(0, 1, 0), framebuffer);
        }

        // Save framebuffer to PNG
        try{
            ReadWriteImage.writeImage(framebuffer,"curves.png");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
