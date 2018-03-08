package no.nav.arbeid.cv.es.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import no.nav.arbeid.cv.es.domene.EsCv;

@Repository
public interface EsCvRepository extends ElasticsearchRepository<EsCv, String> {

  List<EsCv> findByYrkeserfaringStyrkKode(String styrk);

  List<EsCv> findByYrkeserfaringStyrkKodeTekst(String styrkBeskrivelse);

  Page<EsCv> findByYrkeserfaringStyrkKode(String styrk, Pageable pageable);

}
