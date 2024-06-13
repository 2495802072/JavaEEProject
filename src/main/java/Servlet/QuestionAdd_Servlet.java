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

@WebServlet(name = "questionAddServlet", value = "/questionAdd-servlet")
public class QuestionAdd_Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        User user = (User) request.getSession().getAttribute("user");
        Questions Q = new Questions(user.getUid(), title, description);

        try {
            QuestionsManager.add_question(Q);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("index.jsp");
    }

}
