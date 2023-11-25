// ChatListAdapter.java
package com.example.workent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRoomList;
    private Context context;
    private FragmentManager fragmentManager;

    private OnChatItemClickListener onChatItemClickListener;

    public interface OnChatItemClickListener {
        void onChatItemClicked(ChatRoom chatRoom);
        void onChatItemConsolePrint(ChatRoom chatRoom);
    }

    public void setOnChatItemClickListener(OnChatItemClickListener listener) {
        this.onChatItemClickListener = listener;
    }

    public ChatListAdapter(List<ChatRoom> chatRoomList, Context context, FragmentManager fragmentManager) {
        this.chatRoomList = chatRoomList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_item, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {

        private TextView chatRoomIdTextView;
        private ImageView chatRoomIdImageView;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomIdTextView = itemView.findViewById(R.id.chatRoomIdTextView);
            chatRoomIdImageView = itemView.findViewById(R.id.chatRoomImageView);



            // Configura el OnClickListener para abrir un nuevo Fragment al hacer clic en un elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onChatItemClickListener != null) {
                        onChatItemClickListener.onChatItemClicked(chatRoomList.get(position));
                        onChatItemClickListener.onChatItemConsolePrint(chatRoomList.get(position));
                    }
                }
            });
        }

        public void bind(ChatRoom chatRoom) {
            chatRoomIdTextView.setText(chatRoom.getSender() + " - " + chatRoom.getText());
            Picasso.get().load(chatRoom.getImageUrl()).into(chatRoomIdImageView);
        }
    }
}
