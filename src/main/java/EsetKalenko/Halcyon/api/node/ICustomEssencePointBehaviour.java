package EsetKalenko.Halcyon.api.node;

import EsetKalenko.Halcyon.api.essence.EssenceStorage;
import EsetKalenko.Halcyon.api.essence.EssenceType;

public interface ICustomEssencePointBehaviour {
    default boolean canExtractEssence(EssenceStorage from, EssenceStorage to, EssenceType type, float amount) {
        return true;
    }
    default boolean canInsertEssence(EssenceStorage from, EssenceStorage to, EssenceType type, float amount) {
        return true;
    }
}
