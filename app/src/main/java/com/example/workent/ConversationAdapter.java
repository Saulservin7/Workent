package com.example.workent;

// ConversationAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private List<Conversation> conversations;

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations != null ? conversations.size() : 0;
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        private TextView senderTextView;
        private TextView lastMessageTextView;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
        }

        public void bind(Conversation conversation) {
            senderTextView.setText(conversation.getSender());
            List<Message> messages = conversation.getMessages();
            if (!messages.isEmpty()) {
                lastMessageTextView.setText(messages.get(messages.size() - 1).getText());
            } else {
                lastMessageTextView.setText("No hay mensajes");
            }
        }
    }
}

