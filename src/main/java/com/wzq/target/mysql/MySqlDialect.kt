package com.wzq.target.mysql

import com.wzq.sql.type.Dialect
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.reflect.MethodUtils

class MySqlDialect : Dialect() {

    object Constant {
        const val MYSQL_TYPE_CLASS = "com.mysql.cj.MysqlType"
        const val GET_BY_JDBC_TYPE = "getByJdbcType"
        const val GET_CLASS_NAME = "getClassName"
        const val GET_NAME = "getName"
        const val GET_PRECISION = "getPrecision"
        const val IS_DECIMAL = "isDecimal"
        const val IS_ALLOWED = "isAllowed"

        const val DATA_TYPE = "DATA_TYPE"
        const val COLUMN_NAME = "COLUMN_NAME"
    }

    private val mysqlType = ClassUtils.getClass(Constant.MYSQL_TYPE_CLASS)

    override fun getSqlTypeString(sqlType: Int?): String {
        try {
            //
            val invokeStaticMethod = MethodUtils.invokeStaticMethod(mysqlType, Constant.GET_BY_JDBC_TYPE, sqlType)
            val invokeMethod = MethodUtils.invokeMethod(invokeStaticMethod, Constant.GET_NAME)
            var params = ""
            if  (StringUtils.lowerCase(invokeMethod as String) == "varchar") {
                params = "(255)"
            }
            return "$invokeMethod$params"
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
        return getSqlTypeString(getSqlTypeByJavaClass(clazz))
    }

    override fun getSqlTypeStringByJavaClassName(javaClassName: String?): String {
        return getSqlTypeString(getSqlTypeByJavaClassName(javaClassName))
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