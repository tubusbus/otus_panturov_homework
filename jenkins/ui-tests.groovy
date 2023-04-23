timeout(60) {
    node("maven-slave") {
        env.MESSAGE = "UI tests"
        stage("Checkout") {
            checkout scm
        }
    }
}