package lk.urbantech.smartcity360.complaints.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.urbantech.smartcity360.complaints.pattern.Complaint;
import lk.urbantech.smartcity360.complaints.service.ComplaintService;

import java.io.IOException;

/** Thin controller for the complaints department. No pattern logic here. */
@WebServlet("/complaints")
public class ComplaintServlet extends HttpServlet {

    private ComplaintService service;

    @Override
    public void init() {
        service = new ComplaintService();
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
            if ("submit".equals(action)) {
                Complaint c = service.submit(req.getParameter("citizen"),
                        req.getParameter("description"), req.getParameter("severity"));
                String outcome = c.isHandled()
                        ? "handled by " + c.getHandledBy()
                        : "NOT handled by anyone (severity too high for every level)";
                req.setAttribute("message", c.getId() + " submitted: " + outcome + ". Path: " + c.getPathText());
            } else if ("reset".equals(action)) {
                service.reset();
                req.setAttribute("message", "Sample complaints restored.");
            } else {
                throw new IllegalArgumentException("Unknown action: " + action);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        showPage(req, resp);
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("complaints", service.getAll());
        req.getRequestDispatcher("/WEB-INF/views/complaints.jsp").forward(req, resp);
    }
}