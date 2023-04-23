timeout(60) {
    node("maven") {
        env.MESSAGE = "UI tests"
        stage("Checkout") {
            checkout scm
        }
    }
}