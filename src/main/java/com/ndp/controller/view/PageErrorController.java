package com.ndp.controller.view;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode != null) {
            return switch (statusCode) {
                case 403 -> errorReturn(model, "403 - Forbidden", "You don't have permission to access this page.");
                case 404 -> errorReturn(model, "404 - Not Found", "The page you're looking for doesn't exist.");
                case 500 -> errorReturn(model, "500 - Internal Server Error", "Something went wrong on the server.");
                default -> errorReturn(model, "Error", "An unexpected error has occurred.");
            };
        }

        return errorReturn(model, "Error", "An unexpected error has occurred.");
    }

    private String errorReturn(Model model, String title, String desc) {
        model.addAttribute("title", title);
        model.addAttribute("desc", desc);
        return "error";
    }
}
