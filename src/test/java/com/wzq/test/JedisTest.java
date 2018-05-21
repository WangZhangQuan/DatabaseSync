package com.wzq.test;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.MappingStructure;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JedisTest {

    private static final String HOST = "192.168.56.128";
    private static final Integer PORT = 6379;

    private String charset = "utf8";

    private Jedis jedis = null;

    @Before
    public void b() {
        jedis = new Jedis(HOST, PORT);
    }

    @Test
    public void t1() throws UnsupportedEncodingException {
        Mapping mapping = Mapping.parseJson((JSONObject) JSONValue.parse("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}"));
        MappingStructure iMappingStructure = mapping.getIMappingStructure();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        ArrayList<String> strings = new ArrayList<String>();
        String s1 = JSONValue.toJSONString(iMappingStructure);
        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            // jedis 耗时：23945
//          jedis.lpush("list", s1);
//          strings.add(JSONValue.toJSONString(iMappingStructure));
            // json-smart 耗时： 3118 效率最高
            net.minidev.json.JSONValue.toJSONString(iMappingStructure).getBytes();
            // fastjson 耗时：21429
//            com.alibaba.fastjson.JSONObject.toJSONBytes(iMappingStructure);
            // Serialize 耗时：5924
//            SerializationUtils.serialize(iMappingStructure);
        }
        long e = System.currentTimeMillis();

        System.out.println("耗时：" + (e - s));

    }
}
