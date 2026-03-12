package com.cmdpro.datanessence.api.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * PlayerOwned blocks record the uuid of who placed them. In future they shall also keep track of players in their placer's "team"
 */
public interface PlayerOwned {

    UUID getPlacerID();

    void setPlacerID(Player player);

    @Nullable
    default Player getPlacerIfOnline() {
        UUID placerID = getPlacerID();
        if (placerID != null && ServerLifecycleHooks.getCurrentServer() != null)
            return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(placerID);
        return null;
    }

    static void writePlacer(CompoundTag nbt, UUID placer) {
        if (placer != null) {
            nbt.putUUID("Placer", placer);
        }
    }

    static UUID readPlacer(CompoundTag nbt) {
        if (nbt.contains("Placer")) {
            return nbt.getUUID("Placer");
        }
        return null;
    }
}
