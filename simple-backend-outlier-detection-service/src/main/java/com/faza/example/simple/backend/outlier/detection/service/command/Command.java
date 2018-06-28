package com.faza.example.simple.backend.outlier.detection.service.command;

import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public interface Command<REQUEST, RESPONSE> {

    Optional<RESPONSE> execute(REQUEST request) throws Exception;
}
