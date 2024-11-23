package com.example.messengerfruna;

import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import model.ChatMessage;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private List<ChatMessage> messageList;

    public MessageAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);

        // Obtener el ID del usuario actual
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Si el mensaje fue enviado por el usuario actual, alinearlo a la derecha
        if (message.getSenderId().equals(currentUserId)) {
            holder.messageTextView.setBackgroundResource(R.drawable.message_background_right);
            holder.messageTextView.setGravity(Gravity.END);  // Alineación a la derecha

            // Ajustamos las restricciones para que el mensaje esté pegado a la derecha
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.messageTextView.getLayoutParams();
            params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;  // Eliminar cualquier restricción izquierda
            params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;  // Alinearlo a la derecha
            holder.messageTextView.setLayoutParams(params);
        } else {
            // Si el mensaje fue enviado por otro usuario, alinearlo a la izquierda
            holder.messageTextView.setBackgroundResource(R.drawable.message_background_left);
            holder.messageTextView.setGravity(Gravity.START);  // Alineación a la izquierda

            // Ajustamos las restricciones para que el mensaje esté pegado a la izquierda
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.messageTextView.getLayoutParams();
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;  // Alinearlo a la izquierda
            params.rightToRight = ConstraintLayout.LayoutParams.UNSET;  // Eliminar cualquier restricción derecha
            holder.messageTextView.setLayoutParams(params);
        }

        // Establecer el texto del mensaje
        holder.messageTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
