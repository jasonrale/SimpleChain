package org.simplechain.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.security.Security;

@Component
public class SecurityProviderConfig {
    @PostConstruct
    public void init(){
        Security.addProvider(new BouncyCastleProvider());
    }
}
