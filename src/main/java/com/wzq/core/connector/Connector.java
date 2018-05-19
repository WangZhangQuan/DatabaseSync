package com.wzq.core.connector;

import com.wzq.core.command.Command;
import com.wzq.core.structure.Structure;

import java.io.Closeable;

public interface Connector extends Closeable {
    /**
     * 获取己方目标
     * @return
     */
    Target getIt();

    /**
     * 冗余的己方目标，用于实现数据无缝切换
     * @return
     */
    Target getRedundancyIt();

    /**
     * 交换冗余目标，用于实现数据无缝切换
     */
    void swapRedundancyIt();

    /**
     * 获取对方目标
     * @return
     */
    Target getOt();

    /**
     * 用于存放缓存的Target
     * 时效性很重要
     * @return
     */
    Target getCacheIt();

    /**
     * 用于存放缓存的Target
     * 时效性很重要
     * @return
     */
    Target getCacheOt();

    /**
     * 执行命令 根据 命令类型选择it或ot和返回值 无返回值返回null
     * @param command
     * @return
     */
    Iterable<Structure> connect(Command command);

    /**
     * 执行缓存命令 使用cacheIt和返回值 无返回值返回null
     * @param command
     * @return
     */
    Iterable<Structure> cacheI(Command command);

    /**
     * 执行缓存命令 使用cacheOt和返回值 无返回值返回null
     * @param command
     * @return
     */
    Iterable<Structure> cacheO(Command command);

    /**
     * 提交执行代码
     */
    void commit();

    /**
     * 获取数据结构
     * @param t
     * @param tableNames
     * @return
     */
    Structure getStructure(Target t, String[] tableNames);
}
