import java.util.List
import java.util.Map
import hudson.model.ParametersAction

def out = [:]
def parameters = currentBuild.getAction(ParametersAction).getParameters()
def upstreamJobName = parameters.find { it.name == 'environment' } != null ? parameters.find { it.name == 'environment' }.value : 'upstreamJob'

//get artifact version from upstream job
def upstreamJob = parameters.find { it.name == upstreamJobName }
out['ARTIFACT_VERSION'] = upstreamJob.getRun().getEnvVars().get('BUILD_NUMBER')
out['ARTIFACT_URL'] = "http://artifactory:8081/artifactory/libs-release-local/guga/wllab/" + out['ARTIFACT_VERSION'] + "/wllab-" + out['ARTIFACT_VERSION'] + ".war"

//return result
return out
