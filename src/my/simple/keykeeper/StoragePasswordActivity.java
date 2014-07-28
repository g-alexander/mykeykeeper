package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.StorageProvider;

import javax.crypto.BadPaddingException;

/**
 * Created by Alex on 02.06.2014.
 */
public class StoragePasswordActivity extends BaseActivity {

    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.storage_password_activity);
        initWidgets();
    }

    private void initWidgets() {
        initLabel();
        initCancelButton();
        initSaveButton();
    }

    private void initLabel() {
        final String storageName = this.getIntent().getStringExtra("storage_name");
        TextView label = (TextView)findViewById(R.id.storage_password_label);
        label.setText("Password for storage: " + storageName);

    }

    private void initCancelButton() {
        ImageButton cancelButton = (ImageButton)findViewById(R.id.storage_password_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });
    }

    private void initSaveButton() {
        final ImageButton saveButton = (ImageButton)findViewById(R.id.storage_password_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });
    }

    private void cancelButtonClick() {
        finish();
    }

    private void saveButtonClick() {
        try {
            final EditText passwordField = (EditText) findViewById(R.id.storage_password_password_field);
            final String storagePassword = passwordField.getText().toString();
            final String storageName = this.getIntent().getStringExtra("storage_name");
            storageProvider.openStorage(storageName, storagePassword);
            Intent recordsActivity = new Intent(this, KeyListActivity.class);
            startActivity(recordsActivity);
        } catch (BadPaddingException e) {
            Toast.makeText(this, "Can't open storage", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_start, menu);
        return true;
    }
}