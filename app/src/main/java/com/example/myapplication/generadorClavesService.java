package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class generadorClavesService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // No es necesario para este caso, ya que no queremos interactuar directamente con la interfaz.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Generar la clave en segundo plano
        String generatedKey = new generadorDeClaves().generarClave();

        // Enviar el resultado de la clave generada a la actividad
        Intent resultIntent = new Intent();
        resultIntent.putExtra("GENERATED_KEY", generatedKey);
        resultIntent.setAction("com.example.myapplication.KEY_GENERATED");
        sendBroadcast(resultIntent); // Enviar como broadcast

        return START_NOT_STICKY; // No necesitamos que el servicio se reinicie si se detiene
    }
}
