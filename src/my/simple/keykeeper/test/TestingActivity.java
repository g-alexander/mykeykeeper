package my.simple.keykeeper.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import my.simple.keykeeper.R;
import my.simple.keykeeper.data.DataProviderFactory;
import my.simple.keykeeper.data.entity.Category;
import my.simple.keykeeper.data.entity.KeyRecord;

/**
 * Created by Alex on 14.05.2014.
 */
public class TestingActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        Button addButton = (Button)findViewById(R.id.add_category);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategotyClick();
            }
        });

        Button saveDatabaseButton = (Button)findViewById(R.id.save_database);
        saveDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDatabaseClick();
            }
        });

        Button loadDtabaseButton = (Button)findViewById(R.id.load_database);
        loadDtabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatabaseClick();
            }
        });
    }

    public void addCategotyClick() {
        Category category = new Category("Category name");
        DataProviderFactory.INSTANCE.getDataBase().addCategory(category);
        category = new Category("Category 2");
        DataProviderFactory.INSTANCE.getDataBase().addCategory(category);
        KeyRecord rec = new KeyRecord("Rec name", category, "Login 1", "Pass 1", "Description 1");
        DataProviderFactory.INSTANCE.getDataBase().addKeyRecord(rec);
    }

    public void saveDatabaseClick() {
        DataProviderFactory.INSTANCE.getDataBase().save();
    }

    public void loadDatabaseClick() {

       // DataProviderFactory.INSTANCE.getDataBase().load();
    }
}