package com.wzq.core.executor;

import com.wzq.core.command.Command;

public interface CommandExecutor {
    void execute(Command command);
}
