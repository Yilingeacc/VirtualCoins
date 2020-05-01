package BlockChain;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    private static final int keySize = 1024;

    /**
     * 得到密钥字符串（经过base64编码）
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        return (new BASE64Encoder()).encode(keyBytes);
    }

    /**
     * 公钥加密
     * @param content 明文
     * @param publicKeyString 公钥字符串（经过base64编码）
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content, String publicKeyString) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = getPublicKey(publicKeyString);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content);
    }

    /**
     * 公钥解密
     * @param content 密码
     * @param publicKeyString 公钥字符串（经过base64编码）
     * @throws Exception
     */
    public static byte[] publicDecrypt(byte[] content, String publicKeyString) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = getPublicKey(publicKeyString);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, content);
    }

    /**
     * 私钥加密
     * @param content 明文
     * @param privateKeyString 私钥字符串（经过base64编码）
     * @throws Exception
     */
    public static byte[] privateEncrypt(byte[] content, String privateKeyString) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = getPrivateKey(privateKeyString);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, content);
    }

    /**
     * 私钥解密
     * @param content 密码
     * @param privateKeyString 私钥字符串（经过base64编码）
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content, String privateKeyString) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = getPrivateKey(privateKeyString);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, content);
    }

    /**
     * 得到公钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[]keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[]keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    // 初始化秘钥
    public static Map<String, String> initKey() {
        try {
            // 生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            // 初始化秘钥对生成器
            keyPairGen.initialize(keySize);
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();         // 得到公钥
            PrivateKey privateKey = keyPair.getPrivate();      // 得到私钥
            Map<String, String> keyMap = new HashMap<>(2);
            keyMap.put(PUBLIC_KEY, getKeyString(publicKey));
            keyMap.put(PRIVATE_KEY, getKeyString(privateKey));
            return keyMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 分段加密解密
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) throws Exception {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        while(datas.length > offSet){
            if (datas.length - offSet > maxBlock){
                buff = cipher.doFinal(datas, offSet, maxBlock);
            } else {
                buff = cipher.doFinal(datas, offSet, datas.length-offSet);
            }
            out.write(buff, 0, buff.length);
            i++;
            offSet = i * maxBlock;
        }
        return out.toByteArray();
    }

    public static void main(String[]args) throws Exception {
        Map<String, String> keyMap = initKey();
        assert keyMap != null;
        String privateKeyString = keyMap.get(PRIVATE_KEY);
        String publicKeyString = keyMap.get(PUBLIC_KEY);
        String content = "123";
        byte[] encode = publicEncrypt(content.getBytes(), publicKeyString);
        System.out.println(new String(privateDecrypt(encode, privateKeyString)));
    }
}
