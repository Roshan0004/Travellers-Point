package com.example.travellerspoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInPage extends AppCompatActivity {
    private Button signup, googleSignUp;
    private TextView warning;
    private EditText user_name, mail_id, password, conf_password;
    private MyDbHandler db;
    private FirebaseHandler fbHandler;
    private SharedPreferences sp;

    //Firebase Database
    private DatabaseReference dbRef;
    private FirebaseAuth fAuth;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;

    private boolean LOGIN_STAT = false;
    private static final int RC_SIGN_IN = 2003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        user_name = findViewById(R.id.input_username);
        mail_id = findViewById(R.id.input_mail);
        password = findViewById(R.id.input_password);
        conf_password = findViewById(R.id.confirm_password);
        signup = findViewById(R.id.signin_button);
        googleSignUp = findViewById(R.id.floating_google_button);
        warning = findViewById(R.id.warning);
        db = new MyDbHandler(SignInPage.this);
        fbHandler = new FirebaseHandler(SignInPage.this);
        sp = getSharedPreferences("LoginPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //Firebase Database
        dbRef = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignInPage.this);
        progressDialog.setTitle("CreatingAccount");
        progressDialog.setMessage("your account is being created..");
        progressDialog.setCancelable(false);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = user_name.getText().toString();
                String mailId = mail_id.getText().toString();
                String pass = password.getText().toString();
                String confPass = conf_password.getText().toString();

                User newUser = new User();
                newUser.setUser_name(username);
                newUser.setPassword(pass);
                newUser.setMail_id(mailId);

                warning.setAlpha(0f);
                password.setTranslationY(0f);
                conf_password.setTranslationY(0f);

                if(username.equals("") || mailId.equals("") || pass.equals("") || confPass.equals(""))
                    Toast.makeText(SignInPage.this, "Pleas enter all fields...", Toast.LENGTH_SHORT).show();
                else {
                    if(emailValidate(mailId)) {
                        if (pass.equals(confPass)) {
                            fbHandler.checkUser(newUser, new UserCheckListener() {
                                @Override
                                public void onUserExist(Boolean exist) {
                                    if (exist){
                                        //User exists
                                        Toast.makeText(SignInPage.this, "User Already exists! Please LOGIN..", Toast.LENGTH_SHORT).show();
                                        user_name.setTextColor(getResources().getColor(R.color.red));
                                        user_name.startAnimation(AnimationUtils.loadAnimation(SignInPage.this, R.anim.shake));
                                        mail_id.setText("");
                                        password.setText("");
                                        conf_password.setText("");
                                    } else {
                                        Log.d("RoshanDB", "User Does Not Exists!");
                                        fbHandler.insertUser(newUser);
                                        SharedPreference.setDefaults("USERNAME", username, getApplicationContext());
                                        startActivity(new Intent(SignInPage.this, MainActivity.class));
                                        LOGIN_STAT = true;
                                        Intent intent = new Intent(SignInPage.this, LoginPage.class);
                                        intent.putExtra("LOGIN_STAT", LOGIN_STAT);
                                        editor.putBoolean("LOGIN_STAT_SP", LOGIN_STAT);
                                        editor.apply();
                                        finishAffinity();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignInPage.this, "Password Doesn't Match..", Toast.LENGTH_SHORT).show();
                            conf_password.startAnimation(AnimationUtils.loadAnimation(SignInPage.this, R.anim.shake));
                        }
                    } else {
                        Toast.makeText(SignInPage.this, "Invalid Email Address..", Toast.LENGTH_SHORT).show();
                        warning.setText("Invalid Email Address!");
                        warning.setAlpha(0.7f);
                        password.setTranslationY(20f);
                        conf_password.setTranslationY(20f);
                    }
                }
            }
        });

        //google sign-in
        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle(){
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

    public boolean emailValidate(String email){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = PATTERN.matcher(email);
        if (matcher.matches())
            return true;
        else
            return false;
    }
}