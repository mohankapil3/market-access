package com.mcl.market.access;

import quickfix.Message;
import quickfix.field.*;

import static com.mcl.market.access.CommonConstants.*;
import static quickfix.field.OrdStatus.FILLED;
import static quickfix.field.Side.BUY;

public class MessageGenerator {

    public Message createErrorMessage(String error) {
        Message message = new Message();
        Message.Header header = message.getHeader();
        header.setField(new BeginString(FIX_BEGIN_STRING));
        header.setField(new MsgType(MsgType.REJECT));
        header.setField(new MsgSeqNum(MSG_SEQUENCE_NUMBER));
        header.setField(new SenderCompID(COMP_ID));
        header.setField(new TargetCompID(CLIENT_ID));
        header.setField(new SendingTime(SENDING_TIME));
        message.setField(new Text(error));
        return message;
    }

    public Message createExecutionReport() {
        Message message = new Message();
        Message.Header header = message.getHeader();
        header.setField(new BeginString(FIX_BEGIN_STRING));
        header.setField(new MsgType(MsgType.EXECUTION_REPORT));
        header.setField(new MsgSeqNum(MSG_SEQUENCE_NUMBER));
        header.setField(new SenderCompID(COMP_ID));
        header.setField(new TargetCompID(CLIENT_ID));
        header.setField(new SendingTime(SENDING_TIME));
        message.setField(new OrderID(ORDER_ID));
        message.setField(new ClOrdID(CLIENT_ORDER_ID));
        message.setField(new ExecID(EXECUTION_ID));
        message.setField(new OrdStatus(FILLED));
        message.setField(new Symbol(SYMBOL));
        message.setField(new Side(BUY));
        message.setField(new OrderQty(ORDER_QUANTITY));
        message.setField(new CumQty(CUMULATIVE_QUANTITY));
        message.setField(new AvgPx(AVERAGE_PRICE));
        message.setField(new TransactTime(SENDING_TIME));
        return message;
    }
}
