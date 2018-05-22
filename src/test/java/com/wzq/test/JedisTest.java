package com.wzq.test;

import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.DownTableRelation;
import com.wzq.sql.structure.MappingStructure;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class JedisTest {

    private static final String HOST = "192.168.220.128";
    private static final Integer PORT = 6379;

    private String charset = "utf8";

    private Jedis jedis = null;

    private long s = 0;
    private long e = 0;

    @Before
    public void b() {
        jedis = new Jedis(HOST, PORT);
        s = System.currentTimeMillis();
    }

    @After
    public void a() {

        e = System.currentTimeMillis();

        System.out.println("耗时：" + (e - s));
        jedis.close();
    }

    @Test
    public void t1() throws UnsupportedEncodingException, ParseException {
        Mapping mapping = Mapping.parseJson("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}");
        MappingStructure iMappingStructure = mapping.getIMappingStructure();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        ArrayList<String> strings = new ArrayList<String>();
        JSONObject parse = JSONValue.parse(JSONValue.toJSONString(iMappingStructure), JSONObject.class);
        HashMap<String, String> stringObjectHashMap = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : parse.entrySet()) {
            stringObjectHashMap.put(JSONValue.toJSONString(entry.getKey()), JSONValue.toJSONString(entry.getValue()));
        }
        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            // jedis 耗时：23945
//          jedis.hmset("mapping_" + i, stringObjectHashMap);
//          strings.add(JSONValue.toJSONString(iMappingStructure));
            // json-smart 耗时： 3118 效率最高
//            net.minidev.json.JSONValue.toJSONString(iMappingStructure).getBytes();
            Object parse1 = JSONValue.parse(JSONValue.toJSONString(iMappingStructure), MappingStructure.class);
            // fastjson 耗时：21429
//            com.alibaba.fastjson.JSONObject.toJSONBytes(iMappingStructure);
            // Serialize 耗时：5924
//            SerializationUtils.serialize(iMappingStructure);
        }
        long e = System.currentTimeMillis();

        System.out.println("耗时：" + (e - s));

    }

    @Test
    public void t3() {
        ScanResult<String> scan = jedis.scan("0", new ScanParams().match("*"));
        List<String> result = scan.getResult();
        for (String s : result) {
            System.out.println(s);
        }
    }

    @Test
    public void t4() {
        ScanResult<Map.Entry<String, String>> hscan = jedis.hscan("mapping_0", "0", new ScanParams().match("*"));
        List<Map.Entry<String, String>> result = hscan.getResult();
        for (Map.Entry<String, String> stringStringEntry : result) {
            System.out.println(stringStringEntry.getKey() + ", " + stringStringEntry.getValue());
        }
    }

    @Test
    public void t5() throws ParseException {

        Mapping mapping = Mapping.parseJson("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}");
        MappingStructure iMappingStructure = mapping.getIMappingStructure();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        ArrayList<String> strings = new ArrayList<String>();
        String sx = JSONValue.toJSONString(iMappingStructure);

        for (int i = 0; i < 100000; i++) {
            jedis.zadd("mappings", i, sx + "_" + i);
        }
    }

    @Test
    public void t6() {
        ScanResult<Tuple> mappings = jedis.zscan("mappings", "0", new ScanParams().match("*\"name\":\"u8_订单\"*"));
        List<Tuple> result = mappings.getResult();
        int i = 0;
        for (Tuple tuple : result) {
            System.out.println(tuple.getElement());
            i ++;
        }
        System.out.println("次数：" + i);
    }

    @Test
    public void t7() {
        Long mappings = jedis.zcount("mappings", 0, Integer.MAX_VALUE);
        System.out.println(mappings);
    }
    @Test
    public void t8() throws ParseException {
        Set<String> mappings = jedis.zrange("mappings", 0, Integer.MAX_VALUE);
        for (String mapping : mappings) {
            Mapping parse = Mapping.parseJson(mapping.substring(0, mapping.length() - 2));
            try {
                parse.getIMappingStructure().equals(parse.getIMappingStructure());
            }catch (Exception e){}
        }
    }
    @Test
    public void t9() throws ParseException {

        Mapping mapping = Mapping.parseJson("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}");
        String sx = JSONValue.toJSONString(mapping);

        for (int i = 0; i < 100000; i++) {
            jedis.lpush("lmappings", sx + " ");
        }
    }
    @Test
    public void t10() throws ParseException {
        String lmappings = jedis.rpop("lmappings");

        System.out.println(JSONValue.isValidJson(lmappings));
        System.out.println(JSONValue.parse(lmappings));
        Mapping m = Mapping.parseJson(lmappings);
        for (int i = 0; i < 10000000; i++) {
            MappingStructure iMappingStructure = m.getIMappingStructure();
            MappingStructure oMappingStructure = m.getOMappingStructure();
            MappingStructure iMappingStructure1 = m.getIMappingStructure(oMappingStructure);
            MappingStructure oMappingStructure1 = m.getOMappingStructure(iMappingStructure);
            DownTableRelation person = m.getODownTableRelation("person");
        }
    }
}
