package EsetKalenko.Halcyon.client.renderers.block;

import EsetKalenko.Halcyon.client.shaders.MachineOutputShader;
import com.cmdpro.databank.model.DatabankModel;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityModel;
import com.cmdpro.databank.model.blockentity.DatabankBlockEntityRenderer;
import EsetKalenko.Halcyon.DataNEssence;
import EsetKalenko.Halcyon.block.production.MetalShaper;
import EsetKalenko.Halcyon.block.production.MetalShaperBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MetalShaperRenderer extends DatabankBlockEntityRenderer<MetalShaperBlockEntity> {
    public MetalShaperRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new Model());
    }

    @Override
    public void render(MetalShaperBlockEntity metalShaper, float pPartialTick, PoseStack poseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Direction facing = metalShaper.getBlockState().getValue(MetalShaper.FACING);
        Vec3 rotateAround = new Vec3(0.5, 0.5, 0.5);
        ItemStack item = metalShaper.getItemHandler().getStackInSlot(0);
        ItemStack impression = metalShaper.getMoldHandler().getStackInSlot(0);

        if (facing.equals(Direction.NORTH))
            poseStack.rotateAround(Axis.YP.rotationDegrees(0), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.SOUTH))
            poseStack.rotateAround(Axis.YP.rotationDegrees(180), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.EAST))
            poseStack.rotateAround(Axis.YP.rotationDegrees(-90), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);
        if (facing.equals(Direction.WEST))
            poseStack.rotateAround(Axis.YP.rotationDegrees(90), (float) rotateAround.x, (float) rotateAround.y, (float) rotateAround.z);

        // shaping impression disk in the drive
        poseStack.pushPose();
        poseStack.translate(0.845d, 0.53d, 0.17d);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.scale(0.35F, 0.35F, 0.35F);
        Minecraft.getInstance().getItemRenderer().renderStatic(impression, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, poseStack, pBufferSource, metalShaper.getLevel(), 0);
        poseStack.popPose();

        // assembled result on the screen using the hologram shader
        poseStack.pushPose();
        poseStack.translate(1.06d, 0.5d, 0.5d);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.scale(0.4F, 0.4F, 0.4F);
        Minecraft.getInstance().getItemRenderer().renderStatic(metalShaper.result, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY, poseStack, MachineOutputShader.createMachineOutputBufferSource(), metalShaper.getLevel(), 0);
        poseStack.popPose();

        // ingredient on the baseplate
        poseStack.pushPose();
        poseStack.translate(0.31d, 0.14d, 0.31d);
        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        poseStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, poseStack, pBufferSource, metalShaper.getLevel(), 0);
        poseStack.popPose();

        super.render(metalShaper, pPartialTick, poseStack, pBufferSource, pPackedLight, pPackedOverlay);
    }

    public static class Model extends DatabankBlockEntityModel<MetalShaperBlockEntity> {
        public DatabankModel model;

        public DatabankModel getModel() {
            if (model == null) {
                model = DatabankModels.models.get(DataNEssence.locate("metal_shaper"));
            }
            return model;
        }

        @Override
        public void setupModelPose(MetalShaperBlockEntity shaper, float partialTick) {
            shaper.animState.updateAnimDefinitions(getModel());
            if (shaper.workTime >= 0) {
                shaper.animState.setAnim("work_that_thang");
            } else {
                shaper.animState.setAnim("idle");
            }
            animate(shaper.animState);
        }

        @Override
        public ResourceLocation getTextureLocation() {
            return DataNEssence.locate("textures/block/metal_shaper.png");
        }

    }
}
