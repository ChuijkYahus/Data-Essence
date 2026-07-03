package EsetKalenko.Halcyon.registry;

import EsetKalenko.Halcyon.Halcyon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;

public class JukeboxSongRegistry {
    public static ResourceKey<JukeboxSong> UNDER_THE_SKY = create(Halcyon.locate("under_the_sky"));
    public static ResourceKey<JukeboxSong> CONDUCTORS_LULLABY = create(Halcyon.locate("conductors_lullaby"));

    public static ResourceKey<JukeboxSong> create(ResourceLocation loc) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, loc);
    }
}
