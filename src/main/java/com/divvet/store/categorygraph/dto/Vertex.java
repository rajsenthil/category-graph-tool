package com.divvet.store.categorygraph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vertex {
    Long id;
    String value;
    String name;
    List<Vertex> children;
}
