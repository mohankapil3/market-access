package com.mcl.market.access;

import io.vertx.core.AbstractVerticle;

import static com.mcl.market.access.Topic.EXECUTION_REPORT;
import static com.mcl.market.access.Topic.NEW_ORDER;

public class ExchangeAdapter extends AbstractVerticle {
    @Override
    public void start() {
        getVertx().eventBus().consumer(NEW_ORDER.getId(), message -> {
            // TODO add flow to validate on quantity
            getVertx().eventBus().publish(EXECUTION_REPORT.getId(), message.body());
        });
    }
}
