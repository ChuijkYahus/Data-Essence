package EsetKalenko.Halcyon.api.misc;

import EsetKalenko.Halcyon.api.LockableItemHandler;

import java.util.List;

public interface ILockableContainer {
    public List<LockableItemHandler> getLockable();
}
