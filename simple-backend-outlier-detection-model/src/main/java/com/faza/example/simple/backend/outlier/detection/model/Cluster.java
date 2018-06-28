package com.faza.example.simple.backend.outlier.detection.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Getter
@Setter
@EqualsAndHashCode
public class Cluster {

    private Integer id;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean needCentroid;

    @Setter(AccessLevel.NONE)
    private Attribute centroid;

    private List<Attribute> attributes;

    private List<ClusterDistance> clusterDistances;

    private Cluster(ClusterBuilder clusterBuilder) {
        this.id = clusterBuilder.id;
        this.needCentroid = clusterBuilder.needCentroid;
        this.attributes = clusterBuilder.attributes;
        this.clusterDistances = clusterBuilder.clusterDistances;

        sortAttributes();
        sortClusterDistances();

        buildCentroid();
    }

    public static class ClusterBuilder {

        private Integer id;

        private Boolean needCentroid = Boolean.FALSE;

        private List<Attribute> attributes;

        private List<ClusterDistance> clusterDistances = new ArrayList<>();

        private ClusterBuilder(List<Attribute> attributes) {
            this(0, attributes);
        }

        private ClusterBuilder(Integer id, List<Attribute> attributes) {
            this.id = id;
            this.attributes = attributes;
        }

        public ClusterBuilder needCentroid() {
            needCentroid = Boolean.TRUE;
            return this;
        }

        public Cluster build() {
            return new Cluster(this);
        }
    }

    public static ClusterBuilder builder(List<Attribute> attributes) {
        return new ClusterBuilder(attributes);
    }

    public static ClusterBuilder builder(Integer id, List<Attribute> attributes) {
        return new ClusterBuilder(id, attributes);
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
        sortAttributes();

        buildCentroid();
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
        sortAttributes();

        buildCentroid();
    }

    public List<ClusterDistance> getClusterDistances() {
        return clusterDistances;
    }

    public void addClusterDistance(ClusterDistance clusterDistance) {
        this.clusterDistances.add(clusterDistance);
        sortClusterDistances();
    }

    private void buildCentroid() {
        if (this.needCentroid) {
            this.centroid = createInitialCentroid();

            sumAllAttributesField();
            divideAttributesWithAttributesSize();
        }
    }

    private Attribute createInitialCentroid() {
        List<Double> centroidAttributes = createInitialAttributes();

        return Attribute.builder(centroidAttributes)
                .build();
    }

    private List<Double> createInitialAttributes() {
        Integer attributeSize = getAttributeSize();

        return new ArrayList<>(
                Collections.nCopies(attributeSize, 0.00));
    }

    private Integer getAttributeSize() {
        return this.attributes.stream()
                .max(Comparator.comparingDouble(this::getBytesSize))
                .map(this::getBytesSize)
                .orElse(null);
    }

    private Integer getBytesSize(Attribute attribute) {
        return attribute.getData().size();
    }

    private void sumAllAttributesField() {
        this.attributes.forEach(this::sumCentroidAttributes);
    }

    private void sumCentroidAttributes(Attribute attribute) {
        for (Integer i = 0; i < attribute.getData().size(); i++)
            sumCentroidAttribute(attribute, i);
    }

    private void sumCentroidAttribute(Attribute attribute, Integer index) {
        Double newValue = this.centroid.getData().get(index) + attribute.getData().get(index);
        this.centroid.getData().set(index, newValue);
    }

    private void divideAttributesWithAttributesSize() {
        Integer attributesSize = this.attributes.size();
        Integer centroidAttributesSize = this.centroid.getData().size();

        for (Integer i = 0; i < centroidAttributesSize; i++)
            divideAttributeWithAttributesSize(attributesSize, i);
    }

    private void divideAttributeWithAttributesSize(Integer attributesSize, Integer index) {
        Double newValue = this.centroid.getData().get(index) / attributesSize;
        this.centroid.getData().set(index, newValue);
    }

    private void sortAttributes() {
        this.attributes.sort(
                Comparator.comparingInt(Attribute::getId));
    }

    private void sortClusterDistances() {
        this.clusterDistances.sort(
                Comparator.comparingInt(ClusterDistance::getId));
    }

    public Boolean isIdEquals(Integer searchId) {
        return this.id.equals(searchId);
    }

    @Override
    public String toString() {
        return "Cluster-" + this.id;
    }
}
