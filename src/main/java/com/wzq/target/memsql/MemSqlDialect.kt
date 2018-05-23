package com.wzq.target.memsql

import com.wzq.sql.type.Dialect
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.reflect.MethodUtils

class MemSqlDialect : Dialect() {

    object Constant {
        const val MYSQL_TYPE_CLASS = "com.mysql.cj.MysqlType"
        const val GET_BY_JDBC_TYPE = "getByJdbcType"
        const val GET_CLASS_NAME = "getClassName"
        const val GET_NAME = "getName"

        const val DATA_TYPE = "DATA_TYPE"
        const val COLUMN_NAME = "COLUMN_NAME"
    }

    private val mysqlType = ClassUtils.getClass(Constant.MYSQL_TYPE_CLASS)

    override fun getSqlTypeString(sqlType: Int?): String {
        try {
            return MethodUtils.invokeMethod(MethodUtils.invokeStaticMethod(mysqlType, Constant.GET_BY_JDBC_TYPE, sqlType), Constant.GET_NAME) as String
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getJavaClassNameBySqlType(sqlType: Int?): String {
        try {
            return MethodUtils.invokeMethod(MethodUtils.invokeStaticMethod(mysqlType, Constant.GET_BY_JDBC_TYPE, sqlType), Constant.GET_CLASS_NAME) as String
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getSqlTypeByJavaClassName(javaClassName: String?): Int {
        return super.getSqlTypeByJavaClassName(javaClassName)
    }

    override fun getJavaClassBySqlType(sqlType: Int?): Class<*> {
        return ClassUtils.getClass(getJavaClassNameBySqlType(sqlType))
    }

    override fun getSqlTypeByJavaClass(clazz: Class<*>?): Int {
        return super.getSqlTypeByJavaClass(clazz)
    }

    override fun getSqlTypeStringBySqlType(sqlType: Int?): String {
        return getSqlTypeString(sqlType)
    }

    override fun getSqlTypeStringByJavaClass(clazz: Class<*>?): String {
        try {
            return MethodUtils.invokeMethod(MethodUtils.invokeStaticMethod(mysqlType, Constant.GET_BY_JDBC_TYPE, getSqlTypeByJavaClass(clazz)), Constant.GET_CLASS_NAME) as String
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getSqlTypeStringByJavaClassName(javaClassName: String?): String {
        try {
            return MethodUtils.invokeMethod(MethodUtils.invokeStaticMethod(mysqlType, Constant.GET_BY_JDBC_TYPE, getSqlTypeByJavaClassName(javaClassName)), Constant.GET_CLASS_NAME) as String
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getSqlTypeStringBySqlType(sqlType: Int?, typeDialect: MutableMap<Int, String>?): String {
        return super.getSqlTypeStringBySqlType(sqlType, typeDialect)
    }

    override fun getSqlTypeStringByJavaClass(clazz: Class<*>?, typeDialect: MutableMap<Int, String>?): String {
        return super.getSqlTypeStringByJavaClass(clazz, typeDialect)
    }

    override fun getSqlTypeStringByJavaClassName(javaClassName: String?, typeDialect: MutableMap<Int, String>?): String {
        return super.getSqlTypeStringByJavaClassName(javaClassName, typeDialect)
    }
}