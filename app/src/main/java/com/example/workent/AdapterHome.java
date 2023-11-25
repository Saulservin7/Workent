package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workent.ui.theme.Trabajos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {
    private Context context;
    private ArrayList<Trabajos> list;
    private OnItemClickListener listener;
    private List<Trabajos> mList;

    public AdapterHome(Context context, ArrayList<Trabajos> list) {
        this.context = context;
        this.list = list;
        this.mList = list; // Inicializa mList con la lista pasada como argumento
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.services_home, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trabajos work = mList.get(position); // Usa mList en lugar de list
        holder.titulo.setText(work.getTittle());
        holder.descripcion.setText(work.getDescription());
        holder.precio.setText("$" + work.getPrice());

        // Cargar y mostrar la imagen utilizando Picasso
        Picasso.get()
                .load(work.getImageUri()) // work.getImageUri() debe ser la URL de la imagen
                .into(holder.imagen);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion, precio;
        ImageView imagen;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.title);
            descripcion = itemView.findViewById(R.id.description);
            precio = itemView.findViewById(R.id.price);
            imagen = itemView.findViewById(R.id.imageView); // Asegúrate de que el ID sea correcto
        }
    }

    public void filterList(List<Trabajos> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }
}
