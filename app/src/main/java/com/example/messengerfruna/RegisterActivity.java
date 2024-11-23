package com.example.messengerfruna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, password;
    private View registerButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);
        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            if (!emailText.isEmpty() && !passwordText.isEmpty()) {
                auth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (currentUser != null) {
                                    String userId = currentUser.getUid();
                                    String email = currentUser.getEmail();

                                    // Guardar el email en la base de datos
                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                    usersRef.child(userId).setValue(new User(userId, email));
                                }

                                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, LoginActivity.class));  // Redirige al login
                            } else {
                                Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
