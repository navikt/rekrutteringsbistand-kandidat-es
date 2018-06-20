@Library('deploy')
import deploy

def deployLib = new deploy()

node {
    def application = "pam-kandidatsok-es"

    def committer, committerEmail, changelog, pom, releaseVersion, prPomVersion, isSnapshot, isPullRequest, isMaster, isBranch, nextVersion // metadata

    def mvnHome = tool "maven-3.3.9"
    def mvn = "${mvnHome}/bin/mvn"
    def color

    try {

        stage("checkout") {
            checkout scm
        }

        stage("initialize") {
            println ("Initialize $BRANCH_NAME")
            if (BRANCH_NAME.contains("PR-")) {
                println ("Branch is pull request")
                isPullRequest = true
                isMaster = false
                isBranch = false
                prPomVersion = "$BRANCH_NAME".replaceAll("-", "_") + "-SNAPSHOT"
                sh "env"
                println ("Setter ny pom versjon $prPomVersion")
                sh "${mvn} versions:set -B -DnewVersion=${prPomVersion} -DgenerateBackupPoms=false"                         
            } else if (BRANCH_NAME.contains("master")) {
                isPullRequest = false
                isMaster = true
                isBranch = false
            } else {
                isPullRequest = false
                isMaster = false
                isBranch = true

                branchPomVersion = "$BRANCH_NAME".replaceAll("-", "_") + "-SNAPSHOT"
                sh "env"
                println ("Setter ny pom versjon $branchPomVersion")
                sh "${mvn} versions:set -B -DnewVersion=${branchPomVersion} -DgenerateBackupPoms=false"                         
            }
            pom = readMavenPom file: 'pom.xml'
            releaseVersion = isPullRequest ? "$BRANCH_NAME".replaceAll("-", "_") + "_$BUILD_NUMBER" : pom.version.tokenize("-")[0]
            isSnapshot = pom.version.contains("-SNAPSHOT")
            committer = sh(script: 'git log -1 --pretty=format:"%an (%ae)"', returnStdout: true).trim()
            committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
            changelog = sh(script: 'git log `git describe --tags --abbrev=0`..HEAD --oneline', returnStdout: true)
        }

        stage("verify maven versions") {
            sh 'echo "Verifying that no snapshot dependencies is being used."'
            sh 'grep module pom.xml | cut -d">" -f2 | cut -d"<" -f1 > snapshots.txt'
            sh 'echo "./" >> snapshots.txt'
            sh 'while read line;do if [ "$line" != "" ];then if [ `grep SNAPSHOT $line/pom.xml | wc -l` -gt 1 ];then echo "SNAPSHOT-dependencies found. See file $line/pom.xml.";exit 1;fi;fi;done < snapshots.txt'
        }


        stage("build and test") {
            if (isSnapshot) {
                sh "${mvn} clean install -Dit.skip=true -Djava.io.tmpdir=/tmp/${application} -B -e"
            } else {
                println("POM version is not a SNAPSHOT, it is ${pom.version}. Skipping build and testing of backend")
            }
        }

        stage("release version") {
            if (isMaster) {
                withEnv(['HTTPS_PROXY=http://webproxy-utvikler.nav.no:8088']) {
                    withCredentials([string(credentialsId: 'navikt-ci-oauthtoken', variable: 'token')]) {
                        sh "${mvn} versions:set -B -DnewVersion=${releaseVersion} -DgenerateBackupPoms=false"
                        sh "git commit -am \"set version to ${releaseVersion} (from Jenkins pipeline)\""
                        sh ("git push -u https://${token}:x-oauth-basic@github.com/navikt/${application}.git $BRANCH_NAME")
                        sh ("git tag -a ${releaseVersion} -m ${application}-${releaseVersion}")
                        sh ("git push -u https://${token}:x-oauth-basic@github.com/navikt/${application}.git --tags $BRANCH_NAME")
                    }
                }
            }
        }
        
        stage("new dev version") {
            if (isMaster) {
                withEnv(['HTTPS_PROXY=http://webproxy-utvikler.nav.no:8088']) {
                    withCredentials([string(credentialsId: 'navikt-ci-oauthtoken', variable: 'token')]) {
                        nextVersion = (releaseVersion.toInteger() + 1) + "-SNAPSHOT"
                        sh "${mvn} versions:set -B -DnewVersion=${nextVersion} -DgenerateBackupPoms=false"
                        sh "git commit -am \"updated to new dev-version ${nextVersion} after release by ${committer} (from Jenkins pipeline)\""
                        sh "git push -u https://${token}:x-oauth-basic@github.com/navikt/${application}.git $BRANCH_NAME"
                    }
                }
            }
        }

        color = '#BDFFC3'
        GString message = ":heart_eyes_cat: Siste commit på ${application} bygd og deploya OK.\nSiste commit ${changelog}"
        slackSend color: color, channel: '#pam_bygg', message: message, teamDomain: 'nav-it', tokenCredentialId: 'pam-slack'


    } catch (e) {
        color = '#FF0004'
        GString message = ":crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: :crying_cat_face: Halp sad cat! \n Siste commit på ${application} gikk ikkje gjennom. Sjå logg for meir info ${env.BUILD_URL}\nLast commit ${changelog}"
        slackSend color: color, channel: '#pam_bygg', message: message, teamDomain: 'nav-it', tokenCredentialId: 'pam-slack'
    }

}
