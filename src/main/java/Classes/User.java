package Classes;

public class User {
    private int uid;
    private String name;
    private String password;

    public User() {}

    public User(int uid, String name, String password) {
        this.uid = uid;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    public boolean passwordMatch(String password) {
        return this.password.equals(password);
    }
}
