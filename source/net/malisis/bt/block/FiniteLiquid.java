package net.malisis.bt.block;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.malisis.bt.MalisisBetterTerrain;
import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.util.BlockPos;
import net.malisis.core.util.BlockState;
import net.malisis.core.util.RaytraceBlock;
import net.malisis.core.util.replacement.ReplacementTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FiniteLiquid extends BlockDynamicLiquid
{
	public static int renderId = -1;

	private static ForgeDirection[] dirs = new ForgeDirection[] { NORTH, SOUTH, EAST, WEST };
	private int delay = 5;

	public FiniteLiquid()
	{
		super(Material.water);
		setUnlocalizedName("finite_water");
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void registerIcons(IIconRegister p_149651_1_)
	{}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return ReplacementTool.orignalBlock(this).getIcon(side, 0);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (!world.isRemote)
			world.scheduleBlockUpdate(x, y, z, this, delay);

	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack)
	{
		setAmount(world, new BlockState(world, x, y, z), 16);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor)
	{
		if (!world.isRemote)
			world.scheduleBlockUpdate(x, y, z, this, delay);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		//world.scheduleBlockUpdate(x, y, z, this, delay);
		//		int m = world.getBlockMetadata(x, y, z);
		//		m--;
		//		if (m < 1)
		//			m = 15;
		//
		//		world.setBlockMetadataWithNotify(x, y, z, m, 2);

		spreadLiquid(world, x, y, z);

		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if (!world.isRemote)
		{
			spreadLiquid(world, x, y, z);
			//world.scheduleBlockUpdate(x, y, z, this, delay);
			//setAmount(world, new BlockState(world, x, y, z), 0);
		}
	}

	private int getAmount(BlockState state)
	{
		if (state.getBlock() == Blocks.air)
			return 0;
		else if (state.getBlock() != this)
			return -1;
		return state.getMetadata() + 1;
	}

	private void setAmount(World world, BlockState state, int amount)
	{
		if (amount <= 0)
			world.setBlockToAir(state.getX(), state.getY(), state.getZ());
		else
		{
			if (getAmount(state) == amount)
				return;
			world.setBlock(state.getX(), state.getY(), state.getZ(), this, amount - 1, 2);
			world.scheduleBlockUpdate(state.getX(), state.getY(), state.getZ(), this, delay);
		}
	}

	private void spreadLiquid(World world, int x, int y, int z)
	{
		BlockState state = new BlockState(world, x, y, z);
		//		if (getAmount(state) <= 1)
		//			return;

		FloodFill ff = new FloodFill(world, state);
		ff.parse();

	}

	public AxisAlignedBB[] getBoundingBox(IBlockAccess world, int x, int y, int z, BoundingBoxType type)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		return new AxisAlignedBB[] { AxisAlignedBB.getBoundingBox(0, 0, 0, 1, (double) metadata / 16, 1) };
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity)
	{
		for (AxisAlignedBB aabb : getBoundingBox(world, x, y, z, BoundingBoxType.COLLISION))
		{
			if (aabb != null && mask.intersectsWith(aabb.offset(x, y, z)))
				list.add(aabb);
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 src, Vec3 dest)
	{
		return RaytraceBlock.set(world, src, dest, x, y, z).trace();
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		AxisAlignedBB[] aabbs = getBoundingBox(world, x, y, z, BoundingBoxType.SELECTION);
		if (ArrayUtils.isEmpty(aabbs))
			return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
		return aabbs[0].offset(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
	{
		Material material = worldIn.getBlock(x, y, z).getMaterial();
		return material == this.blockMaterial ? false : (side == 1 ? true : super.shouldSideBeRendered(worldIn, x, y, z, side));
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean canRenderInPass(int pass)
	{
		return pass == getRenderBlockPass();
	}

	@Override
	public int getRenderBlockPass()
	{
		return 1;
	}

	@Override
	public int getRenderType()
	{
		return renderId;
	}

	public static class FloodFill
	{
		FiniteLiquid fl = (FiniteLiquid) MalisisBetterTerrain.finiteWater;
		Set<BlockPos> parsed = new HashSet<>();
		LinkedList<BlockPos> toParse = new LinkedList<>();
		World world;
		BlockPos origin;
		int amount = 0;

		public FloodFill(World world, BlockState state)
		{
			this.world = world;
			this.origin = state.getPos();
			this.amount = fl.getAmount(state);
			toParse.add(state.getPos());
		}

		public boolean shouldParse(BlockPos pos)
		{
			if (!origin.isInRange(pos, 16))
				return false;

			if (toParse.contains(pos))
				return false;

			BlockState state = new BlockState(world, pos);
			return state.getBlock() == fl || state.getBlock() == Blocks.air;
		}

		public void parse()
		{
			while (toParse.size() > 0)
			{
				BlockState state = new BlockState(world, toParse.removeFirst());
				if (!process(state))
					break;
				parse(state);
			}

			fl.setAmount(world, new BlockState(world, origin), amount);
		}

		public boolean process(BlockState state)
		{
			BlockState down = new BlockState(world, state.getPos().down());
			int da = fl.getAmount(down);
			if (da != -1 && da != 16)
			{
				int transfered = Math.min(amount, 4);
				transfered = Math.min(transfered, 16 - da);
				fl.setAmount(world, down, da + transfered);
				amount -= transfered;
				return amount > 0;
			}

			if (state.getPos().equals(origin))
				return true;

			int a = fl.getAmount(state);
			if (a < amount - 1)
			{
				fl.setAmount(world, state, a + 1);
				amount--;
			}
			return amount > 1;
		}

		public void parse(BlockState state)
		{
			BlockPos pos = state.getPos();
			if (state.getBlock() == fl)
				for (ForgeDirection dir : dirs)
				{
					BlockPos newPos = pos.offset(dir);
					if (!parsed.contains(newPos) && shouldParse(newPos))
						toParse.add(newPos);
				}
			parsed.add(state.getPos());
		}

	}

	public static class SpreadData
	{
		BlockPos pos;
		int amount;

		public SpreadData(BlockPos pos, int amount)
		{
			this.pos = pos;
			this.amount = amount;
		}
	}
}
