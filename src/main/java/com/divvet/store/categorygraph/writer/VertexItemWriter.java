package com.divvet.store.categorygraph.writer;

import com.divvet.store.categorygraph.dto.Vertex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class VertexItemWriter implements ItemWriter<Vertex> {

    @Autowired
    RestTemplate restTemplate;

    @Value("${rest_url}")
    String restUrl;

    @Override
    public void write(List<? extends Vertex> items) throws Exception {
        log.info("Writing items of size: {}", items.size());
        items.forEach(v -> {
            Vertex result = null;
            final List<Vertex> c = v.getChildren();
//            final String postUrl = restUrl + "/" + v.getId();
            if (c != null && c.size() > 1) {
                final Vertex vertex = c.get(c.size() - 1);
                log.info("Vertex has child and the last element name: {} is created with id: {}"
                        , v.getId(), vertex.getName());
                result = restTemplate.postForEntity(restUrl, vertex, Vertex.class).getBody();
            } else {
                result = restTemplate.postForEntity(restUrl, v, Vertex.class).getBody();
            }
            log.info("Result vertex: id={}, name={} i created", v.getId(), v.getName());
        });
    }

    private Vertex vertexExists(Long id) {
        Vertex vertex = null;
        try {
            vertex = restTemplate.getForEntity(restUrl, Vertex.class, id).getBody();
        } catch (RestClientException e) {
            log.error("Vertex not found: ", e);
            e.printStackTrace();
        }
        return vertex;
    }

    private void addLastRelation(Vertex vertex) {

    }
}
