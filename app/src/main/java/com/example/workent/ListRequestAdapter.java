package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListRequestAdapter extends RecyclerView.Adapter<ListRequestAdapter.ViewHolder> {

    private List<ListRequest> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListRequestAdapter(Context context, List<ListRequest> itemList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientName, workerName, jobId;

        ViewHolder(View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.title);
            workerName = itemView.findViewById(R.id.description);
            jobId = itemView.findViewById(R.id.price);
        }

        void bindData(final ListRequest item) {
            clientName.setText("Cliente: " + item.getCliente());
            workerName.setText("Trabajador: " + item.getTrabajador());
            jobId.setText("ID del Trabajo: " + item.getIdTrabajo());
        }
    }
}
