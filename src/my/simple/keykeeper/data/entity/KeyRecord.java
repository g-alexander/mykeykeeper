package my.simple.keykeeper.data.entity;

import android.util.Base64;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.AES;
import my.simple.keykeeper.data.api.DataBase;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Alex on 09.05.2014.
 */
public class KeyRecord extends EntityRecord {
    private static final long serialVersionUID = 5548902614726794976L;

    private int id = -1;
    private String name;
    private Category category;
    private String login;
    private String password;
    private String description;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void generateId() {
        this.id = DataProviderFactory.INSTANCE.getKeyGenerator().nextKeyRecordId();
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public KeyRecord(String name, Category category, String login, String password, String description) {
        this.id = DataProviderFactory.INSTANCE.getKeyGenerator().nextKeyRecordId();
        this.name = name;
        this.category = category;
        this.login = login;
        this.password = password;
        this.description = description;
    }

    public KeyRecord(int id, String name, Category category, String login, String password, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.login = login;
        this.password = password;
        this.description = description;
    }

    public KeyRecord() {

    }

    private int calculateLength() throws UnsupportedEncodingException {
        return 4 + 4 + name.getBytes("UTF-8").length + 4 + 4 + login.getBytes("UTF-8").length + 4
                + password.getBytes("UTF-8").length + 4 + description.getBytes("UTF-8").length;
    }

    @Override
    public void readObject(ObjectInputStream stream, String password, String storageVersion) throws Exception {
        int objectLen = stream.readInt();
        byte[] buf = new byte[objectLen];
        int read = stream.read(buf, 0, objectLen);
        int count = 0;
        while (read < objectLen && count++ < 10) {
            byte[] ttt = new byte[objectLen - read];
            int r = stream.read(ttt, 0, objectLen - read);
            System.arraycopy(ttt, 0, buf, read, r);
            read += r;
        }
        byte[] decripted = AES.decrypt(AES.prepareKey(password), buf);
        if (storageVersion.equals(DataBase.VERSION_1_1)) {
            byte[] temp = Base64.decode(decripted, Base64.DEFAULT);
            decripted = temp;
        }
        ByteBuffer buffer = ByteBuffer.wrap(decripted);
        buffer.position(0);
        this.id = buffer.getInt();
        int len = buffer.getInt();
        byte[] nameBuf = new byte[len];
        buffer.get(nameBuf);
        this.name = new String(nameBuf);
        int categoryId = buffer.getInt();
        this.category = DataProviderFactory.INSTANCE.getDataBase().getCategoryById(categoryId);
        len = buffer.getInt();
        byte[] loginBuf = new byte[len];
        buffer.get(loginBuf);
        this.login = new String(loginBuf);
        len = buffer.getInt();
        byte[] passwordBuf = new byte[len];
        buffer.get(passwordBuf);
        this.password = new String(passwordBuf);
        len = buffer.getInt();
        byte[] descriptionBuf = new byte[len];
        buffer.get(descriptionBuf);
        this.description = new String(descriptionBuf);
    }

    @Override
    public void writeObject(ObjectOutputStream stream, String password) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(calculateLength());
        buffer.position(0);
        buffer.putInt(id);
        buffer.putInt(name.getBytes("UTF-8").length);
        buffer.put(name.getBytes("UTF-8"));
        buffer.putInt(category.getId());
        buffer.putInt(login.getBytes("UTF-8").length);
        buffer.put(login.getBytes("UTF-8"));
        buffer.putInt(this.password.getBytes("UTF-8").length);
        buffer.put(this.password.getBytes("UTF-8"));
        buffer.putInt(description.getBytes("UTF-8").length);
        buffer.put(description.getBytes("UTF-8"));
        byte[] baseBytes = Base64.encode(buffer.array(), Base64.DEFAULT);
        byte[] encrypted = AES.encrypt(AES.prepareKey(password), baseBytes);
        stream.writeInt(encrypted.length);
        stream.write(encrypted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyRecord keyRecord = (KeyRecord) o;

        if (id != keyRecord.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
