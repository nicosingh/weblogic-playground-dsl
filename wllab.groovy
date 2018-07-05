job('wllab-component') {
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
      details {
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
      resolverDetails {
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
        username('')
        password('')
        credentialsId('artifactory-credentials')
        overridingCredentials(false)
      }
      resolverCredentialsConfig {
        username('')
        password('')
        credentialsId('')
        overridingCredentials(false)
      }
      deployArtifacts(true)
      artifactDeploymentPatterns {
        includePatterns('')
        excludePatterns('')
      }
      includeEnvVars(false)
      deployBuildInfo(true)
      runChecks(false)
      violationRecipients('')
      includePublishArtifacts(false)
      scopes('')
      discardOldBuilds(false)
      discardBuildArtifacts(true)
      asyncBuildRetention(false)
      deploymentProperties('')
      enableIssueTrackerIntegration(false)
      filterExcludedArtifactsFromBuild(true)
      enableResolveArtifacts(false)
      envVarsPatterns {
        includePatterns('')
        excludePatterns('*password*,*secret*,*key*')
      }
      disableLicenseAutoDiscovery(false)
      aggregateBuildIssues(false)
      aggregationBuildStatus('')
      recordAllDependencies(false)
      blackDuckRunChecks(false)
      blackDuckAppName('')
      blackDuckAppVersion('')
      blackDuckReportRecipients('')
      blackDuckScopes('')
      blackDuckIncludePublishedArtifacts(false)
      autoCreateMissingComponentRequests(true)
      autoDiscardStaleComponentRequests(true)
      customBuildName('')
      overrideBuildName(false)
      matrixParams('')
      artifactoryCombinationFilter('')
    }
  }
  steps {
    maven3Builder {
      mavenName('maven-3.5.3')
      rootPom('')
      goals('clean install')
      mavenOpts('')
    }
  }
}

job('wllab-deploy') {
  parameters {
    runParam('upstreamJob', 'wllab-component', '', 'SUCCESSFUL')
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

job('wllab-deploy-custom') {
  parameters {
    runParam('upstreamJob', 'wllab-component', '', 'SUCCESSFUL')
    choiceParam('WEBLOGIC_URL', ['1.2.3.4', '5.6.7.8', '9.0.1.2'], '')
    credentials {
      name('WEBLOGIC_CREDENTIALS')
      description('')
      defaultValue('')
      credentialType('com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl')
      required(false)
    }
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
      usernamePassword('WEBLOGIC_USER', 'WEBLOGIC_PASS', '${WEBLOGIC_CREDENTIALS}')
    }
  }
  environmentVariables {
    env('ARTIFACT_PATH', 'target/wllab-${ARTIFACT_VERSION}.war')
    env('WEBLOGIC_APP_NAME', 'wllab')
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
