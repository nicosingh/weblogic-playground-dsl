job('wllab-deploy-test') {
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
  }
  steps {
    maven('clean package antrun:run@deploy-to-weblogic')
  }
}
