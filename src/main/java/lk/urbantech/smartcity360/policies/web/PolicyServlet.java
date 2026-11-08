package lk.urbantech.smartcity360.policies.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.urbantech.smartcity360.policies.service.PolicyService;

import java.io.IOException;

/** Thin controller for the policy department. No pattern logic here. */
@WebServlet("/policies")
public class PolicyServlet extends HttpServlet {

    private PolicyService service;

    @Override
    public void init() {
        service = new PolicyService();
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
        String noise = req.getParameter("noise");
        String hour = req.getParameter("hour");
        String vehicle = req.getParameter("vehicle");
        String ruleText = req.getParameter("ruleText");

        try {
            if (action == null) {
                throw new IllegalArgumentException("No action given.");
            }
            switch (action) {
                case "evaluate" -> req.setAttribute("evaluation", service.evaluate(ruleText, noise, hour, vehicle));
                case "checkall" -> {
                    req.setAttribute("checks", service.checkAll(noise, hour, vehicle));
                    req.setAttribute("message", "Checked every saved rule against the situation.");
                }
                case "add" -> {
                    String name = req.getParameter("ruleName");
                    service.addRule(name, ruleText);
                    req.setAttribute("message", "Rule '" + name.trim() + "' saved. It is active immediately, with no restart.");
                }
                case "remove" -> {
                    service.removeRule(req.getParameter("name"));
                    req.setAttribute("message", "Rule removed.");
                }
                case "reset" -> {
                    service.reset();
                    req.setAttribute("message", "Sample rules restored.");
                }
                default -> throw new IllegalArgumentException("Unknown action: " + action);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("rules", service.getRules());
        req.getRequestDispatcher("/WEB-INF/views/policies.jsp").forward(req, resp);
    }
}