package web2.project.ToDOList.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.project.ToDOList.Model.EventModel;
import web2.project.ToDOList.Model.UserModel;
import web2.project.ToDOList.Repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Lấy tất cả sự kiện
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    // Lấy sự kiện theo ID
    public Optional<EventModel> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Lấy tất cả sự kiện của một người dùng
    public List<EventModel> getEventsByUser(UserModel user) {
        return eventRepository.findByUser(user);
    }

    // Lấy các sự kiện quan trọng
    public List<EventModel> getImportantEvents() {
        return eventRepository.findByIsImportantTrue();
    }

    // Lấy sự kiện theo khoảng thời gian
    public List<EventModel> getEventsBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByStartTimeBetween(start, end);
    }

    // Lấy sự kiện theo người dùng và khoảng thời gian
    public List<EventModel> getUserEventsBetween(UserModel user, LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByUserAndStartTimeBetween(user, start, end);
    }

    // Lấy sự kiện theo loại
    public List<EventModel> getEventsByType(Long typeId) {
        return eventRepository.findByType_Id(typeId);
    }

    // Tạo hoặc cập nhật sự kiện
    public EventModel saveEvent(EventModel event) {
        return eventRepository.save(event);
    }

    // Xoá sự kiện theo ID
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    
 // Lọc sự kiện theo nhiều điều kiện
    public List<EventModel> filterEvents(UserModel user, Long typeId, Boolean isImportant,
                                         LocalDateTime startDate, LocalDateTime endDate,
                                         String keyword, String sortBy) {
        List<EventModel> events = eventRepository.findByUser(user);

        // Lọc theo loại sự kiện
        if (typeId != null) {
            events.removeIf(e -> e.getType() == null || !typeId.equals(e.getType().getId()));
        }

        // Lọc theo độ quan trọng
        if (isImportant != null && isImportant) {
            events.removeIf(e -> !Boolean.TRUE.equals(e.getIsImportant()));
        }

        // Lọc theo khoảng thời gian
        if (startDate != null && endDate != null) {
            events.removeIf(e -> e.getStartTime().isBefore(startDate) || e.getStartTime().isAfter(endDate));
        }

        // Lọc theo từ khóa (tên sự kiện chứa từ khóa)
        if (keyword != null && !keyword.trim().isEmpty()) {
            events.removeIf(e -> e.getTitle() == null || !e.getTitle().toLowerCase().contains(keyword.toLowerCase()));
        }

        // Sắp xếp
        if (sortBy != null) {
            switch (sortBy) {
                case "oldest":
                    events.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));
                    break;
                case "name":
                    events.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                    break;
                default: // newest
                    events.sort((a, b) -> b.getStartTime().compareTo(a.getStartTime()));
                    break;
            }
        }

        return events;
    }

    
    
}
