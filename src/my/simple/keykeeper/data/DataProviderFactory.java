package my.simple.keykeeper.data;

import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.api.DataProvider;
import my.simple.keykeeper.data.api.KeyGenerator;
import my.simple.keykeeper.data.api.StorageProvider;
import my.simple.keykeeper.data.impl.DataBaseImpl;
import my.simple.keykeeper.data.impl.DataProviderImpl;
import my.simple.keykeeper.data.impl.KeyGeneratorImpl;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.KeyRecord;
import my.simple.keykeeper.data.impl.StorageProviderImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 09.05.2014.
 */
public enum DataProviderFactory {
    INSTANCE;

    private DataProvider dataProvider;
    private KeyGenerator keyGenerator;
    private DataBase dataBase;
    private StorageProvider storageProvider;

    public KeyGenerator getKeyGenerator() {
        if (this.keyGenerator == null) {
            this.keyGenerator = new KeyGeneratorImpl();
        }
        return this.keyGenerator;
    }

    public DataProvider getDataProvider() {
        if (this.dataProvider == null) {
            this.dataProvider = new DataProviderImpl();
        }
        return this.dataProvider;
    }

    public DataBase getDataBase() {
        if (dataBase == null) {
            this.dataBase = new DataBaseImpl();
            //this.dataBase.load();
        }
        return dataBase;
    }

    public StorageProvider getStorageProvider() {
        if (this.storageProvider == null) {
            this.storageProvider = new StorageProviderImpl();
        }
        return this.storageProvider;
    }
}
