package BlockChain;

import static BlockChain.RSAUtils.initKey;
import java.util.Map;

// User类实现
public class User {

    // 用户名
    public final String name;
    // 私钥
    private final String publicKey;
    // 公钥
    private final String privateKey;

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    // Constructor
    public User(String userName) {
        this.name = userName;
        // 生成密钥对
        Map<String, String> keyMap = initKey();
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

    @Override
    public String toString() {
        return this.name;
    }
}
