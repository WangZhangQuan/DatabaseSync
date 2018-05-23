package com.wzq.target.mysql

import com.wzq.command.SqlExecutorCommandArgs
import com.wzq.command.SqlGeneratorCommandArgs
import com.wzq.core.command.Command
import com.wzq.core.structure.Structure
import com.wzq.mapping.Mapping
import com.wzq.sql.structure.ColumnStructure
import com.wzq.sql.structure.MappingStructure
import com.wzq.sql.structure.TableStructure
import com.wzq.sql.type.Dialect
import com.wzq.sql.value.PlaceholderValue
import com.wzq.target.manager.AbstractTargetX
import com.wzq.target.manager.TargetManager
import com.wzq.target.manager.TargetParameter
import java.sql.Connection
import java.util.concurrent.locks.ReentrantLock

class MySqlTarget(targetManager: TargetManager, connection: Connection, override var targetParameter: TargetParameter) : AbstractTargetX(connection) {


    var targetManager: TargetManager = targetManager

    var dialect = MySqlDialect()

    private var lock = ReentrantLock()

    init {
        connection.autoCommit = false
    }

    override fun getStructure(tables: Array<out String>?, mapping: Mapping?): Structure {
        validateClosed()

        val metaData = connection.metaData

        val mappingStructure = MappingStructure()
        mappingStructure.name = mapping?.name
        mappingStructure.mapping = mapping
        mappingStructure.tables = ArrayList()

        if (tables != null) {

            for (table in tables) {
                val tableStructure = TableStructure(table)
                tableStructure.columns = ArrayList()
                val columns = metaData.getColumns(connection.catalog, "%", table, "%")
                while (columns.next()) {
                    tableStructure.columns.add(ColumnStructure(columns.getString(MySqlDialect.Constant.COLUMN_NAME), dialect.getSqlTypeString(columns.getInt(MySqlDialect.Constant.DATA_TYPE)), PlaceholderValue.PLACEHOLDER_VALUE))
                }
                mappingStructure.tables.add(tableStructure)
            }
        }
        return mappingStructure
    }

    override fun commit() {
        validateClosed()
        // 触发提交
        connection.commit()
    }

    override fun execCUD(command: Command?) {
        validateClosed()

        var c: Command? = null
        val ca = command!!.args;
        if (ca is SqlGeneratorCommandArgs) {
            c = ca.generator.generator(command)
        } else if (ca is SqlExecutorCommandArgs) {
            c = command
        } else {
            throw RuntimeException("Parameter types that are not supported: " + ca::class.java)
        }
        val args = c!!.args
        if (args is SqlExecutorCommandArgs) {
            val sqls = args.sqls
            for (sql in sqls) {
                val prepareStatement = connection.prepareStatement(sql.sql)
                val parameters = sql.parameters
                if (parameters != null) {
                    val size = parameters.size
                    for (i in 1..size) {
                        prepareStatement.setObject(i, parameters[i])
                    }
                }
                prepareStatement.execute()

                lock.lock()
                try {
                    // 查看是否满足提交操作
                    val incrementAndGet = batchOffset.incrementAndGet()
                    if (incrementAndGet >= batch.get()) {
                        connection.commit()
                        batchOffset.compareAndSet(incrementAndGet, 0)
                    }
                } finally {
                    lock.unlock()
                }
            }
        }
    }

    override fun close() {
        validateClosed()
        connection.commit()
        this.targetManager.recovery(this as AbstractTargetX)
    }

    override fun execRead(command: Command?): MutableIterable<Structure> {
        validateClosed()
        var c: Command? = null
        val ca = command!!.args;
        if (ca is SqlGeneratorCommandArgs) {
            c = ca.generator.generator(command)
        } else if (ca is SqlExecutorCommandArgs) {
            c = command
        } else {
            throw RuntimeException("Parameter types that are not supported: " + ca::class.java)
        }

        TODO("实现查询")
    }

    override fun getDialect(): Dialect {
        return dialect;
    }
}