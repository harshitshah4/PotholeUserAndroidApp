package com.example.potholeuserandroidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.potholeuserandroidapp.Models.Status;
import com.example.potholeuserandroidapp.R;

import java.util.List;

public class StatusListRecyclerAdapter extends RecyclerView.Adapter<StatusListRecyclerAdapter.StatusListRecyclerViewHolder> {

    Context context;
    List<Status> statusList;

    public StatusListRecyclerAdapter(Context context, List<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
    }



    @NonNull
    @Override
    public StatusListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_card,parent,false);
        return new StatusListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusListRecyclerViewHolder holder, int position) {
        Status status = statusList.get(position);

        if(status.getStatus()!=null){
            holder.statusTitleTextView.setText(status.getStatus());
        }

        if(status.getMessage()!=null){
            holder.statusMessageTextView.setText(status.getMessage());
        }

        if(status.getTimestamp()>0){
            holder.statusTimestampTextView.setText(String.valueOf(status.getTimestamp()));
        }

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class StatusListRecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView statusTitleTextView;
        TextView statusMessageTextView;
        TextView statusTimestampTextView;

        public StatusListRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            statusTitleTextView = itemView.findViewById(R.id.statustitletextviewid);
            statusMessageTextView = itemView.findViewById(R.id.statustitletextviewid);
            statusTimestampTextView = itemView.findViewById(R.id.statustimestamptextviewid);
        }
    }

}
