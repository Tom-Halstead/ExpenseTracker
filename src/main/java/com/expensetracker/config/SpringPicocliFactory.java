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
        try {
            return applicationContext.getBean(cls);
        } catch (Exception e) {
            // Fallback to Picocli's default factory if Spring context does not manage the bean
            return CommandLine.defaultFactory().create(cls);
        }
    }
}
