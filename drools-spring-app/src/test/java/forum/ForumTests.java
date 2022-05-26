package forum;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.drools.core.ClockType;
import org.drools.core.event.DefaultAgendaEventListener;
import org.drools.core.time.SessionPseudoClock;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
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
import org.springframework.core.annotation.Order;

import forum.event.DislikePostEvent;
import forum.event.LikePostEvent;
import forum.event.NewPostEvent;
import forum.event.ReportPostEvent;
import forum.model.Post;
import forum.model.PostLabelEnum;
import forum.model.RulesResponse;
import forum.model.UserLabelEnum;
import forum.repository.PostRepository;
import forum.repository.UserRepository;

@SuppressWarnings("unused")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ForumTests {
	
	/***
	 * Tests are continuously called one after another.
	 * Same instances of KieSessionSigleton and Repository 
	 * classes are used thruough the entire process of testing.
	 * 
	 * Please run them all at once, and not separately!
	 * 
	 * Test coverage(by PDF): (x/25)
	 */
	
	@Test
	public void a_kieConfig() {	
		System.out.println("\n--- KieConfig");
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

	@Test
    public void b_topUserLabel() {
		System.out.println("\n--- Top user label");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	      
		for (int i = 0; i < 3; i++) {
			Post post = new Post("Zack", "Lorem Ipsum...");
			postRepository.create(post); 
			post.setPostId("topUserLabel" + i);
			kieSession.insert(post);
			kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			assertThat(ruleFireCount, equalTo(1));
			clock.advanceTime(30, TimeUnit.SECONDS);
			ruleFireCount = kieSession.fireAllRules();
		}
		
		Post post = new Post("Zack", "Lorem Ipsum...");
		postRepository.create(post); 
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(2));
		
		boolean topUserLabel = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.TOP_USER);
		assertThat(topUserLabel, equalTo(true));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(1));
		
		topUserLabel = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.TOP_USER);
		assertThat(topUserLabel, equalTo(false));
    }
	
	@Test
    public void c_harmfulPostFalse() {
		System.out.println("\n--- Harmful post: false");	
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
    public void d_harmfulPostTrue() {
		System.out.println("\n--- Harmful post: true");	
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
	
	/*
	@Test
    public void e_CommunityContributor() {
		System.out.println("\n--- Community contributor");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    clock.advanceTime(2, TimeUnit.HOURS);
	    ruleFireCount = kieSession.fireAllRules();
	    
		for (int i = 0; i < 3; i++) {
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));
			ruleFireCount = kieSession.fireAllRules();
			clock.advanceTime(60, TimeUnit.SECONDS);
			ruleFireCount = kieSession.fireAllRules();
		}

		boolean hasCommunityContributor = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.COMMUNITY_CONTRIBUTOR);
		//assertThat(hasCommunityContributor, equalTo(true));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		//assertThat(ruleFireCount, equalTo(1));
		
		hasCommunityContributor = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.COMMUNITY_CONTRIBUTOR);
		//assertThat(hasCommunityContributor, equalTo(false));
    } */
	
	@Test
    public void f_SpammerTooManyActions() {
		System.out.println("\n--- Spammer: too many actions");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    clock.advanceTime(2, TimeUnit.HOURS);
	    ruleFireCount = kieSession.fireAllRules();
	    
		for (int i = 0; i < 30; i++) {
			kieSession.insert(new LikePostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));		
		}
		
		ruleFireCount = kieSession.fireAllRules();

		boolean hasSpammer = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.SPAMMER);
		assertThat(hasSpammer, equalTo(true));
		
		boolean hasTempSuspension = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.TEMPORARILY_SUSPENDED);
		assertThat(hasTempSuspension, equalTo(true));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(1));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(1));
    }
	
	@Test
    public void g_SpammerTooManyPosts() {
		System.out.println("\n--- Spammer: too many posts");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    clock.advanceTime(2, TimeUnit.HOURS);
	    ruleFireCount = kieSession.fireAllRules();
	    
		for (int i = 0; i < 10; i++) {
			Post post = new Post("Zack", "spam content");
			postRepository.create(post); 
			post.setPostId("spam" + i);
			kieSession.insert(post);
			kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));		
		}
		
		ruleFireCount = kieSession.fireAllRules();

		boolean hasSpammer = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.SPAMMER);
		assertThat(hasSpammer, equalTo(true));
		
		boolean hasTempSuspension = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.TEMPORARILY_SUSPENDED);
		assertThat(hasTempSuspension, equalTo(true));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(1));
		
		clock.advanceTime(60, TimeUnit.SECONDS);
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(1));
    }
	
	@Test
    public void h_PotentialSpammerAndSuspension() {
		System.out.println("\n--- Spammer&Suspension: reporting good content");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    clock.advanceTime(24, TimeUnit.HOURS);
	    ruleFireCount = kieSession.fireAllRules();
	    
		for (int i = 0; i < 30; i++) {
			kieSession.insert(new ReportPostEvent("Zack", postRepository.getAllPosts().get(0).getPostId()));		
		}
		
		ruleFireCount = kieSession.fireAllRules();

		boolean hasSpammer = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.SPAMMER);
		assertThat(hasSpammer, equalTo(true));
		
		boolean hasSuspension = userRepository.getUserById("Zack").getUserLabels().contains(UserLabelEnum.SUSPENDED);
		assertThat(hasSuspension, equalTo(true));
    }
	
	@Test
    public void i_PoorContent() {
		System.out.println("\n--- Spammer&Suspension: reporting good content");	
	    UserRepository userRepository = UserRepository.getInstance();
	    PostRepository postRepository = PostRepository.getInstance();
	    KieSession kieSession = KieSessionSingleton.getInstance();		
	    SessionPseudoClock clock = kieSession.getSessionClock();
	    Integer ruleFireCount = 0;
	    
	    clock.advanceTime(24, TimeUnit.HOURS);
	    ruleFireCount = kieSession.fireAllRules();
	    
	    Post post = new Post("Angeal", "poor content");
		postRepository.create(post); 
		post.setPostId("poorContent");
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));		
	    
		for (int i = 0; i < 4; i++) {
			kieSession.insert(new LikePostEvent("Zack", post.getPostId()));		
			ruleFireCount = kieSession.fireAllRules();
		}
		
		boolean isPoorContent = post.getPostLabels().contains(PostLabelEnum.POOR_CONTENT);
		assertThat(isPoorContent, equalTo(false));
		
		for (int i = 0; i < 6; i++) {
			kieSession.insert(new DislikePostEvent("Zack", post.getPostId()));	
			ruleFireCount = kieSession.fireAllRules();
		}
			
		isPoorContent = post.getPostLabels().contains(PostLabelEnum.POOR_CONTENT);
		assertThat(isPoorContent, equalTo(true));
    }
	
}