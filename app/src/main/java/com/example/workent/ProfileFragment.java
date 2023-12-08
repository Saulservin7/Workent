package com.example.workent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workent.databinding.FragmentProfileBinding;
import com.example.workent.ui.theme.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FragmentProfileBinding binding;
    Button buttonLogout, btnPersonalData,btnWorks,btnRequests,btnChats;
    TextView mail,name,userType;
    FirebaseUser user;
    FirebaseDatabase db;

    String nombre,usuarioo,selfieurl;

    String email;

    ImageView profilePhoto;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        buttonLogout = rootView.findViewById(R.id.CloseSession);
        btnPersonalData = rootView.findViewById(R.id.PersonalData);
        btnWorks = rootView.findViewById(R.id.Works);
        btnChats = rootView.findViewById(R.id.Chats);
        btnRequests=rootView.findViewById(R.id.Requests);
        mail = rootView.findViewById(R.id.textView2);
        name = rootView.findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        mail.setText(user.getEmail());
        email = user.getEmail();
        userType = rootView.findViewById(R.id.tipoUsuario);
        profilePhoto = rootView.findViewById(R.id.imageView3);
        ObtenerNombre(email);



        btnPersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new PersonalDataFragment());

            }
        });

        btnChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ChatListFragment());
            }
        });





        // Agrega un OnClickListener al botón
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la sesión actual
                auth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();

                // Puedes realizar alguna acción adicional después de cerrar sesión si es necesario

                // Por ejemplo, redirigir al usuario a la pantalla de inicio de sesión
                // Intent intent = new Intent(getActivity(), LoginActivity.class);
                // startActivity(intent);
            }
        });

        btnWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la sesión actual
                replaceFragment(new WorkFragment());

                // Puedes realizar alguna acción adicional después de cerrar sesión si es necesario

                // Por ejemplo, redirigir al usuario a la pantalla de inicio de sesión
                // Intent intent = new Intent(getActivity(), LoginActivity.class);
                // startActivity(intent);
            }
        });
        btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la sesión actual
                replaceFragment(new RequestsFragment());

                // Puedes realizar alguna acción adicional después de cerrar sesión si es necesario

                // Por ejemplo, redirigir al usuario a la pantalla de inicio de sesión
                // Intent intent = new Intent(getActivity(), LoginActivity.class);
                // startActivity(intent);
            }
        });



        return rootView;




    }
    public void ObtenerNombre(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                nombre = snapshot1.child("firstName").getValue().toString();
                                usuarioo = snapshot1.child("userType").getValue().toString();
                                selfieurl= snapshot1.child("selfie").getValue().toString();
                                name.setText(nombre);

                                Picasso.get().load(selfieurl).into(profilePhoto);

                                // Verificar si el tipo de usuario es "Trabajador" y establecer la visibilidad del botón en consecuencia
                                if ("Trabajador".equals(usuarioo)) {
                                    btnWorks.setVisibility(View.VISIBLE);
                                } else {
                                    btnWorks.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}
