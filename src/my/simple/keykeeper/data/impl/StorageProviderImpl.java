package my.simple.keykeeper.data.impl;

import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.Constants;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.api.StorageProvider;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Alex on 30.05.2014.
 */
public class StorageProviderImpl implements StorageProvider, Serializable {

    DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();

    @Override
    public Set<String> getStorageSet() {
        File root = new File(Constants.APP_DIR);
        if (!root.exists()) {
            root.mkdirs();
        }
        File[] files = root.listFiles();
        List<File> filesList = new ArrayList<File>(Arrays.asList(files));
        Set<String> result = new HashSet<String>();
        for (File file : files) {
            if (file.getName().endsWith(Constants.CATEGORY_PREFIX)) {
                String name = file.getName().substring(0, file.getName().length() - Constants.CATEGORY_PREFIX.length());
                if (filesList.contains(new File(Constants.APP_DIR + name + Constants.KEYRECORD_PREFIX))) {
                    result.add(name);
                }
            }
            if (file.getName().endsWith(Constants.KEYRECORD_PREFIX)) {
                String name = file.getName().substring(0, file.getName().length() - Constants.KEYRECORD_PREFIX.length());
                if (filesList.contains(new File(Constants.APP_DIR + name + Constants.CATEGORY_PREFIX))) {
                    result.add(name);
                }
            }
        }
        return result;
    }

    @Override
    public void addStorage(String storageName, String storagePassword) {
        dataBase.selectStorage(storageName, storagePassword);
        dataBase.save();
    }

    @Override
    public void removeStorage(String storageName) {
        File categoryFile = new File(Constants.APP_DIR + storageName + Constants.CATEGORY_PREFIX);
        if (categoryFile.exists()) {
            categoryFile.delete();
        }
        File recordFile = new File(Constants.APP_DIR + storageName + Constants.KEYRECORD_PREFIX);
        if (recordFile.exists()) {
            recordFile.delete();
        }
    }

    @Override
    public void openStorage(String storageName, String storagePassword)  throws BadPaddingException {
        dataBase.selectStorage(storageName, storagePassword);
        dataBase.load();
    }

    @Override
    public void renameStorage(String oldName, String newName) {
        File categoryFile = new File(Constants.APP_DIR + oldName + Constants.CATEGORY_PREFIX);
        if (categoryFile.exists()) {
            categoryFile.renameTo(new File(Constants.APP_DIR + newName + Constants.CATEGORY_PREFIX));
        }
        File recordFile = new File(Constants.APP_DIR + oldName + Constants.KEYRECORD_PREFIX);
        if (recordFile.exists()) {
            recordFile.renameTo(new File(Constants.APP_DIR + newName + Constants.KEYRECORD_PREFIX));
        }
    }
}
