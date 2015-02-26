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
public class NorthEastFace extends Face
{
	public NorthEastFace()
	{
		super(new Vertex[] { new Vertex.TopSouthEast(), new Vertex.BottomSouthEast(), new Vertex.BottomNorthWest(),
				new Vertex.TopNorthWest() });

		params.direction.set(EAST);
		params.textureSide.set(EAST);
		params.colorFactor.set(0.7F);
		params.aoMatrix.set(new int[][][] {
				{ FacePresets.aom("Top"), FacePresets.aom("TopSouth"), FacePresets.aom("TopSouthEast"), FacePresets.aom("SouthEast") },
				{ FacePresets.aom("Bottom"), FacePresets.aom("BottomSouth"), FacePresets.aom("BottomSouthEast"),
						FacePresets.aom("SouthEast") },
				{ FacePresets.aom("Bottom"), FacePresets.aom("BottomWest"), FacePresets.aom("BottomNorthWest"),
						FacePresets.aom("NorthWest") },
				{ FacePresets.aom("Top"), FacePresets.aom("TopWest"), FacePresets.aom("TopNorthWest"), FacePresets.aom("NorthWest") } });
	}
}
