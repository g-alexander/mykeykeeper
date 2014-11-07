package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.KeyRecord;

/**
 * Created by Alex on 29.05.2014.
 */
public class PreviewRecordActivity extends BaseActivity {

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();

    private String copiedToClipboardString;
    private String nameString;
    private String categoryString;
    private String loginString;
    private String passwordString;
    private String descriptionString;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview_record_layout);
        prepareStrings();
        prepareRecordData();
        initDefaultWidgets();
    }

    private void prepareStrings() {
        copiedToClipboardString = getResources().getString(R.string.copied_to_clipboard);
        nameString = getResources().getString(R.string.name);
        categoryString = getResources().getString(R.string.category);
        loginString = getResources().getString(R.string.login);
        passwordString = getResources().getString(R.string.password);
        descriptionString = getResources().getString(R.string.description);
    }

    private void prepareRecordData() {
        int recId = getIntent().getIntExtra("preview_record_id", -1);
        if (recId != -1) {
            final KeyRecord record = dataBase.getKeyRecordById(recId);
            fillFields(record);
            prepareCopyFields(record);
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

    private void prepareCopyFields(final KeyRecord record) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final View name = findViewById(R.id.preview_name_copy_field);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.setText(record.getName());
                Toast.makeText(PreviewRecordActivity.this, nameString + " " + copiedToClipboardString, Toast.LENGTH_LONG).show();
            }
        });
        final View category = findViewById(R.id.preview_category_copy_field);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.setText(record.getCategory().getName());
                Toast.makeText(PreviewRecordActivity.this, categoryString + " " + copiedToClipboardString, Toast.LENGTH_LONG).show();
            }
        });
        final View login = findViewById(R.id.preview_login_copy_field);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.setText(record.getLogin());
                Toast.makeText(PreviewRecordActivity.this, loginString + " " + copiedToClipboardString, Toast.LENGTH_LONG).show();
            }
        });
        final View pass = findViewById(R.id.preview_password_copy_field);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.setText(record.getPassword());
                Toast.makeText(PreviewRecordActivity.this, passwordString + " " + copiedToClipboardString, Toast.LENGTH_LONG).show();
            }
        });
        final View description = findViewById(R.id.preview_description_copy_field);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipboard.setText(record.getDescription());
                Toast.makeText(PreviewRecordActivity.this, descriptionString + " " + copiedToClipboardString, Toast.LENGTH_LONG).show();
            }
        });
    }
}