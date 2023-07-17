package com.example.travellerspoint.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travellerspoint.R;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.data.SharedPreference;
import com.example.travellerspoint.model.Comment;
import com.example.travellerspoint.model.Plan;
import com.example.travellerspoint.model.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private Context context;
    private List<Plan> planList;

    public PlanAdapter(Context context, List<Plan> planList){
        this.context = context;
        this.planList = planList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyDbHandler dbHandler = new MyDbHandler(context.getApplicationContext());
        Plan plan = planList.get(position);
        holder.planTitle.setText(plan.getPlan_name());
        holder.source.setText(String.format("%s%s", holder.source.getText(), plan.getSource_location()));
        holder.destination.setText(String.format("%s%s", holder.destination.getText(), plan.getDestination_location()));
        holder.departure.setText(plan.getDeparture());
        holder.expected.setText(plan.getExpected_time());
        holder.date.setText(plan.getPlan_date());
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView planTitle, source, destination, departure, expected, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            planTitle = itemView.findViewById(R.id.planTitle);
            source = itemView.findViewById(R.id.txt_source);
            destination = itemView.findViewById(R.id.txt_destination);
            departure = itemView.findViewById(R.id.txt_departure);
            expected = itemView.findViewById(R.id.txt_expected);
            date = itemView.findViewById(R.id.txt_datetime);
        }
    }

    private String getDateTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
