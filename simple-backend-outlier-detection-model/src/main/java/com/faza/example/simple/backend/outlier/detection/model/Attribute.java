package com.faza.example.simple.backend.outlier.detection.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
public class Attribute {

    private Integer id;

    private List<Double> data;

    private List<AttributeDistance> attributeDistances;

    private Attribute(AttributeBuilder attributeBuilder) {
        this.id = attributeBuilder.id;
        this.data = attributeBuilder.data;
        this.attributeDistances = attributeBuilder.attributeDistances;

        sortAttributeDistance();
    }

    public static class AttributeBuilder {

        private Integer id;

        private List<Double> data;

        private List<AttributeDistance> attributeDistances = new ArrayList<>();

        private AttributeBuilder() {
            this(0, new ArrayList<>());
        }

        private AttributeBuilder(List<Double> data) {
            this(0, data);
        }

        private AttributeBuilder(Integer id, List<Double> data) {
            this.id = id;
            this.data = data;
        }

        public Attribute build() {
            return new Attribute(this);
        }
    }

    public static AttributeBuilder builder() {
        return new AttributeBuilder();
    }

    public static AttributeBuilder builder(List<Double> data) {
        return new AttributeBuilder(data);
    }

    public static AttributeBuilder builder(Integer id, List<Double> data) {
        return new AttributeBuilder(id, data);
    }

    public void addAttributeDistance(AttributeDistance attributeDistance) {
        this.attributeDistances.add(attributeDistance);
        sortAttributeDistance();
    }

    private void sortAttributeDistance() {
        this.attributeDistances.sort(
                Comparator.comparingInt(AttributeDistance::getId));
    }

    public Boolean isIdEquals(Integer searchId) {
        return this.id.equals(searchId);
    }

    @Override
    public String toString() {
        return "Attribute-" + this.id;
    }
}
