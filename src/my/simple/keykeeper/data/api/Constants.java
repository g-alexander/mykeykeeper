package my.simple.keykeeper.data.api;

import android.os.Environment;

/**
 * Created by Alex on 13.05.2014.
 */
public class Constants {
    public static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/keykeeper/";
    public static final String APP_NAME = "keykeeper";
    public static final String CATEGORIES_FILE = "categories.dat";
    public static final String KEYRECORDS_FILE = "rekords.dat";
    public static final String CATEGORY_PREFIX = "_c.dat";
    public static final String KEYRECORD_PREFIX = "_r.dat";
}
