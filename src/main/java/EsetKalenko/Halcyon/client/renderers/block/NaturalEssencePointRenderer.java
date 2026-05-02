package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.api.node.renderers.BaseEssencePointRenderer;
import EsetKalenko.Halcyon.block.transmission.NaturalEssencePointBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class NaturalEssencePointRenderer extends BaseEssencePointRenderer<NaturalEssencePointBlockEntity> {
    public NaturalEssencePointRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super( new Model<>(DataNEssence.locate("textures/block/natural_essence_point.png")), new RelayModel(DataNEssence.locate("textures/block/natural_essence_point.png")) );
    }
}