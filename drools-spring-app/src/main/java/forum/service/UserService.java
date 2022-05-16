package forum.service;

import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import forum.ForumRuleEngineApp;
import forum.model.Post;
import forum.model.User;
import forum.repository.PostRepository;
import forum.repository.UserRepository;

@Service
public class UserService {
	
	private static Logger log = LoggerFactory.getLogger(ForumRuleEngineApp.class);

	private final KieContainer kieContainer;
	
	private PostRepository postRepository = PostRepository.getInstance();
	private UserRepository userRepository = UserRepository.getInstance();
	
	@Autowired
	public UserService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}
	
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
     }    

}
