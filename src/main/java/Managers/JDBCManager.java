package Managers;
import java.sql.*;

public class JDBCManager {
    private String dbname ="qadb";
    private String username ="root";
    private String password ="123456";

    public JDBCManager(String dbname, String username, String password) {
        this.dbname = dbname;
        this.username = username;
        this.password = password;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/"+this.dbname+"?serverTimezone=UTC",
                this.username,
                this.password
        );
        return conn;
    }
    public void close(Connection connect, PreparedStatement pstate, ResultSet data,Statement state) throws SQLException{
        if (connect != null) connect.close();
        if (pstate != null) pstate.close();
        if (data != null) data.close();
        if (state != null) state.close();
    }
}
