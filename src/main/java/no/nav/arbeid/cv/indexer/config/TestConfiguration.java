package no.nav.arbeid.cv.indexer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

// FIXME betingelsesløs test-konfig i main-classpath
@Configuration
public class TestConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfiguration.class);
    //
    // @PostConstruct
    // public void skrivUtEnvVariabler() {
    // Map<String, String> env = System.getenv();
    // LOGGER.info("Printint all env variables");
    // for (String envName : env.keySet()) {
    // LOGGER.info("Environment variable: {}={}", envName, env.get(envName));
    // }
    // }

    @PostConstruct
    public void disableTlsCertificateVerification()
            throws NoSuchAlgorithmException, KeyManagementException {

        LOGGER.warn("Skrur av TLS sertifikatsjekk for javax.net.ssl.HttpsUrlConnection");

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
