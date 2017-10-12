package com.gsobko.act.db;

public class DuplicateKeyViolationException extends DaoExcepton {
    public DuplicateKeyViolationException(String message) {
        super(message);
    }

    public DuplicateKeyViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyViolationException(Throwable cause) {
        super(cause);
    }
}
