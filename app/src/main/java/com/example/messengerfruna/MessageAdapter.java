package com.example.messengerfruna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import model.ChatMessage;

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

        ConstraintLayout.LayoutParams paramsLeft = (ConstraintLayout.LayoutParams) holder.spaceLeft.getLayoutParams();
        ConstraintLayout.LayoutParams paramsRight = (ConstraintLayout.LayoutParams) holder.spaceRight.getLayoutParams();

        if (message.getSenderId().equals(currentUserId)) {
            // Mensaje del usuario actual -> Alinear a la derecha
            paramsLeft.horizontalWeight = 1f;  // Espacio izquierdo pequeño
            paramsRight.horizontalWeight = 0f; // Espacio derecho toma el resto

            holder.messageTextView.setBackgroundResource(R.drawable.message_background_right);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            // Mensaje de otro usuario -> Alinear a la izquierda
            paramsLeft.horizontalWeight = 0f;  // Espacio izquierdo toma el resto
            paramsRight.horizontalWeight = 1f; // Espacio derecho pequeño

            holder.messageTextView.setBackgroundResource(R.drawable.message_background_left);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        holder.spaceLeft.setLayoutParams(paramsLeft);
        holder.spaceRight.setLayoutParams(paramsRight);

        // Establecer el texto del mensaje
        holder.messageTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public View spaceLeft;
        public View spaceRight;

        public MessageViewHolder(View itemView) {
            super(itemView);
<<<<<<< HEAD
            messageTextView = itemView.findViewById(R.id.recyclerViewMessages);
=======
            messageTextView = itemView.findViewById(R.id.messageTextView);
            spaceLeft = itemView.findViewById(R.id.spaceLeft);
            spaceRight = itemView.findViewById(R.id.spaceRight);
>>>>>>> dd9a3e3c4fddadb937420f5da1f04ec9d8d4a0f1
        }
    }
}
