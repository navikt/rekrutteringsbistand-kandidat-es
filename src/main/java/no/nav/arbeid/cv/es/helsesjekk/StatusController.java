package no.nav.arbeid.cv.es.helsesjekk;

import no.nav.security.spring.oidc.validation.api.Unprotected;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Unprotected
public class StatusController {

  @RequestMapping(method = {RequestMethod.GET}, value = "/rest/internal/isAlive")
  public ResponseEntity<String> isAlive() {

    return new ResponseEntity<String>("OK", HttpStatus.OK);
  }

  @RequestMapping(method = {RequestMethod.GET}, value = "/rest/internal/isReady")
  public ResponseEntity<String> isReady() {

    return new ResponseEntity<String>("OK", HttpStatus.OK);
  }

}
