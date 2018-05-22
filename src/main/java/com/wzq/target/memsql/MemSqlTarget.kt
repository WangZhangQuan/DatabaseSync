package com.wzq.target.memsql

import com.wzq.connnector.AbstractTarget
import com.wzq.core.command.Command
import com.wzq.core.structure.Structure
import com.wzq.mapping.Mapping
import com.wzq.target.manager.TargetManager

class MemSqlTarget : AbstractTarget {

    constructor(host: String, port: Int, pass: String, url: String, targetManager: TargetManager) : super() {
        this.host = host
        this.port = port
        this.pass = pass
        this.url = url
        this.targetManager = targetManager
    }

    var host: String
    var port: Int
    var pass: String
    var url: String
    var targetManager: TargetManager

    override fun getStructure(tables: Array<out String>?, mapping: Mapping?): Structure {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun commit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execCUD(command: Command?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execRead(command: Command?): MutableIterable<Structure> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}