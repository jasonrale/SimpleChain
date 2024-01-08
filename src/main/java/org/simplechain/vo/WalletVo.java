package org.simplechain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletVo {
    public String publicKey;

    public String privateKey;
}
