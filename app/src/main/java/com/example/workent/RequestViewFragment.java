package com.example.workent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workent.ui.theme.Calificaciones;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.stripe.android.Stripe;
import com.stripe.android.paymentsheet.PaymentSheetResult;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseDatabase db;
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret,cuentaStripe,nuevaCalificacionKey;

    PaymentSheet.CustomerConfiguration customerConfig;

    DatabaseReference databaseReference;

    private static final String ARG_TITULO = "titulo";
    private static final String ARG_UBICACION = "ubicacion";
    private static final String ARG_PRECIO = "precio";

    private static final String ARG_USUARIO = "user";

    private static final String ARG_IMAGE = "Url";

    private static final String ARG_IDTRABAJO="idTrabajo";


    private static final String ARG_FECHA="fecha";

    String nombre,token,photoWorker,idTrabajo,usuario,idTrabajador,titulo,tokenClient,estatus,id,trabajador,clienteSeleccionado,idWork,tokenWorker;


    TextView tittle,price,location,client,time,TextViewdate;

    ImageView photo,profileClient;

    Button accept,reject;
    String precio,photoWork;

    public RequestViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestViewFragment newInstance(String param1, String param2) {
        RequestViewFragment fragment = new RequestViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PaymentSheet paymentSheet;
        String paymentIntentClientSecret;
        PaymentSheet.CustomerConfiguration customerConfig;




        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()){
                            System.out.println("FALLIDO");
                            return;
                        }

                        token = task.getResult();


                    }
                });
    }

    public static RequestViewFragment newInstance(String titulo, String ubicacion, String precio, String imageUrl, String user,String idTrabajo, String fecha) {
        RequestViewFragment fragment = new RequestViewFragment();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("ubicacion", ubicacion);
        args.putString("precio", precio);
        args.putString("Url", imageUrl); // Pasa la URL de la imagen
        args.putString("user",user);
        args.putString("idTrabajo", idTrabajo);
        args.putString("user",user);
        args.putString("idTrabajo", idTrabajo);
        args.putString("horaInicio",fecha);

        //System.out.println(("id del trabajo es : "+idTrabajo));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_view, container, false);

        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference();
        tittle = rootView.findViewById(R.id.textView4);
        location = rootView.findViewById(R.id.textView7);
        price = rootView.findViewById(R.id.textView5);
        photo = rootView.findViewById(R.id.imageView4);
        accept = rootView.findViewById(R.id.btn_accept);
        reject = rootView.findViewById(R.id.btn_reject);
        client = rootView.findViewById(R.id.textView8);
        time = rootView.findViewById(R.id.time);
        TextViewdate = rootView.findViewById(R.id.date);
        profileClient = rootView.findViewById(R.id.profile_image);





            //ObtenerNombre(usuario);


            /*Picasso.get()
                    .load(imageUrl) // Utiliza la URL de la imagen
                    .into(photo);*/

        Bundle bundle = getArguments();
        if (bundle != null) {
             clienteSeleccionado = bundle.getString("clienteSeleccionado", "");
            String workTitle = bundle.getString("workTitle", "");
            String idUsuario = bundle.getString("idUsuario", "");
            String date = bundle.getString("date", "");
            idWork = bundle.getString("idWork","");
            id = bundle.getString("id","");
            estatus=bundle.getString("status","");
            trabajador=bundle.getString("trabajador","");
            ObtenerNombre(clienteSeleccionado);

            if (estatus.equals("Finalizada"))
            {
                reject.setVisibility(View.INVISIBLE);
                accept.setText("Finalizada");
                accept.setEnabled(false);

            }
            if ("Aceptada".equals(estatus)) {
                accept.setText("Finalizar");
                reject.setVisibility(View.INVISIBLE);

            }

            if (clienteSeleccionado.equals(user.getEmail()))
            {
                TextViewdate.setVisibility(View.VISIBLE);
                TextViewdate.setText(estatus);
                accept.setVisibility(View.INVISIBLE); // Deshabilitar el botón "Aceptar"
                reject.setVisibility(View.INVISIBLE);
                ObtenerNombre2(trabajador);

                if (estatus.equals("Aceptada")){
                    accept.setVisibility(View.VISIBLE);
                    accept.setText("Pagar");

                }

            }
            idTrabajo=idWork;
            time.setText(date);

            ObtenerTrabajo(idWork);
            ObtenerCuenta(trabajador);
           // System.out.println("Dios"+precio+cuentaStripe);
            //fetchPaymentApi(precio+"00",cuentaStripe);


        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (accept.getText().toString().equals("Finalizar"))
                {
                   showConfirmationDialog();
                   accept.setEnabled(false);

                    Calificaciones calificacion = new Calificaciones(0, clienteSeleccionado, user.getEmail(), idWork, "");

                    // Obtener una nueva clave única para la calificación en la base de datos
                    nuevaCalificacionKey = databaseReference.child("calificaciones").push().getKey();

                    // Establecer la calificación en la ubicación correspondiente en la base de datos
                    databaseReference.child("calificaciones").child(nuevaCalificacionKey).setValue(calificacion);
                } else if (accept.getText().toString().equals("Pagar")) {
                    if (paymentIntentClientSecret != null)
                        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, new PaymentSheet.Configuration("Codes Easy",
                                customerConfig));

                } else {
                    // Actualizar el estado en la base de datos a "Aceptada"
                    estatus="Aceptada";

                    //updateStatusInDatabase(estatus);
                    DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Solicitudes").child(id);
                    requestRef.child("estatus").setValue(estatus);
                    sendNotification(tokenClient,estatus);

                }

            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estatus="Rechazada";
                // Actualizar el estado en la base de datos a "Rechazada"
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Solicitudes").child(id);
                requestRef.child("estatus").setValue(estatus);
                sendNotification(tokenClient,estatus);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea una vista para contener el MapView
                View mapViewLayout = LayoutInflater.from(getContext()).inflate(R.layout.map_popup, null);
                MapView mapView = mapViewLayout.findViewById(R.id.mapView);

                // Configura el mapa en la ventana emergente
                mapView.onCreate(null);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        // Configura el mapa
                        googleMap.getUiSettings().setAllGesturesEnabled(false); // Desactiva los gestos del usuario si es necesario

                        // Obtén la dirección desde el TextView
                        String address = location.getText().toString();

                        // Convierte la dirección en coordenadas utilizando la API de Geocodificación
                        LatLng locationLatLng = getLatLngFromAddress(address);

                        // Muestra la ubicación en el mapa
                        if (locationLatLng != null) {
                            googleMap.addMarker(new MarkerOptions().position(locationLatLng).title("Ubicación"));

                            // Ajusta la cámara para hacer el mapa más grande y con un poco de zoom
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15.0f));
                        }
                    }
                });

                // Crea el AlertDialog con la vista del MapView
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(mapViewLayout)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cierra la ventana emergente
                                dialog.dismiss();
                            }
                        });

                // Muestra el AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
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
                                nombre = snapshot1.child("firstName").getValue().toString() + " " + snapshot1.child("lastName").getValue().toString();
                                client.setText(nombre);
                                location.setText(snapshot1.child("address").getValue().toString());
                                tokenClient=snapshot1.child("id").getValue().toString();
                                idTrabajador= snapshot1.child("id").getValue().toString();
                                photoWorker = snapshot1.child("selfie").getValue().toString();
                                Picasso.get().load(photoWorker).into(profileClient);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja errores aquí si es necesario
                    }
                });
    }

    public void ObtenerNombre2(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                nombre = snapshot1.child("firstName").getValue().toString() + " " + snapshot1.child("lastName").getValue().toString();
                                client.setText(nombre);
                                tokenWorker= snapshot1.child("id").getValue().toString();
                                photoWorker = snapshot1.child("selfie").getValue().toString();
                                Picasso.get().load(photoWorker).into(profileClient);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja errores aquí si es necesario
                    }
                });
    }

    public void ObtenerTrabajo(String idWork) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Trabajos").orderByChild("id").equalTo(idWork)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                titulo=snapshot1.child("tittle").getValue().toString();
                                tittle.setText(titulo);
                                 precio= snapshot1.child("price").getValue().toString();
                                 price.setText(precio);
                                photoWork = snapshot1.child("imageUri").getValue().toString();
                                Picasso.get().load(photoWork).into(photo);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja errores aquí si es necesario
                    }
                });
    }

    private void sendNotification(String id, String status){

        RequestQueue myrequest= Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();

        try {
            json.put("to",id);
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo","Tu solicitud fue "+status);
            notificacion.put("detalle",titulo);


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

    private void updateStatusInDatabase(String newStatus) {
        // Obtener la referencia a la solicitud específica en la base de datos
        DatabaseReference solicitudRef = FirebaseDatabase.getInstance().getReference("Solicitudes").child(idTrabajo);

        // Actualizar el campo 'status' con el nuevo estado
        solicitudRef.child("estatus").setValue(newStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // La actualización en la base de datos fue exitosa
                            // Puedes agregar aquí cualquier otra lógica que necesites
                            sendNotification(tokenClient,estatus); // Enviar notificación después de actualizar el estado
                        } else {
                            // La actualización en la base de datos falló
                            // Manejar el error aquí si es necesario
                        }
                    }
                });
    }



    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(address, 1);

            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void fetchPaymentApi(String price, String idStripe) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://us-central1-stripe-795af.cloudfunctions.net/makePayment?monto="+price+"00"+"&destino="+idStripe+"";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("R", response.toString());
                        try {
                            customerConfig = new PaymentSheet.CustomerConfiguration(
                                    response.getString("customer"),
                                    response.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = response.getString("paymentIntent");
                            PaymentConfiguration.init(getContext(), response.getString("publishableKey"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);
    }


    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.e("R:", "Pago cancelado");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e("App", "Error en el pago: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Puedes mostrar un mensaje de confirmación o realizar acciones adicionales
            Log.e("R:", "Pago completado");
            estatus = "Finalizada";
            DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Solicitudes").child(id);
            requestRef.child("estatus").setValue(estatus);
            sendNotification(tokenWorker, estatus);
            Calificaciones calificacion = new Calificaciones(0, clienteSeleccionado, user.getEmail(), idWork, "");

            // Obtener una nueva clave única para la calificación en la base de datos
            databaseReference = FirebaseDatabase.getInstance().getReference();
            nuevaCalificacionKey = databaseReference.child("calificaciones").push().getKey();

            // Establecer la calificación en la ubicación correspondiente en la base de datos
            databaseReference.child("calificaciones").child(nuevaCalificacionKey).setValue(calificacion);

        }
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmar");
        builder.setMessage("¿Ya recibió el pago en efectivo o con tarjeta?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realizar acciones para "Sí"
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("Solicitudes").child(id);
                requestRef.child("estatus").setValue("Finalizada");
                sendNotification(tokenClient, "Finalizada");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Realizar acciones para "No" (opcional)
            }
        });
        builder.show();
    }

    public void ObtenerCuenta(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                cuentaStripe = snapshot1.child("idStripe").getValue().toString();
                                fetchPaymentApi(precio, cuentaStripe);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejo de errores
                    }
                });
    }




}