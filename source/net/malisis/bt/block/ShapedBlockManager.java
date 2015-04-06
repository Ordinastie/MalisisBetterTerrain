package net.malisis.bt.block;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import net.malisis.core.util.replacement.ReplacementTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapedBlockManager
{
	private static final int MAX_NEIGHBOR_UPDATE_COUNT = 25;
	// corners
	public static final int NONE = -1;
	public static final int NORTHWEST = 0;
	public static final int NORTHEAST = 1;
	public static final int SOUTHWEST = 2;
	public static final int SOUTHEAST = 3;
	// slopes
	public static final int TOPNORTH = 4;
	public static final int TOPSOUTH = 5;
	public static final int TOPWEST = 6;
	public static final int TOPEAST = 7;
	// corner slopes
	public static final int TOPNORTHWEST = 8;
	public static final int TOPNORTHEAST = 9;
	public static final int TOPSOUTHEAST = 10;
	public static final int TOPSOUTHWEST = 11;
	// inverted corner slopes
	public static final int INVTOPNORTHWEST = 12;
	public static final int INVTOPNORTHEAST = 13;
	public static final int INVTOPSOUTHWEST = 14;
	public static final int INVTOPSOUTHEAST = 15;

	private static HashMap<IShapedBlock, IShapedBlock> blockMap = new HashMap<>();
	private static int neighborUpdated = 0;

	public static void clearNeighborUpdated()
	{
		neighborUpdated = 0;
	}

	public static void setMetadata(World world, int x, int y, int z, IShapedBlock block)
	{

		if (neighborUpdated++ >= MAX_NEIGHBOR_UPDATE_COUNT)
			world.scheduleBlockUpdate(x, y, z, (Block) block, 5);
		else
		{
			int s = calcShape(world, x, y, z);
			if (!block.isShaped() && s == NONE)
				return;
			else if (block.isShaped() && s == NONE)
				world.setBlock(x, y, z, (Block) blockMap.get(block), 0, 3);
			else if (!block.isShaped() && s != NONE)
				world.setBlock(x, y, z, (Block) blockMap.get(block), s, 3);
			else
			{
				world.setBlockMetadataWithNotify(x, y, z, s, 3);
				world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
			}
		}
	}

	public static int calcShape(World world, int x, int y, int z)
	{
		if (isNextToBlockFluid(world, x, y, z))
			return NONE;

		boolean north = shouldConnect(world, x, y, z - 1, ForgeDirection.SOUTH);
		boolean south = shouldConnect(world, x, y, z + 1, ForgeDirection.NORTH);
		boolean west = shouldConnect(world, x - 1, y, z, ForgeDirection.EAST);
		boolean east = shouldConnect(world, x + 1, y, z, ForgeDirection.WEST);
		boolean bottom = shouldConnect(world, x, y - 1, z, ForgeDirection.UP);
		// boolean top = shouldConnect(world, x, y + 1, z, ForgeDirection.DOWN);
		boolean top = !world.isAirBlock(x, y + 1, z);

		int fSouth = getBlockShape(world, x, y, z + 1);
		int fNorth = getBlockShape(world, x, y, z - 1);
		int fWest = getBlockShape(world, x - 1, y, z);
		int fEast = getBlockShape(world, x + 1, y, z);
		int fTop = getBlockShape(world, x, y + 1, z);
		int fBottom = getBlockShape(world, x, y - 1, z);

		int f = NONE;

		if (west && bottom && fBottom == NONE && fWest == NONE && !east && !top)
			f = TOPEAST;
		else if (east && bottom && fBottom == NONE && fEast == NONE && !west && !top)
			f = TOPWEST;
		else if (north && bottom && fBottom == NONE && fNorth == NONE && !south && !top)
			f = TOPSOUTH;
		else if (south && bottom && fBottom == NONE && fSouth == NONE && !north && !top)
			f = TOPNORTH;

		if (north && fNorth == NONE && east && fEast == NONE && !south && !west)
			f = SOUTHWEST;
		else if (north && fNorth == NONE && west && fWest == NONE && !south && !east)
			f = SOUTHEAST;
		else if (south && fSouth == NONE && east && fEast == NONE && !north && !west)
			f = NORTHWEST;
		else if (south && fSouth == NONE && west && fWest == NONE && !north && !east)
			f = NORTHEAST;

		if (((east && fSouth == TOPWEST) || (south && fEast == TOPNORTH)) && bottom && !top && !north && !west)
			f = TOPNORTHWEST;
		else if (((east && fNorth == TOPWEST) || (north && fEast == TOPSOUTH)) && bottom && !top && !south && !west)
			f = TOPSOUTHWEST;
		else if (((west && fSouth == TOPEAST) || (south && fWest == TOPNORTH)) && bottom && !top && !north && !east)
			f = TOPNORTHEAST;
		else if (((west && fNorth == TOPEAST) || (north && fWest == TOPSOUTH)) && bottom && !top && !south && !east)
			f = TOPSOUTHEAST;

		else if ((fNorth == TOPWEST || fNorth == TOPNORTHWEST) && (fWest == TOPNORTH || fWest == TOPNORTHWEST) && fSouth == NONE
				&& fEast == NONE && (!top || fTop == NORTHWEST || fTop == TOPNORTHWEST) && south && east)
			f = INVTOPNORTHWEST;
		else if ((fSouth == TOPWEST || fSouth == TOPSOUTHWEST) && (fWest == TOPSOUTH || fWest == TOPSOUTHWEST) && fNorth == NONE
				&& fEast == NONE && (!top || fTop == SOUTHWEST || fTop == TOPSOUTHWEST) && north && east)
			f = INVTOPSOUTHWEST;
		else if ((fNorth == TOPEAST || fNorth == TOPNORTHEAST) && (fEast == TOPNORTH || fEast == TOPNORTHEAST) && fSouth == NONE
				&& fWest == NONE && (!top || fTop == NORTHEAST || fTop == TOPNORTHEAST) && south && west)
			f = INVTOPNORTHEAST;
		else if ((fSouth == TOPEAST || fSouth == TOPSOUTHEAST) && (fEast == TOPSOUTH || fEast == TOPSOUTHEAST) && fNorth == NONE
				&& fWest == NONE && (!top || fTop == SOUTHEAST || fTop == TOPSOUTHEAST) && north && west)
			f = INVTOPSOUTHEAST;

		return f;
	}

	public static boolean isNextToBlockFluid(World world, int x, int y, int z)
	{
		return world.getBlock(x - 1, y, z) instanceof BlockLiquid || world.getBlock(x + 1, y, z) instanceof BlockLiquid
				|| world.getBlock(x, y, z - 1) instanceof BlockLiquid || world.getBlock(x, y, z + 1) instanceof BlockLiquid
				|| world.getBlock(x, y - 1, z) instanceof BlockLiquid || world.getBlock(x, y + 1, z) instanceof BlockLiquid;
	}

	public static int getBlockShape(World world, int x, int y, int z)
	{
		return isShapedBlock(world, x, y, z) ? world.getBlockMetadata(x, y, z) : NONE;
	}

	public static boolean isShapedBlock(World world, int x, int y, int z)
	{
		return isShapedBlock(world.getBlock(x, y, z));
	}

	public static boolean isShapedBlock(Block block)
	{
		if (block == null)
			return false;
		else if (block instanceof IShapedBlock)
			return ((IShapedBlock) block).isShaped();
		else
			return false;
	}

	public static boolean shouldConnect(World world, int x, int y, int z, ForgeDirection side)
	{
		Block block = world.getBlock(x, y, z);
		return (isShapedBlock(block) || block.isSideSolid(world, x, y, z, side));
	}

	public static boolean isShapeSolid(int metadata, ForgeDirection side)
	{
		if (side == ForgeDirection.DOWN)
			return metadata == TOPNORTH || metadata == TOPSOUTH || metadata == TOPWEST || metadata == TOPEAST;

		if (side == ForgeDirection.NORTH && (metadata == TOPSOUTH || metadata == SOUTHEAST || metadata == SOUTHWEST))
			return true;
		if (side == ForgeDirection.SOUTH && (metadata == TOPNORTH || metadata == NORTHEAST || metadata == NORTHWEST))
			return true;
		if (side == ForgeDirection.WEST && (metadata == TOPEAST || metadata == NORTHEAST || metadata == SOUTHEAST))
			return true;
		if (side == ForgeDirection.EAST && (metadata == TOPWEST || metadata == NORTHWEST || metadata == SOUTHWEST))
			return true;

		return false;
	}

	public static AxisAlignedBB[] getBoundingBoxesFromMetadata(IShapedBlock block, int metadata)
	{
		if (!block.isShaped())
			return new AxisAlignedBB[] { AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1) };

		final int START = 0;
		final int MIN = 0;
		final int END = 1;
		final int MAX = 1;
		boolean vertical = true;
		int slices = 8;
		float d = 1 / (float) slices;
		float[][] fx = { { 0, 1 }, { 0, 1 } };
		float[][] fy = { { 0, 1 }, { 0, 1 } };
		float[][] fz = { { 0, 1 }, { 0, 1 } };

		switch (metadata)
		{
			case TOPNORTH:
				fz[END][MIN] = 1;
				break;
			case TOPSOUTH:
				fz[END][MAX] = 0;
				break;
			case TOPWEST:
				fx[END][MIN] = 1;
				break;
			case TOPEAST:
				fx[END][MAX] = 0;
				break;
			case NORTHWEST:
				vertical = false;
				fz[START][MIN] = 1;
				break;
			case NORTHEAST:
				vertical = false;
				fz[END][MIN] = 1;
				break;
			case SOUTHWEST:
				vertical = false;
				fz[START][MAX] = 0;
				break;
			case SOUTHEAST:
				vertical = false;
				fz[END][MAX] = 0;
				break;
			case TOPNORTHWEST:
				fz[END][MIN] = 1;
				fx[END][MIN] = 1;
				break;
			case TOPNORTHEAST:
				fz[END][MIN] = 1;
				fx[END][MAX] = 0;
				break;
			case TOPSOUTHWEST:
				fz[END][MAX] = 0;
				fx[END][MIN] = 1;
				break;
			case TOPSOUTHEAST:
				fz[END][MAX] = 0;
				fx[END][MAX] = 0;
				break;
			case INVTOPNORTHWEST:
				fz[END][MIN] = 0.75F;
				fx[END][MIN] = 0.75F;
				break;
			case INVTOPNORTHEAST:
				fz[END][MIN] = 0.75F;
				fx[END][MAX] = 0.25F;
				break;
			case INVTOPSOUTHWEST:
				fz[END][MAX] = 0.25F;
				fx[END][MIN] = 0.75F;
				break;
			case INVTOPSOUTHEAST:
				fz[END][MAX] = 0.25F;
				fx[END][MAX] = 0.25F;
				break;

			default:
				break;
		}

		AxisAlignedBB[] aabb = new AxisAlignedBB[slices];
		for (int i = 0; i < slices; i++)
		{
			float bx = fx[START][MIN] + (fx[END][MIN] - fx[START][MIN]) * i * d;
			float bX = fx[START][MAX] + (fx[END][MAX] - fx[START][MAX]) * i * d;
			float by = fy[START][MIN] + (fy[END][MIN] - fy[START][MIN]) * i * d;
			float bY = fy[START][MAX] + (fy[END][MAX] - fy[START][MAX]) * i * d;
			float bz = fz[START][MIN] + (fz[END][MIN] - fz[START][MIN]) * i * d;
			float bZ = fz[START][MAX] + (fz[END][MAX] - fz[START][MAX]) * i * d;

			if (vertical)
			{
				by = i * d;
				bY = by + d;
			}
			else
			{
				bx = i * d;
				bX = bx + d;
			}

			aabb[i] = AxisAlignedBB.getBoundingBox(bx, by, bz, bX, bY, bZ);
		}

		return aabb;
	}

	public static void addBoxesToList(int x, int y, int z, AxisAlignedBB mask, List list, AxisAlignedBB[] aabb)
	{
		for (AxisAlignedBB a : aabb)
		{
			a = a.getOffsetBoundingBox(x, y, z);
			if (mask.intersectsWith(a))
				list.add(a);
		}

	}

	public static void register(Class<? extends IShapedBlock> clazz)
	{
		try
		{
			Constructor<? extends IShapedBlock> ctr = clazz.getConstructor(Boolean.TYPE);

			IShapedBlock normal = ctr.newInstance(false);
			IShapedBlock shaped = ctr.newInstance(true);

			String name = ((Block) normal).getUnlocalizedName().substring(5);
			if (name.equals("stonebrick"))
				name = "cobblestone";

			ReplacementTool.replaceVanillaBlock(Block.getIdFromBlock(normal.getOriginalBlock()), name, normal.getSrgName(), (Block) normal,
					normal.getOriginalBlock());
			GameRegistry.registerBlock((Block) shaped, name + "_shaped");

			blockMap.put(normal, shaped);
			blockMap.put(shaped, normal);
		}
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}

	}

}
