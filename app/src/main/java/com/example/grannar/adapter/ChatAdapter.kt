package com.example.grannar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grannar.R
import com.example.grannar.data.model.ChatMessage
import com.example.grannar.databinding.ItemReceiveBinding
import com.example.grannar.databinding.ItemSendBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private val messages: List<ChatMessage>, private val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MESSAGE_SENT = 1
    private val MESSAGE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) MESSAGE_SENT else MESSAGE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MESSAGE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_send, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_receive, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.senderNameTextView)
        private val messageText: TextView = itemView.findViewById(R.id.messageTextView)
        private val timeText: TextView = itemView.findViewById(R.id.sendtimeTextView)

        fun bind(chatMessage: ChatMessage) {
            nameText.text = chatMessage.senderName
            messageText.text = chatMessage.message
            timeText.text = formatTime(chatMessage.timestamp)
        }
    }

    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderNameText: TextView = itemView.findViewById(R.id.receveNameTextView)
        private val messageText: TextView = itemView.findViewById(R.id.recevmessageTextView)
        private val timeText: TextView = itemView.findViewById(R.id.recevtimeTextView)

        fun bind(chatMessage: ChatMessage) {
            senderNameText.text = chatMessage.senderName
            messageText.text = chatMessage.message
            timeText.text = formatTime(chatMessage.timestamp)
        }
    }
}
