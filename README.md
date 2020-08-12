## PAM Kandidatsøk ES

PAM Kandidatsøk ES er et bibliotek som håndterer indeksering og søk mot Elasticsearch for bruk i 
kandidatsøk og cv-indekser. Innholder blant annet abstraksjoner for spørringer fra arbeidsgivers og veileders kandidatsøk, 
ElasticSearch CV mapping (schema) og oppsett av ElasticSearch klient.

### Krav for utviklere

Du trenger Docker installert lokalt, i tillegg til et "vanlig" oppsett med Java og Maven. Se 
[Auras side om Linux utviklerimage på 
Confluence](https://confluence.adeo.no/display/AURA/Linux+utviklerimage)

Du må installere [docker-compose](https://docs.docker.com/compose/install/#install-compose)

Docker-compose krever endring av Linuxkonfigurasjonen. Kjør følgende kommando som root:
```
sysctl -w vm.max_map_count=262144
```

For at innstillingen skal være permanent må du opprette filen 
*/etc/sysctl.d/01-increase_vm_max_map_count.conf*. Den skal inneholde linjen:

```
vm.max_map_count=262144
```

Nå kan du kjøre *mvn clean install*, som vil kjøre testene og bygge JAR.
