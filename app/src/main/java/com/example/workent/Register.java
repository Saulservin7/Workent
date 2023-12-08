package com.example.workent;

import static android.content.ContentValues.TAG;
import static com.example.workent.AddWorkFragment.REQUEST_GALLERY;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.workent.ui.theme.Usuarios;
import com.example.workent.ui.theme.VerifyAccount;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;





public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword,editTextCellphone,editTextFirstName,editTextLastName,editTextClabeText;
    TextInputLayout editTextClabe;

    TextView textClabe;
    Button buttonReg;

    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;


    AutoCompleteTextView location;
    Button buttonLog;

    String nombre;

    Boolean checkFN=false,checkLN=false,checkE=false,checkP=false;

    ImageButton selfie;

    private Uri selectedImageUri;

    static final int REQUEST_GALLERY = 2;



    RadioGroup userType;

    FirebaseAuth mAuth;
    FirebaseAuth auth;

    String locationAddress;
    ProgressBar progressBar;
    TextView textView;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void fieldsVerified() {
        if (checkE && checkFN && checkLN && checkP) {
            buttonReg.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextCellphone = findViewById(R.id.cellphone);
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextClabe = findViewById(R.id.clabe);
        userType = findViewById(R.id.userTypeRadioGroup);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        buttonLog = findViewById(R.id.btn_login);
        selfie = findViewById(R.id.imageButton);
        textClabe = findViewById(R.id.textView23);
        editTextClabeText=findViewById(R.id.clabeText);
        db = FirebaseDatabase.getInstance();
        buttonReg.setEnabled(false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Button loginNowButton = findViewById(R.id.btn_login);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Ingresa tu Direccion");
         autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));



        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String locationName = place.getName();
                locationAddress = place.getAddress();
                // Puedes almacenar estos datos en la base de datos o realizar otras operaciones según tus necesidades
                //Toast.makeText(Register.this, "Ubicación seleccionada: " + locationName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "Error al seleccionar la ubicación: " + status);
            }
        });

        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loginNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tu código para manejar el evento onClick del botón
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = editable.toString().trim();

                if (email.matches("^[A-Za-z0-9+_.-]+@gmail.com$") || email.matches("^[A-Za-z0-9+_.-]+@outlook.com$")) {
                    // Cambia el color a negro si es válido
                    checkE=true;
                    fieldsVerified();
                } else {
                    editTextEmail.setError("Ingrese un correo Electronico Valido"); // Mantiene el color en rojo si no es válido
                    buttonReg.setEnabled(false);
                    checkE=false;
                }

            }
        });

        editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                 nombre= editable.toString().trim();

                if (nombre.matches("^[A-Za-z]+$")) {
                    // Cambia el color a negro si es válido
                    checkFN=true;
                    fieldsVerified();
                } else {
                    editTextFirstName.setError("Ingrese su nombre correctamente"); // Mantiene el color en rojo si no es válido
                    buttonReg.setEnabled(false);
                    checkLN=false;


                }



            }
        });

        editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String apellido= editable.toString().trim();

                if (apellido.matches("^[A-Za-z]+$")) {
                    // Cambia el color a negro si es válido
                    checkLN=true;
                    fieldsVerified();
                } else {
                    buttonReg.setEnabled(false);
                    checkLN=false;
                    editTextLastName.setError("Ingrese su nombre correctamente"); // Mantiene el color en rojo si no es válido
                }





            }
        });

        editTextCellphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String celular= editable.toString().trim();
                if (celular.matches("^\\d{10}$")) {
                    // Cambia el color a negro si es válido
                    checkP=true;
                    fieldsVerified();
                } else {
                    buttonReg.setEnabled(false);
                    checkP=false;
                    editTextCellphone.setError("Ingrese un celular valido");
                }

            }
        });

        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioWorker) {
                    // Muestra el EditText clabe cuando se selecciona Trabajador
                    editTextClabe.setVisibility(View.VISIBLE);
                    textClabe.setVisibility(View.VISIBLE);
                } else {
                    // Oculta el EditText clabe para otros tipos de usuario
                    editTextClabe.setVisibility(View.GONE);
                    textClabe.setVisibility(View.GONE);
                }
            }
        });


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, cellphone,firstname,lastname,usertype;
                int userSelected = userType.getCheckedRadioButtonId();
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                cellphone = String.valueOf(editTextCellphone.getText());
                firstname = String.valueOf(editTextFirstName.getText());
                lastname = String.valueOf(editTextLastName.getText());

                if (userSelected!=-1)
                {
                    RadioButton selectedUser = findViewById(userSelected);
                    usertype = selectedUser.getText().toString();



                } else {
                    usertype = "";
                }


                if (TextUtils.isEmpty(cellphone)) {
                    Toast.makeText(Register.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Drawable drawable = selfie.getDrawable(); // Obtiene la imagen del ImageButton

                if (drawable == null) {
                    Toast.makeText(Register.this, "El ImageButton está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Registro exitoso
                                    if (selectedImageUri != null) {

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
                                                        FirebaseMessaging.getInstance().getToken()
                                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<String> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                                            return;
                                                                        }

                                                                        // Get new FCM registration token
                                                                        String token = task.getResult();
                                                                        Usuarios nuevoUsuario = new Usuarios(email, password, cellphone, firstname, lastname, usertype, imageUrl,token,locationAddress,editTextClabeText.getText().toString(),"");

                                                                        DatabaseReference usuariosRef = databaseReference.child("Usuarios");
                                                                        usuariosRef.push().setValue(nuevoUsuario);

                                                                        // Log and toast

                                                                    }
                                                                });


                                                        // Resto del código
                                                        // ...

                                                    });
                                                })
                                                .addOnFailureListener(exception -> {
                                                    // Maneja cualquier error que ocurra durante la carga de la imagen
                                                    progressBar.setVisibility(View.GONE);
                                                });
                                    } else {
                                        Toast.makeText(Register.this, "No se ha seleccionado una imagen", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    FirebaseUser user = auth.getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    // Correo de verificación enviado exitosamente
                                                    // Pide al usuario que verifique su correo antes de continuar
                                                    Toast.makeText(Register.this, "Revise su correo electronico",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), VerifyAccount.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Error al enviar el correo de verificación
                                                    // Maneja el error apropiadamente
                                                }
                                            });

                                } else {
                                    // Registro fallido, obtén el error y muestra el mensaje de error al usuario
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Register.this, "Registro Fallido: " + errorMessage,
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.getData();
                selfie.setImageURI(selectedImageUri);
            }
        }
    }

}

