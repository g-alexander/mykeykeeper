package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.StorageProvider;

/**
 * Created by Alex on 30.05.2014.
 */
public class AddStorageActivity extends BaseActivity {

    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();

    private String differentPasswords;
    private String enterStorageName;
    private String storageAlreadyExists;
    private String defaultStr;
    private String addNewKeyStorage;

    private void initTextStrings() {
        differentPasswords = getResources().getString(R.string.different_passwords);
        enterStorageName = getResources().getString(R.string.enter_storage_name);
        storageAlreadyExists = getResources().getString(R.string.storage_already_exists);
        defaultStr = getResources().getString(R.string.default_str);
        addNewKeyStorage = getResources().getString(R.string.add_new_key_storage);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_storage_layout);
        initTextStrings();
        initWidgets();
    }

    private void initWidgets() {
        initSaveButton();
        initCancelButton();
        checkAutocreate();
    }

    private void initSaveButton() {
        final View saveButton = findViewById(R.id.add_storage_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });
        final TextView saveText = (TextView)findViewById(R.id.add_storage_text_ok);
        if (saveText != null) {
            saveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveButtonClick();
                }
            });
        }
    }

    private void initCancelButton() {
        final View cancelButton = findViewById(R.id.add_storage_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });
        final TextView cancelText = (TextView)findViewById(R.id.add_storage_text_cancel);
        if (cancelText != null) {
            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelButtonClick();
                }
            });
        }
    }

    private void saveButtonClick() {
        final EditText nameField = (EditText)findViewById(R.id.add_storage_name_field);
        final String storageName = nameField.getText().toString();
        final EditText passwordField = (EditText)findViewById(R.id.add_storage_password_field);
        final String storagePassword = passwordField.getText().toString();
        final EditText repeatPasswordField = (EditText)findViewById(R.id.add_storage_repeat_password_field);
        final String storagePasswordRepeat = repeatPasswordField.getText().toString();
        if (!storagePassword.equals(storagePasswordRepeat)) {
            Toast.makeText(this, differentPasswords, Toast.LENGTH_LONG).show();
            return;
        }
        if (storageName.equals("")) {
            Toast.makeText(this, enterStorageName, Toast.LENGTH_LONG).show();
            return;
        }
        if (storageProvider.getStorageSet().contains(storageName)) {
            Toast.makeText(this, storageAlreadyExists, Toast.LENGTH_LONG).show();
            return;
        }
        storageProvider.addStorage(storageName, storagePassword);
        finish();
    }

    private void cancelButtonClick() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_start, menu);
        return true;
    }

    private void checkAutocreate() {
        boolean autoCreate = getIntent().getBooleanExtra("auto_create", false);
        if (autoCreate) {
            EditText nameField = (EditText)findViewById(R.id.add_storage_name_field);
            nameField.setText(defaultStr);
            Toast.makeText(this, addNewKeyStorage, Toast.LENGTH_LONG).show();
        }
    }
}