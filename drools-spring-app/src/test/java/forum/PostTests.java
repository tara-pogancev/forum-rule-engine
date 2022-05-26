package forum;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.drools.core.ClockType;
import org.drools.core.event.DefaultAgendaEventListener;
import org.drools.core.time.SessionPseudoClock;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.io.ResourceFactory;

import forum.event.DislikePostEvent;
import forum.event.LikePostEvent;
import forum.event.NewPostEvent;
import forum.event.ReportPostEvent;
import forum.model.Post;
import forum.model.RulesResponse;
import forum.model.UserLabelEnum;
import forum.repository.PostRepository;
import forum.repository.UserRepository;

@SuppressWarnings("unused")
public class PostTests {
	
	@Test
	public void kieConfig() {	
		System.out.println("--------------------------------------------");
		KieServices ks = KieServices.Factory.get();		
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("forum", "drools-spring-kjar", "0.0.1-SNAPSHOT"));		
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
		kbconf.setOption(EventProcessingOption.STREAM);
		KieBase kbase = kContainer.newKieBase(kbconf);
		KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
		ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
		new KieSessionSingleton(kbase, ksconf);
			
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
		assertThat(kieSession.getFactCount(), equalTo(10L));
	}		
/*
	@Test
    public void topUserLabel() {
		System.out.println("--------------------------------------------");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	      
		for (int i = 0; i < 3; i++) {
			Post post = new Post("Angeal", "Lorem Ipsum...");
			postRepository.create(post); 
			post.setPostId("topUserLabel" + i);
			kieSession.insert(post);
			kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			assertThat(ruleFireCount, equalTo(1));
			ruleFireCount = kieSession.fireAllRules();
		}
		
		Post post = new Post("Angeal", "Lorem Ipsum...");
		postRepository.create(post); 
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(2));
    }
	*/
	@Test
    public void harmfulPostFalse() {
		System.out.println("--------------------------------------------");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    Post post = new Post("Angeal", "good content");
		postRepository.create(post); 
		post.setPostId("harmfulPostFalse");
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
	      
		for (int i = 0; i < 9; i++) {
			kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			assertThat(ruleFireCount, equalTo(1));
		}
		
		kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(3));
    }
	
	@Test
    public void harmfulPostTrue() {
		System.out.println("--------------------------------------------");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    Post post = new Post("Angeal", "harm!!!");
		postRepository.create(post); 
		post.setPostId("harmfulPostTrue");
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
	      
		for (int i = 0; i < 9; i++) {
			kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			assertThat(ruleFireCount, equalTo(1));
		}
		
		kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(5));
		
		boolean hasHarmfulLabel = userRepository.getUserById("Angeal").getUserLabels().contains(UserLabelEnum.HARMFUL_USER);
		assertThat(hasHarmfulLabel, equalTo(true));
    }
	
}