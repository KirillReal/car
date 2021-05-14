package ru.job4j.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.User;
import ru.job4j.model.Ads;
import ru.job4j.service.AdsService;
import ru.job4j.store.UserStore;
import ru.job4j.store.Store;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/ads")
public class AdsServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AdsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        StringBuilder url = new StringBuilder("ads/");
        String page = req.getParameter("page");
        url.append(page);
        url.append(".jsp");
        String id = req.getParameter("id");
        if (id != null) {
            Store store = UserStore.instOf();
            Ads ads = store.findAdsById(Integer.parseInt(id));
            req.setAttribute("ads", ads);
            HttpSession session = req.getSession();
            User currentUser = (User) session.getAttribute("user");
            req.setAttribute("user", currentUser);
        }
        req.getRequestDispatcher(url.toString()).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AdsService service = AdsService.getInstance();
        req.setCharacterEncoding("UTF-8");
        Optional<String> rsl = service.execute(req);
        if (rsl.isEmpty()) {
            throw new IllegalStateException("Ошибка при выполнении операции!");
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.write(rsl.get());
        writer.flush();
    }
}
