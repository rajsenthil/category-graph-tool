package com.divvet.store.categorygraph.dto;

import lombok.Data;

import java.util.List;

@Data
public class VertexRelation {
    String relationName;
    Vertex from;
    List<Vertex> to;
}
