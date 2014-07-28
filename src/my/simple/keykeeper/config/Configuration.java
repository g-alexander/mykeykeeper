package my.simple.keykeeper.config;

/**
 * Created by Alex on 06.06.2014.
 */
public interface Configuration {
    public final static String CONFIG_NUMBERS_09 = "numbers_09";
    public final static String CONFIG_UPPER_LETTERS = "upper_letters";
    public final static String CONFIG_LOWER_LETTERS = "lower_letters";
    public final static String CONFIG_PASSWORD_LENGTH = "password_length";
    public final static String CONFIG_SPECIAL_LETTERS = "special_letters";
    public final static String CONFIG_SHOW_TERMS_OF_USE = "show_terms_of_use";

    public boolean getBoolean(String key, boolean defaultValue);
    public void setBoolean(String key, boolean value);
    public String getString(String key, String defaultValue);
    public void setString(String key, String value);
    public int getInteger(String key, int defaultValue);
    public void setInteger(String key, int value);
}
