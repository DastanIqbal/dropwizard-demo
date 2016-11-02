package com.dastan.repository;

import com.dastan.core.Rule;

import java.util.Optional;

public interface RulesRepository {

    Optional<Rule> random();
}