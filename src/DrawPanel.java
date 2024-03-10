import javax.swing.*;
import java.awt.*;
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
    public int angleRad;
    public int angle;
    public int secAngle;
    public int secAngleDiscret;
    public int minAngleDiscret;
    public int hourAngleDiscret;
    public int minAngle;
    public int hourAngle;
    public int divAngle;
    public int fastAngleRad;
    public int fastAngle;
    public BufferedImage buffer;
    public BufferedImage bgBuffer;
    public Graphics gBuffer;
    public Graphics gBgBuffer;
    public boolean slices;

    public DrawPanel(int width, int height) {
        setSize(width, height);

        this.R = (int) Math.sqrt(2 * Math.pow(width / 2, 2));
        this.xc = width / 2;
        this.yc = height / 2;
        this.numPoints = (int) (2 * Math.PI * this.R);

        this.sec = 0;
        this.min = 0;
        this.hour = 0;

        this.angleRad = (3 * numPoints / 4) - 2;
        this.angle = 264;

        this.secAngle = 264;
        this.minAngle = 264;
        this.hourAngle = 264;

        this.secAngleDiscret = 264;
        this.minAngleDiscret = 264;
        this.hourAngleDiscret = 264;

        this.divAngle = 360 / 60;

        this.fastAngleRad = 0;
        this.fastAngle = 0;

        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.gBuffer = buffer.getGraphics();

        this.bgBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.gBgBuffer = bgBuffer.getGraphics();

        this.slices = false;
    }
    public void updateTime() {
        Calendar cal = Calendar.getInstance();

        // Fast angle
        fastAngleRad += 1; // speed
        if(fastAngleRad >= numPoints) fastAngleRad -= numPoints;
        fastAngle = (int) ((double) fastAngleRad * ((double) 360 / (double) numPoints));

        // Continuous angle
        int mili = cal.get(Calendar.MILLISECOND) + cal.get(Calendar.SECOND) * 1000;

        angleRad = (int) (((double) mili * numPoints / 60000) + ((double) 3 * numPoints / 4));
        if (angleRad >= numPoints) angleRad -= numPoints;

        angle = (int) ((double) angleRad * ((double) 360 / (double) numPoints));

        // Seconds angle
        if (sec != cal.get(Calendar.SECOND)) {
            sec = cal.get(Calendar.SECOND);

            secAngleDiscret = (int) ((double) sec * 360 / 60) + 264;
            if (secAngleDiscret >= 360) secAngleDiscret -= 360;
        }

        if (secAngle != secAngleDiscret) {
            secAngle += 1;
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
        }

        if (minAngle != minAngleDiscret) {
            minAngle += 1;
            if (minAngle >= 360) minAngle -= 360;
        }

        if (hourAngle != hourAngleDiscret) {
            hourAngle += 1;
            if (hourAngle >= 360) hourAngle -= 360;
        }


       /* if(fastAngle % 10 == 0) {
            hourAngle += 1;
            if(hourAngle >= 360) hourAngle -= 360;
        }
        if(fastAngle % 20 == 0) {
            minAngle += 1;
            if(minAngle >= 360) minAngle -= 360;
        }*/
    }

    // Draw elements functions
    public void drawTree(BufferedImage buffer, int arc) {
        // Angle in circumference

        Color leavesColor = new Color(12, 116, 24);
        Color trunkColor = new Color(74, 59, 45);

        double cos = Math.cos((double) (arc) / ((double) R));
        double sin = Math.sin((double) (arc) / ((double) R));

        // Trunk
        for (int i = 0; i <= 14; i++) {
            double cosTrunk = Math.cos((double) (arc + i - 7) / ((double) R));
            double sinTrunk = Math.sin((double) (arc + i - 7) / ((double) R));

            int x1 = (int) (255 * cosTrunk);
            int y1 = (int) (255 * sinTrunk);

            boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

            Line.drawLineSlopeThick(xc + x1, yc + y1, 40, Math.tan((double) arc / (double) R), trunkColor, buffer, reverse);
        }

        // Leaves
        for (int i = 0; i < 14; i++) {
            Line.drawLineSlope(xc + (int) ((280 + i) * cos), yc + (int) ((280 + i) * sin), 40, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), leavesColor, buffer);
        }
        for (int i = 0; i < 14; i++) {
            Line.drawLineSlope(xc + (int) ((294 + i) * cos), yc + (int) ((294 + i) * sin), 20, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), leavesColor, buffer);
        }
    }

    public void drawSteve(BufferedImage buffer, int layer, int angle) {
        // Angle in degrees
        int arc = (int) ((double) (angle) * (double) numPoints / 360);

        Color shirtColor = new Color(20, 145, 222);
        Color diamondColor = new Color(75, 188, 255);
        Color pantsColor = new Color(20, 29, 222);
        Color skinColor = new Color(215, 176, 146);
        Color hairColor = new Color(69, 47, 29);
        Color pickaxeStickColor = new Color(100, 64, 46);

        // Pants and shirt
        for (int i = 0; i <= 14; i++) {
            double cos = Math.cos((double) (arc + i - 7) / ((double) R));
            double sin = Math.sin((double) (arc + i - 7) / ((double) R));

            int xPants = (int) (layer * cos);
            int yPants = (int) (layer * sin);

            int xShirt = (int) ((layer + 11) * cos);
            int yShirt = (int) ((layer + 11) * sin);

            boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

            Line.drawLineSlopeThick(xc + xPants, yc + yPants, 11, Math.tan((double) arc / (double) R), pantsColor, buffer, reverse);
            Line.drawLineSlopeThick(xc + xShirt, yc + yShirt, 11, Math.tan((double) arc / (double) R), shirtColor, buffer, reverse);
        }

        // Head
        if ((angle > 40 && angle < 140) || (angle > 220 && angle < 320)) {
            double cosHead = Math.cos((double) (arc) / ((double) R));
            double sinHead = Math.sin((double) (arc) / ((double) R));

            double cosBackHair = Math.cos((double) (arc - 10) / ((double) R));
            double sinBackHair = Math.sin((double) (arc - 10) / ((double) R));

            for (int i = 0; i < 8; i++) {
                if (i < 6) {
                    Line.drawLineSlope(xc + (int) ((layer + 22 + i) * cosHead), yc + (int) ((layer + 22 + i) * sinHead), 6, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), skinColor, buffer);
                } else {
                    Line.drawLineSlope(xc + (int) ((layer + 22 + i) * cosHead), yc + (int) ((layer + 22 + i) * sinHead), 6, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), hairColor, buffer);
                }
                Line.drawLineSlope(xc + (int) ((layer + 22 + i) * cosBackHair), yc + (int) ((layer + 22 + i) * sinBackHair), 2, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), hairColor, buffer);

            }
        } else {
            for (int i = 0; i <= 22; i++) {
                double cos = Math.cos((double) (arc + i - 11) / ((double) R));
                double sin = Math.sin((double) (arc + i - 11) / ((double) R));

                int xHead = (int) ((layer + 22) * cos);
                int yHead = (int) ((layer + 22) * sin);

                int backHair = i > 8 ? 0 : 7;
                boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

                Line.drawLineSlopeThick(xc + xHead, yc + yHead, 7, Math.tan((double) arc / (double) R), hairColor, buffer, reverse);

                if (i > 5) {
                    int xFace = (int) ((layer + 22) * cos);
                    int yFace = (int) ((layer + 22) * sin);

                    Line.drawLineSlopeThick(xc + xFace, yc + yFace, 5, Math.tan((double) arc / (double) R), skinColor, buffer, reverse);
                }
            }
        }

        // Pickaxe stick
        double cosPickaxe = Math.cos((double) (arc) / ((double) R));
        double sinPickaxe = Math.sin((double) (arc) / ((double) R));

        int xPickaxeHand = (int) ((layer + 11) * cosPickaxe);
        int yPickaxeHand = (int) ((layer + 11) * sinPickaxe);

        boolean reversePickaxe = angle >= 48 && angle < 228;
        double mPickaxe = Math.tan((arc + ((double) numPoints / 8)) / (double) R);

        Line.drawLineSlopeThick(xc + xPickaxeHand, yc + yPickaxeHand, 15, mPickaxe, pickaxeStickColor, buffer, reversePickaxe);

        // Pickaxe diamond
        int xPickaxeStick = 0;
        int yPickaxeStick = 0;

        if (Math.abs(mPickaxe) < 300) {
            // Calculate the direction vectors for the lines
            int dx = (int) Math.round(15 / Math.sqrt(1 + mPickaxe * mPickaxe));
            int dy = (int) Math.round(mPickaxe * dx);

            if (!reversePickaxe) {
                xPickaxeStick = xPickaxeHand + dx;
                yPickaxeStick = yPickaxeHand + dy;
            } else {
                xPickaxeStick = xPickaxeHand - dx;
                yPickaxeStick = yPickaxeHand - dy;
            }
        } else {
            if (!reversePickaxe) {
                xPickaxeStick = xPickaxeHand;
                yPickaxeStick = yPickaxeHand + 15;
            } else {
                xPickaxeStick = xPickaxeHand;
                yPickaxeStick = yPickaxeHand + 15;
            }
        }

        boolean reversePickaxe1 = angle >= 276 || angle < 96;
        boolean reversePickaxe2 = angle >= 186 || angle < 6;

        Line.drawLineSlopeThick(xc + xPickaxeStick, yc + yPickaxeStick, 7, Math.tan((double) arc / (double) R), diamondColor, buffer, reversePickaxe1);
        Line.drawLineSlopeThick(xc + xPickaxeStick, yc + yPickaxeStick, 7, Math.tan(((double) arc + ((double) numPoints / 4)) / (double) R), diamondColor, buffer, reversePickaxe2);

        // Arms
        for (int i = 0; i <= 4; i++) {
            double cos = Math.cos((double) (arc + i - 2) / ((double) R));
            double sin = Math.sin((double) (arc + i - 2) / ((double) R));

            int xArm = (int) ((layer + 11) * cos);
            int yArm = (int) ((layer + 11) * sin);

            boolean reverse = arc >= (double) numPoints / 4 && arc <= 3 * (double) numPoints / 4;

            Line.drawLineSlopeThick(xc + xArm, yc + yArm, 5, Math.tan((double) arc / (double) R), skinColor, buffer, reverse);
        }
    }

    // Main drawing functions
    public void drawSky(Graphics g) {
        // ------------------------------ Background sky ------------------------------
        // Sky colors
        Color[] rainbowColors = new Color[60];
        for (int i = 0; i < 30; i++) {
            float hue = (float) (((double) i + 1) / 180) + (29 / 60.0f);
            float brightness = (float) ((60 - i - 1) / 60.0f);
            rainbowColors[i] = Color.getHSBColor(hue, 1.0f, brightness);
        }
        for (int i = 30; i < 60; i++) {
            float hue = (float) (((double) 60 - i) / 180) + (29 / 60.0f);
            float brightness = (float) ((i) / 60.0f);
            rainbowColors[i] = Color.getHSBColor(hue, 1.0f, brightness);
        }

        // Draw sky
        for (int i = 0; i < 60; i++) {
            g.setColor(rainbowColors[i]);
            g.fillArc(xc - R, yc - R, 2 * R, 2 * R, (-i * divAngle) - angle, -divAngle);
        }

        // Sun and moon
        int x = (int) (R * Math.cos(angleRad / ((double) R)));
        int y = (int) (R * Math.sin(angleRad / ((double) R)));

        Shape.fillCircleRect(xc + (int) ((double) x * 28) / 40, yc + (int) ((double) y * 28) / 40, 30, new Color(250, 255, 98), bgBuffer);
        Shape.fillCircleRect(xc - (int) ((double) x * 28) / 40, yc - (int) ((double) y * 28) / 40, 30, Color.white, bgBuffer);

        // Trees
        for (int i = 0; i < numPoints - (numPoints / 12); i += (numPoints / 12)) {
            drawTree(bgBuffer, i);
        }

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

        Color grassColor = new Color(26, 148, 72);
        Color dirtColor = new Color(113, 90, 81);
        Color stoneColor = new Color(120, 120, 120);
        Color obsidianColor = new Color(42, 33, 134);
        //Color blackstoneColor = new Color(60, 60, 60);
        Color blackstoneColor = obsidianColor;
        Color lavaColor = new Color(255, 135, 45);
        Color waterColor = new Color(24, 151, 193);

        // Grass
        int rGrass = r;
        g.setColor(grassColor);
        g.fillOval(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass);

        // Dirt
        int rDirt = 251;
        g.setColor(dirtColor);
        g.fillOval(xc - rDirt, yc - rDirt, 2 * rDirt, 2 * rDirt);

        // Water slices
        g.setColor(waterColor);
        //g.fillArc(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass, -14 * divAngle, -3 * divAngle);
        g.fillArc(xc - rGrass, yc - rGrass, 2 * rGrass, 2 * rGrass, -31 * divAngle, -3 * divAngle);

        // Dirt
        rDirt = 240;
        g.setColor(dirtColor);
        g.fillOval(xc - rDirt, yc - rDirt, 2 * rDirt, 2 * rDirt);

        // Stone slices
        if(slices) {
            g.setColor(stoneColor);
            g.fillArc(xc - 240, yc - 240, 2 * 240, 2 * 240, - 3, -3 * divAngle);
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
        int stoneRGB = 80;

        for (int i = 0; i < 60; i++) {
            stoneColors[i] = i <= 40 ? new Color(stoneRGB + i, stoneRGB + i, stoneRGB + i) : stoneColor;
        }

        int rStoneDynamic = 225;
        for (int i = 0; i < 60; i++) {
            g.setColor(stoneColors[i]);
            g.fillArc(xc - rStoneDynamic, yc - rStoneDynamic, 2 * rStoneDynamic, 2 * rStoneDynamic, (i * divAngle) - minAngleDiscret - 3, -divAngle);
        }

        // Stone
        int rStone = 195;
        g.setColor(stoneColor);
        g.fillOval(xc - rStone, yc - rStone, 2 * rStone, 2 * rStone);

        // Blackstone slices
        if(slices) {
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
            blackstoneColors[i] = i <= 40 ? new Color(obsidianRGB[0] + i, obsidianRGB[1] + i, obsidianRGB[2] + i) : blackstoneColor;
        }

        int rBlackstoneDynamic = 150;
        for (int i = 0; i < 60; i++) {
            g.setColor(blackstoneColors[i]);
            g.fillArc(xc - rBlackstoneDynamic, yc - rBlackstoneDynamic, 2 * rBlackstoneDynamic, 2 * rBlackstoneDynamic, (i * divAngle) - hourAngleDiscret - 3, -divAngle);
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

    public void grid (Graphics g) {
        Color gridColor = new Color(100, 100, 100);
        for(int i = 15; i <= 255; i += 15) {
            Shape.drawCirclePolar(xc, yc, i, gridColor, buffer);
        }

        g.setColor(gridColor);
        for(int i = 0; i < 60; i ++) {
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

        drawPlanet(gBuffer);

        drawSteve(buffer, 195, minAngle + 6);
        drawSteve(buffer, 120, hourAngle + 6);

        //grid(gBuffer);

        g.drawImage(buffer, 0, 0, this);
    }
}