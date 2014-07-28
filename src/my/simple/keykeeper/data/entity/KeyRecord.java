package my.simple.keykeeper.data.entity;

import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.AES;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by Alex on 09.05.2014.
 */
public class KeyRecord extends EntityRecord {
    private static final long serialVersionUID = 5548902614726794976L;

    private int id;
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
    public void readObject(ObjectInputStream stream, String password) throws Exception {
        int objectLen = stream.readInt();
        byte[] buf = new byte[objectLen];
        stream.read(buf, 0, objectLen);
        byte[] decripted = AES.decrypt(AES.prepareKey(password), buf);
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
        byte[] encripted = AES.encrypt(AES.prepareKey(password), buffer.array());
        stream.writeInt(encripted.length);
        stream.write(encripted);
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
