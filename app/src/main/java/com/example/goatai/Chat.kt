package com.example.goatai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class Chat : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    private lateinit var retrofit: Retrofit
    private lateinit var geminiApiService: GeminiApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        retrofit = Retrofit.Builder()
            .baseUrl("http://172.26.24.103:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        geminiApiService = retrofit.create(GeminiApiService::class.java)

        // Dropdown menu in the Chat-title
        val spinner = findViewById<Spinner>(R.id.drop_spinner)
        ArrayAdapter.createFromResource(
            this@Chat,
            R.array.dropdown_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val menuspinner = findViewById<Spinner>(R.id.menu_icon)
        ArrayAdapter.createFromResource(
            this@Chat,
            R.array.menu_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            menuspinner.adapter = adapter
        }
        // Chat RecyclerView and Send button functionality
        recyclerView = findViewById(R.id.recyclerView)
        adapter = MessageAdapter(messages)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sendButton: ImageButton = findViewById(R.id.send_button)
        val messageEditText = findViewById<TextInputLayout>(R.id.edt_chatbox).editText as TextInputEditText

        sendButton.setOnClickListener {
            val content = messageEditText.text.toString()
            if (content.isNotEmpty()) {
                messages.add(Message(content, true))
                adapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                messageEditText.text?.clear()

                sendMessageToGemini(content)
            }
        }
    }

    private fun sendMessageToGemini(content: String) {
        val requestBody = RequestBody(inputMessage = content)
        geminiApiService.sendMessage(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    val reply = response.body()!!.text
                    // Add chatbot's response to RecyclerView
                    messages.add(Message(reply, false))
                    adapter.notifyItemInserted(messages.size - 1)
                    recyclerView.scrollToPosition(messages.size - 1)
                } else {
                    showToast("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




    inner class MessageAdapter(private val messages: List<Message>) :
        RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.messageTextview)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.messagetextview, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.textView.text = message.content
        }

        override fun getItemCount(): Int = messages.size
    }

    data class Message(val content: String, val isSentByUser: Boolean)
}
