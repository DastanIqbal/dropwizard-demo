package com.dastan.rule;

import java.util.Optional;

public interface RulesRepository {

    Optional<Rule> random();
}