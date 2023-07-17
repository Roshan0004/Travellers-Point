package com.example.travellerspoint.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.travellerspoint.MainActivity;
import com.example.travellerspoint.SignInPage;
import com.example.travellerspoint.interfaces.UserCheckListener;
import com.example.travellerspoint.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseHandler {
    private DatabaseReference dbRef;
    private FirebaseAuth fAuth;
    private Context context;
    public static final String TAG = "firebase_log";


    public interface FirebaseCheckUserPassCallback {
        void onCheckUserPass(boolean passwordMatch);
    }

    public interface UserDetailsCallback {
        void onUserDetailsReceived(User user);
        void onCancelled(DatabaseError databaseError);
    }

    public interface  UsernameCallback {
        void onUserNameReceived(String username);
    }

    public interface UpdatePassCallback {
        void onPasswordUpdated(boolean updated);
    }

    public interface CheckPasswordCallback{
        void onPasswordExist(boolean exist);
    }

    public FirebaseHandler(Context context) {
        dbRef = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        this.context = context;
    }


    public void insertUser(User user){
        DatabaseReference userRef = dbRef.child("Users");
        String userId = userRef.push().getKey();

        if (userId != null){
           userRef.child(userId).setValue(user)
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show();
                           Log.d(TAG, "onSuccess: User inserted..");
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(context, "Registration failed!", Toast.LENGTH_SHORT).show();
                           Log.d(TAG, "onFailure: Failed to insert..");
                       }
                   });
        }
    }

    public void checkUser(User user, UserCheckListener check){
        DatabaseReference userRef = dbRef.child("Users");
        Query queryByUsername = userRef.orderByChild("user_name").equalTo(user.getUser_name());
        Query queryByMail = userRef.orderByChild("mail_id").equalTo(user.getMail_id());

        Log.d(TAG, "checkUser: is Called..");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check.onUserExist(true);
                    // Remove the listeners after receiving the snapshot
                    queryByMail.removeEventListener(this);
                    queryByUsername.removeEventListener(this);
                } else {
                    check.onUserExist(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Error");
            }
        };

        queryByUsername.addListenerForSingleValueEvent(valueEventListener);
        queryByMail.addListenerForSingleValueEvent(valueEventListener);
    }

    public void checkEmail(String email, UserCheckListener check){
        DatabaseReference userRef = dbRef.child("Users");
        Query query = userRef.orderByChild("mail_id").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    check.onUserExist(true);
                } else {
                    check.onUserExist(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    public void checkPassword(User user, CheckPasswordCallback callback){
        DatabaseReference userRef = dbRef.child("Users");
        Query queryByUsername = userRef.orderByChild("user_name").equalTo(user.getUser_name());
        Query queryByMail = userRef.orderByChild("mail_id").equalTo(user.getMail_id());

        ValueEventListener valueEventListener = new ValueEventListener() {
            boolean isSnapshotReceived = true; //Flag to find if the snapshot is received.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser.getPassword() != null){
                            callback.onPasswordExist(true);
                        } else {
                            callback.onPasswordExist(false);
                        }
                    }
                } else {
                    Log.d(TAG, "checkPassword: no Data Snapshot!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        };

        queryByUsername.addListenerForSingleValueEvent(valueEventListener);
        queryByMail.addListenerForSingleValueEvent(valueEventListener);
    }

    public void checkUserPass(User user, FirebaseCheckUserPassCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        Query queryByUsername = userRef.orderByChild("user_name").equalTo(user.getUser_name());
        Query queryByMail = userRef.orderByChild("mail_id").equalTo(user.getMail_id());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean passwordMatch = false;

                if (snapshot.exists()) {
                    Log.d(TAG, "Email: " + Objects.requireNonNull(snapshot.getValue()).toString());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        Log.d(TAG, "Current User: " + currentUser.getUser_name());
                        if (currentUser != null && currentUser.getPassword().equals(user.getPassword())) {
                            Log.d(TAG, "Entered Password: " + currentUser.getPassword() + "\nPassword: " + user.getPassword());
                            passwordMatch = true;
                            break;
                        }
                    }

                    Log.d(TAG, "Password Match: " + passwordMatch);
                    callback.onCheckUserPass(passwordMatch);
                } else {
                    Log.d(TAG, "DataSnapshot does not exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database Error: " + error.getMessage());
            }
        };

        queryByUsername.addListenerForSingleValueEvent(valueEventListener);
        queryByMail.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getUsername(String username, UsernameCallback usernameCallback) {
        DatabaseReference userRef = dbRef.child("Users");
        Query queryByUsername = userRef.orderByChild("user_name").equalTo(username);
        Query queryByEmail = userRef.orderByChild("mail_id").equalTo(username);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String USERNAME;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        USERNAME = user.getUser_name();
                        Log.d(TAG, "USERNAME: " + USERNAME);
                        usernameCallback.onUserNameReceived(USERNAME);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queryByUsername.addListenerForSingleValueEvent(valueEventListener);
        queryByEmail.addListenerForSingleValueEvent(valueEventListener);
    }

    public void getUserDetails(String username, UserDetailsCallback callback) {
        DatabaseReference userRef = dbRef.child("Users");
        Query query = userRef.orderByChild("user_name").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        callback.onUserDetailsReceived(user);
                        return; // Exit the loop after retrieving the first matching user
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCancelled(error);
            }
        });
    }

    public void updatePass(Context context, User user, UpdatePassCallback callback){
        DatabaseReference usersRef = dbRef.child("Users");
        Query queryByUsername = usersRef.orderByChild("user_name").equalTo(user.getUser_name());
        Query queryByMail = usersRef.orderByChild("mail_id").equalTo(user.getMail_id());
        Map<String, Object> updates = new HashMap<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String userID = dataSnapshot.getKey();
                        assert userID != null;
                        DatabaseReference userRef = usersRef.child(userID);
                        updates.put("password", user.getPassword());

                        userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Password Updated!", Toast.LENGTH_SHORT).show();
                                callback.onPasswordUpdated(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "update failed", Toast.LENGTH_SHORT).show();
                                callback.onPasswordUpdated(false);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "onDataChange: No Data Snapshot!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        };

        queryByUsername.addListenerForSingleValueEvent(valueEventListener);
        queryByMail.addListenerForSingleValueEvent(valueEventListener);
    }

    public void firebaseAuthentication(Context context, Activity activity, SharedPreferences sp, GoogleSignInAccount account, ProgressDialog progressDialog){
        Log.d(TAG, "firebaseAuthentication: is called..");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        sp = context.getSharedPreferences("LoginPref", 0);
        SharedPreferences.Editor editor = sp.edit();
        fAuth = FirebaseAuth.getInstance();

        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Task is Completed");
                            FirebaseUser user = fAuth.getCurrentUser();

                            progressDialog.dismiss();

                            if (user != null) {
                                checkEmail(user.getEmail(), new UserCheckListener() {
                                    @Override
                                    public void onUserExist(Boolean exist) {
                                        if (exist){
                                            //User exists
                                            Log.d(TAG, "onUserExist: User Exist..");
                                            Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show();
                                            SharedPreference.setDefaults("USERNAME", user.getDisplayName(), context);
                                            context.startActivity(new Intent(context, MainActivity.class));
                                            editor.putBoolean("LOGIN_STAT_SP", true);
                                            editor.apply();
                                            activity.finishAffinity();
                                        } else {
                                            Log.d("RoshanDB", "User Does Not Exists!");
                                            User newUser = new User();
                                            newUser.setUser_name(user.getDisplayName());
                                            newUser.setMail_id(user.getEmail());
                                            newUser.setPassword(null);
                                            insertUser(newUser);
                                            SharedPreference.setDefaults("USERNAME", user.getDisplayName(), context);
                                            context.startActivity(new Intent(context, MainActivity.class));
                                            editor.putBoolean("LOGIN_STAT_SP", true);
                                            editor.apply();
                                            activity.finishAffinity();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
