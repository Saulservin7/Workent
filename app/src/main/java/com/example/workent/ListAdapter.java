package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListWork> mData;
    private LayoutInflater minflater;
    private Context context;

    public ListAdapter(List<ListWork> itemList, Context context){ // Cambia el tipo de contexto aquí
        this.context = context; // Asigna el contexto adecuado
        this.minflater = LayoutInflater.from(context); // Crea el LayoutInflater con el contexto
        this.mData = itemList;
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = minflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListWork> items) { mData=items;}


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView tittle,description,price;

        ViewHolder(View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tittle = itemView.findViewById(R.id.title);
            description= itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);

        }

        void bindData (final ListWork item){
            imageView.setImageBitmap(item.getImage());
            tittle.setText(item.getTittle());
            description.setText(item.getDescription());
            price.setText(item.getPrice().toString());
        }


    }


}
