// ChatListFragment.java
package com.example.workent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment implements ChatListAdapter.OnChatItemClickListener {

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private List<ChatRoom> chatRoomList;

    private static final String TAG = "ChatListFragment";

    private FirebaseUser currentUser;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        chatRoomList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatRoomList, getContext(), getFragmentManager());
        chatListAdapter.setOnChatItemClickListener(this); // Establece el listener en el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatListAdapter);

        loadChatRooms();

        return rootView;
    }

    // ...

    private void loadChatRooms() {
        DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference("messages");

        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRoomList.clear(); // Limpia la lista antes de agregar nuevos datos

                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    Message lastMessage = null;

                    for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        if (message != null) {
                            // Verifica si es el mensaje más reciente y si el receiver es igual al usuario actual
                            // o si el sender es igual al usuario actual
                            if ((message.getReceiver() != null && message.getReceiver().equals(currentUser.getEmail())) ||
                                    (message.getSender() != null && message.getSender().equals(currentUser.getEmail())) &&
                                            (lastMessage == null || message.getTimestamp().after(lastMessage.getTimestamp()))) {
                                lastMessage = message;
                            }
                        }
                    }

                    // Crea un objeto ChatRoom solo si el último mensaje cumple con las condiciones
                    if (lastMessage != null) {
                        String sender = lastMessage.getSender();
                        String receiver = lastMessage.getReceiver();
                        String text = lastMessage.getText();

                        String image = lastMessage.getImageUrl();


                        ChatRoom chatRoom = new ChatRoom(sender,receiver, text,image);
                        chatRoomList.add(chatRoom);
                    }
                }

                // Notifica al adaptador que los datos han cambiado
                chatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error en la consulta: " + error.getMessage());
            }
        });
    }

// ...


    // Implementa el nuevo método onChatItemClicked
    @Override
    public void onChatItemClicked(ChatRoom chatRoom) {
        // Abre ChatFragment al hacer clic en un elemento de la lista
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ChatFragment chatFragment = new ChatFragment();

        // Puedes pasar datos adicionales a ChatFragment utilizando Bundle si es necesario
        Bundle bundle2 = new Bundle();
        bundle2.putString("sender", chatRoom.getSender());
        bundle2.putString("text", chatRoom.getText());
        bundle2.putString("receiver", chatRoom.getReceiver());
        chatFragment.setArguments(bundle2);

        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, chatFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onChatItemConsolePrint(ChatRoom chatRoom) {
        // Imprime los datos del chatRoom en la consola
        Log.d(TAG, "Datos del elemento seleccionado: " +
                "Sender: " + chatRoom.getSender() +
                ", Text: " + chatRoom.getReceiver());
    }
}
