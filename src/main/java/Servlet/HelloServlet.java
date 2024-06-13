package Servlet;

import Classes.User;
import Managers.UserManager;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");

        ArrayList<User> users = null;
        try {
            users = UserManager.getAllUsers();  //这一行无法成功运行，浏览器直接没有内容了，报错也没有
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        out.println("<h3>" + users.size() + "</h3>");
        for (User user:users){
            out.println("<h3>" + user.getName() + "</h3>");
            out.println("<h3>" + user.getUid() + "</h3>");
        }

    }

    public void destroy() {
    }
}