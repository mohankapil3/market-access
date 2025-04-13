package com.mcl.market.access;

import quickfix.Message;
import quickfix.field.*;

import java.time.LocalDateTime;

import static quickfix.field.OrdStatus.FILLED;
import static quickfix.field.Side.BUY;

public class MessageGenerator {
    private static final String FIX_BEGIN_STRING = "FIX.5.0";
    private static final String COMP_ID = "TRADING";
    private static final String CLIENT_ID = "USER1";
    private static final LocalDateTime SENDING_TIME = LocalDateTime.parse("2025-04-12T15:30:00");
    private static final int DUMMY_MSG_SEQUENCE_NUMBER = 1;
    private static final int DUMMY_BODY_LENGTH = 55;

    public Message createErrorMessage(String error) {
        Message message = new Message();
        message.getHeader().setField(new BeginString(FIX_BEGIN_STRING));
        message.getHeader().setField(new BodyLength(DUMMY_BODY_LENGTH));
        message.getHeader().setField(new MsgType(MsgType.REJECT));
        message.getHeader().setField(new MsgSeqNum(DUMMY_MSG_SEQUENCE_NUMBER));
        message.getHeader().setField(new SenderCompID(COMP_ID));
        message.getHeader().setField(new TargetCompID(CLIENT_ID));
        message.getHeader().setField(new SendingTime(SENDING_TIME));
        message.setField(new Text(error));
        return message;
    }

    public Message createExecutionReport() {
        Message message = new Message();
        message.getHeader().setField(new BeginString(FIX_BEGIN_STRING));
        message.getHeader().setField(new BodyLength(DUMMY_BODY_LENGTH));
        message.getHeader().setField(new MsgType(MsgType.EXECUTION_REPORT));
        message.getHeader().setField(new MsgSeqNum(DUMMY_MSG_SEQUENCE_NUMBER));
        message.getHeader().setField(new SenderCompID(COMP_ID));
        message.getHeader().setField(new TargetCompID(CLIENT_ID));
        message.getHeader().setField(new SendingTime(SENDING_TIME));
        message.setField(new OrderID("ORD12345"));
        message.setField(new ClOrdID("ORD1002"));
        message.setField(new ExecID("EXEC001"));
        message.setField(new OrdStatus(FILLED));
        message.setField(new Symbol("XYZ"));
        message.setField(new Side(BUY));
        message.setField(new OrderQty(100));
        message.setField(new CumQty(0));
        message.setField(new AvgPx(180.50));
        message.setField(new TransactTime(SENDING_TIME));
        return message;
    }
}
