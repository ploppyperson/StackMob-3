package uk.antiperson.stackmob.api.cache;

import uk.antiperson.stackmob.api.IStackMob;

public interface StackStorage extends StorageMethod {
    IStorageManager getStorageManager();

    IStackMob getStackMob();
}
