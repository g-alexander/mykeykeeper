package my.simple.keykeeper.data.api;

/**
 * Created by Alex on 09.05.2014.
 */
public interface KeyGenerator {
    public int nextCatalogId();
    public int nextKeyRecordId();
}
