package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.KeyRecord;

/**
 * Created by Alex on 29.05.2014.
 */
public class PreviewRecordActivity extends BaseActivity {

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview_record_layout);
        prepareRecordData();
        initDefaultWidgets();
    }

    private void prepareRecordData() {
        int recId = getIntent().getIntExtra("preview_record_id", -1);
        if (recId != -1) {
            final KeyRecord record = dataBase.getKeyRecordById(recId);
            fillFields(record);
        }
    }

    private void fillFields(KeyRecord record) {
        TextView nameField = (TextView)findViewById(R.id.preview_field_name);
        nameField.setText(record.getName());
        TextView categoryField = (TextView)findViewById(R.id.preview_field_category);
        categoryField.setText(record.getCategory().getName());
        TextView loginField = (TextView)findViewById(R.id.preview_field_login);
        loginField.setText(record.getLogin());
        TextView passwordField = (TextView)findViewById(R.id.preview_field_password);
        passwordField.setText(record.getPassword());
        TextView descriptionField = (TextView)findViewById(R.id.preview_field_description);
        descriptionField.setText(record.getDescription());
    }
}