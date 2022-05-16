package forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.Post;
import forum.model.User;
import forum.repository.UserRepository;
import forum.service.PostService;
import forum.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	

	@Autowired
	private UserService userService;
	
    @GetMapping()
    public List<User> getAll() {
       return userService.getAllUsers();
    }

}
