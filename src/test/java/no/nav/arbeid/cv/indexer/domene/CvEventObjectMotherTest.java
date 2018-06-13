package no.nav.arbeid.cv.indexer.domene;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.arbeid.cv.events.CvEvent;
import no.nav.arbeid.cv.indexer.service.EsCvTransformer;

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
