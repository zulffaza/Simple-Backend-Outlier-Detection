package com.faza.example.simple.backend.outlier.detection.strategy.util;

import com.faza.example.simple.backend.outlier.detection.model.Attribute;
import com.faza.example.simple.backend.outlier.detection.model.AttributeDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public class AttributesHelper {

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SQUARED_NUMBER = 2;

    private static AttributesHelper instance;

    private AttributesHelper() {

    }

    public static AttributesHelper getInstance() {
        if (instance == null)
            instance = new AttributesHelper();

        return instance;
    }

    public void calculateAttributesDistances(List<Attribute> attributes) {
        List<Attribute> attributesCopy = new ArrayList<>(attributes);

        attributes.forEach(attribute ->
                calculateAttributeDistances(attribute, attributesCopy));
    }

    private void calculateAttributeDistances(Attribute attribute, List<Attribute> attributes) {
        attributes.forEach(attributeCopy ->
                calculateAttributeDistance(attribute, attributeCopy));
    }

    private void calculateAttributeDistance(Attribute attribute, Attribute attributeCopy) {
        if (!attribute.isIdEquals(attributeCopy.getId())) {
            AttributeDistance attributeDistance = createAttributeDistance(attribute, attributeCopy);
            attribute.addAttributeDistance(attributeDistance);
        }
    }

    private AttributeDistance createAttributeDistance(Attribute attribute, Attribute attributeCopy) {
        Integer id = attributeCopy.getId();
        Double distance = calculateDistance(attribute, attributeCopy);

        return AttributeDistance.builder()
                .id(id)
                .distance(distance)
                .build();
    }

    public Double calculateDistance(Attribute attribute, Attribute attributeCopy) {
        List<Double> result = subtractAllAttributes(attribute, attributeCopy);
        Attribute attributeResult = Attribute.builder(result).build();

        return calculateDistance(attributeResult);
    }

    private List<Double> subtractAllAttributes(Attribute attribute, Attribute attributeCopy) {
        return IntStream.range(FIRST_INDEX, attribute.getData().size())
                .boxed()
                .map(index ->
                        subtractAttribute(attribute, attributeCopy, index))
                .collect(Collectors.toList());
    }

    private Double subtractAttribute(Attribute attribute, Attribute attributeCopy, Integer index) {
        return attribute.getData().get(index) - attributeCopy.getData().get(index);
    }

    private Double calculateDistance(Attribute attribute) {
        Double distance = squareAllAttributes(attribute);

        return Math.sqrt(distance);
    }

    private Double squareAllAttributes(Attribute attribute) {
        return IntStream.range(FIRST_INDEX, attribute.getData().size())
                .mapToDouble(index ->
                        squareAttribute(attribute, index))
                .sum();
    }

    private Double squareAttribute(Attribute attribute, Integer index) {
        return Math.pow(attribute.getData().get(index), SQUARED_NUMBER);
    }
}
