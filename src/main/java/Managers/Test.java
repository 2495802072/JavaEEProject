package Managers;

import Classes.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb","root","123456");
        Connection connect = jdbc.getConnection();
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users");

        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(resultSet.getInt("uid"), resultSet.getString("username"), resultSet.getString("password"));
            System.out.println(resultSet.getString("username"));
            users.add(user);
        }

        jdbc.close(connect,null,resultSet,statement);
    }
}
