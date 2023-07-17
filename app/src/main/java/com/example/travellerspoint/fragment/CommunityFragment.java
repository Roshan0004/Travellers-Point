package com.example.travellerspoint.fragment;

import android.database.CursorWindow;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.example.travellerspoint.R;
import com.example.travellerspoint.adapter.PostAdapter;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.model.Post;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ArrayAdapter<String> arrayAdapter;
    MyDbHandler db = new MyDbHandler(getContext());

    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        postList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            MyDbHandler dbHandler = new MyDbHandler(getContext());
            try {
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
            } catch (Exception e) {
                e.printStackTrace();
            }
            postList = dbHandler.getAllPosts();

            postAdapter = new PostAdapter(getContext(), postList);
            postAdapter.notifyItemInserted(0);
            recyclerView.setAdapter(postAdapter);
        } catch (Exception e) {
            Log.e("roshanTest", "onCreateView: " + e.getMessage());
        }

        return  view;
    }

}