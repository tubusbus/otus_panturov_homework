timeout(60) {
    node("maven") {
        env.MESSAGE = "UI tests"
        stage("Checkout") {
            checkout scm
        }
        stage("Run ui-tests") {
            int exitCode = sh(
                    returnStatus: true,
                    script: """
                    mvn clean test -Dbrowser=$BROWSER -Dbrowser.version=$BROWSER_VERSION -Dwebdriver.base.url=$BASE_URL -Dwebdriver.remote.url=$GRID_URL
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
        stage('Notify to telegram') {
            telegramEntity = "https://api.telegram.org/bot${TOKEN}/"

            wuthCredentials([userAndPassword(credentialId: "BOT_TOKEN", environment: "TOKEN")]) {

                String chatId = URLEncoder.encode("${myteam_chat_id}", "UTF-8")
                String reportMessage = URLEncoder.encode("${env.MESSAGE}", "UTF-8")

                StringBuilder stringBuilder = new StringBuilder("${telegramEntity}/sendMessage");
                stringBuilder.append("?chatId=${chatId}")
                stringBuilder.append("?text=${reportMessage}")

                URL urlConnection = null
                url = new URL(stringBuilder.toString()).openConnection() as HttpURLConnection
                urlConnection.setRequestMethod('GET')
                urlConnection.setDoOutput(true)
                def is = urlConnection.getInputStream() as InputStream
                echo is.getText('utf-8')
            }
        }
    }
}