
//http://sourceforge.net/projects/web1856

import java.awt.*;
import java.awt.image.*;

public class TileRenderor extends java.lang.Object
{
    protected double MULTIPLIER = 1.0;
    protected int WIDTH = (int)(60 * MULTIPLIER);
    protected int HEIGHT = (int)(52 * MULTIPLIER);
    protected int HEX_SIDE = (int)(30 * MULTIPLIER);
    protected int PEN_WIDTH = (int)(4.0 * MULTIPLIER) + 1;
    protected int CIRCLE_RADIUS = (int)(9 * MULTIPLIER);
    protected int FONT_SIZE = (int)(Math.min(12.0, (8 * MULTIPLIER)));
    protected int HEX_SIDE_DIV_2 = HEX_SIDE / 2;
    protected int SHORT_LENGTH = (WIDTH - HEX_SIDE) / 2;
    protected int HEIGHT_DIV_2 = HEIGHT / 2;
    protected int L3 = HEX_SIDE * 3;
    protected int L3div2 = L3 / 2;
    protected int [] xPoints = { 0, SHORT_LENGTH, SHORT_LENGTH + HEX_SIDE, WIDTH,
                               SHORT_LENGTH + HEX_SIDE, SHORT_LENGTH };
    protected int [] yPoints = { HEIGHT_DIV_2, 0, 0, HEIGHT_DIV_2, HEIGHT, HEIGHT };
    
    
    
    final static public int S  = 0;
    final static public int SW = 1;
    final static public int NW = 2;
    final static public int N  = 3;
    final static public int NE = 4;
    final static public int SE = 5;
    
    final static protected int NONE = 0;
    final static protected int STRAIGHT = 1;
    final static protected int GENTLE_CURVE = 2;
    final static protected int TIGHT_CURVE = 3;
    final static protected int HALF_STRAIGHT = 4;
    final static protected int HALF_TIGHT = 5;
    final static protected int REVERSE_HALF_TIGHT = 6;
    final static protected int TIGHT_MINOR = 7;
    final static protected int GENTLE_MINOR = 8;
    final static protected int STRAIGHT_MINOR = 9;
    final static protected int OFFBOARD = 10;
    final static protected int REVERSE_GENTLE_MINOR = 11;
    
    final static protected int TEN = 1;
    final static protected int TWENTY = 2;
    final static protected int THIRTY = 3;
    final static protected int FORTY = 4;
    final static protected int FIFTY = 5;
    final static protected int SIXTY = 6;
    final static protected int EIGHTY = 7;
    final static protected int ONE_HUNDRED = 8;
    
    final static public int [] YELLOW_TILES = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 55, 56, 57, 58, 69 };
    final static public int [] GREEN_TILES = { 14, 15, 16, 17, 18, 19, 20, 23, 24, 25, 26, 27, 28,
                                               29, 59, 120, 121 };
    final static public int [] BROWN_TILES = { 39, 40, 41, 42, 43, 44, 45, 46, 47, 63, 64, 65, 66,
                                               67, 68, 70, 122, 125, 126, 127 };
    final static public int [] GRAY_TILES = { 123, 124 };
    
    protected double radiansPerDegree = Math.PI/180.0;

    protected int [] STrack = { NONE };
    protected int [] SWTrack = { NONE };
    protected int [] NWTrack = { NONE };
    protected int [] NTrack = { NONE };
    protected int [] NETrack = { NONE };
    protected int [] SETrack = { NONE };
    protected int [] [] geography = { STrack, SWTrack, NWTrack, NTrack, NETrack, SETrack };

    protected int [] cityValues = { NONE, NONE, NONE, NONE, NONE, NONE };
    protected String [] cityValueString = { "", "10", "20", "30", "40", "50", "60", "80", "100" };
    
    protected String [] specialValues = { "", "", "", "", "", "" };
    
    final static protected int SINGLE_MAJOR = 1;
    final static protected int DOUBLE_MAJOR = 2;
    final static protected int TRIPPLE_MAJOR = 3;
    final static protected int QUAD_MAJOR = 4;
    final static protected int OO_GREEN = 5;
    final static protected int DOUBLE_DOUBLE_MAJOR = 6;
    final static protected int DOUBLE_MAJOR_OFFSET = 7;
    final static protected int TORONTO_GREEN = 8;
    final static protected int OO_BROWN65 = 9;
    final static protected int OO_BROWN67 = 10;
    final static protected int OO_BROWN68 = 11;
    final static protected int SINGLE_DOT = 12;
    final static protected int DOUBLE_DOT = 13;
    final static protected int MOUNTAIN = 14;
    final static protected int OO_GREEN_MOUNTAIN = 15;
    final static protected int OO_BROWN66 = 16;
    
    final static protected int LAND = -900;
    final static protected int WATER = -901;
    final static protected int GRASS = -902;

    protected int cityType = NONE;
    
    final static protected Color YELLOW = new Color(0xFF, 0xFF, 0x0);
    final static protected Color GREEN = new Color(0x0, 0xE7, 0x08);
    final static protected Color BROWN = new Color(0xCE, 0x63, 0x08);
    final static protected Color GRAY = Color.lightGray;
    final static protected Color RED = Color.red;
    final static protected Color LAND_COLOR = new Color(/*246,245,220*/240, 250, 190);
    final static protected Color WATER_COLOR = new Color(/*120,206,252*/170, 220, 240);
    final static protected Color GRASS_COLOR = new Color(110,170,110);
    
    protected Color c = YELLOW;
    
    protected int x;
    protected int y;
    protected int orientation;
    protected int tileNumber;
    protected String [] railheads = { null, null, null, null }; 

    protected Font font = null;
    protected Font boldFont = null;
    
    protected Point [] cityMiddle = { null, null, null, null };
    
    final static protected String [] CORPORATIONS = { "BBG", "CA", "CPR", "CV", "GT", "GW",
                                                    "LPS", "TGB", "THB", "WGB", "WR", "CGR" };
    final static protected Color [] CORP_BGCOLOR = { new Color(0xFFCCFF), new Color(0xFF0000), new Color(0xFF66FF),
                                                   new Color(0x9900CC), new Color(0x99FF99), new Color(0xCC9900),
                                                   new Color(0x66CCFF), new Color(0xFF9900), new Color(0x00FF00),
                                                   new Color(0x5131FF), new Color(0x663300), Color.black,
                                                   Color.white };
    final static protected Color [] CORP_FGCOLOR = { Color.black, Color.white, Color.white, Color.white, Color.black,
                                                   Color.white, Color.black, Color.black, Color.black, Color.white,
                                                   Color.white, Color.white, Color.black};
    
//    static public int getWidth(double D)
//    {
//        return (int)(60 * D);
//    }
//    
//    static public int getHeight(double H)
//    {
//        return (int)(52 * H);
//    }
//    
//    static public int getHexSide(double H)
//    {
//        return (int)(30 * H);
//    }
//
//    static public int getHexShortSide(double H)
//    {
//        return (int)((getWidth(H) - getHexSide(H)) / 2);
//    }

    public Polygon getPolygon() { return new Polygon (xPoints, yPoints, 5); }
    
    
    public void drawTile(Graphics g, int tileNumber, int orientation, int x, int y)
    {
        int HEIGHT = 52;
        int MULTIPLIER = 1;
        int HEX_SIDE = 30;
        int WIDTH = 60;
        int HEIGHT_DIV_2 = 26;
        int CIRCLE_RADIUS = 9;
        
        g.setFont(font);
        g.setColor(c);
        g.fillPolygon(xPoints, yPoints, 6);
        
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int mid1x = 0;
        int mid2x = 0;
        int mid1y = 0;
        int mid2y = 0;
        int angle = 0;
        int angle1 = 0;
        int angle2 = 0;
        int angle3 = 0;
        int centerX = 0;
        int centerY = 0;
        int offset = 0;
        
        int drawingOffset = HEIGHT / 4;
        
        String tileNumberS = String.valueOf(tileNumber);
        
        // Draw the tile number first
        //Image image = null;
//        if (tileNumber > 0 && MULTIPLIER >= 2 /*&& false*/ )
//        {
//            switch (orientation)
//            {
//                case S:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 0);
//                    //g.drawImage(OffscreenI, SHORT_LENGTH - 2 + x, HEIGHT - OffscreenI.getHeight(parent.getApplet())+ y, parent.getApplet());
//                    break;
//                }
//                case SW:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 300);
//                    //g.drawImage(OffscreenI, x, HEIGHT_DIV_2 + y - 8, parent.getApplet());
//                    break;
//                }
//                case NW:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 240);
//                   // g.drawImage(OffscreenI, SHORT_LENGTH + x - (int)(12 * (MULTIPLIER / 2)) + 4, y - 2, parent.getApplet());
//                    break;
//                }
//                case N:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 180);
//                    //g.drawImage(OffscreenI, SHORT_LENGTH + HEX_SIDE - OffscreenI.getWidth(parent.getApplet()) + 4 + x, y, parent.getApplet());
//                    break;
//                }
//                case NE:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 120);
//                    //g.drawImage(OffscreenI, SHORT_LENGTH + HEX_SIDE + x + 4, HEIGHT_DIV_2 - OffscreenI.getHeight(parent.getApplet()) + 10 + y, parent.getApplet());
//                    break;
//                }
//                case SE:
//                {
//                    createRotatedImage(g, tileNumberS, c, Color.black, 60);
//                    //g.drawImage(OffscreenI, SHORT_LENGTH + HEX_SIDE - (int)(12 * (MULTIPLIER / 2)) + x, HEIGHT - OffscreenI.getHeight(parent.getApplet()) + y - (int)(MULTIPLIER), parent.getApplet());
//                    break;
//                }
//            }
//        }
//
//        g.setColor(Color.black);
        int i;
        int j;
        for (i = 0; i < geography.length; i++)
        {
            for (j = 0; j < geography[i].length; j++)
            {
                switch (geography[i][j])
                {
//                    case OFFBOARD:
//                    {
//                        switch (i)
//                        {
//                            case S:
//                            case N:
//                            {
//                                // Midpoint of S line segment
//                                mid1x = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
//                                mid1y = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
//                                // Midpoint of N line segment
//                                mid2x = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
//                                mid2y = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
//                                angle = 90;
//                                break;
//                            }
//                            case SW:
//                            case NE:
//                            {
//                                // Midpoint of SW line segment
//                                mid1x = ((xPoints[5] - xPoints[0]) / 2) + xPoints[0];
//                                mid1y = ((yPoints[5] - yPoints[0]) / 2) + yPoints[0];
//                                // Midpoint of NE line segment
//                                mid2x = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
//                                mid2y = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
//                                angle = 30;
//                                break;
//                            }
//                            case NW:
//                            case SE:
//                            {
//                                // Midpoint of NW line segment
//                                mid1x = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
//                                mid1y = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
//                                // Midpoint of SE line segment
//                                mid2x = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
//                                mid2y = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
//                                angle = 330;
//                                break;
//                            }
//                        }
//                        if (geography[i][j] != OFFBOARD)
//                        {
//                            if (geography[i][j] == HALF_STRAIGHT)
//                            {
//                                if (i == N || i == NE || i == SE)
//                                {
//                                    mid1x = mid2x;
//                                    mid1y = mid2y;
//                                }
//                                mid2x = (WIDTH / 2) + x;
//                                mid2y = HEIGHT_DIV_2 + y;
//                            }
//                            //GraphicsUtil.drawLine(g, mid1x, mid1y, mid2x, mid2y, PEN_WIDTH);
//                        
//                            if (geography[i][j] == STRAIGHT_MINOR)
//                            {
//                                x1 = mid1x + (int)(Math.cos((angle + 25) * radiansPerDegree) * (drawingOffset));
//                                y1 = mid1y - (int)(Math.sin((angle + 25) * radiansPerDegree) * (drawingOffset));
//                                x2 = mid1x + (int)(Math.cos((angle - 25) * radiansPerDegree) * (drawingOffset));
//                                y2 = mid1y - (int)(Math.sin((angle - 25) * radiansPerDegree) * (drawingOffset));
//                                //GraphicsUtil.drawLine(g, x1, y1, x2, y2, PEN_WIDTH);
//                            }
//                        }
//                        else
//                        {
//                            // Forget the pointy top
//                            if (i == N || i == NE || i == SE)
//                            {
//                                mid1x = mid2x;
//                                mid1y = mid2y;
//                                angle += 180;
//                            }
//                            x1 = mid1x + (int)(Math.cos((angle) * radiansPerDegree) * (drawingOffset));
//                            y1 = mid1y - (int)(Math.sin((angle) * radiansPerDegree) * (drawingOffset));
//                            //GraphicsUtil.drawLine(g, mid1x, mid1y, x1, y1, PEN_WIDTH);
//                        }
//                        break;
//                    }
//                    case GENTLE_CURVE:
//                    case GENTLE_MINOR:
                    case REVERSE_GENTLE_MINOR:
                    {
                        switch (i)
                        {
                            case S:
                            {
                                centerX = (-(HEX_SIDE / 2)) + x;
                                centerY = HEIGHT + y;
                                angle = 0;
                                break;
                            }
                            case SW:
                            {
                                centerX = (-(HEX_SIDE / 2)) + x;
                                centerY = 0 + y;
                                angle = 300;
                                break;
                            }
                            case NW:
                            {
                                centerX = (WIDTH / 2) + x;
                                centerY = (-HEIGHT_DIV_2) + y;
                                angle = 240;
                                break;
                            }
                            case N:
                            {
                                centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                                centerY = 0 + y;
                                angle = 180;
                                break;
                            }
                            case NE:
                            {
                                centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                                centerY = HEIGHT + y;
                                angle = 120;
                                break;
                            }
                            case SE:
                            {
                                centerX = (WIDTH / 2) + x;
                                centerY = (HEIGHT_DIV_2 * 3) + y;
                                angle = 60;
                                break;
                            }
                        }
                        //GraphicsUtil.drawArc(g, centerX - L3div2, centerY - L3div2, L3, L3, angle, 60, PEN_WIDTH);
                        if (geography[i][j] == GENTLE_MINOR)
                        {
                            x1 = (int)(Math.cos((angle + 20) * radiansPerDegree) * (L3div2 - (PEN_WIDTH * 2))) + centerX;
                            y1 = centerY - (int)(Math.sin((angle + 20) * radiansPerDegree) * (L3div2 - (PEN_WIDTH * 2)));
                            x2 = (int)(Math.cos((angle + 20) * radiansPerDegree) * (L3div2 + (PEN_WIDTH * 2))) + centerX;
                            y2 = centerY - (int)(Math.sin((angle + 20) * radiansPerDegree) * (L3div2 + (PEN_WIDTH * 2)));
                            //GraphicsUtil.drawLine(g, x1, y1, x2, y2, PEN_WIDTH);
                        }
                        else if (geography[i][j] == REVERSE_GENTLE_MINOR)
                        {
                            x1 = (int)(Math.cos((angle + 40) * radiansPerDegree) * (L3div2 - (PEN_WIDTH * 2))) + centerX;
                            y1 = centerY - (int)(Math.sin((angle + 40) * radiansPerDegree) * (L3div2 - (PEN_WIDTH * 2)));
                            x2 = (int)(Math.cos((angle + 40) * radiansPerDegree) * (L3div2 + (PEN_WIDTH * 2))) + centerX;
                            y2 = centerY - (int)(Math.sin((angle + 40) * radiansPerDegree) * (L3div2 + (PEN_WIDTH * 2)));
                            //GraphicsUtil.drawLine(g, x1, y1, x2, y2, PEN_WIDTH);
                        }
                        break;
                    }
//                    case TIGHT_CURVE:
//                    case HALF_TIGHT:
//                    case REVERSE_HALF_TIGHT:
                    case TIGHT_MINOR:
                    {
                        int point = 0;
                        switch (i)
                        {
                            case S:
                            {
                                point = 5;
                                angle = 0;
                                break;
                            }
                            case SW:
                            {
                                point = 0;
                                angle = 300;
                                break;
                            }
                            case NW:
                            {
                                point = 1;
                                angle = 240;
                                break;
                            }
                            case N:
                            {
                                point = 2;
                                angle = 180;
                                break;
                            }
                            case NE:
                            {
                                point = 3;
                                angle = 120;
                                break;
                            }
                            case SE:
                            {
                                point = 4;
                                angle = 60;
                                break;
                            }
                        }
                        angle2 = 120;
                        if (geography[i][j] == HALF_TIGHT)
                        {
                            angle2 = 60;
                        }
                       //GraphicsUtil.drawArc(g, xPoints[point] - HEX_SIDE_DIV_2, yPoints[point] - HEX_SIDE_DIV_2, HEX_SIDE, HEX_SIDE, angle, angle2, PEN_WIDTH);
                        if (geography[i][j] == REVERSE_HALF_TIGHT)
                        {
                            angle2 = 60;
                            g.setColor(c);
                            //GraphicsUtil.drawArc(g, xPoints[point] - HEX_SIDE_DIV_2, yPoints[point] - HEX_SIDE_DIV_2, HEX_SIDE, HEX_SIDE, angle, angle2, PEN_WIDTH);
                            g.setColor(Color.black);
                        }
                        
                        if (geography[i][j] == TIGHT_MINOR)
                        {
                            x1 = (int)(Math.cos((angle + 40) * radiansPerDegree) * (HEX_SIDE_DIV_2 - (PEN_WIDTH * 2))) + xPoints[point];
                            y1 = yPoints[point] - (int)(Math.sin((angle + 40) * radiansPerDegree) * (HEX_SIDE_DIV_2 - (PEN_WIDTH * 2)));
                            x2 = (int)(Math.cos((angle + 40) * radiansPerDegree) * (HEX_SIDE_DIV_2 + (PEN_WIDTH * 2))) + xPoints[point];
                            y2 = yPoints[point] - (int)(Math.sin((angle + 40) * radiansPerDegree) * (HEX_SIDE_DIV_2 + (PEN_WIDTH * 2)));
                            //GraphicsUtil.drawLine(g, x1, y1, x2, y2, PEN_WIDTH);
                        }
                        break;
                    }
                    default : break;
                }
            }
        }
        
       // int valueLocation = ((drawingOffset - 6) / 4);
//        for (i = 0; i < cityValues.length; i++)
//        {
//            if (cityValues[i] != NONE)
//            {
//                switch (i)
//                {
//                    case S:
//                    {
//                        mid1x = xPoints[5];
//                        mid1y = yPoints[5];
//                        angle = 60;
//                        break;
//                    }
//                    case SW:
//                    {
//                        mid1x = xPoints[0];
//                        mid1y = yPoints[0];
//                        angle = 0;
//                        break;
//                    }
//                    case NW:
//                    {
//                        mid1x = xPoints[1];
//                        mid1y = yPoints[1];
//                        angle = 300;
//                        break;
//                    }
//                    case N:
//                    {
//                        mid1x = xPoints[2];
//                        mid1y = yPoints[2];
//                        angle = 240;
//                        break;
//                    }
//                    case NE:
//                    {
//                        mid1x = xPoints[3];
//                        mid1y = yPoints[3];
//                        angle = 180;
//                        break;
//                    }
//                    case SE:
//                    {
//                        mid1x = xPoints[4];
//                        mid1y = yPoints[4];
//                        angle = 120;
//                        break;
//                    }
//                }
//                int radius = ((g.getFontMetrics().stringWidth(cityValueString[cityValues[i]])) + 4) / 2;
//                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (WIDTH / 8));
//                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (WIDTH / 8));
//                g.setColor(Color.white);
//                //GraphicsUtil.fillCircle(g, x1, y1, radius);
//                g.setColor(Color.black);
//                //GraphicsUtil.drawCircle(g, x1, y1, radius);
//                g.drawString(cityValueString[cityValues[i]], x1 - radius + 2, y1 + (radius / 2));
//            }
//        }
        
//        for (i = 0; i < specialValues.length; i++)
//        {
//            if (!specialValues[i].equals(""))
//            {
//                switch (i)
//                {
//                    case S:
//                    {
//                        mid1x = xPoints[5];
//                        mid1y = yPoints[5];
//                        angle = 60;
//                        break;
//                    }
//                    case SW:
//                    {
//                        mid1x = xPoints[0];
//                        mid1y = yPoints[0];
//                        angle = 0;
//                        break;
//                    }
//                    case NW:
//                    {
//                        mid1x = xPoints[1];
//                        mid1y = yPoints[1];
//                        angle = 300;
//                        break;
//                    }
//                    case N:
//                    {
//                        mid1x = xPoints[2];
//                        mid1y = yPoints[2];
//                        angle = 240;
//                        break;
//                    }
//                    case NE:
//                    {
//                        mid1x = xPoints[3];
//                        mid1y = yPoints[3];
//                        angle = 180;
//                        break;
//                    }
//                    case SE:
//                    {
//                        mid1x = xPoints[4];
//                        mid1y = yPoints[4];
//                        angle = 120;
//                        break;
//                    }
//                }
//                int radius = ((g.getFontMetrics().stringWidth(cityValueString[cityValues[i]])) + 4) / 2;
//                System.out.println(radius);
//                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (WIDTH / 8)) - (CIRCLE_RADIUS / 2);
//                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (WIDTH / 8));
//                g.setColor(Color.white);
//                if (c == YELLOW || c == LAND_COLOR || c == WATER_COLOR)
//                {
//                    g.setColor(Color.black);
//                }
//                g.setFont(boldFont);
//                g.drawString(specialValues[i], x1, y1);
//                g.setFont(font);
//                g.setColor(Color.black);
//            }
//        }
        
        int middleX = (WIDTH / 2) + x;
        int middleY = HEIGHT_DIV_2 + y;
        switch (cityType)
        {
            case SINGLE_MAJOR:
            {
                g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, middleX, middleY, CIRCLE_RADIUS);
                g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, middleX, middleY, CIRCLE_RADIUS);
                cityMiddle[0] = new Point(middleX, middleY);
                break;
            }
            case SINGLE_DOT:
            {
                g.setColor(Color.black);
                //GraphicsUtil.fillCircle(g, middleX, middleY, CIRCLE_RADIUS / 3);
                //GraphicsUtil.drawCircle(g, middleX, middleY, CIRCLE_RADIUS / 3);
                break;
            }
            case MOUNTAIN:
            {
                int distance = CIRCLE_RADIUS / 2;
                int [] xpos = { middleX - distance, middleX, middleX + distance }; 
                int [] ypos = { middleY + distance, middleY - distance, middleY + distance }; 
                g.setColor(Color.black);
                g.fillPolygon(xpos, ypos, 3);
                break;
            }
            case DOUBLE_MAJOR:
            case DOUBLE_MAJOR_OFFSET:
            {
                offset = 0;
                if (cityType == DOUBLE_MAJOR)
                {
                    offset = 30;
                }
                int edge = (NW + orientation) % 6;
                switch (edge)
                {
                    case S:
                    case N:
                    {
                        angle1 = 330 - 60 + offset;
                        angle2 = 210 - 60 + offset;
                        angle3 = 30 - 60 + offset;
                        break;
                    }
                    
                    case NE:
                    case SW:
                    {
                        angle1 = 330 + 60 + offset;
                        angle2 = 210 + 60 + offset;
                        angle3 = 30 + 60 + offset;
                        break;
                    }
                    
                    case SE:
                    case NW:
                    {
                        angle1 = 330 + offset;
                        angle2 = 210 + offset;
                        angle3 = 30 + offset;
                        break;
                    }
                }
                
                int c2x = middleX + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                int c2y = middleY - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                int c1x = middleX + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                int c1y = middleY - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
                int [] newX = { c1x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY = { c1y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                        
                g.setColor(Color.white);
                g.fillPolygon(newX, newY, 4);
                g.setColor(Color.black);
                g.drawPolygon(newX, newY, 4);
                        
                g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c1x, c1y, CIRCLE_RADIUS);
                g.setColor(Color.black);
               //GraphicsUtil.drawCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c1x, c1y, CIRCLE_RADIUS);
                cityMiddle[0] = new Point(c1x, c1y);
                cityMiddle[1] = new Point(c2x, c2y);
                break;
            }
//            case OO_GREEN:
//            case TORONTO_GREEN:
//            case OO_BROWN65:
//            case DOUBLE_DOT:
            case OO_GREEN_MOUNTAIN:
            {
                mid1x = xPoints[orientation];
                mid1y = yPoints[orientation];
                offset = 3;
                angle = -(60 * orientation);
                angle2 = 180;
                if (cityType == TORONTO_GREEN)
                {
                    offset = 2;
                    angle2 = 240;
                }
                else if (cityType == OO_BROWN65)
                {
                    mid1x = xPoints[(orientation + 1) % 6];
                    mid1y = yPoints[(orientation + 1) % 6];
                    offset = 3;
                    angle = (-(60 * orientation)) - 60;
                    angle2 = 240;
                }
                mid2x = xPoints[(orientation + offset) % 6];
                mid2y = yPoints[(orientation + offset) % 6];
                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                x2 = mid2x + (int)(Math.cos((angle + angle2) * radiansPerDegree) * (HEX_SIDE_DIV_2));
                y2 = mid2y - (int)(Math.sin((angle + angle2) * radiansPerDegree) * (HEX_SIDE_DIV_2));
//                if (cityType == DOUBLE_DOT)
//                {
//                    g.setColor(Color.black);
//                    //GraphicsUtil.fillCircle(g, x1, y1, CIRCLE_RADIUS / 3);
//                    //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS / 3);
//                    //GraphicsUtil.drawCircle(g, x1, y1, CIRCLE_RADIUS / 3);
//                    //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS / 3);
//                }
//                else
//                {
//                    g.setColor(Color.white);
//                    //GraphicsUtil.fillCircle(g, x1, y1, CIRCLE_RADIUS);
//                    //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS);
//                    g.setColor(Color.black);
//                    //GraphicsUtil.drawCircle(g, x1, y1, CIRCLE_RADIUS);
//                    //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS);
//                    
//                    // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
//                    if (cityType == TORONTO_GREEN)
//                    {
//                        cityMiddle[1] = new Point(x1, y1);
//                        cityMiddle[0] = new Point(x2, y2);
//                    }
//                    else
//                    {
//                        cityMiddle[0] = new Point(x1, y1);
//                        cityMiddle[1] = new Point(x2, y2);
//                    }
//                }
                if (cityType == OO_GREEN_MOUNTAIN)
                {
                    int distance = CIRCLE_RADIUS / 2;
                    int [] xpos = { middleX - distance, middleX, middleX + distance };
                    int [] ypos = { middleY + distance, middleY - distance, middleY + distance };
//                    g.setColor(Color.black);
//                    g.fillPolygon(xpos, ypos, 3);
                }
                break;
            }
            case OO_BROWN66:
            {
                // North East midpoint, a little more than the radius
                mid1x = xPoints[orientation];
                mid1y = yPoints[orientation];
                offset = 3;
                angle = -(60 * orientation);
                angle2 = 180;
                mid1x = xPoints[(orientation + 1) % 6];
                mid1y = yPoints[(orientation + 1) % 6];
                offset = 3;
                angle = (-(60 * orientation)) - 60;
                angle2 = 240;
                mid2x = xPoints[(orientation + offset) % 6];
                mid2y = yPoints[(orientation + offset) % 6];

                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                x2 = mid2x + (int)(Math.cos((angle + angle2) * radiansPerDegree) * (HEX_SIDE_DIV_2));
                y2 = mid2y - (int)(Math.sin((angle + angle2) * radiansPerDegree) * (HEX_SIDE_DIV_2));

 //               g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, x1, y1, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS);
//                g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, x1, y1, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS);
                    
//                switch (orientation)
//                {
//                    case S:
//                    {
//                        // Midpoint of NE line segment
//                        angle = 210;
//                        centerX = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
//                        centerY = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
//                        break;
//                    }
//                    case SW:
//                    {
//                        // Midpoint of SE line segment
//                        angle = 150;
//                        centerX = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
//                        centerY = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
//                        break;
//                    }
//                    case NW:
//                    {
//                        // Midpoint of S line segment
//                        angle = 90;
//                        centerX = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
//                        centerY = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
//                        break;
//                    }
//                    case N:
//                    {
//                        // Midpoint of SW line segment
//                        angle = 30;
//                        centerX = ((xPoints[5] - xPoints[0]) / 2) + xPoints[1];
//                        centerY = ((yPoints[5] - yPoints[0]) / 2) + yPoints[1];
//                        break;
//                    }
//                    case NE:
//                    {
//                        // Midpoint of NW line segment
//                        angle = 330;
//                        centerX = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
//                        centerY = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
//                        break;
//                    }
//                    case SE:
//                    {
//                        // Midpoint of N line segment
//                        angle = 270;
//                        centerX = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
//                        centerY = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
//                        break;
//                    }
//                }
                x2 = centerX + (int)(Math.cos((angle) * radiansPerDegree) * (L3div2 / 3));
                y2 = centerY - (int)(Math.sin((angle) * radiansPerDegree) * (L3div2 / 3));
                g.setColor(Color.white);
//                //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS);
//                g.setColor(Color.black);
//                //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS);
//
//                // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
//                cityMiddle[0] = new Point(x1, y1);
//                cityMiddle[1] = new Point(x2, y2);
                break;
            }
            case OO_BROWN67:
            {
                // North midpoint, a little more than the radius
                switch (orientation)
                {
                    case S:
                    {
                        // Midpoint of N line segment
                        mid1x = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
                        mid1y = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
                        angle = 270;
                        centerX = (WIDTH / 2) + x;
                        centerY = (HEIGHT_DIV_2 * 3) + y;
                        break;
                    }
                    case SW:
                    {
                        // Midpoint of NE line segment
                        mid1x = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
                        mid1y = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
                        angle = 210;
                        centerX = (-(HEX_SIDE / 2)) + x;
                        centerY = HEIGHT + y;
                        break;
                    }
                    case NW:
                    {
                        // Midpoint of SE line segment
                        mid1x = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
                        mid1y = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
                        angle = 150;
                        centerX = (-(HEX_SIDE / 2)) + x;
                        centerY = 0 + y;
                        break;
                    }
                    case N:
                    {
                        // Midpoint of S line segment
                        mid1x = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
                        mid1y = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
                        angle = 90;
                        centerX = (WIDTH / 2) + x;
                        centerY = (-HEIGHT_DIV_2) + y;
                        break;
                    }
                    case NE:
                    {
                        // Midpoint of SW line segment
                        mid1x = ((xPoints[5] - xPoints[0]) / 2) + xPoints[0];
                        mid1y = ((yPoints[5] - yPoints[0]) / 2) + yPoints[0];
                        angle = 30;
                        centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                        centerY = 0 + y;
                        break;
                    }
                    case SE:
                    {
                        // Midpoint of NW line segment
                        mid1x = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
                        mid1y = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
                        angle = 330;
                        centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                        centerY = HEIGHT + y;
                        break;
                    }
                }
                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
                x2 = centerX + (int)(Math.cos((angle + 195) * radiansPerDegree) * (L3div2));
                y2 = centerY - (int)(Math.sin((angle + 195) * radiansPerDegree) * (L3div2));
 //               g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, x1, y1, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS);
 //               g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, x1, y1, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS);

                // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
//                cityMiddle[0] = new Point(x1, y1);
//                cityMiddle[1] = new Point(x2, y2);
                break;
            }
//            case OO_BROWN68:
//            {
//                // North midpoint, a little more than the radius
//                switch (orientation)
//                {
//                    case S:
//                    {
//                        // Midpoint of N line segment
//                        mid1x = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
//                        mid1y = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
//                        angle = 270;
//                        // Midpoint of SE line segment
//                        mid2x = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
//                        mid2y = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
//                        break;
//                    }
//                    case SW:
//                    {
//                        // Midpoint of NE line segment
//                        mid1x = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
//                        mid1y = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
//                        angle = 210;
//                        // Midpoint of S line segment
//                        mid2x = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
//                        mid2y = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
//                        break;
//                    }
//                    case NW:
//                    {
//                        // Midpoint of SE line segment
//                        mid1x = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
//                        mid1y = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
//                        angle = 150;
//                        // Midpoint of SW line segment
//                        mid2x = ((xPoints[5] - xPoints[0]) / 2) + xPoints[0];
//                        mid2y = ((yPoints[5] - yPoints[0]) / 2) + yPoints[0];
//                        break;
//                    }
//                    case N:
//                    {
//                        // Midpoint of S line segment
//                        mid1x = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
//                        mid1y = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
//                        angle = 90;
//                        // Midpoint of NW line segment
//                        mid2x = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
//                        mid2y = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
//                        break;
//                    }
//                    case NE:
//                    {
//                        // Midpoint of SW line segment
//                        mid1x = ((xPoints[5] - xPoints[0]) / 2) + xPoints[0];
//                        mid1y = ((yPoints[5] - yPoints[0]) / 2) + yPoints[0];
//                        angle = 30;
//                        // Midpoint of N line segment
//                        mid2x = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
//                        mid2y = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
//                        break;
//                    }
//                    case SE:
//                    {
//                        // Midpoint of NW line segment
//                        mid1x = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
//                        mid1y = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
//                        angle = 330;
//                        // Midpoint of NE line segment
//                        mid2x = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
//                        mid2y = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
//                        break;
//                    }
//                }
//                x1 = mid1x + (int)(Math.cos(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
//                y1 = mid1y - (int)(Math.sin(angle * radiansPerDegree) * (HEX_SIDE_DIV_2));
//                x2 = mid2x + (int)(Math.cos((angle - 120) * radiansPerDegree) * (HEX_SIDE_DIV_2));
//                y2 = mid2y - (int)(Math.sin((angle - 120) * radiansPerDegree) * (HEX_SIDE_DIV_2));
//               // g.setColor(Color.white);
//                //GraphicsUtil.fillCircle(g, x1, y1, CIRCLE_RADIUS);
//                //GraphicsUtil.fillCircle(g, x2, y2, CIRCLE_RADIUS);
//                //g.setColor(Color.black);
//                //GraphicsUtil.drawCircle(g, x1, y1, CIRCLE_RADIUS);
//                //GraphicsUtil.drawCircle(g, x2, y2, CIRCLE_RADIUS);
//
//                // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
////                cityMiddle[0] = new Point(x1, y1);
////                cityMiddle[1] = new Point(x2, y2);
//                break;
//            }
            case DOUBLE_DOUBLE_MAJOR:
            {
                offset = 90;
                int edge = (NW + orientation) % 6;
                int angle4 = 0;
                switch (edge)
                {
                    case S:
                    {
                        angle1 = 330 - 60 + offset;
                        angle2 = 210 - 60 + offset;
                        angle3 = 30 - 60 + offset;
                        angle4 = 0;
                        break;
                    }
                    case N:
                    {
                        angle1 = 330 - 60 + offset;
                        angle2 = 210 - 60 + offset;
                        angle3 = 30 - 60 + offset;
                        angle4 = 180;
                        break;
                    }
                    
                    case NE:
                    {
                        angle1 = 330 + 60 + offset;
                        angle2 = 210 + 60 + offset;
                        angle3 = 30 + 60 + offset;
                        angle4 = 240;
                        break;
                    }
                    case SW:
                    {
                        angle1 = 330 + 60 + offset;
                        angle2 = 210 + 60 + offset;
                        angle3 = 30 + 60 + offset;
                        angle4 = 240 + 180;
                        break;
                    }
                    
                    case SE:
                    {
                        angle1 = 330 + offset;
                        angle2 = 210 + offset;
                        angle3 = 30 + offset;
                        angle4 = 120 + 180;
                        break;
                    }
                    case NW:
                    {
                        angle1 = 330 + offset;
                        angle2 = 210 + offset;
                        angle3 = 30 + offset;
                        angle4 = 120;
                        break;
                    }
                }
                
                // Move middleX and MiddleY
                mid1x = middleX + (int)(Math.cos((240 + angle4) * radiansPerDegree) * HEX_SIDE_DIV_2);
                mid1y = middleY + (int)(Math.sin((240 + angle4) * radiansPerDegree) * HEX_SIDE_DIV_2);
                                
                int c2x = mid1x + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                int c2y = mid1y - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                int c1x = mid1x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                int c1y = mid1y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
                int [] newX = { c1x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY = { c1y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                System.out.println(newX + " " + newY);
                        
//                g.setColor(Color.white);
//                g.fillPolygon(newX, newY, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(newX, newY, 4);
//                        
//                g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c1x, c1y, CIRCLE_RADIUS);
//                g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c1x, c1y, CIRCLE_RADIUS);

                // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
//                cityMiddle[0] = new Point(c1x, c1y);
//                cityMiddle[1] = new Point(c2x, c2y);

                // Move middleX and MiddleY
                mid1x = middleX + (int)(Math.cos((240 + angle4 - 90) * radiansPerDegree) * HEX_SIDE_DIV_2);
                mid1y = middleY + (int)(Math.sin((240 + angle4 - 90) * radiansPerDegree) * HEX_SIDE_DIV_2);
                                
                c2x = mid1x + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                c2y = mid1y - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                c1x = mid1x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                c1y = mid1y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
                int [] newX1 = { c1x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY1 = { c1y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                System.out.println(newX1 + " " + newY1);
                        
//                g.setColor(Color.white);
//                g.fillPolygon(newX1, newY1, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(newX1, newY1, 4);
//                        
//                g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c1x, c1y, CIRCLE_RADIUS);
                //g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c1x, c1y, CIRCLE_RADIUS);

                // MAY NEED TO SWAP THESE TO BE CONSISTENT WITH POSTSCRIPT PROGRAM
//                cityMiddle[2] = new Point(c1x, c1y);
//                cityMiddle[3] = new Point(c2x, c2y);
                
                // Swap cities 1 and 2
//                Point tempPoint = cityMiddle[2];
//                cityMiddle[2] = cityMiddle[1];
//                cityMiddle[1] = tempPoint;
                break;
            }
            case TRIPPLE_MAJOR:
            {
                int angle4 = 0;
                int angle5 = 0;
                switch (orientation)
                {
                    case S:
                    {
                        angle1 = 180;
                        angle2 = 240 + 180;
                        angle3 = 60 + 180;
                        angle4 = 270;
                        angle5 = 90;
                        break;
                    }
                    case N:
                    {
                        angle1 = 0 + 180;
                        angle2 = 240 + 180;
                        angle3 = 60 + 180;
                        angle4 = 270 + 180;
                        angle5 = 90 + 180;
                        break;
                    }

                    case SW:
                    {
                        angle1 = 0 - 60;
                        angle2 = 240 - 60;
                        angle3 = 60 - 60;
                        angle4 = 210;
                        angle5 = 30;
                        break;
                    }
                    case NE:
                    {
                        angle1 = 0 - 60;
                        angle2 = 240 - 60;
                        angle3 = 60 - 60;
                        angle4 = 30;
                        angle5 = 30 + 180;
                        break;
                    }
                    
                    case SE:
                    {
                        angle1 = 0 + 60 + 180;
                        angle2 = 240 + 60 + 180;
                        angle3 = 60 + 60 + 180;
                        angle4 = 330 + 180;
                        angle5 = 330 + 180;
                        break;
                    }
                    case NW:
                    {
                        angle1 = 0 + 60 + 180;
                        angle2 = 240 + 60 + 180;
                        angle3 = 60 + 60 + 180;
                        angle4 = 330;
                        angle5 = 330;
                        break;
                    }
                }
                
                // Move middleX and MiddleY
                mid1x = middleX + (int)(Math.cos((angle1 + angle4) * radiansPerDegree) * CIRCLE_RADIUS);
                mid1y = middleY + (int)(Math.sin((angle1 + angle4) * radiansPerDegree) * CIRCLE_RADIUS);

                int c2x = mid1x + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                int c2y = mid1y - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                int c1x = mid1x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                int c1y = mid1y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
                // Move middleX and MiddleY
                mid2x = middleX + (int)(Math.cos(angle5 * radiansPerDegree) * CIRCLE_RADIUS);
                mid2y = middleY - (int)(Math.sin(angle5 * radiansPerDegree) * CIRCLE_RADIUS);

                //int c4x = mid2x + (int)(Math.cos(angle1 * radiansPerDegree) * (CIRCLE_RADIUS * 2)) + 1;
                //int c4y = mid2y - (int)(Math.sin(angle1 * radiansPerDegree) * (CIRCLE_RADIUS * 2));
//                int c4x = mid2x;
//                int c4y = mid2y;
                //int c3x = mid2x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                //int c3y = mid2y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
//                int [] xxx = { c1x, c2x, c4x};
//                int [] yyy = { c1y, c2y, c4y};
//                g.setColor(Color.white);
//                g.fillPolygon(xxx, yyy, 3);
//                g.setColor(Color.black);
//                g.drawPolygon(xxx, yyy, 3);

                int [] newX = { c1x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY = { c1y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                System.out.println(newX + " " + newY + " ");
                        
//                g.setColor(Color.white);
//                g.fillPolygon(newX, newY, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(newX, newY, 4);
                        
                //g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c1x, c1y, CIRCLE_RADIUS);
                //g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, c2x, c2y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c1x, c1y, CIRCLE_RADIUS);

                //g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c4x, c4y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c3x, c3y, CIRCLE_RADIUS);
                //g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, c4x, c4y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c3x, c3y, CIRCLE_RADIUS);

//                cityMiddle[0] = new Point(c1x, c1y);
//                cityMiddle[1] = new Point(c2x, c2y);
//                cityMiddle[2] = new Point(c4x, c4y);
                
                break;
            }
            case QUAD_MAJOR:
            {
                int angle4 = 0;
                switch (orientation)
                {
                    case S:
                    {
                        angle1 = 180;
                        angle2 = 240 + 180;
                        angle3 = 60 + 180;
                        angle4 = 270;
                        break;
                    }
                    case N:
                    {
                        angle1 = 0 + 180;
                        angle2 = 240 + 180;
                        angle3 = 60 + 180;
                        angle4 = 270 + 180;
                        break;
                    }

                    case SW:
                    {
                        angle1 = 0 - 60;
                        angle2 = 240 - 60;
                        angle3 = 60 - 60;
                        angle4 = 210;
                        break;
                    }
                    case NE:
                    {
                        angle1 = 0 - 60;
                        angle2 = 240 - 60;
                        angle3 = 60 - 60;
                        angle4 = 30;
                        break;
                    }
                    
                    case SE:
                    {
                        angle1 = 0 + 60 + 180;
                        angle2 = 240 + 60 + 180;
                        angle3 = 60 + 60 + 180;
                        angle4 = 330 + 180;
                        break;
                    }
                    case NW:
                    {
                        angle1 = 0 + 60 + 180;
                        angle2 = 240 + 60 + 180;
                        angle3 = 60 + 60 + 180;
                        angle4 = 330;
                        break;
                    }
                }
                
                // Move middleX and MiddleY
                mid1x = middleX + (int)(Math.cos((angle1 + angle4) * radiansPerDegree) * CIRCLE_RADIUS);
                mid1y = middleY + (int)(Math.sin((angle1 + angle4) * radiansPerDegree) * CIRCLE_RADIUS);

                int c2x = mid1x + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                int c2y = mid1y - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                int c1x = mid1x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                int c1y = mid1y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
                // Move middleX and MiddleY
                mid2x = middleX + (int)(Math.cos((angle1 + angle4 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                mid2y = middleY + (int)(Math.sin((angle1 + angle4 + 180) * radiansPerDegree) * CIRCLE_RADIUS);

                int c4x = mid2x + (int)(Math.cos(angle1 * radiansPerDegree) * CIRCLE_RADIUS) + 1;
                int c4y = mid2y - (int)(Math.sin(angle1 * radiansPerDegree) * CIRCLE_RADIUS);
                int c3x = mid2x + (int)(Math.cos((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS) - 1;
                int c3y = mid2y - (int)(Math.sin((angle1 + 180) * radiansPerDegree) * CIRCLE_RADIUS);
                        
//                int [] xxx = { c1x, c3x, c4x, c2x };
//                int [] yyy = { c1y, c3y, c4y, c2y };
//                g.setColor(Color.white);
//                g.fillPolygon(xxx, yyy, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(xxx, yyy, 4);

                int [] newX = { c1x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY = { c1y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c1y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c2y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                        
//                g.setColor(Color.white);
//                g.fillPolygon(newX, newY, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(newX, newY, 4);
//                        
//                g.setColor(Color.white);
//                //GraphicsUtil.fillCircle(g, c2x, c2y, CIRCLE_RADIUS);
//                //GraphicsUtil.fillCircle(g, c1x, c1y, CIRCLE_RADIUS);
//                g.setColor(Color.black);
//                //GraphicsUtil.drawCircle(g, c2x, c2y, CIRCLE_RADIUS);
//                //GraphicsUtil.drawCircle(g, c1x, c1y, CIRCLE_RADIUS);
//
//                cityMiddle[0] = new Point(c1x, c1y);
//                cityMiddle[1] = new Point(c2x, c2y);

                int [] newX1 = { c3x + (int)(Math.cos(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c3x + (int)(Math.cos((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c4x + (int)(Math.cos(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c4x + (int)(Math.cos((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                int [] newY1 = { c3y - (int)(Math.sin(angle2 * radiansPerDegree) * CIRCLE_RADIUS),
                                c3y - (int)(Math.sin((angle2 + 180) * radiansPerDegree) * CIRCLE_RADIUS),
                                c4y - (int)(Math.sin(angle3 * radiansPerDegree) * CIRCLE_RADIUS),
                                c4y - (int)(Math.sin((angle3 + 180) * radiansPerDegree) * CIRCLE_RADIUS) };
                System.out.println(newX + " " + newY +" "+ newX1 + " "+ newY1);
                        
//                g.setColor(Color.white);
//                g.fillPolygon(newX1, newY1, 4);
//                g.setColor(Color.black);
//                g.drawPolygon(newX1, newY1, 4);
//                        
//                g.setColor(Color.white);
                //GraphicsUtil.fillCircle(g, c4x, c4y, CIRCLE_RADIUS);
                //GraphicsUtil.fillCircle(g, c3x, c3y, CIRCLE_RADIUS);
//                g.setColor(Color.black);
                //GraphicsUtil.drawCircle(g, c4x, c4y, CIRCLE_RADIUS);
                //GraphicsUtil.drawCircle(g, c3x, c3y, CIRCLE_RADIUS);
                
//                cityMiddle[2] = new Point(c3x, c3y);
//                cityMiddle[3] = new Point(c4x, c4y);

                break;
            }
        }
        
//        // Paint Railheads
//        g.setFont(boldFont);
//        FontMetrics fm = g.getFontMetrics();
//        int yTranslation = fm.getAscent() / 2;
//        for (i = 0; i < railheads.length; i++)
//        {
//            if (cityMiddle[i] != null && railheads[i] != null)
//            {
//                int index;
//                for (index = 0; index < CORPORATIONS.length; index++)
//                {
//                    if (railheads[i].equals(CORPORATIONS[index]))
//                    {
//                        break;
//                    }
//                }
//                g.setColor(CORP_BGCOLOR[index]);
//                //GraphicsUtil.fillCircle(g, cityMiddle[i].x, cityMiddle[i].y, CIRCLE_RADIUS);
//                g.setColor(Color.black);
//                //GraphicsUtil.drawCircle(g, cityMiddle[i].x, cityMiddle[i].y, CIRCLE_RADIUS);
//                g.setColor(CORP_FGCOLOR[index]);
//                cityMiddle[i].translate(((-1) * fm.stringWidth(railheads[i])) / 2, yTranslation);
//                g.drawString(railheads[i], cityMiddle[i].x, cityMiddle[i].y);
//            }
//        }
//        g.setFont(font);
//        g.setColor(Color.black);
//        
//        if (!(tileNumber == WATER || c == GRASS_COLOR || c == RED))
//        {
//            g.setColor(Color.black);
//            g.drawPolygon(xPoints, yPoints, 6);
//        }
    }

    public void drawRoute(Graphics g, int style, int direction, Color trackColor)
    {
    	int HEIGHT = 52;
        int HEX_SIDE = 30;
        int WIDTH = 60;
        int HEIGHT_DIV_2 = 26;
        
        int L3 = HEX_SIDE * 3;
        int L3div2 = L3 / 2;
        
        g.setColor(trackColor);
        
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int mid1x = 0;
        int mid2x = 0;
        int mid1y = 0;
        int mid2y = 0;
        int angle = 0;
        int angle1 = 0;
        int angle2 = 0;
        int angle3 = 0;
        int centerX = 0;
        int centerY = 0;
        int offset = 0;
        
        int drawingOffset = HEIGHT / 4;
        
        int i;
        int j;
        
        switch (style)
        {
            case STRAIGHT:
            case HALF_STRAIGHT:
            {
                switch(direction)
                {
                    case S:
                    case N:
                    {
                        // Midpoint of S line segment
                        mid1x = ((xPoints[4] - xPoints[5]) / 2) + xPoints[5];
                        mid1y = ((yPoints[4] - yPoints[5]) / 2) + yPoints[5];
                        // Midpoint of N line segment
                        mid2x = ((xPoints[2] - xPoints[1]) / 2) + xPoints[1];
                        mid2y = ((yPoints[2] - yPoints[1]) / 2) + yPoints[1];
                        angle = 90;
                        break;
                    }
                    case SW:
                    case NE:
                    {
                         // Midpoint of SW line segment
                         mid1x = ((xPoints[5] - xPoints[0]) / 2) + xPoints[0];
                         mid1y = ((yPoints[5] - yPoints[0]) / 2) + yPoints[0];
                         // Midpoint of NE line segment
                         mid2x = ((xPoints[3] - xPoints[2]) / 2) + xPoints[2];
                         mid2y = ((yPoints[3] - yPoints[2]) / 2) + yPoints[2];
                         angle = 30;
                         break;
                     }
                     case NW:
                     case SE:
                     {
                         // Midpoint of NW line segment
                         mid1x = ((xPoints[1] - xPoints[0]) / 2) + xPoints[0];
                         mid1y = ((yPoints[0] - yPoints[1]) / 2) + yPoints[1];
                         // Midpoint of SE line segment
                         mid2x = ((xPoints[3] - xPoints[4]) / 2) + xPoints[4];
                         mid2y = ((yPoints[4] - yPoints[3]) / 2) + yPoints[3];
                         angle = 330;
                         break;
                     }
                }
                if (style == HALF_STRAIGHT)
                {
                    if (direction == N || direction == NE || direction == SE)
                    {
                        mid1x = mid2x;
                        mid1y = mid2y;
                    }
                    mid2x = (WIDTH / 2) + x;
                    mid2y = HEIGHT_DIV_2 + y;
               }
                //GraphicsUtil.drawLine(g, mid1x, mid1y, mid2x, mid2y, PEN_WIDTH);
                System.out.println(g + " " + mid1x + " " + mid1y + " " + mid2x + " " + mid2y + " " + PEN_WIDTH);
                break;
            }
            case GENTLE_CURVE:
            {
                switch (direction)
                {
                    case S:
                    {
                        centerX = (-(HEX_SIDE / 2)) + x;
                        centerY = HEIGHT + y;
                        angle = 0;
                        break;
                    }
                    case SW:
                    {
                        centerX = (-(HEX_SIDE / 2)) + x;
                        centerY = 0 + y;
                        angle = 300;
                        break;
                    }
                    case NW:
                    {
                        centerX = (WIDTH / 2) + x;
                        centerY = (-HEIGHT_DIV_2) + y;
                        angle = 240;
                        break;
                    }
                    case N:
                    {
                        centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                        centerY = 0 + y;
                        angle = 180;
                        break;
                    }
                    case NE:
                    {
                        centerX = (WIDTH + (HEX_SIDE / 2)) + x;
                        centerY = HEIGHT + y;
                        angle = 120;
                        break;
                    }
                    case SE:
                    {
                        centerX = (WIDTH / 2) + x;
                        centerY = (HEIGHT_DIV_2 * 3) + y;
                        angle = 60;
                        break;
                    }
                }
                //GraphicsUtil.drawArc(g, centerX - L3div2, centerY - L3div2, L3, L3, angle, 60, PEN_WIDTH);
                System.out.println(g + " " + (centerX - L3div2) +  " " + (centerY - L3div2) + L3 + angle + " " + PEN_WIDTH);
                break;
            }
            case TIGHT_CURVE:
            {
                int point = 0;
                switch (direction)
                {
                    case S:
                    {
                        point = 5;
                        angle = 0;
                        break;
                    }
                    case SW:
                    {
                        point = 0;
                        angle = 300;
                        break;
                    }
                    case NW:
                    {
                        point = 1;
                        angle = 240;
                        break;
                    }
                    case N:
                    {
                        point = 2;
                        angle = 180;
                        break;
                    }
                    case NE:
                    {
                        point = 3;
                        angle = 120;
                        break;
                    }
                    case SE:
                    {
                        point = 4;
                        angle = 60;
                        break;
                    }
                }
                angle2 = 120;
                //GraphicsUtil.drawArc(g, xPoints[point] - HEX_SIDE_DIV_2, yPoints[point] - HEX_SIDE_DIV_2, HEX_SIDE, HEX_SIDE, angle, angle2, PEN_WIDTH);
                System.out.println(g + " " + (xPoints[point] - HEX_SIDE_DIV_2) + (yPoints[point] - HEX_SIDE_DIV_2) + HEX_SIDE +  angle +  angle2 + PEN_WIDTH);
                break;
            }
        }
        g.setColor(Color.black);
    }
    
    private void createRotatedImage(Graphics g, String text, Color bgColor, Color fgColor, int angle1) 
	{
	    /*
	     * Get fontmetrics and calculate position.
	     */
	    double angle = (double)angle1 * radiansPerDegree;
	    FontMetrics fm = g.getFontMetrics();

	    int width = fm.stringWidth(text);
	    int height = fm.getHeight();
	    int ascent = fm.getMaxAscent();
	    int leading = fm.getLeading();
	    int verMarzin = (height+ascent+leading - height)/2;
	    
	    int h = verMarzin + ascent + leading;

//	    /*
//	     * Create the image.
//	     */
//	    destroyOffscreen();
//        if (OffscreenI == null)
//        {
//            createOffscreen(width + 8, height);
//        }
//        updateOffscreen(width + 8, height);
//	     
//	    //Image image = parent.getApplet().createImage(width + 8, height);
//	    
//	    /*
//	     * Set graphics attributes and draw the string.
//	     */
//	    //Graphics gr = image.getGraphics();
//	    OffscreenG.setColor(bgColor);
//	    //OffscreenG.fillRect(0, 0, OffscreenI.getWidth(parent.getApplet()), OffscreenI.getHeight(parent.getApplet()));
//
//	    OffscreenG.setFont(g.getFont());
//	    
//	    OffscreenG.setColor(fgColor);
//	    OffscreenG.drawString(text, 4, ascent + leading);

	    /*
	     * Create an imagefilter to rotate the image.
	     */
	    //ImageFilter filter = new RotateFilter(angle);
	    
	    /*
	     * Produce the rotated image.
	     */
	    //ImageProducer producer = new FilteredImageSource(OffscreenI.getSource(), filter);
	    /*
	     * Create the rotated image.
	     */
	    // if (true) return image;
	    //OffscreenI = parent.getApplet().createImage(producer);

	    //return image;
	}
}
