package web2.project.ToDOList.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web2.project.ToDOList.Model.TypeEventModel;
import web2.project.ToDOList.Service.TypeEventService;

import java.util.List;

@RestController
@RequestMapping("/api/type-events")
public class TypeEventController {

    @Autowired
    private TypeEventService typeEventService;

    // Lấy tất cả type events
    @GetMapping
    public List<TypeEventModel> getAllTypeEvents() {
        return typeEventService.getAllTypeEvents();
    }

    // Lấy type event theo id
    @GetMapping("/{id}")
    public ResponseEntity<TypeEventModel> getTypeEventById(@PathVariable Long id) {
        TypeEventModel typeEvent = typeEventService.getTypeEventById(id);
        if (typeEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(typeEvent);
    }

    // Tạo mới type event
    @PostMapping
    public ResponseEntity<TypeEventModel> createTypeEvent(@RequestBody TypeEventModel typeEvent) {
        if (typeEvent.getTypeName() == null || typeEvent.getTypeName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        TypeEventModel savedTypeEvent = typeEventService.saveTypeEvent(typeEvent);
        return ResponseEntity.ok(savedTypeEvent);
    }

    // Cập nhật type event
    @PutMapping("/{id}")
    public ResponseEntity<TypeEventModel> updateTypeEvent(@PathVariable Long id, @RequestBody TypeEventModel updatedTypeEvent) {
        TypeEventModel existingTypeEvent = typeEventService.getTypeEventById(id);
        if (existingTypeEvent == null) {
            return ResponseEntity.notFound().build();
        }

        existingTypeEvent.setTypeName(updatedTypeEvent.getTypeName());
        existingTypeEvent.setNote(updatedTypeEvent.getNote());
        existingTypeEvent.setColor(updatedTypeEvent.getColor());

        TypeEventModel savedTypeEvent = typeEventService.saveTypeEvent(existingTypeEvent);
        return ResponseEntity.ok(savedTypeEvent);
    }

    // Xóa type event
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeEvent(@PathVariable Long id) {
        TypeEventModel existingTypeEvent = typeEventService.getTypeEventById(id);
        if (existingTypeEvent == null) {
            return ResponseEntity.notFound().build();
        }
        typeEventService.deleteTypeEvent(id);
        return ResponseEntity.noContent().build();
    }
}
