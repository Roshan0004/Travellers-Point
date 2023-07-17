package com.example.travellerspoint.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.example.travellerspoint.model.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;
    private int lik_count = 0;
    private Boolean LIKED;
    private int cur_position = 0;

    public PostAdapter(Context context, List<Post> postList){
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyDbHandler dbHandler = new MyDbHandler(context.getApplicationContext());
        Post post = postList.get(position);
        Bitmap imageBitmap = doByteToBitmap(post.getImageUrl());

        String noOfLikes = String.valueOf(dbHandler.getNoOfLikes(postList.size() - holder.getPosition()));
//        holder.imageProfile.setImageResource(post.getProfileUrl());
        holder.username.setText(post.getUser_name());
        holder.post_image.setImageBitmap(imageBitmap);
        holder.noOfLikes.setText(post.getLikes());
        holder.author.setText(post.getAuthor());
        holder.description.setText(post.getDescription());
        holder.noOfComments.setText(String.format("See all %s comments", post.getCom_count()));
        holder.date.setText(post.getDate());
        holder.noOfLikes.setText(String.format("%s likes", noOfLikes));

        holder.like.setOnClickListener(new View.OnClickListener() {
            Boolean LIKED = false;
            @Override
            public void onClick(View v) {
                MyDbHandler dbHandler = new MyDbHandler(context.getApplicationContext());
                lik_count = dbHandler.getNoOfLikes(postList.size() - holder.getPosition());
                Animation popup = AnimationUtils.loadAnimation(v.getContext(), R.anim.popup);
                List<Post> postList = dbHandler.getAllPosts();

                if (!LIKED) {
                    holder.like.setImageResource(R.drawable.ic_bold_like);
                    holder.like.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                    holder.like.startAnimation(popup);

                    ++lik_count;
                    holder.noOfLikes.setText(String.valueOf(lik_count + " likes"));
                    dbHandler.updateLikes(lik_count, postList.size() - holder.getPosition());

                    LIKED = true;
                } else if (LIKED){
                    holder.like.setImageResource(R.drawable.ic_like);
                    holder.like.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN);

                    --lik_count;
                    holder.noOfLikes.setText(String.valueOf(lik_count + " likes"));
                    dbHandler.updateLikes(lik_count, postList.size() - holder.getPosition());

                    LIKED = false;
                }
            }
        });

        //Comment section
        int noOfComments = 0;
        CommentAdapter commentAdapter;
        List<Comment> commentList;
        MyDbHandler db = new MyDbHandler(context.getApplicationContext());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));

        //getting all posts from database and inserting into commentList
        commentList = db.getAllComments(postList.size() - holder.getPosition());
        holder.noOfComments.setText(new StringBuilder().append("See all ").append(commentList.size()).append(" comments").toString());
        commentAdapter = new CommentAdapter(context.getApplicationContext(), commentList);
        commentAdapter.notifyItemInserted(0);
        holder.recyclerView.setAdapter(commentAdapter);
        collapse(holder.recyclerView);
        try {
            holder.noOfComments.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(holder.noOfComments.getText() == "Show less") {
                        holder.noOfComments.setText(new StringBuilder().append("See all ").append(commentList.size()).append(" comments").toString());
                        holder.noOfComments.setTextColor(ContextCompat.getColor(context, R.color.gray_88));
                        collapse(holder.recyclerView);
                    } else {
                        holder.noOfComments.setText("Show less");
                        holder.noOfComments.setTextColor(ContextCompat.getColor(context, R.color.blue_200));
                        expand(holder.recyclerView);
                    }
//                commentList = dbHandler.getAllComments();
                }
            });

            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.commentField.getVisibility() == View.VISIBLE){
                        holder.commentField.setVisibility(View.GONE);
                        holder.noOfComments.setText(new StringBuilder().append("See all ").append(commentList.size()).append(" comments").toString());
                        holder.noOfComments.setTextColor(ContextCompat.getColor(context, R.color.gray_88));
                        collapse(holder.recyclerView);
                    } else {
                        holder.commentField.setVisibility(View.VISIBLE);
                        holder.noOfComments.setText("Show less");
                        holder.noOfComments.setTextColor(ContextCompat.getColor(context, R.color.blue_200));
                        expand(holder.recyclerView);
                    }
                }
            });

            holder.upload_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment_description = holder.enter_comment.getText().toString();
                    String UserName = SharedPreference.getDefaults("USERNAME", context.getApplicationContext());

                    Comment comment = new Comment();
                    comment.setPost_id(postList.size() - holder.getPosition());
                    comment.setUser_name(UserName);
                    comment.setDescription(comment_description);
                    comment.setDate(getDateTime());
                    Boolean insert = dbHandler.addCommment(comment);
                    if (insert) {
                        Toast.makeText(holder.comment.getContext(), "Comment posted!", Toast.LENGTH_SHORT).show();
                        Log.d("roshanDB", "onClickUploadCOmment: Comment added succesfully");
                    }

                    holder.commentField.setVisibility(View.GONE);
                }
            });
        } catch (Exception e){
            Log.e("roshanTest", "onBindViewHolder: "+e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageProfile, post_image, like, comment, more, upload_comment;
        public TextView username, noOfLikes, author, noOfComments, description, date;
        public RecyclerView recyclerView;
        public EditText enter_comment;
        public LinearLayout commentField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.profile_image);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            more = itemView.findViewById(R.id.more_options);
            upload_comment = itemView.findViewById(R.id.upload_comment);

            username = itemView.findViewById(R.id.username);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.upload_date);

            recyclerView = itemView.findViewById(R.id.comments);
            enter_comment = itemView.findViewById(R.id.input_comment);
            commentField = itemView.findViewById(R.id.comment_field);
        }
    }

    public Bitmap doByteToBitmap(byte[] blob){
        Bitmap retrievedBitmap = null;
        if (blob != null) {
            retrievedBitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
        }
        return retrievedBitmap;
    }

    private String getDateTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static  void expand(final View v){
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v. getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
