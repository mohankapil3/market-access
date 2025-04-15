package com.mcl.market.access;

import quickfix.InvalidMessage;
import quickfix.Message;

public class MessageParser {

    public Message parse(String raw) throws InvalidMessage {
        return new Message(raw, true);
    }
}
