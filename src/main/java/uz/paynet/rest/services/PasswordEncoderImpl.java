package uz.paynet.rest.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.paynet.rest.util.HashUtil;

@Service
public class PasswordEncoderImpl implements PasswordEncoder {

    public PasswordEncoderImpl() {
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return HashUtil.sha256hex(rawPassword.toString(), 8);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

}
