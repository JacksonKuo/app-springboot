package com.jkuo.sample.unit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SampleTest {

    @Test
    public void sampleTest() throws Exception {
        String test = "test";
        assertTrue(test.equals("test"));
    }
}