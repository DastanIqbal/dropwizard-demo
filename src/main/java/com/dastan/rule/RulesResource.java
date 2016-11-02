package com.dastan.rule;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rules")
@Produces(MediaType.APPLICATION_JSON)
public class RulesResource {

    private final RulesRepository rulesRepository;
    private final Rule notDefinedRule;

    public RulesResource(final RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
        this.notDefinedRule = new Rule("No title", "No description");
    }

    @GET
    @Timed
    public Rule presentRandomRule() {
        return rulesRepository.random().orElse(notDefinedRule);
    }
}