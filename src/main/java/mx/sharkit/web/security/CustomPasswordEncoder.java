package mx.sharkit.web.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adrián Salgado D.
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String hashed = null;
        try {
            hashed =  SHA1.encriptarBase64(rawPassword.toString());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomPasswordEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String hashed = null;
        try {
            hashed =  SHA1.encriptarBase64(rawPassword.toString());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomPasswordEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StringUtils.equals(hashed, encodedPassword);
    }
    
}
