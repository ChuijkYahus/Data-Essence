package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.renderers.BaseEssencePointRenderer;
import EsetKalenko.Halcyon.block.transmission.EssencePointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class EssencePointRenderer extends BaseEssencePointRenderer<EssencePointBlockEntity> {
    public EssencePointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(DataNEssence.locate("textures/block/essence_point.png")), new RelayModel(DataNEssence.locate("textures/block/essence_point.png")) );
    }
}