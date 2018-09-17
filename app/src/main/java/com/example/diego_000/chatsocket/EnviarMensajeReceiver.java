package com.example.diego_000.chatsocket;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.io.PrintWriter;

public class EnviarMensajeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = RemoteInput.getResultsFromIntent(intent);

        if(bundle != null)
        {
            try
            {
                PrintWriter out;
                String message = bundle.getCharSequence(MyReceiver.KEY_TEXT_REPLY).toString();
                // envía lo que el usuario teclea hasta que escriba "bye"
                out = new PrintWriter(MyReceiver.cliente.getOutputStream(), true);

                String eco = "";
                eco = message;
                out.println(eco);

            }
            catch(IOException e)
            {

            }
        }
    }
}
