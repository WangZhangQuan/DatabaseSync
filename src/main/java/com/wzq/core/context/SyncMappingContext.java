package com.wzq.core.context;

import com.wzq.core.connector.Connector;
import com.wzq.manager.MappingManager;
import com.wzq.mapping.Mapping;
import com.wzq.sql.structure.MappingStructure;

public class SyncMappingContext extends SyncContext {

    private Mapping mapping;
    private MappingStructure mappingStructure;

    public SyncMappingContext(MappingManager mappingManager, Connector connector, Mapping mapping, MappingStructure mappingStructure) {
        super(mappingManager, connector);
        this.mapping = mapping;
        this.mappingStructure = mappingStructure;
    }

    public SyncMappingContext(SyncContext syncContext, Mapping mapping, MappingStructure mappingStructure) {
        super(syncContext.getMappingManager(), syncContext.getConnector());
        this.mapping = mapping;
        this.mappingStructure = mappingStructure;
        valueOf(syncContext);
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public MappingStructure getMappingStructure() {
        return mappingStructure;
    }

    public void setMappingStructure(MappingStructure mappingStructure) {
        this.mappingStructure = mappingStructure;
    }

    public void valueOf(Context context) {
        super.valueOf(context);
        if (context instanceof SyncMappingContext) {
            mapping = ((SyncMappingContext) context).mapping;
            this.mappingStructure = ((SyncMappingContext) context).mappingStructure;
        }
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
