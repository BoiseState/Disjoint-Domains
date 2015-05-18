/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package test;
//http://sourceforge.net/projects/abbadon

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * @author -Nemesiss-
 */
public class GeoEngine
{
	private static Logger _log = Logger.getLogger(GeoData.class.getName());
	private static GeoEngine _instance;
	private final static byte _e = 1;
	private final static byte _w = 2;
	private final static byte _s = 4;
	private final static byte _n = 8;
//	private static Map<Short, MappedByteBuffer> _geodata = new FastMap<Short, MappedByteBuffer>();
//	private static Map<Short, IntBuffer> _geodataIndex = new FastMap<Short, IntBuffer>();
	private static BufferedOutputStream _geoBugsOut;

	public static GeoEngine getInstance()
	{
		if (_instance == null) {
			_instance = new GeoEngine();
		}
		return _instance;
	}
	
//	private static boolean canSee(int x, int y, int z, int tx, int ty, int tz)
//	{
//		int dx = tx - x;
//		int dy = ty - y;
//		final int dz = tz - z;
//		final int distance2 = dx * dx + dy * dy;
//		if (distance2 > 90000) {
//			return false; // Avoid too long check
//		} else if (distance2 < 82)
//		{
//			// 200 too deep/high. This value should be in sync with NLOS
//			if (dz * dz > 40000)
//			{
//				short region = getRegionOffset(x, y);
//				// geodata is loaded for region and mobs should have correct Z coordinate...
//				// so there would likely be a floor in between the two
////				if (_geodata.get(region) != null) {
////					return false;
////				}
//			}
//			return true;
//		}
//		// Increment in Z coordinate when moving along X or Y axis
//		// and not straight to the target. This is done because
//		// calculation moves either in X or Y direction.
//		final int inc_x = dx > 0 ? 1 : -1; //sign(dx);
//		final int inc_y = dy > 0? 1 : -1; //sign(dy);
//		dx = dx < 0 ? dx *-1 : dx; //Math.abs(dx);
//		dy = dy <0 ? dy * -1 : dy; //Math.abs(dy);
//		final double inc_z_directionx = dz * dx / distance2;
//		final double inc_z_directiony = dz * dy / distance2;
//		// next_* are used in NLOS check from x,y
//		int next_x = x;
//		int next_y = y;
//		// creates path to the target
//		// calculation stops when next_* == target
//		if (dx >= dy)// dy/dx <= 1
//		{
//			int delta_A = 2 * dy;
//			int d = delta_A - dx;
//			int delta_B = delta_A - 2 * dx;
//			for (int i = 0; i < dx; i++)
//			{
//				x = next_x;
//				y = next_y;
//				if (d > 0)
//				{
//					d += delta_B;
//					next_x += inc_x;
//					z += inc_z_directionx;
//					next_y += inc_y;
//					z += inc_z_directiony;
//					// _log.warning("1: next_x:"+next_x+" next_y"+next_y);
////					if (!nLOS(x, y, (int) z, inc_x, inc_y, tz, false)) {
////						return false;
////					}
//				}
//				else
//				{
//					d += delta_A;
//					next_x += inc_x;
//					// _log.warning("2: next_x:"+next_x+" next_y"+next_y);
//					z += inc_z_directionx;
////					if (!nLOS(x, y, (int) z, inc_x, 0, tz, false)) {
////						return false;
////					}
//				}
//			}
//		}
//		else
//		{
//			int delta_A = 2 * dx;
//			int d = delta_A - dy;
//			int delta_B = delta_A - 2 * dy;
//			for (int i = 0; i < dy; i++)
//			{
//				x = next_x;
//				y = next_y;
//				if (d > 0)
//				{
//					d += delta_B;
//					next_y += inc_y;
//					z += inc_z_directiony;
//					next_x += inc_x;
//					z += inc_z_directionx;
//					// _log.warning("3: next_x:"+next_x+" next_y"+next_y);
////					if (!nLOS(x, y, (int) z, inc_x, inc_y, tz, false)) {
////						return false;
////					}
//				}
//				else
//				{
//					d += delta_A;
//					next_y += inc_y;
//					// _log.warning("4: next_x:"+next_x+" next_y"+next_y);
//					z += inc_z_directiony;
////					if (!nLOS(x, y, (int) z, 0, inc_y, tz, false)) {
////						return false;
////					}
//				}
//			}
//		}
//		return true;
//	}

	/*
	 * Debug function for checking if there's a line of sight between two coordinates. Creates points for line of sight check (x,y,z towards target) and in each point, layer and movement checks are made with NLOS function. Coordinates here are geodata x,y but z coordinate is world coordinate
	 */
//	private static boolean canSeeDebug(int x, int y, int z, int tx, int ty, int tz)
//	{
//		int dx = tx - x;
//		int dy = ty - y;
//		final int dz = tz - z;
//		final int distance2 = dx * dx + dy * dy;
//		if (distance2 > 90000) // (300*300) 300*16 = 4800 in world coord
//		{
//			// Avoid too long check
//			//gm.sendMessage("dist > 300");
//			return false;
//		}
//		// very short checks: 9 => 144 world distance
//		// this ensures NLOS function has enough points to calculate,
//		// it might not work when distance is small and path vertical
//		else if (distance2 < 82)
//		{
//			// 200 too deep/high. This value should be in sync with NLOS
//			if (dz * dz > 40000)
//			{
//				short region = getRegionOffset(x, y);
//				// geodata is loaded for region and mobs should have correct Z coordinate...
//				// so there would likely be a floor in between the two
////				if (_geodata.get(region) != null) {
////					return false;
////				}
//			}
//			return true;
//		}
//		// Increment in Z coordinate when moving along X or Y axis
//		// and not straight to the target. This is done because
////		// calculation moves either in X or Y direction.
////		final int inc_x = sign(dx);
////		final int inc_y = sign(dy);
////		dx = Math.abs(dx);
////		dy = Math.abs(dy);
//		final int inc_x = dx > 0 ? 1 : -1;
//		final int inc_y = dy >0 ? 1 : -1;
//		dx = dx < 0 ? dx * -1 : dx; // Math.abs(dx);
//		dy = dy < 0 ? dy*-1 : dy; //Math.abs(dy);
//		final double inc_z_directionx = dz * dx / distance2;
//		final double inc_z_directiony = dz * dy / distance2;
//		//gm.sendMessage("Los: from X: " + x + "Y: " + y + "--->> X: " + tx + " Y: " + ty);
//		// next_* are used in NLOS check from x,y
//		int next_x = x;
//		int next_y = y;
//		// creates path to the target
//		// calculation stops when next_* == target
//		if (dx >= dy)// dy/dx <= 1
//		{
//			int delta_A = 2 * dy;
//			int d = delta_A - dx;
//			int delta_B = delta_A - 2 * dx;
//			for (int i = 0; i < dx; i++)
//			{
//				x = next_x;
//				y = next_y;
//				if (d > 0)
//				{
//					d += delta_B;
//					next_x += inc_x;
//					z += inc_z_directionx;
//					next_y += inc_y;
//					z += inc_z_directiony;
//					// _log.warning("1: next_x:"+next_x+" next_y"+next_y);
////					if (!nLOS(x, y, (int) z, inc_x, inc_y, tz, true)) {
////						return false;
////					}
//				}
//				else
//				{
//					d += delta_A;
//					next_x += inc_x;
//					// _log.warning("2: next_x:"+next_x+" next_y"+next_y);
//					z += inc_z_directionx;
////					if (!nLOS(x, y, (int) z, inc_x, 0, tz, true)) {
////						return false;
////					}
//				}
//			}
//		}
//		else
//		{
//			int delta_A = 2 * dx;
//			int d = delta_A - dy;
//			int delta_B = delta_A - 2 * dy;
//			for (int i = 0; i < dy; i++)
//			{
//				x = next_x;
//				y = next_y;
//				if (d > 0)
//				{
//					d += delta_B;
//					next_y += inc_y;
//					z += inc_z_directiony;
//					next_x += inc_x;
//					z += inc_z_directionx;
//					// _log.warning("3: next_x:"+next_x+" next_y"+next_y);
////					if (!nLOS(x, y, (int) z, inc_x, inc_y, tz, true)) {
////						return false;
////					}
//				}
//				else
//				{
//					d += delta_A;
//					next_y += inc_y;
//					// _log.warning("4: next_x:"+next_x+" next_y"+next_y);
//					z += inc_z_directiony;
////					if (!nLOS(x, y, (int) z, 0, inc_y, tz, true)) {
////						return false;
////					}
//				}
//			}
//		}
//		return true;
//	}

	/*
	 * MoveCheck
	 */
	private static Object moveCheck(Object startpoint, Object destiny, int x, int y, int z, int tx, int ty, int tz)
	{
		int dx = tx - x;
		int dy = ty - y;
		final int distance2 = dx * dx + dy * dy;
		if (distance2 == 0) {
			return destiny;
		}
		if (distance2 > 36100) // 190*190*16 = 3040 world coord
		{
			// Avoid too long check
			// Currently we calculate a middle point
			// for wyvern users and otherwise for comfort
			int divider = (int) Math.sqrt((double) 30000 / distance2);
			tx = x + divider * dx;
			ty = y + divider * dy;
			int dz = tz;
			tz = divider * dz;
			dx = tx - x;
			dy = ty - y;
			// return startpoint;
		}
		// Increment in Z coordinate when moving along X or Y axis
		// and not straight to the target. This is done because
		// calculation moves either in X or Y direction.
		final int inc_x = dx > 0 ? 1 : -1;
		final int inc_y = dy >0 ? 1 : -1;
		dx = dx < 0 ? dx * -1 : dx; // Math.abs(dx);
		dy = dy < 0 ? dy*-1 : dy; //Math.abs(dy);
		// gm.sendMessage("MoveCheck: from X: "+x+ "Y: "+y+ "--->> X: "+tx+" Y: "+ty);
		// next_* are used in NcanMoveNext check from x,y
		int next_x = x;
		int next_y = y;
		//double tempz = z;
		// creates path to the target, using only x or y direction
		// calculation stops when next_* == target
		if (dx >= dy)// dy/dx <= 1
		{
			int delta_A = 2 * dy;
			int d = delta_A - dx;
			int delta_B = delta_A - 2 * dx;
			for (int i = 0; i < dx; i++)
			{
				x = next_x;
				y = next_y;
				if (d > 0)
				{
					d += delta_B;
					next_x += inc_x;
					next_y += inc_y;
					// _log.warning("2: next_x:"+next_x+" next_y"+next_y);
//					tempz = nCanMoveNext(x, y, (int) z, next_x, next_y, tz);
//					if (tempz == Double.MIN_VALUE) {
//						return new Object(); //new Location((x << 4) + L2World.MAP_MIN_X, (y << 4) + L2World.MAP_MIN_Y, (int) z);
//					} else {
//						z = tempz;
//					}
				}
				else
				{
					d += delta_A;
					next_x += inc_x;
					// _log.warning("3: next_x:"+next_x+" next_y"+next_y);
//					tempz = nCanMoveNext(x, y, (int) z, next_x, next_y, tz);
//					if (tempz == Double.MIN_VALUE) {
//						return new Object(); //new Location((x << 4) + L2World.MAP_MIN_X, (y << 4) + L2World.MAP_MIN_Y, (int) z);
//					} else {
//						z = tempz;
//					}
				}
			}
		}
		else
		{
			int delta_A = 2 * dx;
			int d = delta_A - dy;
			int delta_B = delta_A - 2 * dy;
			for (int i = 0; i < dy; i++)
			{
				x = next_x;
				y = next_y;
				if (d > 0)
				{
					d += delta_B;
					next_y += inc_y;
					next_x += inc_x;
					// _log.warning("5: next_x:"+next_x+" next_y"+next_y);
//					tempz = nCanMoveNext(x, y, (int) z, next_x, next_y, tz);
//					if (tempz == Double.MIN_VALUE) {
//						return new Object(); //new Location((x << 4) + L2World.MAP_MIN_X, (y << 4) + L2World.MAP_MIN_Y, (int) z);
//					} else {
//						z = tempz;
//					}
				}
				else
				{
					d += delta_A;
					next_y += inc_y;
					// _log.warning("6: next_x:"+next_x+" next_y"+next_y);
//					tempz = nCanMoveNext(x, y, (int) z, next_x, next_y, tz);
//					if (tempz == Double.MIN_VALUE) {
//						return new Object(); //new Location((x << 4) + L2World.MAP_MIN_X, (y << 4) + L2World.MAP_MIN_Y, (int) z);
//					} else {
//						z = tempz;
//					}
				}
			}
		}
		return destiny; // should actually return correct z here instead of tz
	}

//	private static byte sign(int x)
//	{
//		if (x >= 0) {
//			return +1;
//		} else {
//			return -1;
//		}
//	}

	// GeoEngine

	// Geodata Methods
	/**
	 * @param x
	 * @param y
	 * @return Region Offset
	 */
	private static short getRegionOffset(int x, int y)
	{
		int rx = x >> 11; // =/(256 * 8)
		int ry = y >> 11;
		return (short) ((rx + 16 << 5) + ry + 10);
	}

	/**
	 * @param pos
	 * @return Block Index: 0-255
	 */
	private static int getBlock(int geo_pos)
	{
		return (geo_pos >> 3) % 256;
	}

	/**
	 * @param pos
	 * @return Cell Index: 0-7
	 */
	private static int getCell(int geo_pos)
	{
		return geo_pos % 8;
	}

	// Geodata Functions


	

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param tx
	 * @param ty
	 * @param tz
	 * @return True if char can move to (tx,ty,tz)
	 */
	private static double nCanMoveNext(int x, int y, int z, int tx, int ty, int tz, byte type, short inShort, Object o)
	{
		short region = getRegionOffset(x, y);
		int blockX = getBlock(x);
		int blockY = getBlock(y);
		int cellX, cellY;
		short NSWE = 0;
		int index = 0;
		// Geodata without index - it is just empty so index can be calculated on the fly
		//if (_geodataIndex.get(region) == null) {
		if (o == null) {
			index = ((blockX << 8) + blockY) * 3;
		// Get Index for current block of current region geodata
		} else {
			//index = _geodataIndex.get(region).get((blockX << 8) + blockY);
			index = (int)Math.random()*100 + blockY;
		}
		// Buffer that Contains current Region GeoData
		ByteBuffer geo = (ByteBuffer)o;//_geodata.get(region);
		if (geo == null)
		{
//			if (Config.DEBUG) {
//				_log.warning("Geo Region - Region Offset: " + region + " dosnt exist!!");
//			}
			return z;
		}
		// Read current block type: 0-flat,1-complex,2-multilevel
		index++;
		if (type == 0) {
			return z;
		} else if (type == 1) // complex
		{
			cellX = getCell(x);
			cellY = getCell(y);
			index += (cellX << 3) + cellY << 1;
			short height = inShort; //geo.getShort(index);
			NSWE = (short) (height & 0x0F);
			height = (short) (height & 0x0fff0);
			height = (short) (height >> 1); // height / 2
			if (checkNSWE(NSWE, x, y, tx, ty)) {
				return height;
			} else {
				return Double.MIN_VALUE;
			}
		}
		else
		// multilevel, type == 2
		{
			cellX = getCell(x);
			cellY = getCell(y);
			int offset = (cellX << 3) + cellY;
			while (offset > 0) // iterates (too many times?) to get to layer count
			{
				short lc = inShort;
				index += (lc << 1) + 1;
				offset--;
			}
			int layers = (int) Math.random()*100;
			// _log.warning("layers"+layers);
			index++;
			short height = -1;
			if (layers <= 0 || layers > 125)
			{
				_log.warning("Broken geofile (case3), region: " + region + " - invalid layer count: " + layers + " at: " + x + " " + y);
				return z;
			}
			short tempz = Short.MIN_VALUE;
			while (layers > 0)
			{
				height = inShort;
				height = (short) (height & 0x0fff0);
				height = (short) (height >> 1); // height / 2
				// searches the closest layer to current z coordinate
				if ((z - tempz) * (z - tempz) > (z - height) * (z - height))
				{
					// layercurr = layers;
					tempz = height;
					NSWE = inShort;
					NSWE = (short) (NSWE & 0x0F);
				}
				layers--;
				index += 2;
			}
			if (checkNSWE(NSWE, x, y, tx, ty)) {
				return tempz;
			} else {
				return Double.MIN_VALUE;
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param inc_x
	 * @param inc_y
	 * @param tz
	 * @return True if Char can see target
	 */
	private static boolean nLOS(int x, int y, int z, int inc_x, int inc_y, int tz, boolean debug, short inShort, byte inByte, int index)
	{
		short region = getRegionOffset(x, y);
		int blockX = getBlock(x);
		int blockY = getBlock(y);
		int cellX, cellY;
		short NSWE = 0;
//		// Geodata without index - it is just empty so index can be calculated on the fly
//		if (_geodataIndex.get(region) == null) {
//			index = ((blockX << 8) + blockY) * 3;
//		// Get Index for current block of current region geodata
//		} else {
//			index = _geodataIndex.get(region).get((blockX << 8) + blockY);
//		}
//		// Buffer that Contains current Region GeoData
//		ByteBuffer geo = _geodata.get(region);
//		if (geo == null)
//		{
//			if (Config.DEBUG) {
//				_log.warning("Geo Region - Region Offset: " + region + " dosnt exist!!");
//			}
//			return true;
//		}
		// Read current block type: 0-flat,1-complex,2-multilevel
		byte type = inByte; //geo.get(index);
		index++;
		if (type == 0) // flat, movement and sight always possible
		{
//			if (debug) {
//				_log.warning("flatheight:" + geo.getShort(index));
//			}
			return true;
		}
		else if (type == 1) // complex
		{
			cellX = getCell(x);
			cellY = getCell(y);
			index += (cellX << 3) + cellY << 1;
			short height = inShort; //geo.getShort(index);
			NSWE = (short) (height & 0x0F);
			height = (short) (height & 0x0fff0);
			height = (short) (height >> 1); // height / 2
//			if (debug)
//			{
//				_log.warning("height:" + height + " z" + z);
//				if (!checkNSWE(NSWE, x, y, x + inc_x, y + inc_y)) {
//					_log.warning("would block");
//				}
//			}
			if (z - height > 50) {
				return true; // this value is just an approximate
			}
		}
		else
		// multilevel, type == 2
		{
			cellX = getCell(x);
			cellY = getCell(y);
			int offset = (cellX << 3) + cellY;
			while (offset > 0) // iterates (too many times?) to get to layer count
			{
				byte lc = inByte; //geo.get(index);
				index += (lc << 1) + 1;
				offset--;
			}
			byte layers = inByte; //geo.get(index);
//			if (debug) {
//				_log.warning("layers" + layers);
//			}
			index++;
			short height = -1;
			if (layers <= 0 || layers > 125)
			{
				_log.warning("Broken geofile (case4), region: " + region + " - invalid layer count: " + layers + " at: " + x + " " + y);
				return false;
			}
			short tempz = Short.MIN_VALUE; // big negative value
			byte temp_layers = layers;
			boolean highestlayer = true;
			z -= 25; // lowering level temporarily to avoid selecting ceiling
			while (temp_layers > 0)
			{
				// reads height for current layer, result in world z coordinate
				height = inShort; //geo.getShort(index);
				height = (short) (height & 0x0fff0);
				height = (short) (height >> 1); // height / 2
				// height -= 8; // old geo files had -8 around giran, new data seems better
				// searches the closest layer to current z coordinate
				if ((z - tempz) * (z - tempz) > (z - height) * (z - height))
				{
					if (tempz > Short.MIN_VALUE) {
						highestlayer = false;
					}
					tempz = height;
//					if (debug) {
//						_log.warning("z" + (z + 45) + " tempz" + tempz + " dz" + (z - tempz));
//					}
					NSWE = inShort;//geo.getShort(index);
					NSWE = (short) (NSWE & 0x0F);
				}
				temp_layers--;
				index += 2;
			}
			z += 25; // level rises back
			// Check if LOS goes under a layer/floor
			if (z - tempz < -20) {
				return false; // -20 => clearly under, approximates also fence width
			}
			// this helps in some cases (occasional under-highest-layer block which isn't wall)
			// but might also create problems in others (passes walls when you're standing high)
			if (z - tempz > 250) {
				return true;
			}
			// or there's a fence/wall ahead when we're not on highest layer
			// this part of the check is problematic
			if (!highestlayer)
			{
				// a probable wall, there's movement block and layers above you
				if (!checkNSWE(NSWE, x, y, x + inc_x, y + inc_y)) // cannot move
				{
					// the height after 2 inc_x,inc_y
					short nextheight = inShort; //nGetHeight(x + 2 * inc_x, y + 2 * inc_y, z - 50);
//					if (debug)
//					{
//						_log.warning("0: z:" + z + " tz" + nGetHeight(x, y, z - 60));
//						_log.warning("1: z:" + z + " tz" + nGetHeight(x + inc_x, y + inc_y, z - 60));
//						_log.warning("2: z:" + z + " tz" + nGetHeight(x + 2 * inc_x, y + 2 * inc_y, z - 60));
//						_log.warning("3: z:" + z + " tz" + nGetHeight(x + 3 * inc_x, y + 3 * inc_y, z - 60));
//					}
					// Probably a very thin fence (e.g. castle fences above artefact),
					// where height instantly drops after 1-2 cells and layer ends.
					if (z - nextheight > 100) {
						return true;
					}
					// layer continues so close we can see over it
					if (nextheight - tempz > 5 && nextheight - tempz < 20) {
						return true;
					}
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return checkNSWE(NSWE, x, y, x + inc_x, y + inc_y);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return NSWE: 0-15
	 */
	private short nGetNSWE(int x, int y, int z, byte inByte, short inShort)
	{
		short region = getRegionOffset(x, y);
		int blockX = getBlock(x);
		int blockY = getBlock(y);
		int cellX, cellY;
		short NSWE = 0;
		int index = 0;
		// Geodata without index - it is just empty so index can be calculated on the fly
//		if (_geodataIndex.get(region) == null) {
//			index = ((blockX << 8) + blockY) * 3;
//		// Get Index for current block of current region geodata
//		} else {
//			index = _geodataIndex.get(region).get((blockX << 8) + blockY);
//		}
//		// Buffer that Contains current Region GeoData
//		ByteBuffer geo = _geodata.get(region);
//		if (geo == null)
//		{
//			if (Config.DEBUG) {
//				_log.warning("Geo Region - Region Offset: " + region + " dosnt exist!!");
//			}
//			return 15;
//		}
		// Read current block type: 0-flat,1-complex,2-multilevel
		byte type = inByte; //geo.get(index);
		index++;
		if (type == 0) {
			return 15;
		} else if (type == 1)// complex
		{
			cellX = getCell(x);
			cellY = getCell(y);
			index += (cellX << 3) + cellY << 1;
			short height = inShort; //geo.getShort(index);
			NSWE = (short) (height & 0x0F);
		}
		else
		// multilevel
		{
			cellX = getCell(x);
			cellY = getCell(y);
			int offset = (cellX << 3) + cellY;
			while (offset > 0)
			{
				byte lc = inByte; //geo.get(index);
				index += (lc << 1) + 1;
				offset--;
			}
			byte layers = inByte; //geo.get(index);
			index++;
			short height = -1;
			if (layers <= 0 || layers > 125)
			{
				_log.warning("Broken geofile (case5), region: " + region + " - invalid layer count: " + layers + " at: " + x + " " + y);
				return 15;
			}
			short tempz = Short.MIN_VALUE;
			while (layers > 0)
			{
				height = inShort; //geo.getShort(index);
				height = (short) (height & 0x0fff0);
				height = (short) (height >> 1); // height / 2
				if ((z - tempz) * (z - tempz) > (z - height) * (z - height))
				{
					tempz = height;
					NSWE = inShort; ///geo.get(index);
					NSWE = (short) (NSWE & 0x0F);
				}
				layers--;
				index += 2;
			}
		}
		return NSWE;
	}

	/**
	 * @param NSWE
	 * @param x
	 * @param y
	 * @param tx
	 * @param ty
	 * @return True if NSWE dont block given direction
	 */
	private static boolean checkNSWE(short NSWE, int x, int y, int tx, int ty)
	{
		// Check NSWE
		if (NSWE == 15) {
			return true;
		}
		if (tx > x)// E
		{
			if ((NSWE & _e) == 0) {
				return false;
			}
		}
		if (tx < x)// W
		{
			if ((NSWE & _w) == 0) {
				return false;
			}
		}
		if (ty > y)// S
		{
			if ((NSWE & _s) == 0) {
				return false;
			}
		}
		if (ty < y)// N
		{
			if ((NSWE & _n) == 0) {
				return false;
			}
		}
		return true;
	}
}
