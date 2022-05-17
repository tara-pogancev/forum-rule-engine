package forum;

import org.drools.core.ClockType;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
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
	public KieSessionSingleton(KieBase kbase) {
        KieServices ks = KieServices.Factory.get();
		KieSessionConfiguration ksconf1 = ks.newKieSessionConfiguration();
        ksconf1.setOption(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));
		KieSessionSingleton.instance = kbase.newKieSession(ksconf1, null);
	}
	
	public static void removeListeners() {
		for (AgendaEventListener listener: KieSessionSingleton.instance.getAgendaEventListeners()) {
			KieSessionSingleton.instance.removeEventListener(listener);
		}
	}


}
