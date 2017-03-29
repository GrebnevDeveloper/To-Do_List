package com.developer.grebnev.to_do_list;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Grebnev on 22.01.2016.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;

    private Button btnSignUp;
    private Button btnSingIn;
    private ImageButton imbtnGooglePlus;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
//        etRepeatPassword = (EditText) findViewById(R.id.input_repeat_password);
//        etRepeatPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == R.id.et_action_sign_up || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
//                    signUp();
//                    return true;
//                }
//                return false;
//            }
//        });

        btnSingIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        imbtnGooglePlus = (ImageButton) findViewById(R.id.imbtn_google_plus);
        btnSingIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        imbtnGooglePlus.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAutWithGooglePlus(account);
            }
        }

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

        dialog = new ProgressDialog(SignUp.this);
        dialog.setMessage(getString(R.string.request_in_progress));
        dialog.show();

        switch (v.getId()) {
            case R.id.btn_sign_in:
                Log.v(TAG, "Button sign in click");
                signIn(userEmail, userPassword, dialog);
                break;
            case R.id.btn_sign_up:
                Log.v(TAG, "Button create account click");
                signUp(userEmail, userPassword, dialog);
                break;
            case R.id.imbtn_google_plus:
                Log.v(TAG, "ImageButton google plus click");
                signInWithGoogle();
                break;
        }
    }

    private void signIn(String userEmail, String userPassword, final ProgressDialog dialog) {
        Log.v(TAG, userEmail + " " + userPassword + " SignIn");
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.v(TAG, "Complete");
                Toast.makeText(SignUp.this, task.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignUp.this, task.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signUp(String userEmail, String userPassword, final ProgressDialog dialog) {
        Log.v(TAG, userEmail + " " + userPassword + "SignUp");
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.v(TAG, "Complete");
                Toast.makeText(SignUp.this, task.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(SignUp.this, task.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signInWithGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void firebaseAutWithGooglePlus(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        dialog.dismiss();


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
