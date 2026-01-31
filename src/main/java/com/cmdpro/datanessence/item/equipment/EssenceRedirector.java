package com.cmdpro.datanessence.item.equipment;

import com.cmdpro.datanessence.api.block.RedirectorInteractable;
import com.cmdpro.datanessence.registry.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EssenceRedirector extends Item {

    public EssenceRedirector(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockEntity tile = world.getBlockEntity(context.getClickedPos());
        BlockState block = world.getBlockState(context.getClickedPos());

        if ( block.getBlock() instanceof RedirectorInteractable ri ) {
            if ( ri.onRedirectorUse(context) ) {
                // mmmmaybe leave sound playing up to individual interactions? works for now though
                world.playSound(null, context.getClickedPos(), SoundRegistry.REDIRECTOR_USE.value(), SoundSource.PLAYERS);
                return InteractionResult.SUCCESS;
            }
        }

        if ( tile instanceof RedirectorInteractable ri ) {
            if ( ri.onRedirectorUse(context) ) {
                world.playSound(null, context.getClickedPos(), SoundRegistry.REDIRECTOR_USE.value(), SoundSource.PLAYERS);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
