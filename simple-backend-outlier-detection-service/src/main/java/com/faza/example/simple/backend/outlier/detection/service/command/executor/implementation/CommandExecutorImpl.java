package com.faza.example.simple.backend.outlier.detection.service.command.executor.implementation;

import com.faza.example.simple.backend.outlier.detection.service.command.Command;
import com.faza.example.simple.backend.outlier.detection.service.command.executor.CommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 28 June 2018
 */

@Service
public class CommandExecutorImpl implements CommandExecutor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public <REQUEST, RESPONSE> Optional<RESPONSE> doExecute(
            Class<? extends Command<REQUEST, RESPONSE>> commandClass, REQUEST request) throws Exception {
        return applicationContext.getBean(commandClass).execute(request);
    }
}
