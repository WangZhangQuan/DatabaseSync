package com.wzq.connnector.tagert;


import com.wzq.command.SqlGeneratorCommandArgs;
import com.wzq.core.command.Command;
import com.wzq.core.command.CommandArgs;

public class OpreatorRun implements Runnable {

    private SimpleTarget simpleTarget;
    private Command command;

    public OpreatorRun(SimpleTarget simpleTarget, Command command) {
        this.simpleTarget = simpleTarget;
        this.command = command;
    }

    public void run() {
        exec();
    }

    public void exec() {
        CommandArgs args = command.getArgs();
        SqlGeneratorCommandArgs sqlGeneratorCommandArgs = null;

        if (!(args instanceof SqlGeneratorCommandArgs)) {
            throw new RuntimeException("Cannot process args:" + args);
        } else {
            sqlGeneratorCommandArgs = (SqlGeneratorCommandArgs) args;
        }

        switch (command.getOpreator()) {

            case UPDATE:
                break;
            case REVERSE_UPDATE:
                break;
            case NEW:
                break;
            case REVERSE_NEW:
                break;
            case DELETE:
                break;
            case REVERSE_DELETE:
                break;
            case SHOW:
                break;
            case REVERSE_SHOW:
                break;
            case CREATE:
                break;
            case REVERSE_CREATE:
                break;
            case DROP:
                break;
            case REVERSE_DROP:
                break;
            default:
                throw new UnsupportedOperationException("Unsupport Opreator:" + command.getOpreator());
        }
    }

}