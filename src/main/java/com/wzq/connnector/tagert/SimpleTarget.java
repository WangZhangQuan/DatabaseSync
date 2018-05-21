//package com.wzq.connnector.tagert;
//
//import com.wzq.command.SqlGeneratorCommandArgs;
//import com.wzq.connnector.AbstractTarget;
//import com.wzq.connnector.TargetCloseException;
//import com.wzq.connnector.tagert.index.HashIndex;
//import com.wzq.core.command.Command;
//import com.wzq.core.command.CommandArgs;
//import com.wzq.core.command.Opreator;
//import com.wzq.core.structure.Structure;
//import com.wzq.mapping.Mapping;
//import com.wzq.sql.structure.DownTableRelation;
//import com.wzq.sql.structure.MappingStructure;
//import com.wzq.sql.structure.TableStructure;
//import com.wzq.util.ColumnComparator;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
//
//import java.io.IOException;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Deprecated
//public class SimpleTarget extends AbstractTarget {
//
//    public static final String DEFAULT_CACHE_NAME = "cache";
//
//    private static final Random random;
//
//    static {
//        random = new Random(System.currentTimeMillis());
//    }
//
//    private String name = getDefaultCacheName();
//
//    private final Map<String, TableStructure> meta = new ConcurrentHashMap<String, TableStructure>();
//
//    private final List<Command> dels = new CopyOnWriteArrayList<Command>();
//    private final List<Command> incs = new CopyOnWriteArrayList<Command>();
//    private final List<Command> upds = new CopyOnWriteArrayList<Command>();
//
//    private AtomicBoolean closed = new AtomicBoolean(false);
//
//    private ExecutorService executorService = Executors.newCachedThreadPool();
//
//    private HashIndex hashIndex = new HashIndex();
//
//    private Class<? extends OpreatorRun> opreatorRunClass = OpreatorRun.class;
//
//    public SimpleTarget() {
//    }
//
//    public SimpleTarget(String name) {
//        this.name = name;
//    }
//
//    public Structure getStructure(String[] tables, Mapping mapping) {
//        // 验证是否被关闭
//        validateClosed();
//        MappingStructure ms = new MappingStructure(mapping);
//        List<TableStructure> tss = new ArrayList<TableStructure>();
//        for (String t : tables) {
//            Structure s = meta.get(t);
//            if (s != null) {
//                tss.add((TableStructure) s);
//            }
//        }
//        ms.setTables(tss);
//        return ms;
//    }
//
//    public void commit() {
//        // 验证是否被关闭
//        validateClosed();
//        Constructor<? extends OpreatorRun> constructor = null;
//        try {
//            constructor = opreatorRunClass.getConstructor(SimpleTarget.class, Command.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            throw new RuntimeException("The construction method of NoSuchMethodException OpreatorRun: OpreatorRun (SimpleTarget, Command)", e);
//        }
//        submitAll(constructor);
//    }
//
//    private void submitAll(Constructor<? extends OpreatorRun> constructor) {
//        submitCommands(incs, constructor);
//        submitCommands(upds, constructor);
//        submitCommands(dels, constructor);
//    }
//
//    private void submitCommands(List<Command> commands, Constructor<? extends OpreatorRun> constructor) {
//        for (Command c : commands) {
//            try {
//                OpreatorRun oc = constructor.newInstance(this, c);
////                executorService.submit(oc);
//                // TODO 暂时同步执行，防止监听器不同步的问题，后面可将监听器放置在回调中执行
//                executorService.execute(oc);
//                // 提交完成从列表移除此对象
//                commands.remove(c);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void execCUD(Command command) {
//        // 验证是否被关闭
//        validateClosed();
//        CommandArgs args = command.getArgs();
//        if (!(args instanceof SqlGeneratorCommandArgs)) {
//            throw new RuntimeException("Parameters that are not supported by the cache:" + args);
//        }
//        if (Opreator.isReverse(command.getOpreator())) {
//            // TODO 暂时不支持逆向操作
//            throw new RuntimeException("Caching does not support reverse operation for the time being");
//        }
//        SqlGeneratorCommandArgs argsx = (SqlGeneratorCommandArgs) args;
//
//        MappingStructure ms = (MappingStructure) command.getStructure();
//        if (argsx.getWhereColumnMap() == null) {
//            Mapping mapping = command.getMapping();
//            String[] otNames = argsx.getOtNames() == null ? mapping.getAllOtNames(argsx.getItName()) : argsx.getOtNames();
//            String[] allItWhereColumns = mapping.getAllItWhereColumns(argsx.getItName(), otNames);
//            argsx.setWhereColumnMap(ms.findTableAndColumnValues(argsx.getItName(), allItWhereColumns));
//        }
//
//        Object[] index = createIndex(argsx.getItName(), argsx.getWhereColumnMap());
//        TableStructure table = ms.findTable(argsx.getItName());
//
//        switch (command.getOpreator()) {
//
//            case UPDATE:
////            case REVERSE_UPDATE:
//                upds.add(command);
//                Set<Object> indexValues = hashIndex.findIndexValues(index);
//                if (indexValues != null) {
//                    if (indexValues.size() > 0) {
//                        for (Object indexValue : indexValues) {
//                            if (indexValue instanceof TableStructure) {
//                                TableStructure ts = (TableStructure) indexValue;
//                                if (ts.getName().equals(argsx.getItName())) {
//                                    if (ts.where(argsx.getWhereColumnMap())) {
//                                        hashIndex.deleteIndexAndByValue(index, ts);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                hashIndex.addIndex(index, table);
//                break;
//            case NEW:
////            case REVERSE_NEW:
//                incs.add(command);
//                hashIndex.addIndex(index, table);
//                break;
//            case DELETE:
////            case REVERSE_DELETE:
//                dels.add(command);
//                hashIndex.deleteIndexAndByValue(index, table);
//                break;
//            case CREATE:
////            case REVERSE_CREATE:
//                createMeta(command);
//                break;
//            case DROP:
////            case REVERSE_DROP:
//                dropMeta(command);
//                break;
//            default:
//                throw new UnsupportedOperationException("Unsupport Opreator:" + command.getOpreator());
//        }
//
//        if ((upds.size() + incs.size() + dels.size()) >= getBatch()) {
//            Constructor<? extends OpreatorRun> constructor = null;
//            try {
//                constructor = opreatorRunClass.getConstructor(SimpleTarget.class, Command.class);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                throw new RuntimeException("The construction method of NoSuchMethodException OpreatorRun: OpreatorRun (SimpleTarget, Command)", e);
//            }
//            submitAll(constructor);
//        }
//    }
//
//    public Iterable<Structure> execRead(Command command) {
//        // 验证是否被关闭
//        validateClosed();
//        MappingStructure ms = (MappingStructure) command.getStructure();
//        Iterable<Structure> structures = new ArrayList<Structure>(0);
//        if (command.getOpreator() == Opreator.SHOW || command.getOpreator() == Opreator.REVERSE_SHOW) {
//            if (command.getOpreator() == Opreator.REVERSE_SHOW) {
//                // TODO 缓存不支持逆向查询
//                throw new RuntimeException("Caching does not support reverse operation for the time being");
//            }
//            // 查询操作
//            CommandArgs args = command.getArgs();
//            if (args instanceof SqlGeneratorCommandArgs) {
//                SqlGeneratorCommandArgs sargs = (SqlGeneratorCommandArgs) args;
//                structures = find(command.getOpreator() , command.getMapping(),ms, sargs.getWhereColumnMap());
//            }
//        } else {
//            throw new UnsupportedOperationException("Unsupported operation types of read " + command.getOpreator());
//        }
//
//        return structures;
//    }
//
//    private synchronized static String getDefaultCacheName() {
//        return new StringBuffer()
//                .append(DEFAULT_CACHE_NAME)
//                .append("_")
//                .append(random.nextInt(10000))
//                .append("_")
//                .append(System.currentTimeMillis())
//                .toString();
//    }
//
//    public void close() throws IOException {
//        // 关闭时先提交操作 避免操作无法提交
//        commit();
//        closed.compareAndSet(false, true);
//    }
//
//    private void validateClosed() {
//        if (closed.get()) {
//            throw new TargetCloseException("Target has been shut down:" + this.name);
//        }
//    }
//
//    public Iterable<Structure> find(Opreator opreator, Mapping mapping, MappingStructure ms, Map<String, Object> wheres) {
//
//        // 获取主导表
//        TableStructure mainTs = null;
//        String[] mainTWhereColumns = null;
//        DownTableRelation odtr = null;
//        if (Opreator.isReverse(opreator)) {
//            ms = mapping.getOMappingStructure(ms);
//            mainTWhereColumns = mapping.getAllOtColumns(mapping.getMainIt(), mapping.getMainOt()).get(mapping.getMainOt());
//            odtr = mapping.getODownTableRelation(mapping.getMainOt());
//        } else {
//            ms.findTable(mapping.getMainIt());
//            mainTWhereColumns = mapping.getAllItWhereColumns(mapping.getMainIt(), mapping.getAllOtNames(mapping.getMainIt()));
//        }
//
//        ArrayList<Structure> structures = new ArrayList<Structure>();
//        // 创建主导表索引
//        Object[] index = createIndex(mainTs.getName(), ms.findTableAndColumnValues(mainTs.getName(), mainTWhereColumns));
//        Set<Object> indexValues = hashIndex.findIndexValues(index);
//        for (Object indexValue : indexValues) {
//            if (indexValue instanceof TableStructure) {
//                TableStructure ts = (TableStructure) indexValue;
//                if (Opreator.isReverse(opreator)) {
//                    // TODO 根据DownTableRelation 查取相关数据表
//                } else {
//                    if (ts.where(wheres)) {
//                        structures.add(ts);
//                    }
//                }
//            }
//        }
//        // 对stauctures排序根据where
//        sortTableStructures(structures, getSortedColumns(mapping.getMainIt(), new ArrayList<String>(wheres.keySet())), true);
//
//        // 转换成MappingStructure
//        List<Structure> mss = new ArrayList<Structure>();
//        for (Structure structure : structures) {
//            if (Opreator.isReverse(opreator)) {
//                mss.add(mapping.getOMappingStructure(wrapMappingStructure(mapping, Arrays.asList((TableStructure)structure))));
//            } else {
//                mss.add(wrapMappingStructure(mapping, Arrays.asList((TableStructure)structure)));
//            }
//        }
//
//        return mss;
//    }
//
//    private List<Column> getSortedColumns(String tableName, List<String> columns) {
//        List<Column> ws = new ArrayList<Column>();
//        Table t = new Table();
//        t.setName(tableName);
//        for (String cn : columns) {
//            ws.add(new Column(t, cn));
//        }
//        Collections.sort(ws, ColumnComparator.COMPARATOR);
//        return ws;
//    }
//
//    private Object[] createIndex(String tableName, Map<String, Object> wheres) {
//        // 创建索引
//        List<Column> ws = getSortedColumns(tableName, new ArrayList<String>(wheres.keySet()));
//        Object[] index = new Object[ws.size() + 1];
//        index[0] = tableName;
//        for (int i = 1; i < ws.size(); i++) {
//            index[i] = ws.get(i).getColumnName();
//        }
//        return index;
//    }
//
//    private MappingStructure wrapMappingStructure(Mapping mapping, List<TableStructure> tableStructures) {
//        return new MappingStructure(mapping, tableStructures);
//    }
//
//    public void createMeta(Command command) {
//        if (command.getOpreator() == Opreator.CREATE || command.getOpreator() == Opreator.REVERSE_CREATE) {
//            MappingStructure mappingStructure = (MappingStructure) command.getStructure();
//            List<TableStructure> tables = mappingStructure.getTables();
//            for (TableStructure ts : tables) {
//                if (meta.containsKey(ts.getName())) {
//                    throw new RuntimeException("The table has already existed: " + ts.getName());
//                }
//                meta.put(ts.getName(), (TableStructure) ts.clone());
//            }
//        } else {
//            throw new UnsupportedOperationException("Unsupported operation types of create " + command.getOpreator());
//        }
//    }
//
//    public void dropMeta(Command command) {
//        if (command.getOpreator() == Opreator.DROP || command.getOpreator() == Opreator.REVERSE_DROP) {
//            MappingStructure mappingStructure = (MappingStructure) command.getStructure();
//            List<TableStructure> tables = mappingStructure.getTables();
//            for (TableStructure ts : tables) {
//                if (!meta.containsKey(ts.getName())) {
//                    throw new RuntimeException("The table does not exist: " + ts.getName());
//                }
//                meta.remove(ts.getName());
//            }
//        } else {
//            throw new UnsupportedOperationException("Unsupported operation types of drop " + command.getOpreator());
//        }
//    }
//
//    public void sortTableStructures(ArrayList<Structure> tableStructures, List<Column> columns, boolean asc) {
//        final List<String> cns = new ArrayList<String>();
//        for (Column column : columns) {
//            cns.add(column.getColumnName());
//        }
//        Collections.sort(tableStructures, new Comparator<Structure>() {
//            public int compare(Structure o1, Structure o2) {
//                if (o1 instanceof TableStructure && o2 instanceof TableStructure) {
//                    return ((TableStructure) o1).compareTo((TableStructure) o2, cns);
//                }
//                return 0;
//            }
//        });
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Class<? extends OpreatorRun> getOpreatorRunClass() {
//        return opreatorRunClass;
//    }
//
//    public void setOpreatorRunClass(Class<? extends OpreatorRun> opreatorRunClass) {
//        this.opreatorRunClass = opreatorRunClass;
//    }
//
//    public HashIndex getHashIndex() {
//        return hashIndex;
//    }
//
//    public void setHashIndex(HashIndex hashIndex) {
//        this.hashIndex = hashIndex;
//    }
//}
