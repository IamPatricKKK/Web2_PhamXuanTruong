package web2.project.ToDOList.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.project.ToDOList.Model.TypeEventModel;
import web2.project.ToDOList.Repository.TypeEventRepository;

import java.util.List;

@Service
public class TypeEventService {

    @Autowired
    private TypeEventRepository typeEventRepository;

    public TypeEventModel saveTypeEvent(TypeEventModel typeEvent) {
        return typeEventRepository.save(typeEvent);
    }
    
    public List<TypeEventModel> findAll() {
        return typeEventRepository.findAll();
    }

    public List<TypeEventModel> getAllTypeEvents() {
        return typeEventRepository.findAll();
    }

    public TypeEventModel getTypeEventById(Long id) {
        return typeEventRepository.findById(id).orElse(null);
    }

    public TypeEventModel getTypeEventByName(String typeName) {
        return typeEventRepository.findByTypeName(typeName);
    }

    public void deleteTypeEvent(Long id) {
        typeEventRepository.deleteById(id);
    }
}
