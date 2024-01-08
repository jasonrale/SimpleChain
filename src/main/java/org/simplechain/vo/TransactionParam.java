package org.simplechain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
public class TransactionParam {
    public String sender;
    public String recipient;
    public double amount;
    public PrivateKey privateKey;
}
