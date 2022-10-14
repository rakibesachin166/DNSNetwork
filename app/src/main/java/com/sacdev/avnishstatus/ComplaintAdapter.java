package com.sacdev.avnishstatus;

import static com.sacdev.avnishstatus.R.color.danger;
import static com.sacdev.avnishstatus.R.color.orange;
import static com.sacdev.avnishstatus.R.color.success;
import static com.sacdev.avnishstatus.R.color.warning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sacdev.avnishstatus.model.Historymodel;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter< ComplaintAdapter.ViewHolder> {
      ArrayList<Historymodel> list;
     Context context;

    public ComplaintAdapter(ArrayList<Historymodel> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Historymodel model = list.get(position);

        holder.description.setText(model.getDescription());
        holder.type.setText(model.getType());
        holder.complaintdate.setText(model.getCreated_date());
        holder.complainttime.setText(model.getCreat_time());
        holder.status.setText(model.getStatus());


     switch (model.getStatus()){
         case "Pending":
             holder.status.setBackgroundColor(context.getColor(orange));
             break;
         case  "Rejected":
             holder.status.setBackgroundColor(context.getColor(danger));
             break;
         case  "Completed":
             holder.status.setBackgroundColor(context.getColor(success));
             break;

     }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView   status , type , complaintdate , complainttime , description;
        RelativeLayout cardLy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.complaintStatus_id);
            description = itemView.findViewById(R.id.complaintDescription_id);
            type = itemView.findViewById(R.id.complaintType_id);
            complaintdate = itemView.findViewById(R.id.complaintDate_id);
            complainttime = itemView.findViewById(R.id.complaintTime_id);
            cardLy = itemView.findViewById(R.id.cardLayout_id);
        }
    }
}

