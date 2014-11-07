package my.simple.keykeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.export.Exporter;
import my.simple.keykeeper.export.ExporterFactory;

import java.io.File;

/**
 * Created by Alex on 10.06.2014.
 */
public class BaseActivity extends Activity {

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();

    private boolean enableMenu = true;
    private final static int REQUEST_EXPORT_FILE_NAME = 1;
    private final static int REQUEST_SET_PASSWORD = 2;
    private final static int REQUEST_IMPORT_FILE_NAME = 3;
    private final static int REQUEST_IMPORT_STORAGE_NAME = 4;

    private String exportSuccess;
    private String exportError;
    private String fileNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initDefaultWidgets() {
        initBackButton();
        initExitButton();
        initOptionsButton();

        exportSuccess = getResources().getString(R.string.export_successful);
        exportError = getResources().getString(R.string.export_error);
        fileNotFound = getResources().getString(R.string.file_not_found);
    }

    private void initOptionsButton() {
        final ImageView optionsButton = (ImageView)findViewById(R.id.options_button);
        if (optionsButton != null) {
            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOptions();
                }
            });
        }
        final TextView optionsText = (TextView)findViewById(R.id.options_text);
        if (optionsText != null) {
            optionsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOptions();
                }
            });
        }
    }

    private void showOptions() {
        Intent optionsActivity = new Intent(this, OptionsActivity.class);
        startActivity(optionsActivity);
    }

    private void initBackButton() {
        ImageView back = (ImageView)findViewById(R.id.back_image_view);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        TextView backText = (TextView)findViewById(R.id.back_image_text);
        if (backText != null) {
            backText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void initExitButton() {
        final ImageView exit = (ImageView)findViewById(R.id.storage_close_button);
        if (exit != null) {
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exit();
                }
            });
        }
        final TextView exitText = (TextView)findViewById(R.id.storage_close_text);
        if (exitText != null) {
            exitText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exit();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (enableMenu) {
            final MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.options_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_options:
                showOptions();
                break;
            case R.id.main_menu_exit:
                exit();
                break;
            case R.id.main_menu_export:
                exportAction();
                break;
            case R.id.main_menu_import:
                importAction();
        }
        return true;
    }

    protected void exit() {
        Intent intent = new Intent(getApplicationContext(), StorageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void exportAction() {
        Intent intent = new Intent(getApplicationContext(), FileDialog.class);
        startActivityForResult(intent, REQUEST_EXPORT_FILE_NAME);
    }

    protected void importAction() {
        Intent intent = new Intent(getApplicationContext(), FileDialog.class);
        startActivityForResult(intent, REQUEST_IMPORT_FILE_NAME);
    }

    public boolean isEnableMenu() {
        return enableMenu;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXPORT_FILE_NAME && resultCode == RESULT_OK) {
            final String fileName = data.getStringExtra("file_name");
            final File exportFile = new File(fileName);
            if (exportFile.exists()) {
                final DialogInterface.OnClickListener handler = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            getPassword(fileName);
                        }
                    }
                };
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(getResources().getString(R.string.override_warning))
                        .setPositiveButton(R.string.yes, handler)
                        .setNegativeButton(R.string.no, handler)
                        .show();
            } else {
                getPassword(fileName);
            }
        }
        if (requestCode == REQUEST_SET_PASSWORD && resultCode == RESULT_OK) {
            final String fileName = data.getStringExtra("file_name");
            final String password = data.getStringExtra("password");
            exportProcess(fileName, password);
        }
        if (requestCode == REQUEST_IMPORT_FILE_NAME && resultCode == RESULT_OK) {
            final String fileName = data.getStringExtra("file_name");
            final File exportFile = new File(fileName);
            if (exportFile.exists()) {
                getImportPasswords(fileName);
            } else {
                Toast.makeText(this, fileNotFound, Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_IMPORT_STORAGE_NAME && resultCode == RESULT_OK) {
            final String fileName = data.getStringExtra("file_name");
            final String filePassword = data.getStringExtra("file_password");
            final String storageName = data.getStringExtra("storage_name");
            final String storagePassword = data.getStringExtra("storage_password");
            importProcess(fileName, filePassword, storageName, storagePassword);
        }
    }

    private void exportProcess(String fileName, String password) {
        final Exporter exporter = ExporterFactory.createExporter();
        final String storageName = dataBase.getStorageName();
        final String storagePassword = dataBase.getStoragePassword();
        final boolean success = exporter.exportDb(storageName, storagePassword, fileName, password);
        if (success) {
            Toast.makeText(this, exportSuccess, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, exportError, Toast.LENGTH_LONG).show();
        }
    }

    private void importProcess(String fileName, String filePassword, String storageName, String storagePassword) {
        final Exporter exporter = ExporterFactory.createExporter();
        final boolean success = exporter.importDb(storageName, storagePassword, fileName, filePassword);
        if (success) {
            Toast.makeText(this, R.string.import_successful, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.import_error, Toast.LENGTH_LONG).show();
        }
    }

    private void getImportPasswords(final String fileName) {
        Intent i = new Intent(getApplicationContext(), GetImportPasswords.class);
        i.putExtra("file_name", fileName);
        startActivityForResult(i, REQUEST_IMPORT_STORAGE_NAME);
    }

    private void getPassword(String fileName) {
        Intent i = new Intent(getApplicationContext(), SetPasswordActivity.class);
        i.putExtra("file_name", fileName);
        startActivityForResult(i, REQUEST_SET_PASSWORD);
        //Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();
    }
}
