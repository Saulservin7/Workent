package com.example.workent;

import static android.content.ContentValues.TAG;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workent.ui.theme.Trabajos;
import com.example.workent.ui.theme.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    List<ListWork> elements;

    RecyclerView recyclerView, recyclerView2;
    DatabaseReference database,chats;
    AdapterHome adapterHome;

    AdapterHomeWorkers adapterHome2;
    ArrayList<Trabajos> list;
    ArrayList<Usuarios> list2;
    String name;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference reference;
    TextView nombre;

    String titulo, descripcion, precio,selfieUrl,clabe,terms;

    ImageView profilePhoto;

    WebView webView;

    EditText searchBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        nombre = rootView.findViewById(R.id.textView16);
        profilePhoto= rootView.findViewById(R.id.imageView5);

        ObtenerNombre(user.getEmail());


        recyclerView = rootView.findViewById(R.id.recycler_home);
        recyclerView2 = rootView.findViewById(R.id.recycler_home_workers);
        database = FirebaseDatabase.getInstance().getReference("Trabajos");
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        chats = FirebaseDatabase.getInstance().getReference("messages");
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager1);
        searchBar=rootView.findViewById(R.id.editTextText);
        verificarCalificacionesUsuarioActual();
        list2 = new ArrayList<>();
        adapterHome2 = new AdapterHomeWorkers(getContext(),list2);
        recyclerView2.setAdapter(adapterHome2);
        list = new ArrayList<>();
        adapterHome = new AdapterHome(getContext(), list);
        recyclerView.setAdapter(adapterHome);







        adapterHome.setOnItemClickListener(new AdapterHome.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Trabajos selectedItem = list.get(position);
                String toastMessage = "Elemento seleccionado: " + selectedItem.getImageUri();
                Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();

                // Obtén los datos del elemento seleccionado
                String titulo = selectedItem.getTittle();
                String descripcion = selectedItem.getDescription();
                String precio = selectedItem.getPrice();
                String imageURL = selectedItem.getImageUri();
                String horaInicio=selectedItem.getStartTime();
                String horaFin=selectedItem.getEndTime();

                String user = selectedItem.getUser();

                String idTrabajo = selectedItem.getId();
                // Crea un nuevo ServiceViewFragment con los datos
                Fragment serviceViewFragment = ServiceViewFragment.newInstance(titulo, descripcion, precio,imageURL,user,idTrabajo,horaInicio,horaFin);

                replaceFragment(serviceViewFragment);
            }
        });

        // Supongamos que user.getEmail() devuelve el correo electrónico del usuario actual





        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Trabajos users = dataSnapshot.getValue(Trabajos.class);

                    if (users.getUser().equals(user.getEmail()))
                    {

                    }
                    else {
                        list.add(users);
                    }
                }
                adapterHome.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Usuarios workers = dataSnapshot.getValue(Usuarios.class);
                    if (workers.getEmail().equals(user.getEmail()))
                    {
                    }
                    else if (workers.getUserType().equals("Trabajador")){
                        list2.add(workers);
                    }
                }
                adapterHome2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Llamar al método para filtrar la lista cuando cambie el texto
                filterList(s.toString());
                filterListWorkers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return rootView;
    }

    private void filterList(String searchText) {
        List<Trabajos> filteredList = new ArrayList<>();

        // Recorrer la lista original y agregar elementos cuyos títulos contengan el texto de búsqueda
        for (Trabajos item : list) {
            if (item.getTittle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Actualizar el adaptador del RecyclerView con la lista filtrada
        adapterHome.filterList(filteredList);

    }

    private void filterListWorkers(String searchText) {
        List<Usuarios> filteredList = new ArrayList<>();

        for (Usuarios item : list2) {
            if (item.getFirstName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterHome2.filterList(filteredList);
    }



    public void ObtenerNombre(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                name = snapshot1.child("firstName").getValue().toString();
                                selfieUrl=snapshot1.child("selfie").getValue().toString();
                                String userType = snapshot1.child("userType").getValue().toString();
                                if (userType.equals("Trabajador"))
                                {
                                    clabe=snapshot1.child("clabe").getValue().toString();
                                    terms=snapshot1.child("terms").getValue().toString();
                                    if (!clabe.isEmpty()&&terms.equals("")) {
                                            mostrarMensajeConOpciones();
                                    }
                                }
                                nombre.setText("Hola, " + name);
                                Picasso.get().load(selfieUrl).into(profilePhoto);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de errores
                    }
                });
    }

    private void verificarCalificacionesUsuarioActual() {
        final String userEmail = user.getEmail();

        DatabaseReference calificacionesReference = FirebaseDatabase.getInstance().getReference("calificaciones");

        calificacionesReference.orderByChild("cliente").equalTo(userEmail)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot calificacionSnapshot : snapshot.getChildren()) {
                            // Obtén los datos de cada calificación
                            String cliente = calificacionSnapshot.child("cliente").getValue(String.class);
                            int calificacion = calificacionSnapshot.child("calificacion").getValue(Integer.class);
                            String idTrabajo = calificacionSnapshot.child("idTrabajo").getValue(String.class);
                            String comentario = calificacionSnapshot.child("comentario").getValue(String.class);


                            // Verifica si la calificación es 0 y el comentario es "hola"
                            if (calificacion == 0 && "".equals(comentario)) {
                                // Abre el fragmento QualifyFragment con los datos
                                openQualifyFragment(cliente);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar errores de la consulta
                    }
                });
    }

    private void mostrarMensajeConOpciones() {
        Context context = getContext();
        if (context == null) {
            Log.e("HomeFragment", "El contexto es nulo en mostrarMensajeConOpciones()");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Workent utiliza la plataforma de Stripe");
        builder.setMessage("Para poder recibir los pagos debe llenar un pequeño formulario.");

        builder.setPositiveButton("Aceptar y Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Abre el fragmento StripeFragment
                Fragment stripeFragment = new StripeFragment();
                replaceFragment(stripeFragment);
            }
        });

        builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Puedes mostrar un mensaje o realizar alguna acción si el usuario rechaza
                actualizarCampoTermsEnDatabase();
            }
        });

        builder.show();
    }




    private void openQualifyFragment(String cliente) {
        // Crea una nueva instancia de QualifyFragment con los datos
        QualifyFragment qualifyFragment = QualifyFragment.newInstance(cliente,cliente);

        // Reemplaza el fragmento actual con QualifyFragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, qualifyFragment);
        fragmentTransaction.addToBackStack(null); // Agrega la transacción al back stack para que el usuario pueda retroceder
        fragmentTransaction.commit();
    }


    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void actualizarCampoTermsEnDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Obtener una referencia a la base de datos
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            // Actualizar el campo 'terms' en el nodo 'Usuarios'
            databaseReference.child("Usuarios").orderByChild("email").equalTo(userEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Obtener la clave del nodo
                                String usuarioKey = snapshot.getKey();

                                // Actualizar el campo 'terms' a 'nulo'
                                databaseReference.child("Usuarios").child(usuarioKey).child("terms").setValue("nulo");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("HomeFragment", "Error al actualizar el campo 'terms' en Realtime Database", databaseError.toException());
                        }
                    });
        }
    }
}
