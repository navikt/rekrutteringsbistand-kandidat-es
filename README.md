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

For at innstillingen skal være permanent må du opprette filen 
*/etc/sysctl.d/01-increase_vm_max_map_count.conf*. Den skal inneholde linjen:

```
vm.max_map_count=262144
```

Nå kan du kjøre *mvn clean install*, som vil kjøre testene og bygge JAR.



# Henvendelser

## For Nav-ansatte
* Dette Git-repositoriet eies av [Team inkludering i Produktområde arbeidsgiver](https://navno.sharepoint.com/sites/intranett-prosjekter-og-utvikling/SitePages/Produktomr%C3%A5de-arbeidsgiver.aspx).
* Slack-kanaler:
  * [#inkludering-utvikling](https://nav-it.slack.com/archives/CQZU35J6A)
  * [#arbeidsgiver-utvikling](https://nav-it.slack.com/archives/CD4MES6BB)
  * [#arbeidsgiver-general](https://nav-it.slack.com/archives/CCM649PDH)

## For folk utenfor Nav
* Opprett gjerne en issue i Github for alle typer spørsmål
* IT-utviklerne i Github-teamet https://github.com/orgs/navikt/teams/arbeidsgiver
* IT-avdelingen i [Arbeids- og velferdsdirektoratet](https://www.nav.no/no/NAV+og+samfunn/Kontakt+NAV/Relatert+informasjon/arbeids-og-velferdsdirektoratet-kontorinformasjon)
