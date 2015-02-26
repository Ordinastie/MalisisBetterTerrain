/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.malisis.bt.renderer;

import java.util.HashMap;

public class FacePresets
{

	private static HashMap<String, int[]> aom = new HashMap<String, int[]>();
	static
	{
		buildDirectionMatrix();
	}

	/**
	 * Shortcut to get the aom mapping for a named position
	 *
	 * @param s
	 * @return
	 */
	public static int[] aom(String s)
	{
		int[] a = aom.get(s);
		return a == null ? new int[] { 0, 0, 0 } : a;
	}

	/**
	 * Build a mapping between name and position (ie TopSouthWest => 0,1,1) for all 27 possibilities
	 */
	private static void buildDirectionMatrix()
	{
		int[] a = { -1, 0, 1 };
		for (int x : a)
			for (int y : a)
				for (int z : a)
					aom.put(dirToString(x, y, z), new int[] { x, y, z });
	}

	/**
	 * Get a name for a specific position
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private static String dirToString(int x, int y, int z)
	{
		String s = "";
		s += y == 0 ? "" : y == 1 ? "Top" : "Bottom";
		s += z == 0 ? "" : z == 1 ? "South" : "North";
		s += x == 0 ? "" : x == 1 ? "East" : "West";
		return s != "" ? s : "Center";
	}

}
