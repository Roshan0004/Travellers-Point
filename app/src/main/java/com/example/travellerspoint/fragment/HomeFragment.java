package com.example.travellerspoint.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.travellerspoint.LoginPage;
import com.example.travellerspoint.R;
import com.example.travellerspoint.adapter.PostAdapter;
import com.example.travellerspoint.data.SharedPreference;
import com.example.travellerspoint.model.Post;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private CardView community_card, plans_card, history_card, safety_card;
    private TextView welcomeText;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> postArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private String UserName;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        community_card = view.findViewById(R.id.community);
        plans_card = view.findViewById(R.id.plans);
        history_card = view.findViewById(R.id.history);
        safety_card = view.findViewById(R.id.safety);
        welcomeText = view.findViewById(R.id.welcome_text);

        UserName = SharedPreference.getDefaults("USERNAME", getContext());
        String welcome = "Welcome \n" + UserName + "!";
        welcomeText.setText(welcome);

        final Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

//        Post post = new Post();
//        post.setProfileUrl(R.id.profile_image);
//        post.setUser_name("Roshan_0004");
//        post.setImageUrl(R.drawable.bg);
//        post.setLikes("1000");
//        post.setAuthor("Aditya Singh");
//        post.setDescription("Wow! this is awesome!");
//        post.setCom_count("See all 300 comments");

        //Community fragment code start
        postArrayList = new ArrayList<>();

        try {
            //Community fragment code end
            community_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Animation animClick = AnimationUtils.loadAnimation(getContext(), R.anim.click);
                    community_card.startAnimation(animClick);
                    community_card.setClickable(false);
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Fragment fragment = (Fragment) new CommunityFragment();
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                                transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                                transaction.replace(R.id.frag_container, fragment);
                                transaction.addToBackStack("replacement");
                                transaction.commit();
                                community_card.setClickable(true);
                            }
                        }, 300);
                    } catch (Exception e) {
                        Log.e("roshanTest", "onClick: " + e.toString());
                    }
                }
            });
        } catch (Exception e){
            Log.e("roshanTest", "CommunityFragment" + e.getMessage());
        }

        plans_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation animClick = AnimationUtils.loadAnimation(getContext(), R.anim.click);
                plans_card.startAnimation(animClick);
                plans_card.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fragment fragment = new PlansFragment();
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                        transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                        transaction.replace(R.id.frag_container, fragment);
                        transaction.addToBackStack("replacement");
                        transaction.commit();
                        plans_card.setClickable(true);
                    }
                }, 300);
            }
        });

        history_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation animClick = AnimationUtils.loadAnimation(getContext(), R.anim.click);
                history_card.startAnimation(animClick);
                history_card.setClickable(false);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragment = (Fragment) new HistoryFragment();
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                            transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                            transaction.replace(R.id.frag_container, fragment);
                            transaction.addToBackStack("replacement");
                            transaction.commit();
                            history_card.setClickable(true);
                        }
                    }, 300);
                } catch (Exception e) {
                    Log.e("roshanTest", "onClick: " + e.toString());
                }
            }
        });

        safety_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation animClick = AnimationUtils.loadAnimation(getContext(), R.anim.click);
                safety_card.startAnimation(animClick);
                safety_card.setClickable(false);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragment = (Fragment) new SafetyFragment();
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                            transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                            transaction.replace(R.id.frag_container, fragment);
                            transaction.addToBackStack("replacement");
                            transaction.commit();
                            safety_card.setClickable(true);
                        }
                    }, 300);
                } catch (Exception e) {
                    Log.e("roshanTest", "onClick: " + e.toString());
                }
            }
        });

        return view;
    }

    public byte[] getBytesFromBitmap (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
