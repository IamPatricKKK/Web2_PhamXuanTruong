package web2.project.ToDOList.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web2.project.ToDOList.Service.EventService;
import web2.project.ToDOList.Model.EventModel;
import web2.project.ToDOList.Model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Controller
public class CalendarController {
    
    @Autowired
    private EventService eventService;
    
    @GetMapping("/calendar")
    public String showCalendar(Model model, HttpSession session) {
        // Lấy thông tin user từ session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Lấy danh sách sự kiện của user
        UserModel user = new UserModel();
        user.setId(userId);
        List<EventModel> events = eventService.getEventsByUser(user);
        
        // In ra console để debug
        System.out.println("Number of events: " + events.size());
        for (EventModel event : events) {
            System.out.println("Event: " + event.getTitle() + ", Start: " + event.getStartTime());
        }
        
        // Convert events to JSON for JavaScript
        try {
            ObjectMapper mapper = new ObjectMapper();
            String eventsJson = mapper.writeValueAsString(events);
            model.addAttribute("events", eventsJson);
            
            // In ra console để debug
            System.out.println("Events JSON: " + eventsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "calendar";
    }
} 