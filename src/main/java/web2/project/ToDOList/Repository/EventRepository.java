package web2.project.ToDOList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web2.project.ToDOList.Model.EventModel;
import web2.project.ToDOList.Model.UserModel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {

    // Tìm tất cả sự kiện của 1 người dùng
    List<EventModel> findByUser(UserModel user);

    // Tìm tất cả sự kiện quan trọng
    List<EventModel> findByIsImportantTrue();

    // Tìm sự kiện theo khoảng thời gian bắt đầu
    List<EventModel> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Tìm sự kiện theo loại (type)
    List<EventModel> findByType_Id(Long typeId);

    // Tìm sự kiện theo người dùng và thời gian cụ thể
    List<EventModel> findByUserAndStartTimeBetween(UserModel user, LocalDateTime start, LocalDateTime end);
}
