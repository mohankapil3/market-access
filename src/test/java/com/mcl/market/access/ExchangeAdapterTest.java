package com.mcl.market.access;

import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.*;
import quickfix.fix50.NewOrderSingle;

import static com.mcl.market.access.CommonConstants.SENDING_TIME;
import static com.mcl.market.access.Topic.NEW_ORDER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static quickfix.field.OrdType.MARKET;
import static quickfix.field.Side.BUY;

@ExtendWith(VertxExtension.class)
class ExchangeAdapterTest {

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void executionReport(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new ExchangeAdapter(), testContext.succeeding(onSuccess -> {
            vertx.eventBus().consumer(Topic.EXECUTION_REPORT.getId(), message -> {
                Message received = (Message) message.body();
                try {
                    assertEquals(MsgType.EXECUTION_REPORT, received.getHeader().getField(new MsgType()).getValue());
                } catch (FieldNotFound exception) {
                    testContext.failNow(new RuntimeException("Unexpected FIX response", exception));
                }
                testContext.completeNow();
            });

            vertx.eventBus().publish(NEW_ORDER.getId(), new NewOrderSingle(new ClOrdID("ABC"), new Side(BUY), new TransactTime(SENDING_TIME), new OrdType(MARKET)));
        }));
    }
}