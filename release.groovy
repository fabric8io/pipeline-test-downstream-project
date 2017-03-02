#!/usr/bin/groovy
def imagesBuiltByPipline() {
  return ['pipeline-test-project']
}


def externalImages(){
  return ['pipeline-test-external-image']
}

def repo(){
 return 'fabric8io/pipeline-test-project'
}

def stage(){
  return stageProject{
    project = repo()
    useGitTagForNextVersion = true
    extraImagesToStage = externalImages()
  }
}


def deploy(project){
  deployProject{
    stagedProject = project
    resourceLocation = 'target/classes/kubernetes.json'
    environment = 'fabric8'
  }
  //echo 'unable to deploy on plain kuberentes see https://github.com/fabric8io/kubernetes-client/issues/437'
}

def approveRelease(project){
  def releaseVersion = project[1]
  approve{
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'pipeline-test-project'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = imagesBuiltByPipline()
    extraImagesToTag = externalImages()
  }
}

def mergePullRequest(prId){
  mergeAndWaitForPullRequest{
    project = repo()
    pullRequestId = prId
  }

}

def website(stagedProject) {
  Model m = readMavenPom file: 'pom.xml'
  def projectArtifactId = m.artifactId
  genWebsiteDocs {
    project = stagedProject
    artifactId = projectArtifactId
  }
}

return this
