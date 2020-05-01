package BlockChain;

import static BlockChain.RSAUtils.*;
import static BlockChain.SHA256.getSHA256;

import java.util.Arrays;

import com.alibaba.fastjson.annotation.JSONField;

// Transaction类实现

public class Transaction {

    @JSONField(ordinal = 0)
    User from;                  // 交易发送方

    @JSONField(ordinal = 1)
    User to;                    // 交易接收方

    @JSONField(ordinal = 2)
    int amount;                 // 交易数额

    byte[] signature;           // 数字签名

    // Constructor
    Transaction(User from, User to, int amount) throws Exception {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.sign();
    }

    public String getFrom() {
        return this.from.name;
    }

    public String getTo() {
        return this.to.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public String computeHash() {
        return getSHA256(this.from.name + this.to.name + this.amount);
    }

    // 签名
    public void sign() throws Exception {
        this.signature = publicEncrypt(this.computeHash().getBytes(), this.to.getPublicKey());
    }

    // 验证交易是否合法
    public boolean isValid() throws Exception {
        return Arrays.equals(privateDecrypt(this.signature, this.to.getPrivateKey()), this.computeHash().getBytes());
    }

    public String toString() {
        return "\t\tfrom:" + this.from +
                "\t\tto:" + this.to +
                "\t\tamount:" + this.amount + "\n";
    }
}
