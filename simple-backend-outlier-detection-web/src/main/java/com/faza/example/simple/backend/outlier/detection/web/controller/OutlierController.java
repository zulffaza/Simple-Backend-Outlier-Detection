package com.faza.example.simple.backend.outlier.detection.web.controller;

import com.faza.example.simple.backend.outlier.detection.service.command.executor.CommandExecutor;
import com.faza.example.simple.backend.outlier.detection.service.command.implementation.ClusterBandCommandImpl;
import com.faza.example.simple.backend.outlier.detection.service.model.request.ClusterCommandRequest;
import com.faza.example.simple.backend.outlier.detection.web.helper.RequestHelper;
import com.faza.example.simple.backend.outlier.detection.web.helper.ResponseHelper;
import com.faza.example.simple.backend.outlier.detection.web.model.request.ClusterRequest;
import com.faza.example.simple.backend.outlier.detection.web.model.response.ApiResponse;
import com.faza.example.simple.backend.outlier.detection.web.model.response.ClusterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@RestController
@RequestMapping("/outlier")
public class OutlierController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping(
            value = "/detect",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiResponse<ClusterResponse> outlierDetection(@Valid ClusterRequest clusterRequest) throws Exception {
        ClusterCommandRequest clusterCommandRequest = RequestHelper.createClusterCommandRequest(clusterRequest);
        Optional<ClusterResponse> clusterResponse = commandExecutor.doExecute(ClusterBandCommandImpl.class,
                clusterCommandRequest);

        return ResponseHelper.createResponse(
                clusterResponse.orElse(null));
    }
}
