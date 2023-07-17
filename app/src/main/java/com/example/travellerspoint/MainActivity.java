package com.example.travellerspoint;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travellerspoint.data.FirebaseHandler;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.data.SharedPreference;
import com.example.travellerspoint.fragment.CommunityFragment;
import com.example.travellerspoint.fragment.HomeFragment;
import com.example.travellerspoint.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
//    private SharedPreferences sp;
    private View profileLayout, header;
    private TextView Username, mail;
    private Map<String, String> userDetails;
    private AlertDialog dialog;
    private AlertDialog.Builder dBuilder;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ImageView profile, side_menu, more_options, search, add;
    private CardView community_card, plans_card, history_card, safety_card;

    //Firebase databse
    private DatabaseReference dbRef;
    private FirebaseHandler fbHandler;

    private static final String TAG = "firebase_log";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Handle the search query
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Handle the search query as the user types
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.navigation_bar);
        profile = (ImageView) findViewById(R.id.profile_image);
        side_menu = findViewById(R.id.side_menu);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        more_options = findViewById(R.id.more_options);
        profileLayout = navigationView.getHeaderView(0);
        Username = profileLayout.findViewById(R.id.username);
        mail = profileLayout.findViewById(R.id.user_mail);
        search = (ImageView) findViewById(R.id.search_icon);
        add = (ImageView) findViewById(R.id.add_post);
        header = navigationView.getHeaderView(0);
        fbHandler = new FirebaseHandler(getApplicationContext());

        String USERNAME;
        USERNAME = SharedPreference.getDefaults("USERNAME", MainActivity.this);

        fbHandler.getUserDetails(USERNAME, new FirebaseHandler.UserDetailsCallback() {
            @Override
            public void onUserDetailsReceived(User user) {
                String UName = user.getUser_name();
                String UMail = user.getMail_id();
                Log.d("roshanTest", "Username: " + UName + "\nMail: " + UMail);
                Username.setText(UName);
                mail.setText(UMail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation click = AnimationUtils.loadAnimation(MainActivity.this, R.anim.click);
                Menu menu = navigationView.getMenu();

                side_menu.startAnimation(click);
                drawer.openDrawer(Gravity.LEFT);
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frag_container);
                        switch (item.getItemId()) {
                            case R.id.home_menu:
                                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                                if (currentFragment != null && !(currentFragment instanceof HomeFragment)) {
                                    Fragment homeFragment = new HomeFragment();
                                    loadFragment(homeFragment);
                                }
                                break;
                            case R.id.community_menu:
                                Toast.makeText(MainActivity.this, "Community", Toast.LENGTH_SHORT).show();
                                if (currentFragment != null && !(currentFragment instanceof CommunityFragment)) {
                                    Fragment communityFragment = new CommunityFragment();
                                    loadFragment(communityFragment);
                                }
                                break;
                            case R.id.posts_menu:
                                Toast.makeText(MainActivity.this, "Posts Clicked", Toast.LENGTH_SHORT).show();
                                if (currentFragment != null && !(currentFragment instanceof CommunityFragment)) {
                                    Fragment communityFragment = new CommunityFragment();
                                    loadFragment(communityFragment);
                                }
                                break;
                            case R.id.website_menu:
                                Toast.makeText(MainActivity.this, "Under development, pleas provide feedbacks!", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.share_menu:
                                Toast.makeText(MainActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.feedback_menu:
                                Toast.makeText(MainActivity.this, "Feedbck Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.about_menu:
                                Toast.makeText(MainActivity.this, "About Us Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.logout_menu:
                                Toast.makeText(MainActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                SharedPreference.setDefaults("LOGIN_STAT_SP", false, MainActivity.this);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation click = AnimationUtils.loadAnimation(MainActivity.this, R.anim.click);
                more_options.startAnimation(click);
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menue_more_options, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_settings:
                                Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_profile:
                                Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_logout:
                                Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                SharedPreference.setDefaults("LOGIN_STAT_SP", false, getApplicationContext());
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.exit:
                                Toast.makeText(MainActivity.this, "Exit Clicked", Toast.LENGTH_SHORT).show();
                                showAlertDialogue(MainActivity.this);
//                                finish();
                                break;
                        }
                        return  true;
                    }
                });
                popup.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadPost.class);
                final Animation animClick = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click);
                add.setAnimation(animClick);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake));
            }
        });

        loadFragment(new HomeFragment());


    }

    @Override
    public void onBackPressed() {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.frag_container);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragment instanceof HomeFragment) {
            showAlertDialogue(MainActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.commit();
    }

    public void showAlertDialogue(Context context){
        dBuilder = new AlertDialog.Builder(context);
        dBuilder.setTitle("Confirm exit?");
        dBuilder.setMessage("Are you sure you want to exit Traveller's point?");
        dBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog = dBuilder.create();
        dialog.show();
    }
}