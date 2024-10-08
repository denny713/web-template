package com.ndp.controller.view;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class PageErrorController implements ErrorController {

    @GetMapping("/")
    public String internalServerError(Model model) {
        return errorReturn(model, "500 - Internal Server Error", "Something went wrong on the server.");
    }

    @GetMapping("/404")
    public String notFoundError(Model model) {
        return errorReturn(model, "404 - Not Found", "The page you're looking for doesn't exist.");
    }

    @GetMapping("/403")
    public String forbiddenError(Model model) {
        return errorReturn(model, "403 - Forbidden", "You don't have permission to access this page.");
    }

    private String errorReturn(Model model, String title, String desc) {
        model.addAttribute("title", title);
        model.addAttribute("desc", desc);
        return "error";
    }
}
