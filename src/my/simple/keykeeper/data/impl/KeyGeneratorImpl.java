package my.simple.keykeeper.data.impl;

import android.content.Context;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.KeyGenerator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alex on 09.05.2014.
 */
public class KeyGeneratorImpl implements KeyGenerator {

    private AtomicInteger catalogsGenerator;
    private AtomicInteger keyRecordsGenerator;
    private Object mutex = new Object();

    public KeyGeneratorImpl() {
        this.catalogsGenerator = new AtomicInteger(DataProviderFactory.INSTANCE.getDataBase().getMaxCategoryId());
        this.keyRecordsGenerator = new AtomicInteger(DataProviderFactory.INSTANCE.getDataBase().getMaxKeyRecordId());
    }

    @Override
    public int nextCatalogId() {
        return catalogsGenerator.incrementAndGet();
    }

    @Override
    public int nextKeyRecordId() {
        return keyRecordsGenerator.incrementAndGet();
    }
}
