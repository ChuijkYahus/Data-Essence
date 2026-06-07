package EsetKalenko.Halcyon.item.equipment;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceBlockEntity;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.api.util.PlayerDataUtil;
import EsetKalenko.Halcyon.moddata.ClientPlayerData;
import EsetKalenko.Halcyon.networking.packet.s2c.MachineEssenceValueSync;

import EsetKalenko.Halcyon.registry.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class EssenceMeter extends Item {
    public static MachineEssenceValueSync.MachineEssenceValue currentMachineEssenceValue;
    public EssenceMeter(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockEntity tile = world.getBlockEntity(context.getClickedPos());
        Player player = context.getPlayer();

        if (tile instanceof EssenceBlockEntity machine) {
            if (!world.isClientSide) {
                var storage = machine.getStorage();
                var component = Component.translatable("item.datanessence.essence_meter.contains");

                for (EssenceType type : storage.getSupportedEssenceTypes().stream().sorted(Comparator.comparing((i) -> i.tier)).toList()) {
                    component.append("\n  " + storage.getEssence(type) + " / " + storage.getMaxEssence() + " ");
                    component.append( PlayerDataUtil.getUnlockedEssences( (ServerPlayer) player).getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(type), false) ? type.getName() : Component.translatable("datanessence.essence_types.unknown"));
                }

                player.sendSystemMessage(component.withStyle(Style.EMPTY
                        .withItalic(true)
                        .withColor(0x7904c7)));
                player.playNotifySound(SoundRegistry.UI_CLICK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
