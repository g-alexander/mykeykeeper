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
import android.widget.TextView;

/**
 * Created by Alex on 10.06.2014.
 */
public class BaseActivity extends Activity {

    private boolean enableMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initDefaultWidgets() {
        initBackButton();
        initExitButton();
        initOptionsButton();
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
        }
        return true;
    }

    protected void exit() {
        Intent intent = new Intent(getApplicationContext(), StorageListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean isEnableMenu() {
        return enableMenu;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }
}
