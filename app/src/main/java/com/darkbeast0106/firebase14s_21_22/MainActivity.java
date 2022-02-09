package com.darkbeast0106.firebase14s_21_22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        btnRegister.setOnClickListener(view -> {
            Intent toRegister = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(toRegister);
            finish();
        });
        btnLogin.setOnClickListener(view -> {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); //API 24-től

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean error = false;
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                    != NetworkInfo.State.CONNECTED
                    && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                    != NetworkInfo.State.CONNECTED) {
                error = true;
                Toast.makeText(this, "Nincs internetkapcsolat", Toast.LENGTH_SHORT).show();
            }

            if (email.isEmpty()) {
                etEmail.setError(getString(R.string.requiredError));
                error = true;
            }
            if (password.isEmpty()) {
                etPassword.setError(getString(R.string.requiredError));
                error = true;
            }
            if (error) {
                return;
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (!user.isEmailVerified()){
                        emailConfirmResend(user);
                        return;
                    }
                    Toast.makeText(this, "Hello "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
//                    Intent feltoltesre = new Intent(this, FeltoltesActivity.class);
//                    startActivity(feltoltesre);
//                    finish();
                }
            });
        });
    }

    private void emailConfirmResend(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            Toast.makeText(this, "Erősítsd meg az e-mail címed", Toast.LENGTH_SHORT).show();
        });
    }

    private void initialize() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();
    }
}