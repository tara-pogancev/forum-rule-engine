package forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.Post;
import forum.model.User;
import forum.repository.PostRepository;
import forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private UserRepository userRepository = UserRepository.getInstance();
	
    @GetMapping()
    public List<User> getAll() {
       return userRepository.getAllUsers();
    }

}
