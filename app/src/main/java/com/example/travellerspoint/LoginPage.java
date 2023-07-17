package com.example.travellerspoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travellerspoint.data.FirebaseHandler;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.data.SharedPreference;
import com.example.travellerspoint.interfaces.UserCheckListener;
import com.example.travellerspoint.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPage extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2003;
    private static final String TAG = "firebase_log";
    private SharedPreferences sp;
    private EditText userid, password;
    private TextView warning, forgotPass;
    private Button login, googleSignIn;
    private MyDbHandler db;
    private FirebaseHandler fbHandler;
    private ImageView eye;
    private int type;
    private Boolean LOGIN_STAT;
    private String USERNAME;

    //Firebase values
    private FirebaseAuth fAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference dbRef;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userid = findViewById(R.id.input_login);
        password = findViewById(R.id.input_password);
        warning = findViewById(R.id.warning);
        login = findViewById(R.id.login_button);
        googleSignIn = findViewById(R.id.google_button);
        eye = findViewById(R.id.EyePass_icon);
        forgotPass = findViewById(R.id.btn_forgotPass);
        LOGIN_STAT = false;
        db = new MyDbHandler(LoginPage.this);
        fbHandler = new FirebaseHandler(LoginPage.this);

        type = password.getInputType();

        dbRef = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginPage.this);
        progressDialog.setTitle("CreatingAccount");
        progressDialog.setMessage("your account is being created..");
        progressDialog.setCancelable(false);

        //Login State true ot false. If true login directly
        sp = getSharedPreferences("LoginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Log.d("roshanTest", "LOGIN_STAT_SP:  " + sp.getBoolean("LOGIN_STAT_SP", LOGIN_STAT));

        // Check if for login state - if true then open Main activity / if false stay here
        if (sp.getBoolean("LOGIN_STAT_SP", LOGIN_STAT)) {
            Log.d("roshanTest", "LOGIN_STAT: " + sp.getBoolean("LOGIN_STAT_SP", LOGIN_STAT));

            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finishAffinity();
        }

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eye.getAlpha() == 1f) {
                    eye.setAlpha(.3f);
                    password.setInputType(type);
                    password.setSelection(password.getText().length());
                } else {
                    eye.setAlpha(1f);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userid.getText().toString();
                String pass = password.getText().toString();
                User user = new User();
                user.setUser_name(userName);
                user.setPassword(pass);
                user.setMail_id(userName);

                warning.setAlpha(0f);
                password.setTranslationY(0f);

                if (userName.equals("") || pass.equals(""))
                    Toast.makeText(LoginPage.this, "Pleas enter all fields...", Toast.LENGTH_SHORT).show();
                else {
                    fbHandler.checkPassword(user, new FirebaseHandler.CheckPasswordCallback() {
                        @Override
                        public void onPasswordExist(boolean exist) {
                            if (exist) {
                                fbHandler.checkUserPass(user, new FirebaseHandler.FirebaseCheckUserPassCallback() {
                                    @Override
                                    public void onCheckUserPass(boolean passwordMatch) {
                                        if (passwordMatch) {
                                            Toast.makeText(LoginPage.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginPage.this, MainActivity.class));

                                            fbHandler.getUsername(userName, new FirebaseHandler.UsernameCallback() {
                                                @Override
                                                public void onUserNameReceived(String username) {
                                                    USERNAME = username;
                                                    SharedPreference.setDefaults("USERNAME", USERNAME, getApplicationContext());

                                                    Log.d("roshanTest", "LOGIN_STAT in Login: " + LOGIN_STAT);

                                                    editor.putBoolean("LOGIN_STAT_SP", true);
                                                    editor.apply();
                                                    finish();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginPage.this, "Invalid username or password.. Try Again!", Toast.LENGTH_SHORT).show();
                                            warning.setText("Invalid username or password");
                                            warning.setAlpha(0.5f);
                                            password.setTranslationY(10f);
                                        }
                                    }
                                });
                            } else {
                                String title = "Incorrect Authentication Method!";
                                String message = "Password is not set.\nPleas try logging in with google..";
                                showAlertDialogue(LoginPage.this, title, message);
                            }
                        }
                    });
                }
            }
        });

        //Forgot password button
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                loginWithGoogle();
            }
        });
    }

    public void newAccount(View view) {
        Intent intent = new Intent(LoginPage.this, SignInPage.class);
        startActivity(intent);
    }

    public void showAlertDialogue(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loginWithGoogle(){
        // Sign out the current user
        fAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("404977261275-8vml9vfogn7u8lb71iveialcp1t16su4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        if (!(GoogleSignIn.getClient(this, gso)).equals("com.google.android.gms.auth.api.signin.GoogleSignInClient@ee82a4a")) {
            GoogleSignIn.getClient(this, gso).signOut();
        }
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.d("firebase_log", "signInWithGoogle: " + googleSignInClient);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    fbHandler.firebaseAuthentication(this, this, sp, account, progressDialog);
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}