package com.wzq.connnector;

import com.wzq.core.command.Command;
import com.wzq.core.connector.Connector;
import com.wzq.core.connector.Target;
import com.wzq.core.structure.Structure;

import java.io.IOException;

public class SimpleConnector implements Connector {
    public Target getIt() {
        return null;
    }

    public Target getRedundancyIt() {
        return null;
    }

    public void swapRedundancyIt() {

    }

    public Target getOt() {
        return null;
    }

    public Target getCacheIt() {
        return null;
    }

    public Target getCacheOt() {
        return null;
    }

    public Iterable<Structure> connect(Command command) {
        return null;
    }

    public Iterable<Structure> cacheI(Command command) {
        return null;
    }

    public Iterable<Structure> cacheO(Command command) {
        return null;
    }

    public void commit() {

    }

    public Structure getStructure(Target t, String[] tableNames) {
        return null;
    }

    public void close() throws IOException {

    }
}
