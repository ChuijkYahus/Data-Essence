package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.block.production.SimpleHarvesterBlockEntity;
import EsetKalenko.Halcyon.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SimpleHarvesterRenderer implements BlockEntityRenderer<SimpleHarvesterBlockEntity> {
    ItemStack theWatcher = ItemRegistry.HARVESTER_EYE.get().getDefaultInstance();

    @Override
    public void render(SimpleHarvesterBlockEntity harvester, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        poseStack.pushPose();
        poseStack.translate(0.5d, 1.45d, 0.5d);

        poseStack.mulPose(Axis.YP.rotationDegrees((harvester.getLevel().getLevelData().getGameTime() % 360)+partialTick));
        // TODO make this work like the enchanting table's book where when a player is near it stops and Looks At Them

        Minecraft.getInstance().getItemRenderer().renderStatic(
                theWatcher, ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, packedOverlay,
                poseStack, bufferSource, harvester.getLevel(), 0);
        poseStack.popPose();
    }

    EntityRenderDispatcher renderDispatcher;
    public SimpleHarvesterRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        renderDispatcher = rendererProvider.getEntityRenderer();
    }
}
