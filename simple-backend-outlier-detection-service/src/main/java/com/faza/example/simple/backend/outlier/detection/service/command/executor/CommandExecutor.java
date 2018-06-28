package com.faza.example.simple.backend.outlier.detection.service.command.executor;

import com.faza.example.simple.backend.outlier.detection.service.command.Command;

import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

public interface CommandExecutor {

    <REQUEST, RESPONSE> Optional<RESPONSE> doExecute(
            Class<? extends Command<REQUEST, RESPONSE>> commandClass, REQUEST request) throws Exception;
}
