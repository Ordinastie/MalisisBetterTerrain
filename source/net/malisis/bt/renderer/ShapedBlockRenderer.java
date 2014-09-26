package net.malisis.bt.renderer;

import net.malisis.bt.block.IShapedBlock;
import net.malisis.bt.block.ShapedBlockManager;
import net.malisis.bt.block.ShapedGrass;
import net.malisis.core.renderer.BaseRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.preset.ShapePreset;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ShapedBlockRenderer extends BaseRenderer
{
	public static int renderId = 0;
	private int colorMultiplier;
	private boolean isGrassBlock = false;
	private boolean grassSideOverlay = false;
	private IIcon grassOverlayIcon = null;

	@Override
	protected void initParameters()
	{
		rp = new RenderParameters();
		rp.useBlockBounds.set(false);
		rp.useBlockBrightness.set(true);
	}

	/**
	 * Overridden because of the case where two block of the same slope next to each other shouldn't cast any shadow
	 */
	@Override
	protected float getBlockAmbientOcclusion(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if (block == null)
			return 1.0F;

		if (block instanceof IShapedBlock && ((IShapedBlock) block).isShaped() && world.getBlockMetadata(x, y, z) == blockMetadata)
			return 1.0F;

		return block.getAmbientOcclusionLightValue();
	}

	@Override
	public void drawFace(Face face, RenderParameters rp)
	{
		if (rp.textureSide.get() == ForgeDirection.UP && isGrassBlock)
			rp.colorMultiplier.set(colorMultiplier);

		super.drawFace(face, rp);

		if (grassSideOverlay && rp.textureSide.get() != ForgeDirection.UP && rp.textureSide.get() != ForgeDirection.DOWN)
		{
			rp.icon.set(grassOverlayIcon);
			rp.colorMultiplier.set(colorMultiplier);
			super.drawFace(face, rp);
		}
	}

	@Override
	public void render()
	{
		isGrassBlock = block instanceof ShapedGrass;
		grassSideOverlay = (isGrassBlock && RenderBlocks.fancyGrass);
		if (isGrassBlock && renderType == TYPE_ISBRH_WORLD)
		{
			grassOverlayIcon = BlockGrass.getIconSideOverlay();
			colorMultiplier = block.colorMultiplier(world, x, y, z);
		}

		Shape shape;
		if (!((IShapedBlock) this.block).isShaped() || renderType != TYPE_ISBRH_WORLD)
			shape = ShapePreset.Cube();
		else
		{
			switch (blockMetadata)
			{
				case ShapedBlockManager.NORTHWEST:
					shape = ShapePreset.NorthWest();
					break;
				case ShapedBlockManager.NORTHEAST:
					shape = ShapePreset.NorthEast();
					break;
				case ShapedBlockManager.SOUTHWEST:
					shape = ShapePreset.SouthWest();
					break;
				case ShapedBlockManager.SOUTHEAST:
					shape = ShapePreset.SouthEast();
					break;
				case ShapedBlockManager.TOPNORTH:
					shape = ShapePreset.TopNorth();
					break;
				case ShapedBlockManager.TOPSOUTH:
					shape = ShapePreset.TopSouth();
					break;
				case ShapedBlockManager.TOPWEST:
					shape = ShapePreset.TopWest();
					break;
				case ShapedBlockManager.TOPEAST:
					shape = ShapePreset.TopEast();
					break;
				case ShapedBlockManager.TOPNORTHEAST:
					shape = ShapePreset.TopNorthEast();
					break;
				case ShapedBlockManager.TOPNORTHWEST:
					shape = ShapePreset.TopNorthWest();
					break;
				case ShapedBlockManager.TOPSOUTHEAST:
					shape = ShapePreset.TopSouthEast();
					break;
				case ShapedBlockManager.TOPSOUTHWEST:
					shape = ShapePreset.TopSouthWest();
					break;
				case ShapedBlockManager.INVTOPNORTHEAST:
					shape = ShapePreset.InvTopNorthEast();
					break;
				case ShapedBlockManager.INVTOPNORTHWEST:
					shape = ShapePreset.InvTopNorthWest();
					break;
				case ShapedBlockManager.INVTOPSOUTHEAST:
					shape = ShapePreset.InvTopSouthEast();
					break;
				case ShapedBlockManager.INVTOPSOUTHWEST:
					shape = ShapePreset.InvTopSouthWest();
					break;
				default:
					shape = ShapePreset.Cube();
					break;

			}
		}

		drawShape(shape);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

}
