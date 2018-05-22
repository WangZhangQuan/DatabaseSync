package com.wzq.test

import com.wzq.mapping.Mapping
import net.minidev.json.JSONValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import redis.clients.jedis.Jedis

class KotinTest {

    lateinit var jedis: Jedis
    val HOST = "192.168.220.128"
    val PORT = 6379

    var s: Long = 0
    var e: Long = 0

    @Before
    fun b() {
        jedis = Jedis(HOST, PORT)
        s = System.currentTimeMillis()
    }
    @After
    fun a() {
        e = System.currentTimeMillis()
        println("耗时：${e - s}")
        jedis.close()
    }

    @Test
    fun t1() {

        val mapping = JSONValue.parseWithException("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}", Mapping::class.java)

        for (i in 0..100000) {
            jedis.lpush("kotlin", JSONValue.toJSONString(mapping))
        }

    }

    @Test
    fun t2() {
        val lpop = jedis.lpop("kotlin")
        println(lpop)
    }
    @Test
    fun t3() {
        val lpop = jedis.llen("kotlin")
        println(lpop)
    }
}