package com.divvet.store.categorygraph.mapper;

import com.divvet.store.categorygraph.dto.Vertex;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
public class VertexFieldSetMapper implements FieldSetMapper {
    @Override
    public Vertex mapFieldSet(FieldSet fieldSet) throws BindException {
        Vertex vertex = new Vertex();
        vertex.setId(fieldSet.readLong(0));
        final int fieldCount = fieldSet.getFieldCount();
        StringBuilder stringBuilder = new StringBuilder();
        if (fieldCount > 1) {
            for(int index = 1; index < fieldCount; index++) {
                stringBuilder.append(fieldSet.readString(index)).append("|");
            }
        }
        vertex.setValue(stringBuilder.toString());
        return vertex;
    }
}
