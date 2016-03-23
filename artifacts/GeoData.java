/*
 * This file is part of aion-yustiel <aion-yustiel.com>.
 *
 *  aion-yustiel is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-yustiel is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-yustiel.  If not, see <http://www.gnu.org/licenses/>.
 */

//http://sourceforge.net/projects/optimisation

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author leolegolas
 *
 */
public class GeoData
{

	/**
	 * @param worldId
	 * @param x
	 * @param y
	 * @param clientZ
	 * @return fixed z
	 */
	public static float getZ(int worldId,  int x, int y, int clientZ) {
		int z = 0;
		int x0 = x / 2;
		int y0 = y / 2;
		int x1 = x0;
		int y1 = y0;
		int x2 = x0;
		int y2 = y0 + 1;
		int x3 = x0 + 1;
		int y3 = y0;
		int x4 = x0 + 1;
		int y4 = y0 + 1;
		int z1 = 0;
		int z2 = 0;
		int z3 = 0;
		int z4 = 0;
		int z12 = 0;
		int z23 = 0;
		int z34 = 0;
		int z41 = 0;
		int z1234 = 0;
		int z2341 = 0;
		
		int fraction = 0;
		int number = 0;
		long filePosition = 0;
		int side = 0;
		
		RandomAccessFile geoFile;
		try
		{
			geoFile = new RandomAccessFile(new File("./data/geo/"+String.valueOf(worldId)+".map"), "r");
			side = (int) Math.sqrt(geoFile.length() / 2);
			/*
			 * P1
			 */
			filePosition = 2 * (y1 + x1 * side);
			geoFile.seek(filePosition);
			number = geoFile.read();
			fraction = geoFile.read();
			z1 = (int) (number * 8 + fraction * 0.03125F);

			/*
			 * P2
			 */
			filePosition = 2 * (y2 + x2 * side);
			geoFile.seek(filePosition);
			number = geoFile.read();
			fraction = geoFile.read();
			z2 = (int) (number * 8F + fraction * 0.03125F);

			/*
			 * P3
			 */
			filePosition = 2 * (y3 + x3 * side);
			geoFile.seek(filePosition);
			number = geoFile.read();
			fraction = geoFile.read();
			z3 = (int) (number * 8F + fraction * 0.03125F);

			/*
			 * P4
			 */
			filePosition = 2 * (y4 + x4 * side);
			geoFile.seek(filePosition);
			number = geoFile.read();
			fraction = geoFile.read();
			z4 = (int) (number * 8F + fraction * 0.03125F);

			geoFile.close();

			/*
			 * Linear interpolation with square around P(x,y)
			 */
			/*
			 * P12
			 */
			z12 = (y0 - y1) * (z2 - z1) + z1;

			/*
			 * P23
			 */
			z23 = (x0 - x1) * (z3 - z2) + z2;

			/*
			 * P34
			 */
			z34 = (y0 - y1) * (z4 - z3) + z3;

			/*
			 * P41
			 */
			z41 = (x0 - x1) * (z1 - z4) + z4;

			/*
			 * P1234
			 */
			z1234 = (x0 - x1) * (z34 - z12) + z12;

			/*
			 * P2341
			 */
			z2341 = (y0 - y1) * (z23 - z41) + z41;
			
			z = (z1234 + z2341) / 2;
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			z = clientZ;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			z = clientZ;
		}
		return z;
	}
}
