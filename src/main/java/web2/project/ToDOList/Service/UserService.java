package web2.project.ToDOList.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.project.ToDOList.Model.UserModel;
import web2.project.ToDOList.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
