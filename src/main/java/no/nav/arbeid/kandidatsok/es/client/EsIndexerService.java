package no.nav.arbeid.kandidatsok.es.client;

import no.nav.arbeid.cv.kandidatsok.es.domene.EsCv;
import org.elasticsearch.client.Response;

import java.util.Collection;
import java.util.List;

public interface EsIndexerService {

    void index(EsCv esCv, String indexName);

    int bulkIndex(List<EsCv> esCver, String indexName);

    void bulkSlettKandidatnr(List<String> kandidatnr, String indexName);

    /**
     * Slett basert på aktør-id-er
     * @param aktorId liste med aktør-id-er
     * @param indexName navn på målindeks
     * @return antall dokumenter faktisk slettet fra ElasticSearch
     */
    int bulkSlettAktorId(List<String> aktorId, String indexName);

    void createIndex(String indexName);

    void deleteIndex(String indexName);

    boolean doesIndexExist(String indexName);

    long antallIndeksert(String indexName);

    long antallIndeksertSynligForVeileder(String indexName);

    long antallIndeksertSynligForArbeidsgiver(String indexName);

    Collection<String> getTargetsForAlias(String alias);

    Response updateIndexAlias(String alias, String indexName);
}
