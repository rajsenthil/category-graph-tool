package com.divvet.store.categorygraph.mapper;

import com.divvet.store.categorygraph.dto.Vertex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomLineMapper<T> extends DefaultLineMapper<T> {
//    @Autowired
    LineTokenizer lineTokenizer;

//    @Autowired
    FieldSetMapper fieldSetMapper;

    @Autowired
    public CustomLineMapper(LineTokenizer lineTokenizer, FieldSetMapper fieldSetMapper) {
        super.setLineTokenizer(lineTokenizer);
        super.setFieldSetMapper(fieldSetMapper);
        this.lineTokenizer = lineTokenizer;
        this.fieldSetMapper = fieldSetMapper;
    }

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        log.info("line #: {}=>{}", lineNumber, line);
        setLineTokenizer(lineTokenizer);
        final FieldSet tokenize = this.lineTokenizer.tokenize(line);
        final int fieldCount = tokenize.getFieldCount();

        if (fieldCount > 1) {

        }
        return (T) this.fieldSetMapper.mapFieldSet(tokenize);
    }

    public LineTokenizer getLineTokenizer() {
        return lineTokenizer;
    }

    @Override
    public void setLineTokenizer(LineTokenizer lineTokenizer) {
        super.setLineTokenizer(lineTokenizer);
        this.lineTokenizer = lineTokenizer;
    }

    public FieldSetMapper<Vertex> getFieldSetMapper() {
        return fieldSetMapper;
    }

    @Override
    public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
        super.setFieldSetMapper(fieldSetMapper);
    }
}
