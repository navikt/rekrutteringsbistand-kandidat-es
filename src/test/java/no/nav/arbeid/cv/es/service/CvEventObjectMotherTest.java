package no.nav.arbeid.cv.es.service;

import static org.assertj.core.api.Assertions.assertThat;

import no.nav.arbeid.cv.es.domene.EsCv;
import no.nav.arbeid.cv.events.CvEvent;
import org.junit.Test;

public class CvEventObjectMotherTest {

  @Test
  public void twoCvEventsFromSameObjectMotherShouldBeIdentical() {
    CvEvent cvEvent1 = CvEventObjectMother.giveMeCvEvent();
    CvEvent cvEvent2 = CvEventObjectMother.giveMeCvEvent();

    assertThat(cvEvent1).isEqualTo(cvEvent2);
  }

  @Test
  public void twoEsCvObjectsFromSameCvEventObjectMotherShouldBeIdentical() {
    CvEvent cvEvent1 = CvEventObjectMother.giveMeCvEvent();
    CvEvent cvEvent2 = CvEventObjectMother.giveMeCvEvent();

    EsCvTransformer esCvTransformer = new EsCvTransformer();

    EsCv esCv1 = esCvTransformer.transform(cvEvent1);
    EsCv esCv2 = esCvTransformer.transform(cvEvent2);

    assertThat(esCv1).isEqualTo(esCv2);
  }

}
