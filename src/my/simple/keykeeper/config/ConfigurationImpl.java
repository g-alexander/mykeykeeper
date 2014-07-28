package my.simple.keykeeper.config;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigurationImpl implements Configuration {

    private final Context context;
    private final static String PREFERENCES_NAME = "my_key_keeper_pref_name";

    private SharedPreferences getPreferences(String prefName) {
        return context.getSharedPreferences(prefName, 0);
    }

    private SharedPreferences.Editor getEditor(String prefName) {
        return getPreferences(prefName).edit();
    }

    public ConfigurationImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getPreferences(PREFERENCES_NAME).getBoolean(key, defaultValue);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        getEditor(PREFERENCES_NAME).putBoolean(key, value).commit();
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getPreferences(PREFERENCES_NAME).getString(key, defaultValue);
    }

    @Override
    public void setString(String key, String value) {
        getEditor(PREFERENCES_NAME).putString(key, value).commit();
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return getPreferences(PREFERENCES_NAME).getInt(key, defaultValue);
    }

    @Override
    public void setInteger(String key, int value) {
        getEditor(PREFERENCES_NAME).putInt(key, value).commit();
    }
}
