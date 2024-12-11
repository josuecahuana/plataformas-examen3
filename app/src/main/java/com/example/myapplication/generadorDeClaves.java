package com.example.myapplication;
import java.security.SecureRandom;

public class generadorDeClaves {
    public String generarClave(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" +
                "!@#$%^&*()-_=+<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();

        // Generar una clave de 10 caracteres
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            key.append(characters.charAt(index));
        }

        return key.toString(); // Devolver la clave generada
    }
}
