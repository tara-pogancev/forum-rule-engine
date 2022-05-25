package forum.service;

import java.util.ArrayList;
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
import forum.event.DislikePostEvent;
import forum.model.RulesResponse;
import forum.model.User;
import forum.repository.PostRepository;
import forum.repository.UserRepository;

@Service
@SuppressWarnings("unused")
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

	public User getById(String userId) {
		return userRepository.getUserById(userId);
	}

	public RulesResponse refresh() {
		List<String> firedRules = new ArrayList<>();
		KieSession kieSession = KieSessionSingleton.getInstance();
		kieSession.addEventListener( new DefaultAgendaEventListener() {
			   public void afterMatchFired(AfterMatchFiredEvent event) {
			       super.afterMatchFired( event );
			       firedRules.add(event.getMatch().getRule().getName());
			   }
			});    	    	
		
		Integer ruleFireCount = kieSession.fireAllRules();		
		//log.info(ruleFireCount.toString());				
    	return new RulesResponse(firedRules);
	}    

}
