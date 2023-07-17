package com.example.travellerspoint.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.travellerspoint.R;
import com.example.travellerspoint.services.GestureService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SafetyFragment extends Fragment {

    private Switch safety_switch;
    private FloatingActionButton fab;
    private static int REQUEST_CALL_PERMISSION = 0004;

    public SafetyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety, container, false);

        safety_switch = view.findViewById(R.id.safety_switch);
        fab = view.findViewById(R.id.fab);

        safety_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                try {
                    if (isChecked) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                    Log.e("roshanTest", "onCheckedChanged: " + e.getMessage());
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission if it is not granted
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PERMISSION);
                } else {
                    String phoneNumber = "8591163308";
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(callIntent);
                }
            }
        });

        return  view;
    }
}