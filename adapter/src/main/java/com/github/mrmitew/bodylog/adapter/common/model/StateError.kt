package com.github.mrmitew.bodylog.adapter.common.model

sealed class StateError : Throwable() {
    class Empty private constructor() : StateError() {
        override fun toString(): String {
            return "Empty{}"
        }

        companion object {
            val INSTANCE = Empty()
        }
    }

    override fun toString(): String {
        return "StateError{}"
    }
}