package test;
//http://sourceforge.net/projects/web1856

import java.awt.*;

public class MapViewer extends java.awt.Canvas
{
        
    public void paint (Graphics g, Object[][] theMap, int theHexShortSide, int theHexSide, int distanceBetweenColumns,
    		int getHeight, int theHeight, int theWidth)
    {
        int i;
        int j;
        
        for (i = 0; i < theMap.length; i++)
        {
            for (j = 0; j < theMap[i].length; j++)
            {
                if (theMap[i][j] != null)
                {
                    System.out.println(theMap[i][j]);
                }
            }
        }
        // Paint the column headers and row numbers
        int baseX;
        int baseY;
        // Columns
        for (i = 0; i < theMap[0].length; i++)
        {
            baseY = 12;
            if ((i % 2) == 0)
            {
                baseX = (theHexShortSide + (theHexSide / 2)) + 20 + (i * distanceBetweenColumns) - 2;
            }
            else
            {
                baseX = 20 + (distanceBetweenColumns * (i + 1)) - (theHexSide / 2) - 2;
            }
            char [] theChar = { (char)(65 + i) };
            System.out.println(new String(theChar) + baseX + baseY);
            baseY = (22 / 2) * getHeight + (20 * 2) - 2;
            System.out.println(new String(theChar) +  baseX + baseY);
        }
        for (i = 0; i < theMap.length - 1; i++)
        {
            baseX = 6;
            baseY = 20 + ((theHeight / 2) * (i + 1)) + 4;
            System.out.println(String.valueOf((i + 1)) +  baseX + baseY);
            baseX = ((18 / 2) * (theWidth + theHexSide)) - theHexSide  + (20 * 2) - 14;
            System.out.println(String.valueOf((i + 1)) +  baseX + baseY);
        }

        System.out.println(20 +"  "+ 14 +" " + (((18 / 2) * (theWidth + theHexSide)) + (20 * 2) - theHexSide - 40) + " " + (((22 / 2) * getHeight) + (20 * 2) - 30));
    }
    
    
    protected void paintRoutes(Graphics g, int orientation, Object[][] theMap)
    {
        // Get the currently selected route
        String [] theRoutes = {"aa", "bb", "cc"};
        String [] routeHexes = { "", "", "" };
        int column1 = 0;
        int column2 = 0;
        int column3 = 0;
        int row1 = 0;
        int row2 = 0;
        int row3 = 0;
        int i;
        //int j;
        for (i = 0; i < theRoutes.length; i++)
        {
            //for (j = 0; j < theRoutes[i].length; j++)
            //{
                String currentRoute = new String (theRoutes[i]/*[j]*/);
                if (currentRoute == null) continue;
                while (currentRoute.indexOf("routeEnd") != -1)
                {
                    routeHexes[2] = "";
                    int beginSpot = currentRoute.indexOf("routeBegin");
                    int citySpot = currentRoute.indexOf("routeCity");
                    int legSpot = currentRoute.indexOf("routeLeg");
                    int endSpot = currentRoute.indexOf("routeEnd");
                    
                    if (citySpot == -1) citySpot = Integer.MAX_VALUE;
                    if (legSpot == -1) legSpot = Integer.MAX_VALUE;
                    
                    if (beginSpot != -1 || endSpot < legSpot && endSpot < citySpot)
                    {
                        // do begin stuff
                        // Draw a 1/2 straight coming out of the city for begin
                        // or a 1/2 straight going into the city for coming out.
                        currentRoute = currentRoute.concat(routeHexes[0]);
                        if (beginSpot == -1)
                        {
                            // Switch the start and stop positions
                            String temp = routeHexes[1];
                            routeHexes[1] = routeHexes[0];
                            routeHexes[0] = temp;
                        }
                        int zero = Integer.valueOf((routeHexes[0].charAt(0)));
                        int one = Integer.valueOf((routeHexes[1].charAt(0)));
                        int A = (int)('A');
                        column1 = zero - A;
                        column2 = one - A;
                        row1 = Integer.parseInt(routeHexes[0].substring(1));
                        row2 = Integer.parseInt(routeHexes[1].substring(1));
                        // Draw a line from the middle of the start hex to the edge of the end hex.
                        System.out.println(theMap[row1][column1] + " " + column1 + " " + column2 + " " + row1 + " "+row2 + " " + routeColor[i]);
                    }
                    else if (citySpot < legSpot)
                    {
                        // do city stuff
                        // Draw a 1/2 straight going in to the city and a 1/2 straight coming out
                        // of the city
                        currentRoute = currentRoute.concat(routeHexes[0]);
                        int zero = Integer.valueOf((routeHexes[0].charAt(0)));
                        int one = Integer.valueOf((routeHexes[1].charAt(0)));
                        int two = Integer.valueOf((routeHexes[2].charAt(0)));
                        int A = (int)('A');
                        column1 = zero - A; //(int)(routeHexes[0].charAt(0)) - (int)('A');
                        column2 = one -A;//(int)(routeHexes[1].charAt(0)) - (int)('A');
                        column3 = two - A; //(int)(routeHexes[2].charAt(0)) - (int)('A');
                        row1 = Integer.parseInt(routeHexes[0].substring(1));
                        row2 = Integer.parseInt(routeHexes[1].substring(1));
                        row3 = Integer.parseInt(routeHexes[2].substring(1));
                        // Draw a line from the middle of the start hex to the edge of the end hex.
                       // theMap[row2][column2].drawRoute(OffscreenG, TileRenderor.HALF_STRAIGHT,getRouteOrientation(column2, column1, row2, row1), routeColor[i]);
                        System.out.println(theMap[row2][column2] + " " + column2 + " " + column1 + " " + row2 + " "+row1 + " " + routeColor[i]);
                        // Draw a line from the middle of the start hex to the edge of the end hex.
                        //theMap[row2][column2].drawRoute(OffscreenG, TileRenderor.HALF_STRAIGHT,getRouteOrientation(column2, column3, row2, row3), routeColor[i]);
                        System.out.println(theMap[row2][column2] + " " + column2 + " " + column3 + " " + row2 + " "+row3 + " " + routeColor[i]);
                    }
                    else if (legSpot < citySpot)
                    {
                        // do leg stuff
                        currentRoute = currentRoute.concat(routeHexes[0]);
                        // Draw an arc going from routeHexes[0] to routeHexes[2] passing through routeHexes[1]
                        int zero = Integer.valueOf((routeHexes[0].charAt(0)));
                        int one = Integer.valueOf((routeHexes[1].charAt(0)));
                        int two = Integer.valueOf((routeHexes[2].charAt(0)));
                        int A = (int)('A');
                        column1 = zero - A; //(int)(routeHexes[0].charAt(0)) - (int)('A');
                        column2 = one - A; //(int)(routeHexes[1].charAt(0)) - (int)('A');
                        column3 = two - A; //(int)(routeHexes[2].charAt(0)) - (int)('A');
                        row1 = Integer.parseInt(routeHexes[0].substring(1));
                        row2 = Integer.parseInt(routeHexes[1].substring(1));
                        row3 = Integer.parseInt(routeHexes[2].substring(1));
                        if (column1 > column3)
                        {
                            int temp = column3;
                            column3= column1;
                            column1 = temp;
                            temp = row3;
                            row3 = row1;
                            row1 = temp;
                        }
                        if (column1 < column3)
                        {
                            if (column2 == column3)
                            {
                                if ((row1-1) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.TIGHT_CURVE,
//                                                                    TileRenderor.NW,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else if ((row1+3) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.GENTLE_CURVE,
//                                                                    TileRenderor.S,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else if ((row1+1) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.TIGHT_CURVE,
//                                                                    TileRenderor.S,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.GENTLE_CURVE,
//                                                                    TileRenderor.SW,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                            }
                            else if (column2 == column1)
                            {
                                if ((row1+1) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.TIGHT_CURVE,
//                                                                    TileRenderor.N,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else if ((row1+3) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.GENTLE_CURVE,
//                                                                    TileRenderor.N,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else if ((row1-1) == row3)
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.TIGHT_CURVE,
//                                                                    TileRenderor.SE,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                                else
                                {
//                                    theMap[row2][column2].drawRoute(OffscreenG,
//                                                                    TileRenderor.GENTLE_CURVE,
//                                                                    TileRenderor.NE,
//                                                                    routeColor[i]);
                                	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                }
                            }
                            else /* (column1 < column2 < column3) */
                            {
                                if (row1 < row2)
                                {
                                    if (row1 == row3)
                                    {
//                                        theMap[row2][column2].drawRoute(OffscreenG,
//                                                                        TileRenderor.GENTLE_CURVE,
//                                                                        TileRenderor.NW,
//                                                                        routeColor[i]);
                                    	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                    }
                                    else
                                    {
//                                        theMap[row2][column2].drawRoute(OffscreenG,
//                                                                        TileRenderor.STRAIGHT,
//                                                                        TileRenderor.NW,
//                                                                        routeColor[i]);
                                    	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                    }
                                }
                                else if (row1 > row2)
                                {
                                    if (row1 == row3)
                                    {
//                                        theMap[row2][column2].drawRoute(OffscreenG,
//                                                                        TileRenderor.GENTLE_CURVE,
//                                                                        TileRenderor.SE,
//                                                                        routeColor[i]);
                                    	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                    }
                                    else
                                    {
//                                        theMap[row2][column2].drawRoute(OffscreenG,
//                                                                        TileRenderor.STRAIGHT,
//                                                                        TileRenderor.SW,
//                                                                        routeColor[i]);
                                    	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                                    }
                                }
                            }
                        }
                        else if (column1 == column3)
                        {
                            if (column1 < column2)
                            {
//                                theMap[row2][column2].drawRoute(OffscreenG,
//                                                                TileRenderor.TIGHT_CURVE,
//                                                                TileRenderor.SW,
//                                                                routeColor[i]);
                            	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                            }
                            else if (column1 > column2)
                            {
//                                theMap[row2][column2].drawRoute(OffscreenG,
//                                                                TileRenderor.TIGHT_CURVE,
//                                                                TileRenderor.NE,
//                                                                routeColor[i]);
                            	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                            }
                            else
                            {
//                                theMap[row2][column2].drawRoute(OffscreenG,
//                                                                TileRenderor.STRAIGHT,
//                                                                TileRenderor.S,
//                                                                routeColor[i]);
                            	System.out.println(theMap[row2][column2] +" " + routeColor[i]);
                            }
                        }
                    }
                    else /* if (endSpot < legSpot && endSpot < citySpot) */
                    {
                        // do end stuff.  Handled above ...
                        ;
                    }
                }
            //}
        }
    }
    
    static Color [] routeColor = { Color.cyan, new Color(235, 136, 23) /* violet */,
                                   Color.blue, new Color(255, 128, 0) /* orange */ };
}