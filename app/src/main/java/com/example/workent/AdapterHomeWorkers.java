package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workent.R;
import com.example.workent.ui.theme.Trabajos;
import com.example.workent.ui.theme.Usuarios;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AdapterHomeWorkers extends RecyclerView.Adapter<AdapterHomeWorkers.MyViewHolder> {
    private Context context;
    private ArrayList<Usuarios> userList;
    private OnItemClickListener listener;

    private List<Usuarios> mList;

    public AdapterHomeWorkers(Context context, ArrayList<Usuarios> userList) {
        this.context = context;
        this.userList = userList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.list_workers, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuarios user = userList.get(position);
        holder.firstName.setText(user.getFirstName());


        // Cargar y mostrar la imagen de perfil utilizando Picasso
        Picasso.get()
                .load(user.getSelfie()) // user.getSelfie() debe ser la URL de la imagen de perfil
                .into(holder.selfie);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, email;
        ImageView selfie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.textView22);
            selfie = itemView.findViewById(R.id.imageView3); // Asegúrate de que el ID sea correcto
        }
    }

    public void filterList(List<Usuarios> filteredList) {
        userList = new ArrayList<>(filteredList); // Reemplaza la lista actual con la lista filtrada
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }


}
