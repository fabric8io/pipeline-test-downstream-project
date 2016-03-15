#!/usr/bin/groovy
def updateDependencies(source){
  def properties = []
  properties << ['<pipeline.test.project.version>','io/fabric8/pipeline-test-project']
  properties << ['<docker.maven.plugin.version>','io/fabric8/docker-maven-plugin']

  updatePropertyVersion{
    updates = properties
    repository = source
    project = 'fabric8io/pipeline-test-downstream-project'
  }
}

def stage(thisProject){
  return stageProject{
    project = 'fabric8io/pipeline-test-downstream-project'
    useGitTagForNextVersion = true
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'pipeline-test-downstream-project'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = ['pipeline-test-downstream-project']
    extraImagesToTag = null
  }
}

return this;
