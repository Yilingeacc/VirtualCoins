package BlockChain;

import static BlockChain.RSAUtils.initKey;
import java.util.Map;

// User类实现
public class User {

    public final String name;               // 用户名
    private final String publicKey;         // 私钥
    private final String privateKey;        // 公钥

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    // Constructor
    public User(String userName) {
        this.name = userName;
        Map<String, String> keyMap = initKey();     // 生成密钥对
        assert keyMap != null;
        this.publicKey = keyMap.get(PUBLIC_KEY);
        this.privateKey = keyMap.get(PRIVATE_KEY);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String toString() {
        return this.name;
    }
}
