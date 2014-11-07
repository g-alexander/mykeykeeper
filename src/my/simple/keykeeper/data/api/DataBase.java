package my.simple.keykeeper.data.api;

import my.simple.keykeeper.exceptions.RecordsExistsException;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.KeyRecord;

import javax.crypto.BadPaddingException;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Alex on 12.05.2014.
 */
public interface DataBase {
    public static final String VERSION_1 = "Simple key keeper format version 1.0";
    public static final String VERSION_1_1 = "Simple key keeper format version 1.1";

    public void save();
    public void load() throws BadPaddingException;
    public Category getCategoryById(int id);
    public KeyRecord getKeyRecordById(int id);
    public int getMaxCategoryId();
    public int getMaxKeyRecordId();
    public void addCategory(Category category);
    public void addKeyRecord(KeyRecord record);
    public Collection<Category> getAllCategories();
    public Collection<KeyRecord> getAllKeyRecords();
    public void removeCategory(Category category) throws RecordsExistsException;
    public void removeCategoryForce(Category category);
    public void removeKeyRecord(KeyRecord record);
    public void selectStorage(String storageName, String storagePassword);
    public Collection<KeyRecord> getRecordsByCategory(Category category);
    public String getCurrentVersion();
    public String getStorageName();
    public String getStoragePassword();
}
