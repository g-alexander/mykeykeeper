package my.simple.keykeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.adapters.CategoryAdapter;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.api.DataBase;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.exceptions.RecordsExistsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 26.05.2014.
 */
public class CategoryEditActivity extends BaseActivity {

    private String categoryContainsRecord;

    private final DataBase dataBase = DataProviderFactory.INSTANCE.getDataBase();
    private final List<Category> categories = new ArrayList<Category>();
    private CategoryAdapter adapter;
    private boolean needUpdate = false;
    private Category selectedCategory;

    private void initTextStrings() {
        categoryContainsRecord = getResources().getString(R.string.category_contains_record);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.category_layout);
        initTextStrings();
        initCategoryList();
        initAddButton();
        initDefaultWidgets();
    }

    private void initCategoryList() {
        final ListView categoriesListView = (ListView)findViewById(R.id.category_list_view);
        categories.addAll(dataBase.getAllCategories());
        adapter = new CategoryAdapter(this, this.categories);
        categoriesListView.setAdapter(adapter);
        registerForContextMenu(categoriesListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.category_popup_menu, menu);
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
        this.selectedCategory = this.categories.get(acmi.position);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        //return super.onMenuItemSelected(featureId, item);
        switch (item.getItemId()) {
            case R.id.menu_add :
                addButtonClick();
                break;
            case R.id.menu_edit :
                editButtonClick();
                break;
            case R.id.menu_remove :
                removeCategory();
                break;
        }
        return true;
    }

    private void initAddButton() {
        final View addButton = findViewById(R.id.add_category_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButtonClick();
            }
        });
        final TextView addText = (TextView)findViewById(R.id.add_category_text);
        if (addText != null) {
            addText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addButtonClick();
                }
            });
        }
    }

    private void addButtonClick() {
        Intent addCategoryActivity = new Intent(this, AddCategoryActivity.class);
        startActivity(addCategoryActivity);
    }

    private void editButtonClick() {
        Intent addCategoryActivity = new Intent(this, AddCategoryActivity.class);
        addCategoryActivity.putExtra("category_id", this.selectedCategory.getId());
        startActivity(addCategoryActivity);
    }

    private void removeCategory() {
        try {
            dataBase.removeCategory(selectedCategory);
            dataBase.save();
            refreshData();
        } catch (RecordsExistsException e) {
            removeCategoryForce();
        }
    }

    private void removeCategoryForce() {
        DialogInterface.OnClickListener handle = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    dataBase.removeCategoryForce(selectedCategory);
                    dataBase.save();
                    refreshData();
                }
            }
        };
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(categoryContainsRecord)
                .setPositiveButton("Yes", handle)
                .setNegativeButton("No", handle)
                .show();
    }

    @Override
    protected void onPause() {
        needUpdate = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needUpdate) {
            refreshData();
        }
    }

    private void refreshData() {
        categories.clear();
        categories.addAll(dataBase.getAllCategories());
        adapter.notifyDataSetChanged();
        needUpdate = false;
    }
}