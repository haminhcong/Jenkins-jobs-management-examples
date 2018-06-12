// workaround to check out the Jenkinsfile of the tag because parameters are not replaced
// when using the "Pipeline script from SCM" option
node('linux-master') {
    stage('Check environment') {
        //sh 'ls -al'
        echo "workspace"
        echo "${env.WORKSPACE}"
        echo "${env.WORKSPACE}"
        echo "${env.WORKSPACE}"

        echo "gitlabBranch"
        if ("${env.gitlabActionType}" == "PUSH") {
            checkout changelog: true, poll: true, scm: [
                    $class                           : 'GitSCM', branches: [[name: "${env.gitlabAfter}"]],
                    doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],
                    userRemoteConfigs                : [
                            [credentialsId: '1a199e0b-8b89-45e4-b85a-d400bc95dd0c',
                             url          : 'ssh://git@192.168.122.10:10022/conghm1/JavaWS.git']
                    ]
            ]
            sh 'ls -al'
            sh 'cat Dockerfile'
        } else if ("${env.gitlabActionType}" == "MERGE") {

            checkout changelog: true, poll: true, scm: [
                    $class                           : 'GitSCM',
                    branches                         : [[name: "origin/${env.gitlabSourceBranch}"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions                       : [
                            [
                                    $class : 'PreBuildMerge',
                                    options: [
                                            fastForwardMode: 'FF',
                                            mergeRemote    : 'origin',
                                            mergeStrategy  : 'RESOLVE',
                                            mergeTarget    : "${env.gitlabTargetBranch}"
                                    ]
                            ],
                            [
                                    $class: 'UserIdentity', email: 'conghm1@viettel.com.vn', name: 'conghm1'
                            ]

                    ],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: 'jenkins_clone_credentials',
                                                         name         : 'origin',
                                                         url          : "http://192.168.122.10:10080/conghm1/JavaWS.git"]]
            ]


            sh 'ls -al'
            sh 'cat Dockerfile'
        }

    }
    //load 'Jenkinsfile-test'
}
