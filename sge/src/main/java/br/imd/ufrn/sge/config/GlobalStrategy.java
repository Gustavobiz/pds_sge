package br.imd.ufrn.sge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalStrategy {
    @Value("${tipo_aplicacao}")
    private String escolhaStrategy;

    public String getEscolhaStrategy() {
        return escolhaStrategy;
    }
}
