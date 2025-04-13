package com.mcl.market.access;

import org.junit.jupiter.api.Test;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;

import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {

    private static final char FIX_FIELD_DELIMITER = '\u0001';

    private final MessageParser parser = new MessageParser();

    @Test
    public void newOrder() throws InvalidMessage, FieldNotFound {
        String message = "8=FIX.5.0|9=139|35=D|34=1|49=USER1|56=TRADING|52=20250412-14:30:00.000|11=ORD1002|21=1|55=XYZ|54=1|38=100|40=1|60=20250412-14:30:00.000|10=232|"
                .replace('|', FIX_FIELD_DELIMITER);
        Message parsed = parser.parse(message);
        assertEquals("FIX.5.0", parsed.getHeader().getString(8));
    }

    @Test
    public void reject() throws InvalidMessage, FieldNotFound {
        String message = "8=FIX.5.0|9=126|35=3|34=1|49=TRADING|56=USER1|52=20250412-15:10:00.000|45=8|372=D|373=3|58=Unsupported symbol|10=122|"
                .replace('|', FIX_FIELD_DELIMITER);
        Message parsed = parser.parse(message);
        assertEquals("FIX.5.0", parsed.getHeader().getString(8));
    }

    @Test
    public void success() throws InvalidMessage, FieldNotFound {
        String message = "8=FIX.5.0|9=139|35=8|34=1|49=TRADING|56=USER1|52=20250412-15:30:00.000|37=ORD12345|11=ORD1002|17=EXEC001|39=2|55=XYZ|54=1|38=100|14=0|6=180.50|60=20250410-15:30:00.000|10=110|"
                .replace('|', FIX_FIELD_DELIMITER);
        Message parsed = parser.parse(message);
        assertEquals("FIX.5.0", parsed.getHeader().getString(8));
    }
}