package forum;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KieSessionSingleton {
	
	private static KieSession instance;	

	public static KieSession getInstance() {
		return instance;
	}
	
	@Autowired
	public KieSessionSingleton(KieContainer kieContainer) {
		KieSessionSingleton.instance = kieContainer.newKieSession();
	}


}
