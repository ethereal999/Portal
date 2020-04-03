package com.example.portal;

import android.content.Context;
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

import java.util.ArrayList;

public class jobAdapter extends RecyclerView.Adapter<jobAdapter.MyViewHolder> {
    OnItemClickListener mListener;
    private Context context;
        private ArrayList<job> jobs;

    jobAdapter(Context c, ArrayList<job> i)
        {
            context = c;
            jobs = i;
        }
        @NonNull
        @Override
        public jobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
            return new jobAdapter.MyViewHolder(view,mListener);

        }

        @Override
        public void onBindViewHolder(@NonNull jobAdapter.MyViewHolder holder, int position) {
            holder.name.setText(jobs.get(position).getCompany_name());
            holder.co_position.setText(jobs.get(position).getOff_position());
            Glide.with(context).load(jobs.get(position).getImageURL()).into(holder.ImageUrl);


        }
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
       mListener = listener;
    }


    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, co_position;
        ImageView ImageUrl;
        Button btn;
        MyViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.company_name);
            co_position = (TextView) itemView.findViewById(R.id.company_position);
            ImageUrl = (ImageView) itemView.findViewById(R.id.ImageUrl);
            btn = (Button) itemView.findViewById(R.id.checkDetails);


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}