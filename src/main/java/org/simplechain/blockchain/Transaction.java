package org.simplechain.blockchain;

import com.google.common.io.BaseEncoding;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;


import java.math.BigInteger;
import java.security.*;

@Data
@AllArgsConstructor
public class Transaction {
    public String sender;
    public String recipient;
    public double amount;
    public String signature;

    /**
     * 使用私钥对数据签名
     * @param privateKey 私钥
     */
    public void generateSignature(PrivateKey privateKey) {
        String data = sender + recipient + amount;
        try {
            // 创建并初始化Signature对象
            Signature signatureAlgorithm = Signature.getInstance("ECDSA", "BC");
            signatureAlgorithm.initSign(privateKey);
            // 更新签名数据
            signatureAlgorithm.update(data.getBytes());
            this.signature = BaseEncoding.base16().encode(signatureAlgorithm.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifySignature() {
        String data = sender + recipient + amount;
        try {
            Signature signatureAlgorithm = Signature.getInstance("ECDSA", "BC");
            signatureAlgorithm.initVerify(HexToPublicKey(sender));
            signatureAlgorithm.update(data.getBytes());
            return signatureAlgorithm.verify(Hex.decode(signature));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey HexToPublicKey(String hexPublicKey) throws Exception {
        // 将hex字符串解码为字节数组
        byte[] publicKeyBytes = Hex.decode(hexPublicKey);

        // 获取椭圆曲线参数
        X9ECParameters ecParameters = CustomNamedCurves.getByName("secp256k1");
        ECDomainParameters domainParameters = new ECDomainParameters(ecParameters.getCurve(), ecParameters.getG(), ecParameters.getN(), ecParameters.getH());

        // 创建公钥参数
        ECPoint ecPoint = ecParameters.getG().multiply(new BigInteger(1, publicKeyBytes));
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(ecPoint, new ECParameterSpec(ecParameters.getCurve(), ecParameters.getG(), ecParameters.getN(), ecParameters.getH()));
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
        return keyFactory.generatePublic(publicKeySpec);
    }
}