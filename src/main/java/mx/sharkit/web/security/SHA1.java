package mx.sharkit.web.security;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Adrián Salgado D.
 */
public class SHA1 {

    public static String encriptarBase64(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException("Error al obtener la instancia SHA", ex);
        }

        try {
            md.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedEncodingException("Error al encriptar el password");
        }

        byte raw[] = md.digest();
        
        return Base64.getEncoder().encodeToString(raw);
    }

    public static String encriptarHexadecimal(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException("Error al obtener la instancia SHA", ex);
        }

        try {
            md.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedEncodingException("Error al encriptar el password");
        }

        byte raw[] = md.digest();
        return toHexadecimal(raw);
    }

    private static String toHexadecimal(byte[] datos) {
        String resultado = "\\";
        ByteArrayInputStream input = new ByteArrayInputStream(datos);
        String cadAux;
        int leido = input.read();
        while (leido != -1) {
            cadAux = Integer.toHexString(leido);
            if (cadAux.length() < 2) {
                resultado += "0";
            }

            resultado += cadAux;
            leido = input.read();
        }

        return resultado;
    }

}
