package com.training.junit;

import com.training.junit.extention.GlobalExtension;
import com.training.junit.extention.PostProcessingExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        GlobalExtension.class,
        PostProcessingExtension.class
})
public abstract class TestBase {
}
