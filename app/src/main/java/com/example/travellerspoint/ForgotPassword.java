package com.example.travellerspoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travellerspoint.data.FirebaseHandler;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.interfaces.UserCheckListener;
import com.example.travellerspoint.model.User;

public class ForgotPassword extends AppCompatActivity {

    public static final String USER_NAME = "com.example.travellerspoint.roshan.forgot";
    private TextView createNewAccount;
    private EditText username;
    private Button resetPass;
    private FirebaseHandler fbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        createNewAccount = (TextView) findViewById(R.id.new_account);
        username = (EditText) findViewById(R.id.input_username);
        resetPass = (Button) findViewById(R.id.reset_button);
        fbHandler = new FirebaseHandler(getApplicationContext());

        //reset password
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();

                User user = new User();
                user.setUser_name(userName);
                user.setPassword(null);
                user.setMail_id(userName);

                fbHandler.checkUser(user, new UserCheckListener() {
                    @Override
                    public void onUserExist(Boolean exist) {
                        if (exist){
                            Intent intent = new Intent(ForgotPassword.this, ResetActivity.class);
                            intent.putExtra(USER_NAME, userName);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgotPassword.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //create new account
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, SignInPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}