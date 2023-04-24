timeout(60) {
    node("maven") {
        env.MESSAGE = "UI tests"
        stage("Checkout") {
            checkout scm
        }
        stage("Run ui-tests") {
            def browser = env.BROWSER
            def browserVersion = env.BROWSER_VERSION
            def baseUrl = env.BASE_URL
            def gridUrl = env.GRID_URL
            def token = env.TOKEN
            int exitCode = sh(
                    returnStatus: true,
                    script: """
                    mvn clean test -Dbrowser=${browser} -Dbrowser.version=${browserVersion} -Dwebdriver.base.url=${baseUrl} -Dwebdriver.remote.url=${gridUrl}
                    """
            )
            if(exitCode == 1) {
                currentBuild.result = 'UNSTABLE'
            }
        }
        stage('Publish artifacts') {
            archiveArtifacts artifacts: '**/target/**/*',
                    allowEmptyArchive: true,
                    fingerprint: true,
                    onlyIfSuccessful: true
            junit testResults: '**/target/**/*.xml', skipPublishingChecks: true
        }
        stage('Publish Junit Report') {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
            ])
        }
    }
}