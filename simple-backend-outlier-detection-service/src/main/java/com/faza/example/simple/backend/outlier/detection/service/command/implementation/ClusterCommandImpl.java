package com.faza.example.simple.backend.outlier.detection.service.command.implementation;

import com.faza.example.simple.backend.outlier.detection.model.Cluster;
import com.faza.example.simple.backend.outlier.detection.service.command.ClusterCommand;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.response.ClusterResponse;
import com.faza.example.simple.backend.outlier.detection.strategy.model.request.ClusterStrategyRequest;
import com.faza.example.simple.backend.outlier.detection.strategy.model.response.ClusterStrategyResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Service
public class ClusterCommandImpl implements ClusterCommand {

    @Override
    public Optional<ClusterResponse> execute(ClusterRequest clusterRequest) throws Exception {
        ClusterStrategyRequest clusterStrategyRequest = ClusterStrategyRequest.builder()
                .attributes(clusterRequest.getAttributes())
                .numberOfCluster(clusterRequest.getNumberOfCluster())
                .build();
        ClusterStrategyResponse clusterStrategyResponse = clusterRequest.getClusterStrategy()
                .execute(clusterStrategyRequest);

        return Optional.of(createClusterResponse(
                clusterStrategyResponse.getClusters()));
    }

    private ClusterResponse createClusterResponse(List<Cluster> clusters) {
        return ClusterResponse.builder()
                .clusters(clusters)
                .build();
    }
}
