package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import EsetKalenko.Halcyon.block.transmission.ItemPointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class ItemPointRenderer extends BaseCapabilityPointRenderer<ItemPointBlockEntity> {
    public ItemPointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(DataNEssence.locate("textures/block/item_point.png")), new RelayModel<>(DataNEssence.locate("textures/block/item_point.png")) );
    }
}