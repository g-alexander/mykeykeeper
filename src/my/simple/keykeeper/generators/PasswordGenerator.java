package my.simple.keykeeper.generators;

import android.content.Context;
import my.simple.keykeeper.config.Configuration;
import my.simple.keykeeper.config.ConfigurationImpl;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Alex on 06.06.2014.
 */
public class PasswordGenerator {

    private final Context context;
    private final Configuration configuration;
    private final static byte[] b_az = new byte[] {'a','b','c','d','e','f','g','h', 'i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private final static byte[] b_AZ = new byte[] {'A','B','C','D','E','F','G','H', 'I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    private final static byte[] b_09 = new byte[] {'0','1','2','3','4','5','6','7','8','9'};
    private final static byte[] b__ = new byte[] {'!','@','#','$','%','^','&','*','(',')','-','_','+','='};

    public PasswordGenerator(Context context) {
        this.context = context;
        this.configuration = new ConfigurationImpl(context);
    }

    public boolean isNumbers() {
        return configuration.getBoolean(Configuration.CONFIG_NUMBERS_09, true);
    }

    public boolean isUpperLetters() {
        return configuration.getBoolean(Configuration.CONFIG_UPPER_LETTERS, true);
    }

    public boolean isLowerLetters() {
        return configuration.getBoolean(Configuration.CONFIG_LOWER_LETTERS, true);
    }

    public int getPasswordLen() {
        return configuration.getInteger(Configuration.CONFIG_PASSWORD_LENGTH, 8);
    }

    public boolean isSpecialLetters() {
        return configuration.getBoolean(Configuration.CONFIG_SPECIAL_LETTERS, false);
    }

    public String generatePassword() {
        int bufLen = 0;
        if (isLowerLetters()) {
            bufLen += b_az.length;
        }
        if (isUpperLetters()) {
            bufLen += b_AZ.length;
        }
        if (isNumbers()) {
            bufLen += b_09.length;
        }
        if (isSpecialLetters()) {
            bufLen += b__.length;
        }
        ByteBuffer buffer = ByteBuffer.allocate(bufLen);
        if (isLowerLetters()) {
            buffer.put(b_az);
        }
        if (isUpperLetters()) {
            buffer.put(b_AZ);
        }
        if (isNumbers()) {
            buffer.put(b_09);
        }
        if (isSpecialLetters()) {
            buffer.put(b__);
        }
        byte[] source = buffer.array();
        int passLen = getPasswordLen();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passLen; i++) {
            int pos = (int)(Math.random() * source.length);
            sb.append((char)source[pos]);
        }
        return sb.toString();
    }
}
