package com.example.workent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.workent.databinding.FragmentProfileBinding;
import com.example.workent.ui.theme.Solicitudes;
import com.example.workent.ui.theme.Trabajos;
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
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,selectedDate,selectedTime;

    private static final String ARG_TITULO = "titulo";
    private static final String ARG_DESCRIPCION = "descripcion";
    private static final String ARG_PRECIO = "precio";

    private static final String ARG_USUARIO = "user";

    private static final String ARG_IMAGE = "Url";

    private static final String ARG_IDTRABAJO="idTrabajo";
    private static final String ARG_INICIO="horaInicio";

    private static final String ARG_FIN="horaFin";
    private int mYear, mMonth, mDay, mHour, mMinute;


    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseDatabase db;

    String idWorker;

    DatabaseReference databaseReference;

    String nombre,token,photoWorker,idTrabajo,usuario,idTrabajador,correo;


    TextView tittle,price,description,worker,time,date;

    ImageView photo,profileWorker;

    Button hire;

    ImageButton chat;



    public ServiceViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceViewFragment newInstance(String param1, String param2) {
        ServiceViewFragment fragment = new ServiceViewFragment();
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
    public static ServiceViewFragment newInstance(String titulo, String descripcion, String precio, String imageUrl, String user,String idTrabajo, String horaInicio,String horaFin) {
        ServiceViewFragment fragment = new ServiceViewFragment();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("descripcion", descripcion);
        args.putString("precio", precio);
        args.putString("Url", imageUrl); // Pasa la URL de la imagen
        args.putString("user",user);
        args.putString("idTrabajo", idTrabajo);
        args.putString("user",user);
        args.putString("idTrabajo", idTrabajo);
        args.putString("horaInicio",horaInicio);
        args.putString("horaFin", horaFin);
        //System.out.println(("id del trabajo es : "+idTrabajo));
        fragment.setArguments(args);
        return fragment;
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service_view, container, false);
        db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference();
        tittle = rootView.findViewById(R.id.textView4);
        description = rootView.findViewById(R.id.textView7);
        price = rootView.findViewById(R.id.textView5);
        photo = rootView.findViewById(R.id.imageView4);
        hire = rootView.findViewById(R.id.btn_post);
        worker = rootView.findViewById(R.id.textView8);
        time = rootView.findViewById(R.id.time);
        date = rootView.findViewById(R.id.date);
        chat = rootView.findViewById(R.id.chatButton);
        profileWorker = rootView.findViewById(R.id.profile_image);

        if (getArguments() != null) {
            String titulo = getArguments().getString(ARG_TITULO);
            String descripcion = getArguments().getString(ARG_DESCRIPCION);
            String precio = getArguments().getString(ARG_PRECIO);
            String imageUrl = getArguments().getString(ARG_IMAGE);
            String inicio= getArguments().getString(ARG_INICIO);
            String fin = getArguments().getString(ARG_FIN);
             usuario = getArguments().getString(ARG_USUARIO);

            // Recupera el valor de idTrabajo del Bundle de argumentos
            idTrabajo = getArguments().getString(ARG_IDTRABAJO);

            tittle.setText(titulo);
            description.setText(descripcion);
            price.setText(precio);
            time.setText(inicio+"-"+fin);


            //ObtenerNombre(usuario);
            ObtenerNombre(usuario, new NombreCallback() {
                @Override
                public void onNombreObtenido(String nombre, String idTrabajador) {
                    worker.setText(nombre);
                    //idTrabajo=idTrabajador;
                    idWorker=idTrabajador;
                    photoWorker=photoWorker;
                }


            });

            System.out.println("Tortilla" + nombre);

            Picasso.get()
                    .load(imageUrl) // Utiliza la URL de la imagen
                    .into(photo);
        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Bundle bundle = new Bundle();
                bundle.putString("photoWorker",photoWorker );
                bundle.putString("worker", nombre);
                bundle.putString("correo",correo);

                // Crear una instancia del nuevo fragmento y pasarle el Bundle
                Fragment chatFragment = new ChatFragment();
                chatFragment.setArguments(bundle);

                // Reemplazar el fragmento actual con el nuevo
                replaceFragment(chatFragment);



            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hire.getText().toString()=="Contratar")
                {
                    DatabaseReference solicitudRef = databaseReference.child("Solicitudes");
                    DatabaseReference nuevaSolicitudRef = solicitudRef.push(); // Utiliza push para obtener una clave única
                    String nuevaSolicitudRefKey = nuevaSolicitudRef.getKey(); // Obtén la clave única
                    Solicitudes nuevaSolicitud = new Solicitudes(nuevaSolicitudRefKey, user.getEmail(),usuario, idTrabajo,"Pendiente",selectedDate,selectedTime);
                    nuevaSolicitudRef.setValue(nuevaSolicitud);
                    System.out.println("taco"+idTrabajo);
                    sendNotification(idWorker);
                }
                else
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // Almacena la fecha seleccionada
                                    mYear = year;
                                    mMonth = monthOfYear + 1; // Añade 1 ya que enero es 0
                                    mDay = dayOfMonth;

                                    // Abre el TimePickerDialog para seleccionar la hora
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                      int minute) {
                                                    // Almacena la hora seleccionada
                                                    mHour = hourOfDay;
                                                    mMinute = minute;

                                                    // Formatea la fecha y hora seleccionadas
                                                     selectedDate = String.format("%04d-%02d-%02d", mYear, mMonth, mDay);
                                                     selectedTime = String.format("%02d:%02d", mHour, mMinute);

                                                    // Usa selectedDate y selectedTime según tus necesidades
                                                    // (puedes almacenarlos en Strings o realizar otras acciones)
                                                    date.setText("Fecha y Hora Seleccionada: "+selectedDate+"   "+selectedTime);
                                                    date.setVisibility(View.VISIBLE);
                                                    //Toast.makeText(getContext(), "Fecha seleccionada: " + selectedDate + "\nHora seleccionada: " + selectedTime, Toast.LENGTH_SHORT).show();
                                                }
                                            }, mHour, mMinute, false);

                                    timePickerDialog.show();
                                }
                            }, mYear, mMonth - 1, mDay); // Resta 1 ya que enero es 0

                    datePickerDialog.show();

                    hire.setText("Contratar");
                }
            }
        });



        return rootView;
    }

    public interface NombreCallback {
        void onNombreObtenido(String nombre, String idTrabajador);
    }



    public void ObtenerNombre(String email, NombreCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").orderByChild("email").equalTo(email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                nombre = snapshot1.child("firstName").getValue().toString() + " " + snapshot1.child("lastName").getValue().toString();
                                idTrabajador= snapshot1.child("id").getValue().toString();
                                photoWorker = snapshot1.child("selfie").getValue().toString();
                                correo = snapshot1.child("email").getValue().toString();
                                Picasso.get().load(photoWorker).into(profileWorker);
                                // Llama a la devolución de llamada con el nombre obtenido
                                callback.onNombreObtenido(nombre,idTrabajador);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Maneja errores aquí si es necesario
                    }
                });
    }





    private void sendNotification(String id){

        RequestQueue myrequest= Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();

        try {
            json.put("to",id);
            JSONObject notificacion=new JSONObject();
            notificacion.put("titulo","Tienes una solicitud nueva");
            notificacion.put("detalle",tittle.getText().toString());


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

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}