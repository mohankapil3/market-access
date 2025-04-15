package com.mcl.market.access;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.mcl.market.access.OrderRouter.SERVER_PORT;
import static com.mcl.market.access.TestConstants.SOH;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class OrderRouterTest {

    public static final String SERVER_HOST = "localhost";

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void invalidMessage(Vertx vertx, VertxTestContext testContext) {
        String input = "junk";
        String output = "8=FIX.5.0|9=88|35=3|34=1|49=TRADING|52=20250412-15:30:00.000|56=USER1|58=Unable to parse input message|10=211|";
        executeAndAssert(vertx, testContext, input, output);
    }

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void validNewOrder(Vertx vertx, VertxTestContext testContext) {
        String newOrderMessage = "8=FIX.5.0|9=139|35=D|34=1|49=USER1|56=TRADING|52=20250412-14:30:00.000|11=ORD1002|21=1|55=XYZ|54=1|38=100|40=1|60=20250412-14:30:00.000|10=232|";
        String executionReport = "8=FIX.5.0|9=151|35=8|34=1|49=TRADING|52=20250412-15:30:00.000|56=USER1|6=180.5|11=ORD1002|14=0|17=EXEC001|37=ORD12345|38=100|39=2|54=1|55=XYZ|60=20250412-15:30:00.000|10=058|";
        executeAndAssert(vertx, testContext, newOrderMessage, executionReport);
    }

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void unsupportedSymbol(Vertx vertx, VertxTestContext testContext) {
        String newOrderMessage = "8=FIX.5.0|9=139|35=D|34=1|49=USER1|56=TRADING|52=20250412-14:30:00.000|11=ORD1002|21=1|55=ABC|54=1|38=100|40=1|60=20250412-14:30:00.000|10=163|";
        String output = "8=FIX.5.0|9=81|35=3|34=1|49=TRADING|52=20250412-15:30:00.000|56=USER1|58=Unsupported symbol ABC|10=039|";
        executeAndAssert(vertx, testContext, newOrderMessage, output);
    }

    private void executeAndAssert(Vertx vertx, VertxTestContext testContext, String input, String output) {
        String inputWithSOH = input.replace('|', SOH);
        String outputWithSOH = output.replace('|', SOH);
        vertx.deployVerticle(new OrderRouter(), testContext.succeeding(onSuccess -> {
            NetClient netClient = vertx.createNetClient();
            netClient.connect(SERVER_PORT, SERVER_HOST, connection -> {
                if (connection.succeeded()) {
                    NetSocket netSocket = connection.result();
                    netSocket.write(inputWithSOH);
                    netSocket.handler(buffer -> {
                        assertEquals(outputWithSOH, buffer.toString());
                        testContext.completeNow();
                    });
                } else {
                    testContext.failNow(new RuntimeException("Can't connect to the server", connection.cause()));
                }
            });
        }));
    }
}