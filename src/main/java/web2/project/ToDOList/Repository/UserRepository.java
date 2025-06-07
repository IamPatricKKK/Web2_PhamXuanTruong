package web2.project.ToDOList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web2.project.ToDOList.Model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
	UserModel findByUserName(String userName);
	boolean existsByUserName(String userName);
} 