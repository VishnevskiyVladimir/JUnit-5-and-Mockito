package com.training.junit.extention;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class PostProcessingExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
        System.out.println("TestInstancePostProcessor for " + o.toString());
        System.out.println("such a postprocessor Spring uses to inject dependencies. It works just after instance creation.");
    }
}
