package Servlet;

import Classes.Questions;
import Classes.User;
import Managers.QuestionsManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "questionEditServlet", value = "/questionEdit-servlet")
public class QuestionEdit_Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        int qid = Integer.parseInt(request.getParameter("qid"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Questions q = new Questions(-1,title,description);
        q.setId(qid);
        try {
            QuestionsManager.edit_question(q);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("personalSpace.jsp");
    }

}
