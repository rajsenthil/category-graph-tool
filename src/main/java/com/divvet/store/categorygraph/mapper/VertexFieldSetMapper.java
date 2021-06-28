package com.divvet.store.categorygraph.mapper;

import com.divvet.store.categorygraph.dto.Vertex;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@Component
public class VertexFieldSetMapper implements FieldSetMapper {
    @Override
    public Vertex mapFieldSet(FieldSet fieldSet) throws BindException {
        Vertex vertex = new Vertex();
        vertex.setId(fieldSet.readLong(0)*1000L); // Always end with trailing 1000 zeros
        final int fieldCount = fieldSet.getFieldCount();
        if (fieldCount < 2) throw new RuntimeException("Data is not valid. There is no vertiex to handle");
//        StringBuilder stringBuilder = new StringBuilder();
        vertex.setName(fieldSet.readString(1));
        if (fieldCount > 2) {
            List<Vertex> children = new ArrayList<>();
            for(int index = 2; index < fieldCount; index++) {
                Vertex v = new Vertex();
                v.setName(fieldSet.readString(index));
//                stringBuilder.append(fieldSet.readString(index)).append("|");
                if (index > 1) children.add(v);
            }
            vertex.setChildren(children);
        }
//        vertex.setValue(stringBuilder.toString());
        return vertex;
    }
}
