package com.example.potholeuserandroidapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        final Post post = postList.get(position);

        if(post.getLocation()!=null && post.getLocation().getLongitude()>0 && post.getLocation().getLatitude()>0){
            holder.postLocationButton.setEnabled(true);
            holder.postLocationButton.setVisibility(View.VISIBLE);
        }else{
            holder.postLocationButton.setEnabled(false);
            holder.postLocationButton.setVisibility(View.GONE);
        }



        holder.postLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post.getLocation()!=null && post.getLocation().getLongitude()>0 && post.getLocation().getLatitude()>0){
                    Uri gmmUri = Uri.parse("google.navigation:q="+post.getLocation().getLatitude()+","+post.getLocation().getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if(mapIntent.resolveActivity(context.getPackageManager())!=null){
                        context.startActivity(mapIntent);
                    }else{
                        Toast.makeText(context, "Suitable Apps Not Found", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(post.getLocation()!=null && post.getLocation().getDescription()!=null){
            holder.postLocationTextView.setText(post.getLocation().getDescription());
        }


        if(post.getStatus()!=null && post.getStatus().getStatus()!=null){

            if(post.getStatus().getStatus().equals("Resolved")){
                holder.postViewProofButton.setEnabled(true);
                holder.postViewProofButton.setVisibility(View.VISIBLE);

            }else{
                holder.postViewProofButton.setEnabled(false);
                holder.postViewProofButton.setVisibility(View.GONE);
            }


            holder.postStatusTextView.setText(post.getStatus().getStatus());

        }

        holder.postTimeStampTextView.setText(Long.toString(post.getTimestamp()));

        if(post.getText()!=null){
            holder.postTextView.setText(post.getText());
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
        Button postLocationButton;
        TextView postLocationTextView;

        TextView postStatusTextView;
        Button postViewProofButton;

        TextView postTimeStampTextView;

        Button postViewStatusesButton;

        PostsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


            postImageView = itemView.findViewById(R.id.postimageimageviewid);
            postTextView = itemView.findViewById(R.id.posttexttextviewid);
            postLocationButton = itemView.findViewById(R.id.postlocationbuttonid);
            postLocationTextView = itemView.findViewById(R.id.postlocationtextviewid);


            postStatusTextView = itemView.findViewById(R.id.poststatustextviewid);
            postViewProofButton = itemView.findViewById(R.id.postviewproofbuttonid);

            postTimeStampTextView = itemView.findViewById(R.id.posttimestamptextviewid);

            postViewStatusesButton = itemView.findViewById(R.id.postviewstatusesbuttonid);
        }
    }

}
