package com.dastan;

import com.dastan.health.TemplateHealthCheck;
import com.dastan.repository.JsonFileRulesRepository;
import com.dastan.resources.HelloWorldResource;
import com.dastan.resources.RulesResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardDemoApplication extends Application<AppConfiguration> {

    public static final Logger LOGGER = LoggerFactory.getLogger(DropwizardDemoApplication.class);

    public static void main(final String[] args) throws Exception {
        new DropwizardDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(final AppConfiguration configuration, final Environment environment)
            throws Exception {

        LOGGER.info("Application name: {}", configuration.getAppName());
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        final JsonFileRulesRepository jsonFileRulesRepository = new JsonFileRulesRepository();

        final RulesResource rulesResource = new RulesResource(jsonFileRulesRepository);
        environment.jersey().register(rulesResource);

        final HelloWorldResource helloWorldResource = new HelloWorldResource(configuration.getTemplate(), configuration.getAppName());
        environment.jersey().register(helloWorldResource);
    }
}