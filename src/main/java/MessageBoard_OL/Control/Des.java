package MessageBoard_OL.Control;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by DELL on 14-8-14.
 */
public class Des {
    /**
     * 根据参数生成Key;
     * @param strKey
     */
    private Key getKey(String  strKey)
    {
        Key  key=null;
        try {
            KeyGenerator _generator=KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(strKey.getBytes()));
            key=_generator.generateKey();
            _generator=null;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 加密以string  明文输入，string密文输出;
     * @param strMing
     * @return
     */
    public  String  getencString(String strMing,Key key)
    {
        byte[]  byteMi=null;
        byte[]  byteMing=null;
        String  strMi="";

        BASE64Encoder encoder  =new BASE64Encoder();
        try {
            byteMing=strMing.getBytes("utf-8");
            byteMi=getEncCode(byteMing,key);
            strMi=encoder.encode(byteMi);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            encoder=null;
            byteMi=null;
            byteMing=null;
        }
        return  strMi;
    }

    /**
     * 解密以string 密文输入,String 明文输出;
     * @param strMi
     * @return
     */
    public  String  getDecString(String strMi,Key key)
    {
        BASE64Decoder base64Decoder=new BASE64Decoder();
        byte[] byteMing=null;
        byte[] byteMi=null;
        String strMing="";
        try {
            byteMi=base64Decoder.decodeBuffer(strMi);
            byteMing=getDecCode(byteMi,key);
            strMing=new String(byteMing,"utf-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            base64Decoder=null;
            byteMing=null;
            byteMi=null;
        }
        return strMing;

    }
    /**
     * 加密以byte[] 明文输入，byte[] 密文输出;
     * @param byts
     * @return
     */
    private  byte[]  getEncCode(byte[] byts,Key key)
    {
        byte[]  byteFina=null;
        Cipher cipher;
        try {
            cipher=Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina=cipher.doFinal(byts);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            cipher=null;
        }
        return  byteFina;
    }
    /**
     * 解密以byte[] 密文输入，byte[] 明文输出;
     * @param bytd
     * @return
     */
    private  byte[]  getDecCode(byte[] bytd,Key key)
    {
        byte[] byteFina=null;
        Cipher cipher=null;
        try {
            cipher=Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina=cipher.doFinal(bytd);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            cipher=null;
        }
        return byteFina;
    }

    public static  void  main(String[] args)
    {
        System.out.println(10/3);

        Des td=new Des();
        Key k=td.getKey("testkeyaaaaaa");
        System.out.println("获得的密钥key是:"+k);
        String  encyStr=td.getencString("test",k);
        System.out.println("加密后的密文是:"+encyStr);
        String  decyStr=td.getDecString(encyStr,k);
        System.out.println("解密后的明文是:"+decyStr);
    }
}
