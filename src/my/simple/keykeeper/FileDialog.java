package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.adapters.FileDialogAdapter;
import my.simple.keykeeper.data.entity.FileDialogItem;
import my.simple.keykeeper.providers.ExternalProvider;
import my.simple.keykeeper.providers.FileProvider;

import java.util.List;

/**
 * Created by Alex on 15.09.2014.
 */
public class FileDialog extends BaseActivity {

    private ExternalProvider provider = new FileProvider();
    private FileDialogAdapter adapter;

    private String selectedFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_dialog);
        initListView();
        initSelectButton();
        initDefaultWidgets();
    }

    private void initSelectButton() {
        View selectButton = findViewById(R.id.file_name_ok);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelect();
            }
        });
        TextView selectText = (TextView)findViewById(R.id.file_name_ok_text);
        selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelect();
            }
        });
    }

    private void onSelect() {
        final EditText fileName = (EditText)findViewById(R.id.file_name);
        selectedFile = provider.getCurrentPath() + "/" + fileName.getText();
        Intent resInt = new Intent();
        resInt.putExtra("file_name", selectedFile);
        setResult(RESULT_OK, resInt);
        finish();
    }

    private void initListView() {
        ListView list = (ListView)findViewById(R.id.file_dialog_list);
        adapter = new FileDialogAdapter(this, provider.getCurrentData());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemClicked(i);
            }
        });
    }

    private void itemClicked(int itemPos) {
        FileDialogItem item = adapter.getItem(itemPos);
        if (item.isDirectory() || item.isUp()) {
            provider.changePath(item);
            refreshDialogData();
        } else {
            final EditText edit = (EditText)findViewById(R.id.file_name);
            edit.setText(item.getName());
        }
    }

    private void refreshDialogData() {
        List<FileDialogItem> newList = provider.getCurrentData();
        adapter.clear();
        for (FileDialogItem item : newList) {
            adapter.add(item);
        }
        adapter.notifyDataSetInvalidated();
    }
}