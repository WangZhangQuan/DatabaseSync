package com.wzq.connnector;

import java.io.IOException;

public class TargetCloseException extends IOException {
    public TargetCloseException(String message) {
        super(message);
    }
}
