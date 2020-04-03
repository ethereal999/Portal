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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class internAdapter extends RecyclerView.Adapter<internAdapter.MyViewHolder> {
    OnItemClickListener mListener;
    private Context context;
    private ArrayList<intern> interns;

    internAdapter(Context c, ArrayList<intern> i)
    {
        context = c;
        interns = i;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new MyViewHolder(view,mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(interns.get(position).getCompany_name());
        holder.co_postion.setText(interns.get(position).getPosition());
        Glide.with(context).load(interns.get(position).getImageURL()).into(holder.ImageUrl);


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
        return interns.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,co_postion;
        ImageView ImageUrl;
        Button btn;
        MyViewHolder(View itemView,final OnItemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.company_name);
            co_postion = (TextView) itemView.findViewById(R.id.company_position);
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