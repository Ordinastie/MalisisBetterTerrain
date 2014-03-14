package net.malisis.bt.renderer;

import net.malisis.bt.block.ShapedBlock;
import net.malisis.core.renderer.BaseRenderer;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.RenderParameters;
import net.malisis.core.renderer.element.Shape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ShapedBlockRenderer extends BaseRenderer implements
		ISimpleBlockRenderingHandler
{
	public static int renderId = 0;
	protected ShapedBlock block;
	private int colorMultiplier;
	private boolean isGrassBlock = false;
	private boolean grassSideOverlay = false;
	private Icon grassOverlayIcon = null;

	/**
	 * Overridden because of the case where to block of the same slope next to
	 * each other shouldn't cast any shadow
	 */
	protected float getBlockAmbientOcclusion(IBlockAccess world, int x, int y, int z)
	{
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null)
			return 1.0F;

		if (block instanceof ShapedBlock && ((ShapedBlock) block).shaped
				&& world.getBlockMetadata(x, y, z) == blockMetadata)
			return 1.0F;

		return block.getAmbientOcclusionLightValue(world, x, y, z);
	}

	public void drawFace(Face face, RenderParameters rp)
	{
		rp = new RenderParameters(rp);
		rp.useBlockBounds = false;
		rp.useBlockBrightness = true;
		if (rp.textureSide == ForgeDirection.UP && isGrassBlock)
			rp.colorMultiplier = colorMultiplier;

		super.drawFace(face, rp);

		if (grassSideOverlay && rp.textureSide != ForgeDirection.UP
				&& rp.textureSide != ForgeDirection.DOWN)
		{
			rp.icon = grassOverlayIcon;
			rp.colorMultiplier = colorMultiplier;
			super.drawFace(face, rp);
		}
	}

	//#region ISBRH

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		reset().set(world, block, x, y, z, world.getBlockMetadata(x, y, z));
		this.block = (ShapedBlock) block;

		isGrassBlock = this.block.isShapedGrass();
		grassSideOverlay = (isGrassBlock && RenderBlocks.fancyGrass);
		if (isGrassBlock)
		{
			grassOverlayIcon = BlockGrass.getIconSideOverlay();
			colorMultiplier = block.colorMultiplier(world, x, y, z);
		}

		prepare(TYPE_WORLD);

		Shape shape;
		if (!this.block.shaped)
			shape = Shape.Cube;
		else
		{
			switch (blockMetadata)
			{
				case ShapedBlock.NORTHWEST:
					shape = Shape.NorthWest;
					break;
				case ShapedBlock.NORTHEAST:
					shape = Shape.NorthEast;
					break;
				case ShapedBlock.SOUTHWEST:
					shape = Shape.SouthWest;
					break;
				case ShapedBlock.SOUTHEAST:
					shape = Shape.SouthEast;
					break;
				case ShapedBlock.TOPNORTH:
					shape = Shape.TopNorth;
					break;
				case ShapedBlock.TOPSOUTH:
					shape = Shape.TopSouth;
					break;
				case ShapedBlock.TOPWEST:
					shape = Shape.TopWest;
					break;
				case ShapedBlock.TOPEAST:
					shape = Shape.TopEast;
					break;
				case ShapedBlock.TOPNORTHEAST:
					shape = Shape.TopNorthEast;
					break;
				case ShapedBlock.TOPNORTHWEST:
					shape = Shape.TopNorthWest;
					break;
				case ShapedBlock.TOPSOUTHEAST:
					shape = Shape.TopSouthEast;
					break;
				case ShapedBlock.TOPSOUTHWEST:
					shape = Shape.TopSouthWest;
					break;
				case ShapedBlock.INVTOPNORTHEAST:
					shape = Shape.InvTopNorthEast;
					break;
				case ShapedBlock.INVTOPNORTHWEST:
					shape = Shape.InvTopNorthWest;
					break;
				case ShapedBlock.INVTOPSOUTHEAST:
					shape = Shape.InvTopSouthEast;
					break;
				case ShapedBlock.INVTOPSOUTHWEST:
					shape = Shape.InvTopSouthWest;
					break;
				default:
					shape = Shape.Cube;
					break;

			}
		}

		drawShape(shape);

		clean();

		return true;

	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		reset().set(block, metadata);
		this.block = (ShapedBlock) block;

		isGrassBlock = this.block.isShapedGrass();
		grassSideOverlay = false;// make sure the overlay is not drawn
		if (isGrassBlock)
		{
			colorMultiplier = block.getRenderColor(0);
		}

		prepare(TYPE_INVENTORY);
		drawShape(Shape.Cube);
		clean();
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

	//#end ISBRH

}
