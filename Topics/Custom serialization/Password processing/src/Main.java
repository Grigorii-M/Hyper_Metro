import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class UserProfile implements Serializable {
    private static final long serialVersionUID = 26292552485L;

    private String login;
    private String email;
    private transient String password;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();
        String encryptPassword = encrypt(password);
        oos.writeObject(encryptPassword);
    }

    private String encrypt(String password) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            output.append((char) (ch + 1));
        }

        return String.valueOf(output);
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        this.password = decrypt((String) ois.readObject());
    }

    private String decrypt(String encrypted) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < encrypted.length(); i++) {
            char ch = encrypted.charAt(i);
            output.append((char) (ch - 1));
        }

        return String.valueOf(output);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}