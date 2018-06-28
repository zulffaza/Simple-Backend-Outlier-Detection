package com.faza.example.simple.backend.outlier.detection.strategy.util;

import com.faza.example.simple.backend.outlier.detection.model.Cluster;
import com.faza.example.simple.backend.outlier.detection.model.ClusterDistance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public class ClustersHelper {

    private static final Integer FIRST_INDEX = 0;

    private static ClustersHelper instance;

    private ClustersHelper() {

    }

    public static ClustersHelper getInstance() {
        if (instance == null)
            instance = new ClustersHelper();

        return instance;
    }

    public void buildNewCluster(List<Cluster> clusters, Integer numberOfCluster) {
        List<Integer> mergedCluster = new ArrayList<>();
        clusters.forEach(this::changedClusterDistances);

        for (Integer index = FIRST_INDEX; index < clusters.size(); index++) {
            if (clusters.size() == numberOfCluster)
                break;

            mergeNearestClusters(clusters, clusters.get(index), mergedCluster);
        }
    }

    private void changedClusterDistances(Cluster cluster) {
        ClusterDistance minClusterDistance = cluster.getClusterDistances()
                .stream()
                .min(Comparator.comparingDouble(ClusterDistance::getDistance))
                .orElse(null);

        cluster.getClusterDistances().clear();
        cluster.getClusterDistances().add(minClusterDistance);
    }

    private void mergeNearestClusters(List<Cluster> clusters, Cluster cluster, List<Integer> mergedCluster) {
        Integer clusterId = cluster.getId();
        Integer nearestClusterId = cluster.getClusterDistances().get(FIRST_INDEX).getId();

        if (isAlreadyMerged(mergedCluster, clusterId, nearestClusterId)) {
            mergeNearestCluster(cluster, nearestClusterId, clusters);

            mergedCluster.add(clusterId);
            mergedCluster.add(nearestClusterId);
        }
    }

    private Boolean isAlreadyMerged(List<Integer> mergedCluster, Integer clusterId,
                                    Integer nearestClusterId) {
        return !mergedCluster.contains(clusterId)
                && !mergedCluster.contains(nearestClusterId);
    }

    private void mergeNearestCluster(Cluster cluster, Integer nearestClusterId, List<Cluster> clusters) {
        Cluster nearestCluster = findCluster(nearestClusterId, clusters);
        nearestCluster.getAttributes()
                .forEach(cluster::addAttribute);
        clusters.remove(nearestCluster);
    }

    private Cluster findCluster(Integer clusterId, List<Cluster> clusters) {
        return clusters.stream()
                .filter(cluster -> cluster.isIdEquals(clusterId))
                .findFirst()
                .orElse(null);
    }
}
