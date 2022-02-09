package com.darkbeast0106.firebase14s_21_22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etFullName;
    private EditText etPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            boolean error = false;
            if (email.isEmpty()){
                etEmail.setError(getString(R.string.requiredError));
                error = true;
            }
            if (username.isEmpty()){
                etUsername.setError(getString(R.string.requiredError));
                error = true;
            }
            if (password.isEmpty()){
                etPassword.setError(getString(R.string.requiredError));
                error = true;
            }
            if (fullName.isEmpty()){
                etFullName.setError(getString(R.string.requiredError));
                error = true;
            }
            if (error){
                return;
            }

            firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> createUser(email, username, password, fullName));

        });
        btnLogin.setOnClickListener(view -> {
            Intent toRegister = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(toRegister);
            finish();
        });
    }

    private void createUser(String email, String username, String password, String fullName) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            User user = new User(email, username, password);
            databaseReference.child(firebaseUser.getUid()).setValue(user);
            firebaseUser.sendEmailVerification();
            firebaseAuth.signOut();
        });
    }

    private void initialize() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    }
}