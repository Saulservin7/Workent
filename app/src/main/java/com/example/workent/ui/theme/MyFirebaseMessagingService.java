package com.example.workent.ui.theme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.workent.MainActivity;
import com.example.workent.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "mi_canal";
            CharSequence name = "Mi Canal de Notificación";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("titulo");
            String message = remoteMessage.getData().get("detalle");

            // Crear un Intent para abrir el fragmento deseado al hacer clic en la notificación
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment_to_open", "RequestsFragment"); // Reemplaza con el fragmento que desees abrir

// Configurar el PendingIntent con la bandera FLAG_IMMUTABLE
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // Construir la notificación con el PendingIntent
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "mi_canal")
                    .setSmallIcon(R.drawable.ic_icon_workent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent); // Agregar el PendingIntent

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Mostrar la notificación
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
