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
import forum.event.NewPostEvent;
import forum.event.ReportPostEvent;
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
	
	public Post getById(String postId) {
		return postRepository.getPostById(postId);
	}
	
    public List<Post> getAllPosts() {
        return postRepository.getAllPosts();
     }   
    
    public RulesResponse likePost(String userId, String postId) {
    	RulesResponse retVal = new RulesResponse("");

		KieSession kieSession = KieSessionSingleton.getInstance();
    	
		kieSession.insert(new LikePostEvent(userId, postId));
		Integer ruleFireCount = kieSession.fireAllRules();
		
		log.info(ruleFireCount.toString());

    	return retVal;
    }
    
    public RulesResponse dislikePost(String userId, String postId) {
    	RulesResponse retVal = new RulesResponse("");

		KieSession kieSession = KieSessionSingleton.getInstance();
    	
		kieSession.insert(new DislikePostEvent(userId, postId));
		Integer ruleFireCount = kieSession.fireAllRules();
		
		log.info(ruleFireCount.toString());
		
    	return retVal;
    }

	public RulesResponse reportPost(String userId, String postId) {
    	RulesResponse retVal = new RulesResponse("");

		KieSession kieSession = KieSessionSingleton.getInstance();
    	
		kieSession.insert(new ReportPostEvent(userId, postId));
		Integer ruleFireCount = kieSession.fireAllRules();
		
		log.info(ruleFireCount.toString());
		
    	return retVal;
	}

	public RulesResponse create(Post newPost) {
    	RulesResponse retVal = new RulesResponse("");

		KieSession kieSession = KieSessionSingleton.getInstance();
		
		Post post = new Post(newPost.getPostOwnerId(), newPost.getPostContent());
		postRepository.create(post);    	
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId()));
		
		Integer ruleFireCount = kieSession.fireAllRules();
		
		log.info(ruleFireCount.toString());
		
    	return retVal;
	}

}
