package Managers;

import Classes.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnswersManager {
    //public ArrayList<Answer> answers_list = new ArrayList<Answer>();
    /**
     * 通过关键词搜索answer。
     *
     * @param keyword 搜索的关键词
     * @return 返回一个包含搜索answer的ArrayList
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<Answer> search_by_keyword(String keyword) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM answer " +
                "WHERE (answer LIKE ? AND answer != 'none') ");
        preparedStatement.setString(1, "%"+keyword+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Answer> answers_list = new ArrayList<Answer>();
        while (resultSet.next()) {
            Answer answer = new Answer(resultSet.getInt("ans_id"), resultSet.getInt("owner_id"),
                    resultSet.getDate("date"),resultSet.getString("answer"), resultSet.getInt("question_id"));

            answers_list.add(answer);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return answers_list;
    }
    /**
     * 通过answerID搜索answer。
     *
     * @param id answer的ID
     * @return 返回一个包含搜索answer的Answer对象，如果未找到则抛出异常
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static Answer search_by_id(int id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM answer WHERE answer.ans_id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Answer answer = new Answer(resultSet.getInt("ans_id"), resultSet.getInt("owner_id"),
                    resultSet.getTimestamp("date"),resultSet.getString("answer"), resultSet.getInt("question_id"));
            jdbc.close(connect,preparedStatement,resultSet,null);
            return answer;
        }else{
            jdbc.close(connect,preparedStatement,resultSet,null);
            throw new SQLException("answerID " + id + " 在数据库中未找到。");
        }
    }
    /**
     * 添加一个新的answer到数据库。
     *
     * @param answer 新的answer信息
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static void add_answer(Answer answer) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO answer(owner_id, answer, question_id) VALUE (?,?,?)");
        preparedStatement.setInt(1, answer.getOwner_id());
        preparedStatement.setString(2, answer.getAnswer());
        preparedStatement.setInt(2, answer.getQuestion_id());
        preparedStatement.executeUpdate();

        jdbc.close(connect,preparedStatement,null,null);
    }

    /**
     *
     * @param owner_id 所有者的uid
     * @return ArrayList<Answer>
     * @throws SQLException 如果数据库访问出错
     * @throws ClassNotFoundException 如果JDBC驱动没有找到
     */
    public static ArrayList<Answer> search_by_owner(int owner_id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM answer WHERE owner_id = ?");
        preparedStatement.setInt(1, owner_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Answer> A = new ArrayList<>();
        while (resultSet.next()) {
            Answer answer = new Answer(resultSet.getInt("ans_id"), resultSet.getInt("owner_id"),
                    resultSet.getDate("date"),resultSet.getString("answer"), resultSet.getInt("question_id"));
            A.add(answer);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return A;
    }

    /**
     * 删除answer
     * @param id
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void delete_answer(int id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM answer WHERE ans_id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        jdbc.close(connect,preparedStatement,null,null);
    }

    /**
     * 编辑answer
     * @param answer
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void edit_answer(Answer answer) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        PreparedStatement preparedStatement = connect.prepareStatement("UPDATE answer SET answer = ? WHERE ans_id = ?");
        preparedStatement.setString(1, answer.getAnswer());
        preparedStatement.setInt(2, answer.getAns_id());
        preparedStatement.executeUpdate();
        jdbc.close(connect,preparedStatement,null,null);
    }

    public static ArrayList<Answer> search_in_owner(int owner_id,int answer_id,String answer_String,String date) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        String sql = "SELECT * FROM answer " +
                "WHERE ans_id LIKE ? AND owner_id = ? AND answer LIKE ? ";
        if (date != null && !date.isEmpty()) {
            sql += " AND DATE(Date) = '"+ date + "'";
        }
        PreparedStatement preparedStatement = connect.prepareStatement(sql);
        String id = answer_id==-1?"":answer_id+""; //-1时不参与查询
        preparedStatement.setString(1, "%"+id+"%");
        preparedStatement.setInt(2, owner_id);
        preparedStatement.setString(3, "%"+answer_String+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Answer> A = new ArrayList<>();
        while (resultSet.next()) {
            Answer answer = new Answer(resultSet.getInt("ans_id"), resultSet.getInt("owner_id"),
                    resultSet.getDate("date"),resultSet.getString("answer"), resultSet.getInt("question_id"));
            A.add(answer);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return A;
    }

    public static ArrayList<Answer> search_by_question_id(int question_id) throws SQLException, ClassNotFoundException {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();

        PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM answer " +
                "WHERE question_id = ?");
        preparedStatement.setInt(1, question_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Answer> A = new ArrayList<Answer>();
        while (resultSet.next()) {
            Answer answer = new Answer(resultSet.getInt("ans_id"), resultSet.getInt("owner_id"),
                    resultSet.getDate("date"),resultSet.getString("answer"), resultSet.getInt("question_id"));

            A.add(answer);
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        return A;
    }
}
