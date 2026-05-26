package EsetKalenko.Halcyon.client.renderers.entity;

import EsetKalenko.Halcyon.entity.LunarStrike;
import EsetKalenko.Halcyon.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LunarStrikeRenderer extends EntityRenderer<LunarStrike> {

    public LunarStrikeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(LunarStrike pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.pushPose();
        pPoseStack.translate(0D, 0.5D*1.5, 0D);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-pEntity.getEntityData().get(LunarStrike.DIRECTION)));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-((pEntity.level().getLevelData().getGameTime()*5) % 360)));
        pPoseStack.scale(1.5F, 1.5F, 1.5F);
        pPoseStack.pushPose();
        pPoseStack.translate(0, (0.5*1.5)-(0.5), 0);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(BlockRegistry.TIDAL_METEORITE.get()), ItemDisplayContext.HEAD, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pEntity.level(), 0);
        pPoseStack.popPose();
        pPoseStack.popPose();
    }
    public ResourceLocation getTextureLocation(LunarStrike pEntity) {
        return null;
    }
}