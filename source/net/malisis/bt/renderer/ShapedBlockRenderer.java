package net.malisis.bt.renderer;

import net.malisis.bt.block.IShapedBlock;
import net.malisis.bt.block.ShapedBlockManager;
import net.malisis.bt.block.ShapedGrass;
import net.malisis.bt.renderer.shape.InvTopNorthEastShape;
import net.malisis.bt.renderer.shape.InvTopNorthWestShape;
import net.malisis.bt.renderer.shape.InvTopSouthEastShape;
import net.malisis.bt.renderer.shape.InvTopSouthWestShape;
import net.malisis.bt.renderer.shape.NorthEastShape;
import net.malisis.bt.renderer.shape.NorthWestShape;
import net.malisis.bt.renderer.shape.SouthEastShape;
import net.malisis.bt.renderer.shape.SouthWestShape;
import net.malisis.bt.renderer.shape.TopEastShape;
import net.malisis.bt.renderer.shape.TopNorthEastShape;
import net.malisis.bt.renderer.shape.TopNorthShape;
import net.malisis.bt.renderer.shape.TopNorthWestShape;
import net.malisis.bt.renderer.shape.TopSouthEastShape;
import net.malisis.bt.renderer.shape.TopSouthShape;
import net.malisis.bt.renderer.shape.TopSouthWestShape;
import net.malisis.bt.renderer.shape.TopWestShape;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.element.shape.CubeSides;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ShapedBlockRenderer extends MalisisRenderer
{
	public static int renderId = 0;
	private int colorMultiplier;
	private boolean isGrassBlock = false;
	private boolean grassSideOverlay = false;
	private IIcon grassOverlayIcon = null;

	private Shape[] shapes;
	private Shape cube;
	private Shape cubeSide;

	@Override
	protected void initialize()
	{
		cube = new Cube();
		cubeSide = new CubeSides();
		Shape shape;
		shapes = new Shape[16];
		for (int i = 0; i < 16; i++)
		{
			switch (i)
			{
				case ShapedBlockManager.NORTHWEST:
					shape = new NorthWestShape();
					break;
				case ShapedBlockManager.NORTHEAST:
					shape = new NorthEastShape();
					break;
				case ShapedBlockManager.SOUTHWEST:
					shape = new SouthWestShape();
					break;
				case ShapedBlockManager.SOUTHEAST:
					shape = new SouthEastShape();
					break;
				case ShapedBlockManager.TOPNORTH:
					shape = new TopNorthShape();
					break;
				case ShapedBlockManager.TOPSOUTH:
					shape = new TopSouthShape();
					break;
				case ShapedBlockManager.TOPWEST:
					shape = new TopWestShape();
					break;
				case ShapedBlockManager.TOPEAST:
					shape = new TopEastShape();
					break;
				case ShapedBlockManager.TOPNORTHEAST:
					shape = new TopNorthEastShape();
					break;
				case ShapedBlockManager.TOPNORTHWEST:
					shape = new TopNorthWestShape();
					break;
				case ShapedBlockManager.TOPSOUTHEAST:
					shape = new TopSouthEastShape();
					break;
				case ShapedBlockManager.TOPSOUTHWEST:
					shape = new TopSouthWestShape();
					break;
				case ShapedBlockManager.INVTOPNORTHEAST:
					shape = new InvTopNorthEastShape();
					break;
				case ShapedBlockManager.INVTOPNORTHWEST:
					shape = new InvTopNorthWestShape();
					break;
				case ShapedBlockManager.INVTOPSOUTHEAST:
					shape = new InvTopSouthEastShape();
					break;
				case ShapedBlockManager.INVTOPSOUTHWEST:
					shape = new InvTopSouthWestShape();
					break;
				default:
					shape = new Cube();
					break;
			}
			shape.storeState();
			shapes[i] = shape;
		}

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
	public void drawFace(Face face, RenderParameters params)
	{
		if (isGrassBlock)
		{
			if (params.textureSide.get() == ForgeDirection.UP || rp.icon.get() == grassOverlayIcon)
				params.colorMultiplier.set(colorMultiplier);
			else
				params.colorMultiplier.set(0xFFFFFF);
		}

		super.drawFace(face, params);

		params.colorMultiplier.reset();
		params.icon.reset();
	}

	@Override
	public void render()
	{
		rp.reset();
		rp.useBlockBounds.set(false);
		rp.useBlockBrightness.set(true);

		if (renderType == RenderType.ISBRH_INVENTORY)
		{
			//drawShape(new Cube(), new RenderParameters());
			return;
		}

		isGrassBlock = block instanceof ShapedGrass;
		grassSideOverlay = (isGrassBlock && RenderBlocks.fancyGrass && renderType == RenderType.ISBRH_WORLD);
		if (isGrassBlock && renderType == RenderType.ISBRH_WORLD)
		{
			grassOverlayIcon = BlockGrass.getIconSideOverlay();
			colorMultiplier = block.colorMultiplier(world, x, y, z);
		}

		if (!((IShapedBlock) this.block).isShaped() || renderType != RenderType.ISBRH_WORLD)
		{
			cube.resetState();
			drawShape(cube);
			if (grassSideOverlay)
			{
				rp.icon.set(grassOverlayIcon);
				rp.colorMultiplier.set(colorMultiplier);
				cubeSide.resetState();
				drawShape(cubeSide, rp);
			}
		}
		else
		{
			shapes[blockMetadata].resetState();
			drawShape(shapes[blockMetadata]);
		}

	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

}
