package com.gsobko.act.db;

public class DaoExcepton extends RuntimeException {

    public DaoExcepton(String message) {
        super(message);
    }

    public DaoExcepton(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoExcepton(Throwable cause) {
        super(cause);
    }
}
