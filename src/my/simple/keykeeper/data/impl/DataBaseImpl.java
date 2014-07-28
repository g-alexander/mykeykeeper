package my.simple.keykeeper.data.impl;

import android.util.Log;
import my.simple.keykeeper.data.api.AES;
import my.simple.keykeeper.exceptions.RecordsExistsException;
import my.simple.keykeeper.data.api.Constants;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.EntityRecord;
import my.simple.keykeeper.data.entity.KeyRecord;

import javax.crypto.BadPaddingException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by Alex on 12.05.2014.
 */
public class DataBaseImpl implements DataBase {

    private Map<Integer, Category> categories;
    private Map<Integer, KeyRecord> records;
    private String storageName = "default";
    private String storagePassword = "default";
    final String currentVersion = VERSION_1;

    public DataBaseImpl() {
        this.categories = new HashMap<Integer, Category>();
        this.records = new HashMap<Integer, KeyRecord>();
    }

    @Override
    public void save() {
        saveData(storageName + Constants.CATEGORY_PREFIX, this.categories.values());
        saveData(storageName + Constants.KEYRECORD_PREFIX, this.records.values());
    }

    @Override
    public void load() throws BadPaddingException{
        this.categories = new HashMap<Integer, Category>();
        this.records = new HashMap<Integer, KeyRecord>();
        loadData(storageName + Constants.CATEGORY_PREFIX, categories, Category.class);
        loadData(storageName + Constants.KEYRECORD_PREFIX, records, KeyRecord.class);
        emptyCategoryCheck();

    }

    private void emptyCategoryCheck() {
        if (this.categories.isEmpty()) {
            this.categories.put(1, new Category(1, "E-mail"));
            this.categories.put(2, new Category(2, "Site"));
            this.categories.put(3, new Category(3, "Work"));
            this.categories.put(4, new Category(4, "Game"));
            save();
        }
    }

    @Override
    public Category getCategoryById(int id) {
        return categories.get(id);
    }

    @Override
    public KeyRecord getKeyRecordById(int id) {
        return records.get(id);
    }

    @Override
    public int getMaxCategoryId() {
        if (this.categories.isEmpty()) {
            return 1;
        }
        return Collections.max(this.categories.keySet());
    }

    @Override
    public int getMaxKeyRecordId() {
        if (this.records.isEmpty()) {
            return 1;
        }
        return Collections.max(this.records.keySet());
    }

    @Override
    public void addCategory(Category category) {
        this.categories.put(category.getId(), category);
    }

    @Override
    public void addKeyRecord(KeyRecord record) {
        this.records.put(record.getId(), record);
    }

    @Override
    public Collection<Category> getAllCategories() {
        List<Category> res = new LinkedList<Category>();
        res.addAll(this.categories.values());
        Collections.sort(res, new Comparator<Category>() {
            @Override
            public int compare(Category category, Category category2) {
                return category.getName().compareTo(category2.getName());
            }
        });
        return res;
    }

    @Override
    public Collection<KeyRecord> getAllKeyRecords() {
        List<KeyRecord> res = new LinkedList<KeyRecord>();
        res.addAll(this.records.values());
        Collections.sort(res, new Comparator<KeyRecord>() {
            @Override
            public int compare(KeyRecord keyRecord, KeyRecord keyRecord2) {
                return keyRecord.getName().compareTo(keyRecord2.getName());
            }
        });
        return res;
    }

    @Override
    public void removeCategory(Category category) throws RecordsExistsException {
        if (categoryRecordsExists(category)) {
            throw new RecordsExistsException("У категории ксть записи. Удалние невозможно");
        }
        this.categories.remove(category.getId());
    }

    @Override
    public void removeCategoryForce(Category category) {
        Iterator<Map.Entry<Integer, KeyRecord>> iterator = this.records.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, KeyRecord> entry = iterator.next();
            if (entry.getValue().getCategory().equals(category)) {
                iterator.remove();
            }
        }
        this.categories.remove(category.getId());
    }

    @Override
    public void removeKeyRecord(KeyRecord record) {
        this.records.remove(record.getId());
    }

    @Override
    public void selectStorage(String storageName, String storagePassword) {
        this.storageName = storageName;
        this.storagePassword = storagePassword;
        this.categories.clear();
        this.records.clear();
    }

    private boolean categoryRecordsExists(Category category) {
        for (KeyRecord record : records.values()) {
            if (record.getCategory().equals(category)) {
                return true;
            }
        }
        return false;
    }

    private void writeMagicVersion(ObjectOutputStream oos) throws Exception {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(currentVersion.getBytes("UTF-8").length);
            buffer.position(0);
            buffer.put(currentVersion.getBytes("UTF-8"));
            byte[] encrypted = AES.encrypt(AES.prepareKey(storagePassword), buffer.array());
            oos.writeInt(encrypted.length);
            oos.write(encrypted);
        } catch (UnsupportedEncodingException e) {
            Log.e(Constants.APP_NAME, "[DataBaseImpl] Encoding exception: " + e.getMessage());
        }
    }

    private String readMagicVersion(ObjectInputStream ois) throws Exception {
        int versionDataLen = ois.readInt();
        byte[] encrypted = new byte[versionDataLen];
        if (ois.read(encrypted) != versionDataLen) {
            throw new IOException("Error reading magic version");
        }
        byte[] decrypted = AES.decrypt(AES.prepareKey(storagePassword), encrypted);
        String storageVersion = new String(decrypted, "UTF-8");
        if (storageVersion.equals(VERSION_1)) {
            return VERSION_1;
        } else {
            throw new IOException("Unknown storage version");
        }
    }

    private <T extends EntityRecord> void saveData(String fileName, Collection<T> data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File categoryFile = new File(Constants.APP_DIR + fileName);
            if (!categoryFile.exists()) {
                boolean res = new File(Constants.APP_DIR).mkdirs();
                categoryFile.createNewFile();
            } else {
                categoryFile.delete();
            }
            fos = new FileOutputStream(categoryFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            writeMagicVersion(oos);
            oos.writeInt(data.size());
            for (T record : data) {
                record.writeObject(oos, storagePassword);
            }

        } catch (IOException e) {
            android.util.Log.e(Constants.APP_NAME, "[DataBaseImpl] Error saving data: " + e.getMessage());
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[DataBaseImpl] Exception on saving data: " + e.getMessage());
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                } else {
                    if (fos != null) {
                        fos.close();
                    }
                }
            } catch (Exception e) {
                //Тут можно не логировать
            }
        }
    }

    private <T extends EntityRecord> void loadData(String fileName, Map<Integer, T> map, Class<T> cls) throws BadPaddingException{
        if (map == null) {
            return;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            File categoryFile = new File(Constants.APP_DIR + fileName);
            if (!categoryFile.exists()) {
                Log.w(Constants.APP_NAME, "[DataBAseImpl] file " + fileName + " not exists");
                return;
            }
            fis = new FileInputStream(categoryFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            readMagicVersion(ois);
            int count = ois.readInt();
            for (int i = 0; i < count; i++) {
                T rec = cls.newInstance();
                rec.readObject(ois, storagePassword);
                map.put(rec.getId(), rec);
            }

        } catch (BadPaddingException e) {
            throw new BadPaddingException();
        } catch (IOException e) {
            android.util.Log.e(Constants.APP_NAME, "[DataBaseImpl] Error loading data: " + e.getMessage());
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[DataBaseImpl] Exception on loading data: " + e.getMessage());
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                } else {
                    if (fis != null) {
                        fis.close();
                    }
                }
            } catch (Exception e) {
                //Тут можно не логировать
            }
        }
    }

}
