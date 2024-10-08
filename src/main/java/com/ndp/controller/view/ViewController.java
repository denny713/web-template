package com.ndp.controller.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ndp.model.dto.response.RoleMappingResponseDto;
import com.ndp.service.MenuService;
import com.ndp.token.JwtService;
import com.ndp.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ViewController {

    private final JwtService jwtService;
    private final MenuService menuService;

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

        String path = request.getServletPath();
        return (path.equals("/home") || path.equals("/index") || path.equals("/")) ? "redirect:/dashboard" : "index";
    }

    @GetMapping("/users")
    public String usersPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return render(request, response, model, "user");
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

    @GetMapping("/roles")
    public String rolesPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        return render(request, response, model, "role");
    }

    @GetMapping("/change-pass")
    public String changePassword(HttpServletRequest request, Model model) {
        model.addAttribute("username", jwtService.getUsername(TokenUtil.getToken(request)));
        return "change-pass";
    }

    private String render(HttpServletRequest request, HttpServletResponse response, Model model, String view) {
        String token = TokenUtil.getToken(request);
        String path = request.getServletPath();
        if (!checkAvailableMenu(path)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "redirect:/error/404";
        }

        if (!checkMenuAccess(token, path)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/error/403";
        }

        model.addAttribute("view", checkAccess(token, path, "view"));
        model.addAttribute("create", checkAccess(token, path, "create"));
        model.addAttribute("edit", checkAccess(token, path, "edit"));
        model.addAttribute("delete", checkAccess(token, path, "delete"));
        return view;
    }

    private boolean checkAvailableMenu(String path) {
        List<Map<String, String>> mapList = (List<Map<String, String>>) menuService.getMenusToOptions().getData();

        for (Map<String, String> map : mapList) {
            String url = map.get("value");
            if (url != null && url.equals(path)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMenuAccess(String token, String path) {
        RoleMappingResponseDto mapping = getRoleMappingList(token).stream()
                .filter(x -> x.getMenuUrl().equals(path))
                .findFirst().orElse(null);
        return mapping != null;
    }

    private boolean checkAccess(String token, String path, String view) {
        RoleMappingResponseDto mapping = getRoleMappingList(token).stream()
                .filter(x -> x.getMenuUrl().equals(path))
                .findFirst().orElse(null);

        if (mapping == null) {
            return false;
        }

        return switch (view.toLowerCase()) {
            case "create" -> mapping.isCreate();
            case "edit" -> mapping.isEdit();
            case "delete" -> mapping.isDelete();
            case "view" -> mapping.isView();
            default -> false;
        };
    }

    private List<RoleMappingResponseDto> getRoleMappingList(String token) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String mapList = new Gson().toJson(jwtService.getFromExtraClaim(token, "roleMapping"));
            return mapper.readValue(mapList, new TypeReference<>() {
            });
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
