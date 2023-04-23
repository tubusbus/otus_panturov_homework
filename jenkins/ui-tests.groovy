timeout(60) {
    node("maven") {
        env.MESSAGE = "UI tests"
        stage("Checkout") {
            checkout scm
        }
//        stage('Checkout scripts') {
//            git credentialsId: jenkins, branches: ["${DEV_REPO_BRANCH}"]
//        }
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

//                String token = URLEncoder.encode("${myteam_token}", "UTF-8")
                String chatId = URLEncoder.encode("${myteam_chat_id}", "UTF-8")
                String reportMessage = URLEncoder.encode("${env.MESSAGE}", "UTF-8")

                StringBuilder stringBuilder = new StringBuilder("${telegramEntity}/sendMessage");
//                stringBuilder.append("?token=${token}")
                stringBuilder.append("?chatId=${chatId}")
                stringBuilder.append("?text=${reportMessage}")
//                stringBuilder.append("&parseMode=MarkdownV2")

                URL urlConnection = null
//                if("${HTTP_PROXY}" != "") {
//                    URL proxyUrl = new URL("${HTTP_PROXY}")
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAdress(proxyUrl.getHost(), proxyUrl.getPort()))
//                    urlConnection = new URL(stringBuilder.toString()).openConnection(proxy) as HttpURLConnection
//                } else {
                    url = new URL(stringBuilder.toString()).openConnection() as HttpURLConnection
//                }
                urlConnection.setRequestMethod('GET')
                urlConnection.setDoOutput(true)
                def is = urlConnection.getInputStream() as InputStream
                echo is.getText('utf-8')
            }
        }
    }
}