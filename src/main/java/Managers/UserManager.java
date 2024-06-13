package Managers;

import Classes.*;

import java.sql.*;
import java.util.ArrayList;

public class UserManager {
    /**
     * 从数据库中获取所有用户的信息。
     *
     * @return 返回一个包含所有用户的ArrayList
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<User> getAllUsers() throws SQLException, ClassNotFoundException {
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
        return users;
    }
    /**
     * 根据用户名获取一个用户的信息。
     *
     * @param username 用户名
     * @return 返回一个User对象，如果找不到用户则返回null
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static User getOneUser(String username) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb","root","123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * from users WHERE username = ?");
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();

        User u = null;
        while(resultSet.next()){
            u = new User(resultSet.getInt("uid"),resultSet.getString("username"),resultSet.getString("password"));
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return u;
    }
    /**
     * 从数据库中获取所有用户的用户名。
     *
     * @return 返回一个包含所有用户名的ArrayList
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<String> getAllUserNames() throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        Statement statement = connect.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        int rowCount = resultSet.last() ? resultSet.getRow() : 0;
        ArrayList<String> userNames = new ArrayList<String>(rowCount);
        resultSet.first();
        for (int i = 0; i < rowCount; i++) {
            userNames.add(resultSet.getString("username"));
            resultSet.next();
        }
        jdbc.close(connect, null, resultSet, statement);
        return userNames;
    }
    /**
     * 在数据库中注册一个新用户。
     *
     * @param user 要注册的用户信息
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static void regsiterUser(User user) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO users( username ,password) VALUES (?,?)");
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();

        jdbc.close(connect,preparedStatement,null,null);
    }

    public static String getUserName(int user_id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM users WHERE uid = ?");
        preparedStatement.setInt(1, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        String username = null;
        while(resultSet.next()){
            username = resultSet.getString("username");
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return username;
    }
}
