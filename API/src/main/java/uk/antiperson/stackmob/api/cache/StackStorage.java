package uk.antiperson.stackmob.api.cache;

import uk.antiperson.stackmob.api.StackMob;

public interface StackStorage extends StorageMethod {
    IStorageManager getStorageManager();

    StackMob getStackMob();
}
