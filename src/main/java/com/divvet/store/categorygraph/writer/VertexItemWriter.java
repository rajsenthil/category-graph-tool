package com.divvet.store.categorygraph.writer;

import com.divvet.store.categorygraph.dto.Vertex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class VertexItemWriter implements ItemWriter<Vertex> {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void write(List<? extends Vertex> items) throws Exception {
        log.info("Writing items of size: {}", items.size());
        items.forEach(item -> {
//            restTemplate.postForEntity("url", item, Vertex.class);
            log.info("Vertex: id={}, name={}, value={}", item.getId(), item.getName(), item.getValue());
        });
    }
}
