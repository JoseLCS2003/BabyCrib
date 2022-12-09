package com.example.babycrib.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babycrib.Modelos.Cuna;
import com.example.babycrib.R;

import java.util.List;

public class CunaAdaptador extends RecyclerView.Adapter<CunaAdaptador.viewHolder> {
    List<Cuna> lp;
    public CunaAdaptador(List<Cuna> lp){this.lp=lp;}
    @NonNull
    @Override
    public CunaAdaptador.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new CunaAdaptador.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CunaAdaptador.viewHolder holder, int position) {
        holder.setData(lp.get(position));
    }

    @Override
    public int getItemCount() {
        return lp.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name,desc;
        Button moreDetails;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            moreDetails=itemView.findViewById(R.id.moreDetails);
            name=itemView.findViewById(R.id.cunaName);
            desc=itemView.findViewById(R.id.cunaDesc);
        }
        public void setData(Cuna cuna){
            name.setText(cuna.getName());
            desc.setText(cuna.getDescription());
            moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String datos[]={name.getText().toString(),desc.getText().toString()};
                    Bundle parmetros = new Bundle();
                    parmetros.putStringArray("datos", datos);

                    Intent i = new Intent(itemView.getContext(), com.example.babycrib.Cuna.class);
                    i.putExtras(parmetros);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
