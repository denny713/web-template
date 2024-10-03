package com.ndp.controller.view;

import com.ndp.token.JwtService;
import com.ndp.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ViewController {

    private final JwtService jwtService;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        return StringUtils.isEmpty(TokenUtil.getTokenFromCookie(request)) ? "login" : "redirect:/dashboard";
    }

    @GetMapping({"/", "/dashboard", "/index", "/home"})
    public String dashboard(HttpServletRequest request) {
        boolean passChg = (boolean) jwtService.getFromExtraClaim(TokenUtil.getToken(request), "needChangePass");
        if (passChg) {
            return "redirect:/change-pass";
        }

        return (request.getServletPath().equals("/home")
                || request.getServletPath().equals("/index")
                || request.getServletPath().equals("/"))
                ? "redirect:/dashboard" : "index";
    }

    @GetMapping("/users")
    public String usersPage(HttpServletRequest request) {
        return "user";
    }

    @GetMapping("/profile")
    public String profilePage(HttpServletRequest request) {
        return "profile";
    }

    @GetMapping("/roles")
    public String rolesPage(HttpServletRequest request) {
        return "role";
    }

    @GetMapping("/change-pass")
    public String changePassword(HttpServletRequest request,Model model) {
        model.addAttribute("username", jwtService.getUsername(TokenUtil.getToken(request)));
        return "change-pass";
    }
}
