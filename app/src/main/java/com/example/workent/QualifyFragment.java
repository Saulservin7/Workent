package com.example.workent;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class QualifyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RatingBar ratingBar;
    private ImageView photoWorker;
    private EditText comment;
    private Button qualify;

    TextView nameWorker;

    private String userEmail;
    private DatabaseReference calificacionesReference,TrabajadorReference;

    String Trabajador;

    public QualifyFragment() {
        // Required empty public constructor
    }

    public static QualifyFragment newInstance(String param1, String param2) {
        QualifyFragment fragment = new QualifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail();
        }

        calificacionesReference = FirebaseDatabase.getInstance().getReference("calificaciones");
        TrabajadorReference = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qualify, container, false);

        ratingBar = rootView.findViewById(R.id.ratingBar);
        photoWorker = rootView.findViewById(R.id.photoWorker);
        comment = rootView.findViewById(R.id.editTextComment);
        qualify = rootView.findViewById(R.id.buttonCalificar);
        nameWorker=rootView.findViewById(R.id.textView24);

        qualify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float calificacion = ratingBar.getRating();
                String comentario = comment.getText().toString();
                actualizarCalificacionYComentario(calificacion, comentario);
                Toast.makeText(getActivity(), "Gracias por tu calificación", Toast.LENGTH_SHORT).show();

                // Agrega un temporizador de 1 segundo antes de reemplazar el fragmento
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Cierra el fragmento
                        Fragment homeFragment = new HomeFragment();
                        replaceFragment(homeFragment);
                    }
                }, 1000); // 1000 milisegundos = 1 segundo
            }
        });


        cargarDatosAnteriores();




        return rootView;
    }

    private void cargarDatosAnteriores() {
        calificacionesReference.orderByChild("cliente").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot calificacionSnapshot : snapshot.getChildren()) {
                                // Aquí puedes cargar datos anteriores si es necesario
                                // Por ejemplo, mostrar la calificación anterior en la RatingBar
                                float calificacionAnterior = calificacionSnapshot.child("calificacion").getValue(Float.class);
                                ratingBar.setRating(calificacionAnterior);
                                // También puedes mostrar el comentario anterior en el EditText
                                String comentarioAnterior = calificacionSnapshot.child("comentario").getValue(String.class);
                                comment.setText(comentarioAnterior);
                                Trabajador=calificacionSnapshot.child("trabajador").getValue().toString();
                                System.out.println("Tacos"+Trabajador);
                                ObtenerTrabajo(Trabajador);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar errores de la consulta
                    }
                });
    }

    private void actualizarCalificacionYComentario(float calificacion, String comentario) {
        calificacionesReference.orderByChild("cliente").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot calificacionSnapshot : snapshot.getChildren()) {
                                calificacionSnapshot.getRef().child("calificacion").setValue(calificacion);
                                calificacionSnapshot.getRef().child("comentario").setValue(comentario);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar errores de la consulta
                    }
                });
    }

    private void cerrarFragmento() {
        // Obtén el FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // Comienza una transacción de fragmentos
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remueve este fragmento de la pila
        transaction.remove(this);

        // Realiza la transacción
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public void ObtenerTrabajo(String trabajador) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(trabajador)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                               nameWorker.setText(snapshot1.child("firstName").getValue().toString());
                               Picasso.get().load(snapshot1.child("selfie").getValue().toString()).into(photoWorker);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        // Maneja errores aquí si es necesario
                    }
                });
    }

}
