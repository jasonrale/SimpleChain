package org.simplechain.blockchain;

import lombok.Data;
import org.bouncycastle.util.encoders.Hex;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Data
public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取压缩的公钥字符串
     * @return
     */
    private String compressPublicKey() {
        byte[] pubKeyBytes = publicKey.getEncoded();
        byte[] compressedPubKey;

        if ((pubKeyBytes[pubKeyBytes.length - 1] & 1) == 1) {
            // If the last byte of Y-coordinate is odd, append 03
            compressedPubKey = new byte[33];
            compressedPubKey[0] = 0x03;
            System.arraycopy(pubKeyBytes, 0, compressedPubKey, 1, 32);
        } else {
            // If the last byte of Y-coordinate is even, append 02
            compressedPubKey = new byte[33];
            compressedPubKey[0] = 0x02;
            System.arraycopy(pubKeyBytes, 0, compressedPubKey, 1, 32);
        }

        return Hex.toHexString(compressedPubKey);
    }

    public String getPublicKeyString() {
        return compressPublicKey();
    }

    public String getPrivateKeyString() {
        return Hex.toHexString(privateKey.getEncoded());
    }
}