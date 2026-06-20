package EsetKalenko.Halcyon.api.misc;

import EsetKalenko.Halcyon.Halcyon;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class SymbolTypes {
    public static HashMap<ResourceLocation, SymbolType> symbols = new HashMap<>();
    static {
        /*
        Large symbols are 32x32
        Normal symbols are 16x16
        Small symbols are 8x8

        You can return null for any of the 3 symbol locations, returning null basically just means the symbol has nothing for that size
        It shouldn't crash if one of the symbol locations is null and you try to blit it, it should simply do nothing

        It may also be nice to add a static final variable in here that directly links to the symbol type

        Example:
        symbols.put(DataNEssence.locate("example"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 32),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 48)
        ));

        Example with null:
        symbols.put(DataNEssence.locate("example2"), new SymbolType(
                null,
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 32),
                null
        ));
         */

        // The Cities - also used to represent the seven colors
        symbols.put(Halcyon.locate("shar"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 16)
        ));
        symbols.put(Halcyon.locate("besar"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 32),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 48)
        ));
        symbols.put(Halcyon.locate("nula"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 64),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 80)
        ));
        symbols.put(Halcyon.locate("eniro"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 96),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 112)
        ));
        symbols.put(Halcyon.locate("ipari"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 128),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 144)
        ));
        symbols.put(Halcyon.locate("koru"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 160),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 176)
        ));
        symbols.put(Halcyon.locate("aru"), new SymbolType(
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 0, 0),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 192),
                SymbolType.getSymbolLocation(SymbolType.SYMBOL_LOCATION, 32, 208)
        ));
    }

    public static SymbolType SHAR = SymbolTypes.symbols.get( Halcyon.locate("shar") );
    public static SymbolType BESAR = SymbolTypes.symbols.get( Halcyon.locate("besar") );
    public static SymbolType NULA = SymbolTypes.symbols.get( Halcyon.locate("nula") );
    public static SymbolType ENIRO = SymbolTypes.symbols.get( Halcyon.locate("eniro") );
    public static SymbolType IPARI = SymbolTypes.symbols.get( Halcyon.locate("ipari") );
    public static SymbolType KORU = SymbolTypes.symbols.get( Halcyon.locate("koru") );
    public static SymbolType ARU = SymbolTypes.symbols.get( Halcyon.locate("aru") );
}
