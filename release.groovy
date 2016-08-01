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

def updateDependencies(source){

  def properties = []
  properties << ['<pipeline.test.project.dependency.version>','io/fabric8/pipeline-test-project-dependency']
  properties << ['<docker.maven.plugin.version>','io/fabric8/docker-maven-plugin']

  updatePropertyVersion{
    updates = properties
    repository = source
    project = repo()
  }
}

def pushDependencyUpdates(newVersion){
  def parentPomProjects = ['fabric8-quickstarts/funktion-nodejs-example','fabric8-quickstarts/funktion-kotlin-example','fabric8-quickstarts/funktion-java-example','fabric8-quickstarts/funktion-groovy-example']
  pushParentPomVersionChangePR{
    projects = parentPomProjects
    version = newVersion
  }
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
return this;
