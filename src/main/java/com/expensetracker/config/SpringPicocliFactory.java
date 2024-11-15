package com.expensetracker.config;

import org.springframework.context.ApplicationContext;
import picocli.CommandLine;

public class SpringPicocliFactory implements CommandLine.IFactory {
    private final ApplicationContext applicationContext;

    public SpringPicocliFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return applicationContext.getBean(cls);
    }
}