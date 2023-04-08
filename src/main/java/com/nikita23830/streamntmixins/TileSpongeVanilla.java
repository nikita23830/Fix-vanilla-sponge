package com.nikita23830.streamntmixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileSpongeVanilla extends TileEntity {
    public int cd = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote)
            return;
        if (cd >= (20 * 60 * 10)) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            return;
        }
        for (int x = (xCoord - 4); x <= (xCoord + 4); ++x) {
            for (int y = (yCoord - 4); y <= (yCoord + 4); ++y) {
                for (int z = (zCoord - 4); z <= (zCoord + 4); ++z) {
                    if (y >= 250 || y <= 5)
                        continue;
                    Block b = worldObj.getBlock(x, y, z);
                    if (b != Blocks.air && b instanceof BlockLiquid) {
                        worldObj.setBlockToAir(x, y, z);
                    }
                }
            }
        }
        ++cd;
    }

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);
        n.setInteger("cd", cd);
    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);
        cd = n.getInteger("cd");
    }
}
