package com.example.diego_000.chatsocket;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    String CHANNEL_ID = "mx.edu.itcg.dfajardo.ejemplonotificacion";
    public static String KEY_TEXT_REPLY = "key_text_reply";
    public static Socket cliente = null;

    @Override
    public void onReceive(Context context, Intent intent) {

            String ipServidor = intent.getExtras().getString("ip");
            int puerto = 121;
            String nickname = intent.getExtras().getString("username");
            PrintWriter out;
            try{
                // Abre socket
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                cliente = new Socket(ipServidor,puerto);

                // Crea hilo de escucha
                HiloRecepcion escucha = new HiloRecepcion(cliente, context, intent);
                escucha.start();

                // Establece el flujo de salida

                out = new PrintWriter(MyReceiver.cliente.getOutputStream(), true);
                out.println(nickname);
                escucha.interrupt();

                // Cierra socket
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CharSequence name = "nombreCanal";
            String description = "descripcionCanal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


    class HiloRecepcion extends Thread {
        Socket cliente;
        BufferedReader in;
        Context context;
        Intent intent;

        public HiloRecepcion(Socket cliente, Context context, Intent intent) {
            this.cliente = cliente;
            this.context = context;
            this.intent = intent;
        }

        public void run() {
            String eco = "";
            try {
                in = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
                while (true) {
                    eco = in.readLine();
                    if (eco != null)
                    {
                        createNotificationChannel(context);

                        // Create an explicit intent for an Activity in your app
                        Intent intent2 = new Intent(context, EnviarMensajeReceiver.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);

                        // para el boton reply
                        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                                .setLabel("responder")
                                .build();

                        // Create the reply action and add the remote input.
                        NotificationCompat.Action action =
                                new NotificationCompat.Action.Builder(android.R.drawable.ic_dialog_info,
                                        "Responder aquí", pendingIntent)
                                        .addRemoteInput(remoteInput)
                                        .build();

                        // crea la notificacion
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(android.R.drawable.stat_notify_chat)
                                .setContentTitle("Responder")
                                .setContentText(eco)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                //.setContentIntent(pendingIntent)
                                .addAction(android.R.drawable.ic_btn_speak_now, "Abre actividad", pendingIntent)
                                .addAction(action)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(new Random().nextInt(), mBuilder.build());
                    }
                }

            } catch (Exception e) {
            }
        }
    }


}
