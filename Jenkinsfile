

pipeline {
    agent any
     parameters {
        booleanParam defaultValue: true, description: 'Please check in case you want to build Commons Module', name: 'Build_Commons'
        booleanParam defaultValue: true, description: 'Please check in case you want to build Public Web Module', name: 'Build_Publicweb'
        booleanParam defaultValue: false, description: 'Please check in case you want to build My Tetra Pak Module', name: 'Build_MyTetraPak'
	    booleanParam defaultValue: false, description: 'Please check in case you want to build Tetralaval Module', name: 'Build_Tetralaval'
	    booleanParam defaultValue: false, description: 'Please check in case you want to build Supplierportal Module', name: 'Build_Supplierportal'
        booleanParam defaultValue: true, description: 'Please uncheck in case you do not want to perform sonar analysis', name: 'Sonar_Analysis'
    }
    stages {
        stage('one') {
            steps {
               echo 'hi'
                echo 'f1'
            }
        }
    }
}
