package com.nikita23830.streamntmixins.mixins;

import com.nikita23830.streamntmixins.TileSpongeVanilla;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSponge.class)
public abstract class BlockSpongeMixin extends Block implements ITileEntityProvider {
    private static boolean register = false;

    protected BlockSpongeMixin(Material p_i45394_1_) {
        super(p_i45394_1_);
    }

    @Inject(method = "<init>*", at=@At("RETURN"), cancellable = true)
    public void conctruct(CallbackInfo ci) {
        if (!register) {
            GameRegistry.registerTileEntity(TileSpongeVanilla.class, "TileSpongeVanillaMixin");
            register = true;
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileSpongeVanilla();
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz) {
        if (!w.isRemote && w.getTileEntity(x, y, z) instanceof TileSpongeVanilla)
            p.addChatComponentMessage(new ChatComponentText("Губка простоит еще: " + time(((TileSpongeVanilla)(w.getTileEntity(x, y, z))).cd)));
        return super.onBlockActivated(w, x, y, z, p, s, hx, hy, hz);
    }

    private String time(int tick) {
        int p = ((20 * 60 * 10) - tick) / 20;
        int s = p % 60;
        int m = (p % 3600) / 60;
        int h = p / 3600;
        return h + " ч., " + m + " мин., " + s + " сек.";
    }
}
