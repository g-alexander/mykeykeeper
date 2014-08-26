package my.simple.keykeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.adapters.KeyRecordAdapter;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.KeyRecord;

import java.util.*;

public class KeyListActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();
    private KeyRecordAdapter adapter;
    private List<KeyRecord> records;
    private boolean needUpdate = false;

    private Category filterCategory;
    private TextView header;

    private final KeyRecord selectedRecord = new KeyRecord();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        ListView list = (ListView)findViewById(R.id.records_list);
        records = new ArrayList<KeyRecord>(dataBase.getAllKeyRecords());
        adapter = new KeyRecordAdapter(this, records);
        list.setAdapter(adapter);
        registerForContextMenu(list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showPreview(position);
            }
        });

        prepareButtons();

        selectedRecord.setId(-1);

        initDefaultWidgets();

        this.header = (TextView)findViewById(R.id.records_header);
    }

    private void showPreview(int position) {
        Intent previewActivity = new Intent(this, PreviewRecordActivity.class);
        previewActivity.putExtra("preview_record_id", records.get(position).getId());
        startActivity(previewActivity);
    }

    private void prepareButtons() {
        final ImageView addButton = (ImageView)findViewById(R.id.add_record_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecordButtonClick();
            }
        });
        final TextView addText = (TextView)findViewById(R.id.add_record_text);
        if (addText != null) {
            addText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRecordButtonClick();
                }
            });
        }
        final ImageView filterButton = (ImageView)findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterButtonClick();
            }
        });
        final TextView filterText = (TextView)findViewById(R.id.records_filter);
        if (filterText != null) {
            filterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filterButtonClick();
                }
            });
        }

    }

    private void filterButtonClick() {
        final Collection<Category> categories = dataBase.getAllCategories();
        final List<String> categoryNames = new LinkedList<String>();
        categoryNames.add("All");
        for (Category c : categories) {
            categoryNames.add(c.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Categories");

        builder.setItems(categoryNames.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    filterClicked(null);
                } else {
                    filterClicked(categories.toArray(new Category[0])[item - 1]);
                }
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void filterClicked(Category category) {
        this.filterCategory = category;
        refreshRecordsData();
    }

    private void addRecordButtonClick() {
        Intent addActivity = new Intent(this, AddRecordActivity.class);
        startActivity(addActivity);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (needUpdate) {
            refreshRecordsData();
        }
    }

    private void refreshRecordsData() {
        records.clear();
        if (filterCategory == null) {
            records.addAll(dataBase.getAllKeyRecords());
            header.setText("all records");
        } else {
            records.addAll(dataBase.getRecordsByCategory(filterCategory));
            header.setText(filterCategory.getName());
        }
        adapter.notifyDataSetChanged();
        needUpdate = false;
    }

    @Override
    protected void onPause() {
        this.needUpdate = true;
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.record_popup_menu, menu);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final KeyRecord rec = records.get(acmi.position);
        selectedRecord.setId(rec.getId());
        selectedRecord.setName(rec.getName());
        selectedRecord.setCategory(rec.getCategory());
        selectedRecord.setLogin(rec.getLogin());
        selectedRecord.setPassword(rec.getPassword());
        selectedRecord.setDescription(rec.getDescription());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add_record :
                addRecordButtonClick();
                break;
            case R.id.menu_edit_record :
                editRecordButtonClick();
                break;
            case R.id.menu_remove_record :
                removeRecordButtonClick();
                break;
        }
        return true;
    }

    private void editRecordButtonClick() {
        Intent addRecordActivity = new Intent(this, AddRecordActivity.class);
        addRecordActivity.putExtra("record_edit_id", selectedRecord.getId());
        startActivity(addRecordActivity);
    }

    private void removeRecordButtonClick() {
        dataBase.removeKeyRecord(selectedRecord);
        dataBase.save();
        refreshRecordsData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
