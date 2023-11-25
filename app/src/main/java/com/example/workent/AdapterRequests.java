package com.example.workent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workent.ui.theme.Solicitudes;

import java.util.List;

public class AdapterRequests extends RecyclerView.Adapter<AdapterRequests.ViewHolder> {

    private List<Solicitudes> mData;
    private LayoutInflater mInflater;
    private Context context;
    private OnItemClickListener mListener;

    public AdapterRequests(Context context, List<Solicitudes> itemList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_requests, parent, false);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void bindData(final Solicitudes item) {
            clientName.setText("Cliente:"+item.getCliente());
            workerName.setText("Estatus:"+item.getEstatus());
            jobId.setText("ID del trabajo:"+item.getId());
        }
    }
}
