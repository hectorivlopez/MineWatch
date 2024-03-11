import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class DrawPanel extends JPanel {
    public int R;
    public int xc;
    public int yc;
    public int numPoints;
    public int sec;
    public int min;
    public int hour;
    public double skyAngle;
    public double sunMoonAngle;
    public double steve1Angle;
    public double steve2Angle;
    public double steve3Angle;
    public int steve1BlockAngle;
    public int steve2BlockAngle;
    public int steve3BlockAngle;
    public double angleArc;
    public double secAngleContinuous;
    public double minAngleContinuous;
    public double hourAngleContinuous;
    public int secAngle;
    public int milliAngle;
    public int minAngle;
    public int hourAngle;
    public int secAngleDiscret;
    public int minAngleDiscret;
    public int hourAngleDiscret;
    public int divAngle;
    public int fastAngleArc;
    public int fastAngle;
    public BufferedImage buffer;
    public BufferedImage bgBuffer;
    public BufferedImage planetBuffer;
    public Graphics gBuffer;
    public Graphics gBgBuffer;
    public Graphics gPlanetBuffer;
    public boolean slices;
    public boolean steve3;

    public int pickaxeMove;

    public DrawPanel(int width, int height) {
        setSize(width, height);

        this.R = (int) Math.sqrt(2 * Math.pow(width / 2, 2));
        this.xc = width / 2;
        this.yc = height / 2;
        this.numPoints = (int) (2 * Math.PI * this.R);

        this.sec = 0;
        this.min = 0;
        this.hour = 0;

        this.angleArc = (3 * numPoints / 4) - 2;
        this.secAngleContinuous = 264;
        this.minAngleContinuous = 264;
        this.hourAngleContinuous = 264;

        this.milliAngle = -(numPoints / 4);
        this.secAngle = 264;
        this.minAngle = 264;
        this.hourAngle = 264;

        this.secAngleDiscret = 264;
        this.minAngleDiscret = 264;
        this.hourAngleDiscret = 264;

        this.divAngle = 360 / 60;

        this.fastAngleArc = 0;
        this.fastAngle = 0;

        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.gBuffer = buffer.getGraphics();

        this.bgBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.gBgBuffer = bgBuffer.getGraphics();

        this.planetBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.gPlanetBuffer = planetBuffer.getGraphics();

        this.slices = true;
        this.steve3 = false;
        this.pickaxeMove = 0;
    }

    public void updateTime() {
        Calendar cal = Calendar.getInstance();

        // Fast angle
        fastAngleArc += 1; // speed
        if (fastAngleArc >= numPoints) fastAngleArc -= numPoints;
        fastAngle = (int) ((double) fastAngleArc * ((double) 360 / (double) numPoints));

        if(fastAngleArc % 10 == 0) {
            if(pickaxeMove == 0) pickaxeMove = 40;
            else pickaxeMove = 0;
        }

        // Continuous angle
        int milliSec = cal.get(Calendar.MILLISECOND) + cal.get(Calendar.SECOND) * 1000;

        angleArc = (((double) milliSec * numPoints / 60000) + ( 3 * (double) numPoints / 4));
        if (angleArc >= numPoints) angleArc -= numPoints;

        secAngleContinuous = (angleArc * (360 / (double) numPoints));
        if(secAngleContinuous >= 360) secAngleContinuous -= 360;


        // Millis angle
        int milli = cal.get(Calendar.MILLISECOND);

        milliAngle = (int) ((double) milli * (double) 360 / 1000);

        int startAnimationSpeed = 3;

        // Seconds angle
        if (sec != cal.get(Calendar.SECOND)) {
            sec = cal.get(Calendar.SECOND);

            secAngleDiscret = (int) ((double) sec * 360 / 60) + 264;
            if (secAngleDiscret >= 360) secAngleDiscret -= 360;

            int secMin = cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
            minAngleContinuous = (secMin * (360 / (double) 3600)) + 264;
            if(minAngleContinuous >= 360) minAngleContinuous -= 360;
        }

        if (secAngle != secAngleDiscret) {
            if(Math.abs(secAngleDiscret - secAngle) <= 6) {
                secAngle += 1;
            }
            else {
                secAngle += startAnimationSpeed;
            }

            if (secAngle >= 360) secAngle -= 360;
        }

        // Minutes and hour angles
        if (min != cal.get(Calendar.MINUTE)) {
            min = cal.get(Calendar.MINUTE);

            minAngleDiscret = (int) ((double) min * 360 / 60) + 264;
            if (minAngleDiscret >= 360) minAngleDiscret -= 360;

            hour = cal.get(Calendar.HOUR);
            hourAngleDiscret = (int) ((double) ((hour * 5) + (min / 12)) * 360 / 60) + 264;
            if (hourAngleDiscret >= 360) hourAngleDiscret -= 360;

            int minHour = cal.get(Calendar.HOUR) * 60 + cal.get(Calendar.MINUTE);
            hourAngleContinuous = (minHour * (360 / (double) 720)) + 264;
            if(hourAngleContinuous >= 360) hourAngleContinuous -= 360;
        }

        if (minAngle != minAngleDiscret) {
            if(Math.abs(minAngleDiscret - minAngle) <= 6) {
                minAngle += 1;
            }
            else {
                minAngle += startAnimationSpeed;
            }
            if (minAngle >= 360) minAngle -= 360;
        }

        if (hourAngle != hourAngleDiscret) {
            if(Math.abs(hourAngleDiscret - hourAngle) <= 6) {
                hourAngle += 1;
            }
            else {
                hourAngle += startAnimationSpeed;
            }
            if (hourAngle >= 360) hourAngle -= 360;
        }

        this.skyAngle = minAngleContinuous + 3;
        this.sunMoonAngle = angleArc;  // Don't change

        this.steve1Angle = secAngle;
        this.steve1BlockAngle = secAngleDiscret;

        this.steve2Angle = hourAngle;
        this.steve2BlockAngle = hourAngleDiscret;

        this.steve3Angle = secAngle;
        this.steve3BlockAngle = secAngleDiscret;


    }

    public int toArc(double angle, String angUnit) {
        if(angUnit.equals("R")) {
            return (int) (angle * (double) numPoints / (2 * Math.PI));
        }
        else  {
            return (int) (angle * (double) numPoints / 360);
        }
    }

    // Draw elements functions
    public void drawOak(BufferedImage buffer, int angle) {
        // Angle in circumference
        Color leavesColor = new Color(12, 116, 24);
        Color trunkColor = new Color(74, 59, 45);

        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));

        int xTrunk = xc + (int) (255 * cos);
        int yTrunk = yc + (int) (255 * sin);

        Line.fillRectSlope(xTrunk, yTrunk, 7, 40, angle, "D", trunkColor, buffer);

        // Leaves
        for (int i = 0; i < 14; i++) {
            Line.drawLineSlopeOrthogonal(xc + (int) ((280 + i) * cos), yc + (int) ((280 + i) * sin), 40, angle, "D", leavesColor, buffer);
        }
        for (int i = 0; i < 14; i++) {
            Line.drawLineSlopeOrthogonal(xc + (int) ((294 + i) * cos), yc + (int) ((294 + i) * sin), 20, angle, "D", leavesColor, buffer);
        }

    }

/*    public void drawBirch(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawSpruce(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawJungleTree(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawDarkOak(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawAcacia(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawCherryBlossom(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawCactus(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawMushroom(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawBadlandsMountain(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawSea(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }

    public void drawSwampOak(BufferedImage buffer, int arc) {
        double cos = Math.cos((double) arc / (double) R);
        double sin = Math.sin((double) arc / (double) R);

        int x = (int) (255 * cos);
        int y = (int) (255 * sin);

         boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

        Line.drawLineSlope(xc + x, yc + y, 30, Math.tan((double) arc / (double) R), Color.MAGENTA, buffer, reverse );

    }*/

    public void drawSteve(BufferedImage buffer, int layer, double angle) {
        // Angle in degrees
        int arc = (int) ((angle) * (double) numPoints / 360);

        Color shirtColor = new Color(20, 145, 222);
        Color diamondColor = new Color(75, 188, 255);
        Color pantsColor = new Color(20, 29, 222);
        Color skinColor = new Color(215, 176, 146);
        Color hairColor = new Color(69, 47, 29);
        Color pickaxeStickColor = new Color(100, 64, 46);

        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));

        int xPants = xc +  (int) (layer * cos);
        int yPants = yc + (int) (layer * sin);

        Line.fillRectSlope(xPants, yPants, 7, 13, angle, "D", pantsColor, buffer);

        int xShirt = xc + (int) ((layer + 11) * cos);
        int yShirt = yc + (int) ((layer + 11) * sin);

        Line.fillRectSlope(xShirt, yShirt, 7, 13, angle, "D", shirtColor, buffer);

        int xHead = xc + (int) ((layer + 22) * cos);
        int yHead = yc + (int) ((layer + 22) * sin);

        Line.fillRectSlope(xHead, yHead, 9, 7, angle, "D", skinColor, buffer);

        int xHair = xc + (int) ((layer + 27) * cos);
        int yHair = yc + (int) ((layer + 27) * sin);

        Line.fillRectSlope(xHair, yHair, 9, 3, angle, "D", hairColor, buffer);


        double orthogonalAngle = angle - 90;
        if(orthogonalAngle > 270) orthogonalAngle -= 360;
        int[] vector =  Line.calVector(4, orthogonalAngle, "D");

        int xBackHair = xHead + vector[0];
        int yBackHair = yHead + vector[1];

        Line.fillRectSlope(xBackHair, yBackHair,3, 8, angle, "D", hairColor, buffer);

        // Arm
        int xArm = xc + (int) ((layer + 17) * cos);
        int yArm = yc + (int) ((layer + 17) * sin);

        double armAngle = angle - pickaxeMove;

        int[] handPoints = Line.fillRectSlope(xArm, yArm, 4, -6, armAngle, "D", skinColor, buffer);


        // Pickaxe
        double pickaxeAngle = angle + 60 - pickaxeMove;
        if(pickaxeAngle > 270) pickaxeAngle -= 360;

        int[] pickaxeEnd = Line.drawLineSlope(handPoints[0], handPoints[1], 14, pickaxeAngle, "D", pickaxeStickColor, buffer);

        double diamond1Angle = pickaxeAngle + 135;
        if(diamond1Angle > 270) diamond1Angle -= 360;

        double diamond2Angle = diamond1Angle + 90;
        if(diamond2Angle > 270) diamond2Angle -= 360;

        Line.drawLineSlope(pickaxeEnd[0], pickaxeEnd[1], 6, diamond1Angle, "D", diamondColor, buffer);
        Line.drawLineSlope(pickaxeEnd[0], pickaxeEnd[1], 6, diamond2Angle, "D", diamondColor, buffer);
        Line.fillRectSlope(xArm, yArm, 4, -6, armAngle, "D", skinColor, buffer);
    }

    // Main drawing functions
    public void drawSky(Graphics g) {
        // ------------------------------ Background sky ------------------------------
        // Sky colors
        Color[] rainbowColors = new Color[60];
        for (int i = 0; i < 30; i++) {
            float hue = (float) (((double) i + 1) / 180) + (29 / 60.0f);
            //float brightness = (float) ((60 - i - 1) / 60.0f);
            float brightness = (float) (Math.abs(i - 35) / 35.0f); // Adjust this for smoother transitions
            rainbowColors[i] = Color.getHSBColor(hue, 1.0f, brightness);
        }
        for (int i = 30; i < 60; i++) {
            rainbowColors[i] = rainbowColors[60 - i - 1];
           /* float hue = (float) (((double) 60 - i) / 180) + (29 / 60.0f);
            //float brightness = (float) ((i) / 60.0f);
            float brightness = (float) (Math.abs(-i + 30) / 30.0f); // Adjust this for smoother transitions
            rainbowColors[i] = Color.getHSBColor(hue, 1.0f, brightness);*/
        }

        // Draw sky
        for (int i = 0; i < 60; i++) {
            g.setColor(rainbowColors[i]);
            g.fillArc(xc - R, yc - R, 2 * R, 2 * R, (-i * divAngle) - (int) skyAngle, -divAngle);
        }

        // Sun and moon
        int x = (int) (R * Math.cos(Math.toRadians((int) skyAngle)));
        int y = (int) (R * Math.sin(Math.toRadians((int) skyAngle)));

        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 35, new Color(116, 253, 241), bgBuffer);
        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 32, new Color(165, 248, 255), bgBuffer);
        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 29, new Color(191, 249, 251), bgBuffer);
        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 27, new Color(251, 245, 211), bgBuffer);
        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 26, new Color(240, 255, 188), bgBuffer);
        Shape.fillCircleRect(xc + (int) ((double) x * 7) / 10, yc + (int) ((double) y * 7) / 10, 22, new Color(250, 255, 98), bgBuffer);

        Shape.fillCircleRect(xc - (int) ((double) x * 7) / 10, yc - (int) ((double) y * 7) / 10, 30, new Color(115, 115, 115), bgBuffer);
        Shape.fillCircleRect(xc - (int) ((double) x * 7) / 10, yc - (int) ((double) y * 7) / 10, 29, new Color(155, 155, 155), bgBuffer);
        Shape.fillCircleRect(xc - (int) ((double) x * 7) / 10, yc - (int) ((double) y * 7) / 10, 27, new Color(215, 215, 215), bgBuffer);
        Shape.fillCircleRect(xc - (int) ((double) x * 7) / 10, yc - (int) ((double) y * 7) / 10, 26, new Color(235, 235, 235), bgBuffer);
        Shape.fillCircleRect(xc - (int) ((double) x * 7) / 10, yc - (int) ((double) y * 7) / 10, 24, new Color(255, 255, 255), bgBuffer);


        // Numbers
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(new Color(255, 255, 255));
        for (int i = 1; i <= 12; i++) {
            int divMarkAngle = (i + 9) * 30;

            int xDiv = (int) (R * Math.cos(Math.toRadians(divMarkAngle)));
            int yDiv = (int) (R * Math.sin(Math.toRadians(divMarkAngle)));

            int xCorrection = i < 10 ? 4 : 12;
            int yCorrection = 7;

            g.drawString(String.valueOf(i), xc - xCorrection + (int) ((double) xDiv * 25) / 40, yc + yCorrection + (int) ((double) yDiv * 25) / 40);
            //  g.drawString(String.valueOf(i), xc - xCorrection + (int) ((double) xDiv * 54) / 100, yc + yCorrection + (int) ((double) yDiv * 54) / 100);
        }

    }

    public void drawPlanet(Graphics g) {
        int blockHeight = 15;
        int r = 255;

        Color waterColor = new Color(24, 151, 193);

        Color sandColor = new Color(24, 151, 193);
        Color redSandColor = new Color(24, 151, 193);
        Color snowColor = new Color(24, 151, 193);


        Color grassColor = new Color(26, 148, 72);
        //Color dirtColor = new Color(113, 90, 81);
        Color dirtColor = new Color(146, 95, 73);
        Color stoneColor = new Color(130, 130, 130);
        Color obsidianColor = new Color(42, 33, 134);
        Color blackstoneColor = new Color(60, 60, 60);
        //Color blackstoneColor = obsidianColor;
        Color lavaColor = new Color(255, 135, 45);

        // Grass
        int rGrass = r;
        gPlanetBuffer.setColor(grassColor);
        gPlanetBuffer.fillOval(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass);

        // Dirt
        int rDirt = 251;
        gPlanetBuffer.setColor(dirtColor);
        gPlanetBuffer.fillOval(xc - rDirt, yc - rDirt, 2 * rDirt, 2 * rDirt);

        // Water slices
       /* gPlanetBuffer.setColor(waterColor);
        //gPlanetBuffer.fillArc(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass, -14 * divAngle, -3 * divAngle);
        gPlanetBuffer.fillArc(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass, -31 * divAngle, -3 * divAngle);
*/
        // Dirt
        rDirt = 240;
        gPlanetBuffer.setColor(dirtColor);
        gPlanetBuffer.fillOval(xc - rDirt, yc - rDirt, 2 * rDirt, 2 * rDirt);


        g.drawImage(planetBuffer, 0,0, this);

        // Stone slices
        if (slices) {
            g.setColor(stoneColor);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -3, -3 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -4 * divAngle - 3, -6 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -15 * divAngle - 3, -4 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -22 * divAngle - 3, -2 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -25 * divAngle - 3, -1 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -29 * divAngle - 3, -3 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -38 * divAngle - 3, -7 * divAngle);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, -49 * divAngle - 3, -6 * divAngle);
        }

        // Stone dynamic
        Color[] stoneColors = new Color[60];
        int stoneRGB = 90;

        for (int i = 0; i < 60; i++) {
            stoneColors[i] = i <= 40 ? new Color(stoneRGB + i, stoneRGB + i, stoneRGB + i) : stoneColor;
        }

        int rStoneDynamic = 225;
        for (int i = 0; i < 60; i++) {
            g.setColor(stoneColors[i]);
            g.fillArc(xc - rStoneDynamic, yc - rStoneDynamic, 2 * rStoneDynamic, 2 * rStoneDynamic, (i * divAngle) - steve1BlockAngle - 3, -divAngle);
        }

        // Stone
        int rStone = 195;
        g.setColor(stoneColor);
        g.fillOval(xc - rStone, yc - rStone, 2 * rStone, 2 * rStone);

        // Blackstone slices
        if (slices) {
            g.setColor(blackstoneColor);

            g.fillArc(xc - 165, yc - 165, 2 * 165, 2 * 165, 2 * divAngle - 3, -9 * divAngle);
            g.fillArc(xc - 165, yc - 165, 2 * 165, 2 * 165, -10 * divAngle - 3, -4 * divAngle);
            g.fillArc(xc - 165, yc - 165, 2 * 165, 2 * 165, -16 * divAngle - 3, -8 * divAngle);
            g.fillArc(xc - 165, yc - 165, 2 * 165, 2 * 165, -30 * divAngle - 3, -10 * divAngle);
            g.fillArc(xc - 165, yc - 165, 2 * 165, 2 * 165, -43 * divAngle - 3, -13 * divAngle);

            /*g.fillArc(xc - 180, yc - 180, 2 * 180, 2 * 180, -4 * divAngle - 3, -2 * divAngle);
            g.fillArc(xc - 180, yc - 180, 2 * 180, 2 * 180, -17 * divAngle - 3, -3 * divAngle);
            g.fillArc(xc - 180, yc - 180, 2 * 180, 2 * 180, -33 * divAngle - 3, -3 * divAngle);
            g.fillArc(xc - 180, yc - 180, 2 * 180, 2 * 180, -37 * divAngle - 3, -2 * divAngle);
            g.fillArc(xc - 180, yc - 180, 2 * 180, 2 * 180, -48 * divAngle - 3, -6 * divAngle);*/
        }

        // Blackstone dynamic
        Color[] blackstoneColors = new Color[60];
        int[] blackStoneRGB = {20, 20, 20};
        int[] obsidianRGB = {0, 0, 80};

        for (int i = 0; i < 60; i++) {
            blackstoneColors[i] = i <= 40 ? new Color(blackStoneRGB[0] + i, blackStoneRGB[1] + i, blackStoneRGB[2] + i) : blackstoneColor;
        }

        int rBlackstoneDynamic = 150;
        for (int i = 0; i < 60; i++) {
            g.setColor(blackstoneColors[i]);
            g.fillArc(xc - rBlackstoneDynamic, yc - rBlackstoneDynamic, 2 * rBlackstoneDynamic, 2 * rBlackstoneDynamic, (i * divAngle) - steve2BlockAngle - 3, -divAngle);
        }

        // Blackstone
        int rBlackstone = 120;
        g.setColor(blackstoneColor);
        g.fillOval(xc - rBlackstone, yc - rBlackstone, 2 * rBlackstone, 2 * rBlackstone);

        // Lava
        int rLava = 105;
        g.setColor(lavaColor);
        g.fillOval(xc - rLava, yc - rLava, 2 * rLava, 2 * rLava);

        // Nether
        int rNether = 90;
        g.setColor(new Color(86, 28, 28));
        g.fillOval(xc - rNether, yc - rNether, 2 * rNether, 2 * rNether);

        int rNetherBg = 75;
        g.setColor(new Color(55, 15, 15));
        g.fillOval(xc - rNetherBg, yc - rNetherBg, 2 * rNetherBg, 2 * rNetherBg);
    }

    public void grid(Graphics g) {
        Color gridColor = new Color(100, 100, 100);
        for (int i = 15; i <= 255; i += 15) {
            Shape.drawCirclePolar(xc, yc, i, gridColor, buffer);
        }

        g.setColor(gridColor);
        for (int i = 0; i < 60; i++) {
            int xDiv = (int) (R * Math.cos(Math.toRadians(i * 6)));
            int yDiv = (int) (R * Math.sin(Math.toRadians(i * 6)));

            g.drawLine(xc, yc, xc + xDiv, yc + yDiv);
        }
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);

        updateTime();

        drawSky(gBgBuffer);
        gBuffer.drawImage(bgBuffer, 0, 0, this);
        // Trees
        for (int i = 0; i < 12; i++) {
            drawOak(buffer, i * 30);
        }


        /*for (int i = 0; i < numPoints - (numPoints / 12); i++) {
            switch (i) {
                case 0:
                    drawOak(buffer, i * (numPoints / 12));
                    break;
                case 1:
                    drawBirch(buffer, i * (numPoints / 12));
                    break;
                case 2:
                    drawSpruce(buffer, i * (numPoints / 12));
                    break;
                case 3:
                    drawJungleTree(buffer, i * (numPoints / 12));
                    break;
                case 4:
                    drawAcacia(buffer, i * (numPoints / 12));
                    break;
                case 5:
                    drawDarkOak(buffer, i * (numPoints / 12));
                    break;
                case 6:
                    drawCherryBlossom(buffer, i * (numPoints / 12));
                    break;
                case 7:
                    drawCactus(buffer, i * (numPoints / 12));
                    break;
                case 8:
                    drawMushroom(buffer, i * (numPoints / 12));
                    break;
                case 9:
                    drawSea(buffer, i * (numPoints / 12));
                    break;
                case 10:
                    drawSwampOak(buffer, i * (numPoints / 12));
                    break;
                case 11:
                    drawBadlandsMountain(buffer, i * (numPoints / 12));
                    break;
            }

        }*/

        /*int cosa = -(numPoints / 4)  + (int) (angle * numPoints / 360);
        drawOak(buffer, cosa);*/


        drawPlanet(gBuffer);

        drawSteve(buffer, 195, steve1Angle + 6);
        drawSteve(buffer, 120, steve2Angle + 6);
        if(steve3) drawSteve(buffer, 45, steve3Angle + 6);



        //grid(gBuffer);

        g.drawImage(buffer, 0, 0, this);
    }
}