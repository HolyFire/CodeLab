package MessageBoard_OL.NettyMsServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
* Created by DELL on 2014-09-17.
*/
public class HttpsKeyStore {
    /**

     * 读取密钥

     *

     * @author linfenliang

     * @date 2012-9-11

     * @version V1.0.0

     * @return InputStream

     */

    public static InputStream getKeyStoreStream() {

        InputStream inStream = null;

        try {

            inStream = new FileInputStream(System.getProperty("user.dir")+"/Web");

        } catch (FileNotFoundException e) {

//            LOGGER.error("读取密钥文件失败", e);

        }

        return inStream;

    }


    /**

     * 获取安全证书密码 (用于创建KeyManagerFactory)

     *

     * @author linfenliang

     * @date 2012-9-11

     * @version V1.0.0

     * @return char[]

     */

    public static char[] getCertificatePassword() {

//        return Constants.CERTIFICATEPASSWORD.toCharArray();
        return "a12345678".toCharArray();
    }


    /**

     * 获取密钥密码(证书别名密码) (用于创建KeyStore)

     *

     * @author linfenliang

     * @date 2012-9-11

     * @version V1.0.0

     * @return char[]

     */

    public static char[] getKeyStorePassword() {

//        return Constants.KEYSTOREPASSWORD.toCharArray();
        return "a12345678".toCharArray();
    }
}
