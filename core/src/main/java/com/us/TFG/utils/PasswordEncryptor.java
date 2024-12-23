package com.us.TFG.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {
    public static void main(String[] args) {
        // Contraseña a encriptar
        String password = "play3r";

        // Generar el hash encriptado
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Imprimir la contraseña encriptada
        System.out.println("Contraseña original: " + password);
        System.out.println("Contraseña encriptada: " + hashedPassword);
    }
}
