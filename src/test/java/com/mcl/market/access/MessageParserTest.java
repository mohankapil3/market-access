package com.mcl.market.access;

import org.junit.jupiter.api.Test;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.field.*;

import static com.mcl.market.access.CommonConstants.*;
import static com.mcl.market.access.TestConstants.SOH;
import static org.junit.jupiter.api.Assertions.*;
import static quickfix.field.HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION;
import static quickfix.field.MsgType.ORDER_SINGLE;

class MessageParserTest {

    private final MessageParser parser = new MessageParser();

    @Test
    public void newOrder() throws InvalidMessage, FieldNotFound {
        String message = "8=FIX.5.0|9=139|35=D|34=1|49=USER1|56=TRADING|52=20250412-15:30:00.000|11=ORD1002|21=1|55=XYZ|54=1|38=100|60=20250412-15:30:00.000|10=23|"
                .replace('|', SOH);
        Message parsed = parser.parse(message);
        Message.Header header = parsed.getHeader();
        assertEquals(FIX_BEGIN_STRING, header.getField(new BeginString()).getValue());
        assertEquals(139, header.getField(new BodyLength()).getValue());
        assertEquals(ORDER_SINGLE, header.getField(new MsgType()).getValue());
        assertEquals(MSG_SEQUENCE_NUMBER, header.getField(new MsgSeqNum()).getValue());
        assertEquals(CLIENT_ID, header.getField(new SenderCompID()).getValue());
        assertEquals(COMP_ID, header.getField(new TargetCompID()).getValue());
        assertEquals(SENDING_TIME, header.getField(new SendingTime()).getValue());
        assertEquals(CLIENT_ORDER_ID, parsed.getField(new ClOrdID()).getValue());
        assertEquals(SYMBOL, parsed.getField(new Symbol()).getValue());
        assertEquals(AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION, parsed.getField(new HandlInst()).getValue());
        assertEquals(Side.BUY, parsed.getField(new Side()).getValue());
        assertEquals(100, parsed.getField(new OrderQty()).getValue());
        assertEquals(SENDING_TIME, parsed.getField(new TransactTime()).getValue());
    }

    @Test
    public void missingSOH() {
        InvalidMessage missingSOH = assertThrows(InvalidMessage.class, () -> parser.parse("8=FIX.5.0"));
        assertEquals("SOH not found at end of field: 8 in 8=FIX.5.0", missingSOH.getMessage());
    }

    @Test
    public void junk() {
        InvalidMessage junkMessage = assertThrows(InvalidMessage.class, () -> parser.parse("junk"));
        assertEquals("Equal sign not found in field in junk", junkMessage.getMessage());
    }
}