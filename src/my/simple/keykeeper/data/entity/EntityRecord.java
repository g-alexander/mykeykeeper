package my.simple.keykeeper.data.entity;

import my.simple.keykeeper.data.DataProviderFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Created by Alex on 12.05.2014.
 */
public abstract class EntityRecord {
    public abstract void readObject(ObjectInputStream stream, String password) throws Exception;
    public abstract void writeObject(ObjectOutputStream stream, String password) throws Exception;
    public abstract int getId();
    public abstract void generateId();



}
