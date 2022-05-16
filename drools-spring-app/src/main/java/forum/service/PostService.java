package forum.service;

import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import forum.ForumRuleEngineApp;
import forum.KieSessionSingleton;
import forum.event.DislikePostEvent;
import forum.event.LikePostEvent;
import forum.model.Post;
import forum.model.RulesResponse;
import forum.repository.PostRepository;
import forum.repository.UserRepository;

@Service
public class PostService {
	
	private static Logger log = LoggerFactory.getLogger(ForumRuleEngineApp.class);

	private final KieContainer kieContainer;

	private PostRepository postRepository = PostRepository.getInstance();
	private UserRepository userRepository = UserRepository.getInstance();
	
	@Autowired
	public PostService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}
	
	public Post getClassifiedPost(Post post) {
		post = postRepository.getAllPosts().get(0);
		
		KieSession kieSession = kieContainer.newKieSession();
		kieSession.insert(post);
		
		log.info("Firing all rules.");
		kieSession.fireAllRules();
		kieSession.dispose();
		
		log.info(post.toString());
		
		postRepository.updatePost(post);
		return post;
	}
	
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
     }    
    
    public RulesResponse dislikePost(String postId, String userId) {
    	RulesResponse retVal = new RulesResponse("");

		KieSession kieSession = KieSessionSingleton.getInstance();
    	
		kieSession.insert(new DislikePostEvent(userId, postId));
		Integer ruleFireCount = kieSession.fireAllRules();
		
		log.info(ruleFireCount.toString());

    	return retVal;
    }
	

}
