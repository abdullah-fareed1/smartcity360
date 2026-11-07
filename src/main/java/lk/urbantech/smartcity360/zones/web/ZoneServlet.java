package lk.urbantech.smartcity360.zones.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.urbantech.smartcity360.zones.service.ZoneService;

import java.io.IOException;

/**
 * Thin controller for the zones department. No pattern logic here.
 */
@WebServlet("/zones")
public class ZoneServlet extends HttpServlet {

    private ZoneService service;

    @Override
    public void init() {
        service = new ZoneService();
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
        String target = req.getParameter("target");

        try {
            if (action == null) {
                throw new IllegalArgumentException("No action given.");
            }
            switch (action) {
                case "shutdown" -> req.setAttribute("message", service.shutdown(target));
                case "restore"  -> req.setAttribute("message", service.restore(target));
                case "report"   -> req.setAttribute("message", service.report(target));
                case "reset"    -> {
                    service.reset();
                    req.setAttribute("message", "Sample data restored.");
                }
                default -> throw new IllegalArgumentException("Unknown action: " + action);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.setAttribute("selected", target);
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("treeText", service.getTreeText());
        req.setAttribute("nodeNames", service.getNodeNames());
        req.setAttribute("totalUsage", service.usageOf("Colombo City"));
        req.getRequestDispatcher("/WEB-INF/views/zones.jsp").forward(req, resp);
    }
}