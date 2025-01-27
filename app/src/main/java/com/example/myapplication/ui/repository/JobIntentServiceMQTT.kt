package com.example.myapplication.ui.repository

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttCallback

class MqttJobIntentService : JobIntentService() {

    private var mqttClient: MqttClient? = null

    // Defina o método onHandleWork para lidar com a tarefa em segundo plano
    override fun onHandleWork(intent: Intent) {
        try {
            mqttClient = MqttClient("tcp://YOUR_MQTT_BROKER_URL", MqttClient.generateClientId(), null)
            mqttClient?.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    // Lidar com a perda de conexão
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    message?.let {
                        // Exibir uma notificação quando uma mensagem chegar
                        showNotification(it.toString())
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // Lidar com a entrega da mensagem
                }
            })

            mqttClient?.connect()
            mqttClient?.subscribe("YOUR_TOPIC")  // Inscreva-se no tópico que o aplicativo irá ouvir

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun showNotification(message: String) {
        // Criar e exibir uma notificação
        val notification = NotificationCompat.Builder(this, "mqtt_channel")
            .setContentTitle("Nova Mensagem MQTT")
            .setContentText(message)
            .setSmallIcon(R.drawable.mep_engenharia)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    // Método para enfileirar o JobIntentService
    companion object {
        private const val JOB_ID = 1

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MqttJobIntentService::class.java, JOB_ID, work)
        }
    }
}