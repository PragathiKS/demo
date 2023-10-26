

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
        stage('init-build-Number') {
            steps {
                script {
                    def now = new Date()
                    def formattedDate
                    formattedDate = now.format("yyyyMMddHHmm")
                    build_id_number = formattedDate
                    echo "build_id_number = ${build_id_number}-SNAPSHOT"
		        }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (params.Build_Commons) {
                        appname = "commons"
                        echo 'common'
                    }
                }

                script {
                    if (params.Build_MyTetraPak) {
                        appname = "customerhub"
                         echo 'cudtomerhub'
                    }
                }

                script {
                    if (params.Build_Publicweb) {
                        appname = "publicweb"
                         echo 'publicweb'
		    }
                    }
                  
                }
                script {
                        if (params.Build_Tetralaval) {
                            appname = "tetra-laval"
                        echo 'teralaval'
                        }
                }
		        script {
                    if (params.Build_Supplierportal) {
                        appname = "supplierportal"
                        echo 'supplier'
                    }
                }
               
            }
        }
}
}
