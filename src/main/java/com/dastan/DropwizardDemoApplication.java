package com.dastan;

import com.dastan.repository.JsonFileRulesRepository;
import com.dastan.rule.RulesResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardDemoApplication extends Application<AppConfiguration> {

    public static final Logger LOGGER = LoggerFactory.getLogger(DropwizardDemoApplication.class);

    public static void main(final String[] args) throws Exception {
        new DropwizardDemoApplication().run(args);
    }

    @Override
    public void run(final AppConfiguration configuration, final Environment environment)
            throws Exception {

        LOGGER.info("Application name: {}", configuration.getAppName());
        JsonFileRulesRepository jsonFileRulesRepository = new JsonFileRulesRepository();
        RulesResource rulesResource = new RulesResource(jsonFileRulesRepository);
        environment.jersey().register(rulesResource);
    }
}