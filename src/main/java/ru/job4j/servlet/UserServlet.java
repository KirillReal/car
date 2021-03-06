package ru.job4j.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String page = req.getParameter("page");
        req.getRequestDispatcher(page + ".jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserService service = UserService.getInstance();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        if (!service.execute(req)) {
            throw new IllegalStateException("Ошибка при выполнении операции!");
        }
        writer.write("Операция выполнена успешно!");
        writer.flush();
    }
}
