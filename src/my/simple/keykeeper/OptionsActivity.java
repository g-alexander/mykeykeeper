package my.simple.keykeeper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import my.simple.keykeeper.config.Configuration;
import my.simple.keykeeper.config.ConfigurationImpl;
import my.simple.keykeeper.data.entity.Category;

import java.util.Arrays;

/**
 * Created by Alex on 06.06.2014.
 */
public class OptionsActivity extends BaseActivity {

    private final Configuration config = new ConfigurationImpl(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.options_layout);
        setEnableMenu(false);
        initWidgets();
        initDefaultWidgets();
    }

    private void initWidgets() {
        initAzCheckBox();
        initAZCheckBox();
        init09CheckBox();
        initSimCheckBox();
        iniLengthTextBox();
    }

    private void initAzCheckBox() {
        final CheckBox field = (CheckBox)findViewById(R.id.options_az_checkbox);
        field.setChecked(config.getBoolean(Configuration.CONFIG_LOWER_LETTERS, true));
        field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setBoolean(Configuration.CONFIG_LOWER_LETTERS, b);
            }
        });
    }
    private void initAZCheckBox() {
        final CheckBox field = (CheckBox)findViewById(R.id.options_AZ_checkbox);
        field.setChecked(config.getBoolean(Configuration.CONFIG_UPPER_LETTERS, true));
        field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setBoolean(Configuration.CONFIG_UPPER_LETTERS, b);
            }
        });
    }
    private void init09CheckBox() {
        final CheckBox field = (CheckBox)findViewById(R.id.options_09_checkbox);
        field.setChecked(config.getBoolean(Configuration.CONFIG_NUMBERS_09, true));
        field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setBoolean(Configuration.CONFIG_NUMBERS_09, b);
            }
        });
    }
    private void initSimCheckBox() {
        final CheckBox field = (CheckBox)findViewById(R.id.options_sim_checkbox);
        field.setChecked(config.getBoolean(Configuration.CONFIG_SPECIAL_LETTERS, false));
        field.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                config.setBoolean(Configuration.CONFIG_SPECIAL_LETTERS, b);
            }
        });
    }
    private void iniLengthTextBox() {
        String[] array = new String[] {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
        final Spinner lengthField = (Spinner)findViewById(R.id.options_length_field);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList(array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthField.setAdapter(adapter);
        int length = config.getInteger(Configuration.CONFIG_PASSWORD_LENGTH, 8);
        lengthField.setSelection(length - 3);
        lengthField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                config.setInteger(Configuration.CONFIG_PASSWORD_LENGTH, i + 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {       }
        });
    }


}