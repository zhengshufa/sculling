package com.sculling.sculling.util;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;



public class EncryptionUtil {
    // ***************************  RSA加密相关常量配置******************************************
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String ALGORITHM_NAME = "RSA";
    private static final String MD5_RSA = "MD5withRSA";

    private static final String RSAPrivateKey = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAP69/wIlhGEnqiU3nglVjCSoU6KFNl2jTpOKAHGWLCDb1uMq2wH3mfuWwxcfFPvhm4oSTSv8DzWDek76BwaYnZfh9o4Uj9pB3wnSZjFbjTugEIoYUev/xXZhTV5wd+4CwSro7kel4+S82f5MM/pqS8iuqgxb6ZlzVD1Pf0ZbEz89AgMBAAECgYEA04nG92tcMKmVeu5ih/HIPLCjMvRYHw37tD0G/UvtPSQRn/b4gvuvnOKnAs5GyJuiY9eqyfm4T4Hs0SSDLRES22Mi2dZ0+jvSSzFVtIkfbRcbQthxWXMFtHlN9N3eBg9nTWMeUImb1zADDHj/CrpYGV2uafNpBdcONYk1MwyQlmkCQQD/pwPF/sS1wxcAIV3x0yO7SYe5Djxm72xnuGdkgJEwzMM+OV4GbAeK1seZcvvzgCQbOS0ylXScPbkA4XOUO32nAkEA/xaqILyaJhPOTSkqwxMSpDpLbSPlnaNcU9SXGu4dacYiBIkJFWBb9XsKYvU1oDSXDF8yJl6407g8o9b5ug8gewJBAN1VGEQ7vMxsWaZoPPBXTvEfyNxjQdIQaPcnRIRTduAb9tERU6EVuDXwmx/z18PJ5fiXuNS5Zhq6qUfk6G6mTrMCQQDJIDyprSs5ZJpf2lqeDra/CDuaZRhRT5Gng1JU6HQGhzMhMk8YLK61AftxhPdwRqbmGVT4Te0a+gPOBt1F51FrAkEAiqtU33C7ZROOlnIxe/ULRghwM9/uLK3bIaa4FEEl/IH1HOSWAewoJ+oezI1wV/HuVmUw3VXorhaRr2AcmDSWDQ==";
    private static final String RSAPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD+vf8CJYRhJ6olN54JVYwkqFOihTZdo06TigBxliwg29bjKtsB95n7lsMXHxT74ZuKEk0r/A81g3pO+gcGmJ2X4faOFI/aQd8J0mYxW407oBCKGFHr/8V2YU1ecHfuAsEq6O5HpePkvNn+TDP6akvIrqoMW+mZc1Q9T39GWxM/PQIDAQAB";
    // ***************************  RSA加密相关常量配置******************************************

    // ***************************  AES加密相关常量配置******************************************
    /**
     *  AES加密,key的大小必须是16个字节
     *  如果没有指定分组密码模式和填充模式,ECB/PKCS5Padding就是默认值
     *  如果没有指定分组密码模式为CBC,必须指定初始向量,初始向量中密钥的长度必须是16个字节
     *  NoPadding模式,原文的长度必须是16个字节的整倍数
     */
    //获取Cipher对象的算法
    private static String transformation = "AES/CBC/PKCS5Padding";

    /**key **/
    public static final String  SJ_KEY="$bonc$:aEs|#key#";

    public static final String  AES= "AES";
    // ***************************  AES加密相关常量配置******************************************


    /**
     * AES加密
     */
    public static String AESEncryptDATA(String data) throws Exception {
        return AESEncrypt(data,SJ_KEY,AES);
    }

    /**
     * AES解密
     */
    public static String AESDecryptDATA(String data) throws Exception {
        return AESDecrypt(data,SJ_KEY,AES);
    }

    /**
     * RSA加密
     */
    public static String RSAEncryptData(String data) throws Exception {
        PublicKey  publicKey = getPublicKey(RSAPublicKey);
        return RSAEncrypt(data,publicKey);
    }

    /**
     * RSA解密
     */
    public static String RSADecryptData(String data) throws Exception {
        PrivateKey privateKey = getPrivateKey(RSAPrivateKey);
        return RSADecrypt(data,privateKey);
    }


    /**
     * 获取密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取base64加密后密钥对
     */
    public static void getKeyPairMap() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
        String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));


        System.out.println("privateKey:"+privateKey);
        System.out.println("publicKey:"+publicKey);
    }

    /**
     * 获取公钥
     *
     * @param publicKey base64加密的公钥字符串
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥
     *
     * @param privateKey base64加密的私钥字符串
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     */
    public static String RSAEncrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     */
    public static String RSADecrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(MD5_RSA);
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(MD5_RSA);
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }



    /**
     * 加密
     * @param input  明文
     * @param key   密钥(AES,密钥的长度必须是16个字节)
     * @param algorithm   获取密钥的算法
     * @return  返回密文
     * @throws Exception
     */
    public static String AESEncrypt(String input, String key, String algorithm) throws Exception {
        // 1,获取Cipher对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // 2.初始化向量的秘钥长度需要根据算法而定,des 8个字节长度  aes 16个字节长度
        IvParameterSpec iv = new IvParameterSpec(key.getBytes()); //java iv与key一样
        cipher.init(Cipher.ENCRYPT_MODE, sks, iv);
//        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // 3. 加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        // 对数据进行Base64编码
        String encode = new String(Base64.encodeBase64((bytes)));
        return encode;
    }

    /**
     * 解密
     * @param input  密文
     * @param key   密钥(AES,密钥的长度必须是16个字节)
     * @param algorithm   获取密钥的算法
     * @return 返回原文
     * @throws Exception
     */
    public static String AESDecrypt(String input, String key, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation);
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, sks, iv);
//         cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(input));
        return new String(bytes);
    }



    public static void main(String[] args) throws Exception {
        String pwd = "Asb#1q2w";
        String enPwd = RSAEncryptData(pwd);
        System.out.println(enPwd.length());
        System.out.println("RSA加解密后字符串："+RSAEncryptData(enPwd));

        String jmhzf = AESEncryptDATA(pwd);
        System.out.println("AES加密后字符串："+jmhzf);
        String jiemhzf = AESDecryptDATA("p85wBCb1A+zcrRZkfj5UnQ==");
        System.out.println("AES解密后字符串："+jiemhzf);


    }

    public static String buildDefaultPwd() throws Exception{
        String defaultPwd = "Asb#1q2w";
        return AESEncryptDATA(defaultPwd);
    }

}
