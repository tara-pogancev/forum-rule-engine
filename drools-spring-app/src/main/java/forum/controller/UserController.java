package forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.model.RulesResponse;
import forum.model.User;
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
    
    @GetMapping("/{userId}")
    public User getById(@PathVariable String userId) {
       return userService.getById(userId);
    }
    
    @GetMapping("/refresh")
    public RulesResponse refresh() {
        return userService.refresh();
     }

}
