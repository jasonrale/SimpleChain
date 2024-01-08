package org.simplechain.blockchain;

import com.google.common.io.BaseEncoding;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@ToString
public class Block {
    private static final Logger logger = LoggerFactory.getLogger(Block.class);

    private Long blockNumber;
    private String hash;
    private String previousHash;
    private long timestamp;
    private int nonce;
    private int difficulty;
    private ArrayList<Transaction> transactions;

    public static Block Genesis() {
        Block block = new Block();
        block.blockNumber = 0L;
        block.hash = "0";
        block.previousHash = "0";
        block.timestamp = System.currentTimeMillis();
        block.nonce = 0;
        block.difficulty = 5;
        block.transactions = new ArrayList<>();

        return block;
    }

    public Block(ArrayList<Transaction> transactions, Block lastBlock) {
        this.blockNumber = lastBlock.getBlockNumber() + 1L;
        this.transactions = transactions;
        this.previousHash = lastBlock.getPreviousHash();
        this.timestamp = System.currentTimeMillis();
        this.difficulty = lastBlock.getDifficulty();
        this.hash = calculateHash();
    }
    public void mineBlock(int reward, String minerAddress) {
        // 目标哈希前缀
        String target = new String(new char[difficulty]).replace('\0', '0');

        transactions.add(new Transaction("0", minerAddress, reward, null));
        //循环计算hash值直到满足目标前缀
        while (!hash.substring(0, difficulty).equals(target)) {
            hash = calculateHash();
            nonce++;
        }

        logger.info("Block mined! Hash: " + hash);
    }

    private String calculateHash() {
        String dataToHash = previousHash + Long.toString(timestamp) + Long.toString(nonce) + transactions.toString();

        try {
            // 创建 MessageDigest 实例，选择 SHA-256 算法
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));

            // 将哈希值转换成十六进制表示形式
            return BaseEncoding.base16().encode(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}