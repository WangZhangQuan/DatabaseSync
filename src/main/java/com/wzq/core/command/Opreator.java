package com.wzq.core.command;

import com.wzq.mapping.Mapping;

import java.util.Map;

public enum Opreator {
    UPDATE,
    REVERSE_UPDATE,
    NEW,
    REVERSE_NEW,
    DELETE,
    REVERSE_DELETE,
    SHOW,
    REVERSE_SHOW,
    CREATE,
    REVERSE_CREATE,
    DROP,
    REVERSE_DROP;
    public static boolean isCUD(Opreator opreator) {
        switch (opreator) {
            case UPDATE:
            case REVERSE_UPDATE:
            case NEW:
            case REVERSE_NEW:
            case DELETE:
            case REVERSE_DELETE:
            case CREATE:
            case REVERSE_CREATE:
            case DROP:
            case REVERSE_DROP:
                return true;
            case SHOW:
            case REVERSE_SHOW:
                return false;
            default:
                throw new UnsupportedOperationException("Unsupport Opreator:" + opreator);
        }
    }
    public static boolean isReverse(Opreator opreator) {
        switch (opreator) {

            case UPDATE:
            case NEW:
            case SHOW:
            case DELETE:
            case CREATE:
            case DROP:
                return false;
            case REVERSE_UPDATE:
            case REVERSE_NEW:
            case REVERSE_DELETE:
            case REVERSE_SHOW:
            case REVERSE_CREATE:
            case REVERSE_DROP:
                return true;
            default:
                throw new UnsupportedOperationException("Unsupport Opreator:" + opreator);

        }
    }
}