package my.simple.keykeeper.export;

/**
 * Created by Alex on 07.10.2014.
 */
public interface Exporter {
    public boolean exportDb(String storageName, String storagePassword, String fileName, String filePassword);
    public boolean importDb(String storageName, String StoragePassword, String fileName, String filePassword);
}
