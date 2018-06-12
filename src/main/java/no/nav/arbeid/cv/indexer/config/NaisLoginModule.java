package no.nav.arbeid.cv.indexer.config;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.kafka.common.security.plain.PlainSaslServerProvider;

public class NaisLoginModule implements LoginModule {

  static {
    PlainSaslServerProvider.initialize();
  }

  @Override
  public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map<String, ?> sharedState, Map<String, ?> options) {

    String username = System.getenv("KAFKA_USER");
    if (username != null) {
      subject.getPublicCredentials().add(username);
    }

    String password = System.getenv("KAFKA_PWD");
    if (password != null) {
      subject.getPrivateCredentials().add(password);
    }
  }

  @Override
  public boolean login() throws LoginException {
    return true;
  }

  @Override
  public boolean logout() throws LoginException {
    return true;
  }

  @Override
  public boolean commit() throws LoginException {
    return true;
  }

  @Override
  public boolean abort() throws LoginException {
    return false;
  }
}
