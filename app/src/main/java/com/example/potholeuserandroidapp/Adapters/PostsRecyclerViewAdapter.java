package com.example.potholeuserandroidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.potholeuserandroidapp.Models.Post;
import com.example.potholeuserandroidapp.R;

import java.util.List;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.PostsRecyclerViewHolder> {

    Context context;
    List<Post> postList;

    public PostsRecyclerViewAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_card,parent,false);
        return new PostsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsRecyclerViewHolder holder, int position) {

        Post post = postList.get(position);


        if(post.getText()!=null){
            holder.postTextView.setText(post.getText());
        }




        holder.postTimeStampTextView.setText(Long.toString(post.getTimestamp()));
        if(post.getStatus()!=null){
            holder.postStatusTextView.setText(post.getStatus().getStatus());
        }

        if(post.getImage()!=null){
            Glide.with(context).load(post.getImage()).into(holder.postImageView);
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostsRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView postImageView;
        TextView postTextView;
        TextView postStatusTextView;
        TextView postTimeStampTextView;

        PostsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


            postImageView = itemView.findViewById(R.id.postimageviewid);
            postTextView = itemView.findViewById(R.id.posttextviewid);
            postStatusTextView = itemView.findViewById(R.id.poststatustextviewid);
            postTimeStampTextView = itemView.findViewById(R.id.posttimestamptextviewid);

        }
    }

}
