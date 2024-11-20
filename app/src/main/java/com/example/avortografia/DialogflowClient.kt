package com.example.avortografia
import android.content.Context
import com.android.identity.util.UUID
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.protobuf.Value
import java.io.InputStream


/*class DialogflowClient(val context: Context) {

    private val sessionClient: SessionsClient
    private val sessionName: SessionName

    init {
        // Especifica directamente tu Project ID
        val projectId = "avortografia-mpbb"

        // Lee la clave JSON desde res/raw
        val stream: InputStream = context.resources.openRawResource(R.raw.dialogflow_key)
        val credentials = GoogleCredentials.fromStream(stream)

        val settings = SessionsSettings.newBuilder()
            .setCredentialsProvider { credentials }
            .build()

        sessionClient = SessionsClient.create(settings)
        sessionName = SessionName.of(projectId, "unique-session-id")
    }


    fun sendMessage(query: String, callback: (String) -> Unit) {
        Thread {
            try {
                // Construye la solicitud a Dialogflow
                val inputText = TextInput.newBuilder().setText(query).setLanguageCode("es").build()
                val queryInput = QueryInput.newBuilder().setText(inputText).build()

                val response = sessionClient.detectIntent(sessionName, queryInput)
                val fulfillmentText = response.queryResult.fulfillmentText

                // Devuelve la respuesta al callback
                callback(fulfillmentText)
            } catch (e: Exception) {
                e.printStackTrace()
                callback("Error: ${e.message}")
            }
        }.start()
    }
}
*/

class DialogflowClient(val context: Context) {

    private val sessionClient: SessionsClient
    private val sessionName: SessionName

    init {
        // Especifica directamente tu Project ID
        val projectId = "avortografia-mpbb"

        // Lee la clave JSON desde res/raw
        val stream: InputStream = context.resources.openRawResource(R.raw.dialogflow_key)
        val credentials = GoogleCredentials.fromStream(stream)

        val settings = SessionsSettings.newBuilder()
            .setCredentialsProvider { credentials }
            .build()

        sessionClient = SessionsClient.create(settings)
        sessionName = SessionName.of(projectId, UUID.randomUUID().toString()) // Genera una sesión única
    }

    fun sendMessage(query: String, callback: (BotResponse) -> Unit) {
        Thread {
            try {
                // Construye la solicitud a Dialogflow
                val inputText = TextInput.newBuilder()
                    .setText(query)
                    .setLanguageCode("es")
                    .build()
                val queryInput = QueryInput.newBuilder().setText(inputText).build()

                // Envía la solicitud y obtiene la respuesta
                val response = sessionClient.detectIntent(sessionName, queryInput)
                val queryResult = response.queryResult

                // Construye la respuesta del bot
                val botResponse = BotResponse(
                    text = queryResult.fulfillmentText,
                    action = queryResult.action,
                    parameters = queryResult.parameters.fieldsMap
                )

                // Devuelve la respuesta al callback
                callback(botResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(BotResponse("Error: ${e.message}", "", emptyMap()))
            }
        }.start()
    }
}

// Clase para estructurar la respuesta del bot
data class BotResponse(
    val text: String,  // Mensaje del bot
    val action: String,  // Acción detectada
    val parameters: Map<String, Value>  // Parámetros procesados
)

