<%--
  Created by IntelliJ IDEA.
  User: 123
  Date: 2024/6/6
  Time: 上午10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="Managers.JDBCManager" %>
<%@ page import="java.sql.*" %>
<%@ page import="Classes.Questions" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>连接数据库</title>
</head>
<body>
<%
    //红字输出 out.println("<p style='color: #f00'> "++" </p>");
    try {
        JDBCManager jdbc = new JDBCManager("qadb", "root", "123456");
        Connection connect = jdbc.getConnection();
        String sql = "SELECT * FROM questions " +
                "WHERE id LIKE ? AND owner = ? AND title LIKE ? AND details LIKE ?";
        PreparedStatement preparedStatement = connect.prepareStatement(sql);

        sql += " AND DATE(Date) = "+ "2024-06-10";

        out.println(sql +"<hr>");

        preparedStatement.setString(1, "%"+""+"%");
        preparedStatement.setInt(2, 1);
        preparedStatement.setString(3, "%"+""+"%");
        preparedStatement.setString(4, "%"+""+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Questions> Q = new ArrayList<>();
        while (resultSet.next()) {
            Questions question = new Questions(resultSet.getInt("owner"), resultSet.getString("title"), resultSet.getString("details"));
            question.setId(resultSet.getInt("id"));
            question.setCreatedDate(resultSet.getDate("Date"));
            Q.add(question);

            out.println(resultSet.getString("title"));
            out.println(resultSet.getDate("Date") + "<hr>");
        }
        jdbc.close(connect,preparedStatement,resultSet,null);
        out.println(Q.size());
    } catch (Exception e) {
        out.println("发生错误: " + e.getMessage());
    }

%>
</body>
</html>
