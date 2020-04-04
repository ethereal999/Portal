package com.example.portal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class application_comadapter extends RecyclerView.Adapter<application_comadapter.MyViewHolder> {

    private Context context;
    private ArrayList<application> applications;

    application_comadapter(Context c, ArrayList<application> i)
    {
        context = c;
        applications = i;
    }
    @NonNull
    @Override
    public application_comadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appl_card,parent,false);
        return new application_comadapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull application_comadapter.MyViewHolder holder, int position) {
        holder.s_name.setText(applications.get(position).getStudentname());
        holder.s_email.setText(applications.get(position).getStudentemail());
        holder.off_pos.setText(applications.get(position).getAppliedposition());
        holder.off_type.setText(applications.get(position).getType());



    }



    @Override
    public int getItemCount() {
        return applications.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView s_name,s_email,off_type,off_pos;

        MyViewHolder(View itemView) {
            super(itemView);
            s_name = (TextView) itemView.findViewById(R.id.stu_name);
            s_email = (TextView) itemView.findViewById(R.id.stu_email);
            off_type = (TextView) itemView.findViewById(R.id.off_type);
            off_pos = (TextView) itemView.findViewById(R.id.off_position);


        }
    }
}
