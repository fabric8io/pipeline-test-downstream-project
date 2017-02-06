#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def dummy
mavenNode {
  dockerNode {
    checkout scm
    evaluate(readTrusted 'release.groovy')
    def pipeline = load 'release.groovy'
    
    sh "git remote set-url origin git@github.com:fabric8io/pipeline-test-project.git"

    stage 'Stage'
    def stagedProject = pipeline.stage()

    stage 'Deploy'
    pipeline.deploy(stagedProject)

    stage 'Approve'
    pipeline.approveRelease(stagedProject)

    stage 'Website'
    pipeline.website(stagedProject)

    stage 'Promote'
    pipeline.release(stagedProject)
  }
}
