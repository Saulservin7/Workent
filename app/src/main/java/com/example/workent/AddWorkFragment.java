package com.example.workent;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.workent.ui.theme.Trabajos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class AddWorkFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private ProgressBar progressBar;

    private ImageButton photo;
    static final int REQUEST_GALLERY = 2;
    private Uri selectedImageUri; // Para almacenar la URI de la imagen seleccionada

    private Button post;
    private TextInputEditText Etitle, Edescription, Eprice, Stime, Etime;

    public AddWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        View rootView = inflater.inflate(R.layout.fragment_add_work, container, false);

        Etitle = rootView.findViewById(R.id.title);
        Edescription = rootView.findViewById(R.id.description);
        Eprice = rootView.findViewById(R.id.price);
        Stime = rootView.findViewById(R.id.starttime);
        Etime = rootView.findViewById(R.id.endtime);
        post = rootView.findViewById(R.id.btn_post);
        progressBar = rootView.findViewById(R.id.progressBar);

        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference();

        photo = rootView.findViewById(R.id.photoWOrk);



        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String titulo = Etitle.getText().toString();
                String descripcion = Edescription.getText().toString();
                String precio = Eprice.getText().toString();
                String horaInicio = Stime.getText().toString();
                String horaFin = Etime.getText().toString();

                if (!titulo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty() && selectedImageUri != null) {
                    // Antes de subir la imagen, asegúrate de tener una referencia a Firebase Storage
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    // Supongamos que "selectedImageUri" contiene la URI de la imagen seleccionada
                    String imageFileName = "images/" + System.currentTimeMillis() + ".jpg"; // Puedes ajustar el nombre del archivo como desees

                    StorageReference imageRef = storageRef.child(imageFileName);

                    // Sube la imagen a Firebase Storage
                    imageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // La imagen se ha cargado correctamente, ahora puedes obtener la URL de la imagen
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Aquí puedes guardar imageUrl en Firebase Realtime Database junto con otros datos

                                    DatabaseReference trabajoRef = databaseReference.child("Trabajos");
                                    DatabaseReference nuevoTrabajoRef = trabajoRef.push(); // Utiliza push para obtener una clave única
                                    String nuevoTrabajoKey = nuevoTrabajoRef.getKey(); // Obtén la clave única
                                    Trabajos nuevoTrabajo = new Trabajos(titulo, descripcion, precio, user.getEmail(), imageUrl, horaInicio, horaFin, nuevoTrabajoKey);

                                    nuevoTrabajoRef.setValue(nuevoTrabajo);

                                    Etitle.setText("");
                                    Edescription.setText("");
                                    Eprice.setText("");
                                    progressBar.setVisibility(View.GONE);
                                });
                            })
                            .addOnFailureListener(exception -> {
                                // Maneja cualquier error que ocurra durante la carga de la imagen
                                progressBar.setVisibility(View.GONE);
                            });
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        Stime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePickerDialog(v);
            }
        });
        Etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePickerDialog(v);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                photo.setImageURI(selectedImageUri);
            }
        }
    }

    public void mostrarTimePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        int horaActual = c.get(Calendar.HOUR_OF_DAY);
        int minutoActual = c.get(Calendar.MINUTE);

        int viewId = view.getId();
        final TextInputEditText editText;

        if (viewId == R.id.starttime) {
            editText = (TextInputEditText) view.findViewById(R.id.starttime);
        } else if (viewId == R.id.endtime) {
            editText = (TextInputEditText) view.findViewById(R.id.endtime);
        } else {
            return; // No debería llegar aquí si se manejan solo los clics de los TextInputEditText esperados
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int horaSeleccionada, int minutoSeleccionado) {
                        String horaFormateada = String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado);
                        editText.setText(horaFormateada);
                    }
                }, horaActual, minutoActual, false);

        timePickerDialog.show();
    }



}
