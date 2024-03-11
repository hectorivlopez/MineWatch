import java.awt.*;
import java.awt.image.BufferedImage;

public class Line {
    private static void draw(int x, int y, Color color, BufferedImage buffer) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    private static double circunf(String unit) {
        return unit.equals("R") ? 2 * Math.PI : 360;
    }

    public static int[] calVector(int length, double angle, String angUnit) {
        if (!angUnit.equals("R")) angle = Math.toRadians(angle);
        while (angle < 0) angle += (2 * Math.PI);

        double m = Math.tan(angle);
        int dx, dy;

        if (Math.abs(m) < 1) {
            int xDirection = angle > (Math.PI / 2) && angle < (3 * Math.PI / 2) ? -1 : 1;

            dx = xDirection * (int) Math.round(length / Math.sqrt(1 + m * m));
            dy = (int) Math.round(m * dx);
        } else {
            int yDirection = angle > 0 && angle < Math.PI ? 1 : -1;

            dy = yDirection * (int) Math.round(length / Math.sqrt(1 + 1 / (m * m)));
            dx = (int) Math.round(dy / m);
        }

        return new int[]{dx, dy};
    }


    private static void drawThick(int x, int y, Color color, BufferedImage buffer) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
            buffer.setRGB(x + 1, y, color.getRGB());
            buffer.setRGB(x - 1, y, color.getRGB());
            buffer.setRGB(x, y + 1, color.getRGB());
            buffer.setRGB(x, y - 1, color.getRGB());
            // buffer.setRGB(x + 1, y + 1, color.getRGB());
            // buffer.setRGB(x + 1, y - 1, color.getRGB());
            //buffer.setRGB(x - 1, y + 1, color.getRGB());
            //buffer.setRGB(x - 1, y - 1, color.getRGB());
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, Color color, BufferedImage buffer) {
        double m = (double) (y2 - y1) / (x2 - x1);
        if ((x2 - x1) == 0) {
            m = 0;
        }

        int r = 20;
        int g = 255;
        int b = 50;

        // Evaluate x
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // Line color
            double colorRate = (double) (Math.abs(x2 - x1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            double y = y1;
            if (x2 > x1) {
                for (int x = x1; x <= x2; x++) {
                    Color gradColor = new Color(r, g, b);
                    draw(x, (int) y, color != null ? color : gradColor, buffer);

                    y += m;

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int x = x1; x >= x2; x--) {
                    Color gradColor = new Color(r, g, b);
                    draw(x, (int) y, color != null ? color : gradColor, buffer);

                    y -= m;

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }

        // Evaluate y
        else {
            // Line color
            double colorRate = (double) (Math.abs(y2 - y1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            double x = x1;
            if (y2 > y1) {
                for (int y = y1; y <= y2; y++) {
                    Color gradColor = new Color(r, g, b);
                    draw((int) x, y, color != null ? color : gradColor, buffer);

                    if (m != 0) {
                        x += 1 / m;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int y = y1; y >= y2; y--) {
                    Color gradColor = new Color(r, g, b);
                    draw((int) x, y, color != null ? color : gradColor, buffer);

                    if (m != 0) {
                        x -= 1 / m;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }

    }


    public static void drawLineThick(int x1, int y1, int x2, int y2, Color color, BufferedImage buffer) {
        double m = (double) (y2 - y1) / (x2 - x1);
        if ((x2 - x1) == 0) {
            m = 0;
        }

        int r = 20;
        int g = 255;
        int b = 50;

        // Evaluate x
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // Line color
            double colorRate = (double) (Math.abs(x2 - x1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            double y = y1;
            if (x2 > x1) {
                for (int x = x1; x <= x2; x++) {
                    Color gradColor = new Color(r, g, b);
                    drawThick(x, (int) y, color != null ? color : gradColor, buffer);

                    y += m;

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int x = x1; x >= x2; x--) {
                    Color gradColor = new Color(r, g, b);
                    drawThick(x, (int) y, color != null ? color : gradColor, buffer);

                    y -= m;

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }

        // Evaluate y
        else {
            // Line color
            double colorRate = (double) (Math.abs(y2 - y1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            double x = x1;
            if (y2 > y1) {
                for (int y = y1; y <= y2; y++) {
                    Color gradColor = new Color(r, g, b);
                    drawThick((int) x, y, color != null ? color : gradColor, buffer);

                    if (m != 0) {
                        x += 1 / m;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int y = y1; y >= y2; y--) {
                    Color gradColor = new Color(r, g, b);
                    drawThick((int) x, y, color != null ? color : gradColor, buffer);

                    if (m != 0) {
                        x -= 1 / m;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }

    }


    public static int[] drawLineSlope(int x0, int y0, int length, double angle, String angUnit, Color color, BufferedImage buffer) {
        // Calculate the direction vectors for the lines
        int[] vector = calVector(length, angle, angUnit);

        drawLineThick(x0, y0, x0 + vector[0], y0 + vector[1], color, buffer);

        return new int[] {x0 + vector[0], y0 + vector[1]};
    }

    public static void drawLineSlopeOrthogonal(int x0, int y0, int length, double angle, String angUnit, Color color, BufferedImage buffer) {
        if(angUnit.equals("D") && angle  > 270) angle -= 360;

        int halfLength = length / 2;

        int[] vector1 = calVector(halfLength, angle + (circunf(angUnit) / 4), angUnit);
        int[] vector2 = calVector(halfLength, angle - (circunf(angUnit) / 4), angUnit);

        drawLineThick(x0, y0, x0 + vector1[0], y0 + vector1[1], color, buffer);
        drawLineThick(x0, y0, x0 + vector2[0], y0 + vector2[1], color, buffer);
    }


    public static int[] fillRectSlope(int xc, int yc, int width, int height, double angle, String angUnit, Color color, BufferedImage buffer) {
        if(angUnit.equals("D") && angle  > 270) angle -= 360;

        int[] vector1 = calVector(width / 2, angle + (circunf(angUnit) / 4), angUnit);
        int[] vector2 = calVector(width / 2, angle - (circunf(angUnit) / 4), angUnit);
        int[] vector3 = calVector(height, angle, angUnit);

        //System.out.println(Math.toRadians(angle));
        int[] xPoints = {xc + vector1[0], xc + vector2[0], xc + vector2[0] + vector3[0], xc + vector1[0] + vector3[0]};
        int[] yPoints = {yc + vector1[1], yc + vector2[1], yc + vector2[1] + vector3[1], yc + vector1[1] + vector3[1]};

        /*Shape.fillCircleRect(xc, yc, 2, Color.red, buffer);

        Shape.drawCircle(xc + vector1[0], yc + vector1[1], 1, Color.red, buffer);
        Shape.drawCircle(xc + vector2[0], yc + vector2[1], 1, Color.magenta, buffer);
        Shape.drawCircle(xc + vector2[0] + vector3[0],yc + vector2[1] + vector3[1], 1, Color.WHITE, buffer);
        Shape.drawCircle(xc + vector1[0] + vector3[0],yc + vector1[1] + vector3[1], 1, Color.black, buffer);*/
        Graphics g = buffer.getGraphics();
        g.setColor(color);

        g.fillPolygon(xPoints, yPoints, 4);

        return new int[] {xc + vector3[0], yc + vector3[1]};
    }


    public static void drawLineMedium(int x1, int y1, int x2, int y2, BufferedImage buffer) {
        int r = 20;
        int g = 255;
        int b = 50;

        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);

        // Evaluate x
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // Line color
            double colorRate = (double) (Math.abs(x2 - x1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            int signum = (int) Math.signum(y2 - y1);

            int y = y1;

            double d = dy - dx / 2;
            double dE = dy;
            double dNE = (dy - dx);

            if (x2 > x1) {
                for (int x = x1; x <= x2; x++) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (d <= 0) {
                        d += dE;
                    } else {
                        d += dNE;
                        y += signum;
                    }

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int x = x1; x >= x2; x--) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (d <= 0) {
                        d += dE;
                    } else {
                        d += dNE;
                        y += signum;
                    }

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }
        // Evaluate y
        else {
            // Line color
            double colorRate = (double) (Math.abs(y2 - y1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            int x = x1;

            int signum = (int) Math.signum(x2 - x1);

            double d = dx - dy / 2;
            double dE = dx;
            double dNE = (dx - dy);

            if (y2 >= y1) {
                for (int y = y1; y <= y2; y++) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (d <= 0) {
                        d += dE;
                    } else {
                        d += dNE;
                        x += signum;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int y = y1; y >= y2; y--) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (d <= 0) {
                        d += dE;
                    } else {
                        d += dNE;
                        x += signum;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }

    }

    public static void drawLineBresenham(int x1, int y1, int x2, int y2, BufferedImage buffer) {
        int r = 20;
        int g = 255;
        int b = 50;

        double dx = Math.abs(x2 - x1);
        double dy = Math.abs(y2 - y1);

        // Evaluate x
        if (Math.abs(x2 - x1) >= Math.abs(y2 - y1)) {
            // Line color
            double colorRate = (double) (Math.abs(x2 - x1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            int signum = (int) Math.signum(y2 - y1);

            int y = y1;

            double pk = 2 * dy - dx;
            double dE = 2 * dy;
            double dNE = 2 * (dy - dx);

            if (x2 > x1) {
                for (int x = x1; x <= x2; x++) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (pk <= 0) {
                        pk += dE;
                    } else {
                        pk += dNE;
                        y += signum;
                    }

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int x = x1; x >= x2; x--) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (pk <= 0) {
                        pk += dE;
                    } else {
                        pk += dNE;
                        y += signum;
                    }

                    if (x % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }
        // Evaluate y
        else {
            // Line color
            double colorRate = (double) (Math.abs(y2 - y1)) / 205;
            int colorIncrement = 1;
            if (colorRate < 1) {
                colorIncrement = (int) Math.floor(1 / colorRate);
                colorRate = 1;
            } else {
                colorRate = Math.ceil(colorRate);
            }

            int x = x1;

            int signum = (int) Math.signum(x2 - x1);

            double pk = 2 * dx - dy;
            double dE = 2 * dx;
            double dNE = 2 * (dx - dy);

            if (y2 >= y1) {
                for (int y = y1; y <= y2; y++) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (pk <= 0) {
                        pk += dE;
                    } else {
                        pk += dNE;
                        x += signum;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            } else {
                for (int y = y1; y >= y2; y--) {
                    Color color = new Color(r, g, b);
                    draw(x, y, color, buffer);

                    if (pk <= 0) {
                        pk += dE;
                    } else {
                        pk += dNE;
                        x += signum;
                    }

                    if (y % (int) colorRate == 0) {
                        b += colorIncrement;
                        r += colorIncrement;
                        g -= colorIncrement;
                    }
                }
            }
        }
    }
}
