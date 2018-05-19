package com.wzq.core.context;

import com.wzq.core.connector.Connector;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;
import com.wzq.mapping.TableMapping;
import com.wzq.sql.structure.MappingStructure;
import com.wzq.sql.structure.TableStructure;

import java.util.List;

public class SyncTableContext extends SyncMappingContext {

    private List<TableMapping> tableMappings;
    private TableStructure tableStructure;

    public SyncTableContext(MappingManager mappingManager, Connector connector, Mapping mapping, List<TableMapping> tableMappings, MappingStructure mappingStructure, TableStructure tableStructure) {
        super(mappingManager, connector, mapping, mappingStructure);
        this.tableMappings = tableMappings;
        this.tableStructure = tableStructure;
    }

    public SyncTableContext(SyncMappingContext syncMappingContext, List<TableMapping> tableMappings,TableStructure tableStructure) {
        super(syncMappingContext.getMappingManager(), syncMappingContext.getConnector(), syncMappingContext.getMapping(), syncMappingContext.getMappingStructure());
        this.tableMappings = tableMappings;
        this.tableStructure = tableStructure;
        valueOf(syncMappingContext);
    }

    public List<TableMapping> getTableMappings() {
        return tableMappings;
    }

    public void setTableMappings(List<TableMapping> tableMappings) {
        this.tableMappings = tableMappings;
    }

    public TableStructure getTableStructure() {
        return tableStructure;
    }

    public void setTableStructure(TableStructure tableStructure) {
        this.tableStructure = tableStructure;
    }

    @Override
    public void valueOf(Context context) {
        super.valueOf(context);
        if (context instanceof SyncTableContext) {
            tableMappings = ((SyncTableContext) context).tableMappings;
            this.tableStructure = ((SyncTableContext) context).tableStructure;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
