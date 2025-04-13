package com.mcl.market.access;

import org.junit.jupiter.api.Test;
import quickfix.FieldNotFound;
import quickfix.Message;

import static org.junit.jupiter.api.Assertions.*;

class MessageGeneratorTest {

    private final MessageGenerator generator = new MessageGenerator();

    @Test
    public void errorResponse() throws FieldNotFound {
        Message error = generator.createErrorMessage("some error");
        assertEquals("FIX.5.0", error.getHeader().getString(8));
    }

    @Test
    public void executionReport() throws FieldNotFound {
        Message executionReport = generator.createExecutionReport();
        assertEquals("FIX.5.0", executionReport.getHeader().getString(8));
    }
}