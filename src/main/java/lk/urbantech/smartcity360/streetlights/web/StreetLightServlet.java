package lk.urbantech.smartcity360.streetlights.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.urbantech.smartcity360.streetlights.service.StreetLightService;

import java.io.IOException;

/** Thin controller for the lighting department. No pattern logic here. */
@WebServlet("/streetlights")
public class StreetLightServlet extends HttpServlet {

    private StreetLightService service;

    @Override
    public void init() {
        service = new StreetLightService();
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
        try {
            if ("run".equals(req.getParameter("action"))) {
                req.setAttribute("result", service.runBenchmark(req.getParameter("count")));
                req.setAttribute("preview", service.sample(5));
            } else {
                throw new IllegalArgumentException("Unknown action.");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/streetlights.jsp").forward(req, resp);
    }
}