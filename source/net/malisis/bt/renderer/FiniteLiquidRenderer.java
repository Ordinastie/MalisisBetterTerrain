package net.malisis.bt.renderer;

import net.malisis.core.renderer.BaseRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.preset.ShapePreset;
import net.minecraft.util.AxisAlignedBB;

public class FiniteLiquidRenderer extends BaseRenderer
{
	public static int renderId;

	@Override
	protected void initParameters()
	{
		rp = new RenderParameters();
		rp.useBlockBounds.set(false);
	}

	@Override
	protected void initShapes()
	{
		shape = ShapePreset.Cube();
	}

	@Override
	public void render()
	{
		rp.renderBounds.set(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, (double) blockMetadata / 16, 1));
		shape.resetState();
		drawShape(shape);

	}
}
