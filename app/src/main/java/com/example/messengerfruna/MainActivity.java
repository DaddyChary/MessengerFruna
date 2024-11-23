package com.example.messengerfruna;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import model.ChatMessage;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private DatabaseReference messagesRef;
    private List<ChatMessage> messageList;
    private MessageAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
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

        // Enviar mensaje
        sendButton.setOnClickListener(view -> {
            String text = messageInput.getText().toString();
            if (!text.isEmpty()) {
                String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Guardar mensaje en Firebase
                ChatMessage message = new ChatMessage(senderId, text);
                messagesRef.push().setValue(message);
                messageInput.setText(""); // Limpiar el campo de entrada
            }
        });

        // Escuchar cambios en los mensajes
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChatMessage message = data.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                adapter.notifyDataSetChanged();

                // Desplazarse automáticamente al último mensaje
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load messages!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
