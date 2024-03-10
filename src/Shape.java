import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

public class Shape {

    private static void draw(int x, int y, Color color, BufferedImage buffer) {
        if(x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    public static void drawRectangle(int x1, int y1, int x2, int y2, Color color, BufferedImage buffer) {
        if (x1 > x2) {
            int xAux = x1;
            x1 = x2;
            x2 = xAux;
        }
        if (y1 > y2) {
            int yAux = y1;
            y1 = y2;
            y2 = yAux;
        }

        for (int x = x1; x < x2; x++) {
            draw(x, y1, color, buffer);
            draw(x, y2, color, buffer);
        }

        for (int y = y1; y < y2; y++) {
            draw(x1, y, color, buffer);
            draw(x2, y, color, buffer);
        }
    }

    public static void drawCircle(int xc, int yc, int r, Color color, BufferedImage buffer) {
        for (int x = xc - r; x < xc + r; x++) {
            int ySup = yc + (int) Math.sqrt((Math.pow(r, 2) - Math.pow(x - xc, 2)));
            int yInf = yc - (int) Math.sqrt((Math.pow(r, 2) - Math.pow(x - xc, 2)));

            draw(x, ySup, color, buffer);
            draw(x, yInf, color, buffer);
        }
        for (int y = yc - r; y < yc + r; y++) {
            int xSup = xc + (int) Math.sqrt((Math.pow(r, 2) - Math.pow(y - yc, 2)));
            int xInf = xc - (int) Math.sqrt((Math.pow(r, 2) - Math.pow(y - yc, 2)));

            draw(xSup, y, color, buffer);
            draw(xInf, y, color, buffer);
        }
    }

    public static void fillCircleRect(int xc, int yc, int r, Color color, BufferedImage buffer) {
        for (int x = xc - r; x < xc + r; x++) {
            int ySup = yc + (int) Math.sqrt((Math.pow(r, 2) - Math.pow(x - xc, 2)));
            int yInf = yc - (int) Math.sqrt((Math.pow(r, 2) - Math.pow(x - xc, 2)));

            draw(x, ySup, color, buffer);
            draw(x, yInf, color, buffer);
        }
        for (int y = yc - r; y < yc + r; y++) {
            int xSup = xc + (int) Math.sqrt((Math.pow(r, 2) - Math.pow(y - yc, 2)));
            int xInf = xc - (int) Math.sqrt((Math.pow(r, 2) - Math.pow(y - yc, 2)));

            draw(xSup, y, color, buffer);
            draw(xInf, y, color, buffer);
        }

        floodFill(buffer, xc,yc,color);

    }

    public static void drawCirclePolar(int xc, int yc, int r, Color color, BufferedImage buffer) {
        int numPoints = (int) (2 * Math.PI * r); // Calculate number of points based on circumference

        int R = 255;
        int g = 255;
        int b = 50;

        double colorRate = (double) numPoints  / 205;
        int colorIncrement = 1;
        if (colorRate < 1) {
            colorIncrement = (int) Math.floor(1 / colorRate);
            colorRate = 1;
        } else {
            colorRate = Math.ceil(colorRate);
        }

        for (int i = 0; i < numPoints ; i++) {
            double t = (i * 2 * Math.PI) / numPoints;

            int x = xc + (int) (r * Math.cos(t));
            int y = yc + (int) (r * Math.sin(t));

            Color grad = new Color(R, g, b);

            draw(x, y, color != null ? color : grad, buffer);

            if (i % (int) colorRate == 0) {
                b += colorIncrement;
                g -= colorIncrement;
            }
        }
    }

    public static void fillCircle(int xc, int yc, int r, Color color, BufferedImage buffer) {
        int numPoints = (int) (2 * Math.PI * r); // Calculate number of points based on circumference


        for (int i = 0; i < numPoints ; i++) {
            double t = (i * 2 * Math.PI) / numPoints;

            int x = xc + (int) (r * Math.cos(t));
            int y = yc + (int) (r * Math.sin(t));

            draw(x, y, color, buffer);
        }

        //floodFill(buffer, xc,yc,Color.RED);
    }

    public static void floodFill(BufferedImage buffer, int x, int y, Color targetColor) {
        /*int currentColor = image.getRGB(x, y);

        // Base case: stop if the current pixel has a different color or is already filled
        if (currentColor == targetColor) {
            return;
        }

        // Change the color of the current pixel
        image.setRGB(x, y, targetColor);



        // Recursively fill neighboring pixels


        if (image.getRGB(x + 1, y) != targetColor) {
            floodFill(image, x + 1, y, targetColor);
        }
        if (image.getRGB(x - 1, y) != targetColor) {
            floodFill(image, x - 1, y, targetColor);
        }
        if (image.getRGB(x, y + 1) != targetColor) {
            floodFill(image, x, y + 1, targetColor);
        }
        if (image.getRGB(x, y - 1) != targetColor) {
            floodFill(image, x, y - 1, targetColor);
        }*/

        int originalColor = buffer.getRGB(x, y);

        if (originalColor == targetColor.getRGB()) {
            return;
        }

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] currentPixel = stack.pop();
            int px = currentPixel[0];
            int py = currentPixel[1];

            if (buffer.getRGB(px, py) != targetColor.getRGB()) {
                //buffer.setRGB(px, py, targetColor.getRGB());
                draw(px, py, targetColor, buffer);

                if(px + 1 >= 0 && px + 1 < buffer.getWidth() && py >= 0 && py < buffer.getHeight()) {
                    stack.push(new int[]{px + 1, py});
                }
                if(px - 1 >= 0 && px - 1 < buffer.getWidth() && py >= 0 && py < buffer.getHeight()) {
                    stack.push(new int[]{px - 1, py});
                }
                if(px >= 0 && px < buffer.getWidth() && py + 1 >= 0 && py + 1 < buffer.getHeight()) {
                    stack.push(new int[]{px, py + 1});
                }
                if(px >= 0 && px < buffer.getWidth() && py - 1 >= 0 && py - 1 < buffer.getHeight()) {
                    stack.push(new int[]{px, py - 1});
                }

            }
        }
    }
}
