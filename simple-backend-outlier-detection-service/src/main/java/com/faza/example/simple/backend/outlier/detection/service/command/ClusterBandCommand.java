package com.faza.example.simple.backend.outlier.detection.service.command;

import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterCommandRequest;
import com.faza.example.simple.backend.outlier.detection.web.model.response.ClusterResponse;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public interface ClusterBandCommand extends Command<ClusterCommandRequest, ClusterResponse> {

}
