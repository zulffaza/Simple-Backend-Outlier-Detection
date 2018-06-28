package com.faza.example.simple.backend.outlier.detection.service.model.request;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.strategy.ClusterStrategy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Data
@Builder
public class ClusterRequest {

    private Integer numberOfCluster;

    private List<Attribute> attributes;

    private ClusterStrategy clusterStrategy;
}
