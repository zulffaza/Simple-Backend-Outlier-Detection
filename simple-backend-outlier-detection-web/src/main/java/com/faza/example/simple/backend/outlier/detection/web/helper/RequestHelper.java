package com.faza.example.simple.backend.outlier.detection.web.helper;

import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterCommandRequest;
import com.faza.example.simple.backend.outlier.detection.web.model.request.ClusterRequest;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public class RequestHelper {

    public static ClusterCommandRequest createClusterCommandRequest(ClusterRequest clusterRequest) {
        return ClusterCommandRequest.builder()
                .clusterRequest(clusterRequest)
                .build();
    }
}
