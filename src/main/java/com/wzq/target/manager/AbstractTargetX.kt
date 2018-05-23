package com.wzq.target.manager

import com.wzq.connnector.AbstractTarget
import java.sql.Connection
import java.util.concurrent.atomic.AtomicBoolean


abstract class AbstractTargetX(connection: Connection) : AbstractTarget() {
    val connection: Connection = connection
    val closed: AtomicBoolean = AtomicBoolean(false)
    abstract var targetParameter: TargetParameter

    fun validateClosed(): Unit {
        if (closed.get() || connection.isClosed) {
            throw RuntimeException("Connection have been closed")
        }
    }

}