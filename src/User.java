public class User {
    private String username;
    private String password;
    public User(String username, String password) {

        if (username.length() > 50) {
            throw new IllegalArgumentException(
                    "Username is too long, try something shorter"
            );
        }

        if (!isValidEmail(username)) {
            throw new IllegalArgumentException(
                    "Please enter a valid Email as username"
            );
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "Your password is too short, add more characters"
            );
        }

        if (password.length() > 12) {
            throw new IllegalArgumentException(
                    "Your password is too long, try a shorter one"
            );
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Please enter a valid password"
            );
        }

        this.username = username;
        this.password = password;
    }
    private boolean isValidEmail(String email) {
        String emailRegex =
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9][A-Za-z0-9.-]*\\.[A-Za-z]{2,}$";

        return email.matches(emailRegex);
    }
    private boolean isValidPassword(String password) {
        String allowedSymbols = "!@#$%^&*()+-_?=";

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z')) {
                hasLetter = true;
            }
            else if (c >= '0' && c <= '9') {
                hasDigit = true;
            }
            else if (allowedSymbols.indexOf(c) != -1) {
                hasSymbol = true;
            }
            else {
                return false;
            }
        }

        return hasLetter && hasDigit && hasSymbol;
    }
    public String getName() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}
