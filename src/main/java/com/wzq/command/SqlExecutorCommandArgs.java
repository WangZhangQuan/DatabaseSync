package com.wzq.command;

import com.wzq.core.command.CommandArgs;
import com.wzq.generator.impl.Sql;

import java.util.ArrayList;
import java.util.List;

public class SqlExecutorCommandArgs extends CommandArgs {
    private List<Sql> sqls;

    public SqlExecutorCommandArgs(List<Sql> sqls) {
        this.sqls = sqls;
    }
    public SqlExecutorCommandArgs(Sql sql) {
        ArrayList<Sql> sqls = new ArrayList<Sql>();
        sqls.add(sql);
        this.sqls = sqls;
    }

    public SqlExecutorCommandArgs() {

    }

    public List<Sql> getSqls() {
        return sqls;
    }

    public void setSqls(List<Sql> sqls) {
        this.sqls = sqls;
    }
}
