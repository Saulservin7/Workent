package com.example.workent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Context context;
    private String currentUserUid;

    public MessageAdapter(List<Message> messageList, Context context, String currentUserUid) {
        this.messageList = messageList;
        this.context = context;
        this.currentUserUid = currentUserUid;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSender().equals(currentUserUid)) {
            // Si el mensaje es del usuario actual, devuelve 0
            return 0;
        } else {
            // Si el mensaje es de otro usuario, devuelve 1
            return 1;
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            // Inflar el layout de mensajes enviados
            view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
        } else {
            // Inflar el layout de mensajes recibidos
            view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Verifica si el texto no está vacío antes de establecerlo en el TextView
        if (message.getText() != null && !message.getText().isEmpty()) {
            holder.textMessage.setText(message.getText());
        } else {
            // Si el texto está vacío, puedes ocultar el TextView o establecer un texto predeterminado
            holder.textMessage.setVisibility(View.GONE);
        }

        if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
            // Verifica si la URL de la imagen no está vacía antes de cargarla
            // Ejemplo con Picasso:
            Picasso.get().load(message.getImageUrl()).into(holder.imageView);

            // Ejemplo con Glide:
            // Glide.with(context).load(message.getImageUrl()).into(holder.imageView);

            // Muestra el ImageView ya que ahora tiene una imagen
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            // Si no hay URL de imagen en el mensaje, oculta el ImageView
            holder.imageView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        ImageView imageView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
