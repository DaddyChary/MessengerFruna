package com.example.messengerfruna;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private DatabaseReference chatsRef;
    private String currentUserId; // ID del usuario logeado

    public UserAdapter(List<User> userList) {
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Obtener el ID del usuario logeado
        this.chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        // Filtrar los usuarios para excluir al usuario logeado
        this.userList = filterUsers(userList);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Establecer el nombre de usuario en el TextView
        if (user != null && user.getUserName() != null) {
            holder.userName.setText(user.getUserName());
        } else {
            holder.userName.setText("Nombre no disponible");
        }

        // Acción al hacer clic en la tarjeta (o el TextView del nombre) para iniciar el chat
        holder.itemView.setOnClickListener(view -> {
            String otherUserId = user.getUserId();
            String userName = user.getUserName();  // Obtener el nombre del usuario

            // Ordenar los userIds para garantizar un chatId consistente
            List<String> users = Arrays.asList(currentUserId, otherUserId);
            Collections.sort(users); // Esto garantiza que los IDs estén en orden consistente

            // Generar el chatId con los userIds ordenados
            String chatId = users.get(0) + "_" + users.get(1);

            // Intent para abrir el chat
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("userId", otherUserId);  // Pasar el userId del otro usuario
            intent.putExtra("userName", userName);  // Pasar el nombre del usuario al chat
            view.getContext().startActivity(intent);
        });

        // Acción al hacer clic en el botón de eliminar conversación
        holder.chatButton.setOnClickListener(view -> {
            String otherUserId = user.getUserId();

            // Crear un cuadro de diálogo de confirmación
            new android.app.AlertDialog.Builder(view.getContext())
                    .setMessage("¿Estás seguro de que deseas eliminar esta conversación?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String chatId = generateChatId(currentUserId, otherUserId);

                            // Eliminar el chat de Firebase
                            chatsRef.child(chatId).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(view.getContext(), "Conversación eliminada", Toast.LENGTH_SHORT).show();

                                    // Obtén la posición actual usando getAdapterPosition()
                                    int currentPosition = holder.getAdapterPosition();
                                    if (currentPosition != RecyclerView.NO_POSITION) {
                                        // Actualizar la lista de usuarios
                                        userList.remove(currentPosition);  // Eliminar usuario de la lista
                                        notifyItemRemoved(currentPosition);  // Actualizar RecyclerView
                                    }
                                } else {
                                    Toast.makeText(view.getContext(), "Error al eliminar la conversación", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Filtrar los usuarios para excluir al usuario logeado
    private List<User> filterUsers(List<User> users) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user != null && !user.getUserId().equals(currentUserId)) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    public void updateUsers(List<User> newUsers) {
        userList = filterUsers(newUsers); // Aplicar el filtro al actualizar la lista
        notifyDataSetChanged();
    }

    // Método para generar un chatId consistente
    private String generateChatId(String currentUserId, String otherUserId) {
        List<String> users = Arrays.asList(currentUserId, otherUserId);
        Collections.sort(users); // Garantiza que el orden de los IDs sea consistente
        return users.get(0) + "_" + users.get(1);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public Button chatButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.emailTextView);  // Asegúrate de que el ID esté correcto
            chatButton = itemView.findViewById(R.id.deleteConversationButton);  // Botón de eliminar conversación
        }
    }
}
