package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.StorageProvider;

/**
 * Created by Alex on 03.06.2014.
 */
public class EditStorageActivity extends BaseActivity {

    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();
    private String oldStorageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_storage_layout);
        initWidgets();
    }

    private void initWidgets() {
        initCancelButton();
        initSaveButton();
        initNameField();
    }

    private void initCancelButton() {
        final ImageButton cancelButton = (ImageButton)findViewById(R.id.edit_storage_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });
    }

    private void initSaveButton() {
        final ImageButton saveButton = (ImageButton)findViewById(R.id.edit_storage_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });
    }

    private void initNameField() {
        this.oldStorageName = this.getIntent().getStringExtra("storage_name");
        final EditText nameField = (EditText)findViewById(R.id.edit_storage_name_field);
        nameField.setText(oldStorageName);
    }

    private void saveButtonClick() {
        final EditText nameField = (EditText)findViewById(R.id.edit_storage_name_field);
        storageProvider.renameStorage(this.oldStorageName, nameField.getText().toString());
        finish();
    }

    private void cancelButtonClick() {
        finish();
    }
}