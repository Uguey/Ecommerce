package es.uc3m.tiw.g01.bank.services;

import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class ApplicationConfig extends Application {

	public Set<Class<?>> getClasses() {
        return getRestClasses();
    }
    
    private Set<Class<?>> getRestClasses() {
		Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
		
		resources.add(es.uc3m.tiw.g01.bank.services.PaymentService.class);
		resources.add(es.uc3m.tiw.g01.bank.services.ConciliationService.class);
		return resources;    
    }
}