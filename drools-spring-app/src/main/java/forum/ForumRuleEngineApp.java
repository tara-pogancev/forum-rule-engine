package forum;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import forum.repository.PostRepository;
import forum.repository.UserRepository;

@SpringBootApplication
public class ForumRuleEngineApp {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ForumRuleEngineApp.class, args);
	}

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();		
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("forum", "drools-spring-kjar", "0.0.1-SNAPSHOT"));		
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		new KieSessionSingleton(kContainer);
		return kContainer;
	}
	
	
}
