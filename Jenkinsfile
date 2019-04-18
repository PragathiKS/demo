pipeline {
	agent any
          triggers {
          cron('30 10,15 * * *')
                   }

	
	parameters {
		choice choices: ['tetrapak-customerhub', 'tetrapak-publicweb'], description: '', name: 'CHOICE'
		booleanParam defaultValue: true, description: 'Please uncheck in case you want to run the pipeline with all Tools execution', name: 'Skip_Tool_Execution'
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
		test_url = "http://tetrapak.sapient.com/content/tetrapak/customerhub/global/dashboard.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/installed-equipment.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/ordering/order-history.html#/?orderdate-from=2018-01-05&orderdate-to=2018-02-20 http://tetrapak.sapient.com/content/tetrapak/customerhub/global/financials.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/training.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/projects.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/contact.html http://tetrapak.sapient.com/content/tetrapak/customerhub/global/about-my-tetra-pak.html"
		test_url_pally_zap = "http://tetrapak.sapient.com"	 
		karmapath =  "${workspace}/${params.CHOICE}/ui.dev/src/coverage"
	}

	stages {

               stage ('Checkout') {
                 steps {
                   git(
                   poll: true,
                   url: 'https://del.tools.publicis.sapient.com/bitbucket/scm/tet/tetrapak.git',
                   credentialsId: '590d0184-e17a-46fe-be24-60c6b6ab4ca2',
                   branch: 'develop'
                      )
                   }
                 }

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
				sh "rm -rf $workspace/tetrapak-commons/ui.dev/src/node_modules/"
				sh "npm install --prefix $workspace/tetrapak-commons/ui.dev/src"
				sh "rm -rf $workspace/${params.CHOICE}/ui.dev/src/node_modules/"
				sh "npm install --prefix $workspace/${params.CHOICE}/ui.dev/src"
				sh "mvn -f $workspace/tetrapak-commons/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
				sh "mvn -f $workspace/${params.CHOICE}/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
			}  
		}

		stage ('Sonar_JS') {
			steps {
				script {
					if (params.Skip_Tool_Execution) {
						echo "Skipping execution of Sonar on JS files"
					}
					else
					{
						sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.exclusions=$workspace/**/ui.dev/src/source/scripts/utils/logger.js -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=${params.CHOICE} -Dsonar.branch=JS -Dbuildversion=${BUILD_NUMBER}"
						sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=js  -Dsonar.exclusions=$workspace/**/ui.dev/src/source/scripts/utils/logger.js -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=JS -Dbuildversion=${BUILD_NUMBER}"
					}
				}
			}
		}

		stage ('Sonar_CSS') {
			steps {
				script {
					if (params.Skip_Tool_Execution) {
						echo "Skipping execution of Sonar on CSS files"
					}
					else
					{
						sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=${params.CHOICE} -Dsonar.branch=CSS -Dbuildversion=${BUILD_NUMBER}"
						sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=CSS -Dbuildversion=${BUILD_NUMBER}"
					}
				}
			}
		}
		
		stage ('Sonar_JAVA') {
			steps {
				script {
					if (params.Skip_Tool_Execution) {
						echo "Skipping execution of Sonar on JAVA files"
					}
					else
					{
						sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.inclusions=**/src/main/java/com/tetrapak/customerhub/core/**/*,**/integration/**/*,**/it.launcher/**/*,**/ui.apps/**/*,**/models/**/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=${params.CHOICE} -Dsonar.branch=JAVA -Dbuildversion=${BUILD_NUMBER}"
						sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.inclusions=**/src/main/java/com/tetrapak/commons/core/**/*,**/integration/**/*,**/it.launcher/**/*,**/ui.apps/**/*,**/models/**/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=JAVA -Dbuildversion=${BUILD_NUMBER}"
					}
				}
			}
		}
		
		stage ('Deployment Author and Publish Parallel') {
			steps {
				parallel (
					"Author Deployment" : {
						echo "Uninstalling Old Commons Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-commons.complete'"
						echo "Deleting Old Commons Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-commons.complete'"
						echo "Uploading New Commons Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F name=${params.CHOICE}.complete -F file=@$workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-DEV${BUILD_NUMBER}.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
						echo "Installing New Commons Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-commons.complete'"
						sleep 20
						echo "Uninstalling Old Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=${params.CHOICE}.complete'"
						echo "Deleting Old Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=${params.CHOICE}.complete'"
						echo "Uploading New Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F name=${params.CHOICE}.complete -F file=@$workspace/${params.CHOICE}/complete/target/${params.CHOICE}.complete-1.0.0-DEV${BUILD_NUMBER}.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
						echo "Installing New Package on author"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=${params.CHOICE}.complete'"
					},
					"Publish_Deployment" : {
						echo "Uninstalling Old Commons Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-commons.complete'"
						echo "Deleting Old Commons Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-commons.complete'"
						echo "Uploading New Commons Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F name=${params.CHOICE}.complete -F file=@$workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-DEV${BUILD_NUMBER}.zip -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
						echo "Installing New Commons Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-commons.complete'"
						sleep 20
						echo "Uninstalling Old Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=${params.CHOICE}.complete'"						
						echo "Deleting Old Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=${params.CHOICE}.complete'"
						echo "Uploading New Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F name=${params.CHOICE}.complete -F file=@$workspace/${params.CHOICE}/complete/target/${params.CHOICE}.complete-1.0.0-DEV${BUILD_NUMBER}.zip -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
						echo "Installing New Package on publish"
						sh "curl -u admin:\\>Hd]HV7T -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=${params.CHOICE}.complete'"


					}
				)  
			}
		}
		
		stage ('Pa11y, Sitespeed, Zap Tools Execution') {
			steps {
				script{
					parallel (
						"karma" : {
							echo "Publising karma Test Report"
							sh 'echo "Karma Report"'
							sh 'cp -r ${karmapath} /app/build-area/releases'
							publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: '/app/build-area/releases/coverage', reportFiles: 'index.html', reportName: 'Karma Report', reportTitles: ''])				
						},				  
						"Pa11y" : {
							if (params.Skip_Tool_Execution) {
								echo "Skipping execution of Pally"
							}
							else
							{
								echo "Starting pa11y test Run on CustomerHub Urls"
                                                                sh 'chmod 777 PallyReporting.sh'
                                                                sh './PallyReporting.sh'
		                                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'PallyReport.html', reportName: 'Pally Report', reportTitles: ''])


							}
						},				  
						"sitespeed_Desktop" : {		
							if (params.Skip_Tool_Execution) {
								echo "Skipping execution of Sitespeed for Desktop"
							}
							else
							{
								echo "Starting Sitespeed Test Run"
								sh 'docker run --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io ${test_url} -b firefox --outputFolder sitespeed_desktop'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_desktop', reportFiles: 'index.html', reportName: 'sitespeed Report Desktop', reportTitles: ''])					
							}
						},														
						"sitespeed_Mobile" : {
							if (params.Skip_Tool_Execution) {
								echo "Skipping execution of Sitespeed for Mobile"
							}
							else
							{
								echo "Starting Sitespeed Test Run"
								sh 'docker run --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --mobile ${test_url} -b firefox --outputFolder sitespeed_mobile'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_mobile', reportFiles: 'index.html', reportName: 'sitespeed Report Mobile', reportTitles: ''])					
							}
						},							
						"sitespeed_IPad" : {
							if (params.Skip_Tool_Execution) {
								echo "Skipping execution of Sitespeed for iPad"
							}
							else
							{
								echo "Starting Sitespeed Test Run"
								sh 'docker run --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --browsertime.viewPort 400x400 --browsertime.userAgent "Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10" ${test_url} -b firefox --outputFolder sitespeed_ipad'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_ipad', reportFiles: 'index.html', reportName: 'sitespeed Report IPad', reportTitles: ''])					
							}
						},							
						"zap" :{
							if (params.Skip_Tool_Execution) {
								echo "Skipping execution of Zap"
							}
							else
							{
								echo "Starting Zap Test Run"
								sh 'zap-cli --zap-path /app/zap start -o "-config api.key=12345"'
								sh 'zap-cli --api-key 12345 spider ${test_url_pally_zap}'
								sh 'zap-cli --api-key 12345 report -f html -o "zap.html"'
								sh 'zap-cli --api-key 12345 shutdown'
								publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: '', reportFiles: 'zap.html', reportName: 'zap Report', reportTitles: ''])
							}
						}				                	
					)		
				}
			}
		}
	}
post {

      success {
      emailext subject: "SUCCESS: Job '${env.JOB_NAME}'",
      body: '''${DEFAULT_CONTENT}''',      
      to: 'amit.pasricha@publicissapient.com, anjali.gulati@publicissapient.com, ankur.gupta11@publicissapient.com, arivazhagan.tamilselvan@publicissapient.com, harsimran.kaur@publicissapient.com, jitendra.nakra@publicissapient.com, kanchan.mitharwal@publicissapient.com, lalit.mahori@publicissapient.com, manoj.varma@publicissapient.com, nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, ruhee.sharma@publicissapient.com, sachin.singh1@publicissapient.com, sumrin.kaur@publicissapient.com, sunil.kumar8@publicissapient.com, swati.lamba@publicissapient.com, tarun.sagar@publicissapient.com, tushar.tushar@publicissapient.com, vanessa.dsouza@publicissapient.com'
}
      failure {
      emailext subject: "FAILURE: Job '${env.JOB_NAME}'",
      body: '''${DEFAULT_CONTENT}''',
      to: 'amit.pasricha@publicissapient.com, anjali.gulati@publicissapient.com, ankur.gupta11@publicissapient.com, arivazhagan.tamilselvan@publicissapient.com, harsimran.kaur@publicissapient.com, jitendra.nakra@publicissapient.com, kanchan.mitharwal@publicissapient.com, lalit.mahori@publicissapient.com, manoj.varma@publicissapient.com, nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, ruhee.sharma@publicissapient.com, sachin.singh1@publicissapient.com, sumrin.kaur@publicissapient.com, sunil.kumar8@publicissapient.com, swati.lamba@publicissapient.com, tarun.sagar@publicissapient.com, tushar.tushar@publicissapient.com, vanessa.dsouza@publicissapient.com'

      }

 always {
    build 'Tetra-SapDev-Dispatcher-Flush'   
    build 'Tetra-Cucumber-Tests'
    }

}

}
