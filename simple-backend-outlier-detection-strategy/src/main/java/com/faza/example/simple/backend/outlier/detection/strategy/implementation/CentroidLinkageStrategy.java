package com.faza.example.simple.backend.outlier.detection.strategy.implementation;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.model.Cluster;
import com.faza.example.simple.backend.outlier.detection.model.ClusterDistance;
import com.faza.example.simple.backend.outlier.detection.strategy.ClusterStrategy;
import com.faza.example.simple.backend.outlier.detection.strategy.model.request.ClusterStrategyRequest;
import com.faza.example.simple.backend.outlier.detection.strategy.model.response.ClusterStrategyResponse;
import com.faza.example.simple.backend.outlier.detection.strategy.util.AttributesHelper;
import com.faza.example.simple.backend.outlier.detection.strategy.util.ClustersHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 11 May 2018
 */

public class CentroidLinkageStrategy implements ClusterStrategy {

    private static final Integer CLUSTER_INITIAL_ID = 1;

    private static CentroidLinkageStrategy instance;

    private CentroidLinkageStrategy() {

    }

    public static CentroidLinkageStrategy getInstance() {
        if (instance == null)
            instance = new CentroidLinkageStrategy();

        return instance;
    }

    @Override
    public ClusterStrategyResponse execute(ClusterStrategyRequest clusterStrategyRequest) {
        List<Cluster> clusters = createInitialCluster(
                clusterStrategyRequest.getAttributes());

        doCentroidLinkage(clusters, clusterStrategyRequest);

        return ClusterStrategyResponse.builder()
                .clusters(clusters)
                .build();
    }

    private List<Cluster> createInitialCluster(List<Attribute> attributes) {
        AtomicInteger id = new AtomicInteger(CLUSTER_INITIAL_ID);

        return attributes.stream()
                .map(attribute ->
                        buildCentroidInitialCluster(id.getAndIncrement(), attribute))
                .collect(Collectors.toList());
    }

    private Cluster buildCentroidInitialCluster(Integer id, Attribute attribute) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        return Cluster.builder(id, attributes)
                .needCentroid()
                .build();
    }

    private void doCentroidLinkage(List<Cluster> clusters, ClusterStrategyRequest clusterStrategyRequest) {
        ClustersHelper clustersHelper = ClustersHelper.getInstance();

        while (checkClustersSize(clusters, clusterStrategyRequest))
            doCentroidLinkageIteration(clustersHelper, clusterStrategyRequest.getNumberOfCluster(), clusters);
    }

    private Boolean checkClustersSize(List<Cluster> clusters, ClusterStrategyRequest clusterStrategyRequest) {
        return clusters.size() != clusterStrategyRequest.getNumberOfCluster();
    }

    private void doCentroidLinkageIteration(ClustersHelper clustersHelper, Integer numberOfCluster,
                                            List<Cluster> clusters) {
        clearCentroidsDistances(clusters);
        calculateCentroidsDistances(clusters);
        clustersHelper.buildNewCluster(clusters, numberOfCluster);
    }

    private void clearCentroidsDistances(List<Cluster> clusters) {
        clusters.forEach(cluster ->
                cluster.getClusterDistances().clear());
    }

    private void calculateCentroidsDistances(List<Cluster> clusters) {
        List<Cluster> clustersCopy = new ArrayList<>(clusters);

        clusters.forEach(cluster ->
                calculateCentroidDistances(cluster, clustersCopy));
    }

    private void calculateCentroidDistances(Cluster cluster, List<Cluster> clusters) {
        clusters.forEach(clusterCopy ->
                calculateCentroidDistance(cluster, clusterCopy));
    }

    private void calculateCentroidDistance(Cluster cluster, Cluster clusterCopy) {
        if (!isIrisIdEquals(cluster, clusterCopy)) {
            ClusterDistance clusterDistance = createClusterDistance(cluster, clusterCopy);
            cluster.addClusterDistance(clusterDistance);
        }
    }

    private Boolean isIrisIdEquals(Cluster cluster, Cluster clusterCopy) {
        return cluster.getId().equals(clusterCopy.getId());
    }

    private ClusterDistance createClusterDistance(Cluster cluster, Cluster clusterCopy) {
        Integer id = clusterCopy.getId();
        Double distance = AttributesHelper.getInstance()
                .calculateDistance(cluster.getCentroid(), clusterCopy.getCentroid());

        return ClusterDistance.builder()
                .id(id)
                .distance(distance)
                .build();
    }
}
