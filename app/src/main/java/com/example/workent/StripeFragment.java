package com.example.workent;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StripeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StripeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Handler handler = new Handler(Looper.getMainLooper());
    private final int INACTIVITY_TIMEOUT = 20000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WebView webView;

    String nombre,apellido,correo,clabe,celular;

    FirebaseUser user;

    FirebaseAuth auth;

    public StripeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StripeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StripeFragment newInstance(String param1, String param2) {
        StripeFragment fragment = new StripeFragment();
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
        // Inflate the layout for this fragment
         View rootView= inflater.inflate(R.layout.fragment_stripe, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

         webView=rootView.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Abre todas las URL dentro del WebView
                view.loadUrl(url);
                return true;
            }
        });

        // Inicia el temporizador de inactividad
        //startInactivityTimer();

        // Configura el evento onTouchListener para reiniciar el temporizador cuando se toca la pantalla
        webView.setOnTouchListener((v, event) -> {
            restartInactivityTimer();
            return false;
        });

        // Inicia el temporizador de inactividad
        //startInactivityTimer();

        // Configura el evento onTouchListener para reiniciar el temporizador cuando se toca la pantalla
        webView.setOnTouchListener((v, event) -> {
            restartInactivityTimer();
            return false;
        });


       ObtenerNombre(user.getEmail());







         return rootView;
    }

    private void llamarFuncionFirebase() {
       // String url = "https://us-central1-workent-1fbff.cloudfunctions.net/createExpressAccount?correo="+correo+"&nombre="+nombre+"&apellido="+apellido+"&celular="+celular+"&clabe="+clabe+"";
       String url="https://us-central1-workent-1fbff.cloudfunctions.net/createExpressAccount?correo="+user.getEmail()+"&nombre="+nombre+"&apellido="+apellido+"&celular="+celular+"&clabe="+clabe+"";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    String account = jsonResponse.getString("account");
                    String onboardingLink = jsonResponse.getString("onboardingLink");
                    actualizarIdStripeEnFirebase(account);

                    webView.loadUrl(onboardingLink);

                } catch (JSONException e) {
                    Log.e(TAG, "Error al procesar la respuesta JSON: " + e.getMessage());
                    Fragment HomeFragment = new HomeFragment();
                    replaceFragment(HomeFragment);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Maneja el error de la solicitud aquí
                Log.e(TAG, "Error en la solicitud: " + error.getMessage());
            }
        });

        queue.add(request);
    }

    private void startInactivityTimer() {
        handler.postDelayed(inactivityRunnable, INACTIVITY_TIMEOUT);
    }

    private void restartInactivityTimer() {
        handler.removeCallbacks(inactivityRunnable);
        startInactivityTimer();
    }

    private final Runnable inactivityRunnable = new Runnable() {
        @Override
        public void run() {
            // Este método se ejecutará después de INACTIVITY_TIMEOUT milisegundos de inactividad
            Toast.makeText(getContext(), "Cerrando WebView debido a inactividad", Toast.LENGTH_SHORT).show();
            // Cierra el WebView o realiza cualquier acción necesaria
            Fragment HomeFragment = new HomeFragment();
            replaceFragment(HomeFragment);

        }
    };

    public void ObtenerNombre(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                nombre = snapshot1.child("firstName").getValue().toString();
                                apellido=snapshot1.child("lastName").getValue().toString();
                                clabe=snapshot1.child("clabe").getValue().toString();
                                correo=snapshot1.child("email").getValue().toString();
                                celular=snapshot1.child("cellphone").getValue().toString();

                                llamarFuncionFirebase();
                                startInactivityTimer();




                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de errores
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

    private void actualizarIdStripeEnFirebase(String idStripe) {
        // Obtiene la referencia del nodo Usuarios para el usuario actual
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        // Crea una consulta para encontrar el nodo Usuarios donde el campo email coincide con el correo del usuario actual
        Query query = usuariosRef.orderByChild("email").equalTo(user.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Itera sobre los nodos coincidentes (debería haber solo uno)
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtiene la referencia del nodo Usuarios para el usuario actual
                    DatabaseReference usuarioRef = usuariosRef.child(snapshot.getKey());

                    // Crea un mapa para almacenar los datos que se van a actualizar
                    Map<String, Object> actualizacionDatos = new HashMap<>();
                    actualizacionDatos.put("idStripe", idStripe);

                    // Actualiza la base de datos en tiempo real con el nuevo campo idStripe
                    usuarioRef.updateChildren(actualizacionDatos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Campo idStripe actualizado exitosamente en la base de datos.");
                            } else {
                                Log.e(TAG, "Error al actualizar el campo idStripe en la base de datos: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de la consulta
                Log.e(TAG, "Error en la consulta: " + error.getMessage());
            }
        });
    }

}