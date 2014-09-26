package net.malisis.bt.block;

import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.malisis.bt.renderer.FiniteLiquidRenderer;
import net.malisis.core.MalisisCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FiniteLiquid extends Block
{
	private HashMap<ForgeDirection, Integer> data = new HashMap<ForgeDirection, Integer>();
	private int delay = 5;
	
	public FiniteLiquid()
	{
		super(Material.water);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	
	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_)
	{
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return Blocks.water.getIcon(side, 0);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if(!world.isRemote)
			world.scheduleBlockUpdate(x, y, z, this, delay);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack)
	{
		setAmount(world, x, y, z, 16);		
	}
	
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		
		if (!world.isRemote)
		{
			spreadLiquid(world, x, y, z);
			world.scheduleBlockUpdate(x, y, z, this, delay);
		}
	}
	
	private int getAmount(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) + 1;
	}
	private void setAmount(World world, int x, int y, int z, int amount)
	{
		if(amount <= 0)
			world.setBlockToAir(x, y, z);
		else
			world.setBlock(x, y, z, this, amount - 1, 2);
	}

	private void spreadLiquid(World world, int x, int y, int z)
	{
		int amount = getAmount(world, x, y, z);
		int total = amount + getSurroundingData(world, x, y, z, amount);
		if(data.size() == 0)
			return;
		
		
		if(data.containsKey(DOWN))
		{
			int ad = data.get(DOWN);
			if(ad != 16)
			{
				int a = Math.min(16 - ad, amount);
				setAmount(world, x, y, z, amount - a);
				setAmount(world, x, y - 1, z, ad + a);
				return;
			}			
		}
		
		int split = (int) (total / (data.size() + 1));
		int left = total % (data.size() + 1);
		
		//set current block amount
		int newAmount = split;
		if(left > 0)
		{
			newAmount++;
			left--;
		}
		setAmount(world, x, y, z, newAmount);
		
		//set neighbors amount
		for (Entry<ForgeDirection, Integer> entry : data.entrySet())
		{
			ForgeDirection dir = entry.getKey();
			newAmount = split;
			if(left > 0)
			{
				newAmount++;
				left--;
			}
			setAmount(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, newAmount);		
			
		}	
	}
	
	private int getSurroundingData(World world, int x, int y, int z, int currentAmount)
	{
		Boolean isAir = true;
		Block block;
		int amount;
		int total = 0;

		data.clear();
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(dir != UP)
			{
				block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				isAir = block.getMaterial() == Material.air;
				if(isAir || block == this)
				{
					amount = isAir ? 0 : getAmount(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
					if(amount < currentAmount - 1 || (dir == DOWN && amount != 16))
					{
						data.put(dir, amount);
						total += amount;
					}
				}
			}
		}
		
		return total;
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
    	int m = world.getBlockMetadata(x, y, z);
    	m--;
    	if(m < 1)
    		m = 15;

    	world.setBlockMetadataWithNotify(x, y, z, m, 2);
    	
    	MalisisCore.message(FMLCommonHandler.instance().getEffectiveSide() + " -> " + world.isRemote);
    	
    	return false;
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
		return FiniteLiquidRenderer.renderId;
	}
}
