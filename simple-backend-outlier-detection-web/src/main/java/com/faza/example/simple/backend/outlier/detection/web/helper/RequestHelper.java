package com.faza.example.simple.backend.outlier.detection.web.helper;

import com.faza.example.simple.backend.outlier.detection.service.model.request.OutlierDetectionCommandRequest;
import com.faza.example.simple.backend.outlier.detection.web.model.request.OutlierDetectionRequest;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public class RequestHelper {

    public static OutlierDetectionCommandRequest createOutlierDetectionCommandRequest(
            OutlierDetectionRequest outlierDetectionRequest) {
        return OutlierDetectionCommandRequest.builder()
                .outlierDetectionRequest(outlierDetectionRequest)
                .build();
    }
}
