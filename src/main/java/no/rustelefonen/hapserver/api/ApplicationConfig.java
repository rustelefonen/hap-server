package no.rustelefonen.hapserver.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lon on 05/04/16.
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {
    private Set<Class<?>> classes;

    public ApplicationConfig(){
        classes = new HashSet<>();
        classes.add(InfoService.class);
        classes.add(ResearchService.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
