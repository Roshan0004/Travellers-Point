package com.example.travellerspoint.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travellerspoint.R;
import com.example.travellerspoint.adapter.PlanAdapter;
import com.example.travellerspoint.adapter.PostAdapter;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.model.Plan;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlanAdapter planAdapter;
    private List<Plan> planList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        planList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_plans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyDbHandler dbHandler = new MyDbHandler(getContext());

        planList = dbHandler.getAllPlans();

        planAdapter = new PlanAdapter(getContext(), planList);
        planAdapter.notifyItemInserted(0);
        recyclerView.setAdapter(planAdapter);

        return view;
    }
}