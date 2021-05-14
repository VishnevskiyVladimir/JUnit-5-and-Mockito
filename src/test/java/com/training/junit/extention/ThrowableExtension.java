package com.training.junit.extention;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;

public class ThrowableExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        //"Here we have access to all exceptions which are thrown in our tests"
        // code below filter all exception except IOException. Our RuntimeException in UserServiceTest doesnt work
        // of course if ThrowableExtension is connected to test class
        if(throwable instanceof IOException) {
            throw throwable;
        }

    }
}
