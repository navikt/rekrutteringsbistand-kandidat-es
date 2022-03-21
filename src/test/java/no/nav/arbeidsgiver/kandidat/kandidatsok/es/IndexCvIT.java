package no.nav.arbeidsgiver.kandidat.kandidatsok.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.sok.*;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchIntegrationTestExtension;
import no.nav.arbeidsgiver.kandidat.kandidatsok.testsupport.ElasticSearchTestConfiguration;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeidsgiver.kandidatsok.es.client.EsSokService;
import org.assertj.core.api.Assertions;
import org.assertj.core.extractor.Extractors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static no.nav.arbeidsgiver.kandidat.kandidatsok.KatKode.Kat1_Kode;
import static no.nav.arbeidsgiver.kandidat.kandidatsok.Tilgjengelighet.*;
import static no.nav.arbeidsgiver.kandidat.kandidatsok.domene.es.EsCvObjectMother.antallDagerTilbakeFraNow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ElasticSearchIntegrationTestExtension.class)
public class IndexCvIT {


    private final EsSokService sokClient = ElasticSearchTestConfiguration.esSokService(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);

    private final EsIndexerService indexerClient = ElasticSearchTestConfiguration.indexerCvService();

    private final ObjectMapper objectMapper = ElasticSearchTestConfiguration.objectMapper();

    private final KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    private static final List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> setupCver = List.of(
            EsCvObjectMother.giveMeEsCv(),
            EsCvObjectMother.giveMeEsCv2(),
            EsCvObjectMother.giveMeEsCv3(),
            EsCvObjectMother.giveMeEsCv4(),
            EsCvObjectMother.giveMeEsCv5(),
            EsCvObjectMother.giveMeEsCv6(),
            EsCvObjectMother.giveMeCvForDoedPerson(),
            EsCvObjectMother.giveMeCvForKode6(),
            EsCvObjectMother.giveMeCvForKode7(),
            EsCvObjectMother.giveMeCvFritattForAgKandidatsok(),
            EsCvObjectMother.giveMeCvFritattForKandidatsok()
    );

    private static final List<String> setupKandidatnumre = setupCver.stream().map(no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv::getKandidatnr).collect(toList());

    private static List<String> kandidatnumre(List<EsCv> cver) {
        return cver.stream().map(EsCv::getKandidatnr).collect(toList());
    }

    /**
     * @return Map hvor nøkkel er CV-ens kandidatdnummer og verdi er innholdet i CV-ens property tilrettelggigngsbehov.
     */
    private Map<String, List<String>> tilretteleggingsbehov(List<EsCv> cver) {
        return cver.stream().collect(Collectors.toMap(EsCv::getKandidatnr, EsCv::getVeilTilretteleggingsbehov));
    }


    @BeforeEach
    public void before() {
        indexerClient.createIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        indexerClient.bulkIndex(setupCver, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @AfterEach
    public void after() {
        indexerClient.deleteIndex(ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void skalIkkeFeileMedIllegalArgumentFraES() throws Exception {
        no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv cv1 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv1.json"),
                        ISO_8859_1),
                no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv.class);
        no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv cv2 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv2.json"),
                        ISO_8859_1),
                no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv.class);

        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer = asList(cv1, cv2);

        int antallIndeksert = indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        assertThat(antallIndeksert).isEqualTo(bulkEventer.size());
    }

    @Test
    @Disabled("Brukes til utforskende testing")
    public void skalLoggeFeilVedBulkIndeksereCVMedNullfelter() {
        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCvMedFeil(),
                        EsCvObjectMother.giveMeEsCv2());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9998));
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void testUtenSokekriterierReturnererAlleCver() {
        List<EsCv> actualCver = sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver();

        assertThat(actualCver.size()).isEqualTo(setupCver.size());
        assertThat(kandidatnumre(actualCver)).containsExactlyInAnyOrderElementsOf(setupKandidatnumre);
    }

    @Test
    public void testFlereInputFritekstGirBredereResultat() {
        SokekriterierVeiledere smaltSøk = SokekriterierVeiledere.med().fritekst("javautvikler").bygg();
        SokekriterierVeiledere bredtSøk = SokekriterierVeiledere.med().fritekst("industrimekaniker javautvikler").bygg();

        List<EsCv> cverSmaltSøk = sokClient.veilederSok(smaltSøk).getCver();
        List<EsCv> cverBredtSøk = sokClient.veilederSok(bredtSøk).getCver();

        assertThat(cverSmaltSøk).isNotEmpty();
        assertThat(cverBredtSøk).isNotEmpty();
        assertThat(cverSmaltSøk.size()).isLessThan(cverBredtSøk.size());
    }

    @Test
    public void testSokPaNorskeStoppordGirIkkeResultat() {
        Sokeresultat sokeresultatYrke = sokClient.veilederSok(SokekriterierVeiledere.med().stillingstitler(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatKomp = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatUtdanning = sokClient.veilederSok(SokekriterierVeiledere.med().utdanninger(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatFritekst =
                sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("og").bygg());

        List<EsCv> cverYrke = sokeresultatYrke.getCver();
        List<EsCv> cverKomp = sokeresultatKomp.getCver();
        List<EsCv> cverUtdanning = sokeresultatUtdanning.getCver();
        List<EsCv> cverFritekst = sokeresultatFritekst.getCver();

        assertThat(cverYrke.size()).isEqualTo(0);
        assertThat(cverKomp.size()).isEqualTo(0);
        assertThat(cverUtdanning.size()).isEqualTo(0);
        assertThat(cverFritekst.size()).isEqualTo(0);
    }

    @Test
    public void testSokMedFlereKriterierGirSvarMedAlleFelter() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().stillingstitler(Collections.singletonList("Butikkmedarbeider"))
                .kompetanser(Collections.singletonList("Hallovert"))
                .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv4()));
    }

    @Test
    public void testFlereInputYrkeGirFlereTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Industrimekaniker")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med().stillingstitler(asList("Butikkmedarbeider", "Industrimekaniker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver2.size()).isGreaterThan(cver.size());
    }

    @Test
    public void testFlereInputKompetanseGirFlereTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("Programvareutvikler")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(asList("Programvareutvikler", "Nyhetsanker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver2.size()).isGreaterThan(cver.size());
    }

    @Test
    public void testKompetanseSkalBrukeFraseMatchMedMulighetForNoeOmstokkingAvOrd() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("Mekanisk arbeid generelt")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("Mekanisk generelt arbeid")).bygg());
        Sokeresultat sokeresultat3 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("arbeid generelt mekanisk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();
        List<EsCv> cver3 = sokeresultat3.getCver();

        assertThat(cver).isNotEmpty();
        assertThat(cver.size()).isEqualTo(cver2.size());
        assertThat(cver.size()).isEqualTo(cver3.size());
    }

    @Test
    public void testKompetanseFraseSkalIkkeMatchePaaTversAvUlikeKompetanserFlerverdi() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("kranførerarbeid landtransport")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver).isEmpty();
    }

    @Test
    public void testFlereInputUtdanningGirMindreTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .utdanninger(asList("Bygg og anlegg", "master i sikkerhet")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testForerkortKriterierGirTreffPaaForerkortSomErInkludertIKriteriet() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .forerkort(Collections.singletonList("B - Personbil"))
                .bygg());
        List<EsCv> cver = sokeresultat.getCver();
        Set<String> forerkort = new HashSet<>();
        cver.forEach(cv -> cv.getForerkort().forEach(f -> forerkort.add(f.getForerkortKodeKlasse())));

        assertThat(forerkort).containsAnyOf(
                "DE - Buss med tilhenger",
                "BE - Personbil med tilhenger",
                "C1 - Lett lastebil",
                "C1E - Lett lastebil med tilhenger",
                "C - Lastebil",
                "CE - Lastebil med tilhenger",
                "D1 - Minibuss",
                "D1E - Minibuss med tilhenger",
                "D - Buss");
    }

    @Test
    public void testForerkortTraktorIkkeInkludertIAndreForerkort() {
        Sokeresultat sokeresultatForerkort = sokClient.veilederSok(SokekriterierVeiledere.med().forerkort(Collections.singletonList("T - Traktor")).bygg());

        List<EsCv> cver = sokeresultatForerkort.getCver();
        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));

        Set<String> forerkort = new HashSet<>();
        cver.forEach(cv -> cv.getForerkort().forEach(f -> forerkort.add(f.getForerkortKodeKlasse())));
        assertThat(forerkort).contains("T - Traktor");
    }

    @Test
    public void testForerkortSnoscooterIkkeInkludertIAndreForerkort() {
        Sokeresultat sokeresultatForerkort = sokClient.veilederSok(SokekriterierVeiledere.med().forerkort(Collections.singletonList("S - Snøscooter")).bygg());

        List<EsCv> cver = sokeresultatForerkort.getCver();
        assertThat(cver)
                .doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));

        Set<String> forerkort = new HashSet<>();
        cver.forEach(cv -> cv.getForerkort().forEach(f -> forerkort.add(f.getForerkortKodeKlasse())));
        assertThat(forerkort).contains("S - Snøscooter");
    }

    @Test
    public void testStemOrdSkalGiSammeResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().stillingstitler(Collections.singletonList("Progger")).bygg());
        Sokeresultat sokeresultatStemOrd = sokClient.veilederSok(SokekriterierVeiledere.med().stillingstitler(Collections.singletonList("Progg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cverStemOrd = sokeresultatStemOrd.getCver();

        assertThat(cver.size()).isEqualTo(cverStemOrd.size());
        assertThat(cver.get(0)).isEqualTo(cverStemOrd.get(0));
    }

    @Test
    public void testSamletKompetanseSkalIkkeGiResultatVedSokPaSprak() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(0);
    }

    @Test
    public void testSokPaSprak() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().sprak(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaSertifikater() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList("Truckførerbevis")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void testSamletKompetanseSkalIkkeGiResultatVedSokPaForerkort() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("T - Traktor")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(0);
    }

    @Test
    @Disabled
    public void testSamletKompetanseSkalGiResultatVedSokPaKurs() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("Spring Boot")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaKompetanse() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("Javautvikler")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void testSokPaFlereGeografiJobbonskerGirFlereResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO12.1201")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(asList("NO12.1201", "NO11.1103")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver2.size()).isGreaterThan(cver.size());
    }

    @Test
    public void testSokPaBostedGirBegrensendeResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO12.1201")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .geografiList(List.of("NO12.1201")).maaBoInnenforGeografi(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testSokPaBostedOgFylkeMed0SomPrefixSkalGiBegrensendeResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO02")).bygg());
        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .geografiList(Collections.singletonList("NO02")).maaBoInnenforGeografi(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
        assertThat(cver2.size()).isEqualTo(1);
        assertThat(cver2.get(0).getFodselsnummer()).isEqualTo(EsCvObjectMother.giveMeEsCv5().getFodselsnummer());
    }

    @Test
    public void testPaTotalYrkeserfaringSkalGiKorrektResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().totalYrkeserfaring(Collections.singletonList("37-75")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isGreaterThan(0);

        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaFlereUtdanningsnivaSkalGiFlereResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Master")).bygg());
        Sokeresultat sokeresultat1 = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(asList("Master", "Videregaende")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();

        assertThat(cver.size()).isLessThan(cver1.size());
    }

    @Test
    public void sokPaIngenUtdanningSkalGiKorrektResultat() {
        Sokeresultat sokeresultatIngen = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Ingen")).bygg());
        Sokeresultat sokeresultatIngenOgGrunnskole = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(asList("Ingen", "Master")).bygg());

        List<EsCv> cverIngen = sokeresultatIngen.getCver();
        List<EsCv> cverIngenOgGrunnskole = sokeresultatIngenOgGrunnskole.getCver();

        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv5())));
        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv2())));
        assertThat(cverIngen.size()).isLessThan(cverIngenOgGrunnskole.size());
    }

    @Test
    public void sokPaaKommuneSkalGiJobbonsketreffPaaKommuneHeleNorge() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO19.1903")).bygg());

        // 09568410230 giveMeEsCv4
        // 03050316895 giveMeEsCv5

        List<EsCv> cver = sokeresultat.getCver();

        // 2 indekserte og synlige CV-er har geografi-ønsker i eksakt kommune eller hele Norge
        assertThat(cver.size()).isEqualTo(2);
        assertTrue(cver.contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv4())));
        assertTrue(cver.contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5())));
    }

    @Test
    public void sokPaFylkeSkalGiTreffPaFylke() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO03")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(toList());

        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()).getKandidatnr());
    }

    @Test
    public void sokPaFylkeSkalGiTreffPaKommune() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO12")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(toList());

        assertThat(cver.size()).isGreaterThanOrEqualTo(3);
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv6()).getKandidatnr());
    }

    @Test
    public void sokPaKommuneSkalGiTreffPaFylke() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO04.0403")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(toList());

        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()).getKandidatnr());
    }

    @Test
    public void sokPaKommuneSkalIkkeInkludereAndreKommuner() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().geografiList(Collections.singletonList("NO12.1201")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(toList());

        assertThat(fnrList).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()).getKandidatnr());
        assertThat(fnrList).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv6()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()).getKandidatnr());
    }

    @Test
    public void skalBulkIndeksereCVerIdempotent() {
        List<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCv2(),
                        EsCvObjectMother.giveMeEsCv3(), EsCvObjectMother.giveMeEsCv4(),
                        EsCvObjectMother.giveMeEsCv5());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9999));

        int antallForBulkIndeksering =
                sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver().size();
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        int antallEtterIndeksering =
                sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());

        // Reindekser
        indexerClient.bulkIndex(bulkEventer, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        antallEtterIndeksering =
                sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());
    }

    @Test
    public void skalBulkSletteCVer() {
        List<String> sletteIder = asList(EsCvObjectMother.giveMeEsCv().getKandidatnr(),
                EsCvObjectMother.giveMeEsCv2().getKandidatnr());

        int antallForBulkSletting =
                sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver().size();
        indexerClient.bulkSlettKandidatnr(sletteIder, ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
        int antallEtterSletting =
                sokClient.veilederSok(SokekriterierVeiledere.med().bygg()).getCver().size();

        Assertions.assertThat(antallForBulkSletting - antallEtterSletting)
                .isEqualTo(sletteIder.size());
    }

    @Test
    public void sokPaYrkeJobbonskerSkalGiKorrektResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Collections.singletonList("Lastebilsjåfør")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void sokPaFlereYrkeJobbonskerSkalGiUtvidendeResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Collections.singletonList("Butikkmedarbeider")).bygg());
        Sokeresultat sokeresultat1 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Ordfører")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();
        assertThat(cver.size()).isLessThan(cver1.size());
    }

    @Test
    public void skalAggregerePaaKompetanse() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Industrimekaniker", "Ordfører"))
                .bygg());

        List<Aggregering> aggregeringer = sokeresultat.getAggregeringer();
        assertThat(aggregeringer.size()).isEqualTo(1);
        assertThat(aggregeringer.get(0).getNavn()).isEqualTo("kompetanse");
        assertThat(aggregeringer.get(0).getFelt().size()).isEqualTo(10);
    }

    @Test
    public void sokPaYrkeSkalGiResultatSortertPaNyestRelevantErfaring() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Industrimekaniker", "Ordfører"))
                .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv1 = cver.get(0);
        EsCv cv2 = cver.get(1);
        EsCv cv3 = cver.get(2);
        EsCv cv4 = cver.get(3);
        assertThat(cv1)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
        assertThat(cv2)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
        assertThat(cv3)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv4()));
        assertThat(cv4)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaTruckoererbevisT1SkalGiRiktigResultat() {
        List<String> typeaheadResultat = sokClient.typeAheadKompetanse("Truckførerbevis T1 Lavt");

        String typeaheadElement = typeaheadResultat.get(0);
        assertThat(typeaheadElement).isEqualTo(
                "Truckførerbevis T1 Lavtløftende plukktruck, palletruck m/perm. førerplass");

        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList(typeaheadElement)).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);
        EsCv cv1 = cver.get(0);
        assertThat(cv1)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void skalKunneIndeksereOppCvUtenKompetanser() {
        indexerClient.index(EsCvObjectMother.giveMeCvUtenKompetanse(), ElasticSearchTestConfiguration.DEFAULT_INDEX_NAME);
    }

    @Test
    public void hentKandidaterBevarerRekkefolge() {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        String KANDIDATNUMMER3 = "2L";
        List<String> kandidatnummer1 = asList(KANDIDATNUMMER1, KANDIDATNUMMER2, KANDIDATNUMMER3);
        List<String> kandidatnummer2 = asList(KANDIDATNUMMER3, KANDIDATNUMMER1, KANDIDATNUMMER2);

        Sokeresultat resultat1 = sokClient.veilederHentKandidater(kandidatnummer1);
        List<String> resultatkandidatnummer1 = resultat1.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer1).isEqualTo(kandidatnummer1);

        Sokeresultat resultat2 = sokClient.veilederHentKandidater(kandidatnummer2);
        List<String> resultatkandidatnummer2 = resultat2.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer2).isEqualTo(kandidatnummer2);
    }

    @Test
    public void hentKandidaterHandtererIkkeeksisterendeKandidatnummer() {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        List<String> kandidatnummer =
                asList(KANDIDATNUMMER1, "IKKEEKSISTERNDE_KANDIDATNUMMER", KANDIDATNUMMER2);

        Sokeresultat resultat = sokClient.veilederHentKandidater(kandidatnummer);
        List<String> resultatkandidatnummer = resultat.getCver().stream()
                .map(EsCv::getKandidatnr).collect(toList());
        assertThat(resultatkandidatnummer).isEqualTo(asList(KANDIDATNUMMER1, KANDIDATNUMMER2));
    }

    @Test
    public void sokMedIngenUtdanningSkalGiFlerResultaterSelvOmManSpesifisererUtdanning() {
        Sokeresultat sokeresultatVideregaende = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Videregaende"))
                .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        Sokeresultat sokeresultatVideregaendeOgIngenUtdanning = sokClient
                .veilederSok(SokekriterierVeiledere.med().utdanningsniva(asList("Ingen", "Videregaende"))
                        .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        List<EsCv> cverVideregaende = sokeresultatVideregaende.getCver();
        List<EsCv> cverVideregaendeOgIngenUtdanning =
                sokeresultatVideregaendeOgIngenUtdanning.getCver();
        assertThat(cverVideregaende.size()).isLessThan(cverVideregaendeOgIngenUtdanning.size());
    }

    @Test
    public void sokMedDoktorgradSkalIkkeGiResulatMedKandidaterUtenDoktorgrad() {
        Sokeresultat sokeresultatDoktorgrad = sokClient.veilederSok(SokekriterierVeiledere.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());

        assertThat(sokeresultatDoktorgrad.getCver()).hasSize(1);
    }

    @Test
    public void sokMedFraTilSkalReturnereRiktigAntallKandidater() {
        SokekriterierVeiledere antall1 = SokekriterierVeiledere.med().antallResultater(1).bygg();
        List<EsCv> cver = sokClient.veilederSok(antall1).getCver();
        assertThat(cver).hasSize(1);

        final int fraIndex = 2;
        assertThat(setupCver.size()).isGreaterThan(fraIndex);
        SokekriterierVeiledere flereEnnDetSomErIIndeksen = SokekriterierVeiledere.med().fraIndex(fraIndex).antallResultater(8888).bygg();
        cver = sokClient.veilederSok(flereEnnDetSomErIIndeksen).getCver();
        assertThat(cver).hasSize(setupCver.size() - fraIndex);
    }

    @Test
    public void sokPaaYrkeSkalIkkeGiTreffPaaLignendeYrker() {
        Sokeresultat sokeresultatKonsulentData = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Collections.singletonList("Konsulent (data)")).bygg());

        Sokeresultat sokeresultatKonsulentBank = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Collections.singletonList("Konsulent (bank)")).bygg());


        List<EsCv> cverKonsulentData = sokeresultatKonsulentData.getCver();
        assertThat(cverKonsulentData.size()).isEqualTo(1);
        List<EsCv> cverKonsulentBank = sokeresultatKonsulentBank.getCver();
        assertThat(cverKonsulentBank.size()).isEqualTo(1);

        EsCv cv1Data = cverKonsulentData.get(0);
        EsCv cv1Bank = cverKonsulentBank.get(0);
        assertThat(cv1Data)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
        assertThat(cv1Bank)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void sokPaKvalifiseringsgruppekodeSkalGiKorrektResultat() {
        final String kvalifiseringsgruppekode = "IKVAL";
        SokekriterierVeiledere sokekriterier = SokekriterierVeiledere.med().kvalifiseringsgruppeKoder(List.of(kvalifiseringsgruppekode)).bygg();

        List<EsCv> cver = sokClient.veilederSok(sokekriterier).getCver();

        assertThat(cver).isNotEmpty();
        String msg = "Kvalifiseringsgruppekode skal være " + kvalifiseringsgruppekode;
        assertThat(cver).allMatch(cv -> cv.getKvalifiseringsgruppekode().equals("IKVAL"), msg);
    }

    @Test
    public void sokPaToKvalifiseringsgruppekodeSkalIkkeInnsnevre() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kvalifiseringsgruppeKoder(Arrays.asList("IKVAL", "IBATT")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).hasSize(1);
        assertThat(cver).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaFnrSkalGiKorrektResultat() {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983651");
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void sokPaIkkeEksisterendeFnrSkalGiEmpty() {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983622");
        assertThat(optional).isNotPresent();
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaBeskrivelse() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("yrkeskarriere").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokeResultaterSkalInkludereFelterSomIkkeHarAnnotasjon() {
        assertThat(sokClient.veilederHent("2L").get().getKompetanseObj().get(0).getKompKode()).isEqualTo("265478");
    }

    @Test
    public void veilederHentEnKandidat() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("2L");
        assertThat(esCv.get().getKandidatnr()).isEqualTo("2L");
    }

    @Test
    public void arbeidsgiverHentEnKandidat() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("1L");
        assertThat(esCv.get().getKandidatnr()).isEqualTo("1L");
    }

    @Test
    public void arbeidsgiverHentEnKandidatSomIkkeFinnes() {
        Optional<no.nav.arbeidsgiver.kandidat.kandidatsok.es.domene.EsCv> esCv = sokClient.veilederHent("finnes-ikke");
        assertThat(esCv).isNotPresent();
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaBeskrivelseUavhengigAvCasing() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("YRKESkarriere").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaArbeidsgiver() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokMedFritekstSkalGiTreffVedBrukAvFlereOrd() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome yrkeskarriere selvstendig").bygg());
        assertThat(sokeresultat.getCver()).hasSize(2);
        assertThat(sokeresultat.getCver()).containsExactlyInAnyOrder(
                kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()),
                kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void sokPaaKandidaterSkalInneholdeMinimumEnMedSattOppstart() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().bygg());
        List<EsCv> collect = sokeresultat.getCver().stream().filter(esCv -> Objects.nonNull(esCv.getOppstartKode())).collect(toList());
        assertThat(collect).size().isGreaterThan(0);
    }

    @Test
    public void sokMedSynonymerIJobbonskerSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .yrkeJobbonsker(Collections.singletonList("Trailersjåffis")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));

    }

    @Test
    public void sokMedSynonymerIYrkeserfaringSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Javaguru")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

    }

    @Test
    public void sokMedTypeaheadForYrkeserfaringSkalGiTreffFordiTypeaheadFeltSkalLiggeISoketitler() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Javautvikler")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));

        List<EsYrkeserfaring> yrkeserfaringer = sokeresultat.getCver().get(0).getYrkeserfaring();

        assertThat(yrkeserfaringer.stream()
                .anyMatch(yrkeserfaring ->
                        yrkeserfaring.getSokeTitler().stream()
                                .anyMatch("Javautvikler"::equals)
                )).isTrue();
    }

    @Test
    public void sokPaaStillingsTittelMedFilterPaaNyligeSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Anleggsmaskinfører")).antallAarGammelYrkeserfaring(null).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(2);

        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "11L", "6L");

        Sokeresultat sokeresultat2 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Anleggsmaskinfører")).antallAarGammelYrkeserfaring(10).bygg());

        List<EsCv> cver2 = sokeresultat2.getCver();
        assertThat(cver2.size()).isEqualTo(1);

        assertThat(cver2).extracting(Extractors.byName("kandidatnr")).containsExactly(
                "11L");

        Sokeresultat sokeresultat3 = sokClient.veilederSok(SokekriterierVeiledere.med()
                .stillingstitler(Collections.singletonList("Anleggsmaskinfører")).antallAarGammelYrkeserfaring(5).bygg());

        List<EsCv> cver3 = sokeresultat3.getCver();
        assertThat(cver3.size()).isEqualTo(0);
    }

    @Test
    public void sokMedSynonymerIKompetanseSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().kompetanser(Collections.singletonList("Java (8)")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);

        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokMedFritekstSkalFungereForArbeidsgivere() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("yrkeskarriere").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void typeaheadPaaNavkontorFungerer() {
        List<String> typeAheadNavkontor = sokClient.typeAheadNavkontor("Gamle O");
        assertThat(typeAheadNavkontor).hasSize(1);
        assertThat(typeAheadNavkontor).containsExactly("0316 NAV Gamle Oslo");
    }

    @Test
    public void typeaheadPaaNavkontorSkalHaandtereFlereValg() {
        List<String> typeAheadNavkontor = sokClient.typeAheadNavkontor("NAV");
        assertThat(typeAheadNavkontor).hasSize(4);
        assertThat(typeAheadNavkontor).containsExactlyInAnyOrder("0316 NAV Gamle Oslo", "0220 NAV Asker", "0602 NAV Drammen", "0215 NAV Drøbak");
    }

    @Test
    public void typeaheadPaaNavkontorSkalVaereCaseInsensitive() {
        List<String> typeAheadNavkontor = sokClient.typeAheadNavkontor("asker");
        assertThat(typeAheadNavkontor).hasSize(1);
        assertThat(typeAheadNavkontor).containsExactly("0220 NAV Asker");
    }

    @Test
    public void typeaheadPaaNavkontorSkalGiFlereValg() {
        List<String> typeAheadNavkontor = sokClient.typeAheadNavkontor("dr");
        assertThat(typeAheadNavkontor).hasSize(2);
        assertThat(typeAheadNavkontor).containsExactlyInAnyOrder("0602 NAV Drammen", "0215 NAV Drøbak");
    }

    @Test
    public void sokPaNavkontorSkalGiKorrektResultat() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .navkontor(Collections.singletonList("0316 NAV Gamle Oslo")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).hasSize(1);
        assertThat(cver).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaToNavkontorSkalIkkeInnsnevre() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .navkontor(Arrays.asList("0316 NAV Gamle Oslo", "NAV Noe annet")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).hasSize(1);
        assertThat(cver).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaaFodselsdatoMedFraTilSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .antallAarFra(39).antallAarTil(40).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isGreaterThanOrEqualTo(1);
        assertThat(cver).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
        assertThat(cver).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()));
    }

    @Test
    public void sokPaaFodselsdatoMedKunFraSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .antallAarFra(39).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isGreaterThanOrEqualTo(1);
        assertThat(cver).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
        assertThat(cver).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()));
    }

    @Test
    public void sokPaaFodselsdatoMedKunTilSkalGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .antallAarTil(40).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isGreaterThanOrEqualTo(1);
        assertThat(cver).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
        assertThat(cver).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()));
    }

    @Test
    public void sokPaaFodselsdatoUtenforRangeSkalIkkeGiTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .antallAarFra(10).antallAarTil(38).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaaArbeidserfaringUtenMatchOgNoeAnnetSkalIkkeGiTreff() {
        String komp =
                "Truckførerbevis T1 Lavtløftende plukktruck, palletruck m/perm. førerplass";

        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kompetanser(Collections.singletonList(komp)).stillingstitler(Collections.singletonList("DENNEFINNESIKKE")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(0);

    }

    @Test
    public void sokMedTilretteleggingsbehovSkalGiKorrektTreff() {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().tilretteleggingsbehov(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(3);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "6L", "4L", "3L");
    }

    @Test
    public void sokMedVeilTilretteleggingsbehovSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().veilTilretteleggingsbehov(Collections.singletonList(Kat1_Kode))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(2);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "4L", "5L");
    }

    @Test
    public void sokMedKunEnRiktigVeilTilretteleggingsbehovSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().veilTilretteleggingsbehov(Arrays.asList(Kat1_Kode, "Kat_Eksistererikke_Kode"))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(2);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "4L", "5L");
    }

    @Test
    public void sokMedVeilTilretteleggingsbehovOgPermittertSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().veilTilretteleggingsbehov(Arrays.asList(Kat1_Kode, "Kat_Eksistererikke_Kode"))
                        .permittert(Boolean.TRUE).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "5L");
    }

    @Test
    public void sokMedVeilTilretteleggingsbehovUtelukketSkalGiKorrektTreff() {
        SokekriterierVeiledere sokekriterier = SokekriterierVeiledere
                .med().veilTilretteleggingsbehovUtelukkes(List.of(Kat1_Kode))
                .bygg();

        List<EsCv> cver = sokClient.veilederSok(sokekriterier).getCver();
        Map<String, List<String>> fraKandidatnrTilBehov = tilretteleggingsbehov(cver);
        assertThat(cver.size()).isGreaterThan(0);
        fraKandidatnrTilBehov.forEach((kandidatnr, behov) -> {
            String msg = "Feil innhold i tilretteleggingsbehov for kandidatnr " + kandidatnr + ": " + behov;
            assertFalse(behov.contains(Kat1_Kode), msg);
        });
    }

    @Test
    public void sokMedKunEnRiktigVeilTilretteleggingsbehovUtelukketSkalGiKorrektTreff() {
        final String skalUtelukkes = Kat1_Kode;
        final String utelukkesIkkeeksisterende = "Kat_Eksistererikke_Kode";
        SokekriterierVeiledere sokekriterier = SokekriterierVeiledere.med().veilTilretteleggingsbehovUtelukkes(asList(skalUtelukkes, utelukkesIkkeeksisterende)).bygg();
        assertThat(setupCver).anyMatch(cv -> cv.getVeilTilretteleggingsbehov().contains(skalUtelukkes));

        List<EsCv> cver = sokClient.veilederSok(sokekriterier).getCver();

        assertThat(cver).isNotEmpty();
        assertThat(cver).allSatisfy(cv -> {
            assertThat(cv.getVeilTilretteleggingsbehov()).doesNotContain(skalUtelukkes);
            assertThat(cv.getVeilTilretteleggingsbehov()).doesNotContain(utelukkesIkkeeksisterende);
        });
    }

    @Test
    public void sokMedVeilTilretteleggingsbehovUtelukketOgIkkePermittertSkalGiKorrektTreff() {
        SokekriterierVeiledere søkekriterier = SokekriterierVeiledere
                .med().veilTilretteleggingsbehovUtelukkes(asList(Kat1_Kode, "Kat_Eksistererikke_Kode"))
                .permittert(Boolean.FALSE).bygg();

        List<EsCv> cver = sokClient.veilederSok(søkekriterier).getCver();

        assertThat(cver).isNotEmpty();
        assertThat(cver).allSatisfy(cv -> {
            List<String> behov = cv.getVeilTilretteleggingsbehov();
            assertThat(behov).doesNotContain(Kat1_Kode);
            assertThat(behov).doesNotContain("Kat_Eksistererikke_Kode");
            assertThat(behov).doesNotContain("permittert");
        });
    }

    @Test
    public void sokMedVeilTilretteleggingsbehovUtelukketOgPermittertSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().veilTilretteleggingsbehovUtelukkes(Arrays.asList(Kat1_Kode, "Kat_Eksistererikke_Kode"))
                        .permittert(Boolean.TRUE).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).doesNotContain(
                "5L");
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).contains(
                "3L");
    }

    @Test
    public void sokMedSistEndretFilter7Dager() {
        final int periodeAntallDager = 7;
        SokekriterierVeiledere søkekriterier = SokekriterierVeiledere.med().antallDagerSistEndret(periodeAntallDager).bygg();
        final List<String> expectedKandidatnumre = setupCver.stream().filter(cv -> cv.getTidsstempel().after(antallDagerTilbakeFraNow(periodeAntallDager + 1))).map(cv -> cv.getKandidatnr()).collect(toList());
        assertThat(expectedKandidatnumre).isNotEmpty();

        List<EsCv> cver = sokClient.veilederSok(søkekriterier).getCver();

        assertThat(kandidatnumre(cver)).containsExactlyInAnyOrderElementsOf(expectedKandidatnumre);
    }

    @Test
    public void sokMedSistEndretFilter15Dager() {
        final int periodeAntallDager = 15;
        SokekriterierVeiledere søkekriterier = SokekriterierVeiledere.med().antallDagerSistEndret(periodeAntallDager).bygg();
        final List<String> expectedKandidatnumre = setupCver.stream().filter(cv -> cv.getTidsstempel().after(antallDagerTilbakeFraNow(periodeAntallDager + 1))).map(cv -> cv.getKandidatnr()).collect(toList());
        assertThat(expectedKandidatnumre).isNotEmpty();

        List<EsCv> cver = sokClient.veilederSok(søkekriterier).getCver();

        assertThat(kandidatnumre(cver)).containsExactlyInAnyOrderElementsOf(expectedKandidatnumre);
    }


    @Test
    public void sokMedTilgjengeligFilterSkalSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Collections.singletonList(tilgjengelig))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder("2L", "6L", "11L");
    }

    @Test
    public void sokMedMidlertidigUtilgjengeligEnUkeSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Collections.singletonList(tilgjengeligInnen1Uke))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactly("4L");
    }

    @Test
    public void sokMedMidlertidigUtilgjengeligSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Collections.singletonList(midlertidigUtilgjengelig))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder("3L", "5L");
    }

    @Test
    public void sokMedIngenEllerAlleMidlertidigTilgjengeligFilterSkalGiAlleKandidater() {
        SokekriterierVeiledere ingen = SokekriterierVeiledere.med().midlertidigUtilgjengelig(emptyList()).bygg();
        SokekriterierVeiledere alle = SokekriterierVeiledere.med().midlertidigUtilgjengelig(asList(tilgjengelig, midlertidigUtilgjengelig, tilgjengeligInnen1Uke)).bygg();

        List<EsCv> cver = sokClient.veilederSok(ingen).getCver();
        assertThat(cver.size()).isEqualTo(setupCver.size());
        assertThat(setupKandidatnumre).containsExactlyInAnyOrderElementsOf(kandidatnumre(cver));

        cver = sokClient.veilederSok(alle).getCver();
        assertThat(cver.size()).isEqualTo(setupCver.size());
        assertThat(setupKandidatnumre).containsExactlyInAnyOrderElementsOf(kandidatnumre(cver));

    }

    @Test
    public void sokMedMidlertidigUtilgjengeligOgMidlertidigUtilgjengeligEnUkeSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Arrays.asList(midlertidigUtilgjengelig, tilgjengeligInnen1Uke))
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder("3L", "4L", "5L");
    }

    @Test
    public void sokMedMidlertidigUtilgjengeligOgTilgjengeligSkalGiKorrektTreff() {
        SokekriterierVeiledere sokekriterier = SokekriterierVeiledere
                .med().midlertidigUtilgjengelig(asList(tilgjengelig, midlertidigUtilgjengelig))
                .bygg();
        List<EsCv> cver = sokClient.veilederSok(sokekriterier).getCver();

        assertThat(cver.size()).isGreaterThan(1);
        Map<String, List<String>> fraKandidatnrTilBehov = tilretteleggingsbehov(cver);
        fraKandidatnrTilBehov.forEach((kandidatnr, behov) -> {
            String msg = "Feil innhold i tilretteleggingsbehov for kandidatnr " + kandidatnr + ": " + behov;
            assertTrue(behov.isEmpty() || behov.contains(midlertidigUtilgjengelig), msg);
            assertFalse(behov.contains(tilgjengeligInnen1Uke), msg);
        });
    }

    @Test
    public void sokMedTilgjengeligOgTilgjengeligEnUkeSkalGiKorrektTreff() {
        SokekriterierVeiledere sokekriterier = SokekriterierVeiledere
                .med().midlertidigUtilgjengelig(asList(tilgjengelig, tilgjengeligInnen1Uke))
                .bygg();
        List<EsCv> cver = sokClient.veilederSok(sokekriterier).getCver();

        assertThat(cver.size()).isGreaterThan(1);
        Map<String, List<String>> fraKandidatnrTilBehov = tilretteleggingsbehov(cver);
        fraKandidatnrTilBehov.forEach((kandidatnr, behov) -> {
            String msg = "Feil innhold i tilretteleggingsbehov for kandidatnr " + kandidatnr + ": " + behov;
            assertTrue(behov.isEmpty() || behov.contains(tilgjengeligInnen1Uke), msg);
            assertFalse(behov.contains(midlertidigUtilgjengelig));
        });
    }

    @Test
    public void sokMedMidlertidigUtilgjengeligOgPermittertSkalGiKorrektTreff() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Collections.singletonList(midlertidigUtilgjengelig))
                        .permittert(true)
                        .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder("3L", "5L");
    }

    @Test
    public void veilederSokSkalReturnereTilretteleggingsbehov() {
        Sokeresultat sokeresultat =
                sokClient.veilederSok(SokekriterierVeiledere
                        .med().midlertidigUtilgjengelig(Collections.singletonList(tilgjengeligInnen1Uke))
                        .bygg());

        List<String> tilretteleggingbehov = sokeresultat.getCver().get(0).getVeilTilretteleggingsbehov();
        assertThat(tilretteleggingbehov).isNotEmpty();
        assertThat(tilretteleggingbehov).containsExactlyInAnyOrderElementsOf(EsCvObjectMother.giveMeEsCv4().getVeilTilretteleggingsbehov());
    }
}
