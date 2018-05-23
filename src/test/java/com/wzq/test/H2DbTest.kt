package com.wzq.test

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.h2.jdbcx.JdbcConnectionPool
import org.h2.jdbcx.JdbcDataSource
import java.sql.Connection
import java.sql.SQLException
import java.sql.ResultSet
import java.util.*


class H2DbTest {

    /**
     * 内存版的h2速度超快
     * */
    object Constant {
        val DB_PATH = "./config/test"
        val DB_MEM_PATH = "mem:test"
        val DB_USER = "sa"
        val DB_PASS = ""
        val DB_URL = "jdbc:h2:${Constant.DB_PATH}"
        val DB_MEM_URL = "jdbc:h2:${Constant.DB_MEM_PATH}"
    }

    private lateinit var con: Connection;
    private var s = 0L
    private var e = 0L

    @Before
    fun b() {
        val jdbcDataSource = JdbcDataSource()
        jdbcDataSource.setURL(Constant.DB_URL)
//        jdbcDataSource.setURL(Constant.DB_MEM_URL)
        jdbcDataSource.user = Constant.DB_USER
        jdbcDataSource.password = Constant.DB_PASS
        con = jdbcDataSource.connection
        s = System.currentTimeMillis()
    }

    @After
    fun a() {
        e = System.currentTimeMillis()
        println("耗时: " + (e - s))
        con.close()
    }

    @Test
    fun t1() {
        val prepareStatement = con.prepareStatement("CREATE TABLE person(id INT IDENTITY PRIMARY KEY, name VARCHAR(255), age INT, money FLOAT, score DOUBLE)")
        prepareStatement.execute()
        con.commit()
    }

    @Test
    fun t2() {
        val prepareStatement = con.prepareStatement("INSERT INTO person(name, age, money, score) VALUES ('张三', 22, 5655.55, 99.99)")
        prepareStatement.execute()
    }

    @Test
    fun t3() {
        val prepareStatement = con.prepareStatement("SELECT * FROM person WHERE name = ?")
        prepareStatement.setString(1, "王王王")
        var executeQuery = prepareStatement.executeQuery()
        var i = 0
        while (executeQuery.next()) {
            val metaData = executeQuery.metaData
//            val stringBuffer = StringBuffer()
//            for (i in 1..metaData.columnCount) {
//                stringBuffer.append("(name:${metaData.getColumnLabel(i)},value:${executeQuery.getObject(metaData.getColumnLabel(i))})\t")
//            }
//            println(stringBuffer)
            i++
        }
        executeQuery.close()
        println("条数：$i")
    }

    @Test
    fun t5() {
        val prepareStatement = con.prepareStatement("DROP TABLE person")
        val executeQuery = prepareStatement.execute()
    }

    @Test
    fun t4() {

        val ps = con.prepareStatement("INSERT INTO person(age, name, money, score)VALUES(?, ?, ?, ?)")
        val names = arrayOf("张", "王", "李", "冰", "鱼", "林", "零", "雨", "浩")

        val random = Random(System.currentTimeMillis())

        val stringBuilder = StringBuilder()
        con.autoCommit = false
        for (i in 1..500000) {
            ps.setInt(1, (i + random.nextInt(30)) % (random.nextInt(100) + 1))
            stringBuilder.append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)])
                    .append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)])
                    .append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)])
            ps.setString(2, stringBuilder.toString())
            ps.setFloat(3, ((i + random.nextInt(30)) / (random.nextInt(30) + 1)).toFloat())
            ps.setDouble(4, ((i + random.nextInt(30)) / (random.nextInt(30) + 1)).toDouble())
            stringBuilder.delete(0, 3)
            //            ps.execute();
            ps.addBatch()
            if (i % 1000 == 0) {
                ps.executeBatch()
            }
        }
        ps.executeBatch()
    }
}