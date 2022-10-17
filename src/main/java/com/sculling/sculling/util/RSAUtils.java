package com.sculling.sculling.util;


import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class RSAUtils {

    // 加密算法
    private final static String ALGORITHM_RSA = "RSA";

    private final static String pukString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKQ0Hr7sh13UdcCP6Kp29aLsMGUv2E0YdCCEX8mvBXjR12kqN4KpGwDZL8hNjsOygHFREaNPrks8VS21+xtTLWLEc5pMptvzv1ofTPhfFqgrYlrGJYIOHc1+cmxGBZLpZiDZjf5JYyIPPtapCGesfbzGh3ZeO1gYu4FglH0RV/MwIDAQAB";

    private final static String prkString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIpDQevuyHXdR1wI/oqnb1ouwwZS/YTRh0IIRfya8FeNHXaSo3gqkbANkvyE2Ow7KAcVERo0+uSzxVLbX7G1MtYsRzmkym2/O/Wh9M+F8WqCtiWsYlgg4dzX5ybEYFkulmINmN/kljIg8+1qkIZ6x9vMaHdl47WBi7gWCUfRFX8zAgMBAAECgYBflwx2j8osIuZ5LQa+2e/Ui8vuDWaVcz9uWcX5VS2hekDKf6thIx1CeMAMsdQ/zs5nxxrh4Yssn1VnaaAJfts7wyjrWG0oVekKXoPuCTtgmT2IzRhJ/NOvGknl/H4rHzAyYQQN8PU3vgNeyND+dyN7TekO5rNyU4Ivj42DUv8kMQJBAP/jCd08cR4ohn0r4o3hUAK7tpepK1bCAD81UqIvnk4JMBSjuEGIpP46lkC84mnSXN4Y6BRz47eVc5eejNf4vY0CQQCKUuf7werCCpWwtdNeOpqAh/zqBqnuFSwgJCvOumkaaTret3xV4/PJ4KvJm+nmKppe3gwjd3mcMcFzMKTMvB+/AkEA+jezzJ+u6VOtdzbzmdOKHrIZ8aGd3H9mKGjm/etcfGhlmLkjkwmgdW0qwfEx4VKwNwnZ3y4Xrcw5tTh39zSnfQJAdHqZ9Qt3d0zYfoSuZ9PrlL5uwFc6M01K6gnrhdq9dMsWEsPqBJ2/BcmWf2A2l2ZatfY/vIH4Owo+/5P/wpPMGwJBAKh2XKQV7kvV07+pFxCSW1Z7FfUzB13agkCtsoam+CwyB2G0xdCu2SAHoNHNcbK1Avxmcd+gVsF02WNmPHgiH50=";


    /**
     * 直接生成公钥、私钥对象
     *
     * @param modulus
     *
     * @throws NoSuchAlgorithmException
     *
     */
    public static List<Key> getRSAKeyObject(int modulus) throws NoSuchAlgorithmException{

        List<Key> keyList = new ArrayList<>(2);
        // 创建RSA密钥生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        // 设置密钥的大小，此处是RSA算法的模长 = 最大加密数据的大小
        keyPairGen.initialize(modulus);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // keyPair.getPublic() 生成的是RSAPublic的是咧
        keyList.add(keyPair.getPublic());
        // keyPair.getPrivate() 生成的是RSAPrivateKey的实例
        keyList.add(keyPair.getPrivate());
        return keyList;
    }

    /**
     * 生成公钥、私钥的字符串
     * 方便传输
     *
     * @param modulus 模长
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static List<String> getRSAKeyString(int modulus) throws NoSuchAlgorithmException{

        List<String> keyList = new ArrayList<>(2);
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyPairGen.initialize(modulus);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        keyList.add(publicKey);
        keyList.add(privateKey);
        return keyList;
    }

    // Java中RSAPublicKeySpec、X509EncodedKeySpec支持生成RSA公钥
    // 此处使用X509EncodedKeySpec生成
    public static RSAPublicKey getPublicKey(String publicKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    // Java中只有RSAPrivateKeySpec、PKCS8EncodedKeySpec支持生成RSA私钥
    // 此处使用PKCS8EncodedKeySpec生成
    public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data)
            throws Exception {
        RSAPublicKey publicKey = RSAUtils.getPublicKey(pukString);
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长n转换成字节数
        int modulusSize = publicKey.getModulus().bitLength() / 8;
        // PKCS Padding长度为11字节，所以实际要加密的数据不能要 - 11byte
        int maxSingleSize = modulusSize - 11;
        // 切分字节数组，每段不大于maxSingleSize
        byte[][] dataArray = splitArray(data.getBytes(), maxSingleSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 分组加密，并将加密后的内容写入输出字节流
        for (byte[] s : dataArray) {
            out.write(cipher.doFinal(s));
        }
        // 使用Base64将字节数组转换String类型
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data)
            throws Exception {
        RSAPrivateKey privateKey = RSAUtils.getPrivateKey(prkString);
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // RSA加密算法的模长 n
        int modulusSize = privateKey.getModulus().bitLength() / 8;
        byte[] dataBytes = data.getBytes();
        // 之前加密的时候做了转码，此处需要使用Base64进行解码
        byte[] decodeData = Base64.getDecoder().decode(dataBytes);
        // 切分字节数组，每段不大于modulusSize
        byte[][] splitArrays = splitArray(decodeData, modulusSize);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for(byte[] arr : splitArrays){
            out.write(cipher.doFinal(arr));
        }
        return new String(out.toByteArray());
    }

    /**
     * 按指定长度切分数组
     *
     * @param data
     * @param len 单个字节数组长度
     * @return
     */
    private static byte[][] splitArray(byte[] data,int len){

        int dataLen = data.length;
        if (dataLen <= len) {
            return new byte[][]{data};
        }
        byte[][] result = new byte[(dataLen-1)/len + 1][];
        int resultLen = result.length;
        for (int i = 0; i < resultLen; i++) {
            if (i == resultLen - 1) {
                int slen = dataLen - len * i;
                byte[] single = new byte[slen];
                System.arraycopy(data, len * i, single, 0, slen);
                result[i] = single;
                break;
            }
            byte[] single = new byte[len];
            System.arraycopy(data, len * i, single, 0, len);
            result[i] = single;
        }
        return result;
    }

    /**
     * 生成公钥和私钥 index=0的是公钥。index=1的是私钥
     * @return
     */
    private static List<String> pukStringGenerate() throws NoSuchAlgorithmException {
        return RSAUtils.getRSAKeyString(1024);
    }


    /**
     * 使用md5的算法进行加密
     */

    public static void main(String[] args) throws Exception {
        // 使用公钥、私钥对象加解密
        List<Key> keyList = RSAUtils.getRSAKeyObject(1024);
        String message = "百年国庆";

        // 生成公钥、私钥
        String encryptedMsg = RSAUtils.encryptByPublicKey(message);
        String decryptedMsg = RSAUtils.decryptByPrivateKey(encryptedMsg);
        System.out.println("string key ! message ==  decryptedMsg ? " + message.equals(decryptedMsg));
    }



}
