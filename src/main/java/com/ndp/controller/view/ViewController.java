package com.ndp.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        return render(model, "login :: login", true);
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        return render(model, "dashboard :: dashboard", false);
    }

    private String render(Model model, String component, boolean isLogin) {
        model.addAttribute("view", component);
        return isLogin ? "layout/sign-layout" : "layout/base-layout";
    }
}
