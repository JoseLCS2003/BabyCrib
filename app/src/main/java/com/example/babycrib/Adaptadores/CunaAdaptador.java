package com.example.babycrib.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView name,desc;
    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.cunaName);
            desc=itemView.findViewById(R.id.cunaDesc);
        }
        public void setData(Cuna cuna){
            name.setText(cuna.getName());
            desc.setText(cuna.getDescription());
        }
    }
}
