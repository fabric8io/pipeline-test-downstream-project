#!/usr/bin/groovy
@Library('github.com/rawlingsj/fabric8-pipeline-library@master')
def dummy
mavenTemplate {
  dockerNode {
    checkout scm
    readTrusted 'release.groovy'
    def pipeline = load 'release.groovy'

    sh "git remote set-url origin git@github.com:fabric8io/pipeline-test-project.git"

    def stagedProject
    stage ('Stage'){
      stagedProject = pipeline.stage()
    }
    
    stage ('Deploy'){
      pipeline.deploy(stagedProject)
    }

    stage ('Approve'){
      pipeline.approveRelease(stagedProject)
    }

    //stage 'Website'
    //pipeline.website(stagedProject)

    stage ('Promote'){
      pipeline.release(stagedProject)
    }
  }
}
