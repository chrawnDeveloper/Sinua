package com.conde.cc_contadordecalorias.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conde.cc_contadordecalorias.R;
import com.conde.cc_contadordecalorias.ui.newAlimento.Alimento;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private List<Alimento> listaAlimentos;

    public Adapter(List<Alimento> lista){
        this.listaAlimentos = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_historico, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Alimento alimento = listaAlimentos.get(position);

//        Date date = Calendar.getInstance().getTime(); //PEGAR O DIA DE HOJE
//        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        String dateTexto = dateTimeFormat.format(date);

        holder.caloriaAlimento.setText(alimento.getCaloriaAlimento());
        holder.nomeAlimento.setText(alimento.getNomeAlimento());
        holder.data.setText(alimento.getDataAlimento());
    }

    @Override
    public int getItemCount() {
        return this.listaAlimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView data;
        TextView nomeAlimento;
        TextView caloriaAlimento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.textData);
            nomeAlimento = itemView.findViewById(R.id.textAlimento);
            caloriaAlimento = itemView.findViewById(R.id.textCalorias);
        }
    }

}
