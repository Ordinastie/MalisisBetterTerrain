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

package net.malisis.bt.renderer.face;

import static net.minecraftforge.common.util.ForgeDirection.*;
import net.malisis.bt.renderer.FacePresets;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Vertex;

/**
 * @author Ordinastie
 *
 */
public class InvTopSouthWestFace extends Face
{
	public InvTopSouthWestFace()
	{
		super(new Vertex[] { new Vertex.TopNorthWest(), new Vertex.BottomSouthWest(), new Vertex.BottomSouthWest(),
				new Vertex.TopSouthEast() });

		params.direction.set(UP);
		params.textureSide.set(UP);
		params.colorFactor.set(0.8F);
		params.aoMatrix.set(new int[][][] {
				{ FacePresets.aom("TopNorth"), FacePresets.aom("TopNorthWest"), FacePresets.aom("TopWest"), FacePresets.aom("West") },
				{ FacePresets.aom("BottomSouth"), FacePresets.aom("SouthWest"), FacePresets.aom("BottomWest") },
				{ FacePresets.aom("BottomSouth"), FacePresets.aom("SouthWest"), FacePresets.aom("BottomWest") },
				{ FacePresets.aom("TopEast"), FacePresets.aom("TopSouthEast"), FacePresets.aom("TopSouth"), FacePresets.aom("South") } });
	}
}
