package no.nav.arbeid.cv.kandidatsok.domene.sok;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class KandidatsokCvObjectMotherTest {

  @Test
  public void twoCvEventsFromSameObjectMotherShouldBeIdentical() {
    EsCv cvEvent1 = KandidatsokCvObjectMother.giveMeCv();
    EsCv cvEvent2 = KandidatsokCvObjectMother.giveMeCv();

    assertThat(cvEvent1).isEqualTo(cvEvent2);
  }

}
