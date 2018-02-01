package no.nav.arbeid.cv.es.helsesjekk;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

//  @Autowired
//  private KafkaTemplate<String, Cv> template;

  @RequestMapping(method = {RequestMethod.GET}, value = "/rest/internal/isAlive")
  public ResponseEntity<String> isAlive() {

    // template.send("mytopic", new Cv(1L, "Helsesjekk er kjørt!"));

    return new ResponseEntity<String>("OK", HttpStatus.OK);
  }

}
