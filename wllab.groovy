job('wllab-component-test') {
  triggers {
    scm '* * * * *'
  }
  scm {
    git {
      remote {
        github('nicosingh/wllab', 'https')
        branch('master')
      }
    }
  }
  wrappers {
    artifactoryMaven3Configurator {
      deployerDetails {
        artifactoryName('artifactory-local')
        artifactoryUrl('http://artifactory:8081/artifactory')
        deployReleaseRepository {
          keyFromText('')
          keyFromSelect('libs-release-local')
          dynamicMode(false)
        }
        deploySnapshotRepository {
          keyFromText('')
          keyFromSelect('libs-snapshot-local')
          dynamicMode(false)
        }
        resolveReleaseRepository {
          keyFromText('')
          keyFromSelect('')
          dynamicMode(false)
        }
        resolveSnapshotRepository {
          keyFromText('')
          keyFromSelect('')
          dynamicMode(false)
        }
        userPluginKey('')
        userPluginParams('')
      }
      deployerCredentialsConfig {
        credentialsId('artifactory-credentials')
      }
      deployArtifacts(true)
      includeEnvVars(false)
      deployBuildInfo(true)
      runChecks(false)
      includePublishArtifacts(false)
      discardOldBuilds(false)
      discardBuildArtifacts(true)
      asyncBuildRetention(false)
      enableIssueTrackerIntegration(false)
      filterExcludedArtifactsFromBuild(true)
      enableResolveArtifacts(false)
      disableLicenseAutoDiscovery(false)
      aggregateBuildIssues(false)
      recordAllDependencies(false)
      blackDuckRunChecks(false)
      blackDuckIncludePublishedArtifacts(false)
      autoCreateMissingComponentRequests(true)
      autoDiscardStaleComponentRequests(true)
      overrideBuildName(false)
    }
  }
  steps {
    maven {
      goals('clean install')
      mavenInstallation('maven-3.5.3')
    }
  }
}

job('wllab-deploy') {
  parameters {
    runParam('upstreamJob', 'wllab-component', '', 'SUCCESSFUL')
  }
  triggers {
    scm '* * * * *'
  }
  scm {
    git {
      remote {
        github('nicosingh/wllab', 'https')
        branch('master')
      }
    }
  }
  wrappers {
    credentialsBinding {
      usernamePassword('WEBLOGIC_USER', 'WEBLOGIC_PASS', 'weblogic-credential')
    }
  }
  environmentVariables {
    env('ARTIFACT_PATH', 'target/wllab-${ARTIFACT_VERSION}.war')
    env('WEBLOGIC_APP_NAME', 'wllab')
    env('WEBLOGIC_URL', '1.2.3.4')
    env('WEBLOGIC_TARGET', 'AdminServer')
    groovy(readFileFromWorkspace("scripts/environmentVariablesDeploy.groovy"))
  }
  steps {
    maven {
      goals('clean antrun:run@download-artifact antrun:run@deploy-to-weblogic')
      mavenInstallation('maven-3.5.3')
    }
  }
}
