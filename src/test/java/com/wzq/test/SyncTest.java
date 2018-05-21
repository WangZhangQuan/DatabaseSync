package com.wzq.test;

import com.alibaba.fastjson.JSON;
import com.wzq.SyncManager;
import com.wzq.connnector.SimpleConnector;
import com.wzq.core.context.SyncContext;
import com.wzq.core.context.SyncMappingContext;
import com.wzq.core.context.SyncTableContext;
import com.wzq.core.listener.SyncListener;
import com.wzq.core.listener.SyncMappingListener;
import com.wzq.core.listener.SyncTableListener;
import com.wzq.core.sync.SyncOpreator;
import com.wzq.manager.impl.SimpleMappingManager;
import com.wzq.mapping.Mapping;
import org.junit.Test;

public class SyncTest {
    @Test
    public void t1() {
        // 创建一个Mapping
        Mapping mapping = Mapping.parseJson(JSON.parseObject("{name:'u8_订单',tableMaps:[{it:'t_user',ot:'person', columnMaps:[{ic:'tisx',oc:'tix',pt:'java.sql.Timestamp',whereIc:false,whereOc:false},{ic:'tix',oc:'tx',pt:'java.sql.Time',whereIc:false,whereOc:false},{ic:'datex',oc:'dax',pt:'java.sql.Date',whereIc:false,whereOc:false},{ic:'doux',oc:'dx',pt:'java.lang.Double',whereIc:false,whereOc:false},{ic:'flox',oc:'fx',pt:'java.lang.Float',whereIc:false,whereOc:false},{ic:'lonx',oc:'lx',pt:'java.lang.Long',whereIc:false,whereOc:false},{ic:'byx',oc:'bx',pt:'java.lang.Byte',whereIc:false,whereOc:false},{ic:'can',oc:'c',pt:'java.lang.Boolean',whereIc:false,whereOc:false},{ic:'price',oc:'p',pt:'java.math.BigDecimal',whereIc:false,whereOc:false},{ic:'username',oc:'name',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'userphone',oc:'phone',pt:'java.lang.String',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]},{it:'t_user',ot:'person_info_clone', columnMaps:[{ic:'remark',oc:'desc',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'addr',oc:'address',pt:'java.lang.String',whereIc:false,whereOc:false},{ic:'id',oc:'identity',pt:'java.lang.Integer',whereIc:true,whereOc:true}]}]}"));
        // 创建一个MappingManager
        SimpleMappingManager smm = new SimpleMappingManager();
        smm.getMappings().add(mapping);
        // 创建三个目标
        // 己方目标
//        SimpleTarget it = new SimpleTarget();
//        // 己方冗余目标
//        SimpleTarget redundancyIt = new SimpleTarget();
//        // 对方目标
//        SimpleTarget ot = new SimpleTarget();
        // 创建一个连接对象
        SimpleConnector sc = new SimpleConnector(null, null, null);
        // 创建一个同步上下文
        SyncContext syncContext = new SyncContext(smm, sc);
        // 创建同步管理
        SyncManager syncManager = new SyncManager(syncContext);
        // 添加几个监听器
        syncManager.addListener(new SyncListener() {
            public void preSync(SyncOpreator syncOpreator, SyncContext syncContext) {
                System.out.println("开始同步");
            }

            public void afterSync(SyncOpreator syncOpreator, SyncContext syncContext) {
                System.out.println("同步结束");
            }
        });
        syncManager.addListener(new SyncMappingListener() {
            public void preSync(SyncOpreator syncOpreator, SyncMappingContext syncContext) {
                System.out.println("开始同步Mapping: " + syncContext.getMappingStructure().getName());
            }

            public void afterSync(SyncOpreator syncOpreator, SyncMappingContext syncContext) {
                System.out.println("同步结束Mapping: " + syncContext.getMappingStructure().getName());
            }
        });
        syncManager.addListener(new SyncTableListener() {
            public void preSync(SyncOpreator syncOpreator, SyncTableContext syncContext) {
                System.out.println("开始同步表: " + syncContext.getTableStructure().getName());
            }

            public void afterSync(SyncOpreator syncOpreator, SyncTableContext syncContext) {
                System.out.println("同步结束表: " + syncContext.getTableStructure().getName());
            }
        });

        // 同步的操作方式
        SyncOpreator syncOpreator = new SyncOpreator();
        // 开始执行同步操作 单向同步
        syncManager.sync(syncOpreator);
    }
}
