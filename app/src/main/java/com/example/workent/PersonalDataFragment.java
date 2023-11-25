package com.example.workent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workent.ui.theme.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalDataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button btnData;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseUser user;
    FirebaseDatabase db;
    FirebaseAuth auth;
    EditText nombre,apellido,celular;

    RadioGroup tipoUsuario;

    Button btnEdit,btnSave;

    TextView fname,sname,mail,phone,userType,prueba;

    public PersonalDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalDataFragment newInstance(String param1, String param2) {
        PersonalDataFragment fragment = new PersonalDataFragment();
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
        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_data, container, false);
        fname = rootView.findViewById(R.id.textView9);
        sname = rootView.findViewById(R.id.textView10);
        mail = rootView.findViewById(R.id.textView12);
        phone = rootView.findViewById(R.id.textView11);
        userType = rootView.findViewById(R.id.textView13);
        nombre = rootView.findViewById(R.id.editTextNombre);
        apellido = rootView.findViewById(R.id.editTextApellido);
        celular = rootView.findViewById(R.id.editTextCelular);
        tipoUsuario = rootView.findViewById(R.id.radioGroup);
        btnEdit = rootView.findViewById(R.id.btn_edit);
        btnSave = rootView.findViewById(R.id.btn_save);
        prueba = rootView.findViewById(R.id.textView14);
        ObtenerDatos(user.getEmail());
        btnEdit.setText("Editar");


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la sesión actual
                btnSave.setText("Guardar");
                fname.setVisibility(View.INVISIBLE);
                sname.setVisibility(View.INVISIBLE);
                phone.setVisibility(View.INVISIBLE);
                userType.setVisibility(View.INVISIBLE);
                nombre.setVisibility(View.VISIBLE);
                apellido.setVisibility(View.VISIBLE);
                celular.setVisibility(View.VISIBLE);
                tipoUsuario.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                nombre.setText(fname.getText());
                apellido.setText(sname.getText());
                celular.setText(phone.getText());


                // Puedes realizar alguna acción adicional después de cerrar sesión si es necesario

                // Por ejemplo, redirigir al usuario a la pantalla de inicio de sesión
                // Intent intent = new Intent(getActivity(), LoginActivity.class);
                // startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userSelected = tipoUsuario.getCheckedRadioButtonId();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
               DatabaseReference dbref= db.getReference(Usuarios.class.getSimpleName());


                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String usertype2="";
                                for (DataSnapshot x : snapshot.getChildren()){
                                    if ( x.child("email").getValue().toString().equalsIgnoreCase(user.getEmail())){
                                        x.getRef().child("firstName").setValue(nombre.getText().toString());
                                        x.getRef().child("lastName").setValue(apellido.getText().toString());
                                        x.getRef().child("cellphone").setValue(celular.getText().toString());

                                        if (userSelected!=-1)
                                        {
                                            RadioButton selectedUser = rootView.findViewById(userSelected);
                                            usertype2 = selectedUser.getText().toString();
                                            x.getRef().child("userType").setValue(usertype2);
                                        }






                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                btnEdit.setVisibility(View.VISIBLE);
                btnSave.setVisibility((View.INVISIBLE));
                ObtenerDatos(user.getEmail());

            }
        });

        return rootView;
    }

    public void ObtenerDatos (String email){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                String Nombre = snapshot1.child("firstName").getValue().toString();
                                String Apellido = snapshot1.child("lastName").getValue().toString();
                                String Correo = snapshot1.child("email").getValue().toString();
                                String Celular = snapshot1.child("cellphone").getValue().toString();
                                String TipoUsuario = snapshot1.child("userType").getValue().toString();
                                fname.setText(Nombre);
                                sname.setText(Apellido);
                                phone.setText(Celular);
                                mail.setText(Correo);
                                userType.setText(TipoUsuario);
                                nombre.setVisibility(View.INVISIBLE);
                                apellido.setVisibility(View.INVISIBLE);
                                celular.setVisibility(View.INVISIBLE);
                                tipoUsuario.setVisibility(View.INVISIBLE);
                                fname.setVisibility(View.VISIBLE);
                                sname.setVisibility(View.VISIBLE);
                                phone.setVisibility(View.VISIBLE);
                                userType.setVisibility(View.VISIBLE);

                                //name.setText(nombre);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }


}