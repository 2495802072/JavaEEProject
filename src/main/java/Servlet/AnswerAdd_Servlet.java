package Servlet;

import Classes.Answer;
import Classes.User;
import Managers.AnswersManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "answerAddServlet", value = "/answerAdd-servlet")
public class AnswerAdd_Servlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String user_sid = request.getParameter("user_id");
            String question_sid = request.getParameter("question_id");
            int user_id = Integer.parseInt(user_sid);
            String answer_text = request.getParameter("answer_text");
            int question_id = Integer.parseInt(question_sid);

            Answer A = new Answer();
            A.setOwner_id(user_id);
            A.setAnswer(answer_text);
            A.setQuestion_id(question_id);

            try {
                AnswersManager.add_answer(A);
                response.sendRedirect("index.jsp");
            } catch (SQLException e) {
                // 处理数据库异常
                throw new RuntimeException("数据库操作失败: " + e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                // 处理类找不到异常
                throw new RuntimeException("类找不到异常: " + e.getMessage(), e);
            }

        } catch (NumberFormatException e) {
            out.println("<html><head><script>");
            out.println("alert('未登录或者缺少回答目标');");
            out.println("window.location.href = 'index.jsp';");
            out.println("</script></head></html>");
        }
    }

}
