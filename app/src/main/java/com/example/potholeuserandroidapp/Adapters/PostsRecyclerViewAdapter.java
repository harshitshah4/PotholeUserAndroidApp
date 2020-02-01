package com.example.potholeuserandroidapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.potholeuserandroidapp.Activities.HomeActivity;
import com.example.potholeuserandroidapp.Fragments.StatusListDialogFragment;
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
            holder.postLocationImageButton.setEnabled(true);
            holder.postLocationImageButton.setVisibility(View.VISIBLE);
        }else{
            holder.postLocationImageButton.setEnabled(false);
            holder.postLocationImageButton.setVisibility(View.GONE);
        }


//    holder.postViewStatusesButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            FragmentManager fragmentManager = ((HomeActivity) context).getSupportFragmentManager();
//
//            StatusListDialogFragment caseListDialogFragment = new StatusListDialogFragment(post.getPid());
//
//            caseListDialogFragment.show(fragmentManager,"statuslistdialog");
//        }
//    });

        holder.postLocationImageButton.setOnClickListener(new View.OnClickListener() {
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

        if(post.getLocation().getDescription()!=null){
            holder.postLocationTextView.setText(post.getLocation().getDescription());
        }


        if(post.getStatus()!=null) {

            if (post.getStatus().getStatus() != null) {
                holder.postStatusTextView.setVisibility(View.VISIBLE);
                holder.postStatusTextView.setText(post.getStatus().getStatus());

                switch (post.getStatus().getStatus()){
                    case "Processing":
                        holder.postStatusTextView.setTextColor(context.getResources().getColor(R.color.processing));
                        holder.postStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_radio_button_unchecked_black_24dp,0,0,0);
                        break;
                    case "Resolved":
                        holder.postStatusTextView.setTextColor(context.getResources().getColor(R.color.resolved));
                        holder.postStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp,0,0,0);
                        break;
                    case "Assigned":
                        holder.postStatusTextView.setTextColor(context.getResources().getColor(R.color.assigned));
                        holder.postStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_outline_black_24dp,0,0,0);
                        break;
                    case "Rejected":
                        holder.postStatusTextView.setTextColor(context.getResources().getColor(R.color.rejected));
                        holder.postStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_outline_black_24dp,0,0,0);
                        break;

                }

            } else {
                holder.postStatusTextView.setVisibility(View.GONE);
            }


        }


//        if(post.getStatus()!=null){
//
//            if(post.getStatus().getStatus()!=null){
//                holder.postStatusTextView.setVisibility(View.VISIBLE);
//                if(post.getStatus().getStatus().equals("Resolved")){
//                    holder.postViewProofButton.setEnabled(true);
//                    holder.postViewProofButton.setVisibility(View.VISIBLE);
//
//                }else{
//                    holder.postViewProofButton.setEnabled(false);
//                    holder.postViewProofButton.setVisibility(View.GONE);
//                }
//                holder.postStatusTextView.setText(post.getStatus().getStatus());
//            }else{
//                holder.postStatusTextView.setVisibility(View.GONE);
//            }
//
//            if(post.getStatus().getMessage()!=null){
//                holder.postStatusMessageTextView.setVisibility(View.VISIBLE);
//                holder.postStatusMessageTextView.setText(post.getStatus().getMessage());
//            }else{
//                holder.postStatusMessageTextView.setVisibility(View.GONE);
//            }
//
//            if(post.getStatus().getTimestamp()>0){
//                holder.postStatusTimeStampTextView.setVisibility(View.VISIBLE);
//                holder.postStatusTimeStampTextView.setText(String.valueOf(post.getStatus().getTimestamp()));
//            }else{
//                holder.postStatusTimeStampTextView.setVisibility(View.GONE);
//            }


//        }


//        holder.postTimeStampTextView.setText(Long.toString(post.getTimestamp()));

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
        ImageButton postLocationImageButton;
        TextView postLocationTextView;

        TextView postStatusTextView;
//        TextView postStatusMessageTextView;
//        TextView postStatusTimeStampTextView;
//
//        Button postViewProofButton;
//
//        TextView postTimeStampTextView;
//
//        Button postViewStatusesButton;

        PostsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);


            postImageView = itemView.findViewById(R.id.postimageimageviewid);
            postTextView = itemView.findViewById(R.id.posttexttextviewid);
            postLocationImageButton = itemView.findViewById(R.id.postlocationimagebuttonid);
            postLocationTextView = itemView.findViewById(R.id.postlocationtextviewid);


            postStatusTextView = itemView.findViewById(R.id.poststatustextviewid);
//            postStatusMessageTextView = itemView.findViewById(R.id.poststatusmessagetextviewid);
//            postStatusTimeStampTextView = itemView.findViewById(R.id.poststatustimestamptextviewid);



//            postViewProofButton = itemView.findViewById(R.id.postviewproofbuttonid);
//
//            postTimeStampTextView = itemView.findViewById(R.id.posttimestamptextviewid);
//
//            postViewStatusesButton = itemView.findViewById(R.id.postviewstatusesbuttonid);
        }
    }

}
