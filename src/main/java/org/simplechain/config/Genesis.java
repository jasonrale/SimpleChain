package org.simplechain.config;

import org.simplechain.blockchain.Blockchain;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Genesis {
    @Bean
    public Blockchain myService() {
        return new Blockchain();
    }
}
