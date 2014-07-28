package my.simple.keykeeper.data.api;

import javax.crypto.BadPaddingException;
import java.util.Set;

/**
 * Created by Alex on 30.05.2014.
 */
public interface StorageProvider {
    public Set<String> getStorageSet();
    public void addStorage(String storageName, String storagePassword);
    public void removeStorage(String storageName);
    public void openStorage(String storageName, String storagePassword) throws BadPaddingException;
    public void renameStorage(String oldName, String newName);
}
