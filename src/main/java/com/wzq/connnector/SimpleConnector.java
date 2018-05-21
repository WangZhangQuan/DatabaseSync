package com.wzq.connnector;

import com.wzq.core.command.Command;
import com.wzq.core.command.Opreator;
import com.wzq.core.connector.Connector;
import com.wzq.core.connector.Target;
import com.wzq.core.structure.Structure;
import com.wzq.mapping.Mapping;

import java.io.IOException;

public class SimpleConnector implements Connector {

    private Target it;
    private Target redundancyIt;
    private Target ot;
    private Target cacheIt;
    private Target cacheOt;

    public SimpleConnector(Target it, Target redundancyIt, Target ot) {
        this.it = it;
        this.redundancyIt = redundancyIt;
        this.ot = ot;
    }

    public SimpleConnector(Target it, Target redundancyIt, Target ot, Target cacheIt, Target cacheOt) {
        this.it = it;
        this.redundancyIt = redundancyIt;
        this.ot = ot;
        this.cacheIt = cacheIt;
        this.cacheOt = cacheOt;
    }

    public Target getIt() {
        return it;
    }

    public void setIt(Target it) {
        this.it = it;
    }

    public Target getRedundancyIt() {
        return redundancyIt;
    }

    public void swapRedundancyIt() {
        Target t = it;
        it = redundancyIt;
        redundancyIt = t;
    }

    public void setRedundancyIt(Target redundancyIt) {
        this.redundancyIt = redundancyIt;
    }

    public Target getOt() {
        return ot;
    }

    public void setOt(Target ot) {
        this.ot = ot;
    }

    public Target getCacheIt() {
        return cacheIt;
    }

    public void setCacheIt(Target cacheIt) {
        this.cacheIt = cacheIt;
    }

    public Target getCacheOt() {
        return cacheOt;
    }

    public Iterable<Structure> connect(Command command) {
        Target t = null;
        if (Opreator.isReverse(command.getOpreator())) {
            t = ot;
        } else {
            t = it;
        }
        Iterable<Structure> r = null;
        if (Opreator.isCUD(command.getOpreator())) {
            t.execCUD(command);
        } else {
            r = t.execRead(command);
        }
        return r;
    }

    public Iterable<Structure> cacheI(Command command) {
        Iterable<Structure> r = null;
        if (Opreator.isCUD(command.getOpreator())) {
            cacheIt.execCUD(command);
        } else {
            r = cacheIt.execRead(command);
        }
        return r;
    }

    public Iterable<Structure> cacheO(Command command) {
        Iterable<Structure> r = null;
        if (Opreator.isCUD(command.getOpreator())) {
            cacheOt.execCUD(command);
        } else {
            r = cacheOt.execRead(command);
        }
        return r;
    }

    public void commit() {
        if (it != null) {
            it.commit();
        }
        if (ot != null) {
            ot.commit();
        }
        if (redundancyIt != null) {
            redundancyIt.commit();
        }
        if (cacheIt != null) {
            cacheIt.commit();
        }
        if (cacheOt != null) {
            cacheOt.commit();
        }
    }

    public Structure getStructure(Target t, Mapping mapping, String[] tableNames) {
        return t.getStructure(tableNames, mapping);
    }

    public void setCacheOt(Target cacheOt) {
        this.cacheOt = cacheOt;
    }

    public void close() throws IOException {
        if (it != null) {
            it.close();
        }
        if (ot != null) {
            ot.close();
        }
        if (redundancyIt != null) {
            redundancyIt.close();
        }
        if (cacheIt != null) {
            cacheIt.close();
        }
        if (cacheOt != null) {
            cacheOt.close();
        }
    }

}
