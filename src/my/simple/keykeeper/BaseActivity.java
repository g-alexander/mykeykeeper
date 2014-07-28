package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Alex on 10.06.2014.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initDefaultWidgets() {
        initBackButton();
        initExitButton();
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
    }

    private void initExitButton() {
        final ImageButton exit = (ImageButton)findViewById(R.id.storage_close_button);
        if (exit != null) {
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exit();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_options:
                Intent optionsActivity = new Intent(this, OptionsActivity.class);
                startActivity(optionsActivity);
                break;
            case R.id.main_menu_exit:
                exit();
                break;
        }
        return true;
    }

    protected void exit() {
        Intent intent = new Intent(getApplicationContext(), StorageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
