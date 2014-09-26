package net.malisis.bt.block;

import java.util.List;
import java.util.Random;

import net.malisis.bt.renderer.ShapedBlockRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ShapedCobblestone extends Block implements IShapedBlock
{
	public Block originalBlock;
	public boolean shaped;

	public ShapedCobblestone(boolean shaped)
	{
		super(Material.rock);
		this.originalBlock = Blocks.cobblestone;
		this.shaped = shaped;

		setBlockName("stonebrick");
		setBlockTextureName("cobblestone");
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypePiston);

		setCreativeTab(CreativeTabs.tabBlock);
		setTickRandomly(true);
	}

	@Override
	public String getSrgName()
	{
		return "field_150347_e";
	}

	@Override
	public Block getOriginalBlock()
	{
		return originalBlock;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return shaped ? ShapedBlockManager.isShapeSolid(world.getBlockMetadata(x, y, z), side) : true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		ShapedBlockManager.setMetadata(world, x, y, z, this);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		ShapedBlockManager.setMetadata(world, x, y, z, this);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		super.updateTick(world, x, y, z, rand);
		if (!world.isRemote)
		{
			ShapedBlockManager.clearNeighborUpdated();
			ShapedBlockManager.setMetadata(world, x, y, z, this);
		}
	}

	/**
	 * Set the collision box stair shaped to allow easy climbing of slopes
	 */
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity par7Entity)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		AxisAlignedBB[] aabb = ShapedBlockManager.getBoundingBoxesFromMetadata(this, metadata);
		ShapedBlockManager.addBoxesToList(x, y, z, mask, list, aabb);
	}

	@Override
	public int getLightOpacity()
	{
		return shaped ? 0 : 255;
	}

	@Override
	public boolean isShaped()
	{
		return shaped;
	}

	@Override
	public int getRenderType()
	{
		return ShapedBlockRenderer.renderId;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return !this.shaped;
	}

	public float getAmbientOcclusionLightValue(IBlockAccess world, int x, int y, int z)
	{
		return this.shaped ? 0.4F : 0.2F;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return !this.shaped;
	}
}
