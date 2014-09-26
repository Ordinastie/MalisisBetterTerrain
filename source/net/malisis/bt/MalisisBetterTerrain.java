package net.malisis.bt;

import net.malisis.bt.block.FiniteLiquid;
import net.malisis.bt.block.ShapedBlockManager;
import net.malisis.bt.block.ShapedCobblestone;
import net.malisis.bt.block.ShapedDirt;
import net.malisis.bt.block.ShapedGrass;
import net.malisis.bt.block.ShapedGravel;
import net.malisis.bt.block.ShapedSand;
import net.malisis.bt.block.ShapedStone;
import net.malisis.bt.renderer.FiniteLiquidRenderer;
import net.malisis.bt.renderer.ShapedBlockRenderer;
import net.minecraft.block.Block;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = MalisisBetterTerrain.modid, name = MalisisBetterTerrain.modname, version = MalisisBetterTerrain.version)
public class MalisisBetterTerrain
{
	public static final String modid = "malisisbt";
	public static final String modname = "Malisis' Better Terrain";
	public static final String version = "${version}";

	public static MalisisBetterTerrain instance;

	public static Block shapedStone;
	public static Block shapedCobblestone;
	public static Block shapedSand;
	public static Block shapedGravel;
	public static Block shapedDirt;
	public static Block shapedGrass;

	public static Block finiteWater;

	public MalisisBetterTerrain()
	{
		instance = this;
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			RenderingRegistry.registerBlockHandler(new ShapedBlockRenderer());
			RenderingRegistry.registerBlockHandler(new FiniteLiquidRenderer());
		}

		ShapedBlockManager.register(ShapedStone.class);
		ShapedBlockManager.register(ShapedGrass.class);
		ShapedBlockManager.register(ShapedDirt.class);
		ShapedBlockManager.register(ShapedCobblestone.class);
		ShapedBlockManager.register(ShapedSand.class);
		ShapedBlockManager.register(ShapedGravel.class);

		finiteWater = (new FiniteLiquid()).setBlockName("finite_water");
		GameRegistry.registerBlock(finiteWater, finiteWater.getUnlocalizedName().substring(5));

	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{

	}

}
