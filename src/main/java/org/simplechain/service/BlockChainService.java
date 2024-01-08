package org.simplechain.service;

import org.simplechain.blockchain.Blockchain;
import org.simplechain.blockchain.Transaction;
import org.simplechain.blockchain.Wallet;
import org.simplechain.vo.TransactionParam;
import org.simplechain.vo.WalletVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BlockChainService {
    public boolean minerStatus = false;

    @Resource
    Blockchain blockchain;

    public WalletVo generateNewWallet() {
        Wallet wallet = new Wallet();
        return new WalletVo(wallet.getPublicKeyString(), wallet.getPrivateKeyString());
    }

    public void minerStart(String publicKey) {
        minerStatus = true;
        mine(publicKey);
    }

    @Async
    public void mine(String publicKey) {
        while (minerStatus) {
            // 挖矿，创建新的区块
            blockchain.minePendingTransactions(publicKey);
        }
    }

    public void minerStop() {
        minerStatus = false;
    }

    public void newTransaction(TransactionParam param) {
        Transaction transaction = new Transaction(param.getSender(), param.getRecipient(), param.getAmount(), null);
        transaction.generateSignature(param.getPrivateKey());
        transaction.verifySignature();

        blockchain.addTransaction(transaction);
    }

    public Blockchain getFullBlockChain() {
        return blockchain;
    }

    public double getBalance(String publicKey) {
        return blockchain.getBalance(publicKey);
    }
}
