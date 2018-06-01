package no.nav.arbeid.cv.es.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.arbeid.cv.arena.domene.ArenaPerson;
import no.nav.arbeid.cv.arena.mapper.ArenaPersonMapper;
import no.nav.arbeid.cv.es.client.EsCvClient;
import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.security.spring.oidc.validation.api.Unprotected;

@RestController
@Unprotected
public class LastController {

  @Autowired
  private EsCvClient esCvClient;

  @Autowired
  private ObjectMapper objectMapper;

  @RequestMapping(method = {RequestMethod.GET}, value = "/rest/lasttestdata")
  public ResponseEntity<String> lastTestData() throws IOException {

    ClassPathResource classPathResource = new ClassPathResource("input.json");
    InputStream inputStream = classPathResource.getInputStream();

    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, "UTF-8");
    String input = writer.toString();
    List<ArenaPerson> arenapersoner =
        objectMapper.readValue(input, new TypeReference<List<ArenaPerson>>() {});

    ArenaPersonMapper mapper = new ArenaPersonMapper();
    List<EsCv> espersoner = arenapersoner.stream().map(mapper::map).collect(Collectors.toList());
    esCvClient.bulkIndex(espersoner);

    return new ResponseEntity<String>("OK", HttpStatus.OK);
  }
}
