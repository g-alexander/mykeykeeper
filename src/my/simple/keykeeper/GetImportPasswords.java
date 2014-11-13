package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.StorageProvider;

import java.util.Set;

/**
 * Created by Alex on 23.10.2014.
 */
public class GetImportPasswords extends Activity {
    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();

    private String fileName;
    private String filePassword;
    private String storageName;
    private String storagePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_import_paswords);
        this.fileName = getIntent().getStringExtra("file_name");

        prepareButtons();
    }

    private void prepareButtons() {
        final View saveButton = findViewById(R.id.get_import_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked();
            }
        });
        TextView saveText = (TextView)findViewById(R.id.get_import_button_ok_text);
        if (saveText != null) {
            saveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveButtonClicked();
                }
            });
        }

        final View cancelButton = findViewById(R.id.get_import_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClicked();
            }
        });
        TextView cancelText = (TextView)findViewById(R.id.get_import_button_cancel_text);
        if (cancelText != null) {
            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelButtonClicked();
                }
            });
        }
    }

    private void saveButtonClicked() {
        final EditText filePasswordField = (EditText)findViewById(R.id.get_import_file_password);
        this.filePassword = filePasswordField.getText().toString();

        final EditText storageNameField = (EditText)findViewById(R.id.get_import_storage_name);
        this.storageName = storageNameField.getText().toString();

        final EditText storagePasswordField = (EditText)findViewById(R.id.get_import_storage_password);
        this.storagePassword = storagePasswordField.getText().toString();

        final EditText storagePasswordRepeat = (EditText)findViewById(R.id.get_import_storage_repeat_password);
        if (!this.storagePassword.equals(storagePasswordRepeat.getText().toString())) {
            Toast.makeText(this, R.string.different_passwords, Toast.LENGTH_LONG).show();
            return;
        }
        if (this.storagePassword.equals("")) {
            Toast.makeText(this, R.string.enter_storage_password, Toast.LENGTH_LONG).show();
            return;
        }
        if (this.filePassword.equals("")) {
            Toast.makeText(this, R.string.enter_file_password, Toast.LENGTH_LONG).show();
            return;
        }
        if (this.storageName.equals("")) {
            Toast.makeText(this, R.string.enter_storage_name, Toast.LENGTH_LONG).show();
            return;
        }

        Set<String> storageSet = storageProvider.getStorageSet();
        for (String st : storageSet) {
            if (st.toLowerCase().equals(storageName.toLowerCase())) {
                Toast.makeText(this, R.string.storage_already_exists, Toast.LENGTH_LONG).show();
                return;
            }
        }

        Intent resInt = new Intent();
        resInt.putExtra("file_password", filePassword);
        resInt.putExtra("storage_name", storageName);
        resInt.putExtra("storage_password", storagePassword);
        resInt.putExtra("file_name", fileName);
        setResult(RESULT_OK, resInt);
        finish();
    }

    private void cancelButtonClicked() {
        Intent resInt = new Intent();
        setResult(RESULT_CANCELED, resInt);
        finish();
    }
}