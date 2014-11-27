package net.malisis.bt.renderer;

import net.malisis.core.renderer.MalisisRenderer;
import net.minecraft.util.AxisAlignedBB;

public class FiniteLiquidRenderer extends MalisisRenderer
{
	public static int renderId;

	@Override
	protected void initialize()
	{
		rp.useBlockBounds.set(false);
	}

	@Override
	public void render()
	{
		rp.renderBounds.set(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, (double) blockMetadata / 16, 1));
		shape.resetState();
		drawShape(shape);

	}
}
