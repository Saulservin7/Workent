package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.workent.R;
import com.example.workent.ui.theme.Usuarios;
import com.squareup.picasso.Picasso;

public class ListWorkers extends ListAdapter<Usuarios, ListWorkers.ViewHolder> {

    private final Context context;
    private OnItemClickListener listener;

    public ListWorkers(Context context) {
        super(diffCallback);
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_workers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuarios user = getItem(position);
        holder.bindData(user);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView selfieImageView;
        private final TextView firstNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            selfieImageView = itemView.findViewById(R.id.imageView3);
            firstNameTextView = itemView.findViewById(R.id.textView22);
        }

        void bindData(final Usuarios user) {
            Picasso.get()
                    .load(user.getSelfie()) // user.getSelfie() debe ser la URL de la imagen de perfil
                    .into(selfieImageView);

            firstNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        }
    }

    private static final DiffUtil.ItemCallback<Usuarios> diffCallback = new DiffUtil.ItemCallback<Usuarios>() {
        @Override
        public boolean areItemsTheSame(@NonNull Usuarios oldItem, @NonNull Usuarios newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Usuarios oldItem, @NonNull Usuarios newItem) {
            return oldItem.equals(newItem);
        }
    };
}
