package eu.piotrbuda.twittory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import javax.servlet.annotation.WebListener;

@WebListener
public class GuiceConfigServletListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new JerseyServletModule() {
            @Override
            protected void configureServlets() {
                bind(Twitter.class).toInstance(new TwitterFactory().getInstance());
                serve("/*").with(GuiceContainer.class);
            }
        });
    }
}
