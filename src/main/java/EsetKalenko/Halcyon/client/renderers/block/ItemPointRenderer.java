package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import EsetKalenko.Halcyon.block.transmission.ItemPointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class ItemPointRenderer extends BaseCapabilityPointRenderer<ItemPointBlockEntity> {
    public ItemPointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(Halcyon.locate("textures/block/item_point.png")), new RelayModel<>(Halcyon.locate("textures/block/item_point.png")) );
    }
}