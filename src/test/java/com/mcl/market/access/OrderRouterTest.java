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
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class OrderRouterTest {

    public static final String SERVER_HOST = "localhost";

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void clientConnection(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new OrderRouter(), testContext.succeeding(onSuccess -> {
            NetClient netClient = vertx.createNetClient();
            netClient.connect(SERVER_PORT, SERVER_HOST, connection -> {
                if (connection.succeeded()) {
                    NetSocket netSocket = connection.result();
                    netSocket.write("hello from unit test");
                    netSocket.handler(buffer -> {
                        assertEquals("Server received hello from unit test", buffer.toString());
                        testContext.completeNow();
                    });
                } else {
                    testContext.failNow(new RuntimeException("Can't connect to the server", connection.cause()));
                }
            });
        }));
    }

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void invalidMessage(Vertx vertx, VertxTestContext testContext) {
        String message = "junk";

        vertx.deployVerticle(new OrderRouter(), testContext.succeeding(onSuccess -> {
            NetClient netClient = vertx.createNetClient();
            netClient.connect(SERVER_PORT, SERVER_HOST, connection -> {
                if (connection.succeeded()) {
                    NetSocket netSocket = connection.result();
                    netSocket.write(message);
                    netSocket.handler(buffer -> {
                        assertEquals("Server received hello from unit test", buffer.toString());
                        testContext.completeNow();
                    });
                } else {
                    testContext.failNow(new RuntimeException("Can't connect to the server", connection.cause()));
                }
            });
        }));
    }

    @Test
    @Timeout(value = 3, timeUnit = SECONDS)
    public void validMessage(Vertx vertx, VertxTestContext testContext) {
        String message = "8=FIX.5.0|9=65|35=D|49=BUYER01|56=SELLER01|11=ORDER12345|55=AAPL|54=1|10=7|".replace('|', '\u0001');

        vertx.deployVerticle(new OrderRouter(), testContext.succeeding(onSuccess -> {
            NetClient netClient = vertx.createNetClient();
            netClient.connect(SERVER_PORT, SERVER_HOST, connection -> {
                if (connection.succeeded()) {
                    NetSocket netSocket = connection.result();
                    netSocket.write(message);
                    netSocket.handler(buffer -> {
                        assertEquals("Server received hello from unit test", buffer.toString());
                        testContext.completeNow();
                    });
                } else {
                    testContext.failNow(new RuntimeException("Can't connect to the server", connection.cause()));
                }
            });
        }));
    }
}