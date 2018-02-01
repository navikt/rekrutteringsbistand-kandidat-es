package no.nav.arbeid.cv.es.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import no.nav.arbeid.cv.es.domene.EsCv;

@Repository
public interface EsCvRepository extends ElasticsearchRepository<EsCv, String> {

  public List<EsCv> findByYrkerStyrkKode(String styrk);
  
  public List<EsCv> findByYrkerStyrkBeskrivelse(String styrkBeskrivelse);

  public Page<EsCv> findByYrkerStyrkKode(String styrk, Pageable pageable);

}
