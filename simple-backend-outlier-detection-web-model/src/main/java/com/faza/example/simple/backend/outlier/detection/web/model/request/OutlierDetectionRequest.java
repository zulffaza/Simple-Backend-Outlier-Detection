package com.faza.example.simple.backend.outlier.detection.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDetectionRequest {

    @NotNull
    private MultipartFile file;

    @NotNull
    private Integer numberOfCluster;

    @NotNull
    private Double multipleAverage;
}
