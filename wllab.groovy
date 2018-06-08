job('wllab-component') {
  //dummy job to have an upstream job at wllab-deploy
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
    env('ARTIFACT_PATH', 'target/wllab.war')
    env('WEBLOGIC_APP_NAME', 'wllab')
    env('WEBLOGIC_URL', '1.2.3.4')
    env('WEBLOGIC_TARGET', 'AdminServer')
    groovy(readFileFromWorkspace("scripts/environmentVariablesDeploy.groovy"))
  }
  steps {
    maven {
      goals('clean package antrun:run@download-artifact antrun:run@deploy-to-weblogic')
      mavenInstallation('maven-3.5.3')
    }
  }
}
