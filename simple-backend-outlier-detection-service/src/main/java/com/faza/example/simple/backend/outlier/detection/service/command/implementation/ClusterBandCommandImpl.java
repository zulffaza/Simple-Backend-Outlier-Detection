package com.faza.example.simple.backend.outlier.detection.service.command.implementation;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.model.Cluster;
import com.faza.example.simple.backend.outlier.detection.service.command.ClusterBandCommand;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterCommandRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ReadDataSetRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.response.ReadDataSetResponse;
import com.faza.example.simple.backend.outlier.detection.strategy.ClusterStrategy;
import com.faza.example.simple.backend.outlier.detection.strategy.implementation.CentroidLinkageStrategy;
import com.faza.example.simple.backend.outlier.detection.web.model.response.ClusterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Service
public class ClusterBandCommandImpl implements ClusterBandCommand {

    private static final Integer FIRST_INDEX = 0;

    @Autowired
    private ReadDataSetCommand readDataSetCommand;

    @Autowired
    private ClusterCommand clusterCommand;

    @Override
    public Optional<ClusterResponse> execute(ClusterCommandRequest clusterCommandRequest) throws Exception {
        List<String> attributes = mergeNumberOfCluster(clusterCommandRequest);
        String[] attributesRequest = changeIntoArray(attributes);
        ReadDataSetResponse readDataSetResponse = readDataSetFromInputs(attributesRequest);
        List<Cluster> clusters = createCluster(readDataSetResponse);
        List<String> clusterLines = createClusterLines(clusters);

        return Optional.of(createClusterResponse(clusterLines));
    }

    private List<String> mergeNumberOfCluster(ClusterCommandRequest clusterCommandRequest) {
        clusterCommandRequest.getClusterRequest().getAttributes()
                .add(FIRST_INDEX, clusterCommandRequest.getClusterRequest().getNumberOfCluster().toString());

        return clusterCommandRequest.getClusterRequest().getAttributes();
    }

    private String[] changeIntoArray(List<String> attributes) {
        String[] inputs = new String[attributes.size()];

        IntStream.range(0, attributes.size())
                .forEach(index -> inputs[index] = attributes.get(index));

        return inputs;
    }

    private ReadDataSetResponse readDataSetFromInputs(String[] attributesRequest) {
        ReadDataSetRequest readDataSetRequest = createReadDataSetRequest(attributesRequest);

        return readDataSetCommand.execute(readDataSetRequest)
                .get();
    }

    private ReadDataSetRequest createReadDataSetRequest(String[] inputs) {
        return ReadDataSetRequest.builder()
                .inputs(inputs)
                .build();
    }

    private List<Cluster> createCluster(ReadDataSetResponse readDataSetResponse) throws Exception {
        ClusterRequest clusterRequest = createClusterRequest(readDataSetResponse.getNumberOfCluster(),
                readDataSetResponse.getAttributes(), CentroidLinkageStrategy.getInstance());

        return clusterCommand.execute(clusterRequest)
                .get()
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

    private List<String> createClusterLines(List<Cluster> clusters) {
        List<String> clusterLines = new ArrayList<>();

        clusters.forEach(cluster -> createLines(cluster, clusterLines));

        return clusterLines;
    }

    private void createLines(Cluster cluster, List<String> clusterLines) {
        StringBuilder lines = new StringBuilder();

        cluster.getAttributes()
                .forEach(attribute -> appendAttribute(attribute, lines));

        clusterLines.add(lines.toString().trim());
    }

    private void appendAttribute(Attribute attribute, StringBuilder lines) {
        lines.append(attribute.getId());
        lines.append(" ");
    }

    private ClusterResponse createClusterResponse(List<String> clusters) {
        return ClusterResponse.builder()
                .clusters(clusters)
                .build();
    }
}
