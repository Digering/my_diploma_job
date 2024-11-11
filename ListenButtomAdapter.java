package com.example.english_4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListenButtomAdapter extends RecyclerView.Adapter<ListenButtomAdapter.ViewHolder> {

    public LayoutInflater inflater;
    public List<ListenButtom> listenButtoms;
    public boolean open_CheckBox;

    ListenButtomAdapter(Context context, List<ListenButtom> listenButtoms, boolean open_CheckBox){
        this.listenButtoms = listenButtoms;
        this.inflater = LayoutInflater.from(context);
        this.open_CheckBox = open_CheckBox;
    }

    @Override//возвращает обьект ViewHolder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.listbuttom_one, parent, false);

        return new ViewHolder(view);
    }

    @Override//наполняет инициализированное в ViewHolder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListenButtom listenButtom = listenButtoms.get(position);
        holder.name.setText(listenButtom.getName());
        holder.col_word.setText(listenButtom.getCol_word() + " слов");

        if(listenButtom.getProgres()== null){
            holder.progres.setVisibility(View.GONE);
        }else{
            holder.progres.setText(listenButtom.getProgres() + " %");
        }

        // Если адаптер используется в Category, скрыть CheckBox
        if (open_CheckBox) {
            holder.progres.setVisibility(View.GONE);
            holder.vidor.setChecked(listenButtom.getVidor());

            holder.vidor.setTag(listenButtom.getName());


        } else {
            holder.vidor.setVisibility(View.GONE);
        }

    }

    @Override//возвращаем колличество item
    public int getItemCount() {
//        Log.println(Log.DEBUG, "SQL", String.valueOf(listenButtoms.size()));
        return listenButtoms.size();// 60
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        Button name;
        TextView col_word, progres;
        CheckBox vidor;

        public  ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            col_word = itemView.findViewById(R.id.col_word);
            progres = itemView.findViewById(R.id.progres);
            vidor = itemView.findViewById(R.id.vidor);

        }
    }
}
