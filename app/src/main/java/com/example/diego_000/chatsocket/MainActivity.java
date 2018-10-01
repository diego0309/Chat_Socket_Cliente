package com.example.diego_000.chatsocket;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{

    EditText username;
    EditText ip;
    Button btIniciar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = findViewById(R.id.etIp);
        btIniciar = findViewById(R.id.btIniciar);
    }


    public void conectar(View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View popupInputDialogView = inflater.inflate(R.layout.layout_personalizado, null);
        username = popupInputDialogView.findViewById(R.id.username);
        builder.setView(popupInputDialogView);
        builder.setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivity.this,MyReceiver.class);
                intent.putExtra("username", username.getText().toString());
                intent.putExtra("ip", ip.getText().toString());
                sendBroadcast(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

}
