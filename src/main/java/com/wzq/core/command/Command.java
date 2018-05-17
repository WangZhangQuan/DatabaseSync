package com.wzq.core.command;

public class Command {
    private Opreator opreator = Opreator.SHOW;

    private CommandArgs args;

    public Command(Opreator opreator, CommandArgs args) {
        this.opreator = opreator;
        this.args = args;
    }

    public Command() {

    }

    public Command(Opreator opreator) {
        this.opreator = opreator;
    }

    public Opreator getOpreator() {
        return opreator;
    }

    public void setOpreator(Opreator opreator) {
        this.opreator = opreator;
    }

    public CommandArgs getArgs() {
        return args;
    }

    public void setArgs(CommandArgs args) {
        this.args = args;
    }

}
