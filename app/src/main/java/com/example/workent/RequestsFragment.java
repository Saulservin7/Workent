package com.example.workent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.workent.ui.theme.Solicitudes;
import com.example.workent.ui.theme.Trabajos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment {

    List<ListRequest> elements;
    RecyclerView recyclerView;
    DatabaseReference database;
    AdapterRequests myAdapter;
    ArrayList<Solicitudes> list;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference reference;

    String status,workTitle,date,idWork,id;



    String clienteSeleccionado,idUsuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        View rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        // Inflate the layout for this fragment
        recyclerView = rootView.findViewById(R.id.recycler);
        database = FirebaseDatabase.getInstance().getReference("Solicitudes");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        myAdapter = new AdapterRequests(getContext(),list);
        recyclerView.setAdapter(myAdapter);
        // Inflate the layout for this fragment


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Solicitudes user = dataSnapshot.getValue(Solicitudes.class);
                    list.add(user);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myAdapter.setOnItemClickListener(new AdapterRequests.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Obtener la solicitud seleccionada
                Solicitudes selectedRequest = list.get(position);

                // Guardar el valor del cliente en la variable clienteSeleccionado
                clienteSeleccionado = selectedRequest.getCliente();
                workTitle = selectedRequest.getDate();
                date=selectedRequest.getDate();
                idWork=selectedRequest.getIdTrabajo();
                id=selectedRequest.getId();
                status=selectedRequest.getEstatus();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
                databaseReference.orderByChild("email").equalTo(clienteSeleccionado).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                 idUsuario = snapshot.child("id").getValue().toString(); // Obtiene el ID del usuario

                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Maneja errores aquí si es necesario
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("clienteSeleccionado", clienteSeleccionado);
                bundle.putString("workTitle", workTitle);
                bundle.putString("idUsuario", idUsuario);
                bundle.putString("date",date);
                bundle.putString("idWork",idWork);
                bundle.putString("id",id);
                bundle.putString("status",status);

                // Crear una instancia del nuevo fragmento y pasarle el Bundle
                Fragment requestViewFragment = new RequestViewFragment();
                requestViewFragment.setArguments(bundle);

                // Reemplazar el fragmento actual con el nuevo
                replaceFragment(requestViewFragment);
            }
        });


        return rootView;
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    private void sendNotification(String id){

        RequestQueue myrequest= Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();

        try {
            json.put("to",id);
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo","Tu solicitud fue "+status);
            notificacion.put("detalle",workTitle);


            json.put("data",notificacion);
            String URL="https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA60KFBbY:APA91bGKqF8O0cxNvEzZoROQirLFsLXG-yTPIsNqmdF5aW2LHBUQhvd-tV7kfDqdVFXz67nBk5btbh4U40GN4tyV8gtKo6IgwGHsZDey886lXeDpo_iV8cL_EaTnMZVzxIREH-qaXjDj");
                    return  header;

                }
            };
            myrequest.add(request);



        }
        catch (JSONException e)
        {

        }



    }
}