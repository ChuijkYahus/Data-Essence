package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import EsetKalenko.Halcyon.block.transmission.RFNodeBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class RFNodeRenderer extends BaseCapabilityPointRenderer<RFNodeBlockEntity> {
    public RFNodeRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model<>(DataNEssence.locate("textures/block/rf_node.png")), new RelayModel<>(DataNEssence.locate("textures/block/rf_node.png")));
    }
}