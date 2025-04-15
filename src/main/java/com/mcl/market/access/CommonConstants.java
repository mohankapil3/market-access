package com.mcl.market.access;

import java.time.LocalDateTime;

public final class CommonConstants {

    public static final String FIX_BEGIN_STRING = "FIX.5.0";

    public static final String COMP_ID = "TRADING";

    public static final String CLIENT_ID = "USER1";

    public static final LocalDateTime SENDING_TIME = LocalDateTime.parse("2025-04-12T15:30:00");

    public static final int MSG_SEQUENCE_NUMBER = 1;

    public static final String ORDER_ID = "ORD12345";

    public static final String CLIENT_ORDER_ID = "ORD1002";

    public static final String EXECUTION_ID = "EXEC001";

    public static final String SYMBOL = "XYZ";

    public static final int ORDER_QUANTITY = 100;

    public static final double AVERAGE_PRICE = 180.50;

    public static final int CUMULATIVE_QUANTITY = 0;
}
