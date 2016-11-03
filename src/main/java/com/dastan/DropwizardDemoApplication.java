package com.dastan;

import com.dastan.core.Person;
import com.dastan.db.PersonDAO;
import com.dastan.health.TemplateHealthCheck;
import com.dastan.repository.JsonFileRulesRepository;
import com.dastan.resources.HelloWorldResource;
import com.dastan.resources.PeopleResource;
import com.dastan.resources.PersonResource;
import com.dastan.resources.RulesResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropwizardDemoApplication extends Application<AppConfiguration> {

    public static final Logger LOGGER = LoggerFactory.getLogger(DropwizardDemoApplication.class);

    public static void main(final String[] args) throws Exception {
        new DropwizardDemoApplication().run(args);
    }

    private final HibernateBundle<AppConfiguration> hibernateBundle =
            new HibernateBundle<AppConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        // nothing to do yet
        //add Bundle,Command, register jackson moudles
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new MigrationsBundle<AppConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final AppConfiguration configuration, final Environment environment)
            throws Exception {
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        LOGGER.info("Application name: {}", configuration.getAppName());
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        final JsonFileRulesRepository jsonFileRulesRepository = new JsonFileRulesRepository();

        final RulesResource rulesResource = new RulesResource(jsonFileRulesRepository);
        environment.jersey().register(rulesResource);

        final HelloWorldResource helloWorldResource = new HelloWorldResource(configuration.getTemplate(), configuration.getAppName());
        environment.jersey().register(helloWorldResource);

        environment.jersey().register(new PeopleResource(dao));
        environment.jersey().register(new PersonResource(dao));
    }
}