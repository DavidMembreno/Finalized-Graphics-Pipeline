package Models;

public class Shader extends ShaderAbstract {

    @Override
    public void solidFill(TriangleAbstract tri, int[][][] framebuffer) {
        VectorAbstract[] v = tri.getVertices();
        ColorAbstract color = v[0].getColor();
        int[] rgb = color.scale(255);

        // Bounding box
        int minX = (int) Math.max(0, Math.floor(Math.min(Math.min(v[0].getX(), v[1].getX()), v[2].getX())));
        int maxX = (int) Math.min(framebuffer[0][0].length - 1, Math.ceil(Math.max(Math.max(v[0].getX(), v[1].getX()), v[2].getX())));
        int minY = (int) Math.max(0, Math.floor(Math.min(Math.min(v[0].getY(), v[1].getY()), v[2].getY())));
        int maxY = (int) Math.min(framebuffer[0].length - 1, Math.ceil(Math.max(Math.max(v[0].getY(), v[1].getY()), v[2].getY())));

        // Barycentric helpers
        double x0 = v[0].getX(), y0 = v[0].getY();
        double x1 = v[1].getX(), y1 = v[1].getY();
        double x2 = v[2].getX(), y2 = v[2].getY();

        double denom = (y1 - y2)*(x0 - x2) + (x2 - x1)*(y0 - y2);

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double lambda1 = ((y1 - y2)*(x - x2) + (x2 - x1)*(y - y2)) / denom;
                double lambda2 = ((y2 - y0)*(x - x2) + (x0 - x2)*(y - y2)) / denom;
                double lambda3 = 1.0 - lambda1 - lambda2;

                if (lambda1 >= 0 && lambda2 >= 0 && lambda3 >= 0) {
                    framebuffer[0][y][x] = rgb[0];
                    framebuffer[1][y][x] = rgb[1];
                    framebuffer[2][y][x] = rgb[2];
                }
            }
        }
    }


    @Override
    public void shadeFill(TriangleAbstract tri, int[][][] framebuffer) {


        // Get triangle vertices
        VectorAbstract[] vertices = tri.getVertices();

        int x0 = (int) vertices[0].getX();
        int y0 = (int) vertices[0].getY();
        int x1 = (int) vertices[1].getX();
        int y1 = (int) vertices[1].getY();
        int x2 = (int) vertices[2].getX();
        int y2 = (int) vertices[2].getY();





        ColorAbstract c0 = vertices[0].getColor();
        ColorAbstract c1 = vertices[1].getColor();
        ColorAbstract c2 = vertices[2].getColor();

        ScanConvertLine line = new ScanConvertLine();

        // Create a working buffer initialized with -1
        int[][][] workBuffer = new int[framebuffer.length][framebuffer[0].length][framebuffer[0][0].length];
        for (int i = 0; i < workBuffer.length; i++) {
            for (int j = 0; j < workBuffer[0].length; j++) {
                for (int k = 0; k < workBuffer[0][0].length; k++) {
                    workBuffer[i][j][k] = -1;
                }
            }
        }

        // Draw edges with color interpolation
        line.bresenham(x0, y0, x1, y1, c0, c1, workBuffer);
        line.bresenham(x1, y1, x2, y2, c1, c2, workBuffer);
        line.bresenham(x2, y2, x0, y0, c2, c0, workBuffer);

        // Scanline fill with interpolated colors
        for (int y = 0; y < workBuffer[0].length; y++) {
            int leftX = -1, rightX = -1;
            ColorAbstract leftColor = null, rightColor = null;

            // Find left and right bounds with color interpolation
            for (int x = 0; x < workBuffer[0][0].length; x++) {
                if (workBuffer[0][y][x] != -1) {
                    leftX = x;
                    leftColor = new Color(
                            workBuffer[0][y][x] / 255.0,
                            workBuffer[1][y][x] / 255.0,
                            workBuffer[2][y][x] / 255.0);
                    break;
                }
            }

            for (int x = workBuffer[0][0].length - 1; x >= 0; x--) {
                if (workBuffer[0][y][x] != -1) {
                    rightX = x;
                    rightColor = new Color(
                            workBuffer[0][y][x] / 255.0,
                            workBuffer[1][y][x] / 255.0,
                            workBuffer[2][y][x] / 255.0
                    );
                    break;
                }
            }

            // Interpolate colors along the scanline
            if (leftX != -1 && rightX != -1 && leftX <= rightX) {
                for (int x = leftX; x <= rightX; x++) {
                    double t = (double)(x - leftX) / (rightX - leftX);

                    int r = (int) ((leftColor.getR() + t * (rightColor.getR() - leftColor.getR())) * 255);
                    int g = (int) ((leftColor.getG() + t * (rightColor.getG() - leftColor.getG())) * 255);
                    int b = (int) ((leftColor.getB() + t * (rightColor.getB() - leftColor.getB())) * 255);

                    framebuffer[0][y][x] = r;
                    framebuffer[1][y][x] = g;
                    framebuffer[2][y][x] = b;
                }
            }
        }
    }


}
