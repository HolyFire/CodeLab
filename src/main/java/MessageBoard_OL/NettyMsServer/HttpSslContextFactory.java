package MessageBoard_OL.NettyMsServer;

import io.netty.handler.codec.base64.Base64;
import io.netty.util.internal.SystemPropertyUtil;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
* Created by DELL on 2014-09-17.
*/
public final class HttpSslContextFactory {


    private static final String PROTOCOL = "SSL";
    private static final SSLContext SERVER_CONTEXT;
    private static final SSLContext CLIENT_CONTEXT;

    private static String CLIENT_KEY_STORE = System.getProperty("user.dir")+"/Web/sslclientkeys";
    private static String CLIENT_TRUST_KEY_STORE =System.getProperty("user.dir")+"/Web/sslclienttrust";
    private static String CLIENT_KEY_STORE_PASSWORD = "a12345678";
    private static String CLIENT_TRUST_KEY_STORE_PASSWORD = "a12345678";


    private static String SERVER_KEY_STORE = System.getProperty("user.dir")+"/Web/sslserverkeys";
    private static String SERVER_TRUST_KEY_STORE = System.getProperty("user.dir")+"/Web/sslservertrust";
    private static String SERVER_KEY_STORE_PASSWORD = "a12345678";
    private static String SERVER_TRUST_KEY_STORE_PASSWORD = "a12345678";

    static {
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        SSLContext serverContext;
        SSLContext clientContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(SERVER_KEY_STORE), SERVER_KEY_STORE_PASSWORD.toCharArray());
            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream(SERVER_TRUST_KEY_STORE), SERVER_TRUST_KEY_STORE_PASSWORD.toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
            tmf.init(tks);

            // Initialize the SSLContext to work with our key managers.
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }

        try {
            KeyStore ks2 = KeyStore.getInstance("JKS");
            ks2.load(new FileInputStream(CLIENT_KEY_STORE), CLIENT_KEY_STORE_PASSWORD.toCharArray());

            KeyStore tks2 = KeyStore.getInstance("JKS");
            tks2.load(new FileInputStream(CLIENT_TRUST_KEY_STORE), CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());
            // Set up key manager factory to use our key store
            KeyManagerFactory kmf2 = KeyManagerFactory.getInstance(algorithm);
            TrustManagerFactory tmf2 = TrustManagerFactory.getInstance("SunX509");
            kmf2.init(ks2, CLIENT_KEY_STORE_PASSWORD.toCharArray());
            tmf2.init(tks2);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf2.getKeyManagers(), tmf2.getTrustManagers(), null);
        } catch (Exception e) {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }

        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }


    public static SSLContext getServerContext() {
        return SERVER_CONTEXT;
    }


    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

    private HttpSslContextFactory() {
        // Unused
    }
}


//    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(HttpSslContextFactory.class);
//    private static final String pkcs12Base64 = ""; // use base64 encoded string from steps above
//    private static SSLContext sslContext = null;
//    public static SSLContext get() {
//        if(sslContext==null) {
//            synchronized (HttpSslContextFactory.class) {
//                if(sslContext==null) {
//                    try {
//                        sslContext = SSLContext.getInstance("TLS");
//                        KeyStore ks = KeyStore.getInstance("PKCS12");
//                        ks.load(new ByteArrayInputStream(Base64.decode(copiedBuffer(pkcs12Base64, CharsetUtil.UTF_8)).array()), "secret".toCharArray());
//                        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//                        kmf.init(ks, "secret".toCharArray());
//                        sslContext.init(kmf.getKeyManagers(), null, null);
//                    } catch (Exception e) {
//                        LOGGER.error("Unable to create SSLContext", e);
//                    }
//                }
//            }
//        }
//        return sslContext;
//    }

//    private static final Logger LOGGER = Logger.getLogger(HttpSslContextFactory.class);
//    private static final String PROTOCOL = "SSLv3";
//
//    private static SSLContext SSLCONTEXT = null;
//
//    static {
//        // 采用的加密算法
//
//        String algorithm = Security
//                .getProperty("ssl.KeyManagerFactory.algorithm");
//        if (algorithm == null) {
//            algorithm = "SunX509";
//        }
//        SSLContext serverContext = null;
//        try {
//            //访问Java密钥库，JKS是keytool创建的Java密钥库，保存密钥。
//
//            KeyStore ks = KeyStore.getInstance("JKS");
//            ks.load(HttpsKeyStore.getKeyStoreStream(), HttpsKeyStore.getKeyStorePassword());
//
//            //创建用于管理JKS密钥库的密钥管理器。
//
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
//            kmf.init(ks, HttpsKeyStore.getCertificatePassword());
//
//            //构造SSL环境，指定SSL版本为3.0，也可以使用TLSv1，但是SSLv3更加常用。
//
//            serverContext = SSLContext.getInstance(PROTOCOL);
//
//            //初始化SSL环境。第二个参数是告诉JSSE使用的可信任证书的来源，设置为null是从javax.net.ssl.trustStore中获得证书。第三个参数是JSSE生成的随机数，这个参数将影响系统的安全性，设置为null是个好选择，可以保证JSSE的安全性。
//
//            serverContext.init(kmf.getKeyManagers(), null, null);
//        } catch (Exception e) {
//            LOGGER.error("初始化客户端SSL失败", e);
//            throw new Error("Failed to initialize the server SSLContext", e);
//        }
//
//        SSLCONTEXT = serverContext;
//    }
//
//    /**
//     * 获取SSLContext实例
//     *
//     * @author linfenliang
//     * @date 2012-9-11
//     * @version V1.0.0
//     * @return
//     * SSLContext
//     */
//    public static SSLContext getServerContext() {
//
//        return SSLCONTEXT ;
//    }
//
//    /**
//
//     * 读取密钥
//
//     *
//
//     * @author linfenliang
//
//     * @date 2012-9-11
//
//     * @version V1.0.0
//
//     * @return InputStream
//
//     */
//
//    public static InputStream getKeyStoreStream() {
//
//        InputStream inStream = null;
//
//        try {
//
//            inStream = new FileInputStream(System.getProperty("user.dir")+"/Web");
//
//        } catch (FileNotFoundException e) {
//
////            LOGGER.error("读取密钥文件失败", e);
//
//        }
//
//        return inStream;
//
//    }
//
//
//    /**
//
//     * 获取安全证书密码 (用于创建KeyManagerFactory)
//
//     *
//
//     * @author linfenliang
//
//     * @date 2012-9-11
//
//     * @version V1.0.0
//
//     * @return char[]
//
//     */
//
//    public static char[] getCertificatePassword() {
//
//        return "a12345678".toCharArray();
//
//    }
//
//
//    /**
//
//     * 获取密钥密码(证书别名密码) (用于创建KeyStore)
//
//     *
//
//     * @author linfenliang
//
//     * @date 2012-9-11
//
//     * @version V1.0.0
//
//     * @return char[]
//
//     */
//
//    public static char[] getKeyStorePassword() {
//
//        return "a12345678".toCharArray();
//
//    }
//}
