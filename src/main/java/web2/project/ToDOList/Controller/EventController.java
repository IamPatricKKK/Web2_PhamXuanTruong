package web2.project.ToDOList.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web2.project.ToDOList.Model.EventModel;
import web2.project.ToDOList.Model.UserModel;
import web2.project.ToDOList.Service.EventService;
import web2.project.ToDOList.Service.TypeEventService;
import web2.project.ToDOList.Service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TypeEventService typeEventService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(
    		HttpSession session,
            Model model,
            @RequestParam(value = "typeId", required = false) Long typeId,
            @RequestParam(value = "isImportant", required = false) Boolean isImportant,
            @RequestParam(value = "sortBy", required = false, defaultValue = "newest") String sortBy,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
            @RequestParam(value = "keyword", required = false) String keyword
    		) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Lấy thông tin user
        UserModel currentUser = userService.findById(userId);
        if (currentUser == null) {
            session.invalidate();
            return "redirect:/login";
        }
        
        // Gọi service lấy sự kiện theo filter
        List<EventModel> events = eventService.filterEvents(
                currentUser, typeId, isImportant, startDate, endDate, keyword, sortBy);

        model.addAttribute("eventTypes", typeEventService.findAll());
        model.addAttribute("events", events);

        // Giữ lại giá trị filter để hiển thị trên UI
        model.addAttribute("typeId", typeId);
        model.addAttribute("isImportant", isImportant);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("startDate", startDate != null ? startDate.toLocalDate().toString() : "");
        model.addAttribute("endDate", endDate != null ? endDate.toLocalDate().toString() : "");
        model.addAttribute("keyword", keyword);
        
        return "events/dashboard";
    }

    @GetMapping("/create")
    public String showCreateForm(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("event", new EventModel());
        model.addAttribute("eventTypes", typeEventService.findAll());
        return "events/form";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute EventModel event, HttpSession session) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Set user cho event
        UserModel currentUser = userService.findById(userId);
        if (currentUser == null) {
            session.invalidate();
            return "redirect:/login";
        }
        
        event.setUser(currentUser);
        eventService.saveEvent(event);
        return "redirect:/events/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Kiểm tra quyền chỉnh sửa
        EventModel event = eventService.getEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (!event.getUser().getId().equals(userId)) {
            return "redirect:/events/dashboard";
        }

        model.addAttribute("event", event);
        model.addAttribute("eventTypes", typeEventService.findAll());
        return "events/form";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute EventModel event, HttpSession session) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Kiểm tra quyền chỉnh sửa
        EventModel existingEvent = eventService.getEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (!existingEvent.getUser().getId().equals(userId)) {
            return "redirect:/events/dashboard";
        }

        // Cập nhật thông tin event
        event.setId(id);
        event.setUser(existingEvent.getUser());
        eventService.saveEvent(event);
        return "redirect:/events/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, HttpSession session) {
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Kiểm tra quyền xóa
        EventModel event = eventService.getEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (!event.getUser().getId().equals(userId)) {
            return "redirect:/events/dashboard";
        }

        eventService.deleteEvent(id);
        return "redirect:/events/dashboard";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<EventModel> filterEvents(
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            HttpSession session) {
        
        // Kiểm tra đăng nhập
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return List.of();
        }

        UserModel currentUser = userService.findById(userId);
        if (currentUser == null) {
            return List.of();
        }

        if (typeId != null) {
            return eventService.getEventsByType(typeId);
        } else if (start != null && end != null) {
            return eventService.getUserEventsBetween(currentUser, start, end);
        }
        
        return eventService.getEventsByUser(currentUser);
    }
}
