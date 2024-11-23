package com.example.messengerfruna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.User;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button backButton, reloadButton;
    private List<User> userList = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference usersRef, chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        backButton = findViewById(R.id.btnBack);
        reloadButton = findViewById(R.id.btnReload);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        loadUsers(); // Cargar todos los usuarios

        backButton.setOnClickListener(v -> {
            finish(); // Cierra la actividad actual y regresa a la anterior
        });

        reloadButton.setOnClickListener(view -> {
            loadUsers();
        });

    }

    private void loadUsers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && !user.getId().equals(auth.getCurrentUser().getUid())) {
                        userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Error al cargar los usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
