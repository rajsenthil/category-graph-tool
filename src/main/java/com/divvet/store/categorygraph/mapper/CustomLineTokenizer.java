package com.divvet.store.categorygraph.mapper;

import io.micrometer.core.lang.Nullable;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomLineTokenizer extends DelimitedLineTokenizer {
/*

    @Value("${delimiter}")
    String delimiter;
*/

    FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

    public CustomLineTokenizer(@Value("${delimiter}")String delimiter) {
        super();
        setDelimiter(delimiter);
    }

    @Override
    public FieldSet tokenize(@Nullable String line) {

        if (line == null) {
            line = "";
        }

        List<String> tokens = new ArrayList<>(doTokenize(line));

        String[] values = tokens.toArray(new String[tokens.size()]);


        if (names.length == 0) {
            return fieldSetFactory.create(values);
        }
        else if (values.length != names.length) {
            throw new IncorrectTokenCountException(names.length, values.length, line);
        }
        return fieldSetFactory.create(values, names);
    }
}
