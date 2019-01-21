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
			    
                sh "mvn -f $workspace/${params.CHOICE}/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
            }
            
        }
		
		stage ('Sonar_JS') {
			steps {
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.sources='./' -Dsonar.exclusions=**/tests/** -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak-${params.CHOICE} -Dsonar.branch=JSBranch -Dbuildversion=${BUILD_NUMBER}"
                }
            }
			
		stage ('Sonar_CSS') {
			steps {
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.sources='./' -Dsonar.exclusions=**/tests/**,**/breadcrumb/** -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak-${params.CHOICE} -Dsonar.branch=CSSBranch -Dbuildversion=${BUILD_NUMBER}"
                }
            }
		stage ('Sonar_JAVA') {
			steps {
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.sources='./' -Dsonar.exclusions=**/tests/** -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak-${params.CHOICE} -Dsonar.branch=JAVABranch -Dbuildversion=${BUILD_NUMBER}"
                }
            }	
		
							
            }						
		}
		