package com.faza.example.simple.backend.outlier.detection.service.command.implementation;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.service.command.Command;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ReadDataSetRequest;
import com.faza.example.simple.backend.outlier.detection.service.model.response.ReadDataSetResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Service
public class ReadDataSetCommand implements Command<ReadDataSetRequest, ReadDataSetResponse> {

    private static final Integer NUMBER_OF_CLUSTER_INDEX = 0;
    private static final Integer FIRST_INDEX = 0;

    @Override
    public Optional<ReadDataSetResponse> execute(ReadDataSetRequest readDataSetRequest) {
        List<String> inputs = changeInputsIntoList(
                readDataSetRequest.getInputs());
        Integer numberOfCluster = getAndRemoveNumberOfClusterFromInputs(inputs);
        List<Attribute> attributes = createAttributesFromInput(inputs);

        return Optional.of(createReadDataSetResponse(
                numberOfCluster, attributes));
    }

    private List<String> changeInputsIntoList(String[] inputs) {
        return new ArrayList<>(Arrays.asList(inputs));
    }

    private Integer getAndRemoveNumberOfClusterFromInputs(List<String> inputs) {
        String numberOfCluster = inputs.remove(NUMBER_OF_CLUSTER_INDEX.intValue()).trim();
        return Integer.valueOf(numberOfCluster);
    }

    private List<Attribute> createAttributesFromInput(List<String> inputs) {
        return IntStream.range(FIRST_INDEX, inputs.size())
                .boxed()
                .map(id ->
                        createAttribute(id, inputs.get(id)))
                .collect(Collectors.toList());
    }

    private Attribute createAttribute(Integer id, String input) {
        List<Double> bytes = splitBytesFromInput(input);

        return Attribute.builder(++id, bytes)
                .build();
    }

    private List<Double> splitBytesFromInput(String input) {
        return Arrays.stream(input.split("-"))
                .map(String::trim)
                .map(Double::valueOf)
                .collect(Collectors.toList());
    }

    private ReadDataSetResponse createReadDataSetResponse(Integer numberOfCluster, List<Attribute> attributes) {
        return ReadDataSetResponse.builder()
                .numberOfCluster(numberOfCluster)
                .attributes(attributes)
                .build();
    }
}
