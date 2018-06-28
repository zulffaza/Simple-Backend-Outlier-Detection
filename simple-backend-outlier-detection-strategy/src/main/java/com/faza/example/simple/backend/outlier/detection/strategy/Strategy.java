package com.faza.example.simple.backend.outlier.detection.strategy;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public interface Strategy<REQUEST, RESPONSE> {

    RESPONSE execute(REQUEST request) throws Exception;
}
