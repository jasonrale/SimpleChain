package org.simplechain.blockchain;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Blockchain {
    private final ArrayList<Block> blockchain;
    private ArrayList<Transaction> pendingTransactions;
    private static final int miningReward = 10;
    private static final int totalSupply = 21000000;

    public Blockchain() {
        this.blockchain = new ArrayList<>();
        this.pendingTransactions = new ArrayList<>();
        // 创建创世区块
        addBlock(Block.Genesis());
    }

    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public void minePendingTransactions(String minerAddress) {
        Block lastBlock = getLastBlock();
        Block block = new Block(pendingTransactions, lastBlock);
        block.mineBlock(miningReward, minerAddress);
        blockchain.add(block);
        pendingTransactions = new ArrayList<>();
    }

    public double getBalance(String address) {
        double balance = 0;
        for (Block block : blockchain) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.getSender().equals(address)) {
                    balance -= transaction.getAmount();
                }
                if (transaction.getRecipient().equals(address)) {
                    balance += transaction.getAmount();
                }
            }
        }
        return balance;
    }

    private Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    private void addBlock(Block block) {
        blockchain.add(block);
    }
}