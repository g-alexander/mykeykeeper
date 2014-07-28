package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.KeyRecord;
import my.simple.keykeeper.generators.PasswordGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddRecordActivity extends BaseActivity {

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();
    private ArrayAdapter<Category> adapter;
    private List<Category> categoryList = new ArrayList<Category>();
    private boolean needUpdate = false;
    private final KeyRecord record = new KeyRecord();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_record_layout);
        initRecordData();
        initWidgets();
    }

    private void initRecordData() {
        int recId = this.getIntent().getIntExtra("record_edit_id", -1);
        if (recId != -1) {
            KeyRecord rec = dataBase.getKeyRecordById(recId);
            record.setId(rec.getId());
            record.setName(rec.getName());
            record.setCategory(rec.getCategory());
            record.setLogin(rec.getLogin());
            record.setPassword(rec.getPassword());
            record.setDescription(rec.getDescription());
        } else {
            record.setId(-1);
        }
    }

    private void initWidgets() {
        initCategories();
        initCancelButton();
        initSaveButton();
        initAddCategoryButton();
        initRecordFields();
        initGenerateButton();
    }

    private void initRecordFields() {
        EditText nameField = (EditText)findViewById(R.id.add_field_name);
        nameField.setText(record.getName());
        Spinner categoryField = (Spinner)findViewById(R.id.add_field_category);
        categoryField.setSelection(categoryList.indexOf(record.getCategory()));
        EditText loginField = (EditText)findViewById(R.id.add_field_login);
        loginField.setText(record.getLogin());
        EditText passwordField = (EditText)findViewById(R.id.add_field_password);
        passwordField.setText(record.getPassword());
        EditText descriptionField = (EditText)findViewById(R.id.add_field_description);
        descriptionField.setText(record.getDescription());
    }

    private void initCategories() {
        final Spinner spinner = (Spinner)findViewById(R.id.add_field_category);
        categoryList.clear();
        categoryList.addAll(dataBase.getAllCategories());
        adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initCancelButton() {
        final ImageButton cancelButton = (ImageButton)findViewById(R.id.add_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initSaveButton() {
        final ImageButton saveButton = (ImageButton)findViewById(R.id.add_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveKeyRecord();
            }
        });
    }

    private void initAddCategoryButton() {
        final ImageButton categoryButton = (ImageButton)findViewById(R.id.add_button_add_category);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategoryClick();
            }
        });
    }

    private void initGenerateButton() {
        final ImageButton generateButton = (ImageButton)findViewById(R.id.add_button_generate_password);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateButtonClick();
            }
        });
    }

    private void generateButtonClick() {
        final EditText passwordField = (EditText)findViewById(R.id.add_field_password);
        passwordField.setText(generatePassword());
    }

    private String generatePassword() {
        PasswordGenerator generator = new PasswordGenerator(this);
        return generator.generatePassword();
    }

    private void addCategoryClick() {
        Intent categoryEditActivity = new Intent(this, CategoryEditActivity.class);
        startActivity(categoryEditActivity);
    }

    private void saveKeyRecord() {
        final KeyRecord record = new KeyRecord();
        final EditText nameField = (EditText)findViewById(R.id.add_field_name);
        final String name = nameField.getText() != null ? nameField.getText().toString() : "";
        final Spinner categoryField = (Spinner)findViewById(R.id.add_field_category);
        final Category category = (Category)categoryField.getSelectedItem();
        final EditText loginField = (EditText)findViewById(R.id.add_field_login);
        final String login = loginField.getText() != null ? loginField.getText().toString() : "";
        final EditText passwordField = (EditText)findViewById(R.id.add_field_password);
        final String password = passwordField.getText() != null ? passwordField.getText().toString() : "";
        final EditText descriptionField = (EditText)findViewById(R.id.add_field_description);
        final String description = descriptionField.getText() !=  null ? descriptionField.getText().toString() : "";
        record.setName(name);
        record.setLogin(login);
        record.setCategory(category);
        record.setPassword(password);
        record.setDescription(description);
        if (record.getId() == -1) {
            record.generateId();
        }
        dataBase.addKeyRecord(record);
        dataBase.save();
        this.finish();
    }

    private void refreshCategories() {
        this.categoryList.clear();
        this.categoryList.addAll(dataBase.getAllCategories());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        this.needUpdate = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needUpdate) {
            refreshCategories();
        }
    }


}