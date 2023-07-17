package com.example.travellerspoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travellerspoint.data.FirebaseHandler;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.model.User;

public class ResetActivity extends AppCompatActivity {
    private EditText username, password, confirm_pass;
    private Button confirm;
    private FirebaseHandler fbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        confirm_pass = (EditText) findViewById(R.id.confirm_password);
        confirm = (Button) findViewById(R.id.confrim_button);
        fbHandler = new FirebaseHandler(this);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra(ForgotPassword.USER_NAME));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();
                String confPass = confirm_pass.getText().toString();

                User user = new User();
                user.setUser_name(userName);
                user.setPassword(pass);
                user.setMail_id(userName);

                if (userName.equals("") || pass.equals("") || confPass.equals(""))
                    Toast.makeText(ResetActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                else {
                    if(pass.equals(confPass)) {
                        fbHandler.updatePass(ResetActivity.this, user, new FirebaseHandler.UpdatePassCallback() {
                            @Override
                            public void onPasswordUpdated(boolean updated) {
                                if (updated) {
                                    Intent intent = new Intent(ResetActivity.this, LoginPage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ResetActivity.this, "Password not updated!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ResetActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}