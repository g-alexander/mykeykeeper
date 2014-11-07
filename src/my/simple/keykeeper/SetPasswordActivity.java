package my.simple.keykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Alex on 07.10.2014.
 */
public class SetPasswordActivity extends Activity {

    private String enterPasswordMsg;
    private String differentPasswordsMsg;
    private String fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_password_layout);
        prepareButtons();

        this.fileName = getIntent().getStringExtra("file_name");

        enterPasswordMsg = getResources().getString(R.string.enter_password);
        differentPasswordsMsg = getResources().getString(R.string.different_passwords);
    }

    private void prepareButtons() {
        final ImageButton okButton = (ImageButton)findViewById(R.id.set_password_button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okButtonClicked();
            }
        });

        final ImageButton cancelButton = (ImageButton)findViewById(R.id.set_password_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClicked();
            }
        });
    }

    private void okButtonClicked() {
        final EditText passwordField = (EditText)findViewById(R.id.set_password_field);
        final String password = passwordField.getText().toString();

        final EditText repeatPasswordField = (EditText)findViewById(R.id.set_repeat_password_field);
        final String repeatPassword = repeatPasswordField.getText().toString();

        if (password.equals("")) {
            Toast.makeText(this, enterPasswordMsg, Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, differentPasswordsMsg, Toast.LENGTH_LONG).show();
            return;
        }

        Intent resInt = new Intent();
        resInt.putExtra("password", password);
        resInt.putExtra("file_name", fileName);
        setResult(RESULT_OK, resInt);
        finish();
    }

    private void cancelButtonClicked() {
        Intent resInt = new Intent();
        setResult(RESULT_CANCELED, resInt);
        finish();
    }
}