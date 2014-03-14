package net.malisis.bt;

import net.malisis.bt.block.ShapedBlock;
import net.malisis.bt.renderer.ShapedBlockRenderer;
import net.minecraft.block.Block;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;


@Mod(modid = MalisisBetterTerrain.modid, name = MalisisBetterTerrain.modname, version = MalisisBetterTerrain.version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MalisisBetterTerrain
{
	public static final String modid = "malisisbt";
	public static final String modname = "Malisis' Better Terrain";
	public static final String version = "0.01";


	public static MalisisBetterTerrain instance;

	public static Block shapedStone;
	public static Block shapedCobblestone;
	public static Block shapedSand;
	public static Block shapedGravel;
	public static Block shapedDirt;
	public static Block shapedGrass;

	public MalisisBetterTerrain()
	{
		instance = this;
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		if(event.getSide() == Side.CLIENT)
		{
			ShapedBlockRenderer.renderId = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(ShapedBlockRenderer.renderId, new ShapedBlockRenderer());
		}
	}


	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		 shapedStone = new ShapedBlock(504, Block.stone);
		 shapedCobblestone = new ShapedBlock(505, Block.cobblestone);
		 shapedSand = new ShapedBlock(506, Block.sand);
		 shapedGravel = new ShapedBlock(507, Block.gravel);
		 shapedDirt = new ShapedBlock(508, Block.dirt);
		 shapedGrass = new ShapedBlock(509, Block.grass);
	}

}
