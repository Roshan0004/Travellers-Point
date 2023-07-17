package com.example.travellerspoint.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.travellerspoint.MainActivity;
import com.example.travellerspoint.R;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.model.Plan;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlansFragment extends Fragment {

    private EditText planTitle, txtSource, txtDestination;
    private ImageView departure_date, expected_date;
    private EditText departure, expected;
    private Button saveButton;
    private String departure_dateTime, expected_dateTime;
    private String selectedTime = null;

    public PlansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        planTitle = view.findViewById(R.id.input_planTitle);
        txtSource = view.findViewById(R.id.input_source);
        txtDestination = view.findViewById(R.id.input_destination);
        saveButton = view.findViewById(R.id.btn_saveplans);
        departure = view.findViewById(R.id.input_departure);
        expected = view.findViewById(R.id.input_expected_time);

        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create the time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update the time EditText with the selected time
                                selectedTime = hourOfDay + ":" + minute;
                                departure.setText(departure.getText().append(" " + selectedTime));
                                // update your view
                            }
                        }, hour, minute, true);
                timePickerDialog.show();

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // do something with the selected date
                                departure.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        year, month, dayOfMonth);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();

                departure_dateTime = dayOfMonth + "/" + (month + 1) + "/" + year + "  " + selectedTime;
            }
        });

        expected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create the time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update the time EditText with the selected time
                                String selectedTime = hourOfDay + ":" + minute;
                                // update your view
                                expected.setText(expected.getText().append(" " + selectedTime));
                            }
                        }, hour, minute, true);
                timePickerDialog.show();

                //creating Date picker dialogue
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // do something with the selected date
                                expected.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        year, month, dayOfMonth);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();

                expected_dateTime = dayOfMonth + "/" + (month + 1) + "/" + year + "  " + selectedTime;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(getContext());

                if (!(planTitle.getText().toString().equals("") || txtSource.getText().toString().equals("")
                        || txtDestination.getText().toString().equals("") || departure.getText().toString().equals("")
                        || expected.getText().toString().equals(""))) {
                    Plan plan = new Plan();
                    plan.setPlan_name(planTitle.getText().toString());
                    plan.setSource_location(txtSource.getText().toString());
                    plan.setDestination_location(txtDestination.getText().toString());
                    plan.setPlan_date(getDateTime());
                    plan.setDeparture(departure.getText().toString());
                    plan.setExpected_time(expected.getText().toString());
                    Boolean insert = dbHandler.addPlan(plan);

                    if (insert) {
                        Toast.makeText(getActivity(), "Plan saved..", Toast.LENGTH_SHORT).show();
                        Log.d("roshanDB", "onClickSaveButton: Plan Added Successfully!");
                    }
                } else {
                    Toast.makeText(getActivity(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private String getDateTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }
}