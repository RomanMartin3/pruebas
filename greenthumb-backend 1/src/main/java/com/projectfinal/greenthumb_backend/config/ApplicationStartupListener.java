package com.projectfinal.greenthumb_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupListener.class);
    private final Environment environment;

    public ApplicationStartupListener(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = environment.getProperty("local.server.port");
        String appName = environment.getProperty("spring.application.name", "GreenThumb Backend"); // Toma el nombre de application.properties

        LOGGER.info("***************************************************************************");
        LOGGER.info("* *");
        LOGGER.info("* {} INICIADO Y CORRIENDO SATISFACTORIAMENTE!  *", String.format("%-60s", appName.toUpperCase()));
        if (port != null) {
            LOGGER.info("* Puerto del Servidor: {}                                                 *", String.format("%-58s", port));
            LOGGER.info("* Acceso local: http://localhost:{}                                      *", String.format("%-57s", port));
        } else {
            LOGGER.info("* Puerto del Servidor: (no disponible a través de 'local.server.port')    *");
        }
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (contextPath != null && !contextPath.isEmpty() && !contextPath.equals("/")) {
            LOGGER.info("* Context Path: {}                                                        *", String.format("%-59s", contextPath));
            if(port != null) {
                LOGGER.info("* URL Completa (ej.): http://localhost:{}{}                               *", String.format("%-50s", port, contextPath));
            }
        }
        LOGGER.info("* *");
        LOGGER.info("***************************************************************************");
    }
}