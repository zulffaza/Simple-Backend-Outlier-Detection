package com.faza.example.simple.backend.outlier.detection.service.command;

import com.faza.example.simple.backend.outlier.detection.service.model.request.OutlierDetectionCommandRequest;
import com.faza.example.simple.backend.outlier.detection.web.model.response.OutlierDetectionResponse;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public interface OutlierDetectionCommand extends Command<OutlierDetectionCommandRequest, OutlierDetectionResponse> {

}
