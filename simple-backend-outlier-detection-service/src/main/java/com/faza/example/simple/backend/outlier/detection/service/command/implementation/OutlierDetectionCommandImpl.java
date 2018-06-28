package com.faza.example.simple.backend.outlier.detection.service.command.implementation;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.model.Cluster;
import com.faza.example.simple.backend.outlier.detection.model.ClusterDistance;
import com.faza.example.simple.backend.outlier.detection.service.command.ClusterCommand;
import com.faza.example.simple.backend.outlier.detection.service.command.OutlierDetectionCommand;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.request.OutlierDetectionCommandRequest;
import com.faza.example.simple.backend.outlier.detection.strategy.ClusterStrategy;
import com.faza.example.simple.backend.outlier.detection.strategy.implementation.CentroidLinkageStrategy;
import com.faza.example.simple.backend.outlier.detection.strategy.util.AttributesHelper;
import com.faza.example.simple.backend.outlier.detection.web.model.response.OutlierDetectionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Service
public class OutlierDetectionCommandImpl implements OutlierDetectionCommand {

    private static final String WHITESPACE_REGEX = "\\s";

    private static final Integer ATTRIBUTES_ID_INITIAL_VALUE = 1;
    private static final Integer FIRST_INDEX = 0;

    private static final Double AVERAGES_DEFAULT_VALUE = 0.0;

    @Autowired
    private ClusterCommand clusterCommand;

    @Override
    public Optional<OutlierDetectionResponse> execute(OutlierDetectionCommandRequest outlierDetectionCommandRequest)
            throws Exception {
        List<String> lines = readLines(outlierDetectionCommandRequest.getOutlierDetectionRequest().getFile());
        List<Attribute> attributes = changeLinesIntoAttributes(lines);
        List<Cluster> clusters = createCluster(
                outlierDetectionCommandRequest.getOutlierDetectionRequest().getNumberOfCluster(), attributes);
        changeClusterDistances(clusters);

        List<Double> averageDistances = calculateClusterAverageDistances(clusters,
                outlierDetectionCommandRequest.getOutlierDetectionRequest().getMultipleAverage());
        List<Integer> outliers = findOutliers(clusters, averageDistances);

        return Optional.of(
                createOutlierDetectionResponse(outliers));
    }

    private List<String> readLines(MultipartFile file) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        return bufferedReader.lines()
                .collect(Collectors.toList());
    }

    private List<Attribute> changeLinesIntoAttributes(List<String> lines) {
        AtomicInteger id = new AtomicInteger(ATTRIBUTES_ID_INITIAL_VALUE);

        return lines.stream()
                .map(line -> createAttribute(id, line))
                .collect(Collectors.toList());
    }

    private Attribute createAttribute(AtomicInteger id, String line) {
        List<Double> data = parseWordsToDoubles(
                line.split(WHITESPACE_REGEX));

        return Attribute.builder(id.getAndIncrement(), data)
                .build();
    }

    private List<Double> parseWordsToDoubles(String[] words) {
        return Arrays.stream(words)
                .filter(this::stringIsNotEmpty)
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private Boolean stringIsNotEmpty(String string) {
        return !string.isEmpty();
    }

    private List<Cluster> createCluster(Integer numberOfCluster, List<Attribute> attributes) throws Exception {
        ClusterRequest clusterRequest = createClusterRequest(numberOfCluster,
                attributes, CentroidLinkageStrategy.getInstance());

        return clusterCommand.execute(clusterRequest)
                .orElseThrow(Exception::new)
                .getClusters();
    }

    private ClusterRequest createClusterRequest(Integer numberOfCluster, List<Attribute> attributes,
                                                ClusterStrategy clusterStrategy) {
        return ClusterRequest.builder()
                .numberOfCluster(numberOfCluster)
                .attributes(attributes)
                .clusterStrategy(clusterStrategy)
                .build();
    }

    private void changeClusterDistances(List<Cluster> clusters) {
        clusters.forEach(this::changeClusterDistance);
    }

    private void changeClusterDistance(Cluster cluster) {
        Attribute centroid = cluster.getCentroid();
        AttributesHelper attributesHelper = AttributesHelper.getInstance();

        cluster.getClusterDistances().clear();
        cluster.getAttributes().stream()
                .map(attribute -> createClusterDistance(attributesHelper, centroid, attribute))
                .forEach(cluster::addClusterDistance);
    }

    private ClusterDistance createClusterDistance(AttributesHelper attributesHelper, Attribute centroid,
                                                  Attribute attribute) {
        Double distance = attributesHelper.calculateDistance(centroid, attribute);

        return ClusterDistance.builder()
                .id(attribute.getId())
                .distance(distance)
                .build();
    }

    private List<Double> calculateClusterAverageDistances(List<Cluster> clusters, Double multiplyAverage) {
        return clusters.stream()
                .map(this::calculateAverageDistance)
                .map(averageDistance -> multiplyAverageDistances(averageDistance, multiplyAverage))
                .collect(Collectors.toList());
    }

    private Double calculateAverageDistance(Cluster cluster) {
        return cluster.getClusterDistances().stream()
                .map(ClusterDistance::getDistance)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(AVERAGES_DEFAULT_VALUE);
    }

    private Double multiplyAverageDistances(Double averageDistance, Double multiplyAverage) {
        return averageDistance * multiplyAverage;
    }

    private List<Integer> findOutliers(List<Cluster> clusters, List<Double> averageDistances) {
        List<Integer> outliers = new ArrayList<>();

        for (Integer index = FIRST_INDEX; index < clusters.size(); index++)
            addOutliers(outliers, clusters.get(index).getClusterDistances(), averageDistances.get(index));

        return outliers;
    }

    private void addOutliers(List<Integer> outliers, List<ClusterDistance> clusterDistances,
                             Double averageDistance) {
        clusterDistances.stream()
                .filter(clusterDistance -> isOutlier(clusterDistance, averageDistance))
                .forEach(clusterDistance -> addOutlier(clusterDistance, outliers));
    }

    private Boolean isOutlier(ClusterDistance clusterDistance, Double averageDistance) {
        return clusterDistance.getDistance() > averageDistance;
    }

    private void addOutlier(ClusterDistance clusterDistance, List<Integer> outliers) {
        outliers.add(clusterDistance.getId());
    }

    private OutlierDetectionResponse createOutlierDetectionResponse(List<Integer> outliers) {
        return OutlierDetectionResponse.builder()
                .outliers(outliers)
                .build();
    }
}
