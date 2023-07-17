package com.example.travellerspoint.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travellerspoint.R;
import com.example.travellerspoint.data.MyDbHandler;
import com.example.travellerspoint.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> commentList;
    private Boolean LIKED;

    public CommentAdapter(Context context, List<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyDbHandler dbHandler = new MyDbHandler(context.getApplicationContext());
        Comment comment = commentList.get(position);

//        holder.username.setText(comment.getUser_name());
//        holder.description.setText(comment.getDescription());
//        holder.date.setText(comment.getDate());
//        holder.noOfLikes.setText(String.format("0 likes"));

        try {
            holder.username.setText(comment.getUser_name());
            holder.description.setText(comment.getDescription());
            holder.date.setText(comment.getDate());
//            holder.noOfLikes.setText(String.format("0 likes"));
        } catch (Exception e){
            Log.e("roshanText", "onBindViewHolder: " + e.toString());
        }

//        holder.like.setOnClickListener(new View.OnClickListener() {
//            Boolean LIKED = false;
//            @Override
//            public void onClick(View v) {
//                MyDbHandler dbHandler = new MyDbHandler(context.getApplicationContext());
//                lik_count = dbHandler.getNoOfLikes(postList.size() - holder.getPosition());
//                Animation popup = AnimationUtils.loadAnimation(v.getContext(), R.anim.popup);
//                List<Post> postList = dbHandler.getAllPosts();
//
//                if (!LIKED) {
//                    holder.like.setImageResource(R.drawable.ic_bold_like);
//                    holder.like.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
//                    holder.like.startAnimation(popup);
//
//                    ++lik_count;
//                    holder.noOfLikes.setText(String.valueOf(lik_count + " likes"));
//                    dbHandler.updateLikes(lik_count, postList.size() - holder.getPosition());
//
//                    LIKED = true;
//                } else if (LIKED){
//                    holder.like.setImageResource(R.drawable.ic_like);
//                    holder.like.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);
//
//                    --lik_count;
//                    holder.noOfLikes.setText(String.valueOf(lik_count + " likes"));
//                    dbHandler.updateLikes(lik_count, postList.size() - holder.getPosition());
//
//                    LIKED = false;
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView like;
        public TextView username, noOfLikes, description, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            like = itemView.findViewById(R.id.like);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            description = itemView.findViewById(R.id.comment_description);
            date = itemView.findViewById(R.id.date_time);
        }
    }
}
