package com.example.avortografia

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Principal : AppCompatActivity() {
    private val REQUEST_CODE_SPEECH_INPUT = 1 //para el reconocimiento de voz

    //Se inicializa en el momento en el que se use
    private val adapter by lazy { ViewPagerAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        // Configura el ViewPager2 y el adaptador
        val pager = findViewById<ViewPager2>(R.id.pager)
        pager.adapter = adapter

        // Configura el TabLayout y TabLayoutMediator para conectar con el ViewPager2
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val tabLayoutMediator = TabLayoutMediator(tabLayout, pager) { tab, position ->
            // Infla el diseño personalizado para cada pestaña
            val customView = layoutInflater.inflate(R.layout.tab_custom, null)
            val tabIcon = customView.findViewById<ImageView>(R.id.tab_icon)
            val tabText = customView.findViewById<TextView>(R.id.tab_text)
            //val badge:BadgeDrawable=tab.orCreateBadge

            // Establece el icono y el texto en función de la posición
            when (position) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.ic_lecciones)
                    tabText.text = "Lección"

                    //badge.backgroundColor=ContextCompat.getColor(applicationContext, R.color.colorLilaFuerte)
                    //badge.isVisible=true
                    //badge.number = 5
                }

                1 -> {
                    tabIcon.setImageResource(R.drawable.i_ejercicios)
                    tabText.text = "Ejercicios"
                }

                2 -> {
                    tabIcon.setImageResource(R.drawable.i_evaluacion)
                    tabText.text = "Evaluación"
                }
            }

            // Configura la vista personalizada para el tab actual
            tab.customView = customView
        }
        // Asocia el TabLayoutMediator con el TabLayout y ViewPager2
        tabLayoutMediator.attach()


        // Reconocimiento de voz

        val fabVoice: FloatingActionButton = findViewById(R.id.fab_mic)
        fabVoice.setOnClickListener {
            startVoiceRecognition()
        }

        // Chat
        val fab_chat: FloatingActionButton = findViewById(R.id.fab_chat)
        fab_chat.setOnClickListener {
            showChatDialog()
        }


    }


    //Reconocimiento de voz
    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "es-MX"
        ) // Usa "es-ES" para español de España, "en-US" para inglés de EEUU
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora")

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Tu dispositivo no soporta reconocimiento de voz",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0) ?: "No se reconoció el texto"
            //Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show()
            // Inicializa DialogflowClient
            val dialogflowClient = DialogflowClient(this)
            // Enviar mensaje a Dialogflow y obtener respuesta
            dialogflowClient.sendMessage(spokenText) { botResponse ->
                runOnUiThread {
                    // Texto de respuesta del bot
                   if(botResponse.text.isNotEmpty()){
                       Log.d("Dialogflow", "Respuesta: ${botResponse.text}")
                       Toast.makeText(this, "Bot: "+botResponse.text, Toast.LENGTH_SHORT).show()
                   }
                    // Opcional: Muestra acción y parámetros en logs
                    if (botResponse.action.isNotEmpty()) {
                        Log.d("Dialogflow", "Action: ${botResponse.action}")
                    }
                    if (botResponse.parameters.isNotEmpty()) {
                        Log.d("Dialogflow", "Parameters: ${botResponse.parameters}")
                    }
                }
            }

        }
    }


//CHAT

    private fun showChatDialog() {
        val dialogView = layoutInflater.inflate(R.layout.chat, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Inicializa DialogflowClient
        val dialogflowClient = DialogflowClient(this)

        // Obtén las vistas del diálogo
        val btnCloseChat: ImageButton = dialogView.findViewById(R.id.btn_close_chat)
        val recyclerMessages: RecyclerView = dialogView.findViewById(R.id.recycler_messages)
        val inputMessage: EditText = dialogView.findViewById(R.id.input_message)
        val btnSend: Button = dialogView.findViewById(R.id.btn_send)

        // Lista mutable para almacenar mensajes
        val messages = mutableListOf<Pair<String, Boolean>>() // Pair(mensaje, esUsuario)
        val messageAdapter = MessageAdapter(messages)
        recyclerMessages.apply {
            layoutManager = LinearLayoutManager(this@Principal)
            adapter = messageAdapter
        }

        // Acción al enviar un mensaje
        btnSend.setOnClickListener {
            val userMessage = inputMessage.text.toString()
            if (userMessage.isNotEmpty()) {
                // Agrega el mensaje del usuario al chat
                messageAdapter.addMessage(Pair(userMessage, true))  // Mensaje del usuario
                inputMessage.text.clear()
                recyclerMessages.scrollToPosition(messages.size - 1)

                // Enviar mensaje a Dialogflow y obtener respuesta
                dialogflowClient.sendMessage(userMessage) { botResponse ->
                    runOnUiThread {
                        // Agrega el texto de respuesta del bot al chat
                        messageAdapter.addMessage(Pair(botResponse.text, false))

                        // Opcional: Muestra acción y parámetros en logs
                        if (botResponse.action.isNotEmpty()) {
                            Log.d("Dialogflow", "Action: ${botResponse.action}")
                        }
                        if (botResponse.parameters.isNotEmpty()) {
                            Log.d("Dialogflow", "Parameters: ${botResponse.parameters}")
                        }

                        // Desplaza la vista al último mensaje
                        recyclerMessages.scrollToPosition(messages.size - 1)
                    }
                }

            }
        }
        // Botón de cerrar
        btnCloseChat.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo cuando se presiona el botón
        }

        dialog.show()
    }

}
