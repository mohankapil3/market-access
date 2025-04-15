package com.mcl.market.access;

import io.vertx.core.AbstractVerticle;

import static com.mcl.market.access.Topic.EXECUTION_REPORT;
import static com.mcl.market.access.Topic.NEW_ORDER;

public class ExchangeAdapter extends AbstractVerticle {

    private final MessageGenerator generator = new MessageGenerator();

    @Override
    public void start() {
        getVertx().eventBus().consumer(NEW_ORDER.getId(), message -> {
            // returns static execution report
            getVertx().eventBus().publish(EXECUTION_REPORT.getId(), generator.createExecutionReport());
        });
    }
}
