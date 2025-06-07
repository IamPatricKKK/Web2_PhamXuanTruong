package web2.project.ToDOList.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2.project.ToDOList.Model.UserModel;
import web2.project.ToDOList.Service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

	/*
	 * @GetMapping("/register") public String showRegistrationForm(Model model) {
	 * model.addAttribute("user", new UserModel()); return "register"; }
	 */

	/*
	 * @PostMapping("/register") public String registerUser(@ModelAttribute("user")
	 * UserModel user) { userService.save(user); return "redirect:/login"; }
	 */

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        UserModel user = userService.findById(userId);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserModel updatedUser, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        UserModel currentUser = userService.findById(userId);
        if (currentUser != null) {
            // Cập nhật thông tin
            currentUser.setFullName(updatedUser.getFullName());
            currentUser.setEmail(updatedUser.getEmail());
            
            // Chỉ cập nhật mật khẩu nếu có nhập mật khẩu mới
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                currentUser.setPassword(updatedUser.getPassword());
            }
            
            userService.save(currentUser);
            redirectAttributes.addFlashAttribute("message", "Thông tin cá nhân đã được cập nhật");
        }
        
        return "redirect:/users/profile";
    }
}
