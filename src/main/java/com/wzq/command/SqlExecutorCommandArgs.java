package com.wzq.command;

import com.wzq.core.command.CommandArgs;

public class SqlExecutorCommandArgs extends CommandArgs {
    private String[] sqls;

    public SqlExecutorCommandArgs(String... sqls) {
        this.sqls = sqls;
    }

    public SqlExecutorCommandArgs() {

    }

    public String[] getSqls() {
        return sqls;
    }

    public void setSqls(String... sqls) {
        this.sqls = sqls;
    }
}
