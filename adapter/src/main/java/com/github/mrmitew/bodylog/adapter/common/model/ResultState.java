package com.github.mrmitew.bodylog.adapter.common.model;

public abstract class ResultState {
    public abstract boolean isInProgress();

    public abstract boolean isSuccessful();

    public abstract Throwable error();
}
