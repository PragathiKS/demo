pipeline {
    agent any
	
	parameters {
    choice choices: ['tetrapak-commons', 'tetrapak-customerhub', 'tetrapak-publicweb'], description: '', name: 'CHOICE'
                }


    tools {
        maven 'maven'
        jdk 'jdk8'
    }
	environment {
     sonar_url = "http://127.0.0.1:9000"
	 author_url = "http://10.202.13.228:4502"
	 publish_url = "http://10.202.13.229:4503"
	 package_name = "tetrapak-complete-package"
	 test_url = "https://diagnostics.roche.com"

   }
   
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
			    
			    sh "cd $workspace/${params.CHOICE}"
                sh "mvn -f $workspace/${params.CHOICE}/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
            }
            
        }
		
		stage ('Sonar_JS') {
			steps {
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.sources='./' -Dsonar.exclusions=target/package/jcr_root/spa/src/coverage/**,src/main/jcr_root/spa/node_modules/**,src/main/jcr_root/spa/src/coverage/**,src/main/jcr_root/spa/app/etc/designs/Tetrapack/clientlibs/js/**,target/package/jcr_root/spa/app/etc/designs/Tetrapack/clientlibs/js/** -Dsonar.host.url='${sonar_url}' -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak -Dsonar.branch=JSBranch -Dbuildversion=1.0.0-DEV01"
                }
            }
			
		
							
            }						
		}
		