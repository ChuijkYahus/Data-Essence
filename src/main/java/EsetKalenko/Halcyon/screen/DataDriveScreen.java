package EsetKalenko.Halcyon.screen;

import EsetKalenko.Halcyon.networking.ModMessages;
import EsetKalenko.Halcyon.networking.packet.c2s.PlayerChangeDriveData;
import EsetKalenko.Halcyon.data.datatablet.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class DataDriveScreen extends DataTabletScreen {
    public DataDriveScreen(Component pTitle, boolean offhand) {
        super(pTitle);
        this.offhand = offhand;
    }
    public boolean offhand;
    @Override
    public boolean clickEntry(Entry entry) {
        ModMessages.sendToServer(new PlayerChangeDriveData(entry.id, entry.getIncompleteStageClient(), offhand));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        onClose();
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
