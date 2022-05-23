package forum.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import forum.ForumRuleEngineApp;
import forum.KieSessionSingleton;
import forum.controller.dto.PostDto;
import forum.event.DislikePostEvent;
import forum.event.LikePostEvent;
import forum.event.LikeQualityPostEvent;
import forum.event.NewPostEvent;
import forum.event.ReportPostEvent;
import forum.model.Post;
import forum.model.PostLabelEnum;
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
    	List<Post> retVal = postRepository.getAllPosts();
    	Collections.reverse(retVal);
        return retVal;
     }   
    
    public RulesResponse likePost(String userId, String postId) {   
		List<String> firedRules = new ArrayList<>();
		KieSession kieSession = KieSessionSingleton.getInstance();
		kieSession.addEventListener( new DefaultAgendaEventListener() {
			   public void afterMatchFired(AfterMatchFiredEvent event) {
			       super.afterMatchFired( event );
			       firedRules.add(event.getMatch().getRule().getName());
			   }
			});		
	    
		kieSession.insert(new LikePostEvent(userId, postId));
		if (!postRepository.getPostById(postId).getPostLabels().contains(PostLabelEnum.HARMFUL) 
				&& !postRepository.getPostById(postId).getPostLabels().contains(PostLabelEnum.POOR_CONTENT) ) {
			kieSession.insert(new LikeQualityPostEvent(userId, postId));
		}
		
		Integer ruleFireCount = kieSession.fireAllRules();		
		//log.info(ruleFireCount.toString());				
    	return new RulesResponse(firedRules);
    }
    
    public RulesResponse dislikePost(String userId, String postId) {
		List<String> firedRules = new ArrayList<>();
		KieSession kieSession = KieSessionSingleton.getInstance();
		kieSession.addEventListener( new DefaultAgendaEventListener() {
			   public void afterMatchFired(AfterMatchFiredEvent event) {
			       super.afterMatchFired( event );
			       firedRules.add(event.getMatch().getRule().getName());
			   }
			});
    	    	
		kieSession.insert(new DislikePostEvent(userId, postId));
		
		Integer ruleFireCount = kieSession.fireAllRules();		
		//log.info(ruleFireCount.toString());				
    	return new RulesResponse(firedRules);
    }

	public RulesResponse reportPost(String userId, String postId) {
		List<String> firedRules = new ArrayList<>();
		KieSession kieSession = KieSessionSingleton.getInstance();
		kieSession.addEventListener( new DefaultAgendaEventListener() {
			   public void afterMatchFired(AfterMatchFiredEvent event) {
			       super.afterMatchFired( event );
			       firedRules.add(event.getMatch().getRule().getName());
			   }
			});
		    	
		kieSession.insert(new ReportPostEvent(userId, postId));
		
		Integer ruleFireCount = kieSession.fireAllRules();		
		//log.info(ruleFireCount.toString());				
    	return new RulesResponse(firedRules);
	}

	public RulesResponse create(PostDto newPost) {		
		List<String> firedRules = new ArrayList<>();
		KieSession kieSession = KieSessionSingleton.getInstance();
		kieSession.addEventListener( new DefaultAgendaEventListener() {
			   public void afterMatchFired(AfterMatchFiredEvent event) {
			       super.afterMatchFired( event );
			       firedRules.add(event.getMatch().getRule().getName());
			   }
			});
				
		Post post = new Post(newPost.postOwnerId, newPost.postContent);
		postRepository.create(post);    	
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId()));
		
		Integer ruleFireCount = kieSession.fireAllRules();		
		//log.info(ruleFireCount.toString());				
    	return new RulesResponse(firedRules);
	}

}
