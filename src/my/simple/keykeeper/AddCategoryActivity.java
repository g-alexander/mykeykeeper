package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.Category;

public class AddCategoryActivity extends BaseActivity {

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();
    private final Category category = new Category();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_category_layout);
        initCategory();
        initWidgets();
    }

    private void initCategory() {
        int categoryId = getIntent().getIntExtra("category_id", -1);
        if (categoryId == -1) {
            this.category.setId(-1);
            this.category.setName("");
        } else {
            final Category cat = dataBase.getCategoryById(categoryId);
            this.category.setId(cat.getId());
            this.category.setName(cat.getName());
        }
    }

    private void initWidgets() {
        initSaveButton();
        initCancelButton();
        initNameField();
    }

    private void initNameField() {
        final EditText categoryNameField = (EditText)findViewById(R.id.add_category_name_field);
        categoryNameField.setText(this.category.getName());
    }

    private void initSaveButton() {
        ImageButton saveButton = (ImageButton)findViewById(R.id.add_category_button_ok);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClick();
            }
        });
    }

    private void initCancelButton() {
        ImageButton cancelButton = (ImageButton)findViewById(R.id.add_category_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });
    }

    private void saveButtonClick() {
        final EditText categoryNameField = (EditText)findViewById(R.id.add_category_name_field);
        this.category.setName(categoryNameField.getText().toString());
        if (this.category.getId() == -1) {
            category.generateId();
        }
        dataBase.addCategory(category);
        dataBase.save();
        finish();
    }

    private void cancelButtonClick() {
        finish();
    }
}