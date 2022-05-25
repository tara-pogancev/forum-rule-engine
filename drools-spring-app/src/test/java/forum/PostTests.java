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
import forum.repository.PostRepository;
import forum.repository.UserRepository;

public class PostTests {
	
	private KieSession getKieSession() {
		System.out.println("Initting...");
		KieServices ks = KieServices.Factory.get();		
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("forum", "drools-spring-kjar", "0.0.1-SNAPSHOT"));		
//        KieFileSystem kfs = ks.newKieFileSystem();
//		KieBuilder kbuilder = ks.newKieBuilder(kfs);
//        kbuilder.buildAll();
//        if (kbuilder.getResults().hasMessages(Message.Level.ERROR)) {
//            throw new IllegalArgumentException("Coudln't build knowledge module" + kbuilder.getResults());
//        }
//        KieContainer kContainer = ks.newKieContainer(kbuilder.getKieModule().getReleaseId());
//		KieScanner kScanner = ks.newKieScanner(kContainer);
//		kScanner.start(10_000);
		KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
		kbconf.setOption(EventProcessingOption.STREAM);
		KieBase kbase = kContainer.newKieBase(kbconf);
		KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
		new KieSessionSingleton(kbase, ksconf);
		return KieSessionSingleton.getInstance();
		
	}
	
	
    @Test
    public void harmfulPostWithAnalisysTest() {
    	KieSession kieSession = getKieSession();
    	SessionPseudoClock clock = kieSession.getSessionClock();
    	Integer ruleFireCount = 0;
    	
    	System.out.println("--------------------------------------------------");
		
    	UserRepository userRepository = UserRepository.getInstance();
    	PostRepository postRepository = PostRepository.getInstance();

		Post post = new Post("Zack", "harm");
		//postRepository.create(post); 
		kieSession.insert(post);
		kieSession.insert(new NewPostEvent(post.getPostOwnerId(), post.getPostId()));
		kieSession.fireAllRules();
		clock.advanceTime(1, TimeUnit.SECONDS);
		
		for (int i = 0; i < 9; i++) {
			System.out.println("hi1");
			kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
			kieSession.fireAllRules();
			System.out.println("hi2");
			clock.advanceTime(1, TimeUnit.SECONDS);
		}

		kieSession.insert(new ReportPostEvent("Zack", post.getPostId()));
		ruleFireCount = kieSession.fireAllRules();
		assertThat(ruleFireCount, equalTo(5));
		
		kieSession.destroy();
    }
    

}
