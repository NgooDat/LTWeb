package webshop.security;

import de.svws_nrw.ext.jbcrypt.BCrypt;

public class Bcrypt {

    // Mã hóa mật khẩu
    public static String encodePassword(String password) {
        return de.svws_nrw.ext.jbcrypt.BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return de.svws_nrw.ext.jbcrypt.BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
