package EsetKalenko.Halcyon.integration.emi.widgets;

import EsetKalenko.Halcyon.api.DataNEssenceRegistries;
import EsetKalenko.Halcyon.api.essence.EssenceBarBackgroundType;
import EsetKalenko.Halcyon.api.essence.EssenceType;
import EsetKalenko.Halcyon.api.util.client.ClientEssenceBarUtil;
import EsetKalenko.Halcyon.moddata.ClientPlayerData;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EssenceBarWidget extends Widget {
    public EssenceType type;
    public int x;
    public int y;
    public float cost;
    public float max;
    public EssenceBarBackgroundType backgroundType;
    public EssenceBarWidget(int x, int y, EssenceType type, float cost, float max, EssenceBarBackgroundType backgroundType) {
        this.type = type;
        this.cost = cost;
        this.x = x;
        this.y = y;
        this.max = max;
        this.backgroundType = backgroundType;
    }
    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, 3, 22);
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        ClientEssenceBarUtil.drawEssenceBarTiny(draw, x, y, type, cost, max, backgroundType);
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        if (cost > 0) {
            if (getBounds().contains(mouseX, mouseY)) {
                if (ClientPlayerData.getUnlockedEssences().getOrDefault(DataNEssenceRegistries.ESSENCE_TYPE_REGISTRY.getKey(type), false)) {
                    return List.of(ClientTooltipComponent.create(Component.translatable(type.getTooltipKey(), cost).getVisualOrderText()));
                } else {
                    return List.of(ClientTooltipComponent.create(Component.translatable("gui.essence_bar.unknown", cost).getVisualOrderText()));
                }
            }
        }
        return super.getTooltip(mouseX, mouseY);
    }
}
