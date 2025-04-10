package com.mcl.market.access;

public enum Topic {
    NEW_ORDER("order.router") ,
    EXECUTION_REPORT("execution.report");

    private final String id;

    Topic(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
