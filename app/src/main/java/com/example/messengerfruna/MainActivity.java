package com.example.messengerfruna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import model.ChatMessage;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private DatabaseReference messagesRef;
    private List<ChatMessage> messageList;
    private MessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TextView userNameTextView; // El TextView donde se mostrará el nombre del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        userNameTextView = findViewById(R.id.textViewUserName); // Inicializa el TextView

        // Obtener el chatId del intent
        String chatId = getIntent().getStringExtra("chatId");

        // Verificar si se recibió un chatId
        if (chatId == null || chatId.isEmpty()) {
            Toast.makeText(MainActivity.this, "Error: chatId no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtener el nombre del usuario del Intent
        String userName = getIntent().getStringExtra("userName");

        // Establecer el nombre del usuario en el TextView de la Toolbar
        if (userName != null) {
            userNameTextView.setText(userName); // Mostrar el nombre del usuario en el TextView
        }

        // Referencia a los mensajes de un chat específico
        messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId).child("messages");

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList);

        // Configuración del RecyclerView
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Desplazar automáticamente al final
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Agregar un observador para desplazamiento automático
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                // Si el usuario ya está al final, desplazarse automáticamente
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || lastVisiblePosition == positionStart - 1) {
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });

        // Botón para volver atrás
        buttonBack.setOnClickListener(v -> finish()); // Cierra la actividad actual y vuelve a la anterior

        // Enviar mensaje
        sendButton.setOnClickListener(view -> {
            String text = messageInput.getText().toString();
            if (!text.isEmpty()) {
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Guardar mensaje en Firebase
                ChatMessage message = new ChatMessage(senderId, text);
                messagesRef.push().setValue(message)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                            }
                        });
                messageInput.setText(""); // Limpiar el campo de entrada
            }
        });

        // Escuchar cambios en los mensajes (agregar nuevos)
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Obtener el mensaje y agregarlo a la lista
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                if (message != null) {
                    messageList.add(message);
                    adapter.notifyItemInserted(messageList.size() - 1); // Notificar al adaptador
                    recyclerView.scrollToPosition(messageList.size() - 1); // Desplazar al último mensaje
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Aquí puedes manejar cambios en los mensajes si es necesario
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Aquí puedes manejar la eliminación de mensajes si es necesario
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Manejar movimiento de mensajes si es necesario
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load messages!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
