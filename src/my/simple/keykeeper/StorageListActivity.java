package my.simple.keykeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import my.simple.keykeeper.config.Configuration;
import my.simple.keykeeper.config.ConfigurationImpl;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.StorageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 30.05.2014.
 */
public class StorageListActivity extends BaseActivity {

    private final StorageProvider storageProvider = DataProviderFactory.INSTANCE.getStorageProvider();
    private List<String> storages;
    private ArrayAdapter<String> adapter;
    private boolean needUpdate = false;
    private int selectedPosition;

    private String license;
    private boolean showLicense;



    private final Configuration conf = new ConfigurationImpl(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.storage_list_layout);
        initWidgets();
        initDefaultWidgets();

        this.license = getResources().getString(R.string.license);
        showLicenseDialog();
    }

    private void initWidgets() {
        initAddButton();
        initListView();
    }

    private void initAddButton() {
        final ImageView addButton = (ImageView)findViewById(R.id.add_storage_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonClick(false);
            }
        });
        final TextView addText = (TextView)findViewById(R.id.add_storage_text);
        if (addText != null) {
            addText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButtonClick(false);
                }
            });
        }
    }

    private void initListView() {
        ListView storageListView = (ListView)findViewById(R.id.storage_list_view);
        storages = new ArrayList<String>(storageProvider.getStorageSet());
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storages);
        storageListView.setAdapter(adapter);
        storageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                storageClick(i);
            }
        });
        registerForContextMenu(storageListView);
        checkEmptyList();
    }

    private void checkEmptyList() {
        if (storages.size() == 0) {
            addButtonClick(true);
        }
    }

    private void storageClick(int position) {
        final String storageName = storages.get(position);
        Intent storagePasswordActivity = new Intent(this, StoragePasswordActivity.class);
        storagePasswordActivity.putExtra("storage_name", storageName);
        startActivity(storagePasswordActivity);
    }

    private void refresh() {
        storages.clear();
        storages.addAll(storageProvider.getStorageSet());
        adapter.notifyDataSetChanged();
    }

    private void addButtonClick(boolean autoCreate) {
        Intent addStorageActivity = new Intent(this, AddStorageActivity.class);
        addStorageActivity.putExtra("auto_create", autoCreate);
        startActivity(addStorageActivity);
    }

    @Override
    protected void onPause() {
        this.needUpdate = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (needUpdate) {
            refresh();
            needUpdate = false;
        }
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.storage_popup_menu, menu);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        this.selectedPosition = acmi.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_storage_add:
                addButtonClick(false);
                break;
            case R.id.menu_storage_edit:
                editButtonClick();
                break;
            case R.id.menu_storage_remove:
                removeButtonClick();
                break;
        }
        return true;
    }

    private void editButtonClick() {
        Intent editStorageActivity = new Intent(this, EditStorageActivity.class);
        editStorageActivity.putExtra("storage_name", storages.get(selectedPosition));
        startActivity(editStorageActivity);
    }

    private void removeButtonClick() {
        DialogInterface.OnClickListener handle = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    storageProvider.removeStorage(storages.get(selectedPosition));
                    refresh();
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Remove storage " + storages.get(selectedPosition) + "?")
                .setPositiveButton("Yes", handle)
                .setNegativeButton("No", handle)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu_start, menu);
        return true;
    }

    private void showLicenseDialog() {

        if (!this.conf.getBoolean(Configuration.CONFIG_SHOW_TERMS_OF_USE, true)) {
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Terms of use");

        final CheckBox showTU = (CheckBox)dialog.findViewById(R.id.show_terms);
        showTU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeTermsOfUse(b);
            }
        });
        changeTermsOfUse(false); //Первоначальный сброс
        final Button closeButton = (Button)dialog.findViewById(R.id.dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void changeTermsOfUse(Boolean show) {
        this.conf.setBoolean(Configuration.CONFIG_SHOW_TERMS_OF_USE, show);
    }
}