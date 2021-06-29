package com.divvet.store.categorygraph.config;

import com.divvet.store.categorygraph.dto.Vertex;
import com.divvet.store.categorygraph.mapper.CustomLineMapper;
import com.divvet.store.categorygraph.writer.VertexItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class AsyncBatchProcessor {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    VertexItemWriter vertexItemWriter;


    @Autowired
    CustomLineMapper customLineMapper;

    @Bean
    @StepScope
    public FlatFileItemReader<Vertex> fileVertexReader(
            @Value("${inputCsvFile}") Resource resource) {
        return new FlatFileItemReaderBuilder<Vertex>()
                .name("prod-definition")
                .saveState(true)
                .resource(resource)
                .lineMapper(customLineMapper).build();
    }

    @Bean
    public AsyncItemProcessor<Vertex, Vertex> asyncItemProcessor() {
        AsyncItemProcessor<Vertex, Vertex> processor = new AsyncItemProcessor<>();

        processor.setDelegate(processor());
        processor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return processor;
    }

    @Bean
    public AsyncItemWriter<Vertex> asyncItemWriter() {
        AsyncItemWriter<Vertex> writer = new AsyncItemWriter<>();

        writer.setDelegate(vertexItemWriter);

        return writer;
    }

    @Bean
    public ItemProcessor<Vertex, Vertex> processor() {
        return (transaction) -> {
            Thread.sleep(5);
            return transaction;
        };
    }

    @Bean
    public Job asyncJob() {
        return this.jobBuilderFactory.get("asyncJob")
                .start(step1async())
                .build();
    }

    @Bean
    public Step step1async() {
        return this.stepBuilderFactory.get("step1async")
                .<Vertex, Vertex>chunk(1)
                .reader(fileVertexReader(null))
                .processor((ItemProcessor) asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(AsyncBatchProcessor.class, args);
    }
}