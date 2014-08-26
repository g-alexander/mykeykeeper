package my.simple.keykeeper.data.entity;

import android.util.Base64;
import android.util.Log;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.AES;
import my.simple.keykeeper.data.api.Constants;
import my.simple.keykeeper.data.api.DataBase;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class Category extends EntityRecord {

    private int id = -1;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.id = DataProviderFactory.INSTANCE.getKeyGenerator().nextCatalogId();
        this.name = name;
    }

    public Category() {

    }

    private int calculateLength() throws UnsupportedEncodingException {
        return 4 + 5 + name.getBytes("UTF-8").length;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void generateId() {
        this.id = DataProviderFactory.INSTANCE.getKeyGenerator().nextCatalogId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void readObject(ObjectInputStream stream, String password, String storageVersion) throws Exception {
        int count = stream.readInt();
        byte[] buf = new byte[count];
        int res;
        if ((res = stream.read(buf, 0, count)) != count) {
            Log.e(Constants.APP_NAME, "[Category] Wrong data count. Must be " + Integer.toString(count) + ", have " + Integer.toString(res));
            return;
        }
        byte[] byteBuffer = AES.decrypt(AES.prepareKey(password), buf);
        if (storageVersion.equals(DataBase.VERSION_1_1)) {
            byte[] temp = Base64.decode(byteBuffer, Base64.DEFAULT);
            byteBuffer = temp;
        }
        ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);
        this.id = buffer.getInt();
        int len = buffer.getInt();
        byte[] nameBuf = new byte[len];
        buffer.get(nameBuf);
        this.name = new String(nameBuf);
    }

    @Override
    public void writeObject(ObjectOutputStream stream, String password) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(calculateLength());
        buffer.position(0);
        buffer.putInt(id);
        byte[] nameBuf = name.getBytes("UTF-8");
        buffer.putInt(nameBuf.length);
        buffer.put(nameBuf);
        byte[] baseBuff = Base64.encode(buffer.array(), Base64.DEFAULT);
        //byte[] source = buffer.array();
        byte[] encrypted = AES.encrypt(AES.prepareKey(password), baseBuff);
        //byte[] decripted = AES.decrypt(AES.prepareKey(password), encrypted);
        stream.writeInt(encrypted.length);
        stream.write(encrypted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
