package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import EsetKalenko.Halcyon.api.node.renderers.BaseCapabilityPointRenderer;
import EsetKalenko.Halcyon.block.transmission.RFNodeBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class RFNodeRenderer extends BaseCapabilityPointRenderer<RFNodeBlockEntity> {
    public RFNodeRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model<>(Halcyon.locate("textures/block/rf_node.png")), new RelayModel<>(Halcyon.locate("textures/block/rf_node.png")));
    }
}