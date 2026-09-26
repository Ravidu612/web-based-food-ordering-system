import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashCheck {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isMatch = encoder.matches("password123", "$2a$10$eAccYoNO32OmArPoetdnQOmcx0K.HgphWwU6jWvXG.xX7lPj5dEFe");
        System.out.println("Match: " + isMatch);
    }
}
