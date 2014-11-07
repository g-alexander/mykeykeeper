package my.simple.keykeeper.export;

import android.util.Log;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.AES;
import my.simple.keykeeper.data.api.Constants;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.api.StorageProvider;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.EntityRecord;
import my.simple.keykeeper.data.entity.KeyRecord;
import my.simple.keykeeper.data.impl.DataBaseImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 07.10.2014.
 */
public class ExporterImpl implements Exporter{

    private DataBase dataBase = new DataBaseImpl();
    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();
    
    @Override
    public boolean exportDb(String storageName, String storagePassword, String fileName, String filePassword) {
        dataBase.selectStorage(storageName, storagePassword);
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }

            dataBase.load();

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            writeMagicVersion(oos, filePassword);
            saveData(oos, dataBase.getAllCategories(), filePassword);
            saveData(oos, dataBase.getAllKeyRecords(), filePassword);

            oos.flush();
            oos.close();
            return true;
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[ExporterImpl] Exception on exporting data: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean importDb(String storageName, String storagePassword, String fileName, String filePassword) {
        try {
            storageProvider.addStorage(storageName, storagePassword);
            dataBase.selectStorage(storageName, storagePassword);

            File file = new File(fileName);
            if (!file.exists()) {
                return false;
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            final String version = readMagicVersion(ois, filePassword);
            final List<Category> categories = new LinkedList<Category>();
            final List<KeyRecord> records = new LinkedList<KeyRecord>();

            loadData(ois, categories, filePassword, version, Category.class);

            for (Category cat : categories) {
                DataProviderFactory.INSTANCE.getDataBase().addCategory(cat);
            }

            loadData(ois, records, filePassword, version, KeyRecord.class);

            for (Category cat : categories) {
                dataBase.addCategory(cat);
            }
            for (KeyRecord rec : records) {
                dataBase.addKeyRecord(rec);
            }
            dataBase.save();

        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[ExporterImpl] Exception on importing data: " + e.getMessage());
            return false;
        }

        return true;
    }

    private <T extends EntityRecord> void saveData(ObjectOutputStream oos, Collection<T> data, String password) {
        try {
            //writeMagicVersion(oos, password);
            oos.writeInt(data.size());
            for (T record : data) {
                record.writeObject(oos, password);
            }
            oos.flush();
        } catch (IOException e) {
            android.util.Log.e(Constants.APP_NAME, "[ExporterImpl] Error saving data: " + e.getMessage());
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[ExporterImpl] Exception on saving data: " + e.getMessage());
        }
    }

    private <T extends EntityRecord> void loadData(ObjectInputStream ois, Collection<T> data, String password, String version, Class<T> cls) {
        try {

            int len = ois.readInt();
            for (int i = 0; i < len; i++) {
                T rec = cls.newInstance();
                try {
                    rec.readObject(ois, password, version);
                    data.add(rec);
                } catch (Exception e) {
                    Log.e(Constants.APP_NAME, "[ExporterImpl] Exception on loading data: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            android.util.Log.e(Constants.APP_NAME, "[ExporterImpl] Error loading data: " + e.getMessage());
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "[ExporterImpl] Exception on loading data: " + e.getMessage());
        }
    }

    private void writeMagicVersion(ObjectOutputStream oos, String password) throws Exception {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(dataBase.getCurrentVersion().getBytes("UTF-8").length);
            buffer.position(0);
            buffer.put(dataBase.getCurrentVersion().getBytes("UTF-8"));
            byte[] encrypted = AES.encrypt(AES.prepareKey(password), buffer.array());
            oos.writeInt(encrypted.length);
            oos.write(encrypted);
        } catch (UnsupportedEncodingException e) {
            Log.e(Constants.APP_NAME, "[ExporterImpl] Encoding exception: " + e.getMessage());
        }
    }

    private String readMagicVersion(ObjectInputStream ois, String storagePassword) throws Exception {
        int versionDataLen = ois.readInt();
        byte[] encrypted = new byte[versionDataLen];
        if (ois.read(encrypted) != versionDataLen) {
            throw new IOException("Error reading magic version");
        }
        byte[] decrypted = AES.decrypt(AES.prepareKey(storagePassword), encrypted);
        String storageVersion = new String(decrypted, "UTF-8");
        if (storageVersion.equals(DataBase.VERSION_1)) {
            return DataBase.VERSION_1;
        }
        if (storageVersion.equals(DataBase.VERSION_1_1)) {
            return DataBase.VERSION_1_1;
        }
        throw new IOException("Unknown storage version");
    }
}
