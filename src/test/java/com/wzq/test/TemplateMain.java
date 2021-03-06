package com.wzq.test;

import com.wzq.SyncManager;
import com.wzq.connnector.SimpleConnector;
import com.wzq.core.context.SyncContext;
import com.wzq.core.listener.SyncListener;
import com.wzq.core.structure.Structure;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.generator.MappingSqlGenerator;
import com.wzq.generator.impl.Sql;
import com.wzq.manager.impl.SimpleMappingManager;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.DownTableRelation;
import com.wzq.sql.structure.MappingStructure;
import com.wzq.sql.value.PlaceholderValue;
import com.wzq.util.FreemarkerUtil;
import com.wzq.util.KeyValue;
import com.wzq.util.MapUtils;
import freemarker.template.Configuration;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.ParseException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateMain {

    @Test
    public void t1() throws ParseException {
        FreemarkerUtil fu = new FreemarkerUtil(TemplateMain.class.getResource("/").getPath(), Configuration.VERSION_2_3_23, "utf-8");
        Map<String, Object> model = new HashMap<String, Object>();
        Mapping mapping = Mapping.parseJson("{name:'u8_订单',mainIt:'t_user',mainOt:'person',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}");
        model.put("mapping", mapping);
        Mapping mx = null;
        try {
            String print = fu.print("mapping.json", model);
            JSONObject js = (JSONObject) JSONValue.parse(print);
            Mapping m = Mapping.parseJson(JSONValue.toJSONString(js));
            mx = m;
            KeyValue<String, String>[][] keyValues = new KeyValue[][]{
                    {new KeyValue<String, String>("user_2", "person_2"), new KeyValue<String, String>("user_3", "person_3")},
                    {new KeyValue<String, String>("user_3", "person_3"), new KeyValue<String, String>("user_1", "person_1")}
            };
            Mapping[] mappings = Mapping.uncoupleMappings(m, keyValues);
            Mapping mapx = Mapping.fusionMappings(mappings);
            System.out.println(js.toJSONString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//
//        Update update = new Update();
//        Table table = new Table();
//        table.setName("user");
//        table.setAlias("u");
//        update.setTable(table);
//
//        List<Expression> expressions = new ArrayList<Expression>();
//        expressions.add(new Column(table, "name"));
//        expressions.add(new Column(table, "money"));
//        update.setColumns(expressions);
//
//        EqualsTo equalsTo = new EqualsTo();
//        equalsTo.setLeftExpression(new Column(table, "id"));
//        equalsTo.setRightExpression(new LongValue("5"));
//        update.setWhere(equalsTo);
//
//        List<Expression> es = new ArrayList<Expression>();
//        es.add(new StringValue("'王长全'"));
//        es.add(new DoubleValue("99999.99"));
//        update.setExpressions(es);
//
//        StatementDeParser statementDeParser = new StatementDeParser(new StringBuffer());
//        statementDeParser.visit(update);
//
//        System.out.println(statementDeParser.getBuffer());
        SimpleMappingManager smm = new SimpleMappingManager();
        smm.getMappings().clear();
        smm.getMappings().addAll(Arrays.asList(new Mapping[]{
                mx
        }));
        MappingSqlGenerator u8_订单 = smm.getGenerator("u8_订单");
        String itName = "t_user";
        String[] otNames = new String[]{
                "person",
                "person_info",
                "person_info_clone"
        };
        long t1 = System.currentTimeMillis();
        Sql sql = u8_订单.generateUpdateSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItColumns(itName, otNames)), PlaceholderValue.getInstance()), MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(sql);
        List<Sql> rsql = u8_订单.generateReverseUpdateSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItColumns(itName, otNames)), PlaceholderValue.getInstance()), MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        Sql isql = u8_订单.generateInsertSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(isql);
        List<Sql> risql = u8_订单.generateReverseInsertSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        Sql dsql = u8_订单.generateDeleteSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(dsql);
        List<Sql> rdsql = u8_订单.generateReverseDeleteSql(itName, MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        Sql ssql = u8_订单.generateSelectSql(itName, u8_订单.getMapping().getAllItColumns(itName, otNames), MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(ssql);
        List<Sql> srsql = u8_订单.generateReverseSelectSql(itName, u8_订单.getMapping().getAllItColumns(itName, otNames), MapUtils.mapFromO(Arrays.asList(u8_订单.getMapping().getAllItWhereColumns(itName, otNames)), PlaceholderValue.getInstance()), otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        Sql drsql = u8_订单.generateDropTableSql(itName, otNames);
        System.out.println(drsql);
        List<Sql> rdrsql = u8_订单.generateReverseDropTableSql(itName, otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        Sql csql = u8_订单.generateCreateTableSql(itName, u8_订单.getMapping().getAllItColumns(itName, otNames), null, otNames);
        System.out.println(csql);
        List<Sql> crsql = u8_订单.generateReverseCreateTableSql(itName, u8_订单.getMapping().getAllItColumns(itName, otNames), null, otNames);
        System.out.println(JSONValue.toJSONString(rsql));

        MappingStructure iMappingStructure = mapping.getIMappingStructure();
        MappingStructure oMappingStructure = mapping.getOMappingStructure();
        KeyValue<Structure, Structure> sskv = iMappingStructure.differenceSet(oMappingStructure);
        MappingStructure msc = (MappingStructure) iMappingStructure.clone();
        msc.getTables().get(0).getColumns().get(0).setValue(5);
        msc.getTables().get(0).getColumns().get(1).setValue(6);
        KeyValue<Structure, Structure> sskvc = iMappingStructure.differenceSet(msc);
        iMappingStructure.valueOf(msc);
        oMappingStructure.valueOf(iMappingStructure);
        Structure intersection = oMappingStructure.intersection(msc);
        Structure intersection1 = msc.intersection(iMappingStructure);
        long t2 = System.currentTimeMillis();

        DownTableRelation odtr = mapping.getODownTableRelation(otNames[0]);
        DownTableRelation idtr = mapping.getIDownTableRelation(itName);

        System.out.println("开始：" + t1 + "，结束：" + t2 + "，耗时：" + (t2 - t1));

        System.out.printf("ssss");
    }
}
