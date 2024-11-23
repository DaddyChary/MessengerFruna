package com.example.messengerfruna;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private DatabaseReference chatsRef;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
        // Inicializamos chatsRef
        this.chatsRef = FirebaseDatabase.getInstance().getReference("chats");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.emailTextView.setText(user.getEmail());

        // Al hacer clic, se crea un chat si no existe
        holder.itemView.setOnClickListener(view -> {
            createChat(user.getId(), view.getContext()); // Pasamos el contexto de la vista
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        Button deleteButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            deleteButton = itemView.findViewById(R.id.deleteConversationButton);
        }
    }

    private void createChat(String otherUserId, Context context) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = currentUserId + "_" + otherUserId;

        // Verificar si el chat ya existe
        chatsRef.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Si no existe, crear un nuevo chat
                    Map<String, Object> chatMap = new HashMap<>();
                    chatMap.put("users", Arrays.asList(currentUserId, otherUserId));

                    chatsRef.child(chatId).setValue(chatMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Si se crea el chat, ir al chat
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("chatId", chatId);
                                    context.startActivity(intent); // Usamos el contexto de la vista
                                } else {
                                    Toast.makeText(context, "Error al crear el chat", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Si ya existe, solo ir al chat
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("chatId", chatId);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error al verificar el chat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
