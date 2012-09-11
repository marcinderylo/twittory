package eu.piotrbuda.twittory;

import com.sun.jersey.api.core.PackagesResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * .
 */
@ApplicationPath("/api/*")
public class TwittoryApplication extends PackagesResourceConfig {
    public TwittoryApplication() {
        super("eu.piotrbuda.twittory");
    }
}
