package com.faza.example.simple.backend.outlier.detection.web.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Data
@Builder
public class OutlierDetectionResponse {

    @JsonProperty("outliers")
    private List<Integer> outliers;
}
