package com.cmdpro.datanessence.item;

import com.cmdpro.datanessence.data.pinging.PingableStructureManager;
import com.cmdpro.datanessence.networking.ModMessages;
import com.cmdpro.datanessence.networking.packet.s2c.CreatePingShader;
import com.cmdpro.datanessence.networking.packet.s2c.PingStructures;
import com.cmdpro.datanessence.data.pinging.StructurePing;
import com.cmdpro.datanessence.registry.DataComponentRegistry;
import com.cmdpro.datanessence.registry.EssenceTypeRegistry;
import com.cmdpro.datanessence.registry.SoundRegistry;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Locator extends Item {
    public Locator(Properties pProperties) {
        super(pProperties);
    }

    public void swapModes(ItemStack locator) {
        var mode = locator.get(DataComponentRegistry.SIGNAL_TRACKER_MODE);
        locator.set(DataComponentRegistry.SIGNAL_TRACKER_MODE, !mode);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        var locator = player.getItemInHand(hand);

        if ( player.isShiftKeyDown()
                && locator.get(DataComponentRegistry.SIGNAL_TRACKER_UPGRADED)) {
            swapModes(locator);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
        }

        if (!world.isClientSide) {

            world.playSound(null, player.blockPosition(), SoundRegistry.LOCATOR_PING.value(), SoundSource.PLAYERS);

            List<StructurePing> pings = new ArrayList<>();
            ServerLevel serverLevel = ((ServerLevel)world);

            for (var structureType : PingableStructureManager.types.entrySet()) {

                // note the nots; these 2 are not identical
                if ( structureType.getValue().requiresUpgrade
                        && !locator.get(DataComponentRegistry.SIGNAL_TRACKER_MODE) ) {
                    continue;
                }

                if ( !structureType.getValue().requiresUpgrade
                        && locator.get(DataComponentRegistry.SIGNAL_TRACKER_MODE) ) {
                    continue;
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    if (structureType.getValue().requiredAdvancement.isPresent()) {
                        AdvancementHolder advancement = serverLevel.getServer().getAdvancements().get(structureType.getValue().requiredAdvancement.get().location());
                        if (advancement != null) {
                            if (!serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                                continue;
                            }
                        }
                    }
                }

                Optional<Registry<Structure>> registry = serverLevel.registryAccess().registry(Registries.STRUCTURE);

                if (registry.isPresent()) {
                    Optional<Holder.Reference<Structure>> structure = registry.get().getHolder(structureType.getValue().structure);
                    if (structure.isPresent()) {
                        var result = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, HolderSet.direct(structure.get()), player.blockPosition(), 50, false);
                        if (result != null) {
                            boolean known = false;
                            if (player instanceof ServerPlayer serverPlayer) {
                                AdvancementHolder advancement = serverLevel.getServer().getAdvancements().get(structureType.getValue().advancement.location());
                                if (advancement != null) {
                                    known = serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
                                }
                            }
                            pings.add(new StructurePing(result.getFirst(), structureType.getKey(), known));
                        }
                    }
                }
            }
            ModMessages.sendToPlayer(new PingStructures(pings), (ServerPlayer)player);
            ModMessages.sendToPlayersNear(new CreatePingShader(player.position()), serverLevel, player.position(), 128);
            player.getCooldowns().addCooldown(this, 20*10);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (!stack.get(DataComponentRegistry.SIGNAL_TRACKER_UPGRADED))
            return;

        var key = stack.get(DataComponentRegistry.SIGNAL_TRACKER_MODE) ?
                "item.datanessence.locator.mode_arcane_resonance" :
                "item.datanessence.locator.mode_data";

        tooltipComponents.add(Component.translatable(key,
                        Component.translatable("item.datanessence.locator.instructions")
                                .withStyle(Style.EMPTY.withColor(0x7904c7)))
                .withStyle(Style.EMPTY.withColor(EssenceTypeRegistry.ESSENCE.get().getColor())));
    }
}
