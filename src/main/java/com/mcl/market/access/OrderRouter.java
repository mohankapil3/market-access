package com.mcl.market.access;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import static com.mcl.market.access.Topic.EXECUTION_REPORT;
import static com.mcl.market.access.Topic.NEW_ORDER;

public class OrderRouter extends AbstractVerticle {
    public static final int SERVER_PORT = 9877;
    private static final Logger logger = LoggerFactory.getLogger(OrderRouter.class);
    private static final String PARSE_FAILURE_ERROR_MESSAGE = "Unable to parse input message";
    private static final String UNSUPPORTED_SYMBOL_ERROR_MESSAGE = "Unsupported symbol %s";
    private static final String SUPPORTED_SYMBOL = "XYZ";

    private final MessageParser messageParser = new MessageParser();
    private final MessageGenerator messageGenerator = new MessageGenerator();

    @Override
    public void start() {
        getVertx().deployVerticle(new ExchangeAdapter());
        NetServer netServer = getVertx().createNetServer();
        netServer.connectHandler(this::handleClient);
        netServer.listen(SERVER_PORT, result -> {
            if (result.succeeded()) {
                logger.info("Started server successfully");
            } else {
                logger.error("Failed to start the server", result.cause());
            }
        });
    }

    private void handleClient(NetSocket socket) {
        logger.info("New client connected {}", socket.remoteAddress());

        socket.handler(buffer -> {
            String rawMessage = buffer.toString();
            try {
                Message message = messageParser.parse(rawMessage);
                String symbol = message.getString(55);
                if (symbol.equals(SUPPORTED_SYMBOL)) {
                    getVertx().eventBus().publish(NEW_ORDER.getId(), message);
                } else {
                    Message errorMessage = messageGenerator.createErrorMessage(String.format(UNSUPPORTED_SYMBOL_ERROR_MESSAGE, symbol));
                    socket.write(serializeMessage(errorMessage));
                }
            } catch (InvalidMessage | FieldNotFound e) {
                logger.error(PARSE_FAILURE_ERROR_MESSAGE, e);
                Message errorMessage = messageGenerator.createErrorMessage(PARSE_FAILURE_ERROR_MESSAGE);
                socket.write(serializeMessage(errorMessage));
            }
        });

        getVertx().eventBus().consumer(EXECUTION_REPORT.getId(), message -> {
           Message fixMessage = (Message) message.body();
           socket.write(serializeMessage(fixMessage));
        });

        socket.closeHandler(_void -> logger.info("Client disconnected {}", socket.remoteAddress()));
    }

    private String serializeMessage(Message message) {
        return message.toString();
    }
}
