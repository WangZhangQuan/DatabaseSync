package com.wzq.core.generator;

import com.wzq.core.command.Command;

public interface Generator {
    /**
     * 生成操作命令
     * @param command
     * @return
     */
    Command generator(Command command);
}
