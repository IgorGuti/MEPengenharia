package com.mep.app.ui.repository

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Chamado quando um novo Token de Registro é gerado para o app.
     * Você deve enviar este token para o seu servidor de backend.
     */
    override fun onNewToken(token: String) {
        Log.d("FCM_Token", "Refreshed token: $token")

        // **IMPORTANTE:** Envie este token para o seu servidor de backend
        // para que você possa enviar mensagens direcionadas a este dispositivo.
        sendRegistrationToServer(token)
    }

    /**
     * Chamado quando uma mensagem FCM é recebida.
     * Use este método para lidar com mensagens em primeiro plano (foreground)
     * e mensagens de dados (data messages).
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM_Message", "From: ${remoteMessage.from}")

        // Verifique se a mensagem contém uma carga de dados.
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM_Data", "Message data payload: " + remoteMessage.data)
        }

        // Verifique se a mensagem contém uma notificação.
        remoteMessage.notification?.let {
            Log.d("FCM_Notification", "Message Notification Body: ${it.body}")
            // Aqui você criaria e exibiria a notificação no status bar
            showNotification(it.title, it.body)
        }
    }

    // Função de exemplo para processar o token (você implementará a lógica de rede)
    private fun sendRegistrationToServer(token: String) {
        // Implemente sua chamada de API para enviar o token ao seu backend
    }

    // Função de exemplo para exibir a notificação
    private fun showNotification(title: String?, message: String?) {
        // Implemente o código para criar um Android Notification Channel e exibir a notificação.
        // Se o app estiver em background, o FCM lida com a exibição de notificações automaticamente.
    }
}