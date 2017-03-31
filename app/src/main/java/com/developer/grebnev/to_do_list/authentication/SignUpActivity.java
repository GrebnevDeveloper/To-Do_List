package com.developer.grebnev.to_do_list.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.grebnev.to_do_list.MainActivity;
import com.developer.grebnev.to_do_list.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Grebnev on 22.01.2016.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;

    private TextInputLayout ilEmail;
    private TextInputLayout ilPassword;
    private TextInputLayout ilRepeatPassword;

    private Button btnSignUp;
    private Button btnLinkSingIn;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        etRepeatPassword = (EditText) findViewById(R.id.input_repeat_password);

        ilEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        ilPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        ilRepeatPassword = (TextInputLayout) findViewById(R.id.input_layout_repeat_password);

        btnLinkSingIn = (Button) findViewById(R.id.btn_link_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnLinkSingIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        String userEmail = etEmail.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();

        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage(getString(R.string.request_in_progress));

        switch (v.getId()) {
            case R.id.btn_link_sign_in:
                Log.v(TAG, "Button link sign in click");
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                if(!checkEmail()) {
                    return;
                }
                if(!checkPassword()) {
                    return;
                }
                if(!checkRepeatPassword()) {
                    return;
                }
                dialog.show();
                Log.v(TAG, "Button create account click");
                signUp(userEmail, userPassword, dialog);
                break;
        }
    }

    private void signUp(String userEmail, String userPassword, final ProgressDialog dialog) {
        Log.v(TAG, userEmail + " " + userPassword + "SignUpActivity");
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.v(TAG, "Complete");
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, R.string.you_sign_in, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignUpActivity.this, R.string.you_can_not_this_email, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkEmail() {
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            ilEmail.setErrorEnabled(true);
            ilEmail.setError(getString(R.string.enter_valid_email));
            etEmail.setError(getString(R.string.required));
            requestFocus(etEmail);
            return false;
        }
        ilEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        String password = etPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            ilPassword.setError(getString(R.string.enter_valid_password));
            etPassword.setError(getString(R.string.required));
            requestFocus(etPassword);
            return false;
        }
        ilPassword.setErrorEnabled(false);
        return true;
    }

    private boolean checkRepeatPassword() {
        String password = etPassword.getText().toString().trim();
        String repeatPassword = etRepeatPassword.getText().toString().trim();
        if (repeatPassword.isEmpty() || !isRepeatPasswordValid(password, repeatPassword)) {

            ilRepeatPassword.setError(getString(R.string.password_not_match));
            etRepeatPassword.setError(getString(R.string.required));
            requestFocus(etRepeatPassword);
            return false;
        }
        ilRepeatPassword.setErrorEnabled(false);
        return true;
    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private static boolean isRepeatPasswordValid(String password, String repeatPassword){
        return (password.equals(repeatPassword));
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
