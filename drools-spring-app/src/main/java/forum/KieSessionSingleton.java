package forum;

import org.kie.api.KieBase;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KieSessionSingleton {
	
	private static KieSession instance;	

	public static KieSession getInstance() {
		removeListeners();
		return instance;
	}
	
	@Autowired
	public KieSessionSingleton(KieBase kbase, KieSessionConfiguration ksconf) {
		KieSessionSingleton.instance = kbase.newKieSession(ksconf, null);
	}
	
	public static void removeListeners() {
		for (AgendaEventListener listener: KieSessionSingleton.instance.getAgendaEventListeners()) {
			KieSessionSingleton.instance.removeEventListener(listener);
		}
	}

}
