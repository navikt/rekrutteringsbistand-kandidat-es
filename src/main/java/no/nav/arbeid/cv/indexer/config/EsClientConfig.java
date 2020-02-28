package no.nav.arbeid.cv.indexer.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class EsClientConfig {

    private EsProperties props;

    public EsClientConfig(EsProperties props) {
        this.props = props;
    }

    public RestHighLevelClient restHighLevelClient() throws RuntimeException {
        try {
            final SSLContext sslContext = createSslContext();

            if (props.getScheme().equalsIgnoreCase("HTTPS")) {
                RestClientBuilder builder = RestClient
                        .builder(new HttpHost(props.getHostname(), props.getPort(), props.getScheme()))
                        .setHttpClientConfigCallback(httpClientBuilder -> {
                            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                            credentialsProvider.setCredentials(AuthScope.ANY,
                                    new UsernamePasswordCredentials(props.getUser(), props.getPassword()));

                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                            RequestConfig requestConfig = RequestConfig.custom()
                                    .setSocketTimeout(props.getRequestTimeoutMS())
                                    .build();
                            httpClientBuilder.setDefaultRequestConfig(requestConfig);
                            return httpClientBuilder.setSSLContext(sslContext);
                        });

                builder.setStrictDeprecationMode(true);
                addPathPrefix(builder);
                addApiKeyHeader(builder);
                return new RestHighLevelClient(builder);

            } else {
                RestClientBuilder builder = RestClient
                        .builder(new HttpHost(props.getHostname(), props.getPort(), props.getScheme()));
                addPathPrefix(builder);
                addApiKeyHeader(builder);

                if (props.getUser() != null && !props.getUser().isEmpty() && props.getPassword() != null && !props.getPassword().isEmpty()) {
                    builder.setHttpClientConfigCallback(httpClientBuilder -> {
                        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(props.getUser(), props.getPassword()));

                        RequestConfig requestConfig = RequestConfig.custom()
                                .setSocketTimeout(props.getRequestTimeoutMS())
                                .build();
                        httpClientBuilder.setDefaultRequestConfig(requestConfig);
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    });
                }

                builder.setStrictDeprecationMode(true);

                return new RestHighLevelClient(builder);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addPathPrefix(RestClientBuilder builder) {
        props.getPathPrefix().ifPresent(p -> builder.setPathPrefix(p));
    }

    private void addApiKeyHeader(RestClientBuilder builder) {
        if (props.getApiKey().isPresent()) {
            Header apiKeyHeader = new BasicHeader("x-nav-apiKey", props.getApiKey().get());
            builder.setDefaultHeaders(Arrays.asList(apiKeyHeader).toArray(new Header[1]));
        }
    }

    private SSLContext createSslContext() throws NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException, IOException, CertificateException {
        if (true) {
            X509TrustManager trustAllX509Manager = mockX509TrustManager();
            SSLContext sslContext = getSslContext(trustAllX509Manager);
            return sslContext;
        }
        if (props.getTrustStoreFilename() != null) {

            // Path keyStorePath = Paths.get(props.getKeyStoreFilename());
            // KeyStore keystore = KeyStore.getInstance("jks");
            // try (InputStream is = Files.newInputStream(keyStorePath)) {
            // keystore.load(is, props.getKeyStorePass().toCharArray());
            // }

            Path trustStorePath = Paths.get(props.getTrustStoreFilename());
            KeyStore truststore = KeyStore.getInstance("jks");
            try (InputStream is = Files.newInputStream(trustStorePath)) {
                truststore.load(is, props.getTrustStorePass().toCharArray());
            }

            SSLContextBuilder sslBuilder = SSLContexts.custom();
            sslBuilder.loadTrustMaterial(truststore, null);
            // sslBuilder.loadKeyMaterial(keystore, props.getKeyStorePass().toCharArray());
            return sslBuilder.build();
        } else {
            return SSLContexts.createDefault();
        }
    }

    private SSLContext getSslContext(X509TrustManager trustAllX509Manager)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{trustAllX509Manager}, new SecureRandom());
        return sc;
    }

    private X509TrustManager mockX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

}
