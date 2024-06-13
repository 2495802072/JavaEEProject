package Managers;

import Classes.Questions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestionsManager {
    //public ArrayList<Questions> questions_list = new ArrayList<Questions>();
    /**
     * 通过关键词搜索问题。
     *
     * @param keyword 搜索的关键词
     * @return 返回一个包含搜索问题的ArrayList
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<Questions> search_by_keyword(String keyword) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM questions " +
                "WHERE (title LIKE ? AND title != 'none') " +
                "OR (details LIKE ? AND details != 'none')");
        preparedStatement.setString(1, "%"+keyword+"%");
        preparedStatement.setString(2, "%"+keyword+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Questions> questions_list = new ArrayList<Questions>();
        while (resultSet.next()) {
            // 获取data步骤
            Questions question = new Questions(resultSet.getInt("owner"), resultSet.getString("title"), resultSet.getString("details"));
            question.setId(resultSet.getInt("id"));
            question.setCreatedDate(resultSet.getTimestamp("Date"));
            // TODO 获取点赞列表uid
            questions_list.add(question);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return questions_list;
    }
    /**
     * 通过问题ID搜索问题。
     *
     * @param id 问题的ID
     * @return 返回一个包含搜索问题的Questions对象，如果未找到则抛出异常
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static Questions search_by_id(int id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM questions WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Questions question = new Questions(resultSet.getInt("owner"), resultSet.getString("title"), resultSet.getString("details"));
            question.setId(id);
            question.setCreatedDate(resultSet.getTimestamp("Date"));
            jdbc.close(connect,preparedStatement,resultSet,null);
            return question;
        }else{
            jdbc.close(connect,preparedStatement,resultSet,null);
            throw new SQLException("问题ID " + id + " 在数据库中未找到。");
        }
    }
    /**
     * 添加一个新的问题到数据库。
     *
     * @param question 新的问题信息
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static void add_question(Questions question) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO questions(owner, title, details) VALUE (?,?,?)");
        preparedStatement.setInt(1, question.getOwner());
        preparedStatement.setString(2, question.getTitle());
        preparedStatement.setString(3, question.getDescription());
        preparedStatement.executeUpdate();

        jdbc.close(connect,preparedStatement,null,null);
    }

    /**
     *
     * @param owner_id 所有者的uid
     * @return ArrayList<Questions>
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<Questions> search_by_owner(int owner_id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM questions WHERE owner = ?");
        preparedStatement.setInt(1, owner_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Questions> Q = new ArrayList<>();
        while (resultSet.next()) {
            Questions question = new Questions(resultSet.getInt("owner"), resultSet.getString("title"), resultSet.getString("details"));
            question.setId(resultSet.getInt("id"));
            question.setCreatedDate(resultSet.getTimestamp("Date"));
            Q.add(question);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return Q;
    }

    /**
     * 删除question
     * @param id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void delete_question(int id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM questions WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        jdbc.close(connect,preparedStatement,null,null);
    }

    /**
     * 编辑question
     * @param question
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void edit_question(Questions question) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("UPDATE questions SET title = ?,details = ? WHERE id = ?");
        preparedStatement.setString(1, question.getTitle());
        preparedStatement.setString(2, question.getDescription());
        preparedStatement.setInt(3, question.getId());
        preparedStatement.executeUpdate();
        jdbc.close(connect,preparedStatement,null,null);
    }

    public static ArrayList<Questions> search_in_owner(int owner_id,int question_id,String title,String summary,String created_date) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        String sql = "SELECT * FROM questions " +
                "WHERE id LIKE ? AND owner = ? AND title LIKE ? AND details LIKE ? ";
        if (created_date != null && !created_date.isEmpty()) {
            sql += " AND DATE(Date) = '"+ created_date + "'";
        }
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        String id = question_id==-1?"":question_id+""; //-1时不参与查询
        preparedStatement.setString(1, "%"+id+"%");
        preparedStatement.setInt(2, owner_id);
        preparedStatement.setString(3, "%"+title+"%");
        preparedStatement.setString(4, "%"+summary+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Questions> Q = new ArrayList<>();
        while (resultSet.next()) {
            Questions question = new Questions(resultSet.getInt("owner"), resultSet.getString("title"), resultSet.getString("details"));
            question.setId(resultSet.getInt("id"));
            question.setCreatedDate(resultSet.getDate("Date"));
            Q.add(question);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return Q;
    }
}
