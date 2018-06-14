## PAM CV indexer
Pam CV Indexer er en applikasjon som mottar CV'er fra Kafka og indekserer dem i elastic search.
DSet tilbyr også et REST-api for å søke i CV'ene som er indeksert i elastic search.

### Krav for utviklere
Du trenger Docker installert lokalt, i tillegg til et "vanlig" oppsett med Java og Maven. Se [Auras side om Linux utviklerimage på Confluence](https://confluence.adeo.no/display/AURA/Linux+utviklerimage)

Du må installere [docker-compose](https://docs.docker.com/compose/install/#install-compose)

Docker-compose krever endring av Linuxkonfigurasjonen. Kjør følgende kommando som root:
```
sysctl -w vm.max_map_count=262144
```

For at innstillingen skal være permanent må du opprette filen */etc/sysctl.d/01-increase_vm_max_map_count.conf*. Den skal inneholde linjen:

```
vm.max_map_count=262144
```

nå kan du kjøre *mvn clean install*.


### Hvordan kjøre applikasjonen lokalt
For å kjøre applikasjonen fra IntelliJ må du først starte docker-imaget som inneholder elastic search:

```
docker-compose -f src/test/resources/docker-compose-kun-es.yml up
```

Deretter starter du *no.nav.arbeid.cv.es.PamCvIndexerApplication* med VM Options:
```
-Dspring.profiles.active=dev -Des.hostname=localhost -Des.scheme=HTTP -Des.port=9250
```

**OIDC:**
For å nå rest grensesnitt som er sikret med OIDC kan man generere token for localhost ved å 
gå til http://localhost:8765/pam-cv-indexer/local/cookie

**Altinn:**
Bruker med fnr 12345678910(default fra cookie-url over) er satt opp med altinn-rolle lokalt (via wiremock).

For å simulere bruker uten rolle i altinn:
Simuler bruker med fnr 12121212121 ved å gå til http://localhost:8765/pam-cv-indexer/local/cookie?subject=12121212121
