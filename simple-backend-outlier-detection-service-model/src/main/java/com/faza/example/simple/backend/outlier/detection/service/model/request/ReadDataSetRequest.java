package com.faza.example.simple.backend.outlier.detection.service.model.request;

import lombok.Builder;
import lombok.Data;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Data
@Builder
public class ReadDataSetRequest {

    private String[] inputs;
}
