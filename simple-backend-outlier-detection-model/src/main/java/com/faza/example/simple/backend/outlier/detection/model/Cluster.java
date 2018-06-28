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

        sortPixels();
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
        sortPixels();

        buildCentroid();
    }

    public void addPixel(Attribute attribute) {
        this.attributes.add(attribute);
        sortPixels();

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

            sumAllPixelsField();
            dividePixelsWithPixelsSize();
        }
    }

    private Attribute createInitialCentroid() {
        List<Double> centroidPixels = createInitialPixels();

        return Attribute.builder(centroidPixels)
                .build();
    }

    private List<Double> createInitialPixels() {
        Integer pixelSize = getPixelSize();

        return new ArrayList<>(
                Collections.nCopies(pixelSize, 0.00));
    }

    private Integer getPixelSize() {
        return this.attributes.stream()
                .max(Comparator.comparingDouble(this::getBytesSize))
                .map(this::getBytesSize)
                .orElse(null);
    }

    private Integer getBytesSize(Attribute attribute) {
        return attribute.getDatas().size();
    }

    private void sumAllPixelsField() {
        this.attributes.forEach(this::sumCentroidPixels);
    }

    private void sumCentroidPixels(Attribute attribute) {
        for (Integer i = 0; i < attribute.getDatas().size(); i++)
            sumCentroidPixel(attribute, i);
    }

    private void sumCentroidPixel(Attribute attribute, Integer index) {
        Double newValue = this.centroid.getDatas().get(index) + attribute.getDatas().get(index);
        this.centroid.getDatas().set(index, newValue);
    }

    private void dividePixelsWithPixelsSize() {
        Integer pixelsSize = this.attributes.size();
        Integer centroidPixelsSize = this.centroid.getDatas().size();

        for (Integer i = 0; i < centroidPixelsSize; i++)
            dividePixelWithPixelsSize(pixelsSize, i);
    }

    private void dividePixelWithPixelsSize(Integer pixelsSize, Integer index) {
        Double newValue = this.centroid.getDatas().get(index) / pixelsSize;
        this.centroid.getDatas().set(index, newValue);
    }

    private void sortPixels() {
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
