package com.developer.grebnev.to_do_list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Grebnev on 22.01.2016.
 */
public class SignUp extends Activity {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;

    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "ShJAJSxb43uB6M2VTWwcxGwjPiEt4leEvaVrZV0R", "hNhxeMcdGioub4xCAlWyFA2NuprQIOFmrHWz3BkF");
        setContentView(R.layout.sign_up);

        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        etRepeatPassword = (EditText) findViewById(R.id.input_repeat_password);
        etRepeatPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.et_action_sign_up || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    signUp();
                    return true;
                }
                return false;
            }
        });

        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }



    private void signUp() {
        String userEmail = etEmail.getText().toString();
        String userPassword = etPassword.getText().toString();
        String userRepeatPassword = etRepeatPassword.getText().toString();

        final ProgressDialog dialog = new ProgressDialog(SignUp.this);
        dialog.setMessage(getString(R.string.progress_sing_up));
        dialog.show();

        ParseUser user = new ParseUser();
        user.setUsername(userEmail);
        user.setPassword(userPassword);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
               }
            }
        });
    }

}
