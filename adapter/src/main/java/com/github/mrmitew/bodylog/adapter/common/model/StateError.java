package com.github.mrmitew.bodylog.adapter.common.model;

public class StateError extends Throwable {
    public static class Empty extends StateError {
        public static final Empty INSTANCE = new Empty();
        private Empty() {
        }

        @Override
        public String toString() {
            return "Empty{}";
        }
    }

    @Override
    public String toString() {
        return "StateError{}";
    }
}