package com.walmart.coffee.squad.test.enums;

public enum JiraStatus {
    Backlog(331),
    Work_In_Progress(311),
    Ready_For_Review(361);

    private final int value;

    JiraStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
