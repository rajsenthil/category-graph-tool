package com.divvet.store.categorygraph.writer;

import com.divvet.store.categorygraph.dto.Vertex;
import com.divvet.store.categorygraph.dto.VertexRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class VertexItemWriter implements ItemWriter<Vertex> {

    @Autowired
    RestTemplate restTemplate;

    @Value("${rest_url}")
    String restUrl;

    private static final String RELATION_NAME = "sub-category";

    @Override
    public void write(List<? extends Vertex> items) throws Exception {
        log.info("Writing items of size: {}", items.size());
        items.forEach(v -> {
            Vertex result = null;
            final List<Vertex> c = v.getChildren();
//            final String postUrl = restUrl + "/" + v.getId();
            if (c != null && c.size() >= 1) {
                final Vertex vertex = c.get(c.size() - 1);
                log.info("Vertex has child and the last element name: {} is created with id: {}"
                        , v.getId(), vertex.getName());
                vertex.setId(v.getId()); // the id assigned to the last child
                result = restTemplate.postForEntity(restUrl, vertex, Vertex.class).getBody();
                addLastRelation(v);
            } else {
                result = restTemplate.postForEntity(restUrl, v, Vertex.class).getBody();
            }
            Vertex result2 = restTemplate.getForEntity(restUrl+"/"+v.getId(), Vertex.class).getBody();
            log.info("Result vertex: id={}, name={} is created", v.getId(), v.getName());
        });
    }

    private Vertex searchExists(String label) {
        Vertex vertex = null;
        try {
            vertex = restTemplate.getForEntity(restUrl+"/search/"+label, Vertex.class, label).getBody();
        } catch (RestClientException e) {
            log.error("Vertex not found: ", e);
        }
        return vertex;
    }

    private void addLastRelation(Vertex vertex) {
        final List<Vertex> children = vertex.getChildren();
        Vertex parent = null;
        Vertex child = null;
        if (children.size() > 1) {
            parent = children.get(children.size() - 2);
            child = children.get(children.size() - 1);
        } else if (children.size() == 1) {
            parent = vertex;
            child = children.get(0);
        }

        final Vertex matchedVertex = searchExists(parent.getName());
        if (matchedVertex.getId() == null) {
            log.error("Parent does not exists fof vertex => id: {}, label: {}", vertex.getId(), vertex.getName());
            throw new RuntimeException("Parent does not exists fof vertex => id: " + vertex.getId() + ", label: "+vertex.getName());
        }

        final VertexRelation vertexRelation = buildVertexRelation(matchedVertex, child);
        final Boolean result = restTemplate.postForEntity(restUrl + "/relation", vertexRelation, Boolean.class).getBody();
        if (result) {
            log.info("Added relation {} from Vertex: [id={},label={}] to Vertex: [id={}, label={}]"
                    , RELATION_NAME, vertexRelation.getFrom().getId(), vertexRelation.getFrom().getName(), child.getId(), child.getName());
        } else {
            log.error("Error in adding relation {} from Vertex: [id={},label={}] to Vertex: [id={}, label={}]"
                    , RELATION_NAME, vertexRelation.getFrom().getId(), vertexRelation.getFrom().getName(), child.getId(), child.getName());
        }
    }

    private VertexRelation buildVertexRelation(Vertex from, Vertex to) {
        final VertexRelation vertexRelation = new VertexRelation();
        vertexRelation.setRelationName(RELATION_NAME);
        vertexRelation.setFrom(from);
        List<Vertex> toList = new ArrayList<>();
        toList.add(to);
        vertexRelation.setTo(toList);
        return vertexRelation;
    }
}
