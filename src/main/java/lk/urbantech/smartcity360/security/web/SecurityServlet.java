package lk.urbantech.smartcity360.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.urbantech.smartcity360.security.service.SecurityService;

import java.io.IOException;

/** Thin controller for the security department. No pattern logic here. */
@WebServlet("/security")
public class SecurityServlet extends HttpServlet {

    private SecurityService service;

    @Override
    public void init() {
        service = new SecurityService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        showPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("send".equals(action)) {
                boolean auth = req.getParameter("auth") != null;          // checked boxes are sent
                boolean compress = req.getParameter("compress") != null;
                boolean encrypt = req.getParameter("encrypt") != null;
                req.setAttribute("result", service.send(auth, compress, encrypt, req.getParameter("apiKey")));
            } else if ("compare".equals(action)) {
                req.setAttribute("comparison", service.compareOrder());
            } else {
                throw new IllegalArgumentException("Unknown action: " + action);
            }
        } catch (SecurityException e) {
            req.setAttribute("error", "Rejected: " + e.getMessage() + ". Nothing was sent.");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/security.jsp").forward(req, resp);
    }
}