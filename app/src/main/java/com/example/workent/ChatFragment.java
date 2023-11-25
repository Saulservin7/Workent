package com.example.workent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText editTextMessage;
    private Button buttonSend;
    private ImageButton buttonAttachImage;
    String uid, email, displayName, mailWorker, nameWorker, photoWorker;

    TextView workerName;
    ImageView workerProfile;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference messagesRef;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Bundle bundle = getArguments();
        nameWorker = bundle.getString("worker", "");
        photoWorker = bundle.getString("photoWorker", "");
        mailWorker = bundle.getString("correo", "");

        recyclerView = rootView.findViewById(R.id.recyclerView);
        editTextMessage = rootView.findViewById(R.id.editTextMessage);
        buttonSend = rootView.findViewById(R.id.buttonSendMessage);
        buttonAttachImage = rootView.findViewById(R.id.buttonAttachImage);
        workerName = rootView.findViewById(R.id.editTextUserName);
        workerProfile = rootView.findViewById(R.id.imageViewProfile);

        if (currentUser != null) {
            uid = currentUser.getUid();
            email = currentUser.getEmail();
            displayName = currentUser.getDisplayName();
        }

        if (mailWorker.equals("") && nameWorker.equals("") && photoWorker.equals("")) {
            mailWorker = bundle.getString("sender", "");
            ObtenerDatos(mailWorker);
        } else {
            Picasso.get().load(photoWorker).into(workerProfile);
            workerName.setText(nameWorker);
        }

        messagesRef = database.getReference("messages");

        if (email.equals(mailWorker)) {
            mailWorker = bundle.getString("receiver", "");
            ObtenerDatos(mailWorker);
        }

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, getContext(), email);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        String chatRoomId = generateChatRoomId(email, mailWorker);

        messagesRef.child(chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        String reversedChatRoomId = generateChatRoomId(mailWorker, email);
        if (!reversedChatRoomId.equals(chatRoomId)) {
            messagesRef.child(reversedChatRoomId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Message message = dataSnapshot.getValue(Message.class);
                        messageList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(editTextMessage.getText().toString().trim());
            }
        });

        buttonAttachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        return rootView;
    }

    private String generateChatRoomId(String sender, String receiver) {
        long senderCode = emailToNumber(sender);
        long receiverCode = emailToNumber(receiver);

        String chatRoomId1 = String.valueOf(senderCode) + receiverCode;
        String chatRoomId2 = receiverCode + String.valueOf(senderCode);

        if (chatRoomId1.compareTo(chatRoomId2) < 0) {
            return chatRoomId1;
        } else {
            return chatRoomId2;
        }
    }

    private long emailToNumber(String email) {
        long code = 0;
        for (char c : email.toCharArray()) {
            code = code * 10 + (int) c;
        }
        return code;
    }

    // En tu método sendMessage
    private void sendMessage(String messageText) {
        String currentUser = email;
        String chatRoomId = generateChatRoomId(email, mailWorker);

        // Modificar esta línea para usar el constructor adecuado
        Message newMessage = new Message(messageText, null, currentUser, mailWorker, new Date());

        DatabaseReference chatRoomRef = messagesRef.child(chatRoomId);
        String messageId = chatRoomRef.push().getKey();
        chatRoomRef.child(messageId).setValue(newMessage);
        editTextMessage.setText("");
    }

    // Y para enviar mensajes con imágenes
    private void sendMessageWithImage(Uri imageUri) {
        String currentUser = email;
        String chatRoomId = generateChatRoomId(email, mailWorker);

        // Obtener una referencia al almacenamiento de Firebase
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Generar un nombre único para la imagen
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";

        // Obtener una referencia al archivo en Firebase Storage
        StorageReference imageRef = storageRef.child("chat_images").child(imageName);

        // Subir la imagen al almacenamiento
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Imagen cargada exitosamente, obten la URL de descarga
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String downloadUrl = uri.toString();

                // Crea un nuevo mensaje con la URL de la imagen
                Message newMessage = new Message("", downloadUrl, currentUser, mailWorker, new Date());

                // Obtiene una referencia a la sala de chat actual
                DatabaseReference chatRoomRef = messagesRef.child(chatRoomId);

                // Generar un nuevo ID para el mensaje en la sala de chat
                String messageId = chatRoomRef.push().getKey();

                // Añadir el nuevo mensaje a la sala de chat
                chatRoomRef.child(messageId).setValue(newMessage);
            }).addOnFailureListener(e -> {
                // Manejar el error al obtener la URL de descarga
                e.printStackTrace();
                Log.e("FirebaseStorage", "Error al obtener la URL de descarga: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            // Manejar el error al cargar la imagen
            e.printStackTrace();
            Log.e("FirebaseStorage", "Error al cargar la imagen: " + e.getMessage());
        });
    }




    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            sendMessageWithImage(imageUri);
        }
    }



    public void ObtenerDatos(String mail) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(mail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                nameWorker = snapshot1.child("firstName").getValue().toString() + snapshot1.child("lastName").getValue().toString();
                                photoWorker = snapshot1.child("selfie").getValue().toString();
                                Picasso.get().load(photoWorker).into(workerProfile);
                                workerName.setText(nameWorker);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here if needed
                    }
                });
    }
}
