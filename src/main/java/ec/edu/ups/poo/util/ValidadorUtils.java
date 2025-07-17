package ec.edu.ups.poo.util;

public class ValidadorUtils {

    public static boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        if (!cedula.matches("\\d+")) {
            return false;
        }

        try {

            int provincia = Integer.parseInt(cedula.substring(0, 2));
            if (provincia < 1 || provincia > 24) {
                return false;
            }


            int digitoVerificadorReal = Integer.parseInt(cedula.substring(9, 10));

            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Integer.parseInt(cedula.substring(i, i + 1));
                int producto;

                if (i % 2 == 0) {
                    producto = digito * 2;
                    if (producto > 9) {
                        producto -= 9;
                    }
                } else {

                    producto = digito * 1;
                }
                suma += producto;
            }


            int digitoVerificadorCalculado = 0;
            if (suma % 10 != 0) {
                digitoVerificadorCalculado = 10 - (suma % 10);
            }


            return digitoVerificadorCalculado == digitoVerificadorReal;

        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean validarPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@_-]).{6,}$";
        return password.matches(passwordRegex);
    }
}