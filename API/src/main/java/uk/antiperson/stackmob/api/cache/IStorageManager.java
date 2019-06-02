package uk.antiperson.stackmob.api.cache;

import uk.antiperson.stackmob.api.IStackMob;

import java.util.Map;
import java.util.UUID;

public interface IStorageManager {
    void onServerEnable();

    void onServerDisable();

    void saveStorage();

    IStackMob getStackMob();

    StackStorage getStackStorage();

    Map<UUID, Integer> getAmountCache();
}
