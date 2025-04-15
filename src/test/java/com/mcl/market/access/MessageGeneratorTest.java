package com.mcl.market.access;

import org.junit.jupiter.api.Test;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.*;

import static com.mcl.market.access.CommonConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static quickfix.field.OrdStatus.FILLED;
import static quickfix.field.Side.BUY;

class MessageGeneratorTest {

    private final MessageGenerator generator = new MessageGenerator();

    @Test
    public void errorResponse() throws FieldNotFound {
        String someError = "some error";
        Message error = generator.createErrorMessage(someError);
        assertOnHeader(error.getHeader(), MsgType.REJECT);
        assertEquals(someError, error.getField(new Text()).getValue());
    }

    @Test
    public void executionReport() throws FieldNotFound {
        Message executionReport = generator.createExecutionReport();
        assertOnHeader(executionReport.getHeader(), MsgType.EXECUTION_REPORT);
        assertEquals(ORDER_ID, executionReport.getField(new OrderID()).getValue());
        assertEquals(CLIENT_ORDER_ID, executionReport.getField(new ClOrdID()).getValue());
        assertEquals(EXECUTION_ID, executionReport.getField(new ExecID()).getValue());
        assertEquals(FILLED, executionReport.getField(new OrdStatus()).getValue());
        assertEquals(SYMBOL, executionReport.getField(new Symbol()).getValue());
        assertEquals(BUY, executionReport.getField(new Side()).getValue());
        assertEquals(ORDER_QUANTITY, executionReport.getField(new OrderQty()).getValue());
        assertEquals(CUMULATIVE_QUANTITY, executionReport.getField(new CumQty()).getValue());
        assertEquals(AVERAGE_PRICE, executionReport.getField(new AvgPx()).getValue());
        assertEquals(SENDING_TIME, executionReport.getField(new TransactTime()).getValue());
    }

    private void assertOnHeader(Message.Header header, String messageType) throws FieldNotFound {
        assertEquals(FIX_BEGIN_STRING, header.getField(new BeginString()).getValue());
        assertEquals(messageType, header.getField(new MsgType()).getValue());
        assertEquals(MSG_SEQUENCE_NUMBER, header.getField(new MsgSeqNum()).getValue());
        assertEquals(COMP_ID, header.getField(new SenderCompID()).getValue());
        assertEquals(CLIENT_ID, header.getField(new TargetCompID()).getValue());
        assertEquals(SENDING_TIME, header.getField(new SendingTime()).getValue());
    }
}