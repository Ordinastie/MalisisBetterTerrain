package net.malisis.bt.block;

import net.minecraft.block.Block;

public interface IShapedBlock
{
	public boolean isShaped();
	public Block getOriginalBlock();
	public String getSrgName();
}
