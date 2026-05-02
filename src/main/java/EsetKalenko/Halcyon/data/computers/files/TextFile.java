package EsetKalenko.Halcyon.data.computers.files;

import EsetKalenko.Halcyon.api.computer.ComputerFile;
import EsetKalenko.Halcyon.api.computer.ComputerFileType;
import EsetKalenko.Halcyon.registry.ComputerFileTypeRegistry;
import net.minecraft.network.chat.Component;

public class TextFile extends ComputerFile {
    public TextFile(Component text, boolean rtl) {
        this.text = text;
        this.rtl = rtl;
    }
    public Component text;
    public boolean rtl;
    @Override
    public ComputerFileType getType() {
        return ComputerFileTypeRegistry.TEXT.get();
    }
}
