package com.divvet.store.categorygraph;

import com.divvet.store.categorygraph.dto.Vertex;
import com.divvet.store.categorygraph.mapper.CustomLineMapper;
import com.divvet.store.categorygraph.mapper.VertexFieldSetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Slf4j
public class CategoryGraphToolApplication {

	@Value("${delimiter}")
	String delimiter;

	@Value("${names}")
	String[] names;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
/*

	@Bean
	public LineTokenizer getLineTokenizer() {
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(delimiter);
		lineTokenizer.setNames(names);
		log.info("Line Tokenizer initialized");
		return lineTokenizer;
	}
*/

	@Bean(name = "vertex")
	public Vertex getVertex() {
		return new Vertex();
	}

	public static void main(String[] args) {
		SpringApplication.run(CategoryGraphToolApplication.class, args);
	}

}
