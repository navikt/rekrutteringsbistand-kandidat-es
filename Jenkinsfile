@Library('deploy')
import deploy

def deployLib = new deploy()

// Obtain ephemeral "installation" API token which can be used for GitHub repository access.
// A token is valid for one hour after creation.
githubAppId = '23179'
githubAppCredentialId = 'teampam-ci'
def newApiToken() {
    withEnv(['HTTPS_PROXY=webproxy-internett.nav.no:8088']) {
        withCredentials([file(credentialsId: githubAppCredentialId, variable: 'KEYFILE')]) {
            dir('token') {
                def generatedToken = sh(script: "generate-jwt.sh \$KEYFILE ${githubAppId} | xargs generate-installation-token.sh", returnStdout: true)
                return generatedToken.trim()
            }
        }
    }
}

node {
    def application = "pam-kandidatsok-es"

    def committer, committerEmail, changelog, releaseVersion // metadata

    def mvnHome = tool "maven-3.3.9"
    def mvn = "${mvnHome}/bin/mvn"
    def deployEnv = "q6"
    def namespace = "q6"
    def policies = "app-policies.xml"
    def notenforced = "not-enforced-urls.txt"
    def appConfig = "nais.yaml"
    def dockerRepo = "repo.adeo.no:5443"
    def zone = 'fss'
    def repo = "navikt"
    def githubAppToken = newApiToken();

    def color

    try {

        stage("checkout") {
            cleanWs()
            withEnv(['HTTPS_PROXY=http://webproxy-internett.nav.no:8088']) {
                // githubAppToken is not a magic secret variable, so mask it manually by disabling shell echo
                // Would be great if withCredentials could be used to mask the value, mark it as secret, or similar
                println("Repository URL is https://x-access-token:****@github.com/${repo}/${application}.git")
                sh(script: "set +x; git clone https://x-access-token:${githubAppToken}@github.com/${repo}/${application}.git .")
            }
        }

        stage("initialize") {
            commitHashShort = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            releaseVersion = "1.3.${env.BUILD_NUMBER}-${commitHashShort}"
            committer = sh(script: 'git log -1 --pretty=format:"%an (%ae)"', returnStdout: true).trim()
            committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
            changelog = sh(script: 'git log `git describe --tags --abbrev=0`..HEAD --oneline', returnStdout: true)
            currentBuild.displayName = releaseVersion
        }

        stage("verify maven versions") {
            sh 'echo "Verifying that no snapshot dependencies is being used."'
            sh 'grep module pom.xml | cut -d">" -f2 | cut -d"<" -f1 > snapshots.txt'
            sh 'echo "./" >> snapshots.txt'
            sh 'while read line;do if [ "$line" != "" ];then if [ `grep SNAPSHOT $line/pom.xml | wc -l` -gt 1 ];then echo "SNAPSHOT-dependencies found. See file $line/pom.xml.";exit 1;fi;fi;done < snapshots.txt'
        }

        stage("build and test backend") {
            sh "${mvn} clean install -Djava.io.tmpdir=/tmp/${application} -B -e -U"
            sh "docker build --build-arg JAR_FILE=${application}-${releaseVersion}.jar -t ${dockerRepo}/${application}:${releaseVersion} ."
        }

        stage("publish") {
            withCredentials([usernamePassword(credentialsId: 'nexusUploader', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                sh "docker login -u ${env.NEXUS_USERNAME} -p ${env.NEXUS_PASSWORD} ${dockerRepo} && docker push ${dockerRepo}/${application}:${releaseVersion}"
                sh "curl --user ${env.NEXUS_USERNAME}:${env.NEXUS_PASSWORD} --upload-file ${appConfig} https://repo.adeo.no/repository/raw/nais/${application}/${releaseVersion}/nais.yaml"
            }
        }

        stage("deploy to preprod, q6") {
            callback = "${env.BUILD_URL}input/Deploy/"

            def deploy = deployLib.deployNaisApp(application, releaseVersion, deployEnv, zone, namespace, callback, committer, false).key

            try {
                timeout(time: 15, unit: 'MINUTES') {
                    input id: 'deploy', message: "Check status here:  https://jira.adeo.no/browse/${deploy}"
                }
            } catch (Exception e) {
                throw new Exception("Deploy feilet :( \n Se https://jira.adeo.no/browse/" + deploy + " for detaljer", e)
            }
        }

        stage("tag") {
            withEnv(['HTTPS_PROXY=http://webproxy-internett.nav.no:8088']) {
                sh "git tag -a ${releaseVersion} -m ${releaseVersion}"
                sh ("git push -q origin --tags")
            }
        }

        color = '#BDFFC3'
        GString message = ":heart_eyes_cat: Siste commit på ${application} bygd og deploya OK.\nSiste commit ${changelog}"
        slackSend color: color, channel: '#pam_bygg', message: message, teamDomain: 'nav-it', tokenCredentialId: 'pam-slack'


    } catch (e) {
        color = '#FF0004'
        GString message = ":crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: Help sad cat! \n Siste commit på ${application} gikk ikkje gjennom. Sjå logg for meir info ${env.BUILD_URL}\nLast commit ${changelog}"
        slackSend color: color, channel: '#pam_bygg', message: message, teamDomain: 'nav-it', tokenCredentialId: 'pam-slack'
    }

}
