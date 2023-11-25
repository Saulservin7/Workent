package com.example.workent.ui.theme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.workent.Login;
import com.example.workent.MainActivity;
import com.example.workent.Register;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.workent.databinding.ActivityVerifyAccountBinding;

import com.example.workent.R;

public class VerifyAccount extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView,goLogin;
    FirebaseUser user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.VerifyAccount);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        goLogin = findViewById(R.id.goLogin);
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            textView.setText(user.getEmail());
        }

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reload();
                //FirebaseAuth.getInstance().signOut();
                if (user.isEmailVerified())
                {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cuenta no Verificada,Revisa tu Correo", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}