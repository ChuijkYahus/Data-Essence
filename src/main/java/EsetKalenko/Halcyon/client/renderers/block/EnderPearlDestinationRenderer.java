package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.Halcyon;
import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityModel;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityRenderer;
import EsetKalenko.Halcyon.api.pearlnetwork.PearlNetworkBlockRenderHelper;
import EsetKalenko.Halcyon.block.transportation.EnderPearlDestinationBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class EnderPearlDestinationRenderer extends DatabankBlockEntityRenderer<EnderPearlDestinationBlockEntity> implements PearlNetworkBlockRenderHelper {
    public EnderPearlDestinationRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public void render(EnderPearlDestinationBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
        renderPearlConnections(pBlockEntity, pPoseStack);
    }
    @Override
    public AABB getRenderBoundingBox(EnderPearlDestinationBlockEntity blockEntity) {
        return AABB.INFINITE;
    }

    public static class Model extends DatabankBlockEntityModel<EnderPearlDestinationBlockEntity> {
        public DatabankModel model;
        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(Halcyon.locate("ender_pearl_destination"));
            }
            return model;
        }
        @Override
        public ResourceLocation getTextureLocation() {
            return Halcyon.locate("textures/block/ender_pearl_destination.png");
        }

        @Override
        public void setupModelPose(EnderPearlDestinationBlockEntity pEntity, float partialTick) {
            pEntity.animState.updateAnimDefinitions(getModel());
            animate(pEntity.animState);
        }
    }
}