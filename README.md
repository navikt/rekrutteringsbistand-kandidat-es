# Rekrutteringsbistand kandidat Elasticsearch

rekrutteringsbistand-kandidat-es er et bibliotek som håndterer indeksering og søk mot Elasticsearch for bruk i 
rekrutteringsbistand-kandidat-api og rekrutteringsbistand-kandidat-indekser. Biblioteket innholder blant annet abstraksjoner for spørringer fra veileders kandidatsøk, 
ElasticSearch CV mapping (schema) og oppsett av ElasticSearch klient.


# For utviklere

Du trenger Docker installert lokalt, i tillegg til et "vanlig" oppsett med Java og Maven. Se 
[Auras side om Linux utviklerimage på 
Confluence](https://confluence.adeo.no/display/AURA/Linux+utviklerimage)

Du må installere [docker-compose](https://docs.docker.com/compose/install/#install-compose)

Docker-compose krever endring av Linuxkonfigurasjonen. Kjør følgende kommando som root:
```
sysctl -w vm.max_map_count=262144
``` 

For colima på mac, bruk i steden 
```
colima start --memory 6 
```

For at innstillingen skal være permanent må du opprette filen 
*/etc/sysctl.d/01-increase_vm_max_map_count.conf*. Den skal inneholde linjen:

```
vm.max_map_count=262144
```

Nå kan du kjøre *mvn clean install*, som vil kjøre testene og bygge JAR.


# Henvendelser

## For Nav-ansatte

* Dette Git-repositoriet eies av [Team tiltak og inkludering (TOI) i Produktområde arbeidsgiver](https://teamkatalog.nais.adeo.no/team/0150fd7c-df30-43ee-944e-b152d74c64d6).
* Slack-kanaler:
    * [#arbeidsgiver-toi-dev](https://nav-it.slack.com/archives/C02HTU8DBSR)
    * [#arbeidsgiver-utvikling](https://nav-it.slack.com/archives/CD4MES6BB)

## For folk utenfor Nav

* Opprett gjerne en issue i Github for alle typer spørsmål
* IT-utviklerne i Github-teamet https://github.com/orgs/navikt/teams/toi
* IT-avdelingen i [Arbeids- og velferdsdirektoratet](https://www.nav.no/no/NAV+og+samfunn/Kontakt+NAV/Relatert+informasjon/arbeids-og-velferdsdirektoratet-kontorinformasjon)
