package no.nav.arbeidsgiver.kandidat.kandidatsok.es.helper;

import no.nav.arbeidsgiver.kandidatsok.es.helper.KandidatnrHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KandidatnrHelperTest {

    @Test
    public void test1() {
        assertEquals(KandidatnrHelper.toKandidatnr(1L), "AB000001");
    }
    @Test
    public void test2() {
        assertEquals(KandidatnrHelper.toKandidatnr(788L), "AB070808");
    }

    @Test
    public void test3() {
        assertEquals(KandidatnrHelper.toKandidatnr(4363582L), "GE354802");
    }

    @Test
    public void test4() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }

    @Test
    public void test5() {
        assertEquals(KandidatnrHelper.toKandidatnr(4363742L), "GE374402");
    }

    @Test
    public void test6() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }

    @Test
    public void test7() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }

    @Test
    public void test8() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }

    @Test
    public void test9() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }

    @Test
    public void test10() {
        assertEquals(KandidatnrHelper.toKandidatnr(1003340L), "AE031400");
    }
}
