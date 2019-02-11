package no.nav.arbeid.cv.kandidatsok.es;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.assertj.core.api.Assertions;
import org.assertj.core.extractor.Extractors;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.ShutdownStrategy;
import com.palantir.docker.compose.connection.DockerMachine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import no.nav.arbeid.cv.indexer.config.EsServiceConfig;
import no.nav.arbeid.cv.kandidatsok.domene.es.EsCvObjectMother;
import no.nav.arbeid.cv.kandidatsok.domene.es.KandidatsokTransformer;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Aggregering;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.EsCv;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokekriterier;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.SokekriterierVeiledere;
import no.nav.arbeid.cv.kandidatsok.es.domene.sok.Sokeresultat;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerHttpService;
import no.nav.arbeid.kandidatsok.es.client.EsIndexerService;
import no.nav.arbeid.kandidatsok.es.client.EsSokService;

@RunWith(SpringRunner.class)
public class IndexCvTest {

    /*
     * For å kunne kjøre denne testen må Linux rekonfigureres litt.. Lag en fil i
     * /etc/sysctl.d/01-increase_vm_max_map_count.conf som inneholder følgende: vm.max_map_count =
     * 262144
     */

    // Kjører "docker-compose up" manuelt istedenfor denne ClassRule:

    @ClassRule
    public static DockerComposeRule docker =
            DockerComposeRule.builder().file("src/test/resources/docker-compose-kun-es.yml")
                    .machine(DockerMachine.localMachine()
                            .withAdditionalEnvironmentVariable("ES_PORT",
                                    System.getProperty("ES_PORT"))
                            .build())
                    .shutdownStrategy(ShutdownStrategy.KILL_DOWN).build();

    @Autowired
    private EsSokService sokClient;

    @Autowired
    private EsIndexerService indexerClient;

    @Autowired
    private ObjectMapper objectMapper;

    private KandidatsokTransformer kandidatsokTransformer = new KandidatsokTransformer();

    @Configuration
    @Import({EsServiceConfig.class})
    static class TestConfig {

        @Bean
        @Autowired
        public RestHighLevelClient restHighLevelClient(@Value("${ES_PORT}") Integer port) {
            return new RestHighLevelClient(
                    RestClient.builder(new HttpHost("localhost", port, "http")));
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public MeterRegistry meterRegistry() {
            Counter counter = mock(Counter.class);
            MeterRegistry meterRegistry = mock(MeterRegistry.class);
            when(meterRegistry.counter(anyString(), any(Tags.class))).thenReturn(counter);

            return meterRegistry;
        }
        @Bean
        public EsIndexerService indexerCvService(RestHighLevelClient restHighLevelClient,
                                                 ObjectMapper objectMapper,
                                                 MeterRegistry meterRegistry
                                                 ) {
            return new EsIndexerHttpService(restHighLevelClient, objectMapper, meterRegistry,
                    WriteRequest.RefreshPolicy.IMMEDIATE);
        }
    }

    @Before
    public void before() throws IOException {
        try {
            indexerClient.deleteIndex();
        } catch (Exception e) {
            // Ignore
        }

        indexerClient.createIndex();

        indexerClient.index(EsCvObjectMother.giveMeEsCv());
        indexerClient.index(EsCvObjectMother.giveMeEsCv2());
        indexerClient.index(EsCvObjectMother.giveMeEsCv3());
        indexerClient.index(EsCvObjectMother.giveMeEsCv4());
        indexerClient.index(EsCvObjectMother.giveMeEsCv5());
        indexerClient.index(EsCvObjectMother.giveMeEsCv6());
        indexerClient.index(EsCvObjectMother.giveMeCvForDoedPerson());
        indexerClient.index(EsCvObjectMother.giveMeCvForKode6());
        indexerClient.index(EsCvObjectMother.giveMeCvForKode7());
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForAgKandidatsok());
        indexerClient.index(EsCvObjectMother.giveMeCvFritattForKandidatsok());
    }

    @After
    public void after() throws IOException {
        indexerClient.deleteIndex();
    }

    @Test
    public void skalIkkeFeileMedIllegalArgumentFraES() throws Exception {
        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv cv1 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv1.json"),
                        "ISO-8859-1"),
                no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);
        no.nav.arbeid.cv.kandidatsok.es.domene.EsCv cv2 = objectMapper.readValue(
                new InputStreamReader(getClass().getResourceAsStream("/utfordrende_cv2.json"),
                        "ISO-8859-1"),
                no.nav.arbeid.cv.kandidatsok.es.domene.EsCv.class);

        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer = asList(cv1, cv2);

        int antallIndeksert = indexerClient.bulkIndex(bulkEventer);
        assertThat(antallIndeksert).isEqualTo(bulkEventer.size());
    }

    @Test
    @Ignore("Brukes til utforskende testing")
    public void skalLoggFeilVedBulkIndeksereCVMedNullfelter() throws Exception {
        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCvMedFeil(),
                        EsCvObjectMother.giveMeEsCv2());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9998));
        indexerClient.bulkIndex(bulkEventer);

    }

    @Test
    public void testUtenSokekriterierReturnererAlleTestPersonerForArbeidsgivere()
            throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(6);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "6L", "5L", "4L", "3L", "2L", "1L");
    }

    @Test
    public void testUtenSokekriterierReturnererIkkeJobbsForVeiledere() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(6);
        assertThat(cver).extracting(Extractors.byName("kandidatnr")).containsExactlyInAnyOrder(
                "11L", "6L", "5L", "4L", "3L", "2L");
    }

    @Test
    public void testFlereInputFritekstGirBredereResultat() throws IOException {
        Sokeresultat sokeresultat1 =
                sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("javautvikler").bygg());
        Sokeresultat sokeresultat = sokClient.veilederSok(
                SokekriterierVeiledere.med().fritekst("industrimekaniker javautvikler").bygg());

        List<EsCv> cver1 = sokeresultat1.getCver();
        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver1.size()).isLessThan(cver.size());
        assertThat(cver1.size()).isEqualTo(1);
        assertThat(cver.size()).isEqualTo(4);
    }

    @Test
    public void testSokPaNorskeStoppordGirIkkeResultat() throws IOException {
        Sokeresultat sokeresultatYrke = sokClient.arbeidsgiverSok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatKomp = sokClient.arbeidsgiverSok(
                Sokekriterier.med().kompetanser(Collections.singletonList("og")).bygg());
        Sokeresultat sokeresultatUtdanning = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanninger(Collections.singletonList("og")).bygg());
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
    public void testSokMedFlereKriterierGirSvarMedAlleFelter() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Butikkmedarbeider"))
                        .kompetanser(Collections.singletonList("Hallovert"))
                        .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(1);
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv4()));
    }

    @Test
    public void testFlereInputYrkeGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .stillingstitler(Collections.singletonList("Industrimekaniker")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(
                Sokekriterier.med().stillingstitler(asList("Progger", "Industrimekaniker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testFlereInputKompetanseGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .kompetanser(Collections.singletonList("Programvareutvikler")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .kompetanser(asList("Programvareutvikler", "Nyhetsanker")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testFlereInputUtdanningGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .utdanninger(asList("Bygg og anlegg", "master i sikkerhet")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testForerkortGirMindreTreff() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .forerkort(Collections.singletonList("Førerkort: Kl. A (tung motorsykkel)"))
                .bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .forerkort(
                        asList("Førerkort: Kl. A (tung motorsykkel)", "Førerkort: Kl. A (Moped)"))
                .bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testStemOrdSkalGiSammeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Progger")).bygg());
        Sokeresultat sokeresultatStemOrd = sokClient.arbeidsgiverSok(
                Sokekriterier.med().stillingstitler(Collections.singletonList("Progg")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cverStemOrd = sokeresultatStemOrd.getCver();

        assertThat(cver.size()).isEqualTo(cverStemOrd.size());
        assertThat(cver.get(0)).isEqualTo(cverStemOrd.get(0));
    }

    @Test
    public void testSamletKompetanseSkalIkkeGiResultatVedSokPaSprak() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(0);
    }

    @Test
    public void testSokPaSprak() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().sprak(Collections.singletonList("Dansk")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaSertifikater() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .kompetanser(Collections.singletonList("Truckførerbevis")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void testSamletKompetanseSkalIkkeGiResultatVedSokPaForerkort() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Traktorlappen")).bygg());

        List<EsCv> cver = sokeresultat.getCver();

        assertThat(cver.size()).isEqualTo(0);
    }

    @Test
    @Ignore
    public void testSamletKompetanseSkalGiResultatVedSokPaKurs() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Spring Boot")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void testSamletKompetanseSkalGiResultatVedSokPaKompetanse() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().kompetanser(Collections.singletonList("Javautvikler")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cver.size()).isEqualTo(1);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void testSokPaFlereGeografiJobbonskerGirFlereResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO12.1201")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(asList("NO12.1201", "NO11.1103")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver2.size()).isGreaterThan(cver.size());
    }

    @Test
    public void testSokPaBostedGirBegrensendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO12.1201")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .geografiList(asList("NO12.1201")).maaBoInnenforGeografi(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        assertThat(cver.size()).isGreaterThan(cver2.size());
    }

    @Test
    public void testSokPaBostedOgFylkeMed0SomPrefixSkalGiBegrensendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO02")).bygg());
        Sokeresultat sokeresultat2 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .geografiList(Collections.singletonList("NO02")).maaBoInnenforGeografi(true).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver2 = sokeresultat2.getCver();

        System.out.println(cver);
        System.out.println(cver2);

        assertThat(cver.size()).isGreaterThan(cver2.size());
        assertThat(cver2.size()).isEqualTo(1);
        assertThat(cver2.get(0).getFodselsnummer()).isEqualTo(EsCvObjectMother.giveMeEsCv5().getFodselsnummer());
    }

    @Test
    public void testPaTotalYrkeserfaringSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().totalYrkeserfaring(Collections.singletonList("37-72")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaFlereUtdanningsnivaSkalGiFlereResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Master")).bygg());
        Sokeresultat sokeresultat1 = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(asList("Master", "Videregaende")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();

        assertThat(cver.size()).isLessThan(cver1.size());
    }

    @Test
    public void sokPaIngenUtdanningSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultatIngen = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Ingen")).bygg());
        Sokeresultat sokeresultatIngenOgGrunnskole = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(asList("Ingen", "Master")).bygg());

        List<EsCv> cverIngen = sokeresultatIngen.getCver();
        List<EsCv> cverIngenOgGrunnskole = sokeresultatIngenOgGrunnskole.getCver();

        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv5())));
        // assertThat(cverIngen).contains(kandidatsokTransformer
        // .transformer(transformer.transform(TempCvEventObjectMother.giveMeEsCv2())));
        assertThat(cverIngen.size()).isLessThan(cverIngenOgGrunnskole.size());
    }

    @Test
    public void sokPaKommuneSkalGiTreffPaKommune() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO19.1903")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);

        assertThat(cv).isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void sokPaFylkeSkalGiTreffPaFylke() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO03")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(Collectors.toList());

        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()).getKandidatnr());
    }

    @Test
    public void sokPaFylkeSkalGiTreffPaKommune() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO12")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(Collectors.toList());

        assertThat(cver.size()).isGreaterThanOrEqualTo(3);
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv6()).getKandidatnr());
    }

    @Test
    public void sokPaKommuneSkalGiTreffPaFylke() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO04.0403")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(Collectors.toList());

        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv()).getKandidatnr());
    }

    @Test
    public void sokPaKommuneSkalIkkeInkludereAndreKommuner() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(
                Sokekriterier.med().geografiList(Collections.singletonList("NO12.1201")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<String> fnrList = cver.stream().map(EsCv::getKandidatnr).collect(Collectors.toList());

        assertThat(fnrList).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()).getKandidatnr());
        assertThat(fnrList).doesNotContain(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv6()).getKandidatnr());
        assertThat(fnrList).contains(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()).getKandidatnr());
    }

    @Test
    public void skalBulkIndeksereCVerIdempotent() throws Exception {
        List<no.nav.arbeid.cv.kandidatsok.es.domene.EsCv> bulkEventer =
                asList(EsCvObjectMother.giveMeEsCv(), EsCvObjectMother.giveMeEsCv2(),
                        EsCvObjectMother.giveMeEsCv3(), EsCvObjectMother.giveMeEsCv4(),
                        EsCvObjectMother.giveMeEsCv5());

        bulkEventer.forEach(e -> e.setKandidatnr(e.getKandidatnr() + 9999));

        int antallForBulkIndeksering =
                sokClient.arbeidsgiverSok(Sokekriterier.med().bygg()).getCver().size();
        indexerClient.bulkIndex(bulkEventer);
        int antallEtterIndeksering =
                sokClient.arbeidsgiverSok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());

        // Reindekser
        indexerClient.bulkIndex(bulkEventer);
        antallEtterIndeksering =
                sokClient.arbeidsgiverSok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallEtterIndeksering - antallForBulkIndeksering)
                .isEqualTo(bulkEventer.size());
    }

    @Test
    public void skalBulkSletteCVer() throws Exception {
        List<String> sletteIder = asList(EsCvObjectMother.giveMeEsCv().getKandidatnr(),
                EsCvObjectMother.giveMeEsCv2().getKandidatnr());

        int antallForBulkSletting =
                sokClient.arbeidsgiverSok(Sokekriterier.med().bygg()).getCver().size();
        indexerClient.bulkSlettKandidatnr(sletteIder);
        int antallEtterSletting =
                sokClient.arbeidsgiverSok(Sokekriterier.med().bygg()).getCver().size();

        Assertions.assertThat(antallForBulkSletting - antallEtterSletting)
                .isEqualTo(sletteIder.size());
    }

    @Test
    public void sokPaYrkeJobbonskerSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .yrkeJobbonsker(Collections.singletonList("Lastebilsjåfør")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        EsCv cv = cver.get(0);
        assertThat(cv)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv5()));
    }

    @Test
    public void sokPaFlereYrkeJobbonskerSkalGiUtvidendeResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .yrkeJobbonsker(Collections.singletonList("Butikkmedarbeider")).bygg());
        Sokeresultat sokeresultat1 = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Ordfører")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        List<EsCv> cver1 = sokeresultat1.getCver();
        assertThat(cver.size()).isLessThan(cver1.size());
    }
    
    @Test
    public void skalAggregerePaaKompetanse() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .yrkeJobbonsker(Arrays.asList("Butikkmedarbeider", "Industrimekaniker", "Ordfører"))
                .bygg());

        List<Aggregering> aggregeringer = sokeresultat.getAggregeringer();
        assertThat(aggregeringer.size()).isEqualTo(1);
        assertThat(aggregeringer.get(0).getNavn()).isEqualTo("kompetanse");
        assertThat(aggregeringer.get(0).getFelt().size()).isEqualTo(10);
    }

    @Test
    public void sokPaYrkeSkalGiResultatSortertPaNyestRelevantErfaring() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
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
    public void sokPaTruckoererbevisT1SkalGiRiktigResultat() throws IOException {
        List<String> typeaheadResultat = sokClient.typeAheadKompetanse("Truckførerbevis T1 Lavt");

        String typeaheadElement = typeaheadResultat.get(0);
        assertThat(typeaheadElement).isEqualTo(
                "Truckførerbevis T1 Lavtløftende plukktruck, palletruck m/perm. førerplass");

        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .kompetanser(Collections.singletonList(typeaheadElement)).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver.size()).isEqualTo(1);
        EsCv cv1 = cver.get(0);
        assertThat(cv1)
                .isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }


    @Test
    public void skalKunneIndeksereOppCvUtenKompetanser() throws IOException {
        indexerClient.index(EsCvObjectMother.giveMeCvUtenKompetanse());
    }

    @Test
    public void hentKandidaterBevarerRekkefolge() throws IOException {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        String KANDIDATNUMMER3 = "2L";
        List<String> kandidatnummer1 = asList(KANDIDATNUMMER1, KANDIDATNUMMER2, KANDIDATNUMMER3);
        List<String> kandidatnummer2 = asList(KANDIDATNUMMER3, KANDIDATNUMMER1, KANDIDATNUMMER2);

        Sokeresultat resultat1 = sokClient.arbeidsgiverHentKandidater(kandidatnummer1);
        List<String> resultatkandidatnummer1 = resultat1.getCver().stream()
                .map(EsCv::getKandidatnr).collect(Collectors.toList());
        assertThat(resultatkandidatnummer1).isEqualTo(kandidatnummer1);

        Sokeresultat resultat2 = sokClient.arbeidsgiverHentKandidater(kandidatnummer2);
        List<String> resultatkandidatnummer2 = resultat2.getCver().stream()
                .map(EsCv::getKandidatnr).collect(Collectors.toList());
        assertThat(resultatkandidatnummer2).isEqualTo(kandidatnummer2);
    }

    @Test
    public void hentKandidaterHandtererIkkeeksisterendeKandidatnummer() throws IOException {
        String KANDIDATNUMMER1 = "5L";
        String KANDIDATNUMMER2 = "1L";
        List<String> kandidatnummer =
                asList(KANDIDATNUMMER1, "IKKEEKSISTERNDE_KANDIDATNUMMER", KANDIDATNUMMER2);

        Sokeresultat resultat = sokClient.arbeidsgiverHentKandidater(kandidatnummer);
        List<String> resultatkandidatnummer = resultat.getCver().stream()
                .map(EsCv::getKandidatnr).collect(Collectors.toList());
        assertThat(resultatkandidatnummer).isEqualTo(asList(KANDIDATNUMMER1, KANDIDATNUMMER2));
    }

    @Test
    public void sokMedIngenUtdanningSkalGiFlerResultaterSelvOmManSpesifisererUtdanning()
            throws IOException {
        Sokeresultat sokeresultatVideregaende = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Videregaende"))
                        .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        Sokeresultat sokeresultatVideregaendeOgIngenUtdanning = sokClient
                .arbeidsgiverSok(Sokekriterier.med().utdanningsniva(asList("Ingen", "Videregaende"))
                        .utdanninger(Collections.singletonList("Bygg og anlegg")).bygg());

        List<EsCv> cverVideregaende = sokeresultatVideregaende.getCver();
        List<EsCv> cverVideregaendeOgIngenUtdanning =
                sokeresultatVideregaendeOgIngenUtdanning.getCver();
        assertThat(cverVideregaende.size()).isLessThan(cverVideregaendeOgIngenUtdanning.size());
    }

    @Test
    public void sokMedDoktorgradSkalIkkeGiResulatMedKandidaterUtenDoktorgrad() throws IOException {
        Sokeresultat sokeresultatDoktorgrad = sokClient.arbeidsgiverSok(
                Sokekriterier.med().utdanningsniva(Collections.singletonList("Doktorgrad")).bygg());

        assertThat(sokeresultatDoktorgrad.getCver()).hasSize(1);
    }

    @Test
    public void sokMedFraTilSkalReturnereRiktigAntallKandidater() throws IOException {
        Sokeresultat sokeresultat =
                sokClient.arbeidsgiverSok(Sokekriterier.med().antallResultater(1).bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);

        // fra og med tredje posisjon
        // av totalt 6 i indexen
        Sokeresultat sokeresultat2 = sokClient
                .arbeidsgiverSok(Sokekriterier.med().fraIndex(2).antallResultater(5).bygg());
        assertThat(sokeresultat2.getCver()).hasSize(4);
    }

    @Test
    public void sokPaaYrkeSkalIkkeGiTreffPaaLignendeYrker() throws IOException {
        Sokeresultat sokeresultatKonsulentData = sokClient.arbeidsgiverSok(Sokekriterier.med()
                .yrkeJobbonsker(Collections.singletonList("Konsulent (data)")).bygg());

        Sokeresultat sokeresultatKonsulentBank = sokClient.arbeidsgiverSok(Sokekriterier.med()
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
    public void sokPaKvalifiseringsgruppekodeSkalGiKorrektResultat() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kvalifiseringsgruppeKoder(Collections.singletonList("IKVAL")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).hasSize(1);
        assertThat(cver).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaToKvalifiseringsgruppekodeSkalIkkeInnsnevre() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med()
                .kvalifiseringsgruppeKoder(Arrays.asList("IKVAL", "IBATT")).bygg());

        List<EsCv> cver = sokeresultat.getCver();
        assertThat(cver).hasSize(1);
        assertThat(cver).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokPaFnrSkalGiKorrektResultat() throws IOException {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983651");
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void sokPaIkkeEksisterendeFnrSkalGiEmpty() throws IOException {
        Optional<EsCv> optional = sokClient.veilederSokPaaFnr("04265983622");
        assertThat(optional).isNotPresent();
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaBeskrivelse() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("yrkeskarriere").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }
    
    @Test
    public void sokeResultaterSkalInkludereFelterSomIkkeHarAnnotasjon() throws IOException {
        assertThat(sokClient.veilederHent("2L").get().getKompetanseObj().get(0).getKompKode()).isEqualTo("265478");
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaBeskrivelseUavhengigAvCasing() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("YRKESkarriere").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokMedFritekstSkalGiTreffPaaArbeidsgiver() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome").bygg());
        assertThat(sokeresultat.getCver()).hasSize(1);
        assertThat(sokeresultat.getCver()).containsExactly(kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()));
    }

    @Test
    public void sokMedFritekstSkalGiTreffVedBrukAvFlereOrd() throws IOException {
        Sokeresultat sokeresultat = sokClient.veilederSok(SokekriterierVeiledere.med().fritekst("Awesome yrkeskarriere selvstendig").bygg());
        assertThat(sokeresultat.getCver()).hasSize(2);
        assertThat(sokeresultat.getCver()).containsExactlyInAnyOrder(
                kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv2()),
                kandidatsokTransformer.transformer(EsCvObjectMother.giveMeEsCv3()));
    }

    @Test
    public void sokPaaKandidaterSkalInneholdeMinimumEnMedSattOppstart() throws IOException {
        Sokeresultat sokeresultat = sokClient.arbeidsgiverSok(Sokekriterier.med().bygg());
        List<EsCv> collect = sokeresultat.getCver().stream().filter(esCv -> Objects.nonNull(esCv.getOppstartKode())).collect(Collectors.toList());
        assertThat(collect).size().isGreaterThan(0);
    }

}
