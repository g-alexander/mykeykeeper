package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.adapters.KeyRecordAdapter;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
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
    }

    private void showPreview(int position) {
        Intent previewActivity = new Intent(this, PreviewRecordActivity.class);
        previewActivity.putExtra("preview_record_id", records.get(position).getId());
        startActivity(previewActivity);
    }

    private void prepareButtons() {
        final ImageButton addButton = (ImageButton)findViewById(R.id.add_record_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecordButtonClick();
            }
        });
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
        records.addAll(dataBase.getAllKeyRecords());
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
}
