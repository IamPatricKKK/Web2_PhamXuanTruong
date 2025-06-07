package web2.project.ToDOList.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web2.project.ToDOList.Model.UserModel;
import web2.project.ToDOList.Service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // tên view login.html
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        UserModel user = userService.findByUserName(username);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUserName());
            session.setAttribute("fullName", user.getFullName());
            return "redirect:/events/dashboard";  // trang sau khi đăng nhập thành công
        }

        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
        return "login";
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserModel user, Model model) {
        if (userService.existsByUserName(user.getUserName())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "register";
        }

        userService.save(user);
        return "redirect:/login";
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
