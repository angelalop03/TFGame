package com.us.TFG.utils.validators;

public class InputValidator {
    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "El nombre de usuario es obligatorio.";
        }
        if (username.length() < 3 || username.length()> 15) {
            return "El nombre de usuario debe tener al entre 3 y 15 caracteres.";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es obligatoria.";
        }
        if (password.length() < 6 || password.length() >20 ) {
            return "La contraseña debe contener entre 6 y 20 caracteres.";
        }
        if (!password.matches(".*\\d.*")) {
            return "La contraseña debe contener al menos un número.";
        }
        return null;
    }

    public static String validateFirstname(String firstname) {
        if (firstname == null || firstname.trim().isEmpty()) {
            return "El nombre es obligatorio.";
        }
        if (!firstname.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "El nombre solo puede contener letras.";
        }
        return null;
    }

    public static String validateLastname(String lastname) {
        if (lastname == null || lastname.trim().isEmpty()) {
            return "El apellido es obligatorio.";
        }
        if (!lastname.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "El apellido solo puede contener letras.";
        }
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "El correo electrónico es obligatorio.";
        }
        if (!email.contains("@") || !email.contains(".")) {
            return "El formato del correo electrónico es incorrecto.";
        }
        return null;
    }
}
