package EsetKalenko.Halcyon.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeleporterPyramid extends Item {

    public TeleporterPyramid(Properties properties) {
        super(
                properties
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack pyramid = player.getItemInHand(hand);

        return InteractionResultHolder.success(pyramid);
    }
}
