package com.mcl.market.access;

import quickfix.Message;
import quickfix.field.*;

public class MessageGenerator {
    private static final String FIX_BEGIN_STRING = "FIX.5.0";

    public Message createErrorMessage(String error, String senderCompId, String targetCompId) {
        Message message = new Message();
        message.getHeader().setField(new BeginString(FIX_BEGIN_STRING));
        message.getHeader().setField(new SenderCompID(senderCompId));
        message.getHeader().setField(new TargetCompID(targetCompId));
        message.getHeader().setField(new MsgType(MsgType.REJECT));
        message.setField(new Text(error));
        return message;
    }
}
