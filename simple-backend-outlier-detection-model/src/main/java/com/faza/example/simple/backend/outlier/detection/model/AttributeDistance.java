package com.faza.example.simple.backend.outlier.detection.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Data
@Builder
public class AttributeDistance {

    private Integer id;

    private Double distance;
}
