package com.my.column.entity;

import java.util.List;
import java.util.Map;

public class MovieUserMatrix {
    public Map<String,Integer> movieIndexMap ;
    public Map<String,Integer> userIndexMap;

    public List<List<Integer>> matrix;

    public MovieUserMatrix(Map<String, Integer> movieIndexMap, Map<String, Integer> userIndexMap, List<List<Integer>> matrix) {
        this.movieIndexMap = movieIndexMap;
        this.userIndexMap = userIndexMap;
        this.matrix = matrix;
    }
}
