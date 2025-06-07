package web2.project.ToDOList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web2.project.ToDOList.Model.TypeEventModel;

@Repository
public interface TypeEventRepository extends JpaRepository<TypeEventModel, Long> {
    TypeEventModel findByTypeName(String TypeName);
}
