package com.wzq.connnector.iterator;

import com.wzq.core.structure.Structure;
import com.wzq.sql.structure.TableStructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TableStructureInterator implements StructureIterator {

    private ResultSet resultSet;

    private String tableName;

    private TableStructure current;

    private Lock reentrantLock = new ReentrantLock();

    private int c = 0;
    private int n = 0;
    private boolean isEnd = false;

    public TableStructureInterator(ResultSet resultSet, String tableName) {
        reentrantLock.lock();
        try {
            this.resultSet = resultSet;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean hasNext() {
        reentrantLock.lock();
        try {
            if (c >= n) {
                boolean flag = resultSet.next();
                if (flag) {
                    n ++;
                }
                return flag;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            reentrantLock.unlock();
        }
    }

    public TableStructure next() {
        reentrantLock.lock();
        try {
            if (c < n) {
                c ++;
                return getStructure();
            } else if (!isEnd) {
                boolean flag = false;
                try {
                    flag = resultSet.next();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (flag) {
                    n ++;
                    c ++;
                    return getStructure();
                } else {
                    isEnd = true;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
        return null;
    }

    private TableStructure getStructure() {
        // TODO 创建Structure
        reentrantLock.lock();
        try {
            if (current != null) {
                // TODO 根据rs创建一个TableMapping
            }
        } finally {
            reentrantLock.unlock();
        }
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    public void close() throws Exception {
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
