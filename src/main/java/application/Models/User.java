package application.Models;

public class User {
    private static int idCounter = 1; // Static counter for ID generation
    private int id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.id = idCounter++;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
