pipeline {
	agent any 
                //{dockerfile {
		//	args  '-v "$HOME/.m2":/.m2 --tmpfs /.npm -u root:root'
		//	label 'linux&&docker'
		//}}
        parameters {
                booleanParam defaultValue: false, description: 'Please check in case you want to build Commons Module', name: 'Build_Commons'
				booleanParam defaultValue: false, description: 'Please check in case you want to build Customer Hub Module', name: 'Build_Customerhub'
				booleanParam defaultValue: false, description: 'Please check in case you want to build Public Web Module', name: 'Build_Publicweb'
				booleanParam defaultValue: true, description: 'Please uncheck in case you do not want to execute the pipeline with all Tools', name: 'Tools_Execution'
        }

        environment {
                sonar_url = "http://127.0.0.1:9000"
                author_url = "http://13.69.79.81:4502"
                publish_url = "http://13.69.73.197:4503"
                package_name = "tetrapak-complete-package"
		test_url_cuhu = "https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/dashboard.html https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/financials.html"
		test_url_pw = "http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en/innovations.html http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en.html http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en/solutions.html"
		test_url_pally_zap_cuhu = "https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/dashboard.html"
		test_url_pally_zap_pw = "http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en.html"
                karmapath_cuhu =  "${workspace}/tetrapak-customerhub/ui.dev/src/coverage"
		karmapath_pw =  "${workspace}/tetrapak-publicweb/ui.dev/src/coverage"
		build_id_number = ""
                M2_HOME= "${workspace}"
                
        }

        stages {

                stage ('Initialize'){ 
                        steps {
                                sh      '''
                                        echo "PATH = ${PATH}"
                                        echo "M2_HOME = ${M2_HOME}"
                                        '''
                               // sh "mkdir releases "
            
        }}
		stage('init-build-Number'){
			steps{
				script{//sh "wget https://tetrapak-dev64a.adobecqms.net"
                                       // sh "wget http://13.69.79.81:4502"
                                       // sh "wget http://13.69.73.197:4503"
                                       // sh "wget http://127.0.0.1:9000"
					def now = new Date()
					def formattedDate
                    formattedDate = now.format("yyyyMMddHHmm")
                    build_id_number = formattedDate
                    echo "build_id_number = ${build_id_number}"
                                  sh 'pwd'
                                  sh 'docker ps -a'
                                  sh 'docker ps'
                                  sh 'docker stop d608d2075635'
                                  sh 'docker rm d608d2075635'' 
				}
			}
		}

                stage ('Build-Commons') {
                      when {
                            // Only say hello if a "greeting" is requested
                           expression { params.Build_Commons == true }
                          }
                      agent {
                      dockerfile {
                      args  '-v "$M2_HOME/.m2":/root/.m2   --tmpfs /.npm -u root:root'
                     // args  '-v "$HOME/.m2":/.m2 -v  --tmpfs /.npm -u root:root'

                      label 'linux&&docker'
                }}

                        steps {
                             script {
                               //  if (params.Build_Commons) {
                                     echo "Build Commons"
                                     sh "echo $HOME"
                                     sh "echo $EXECUTOR_NUMBER"
                                     sh 'pwd'
                                     dir('tetrapak-commons') {
                                        sh "npm install --prefix ui.dev/src"
                                        sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                       // sh "cp $workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-${build_id_number}.zip /app/build-area/releases/DEVBUILD"
                                                                 }

                                                          // }
                                     }
                               }
                                         }




					stage ('Build-CustomerHub') {
                                  agent {
                      dockerfile {
                      args  '-v "$M2_HOME/.m2":/root/.m2 -v "$M2_HOME/reports":/root/reports --tmpfs /.npm -u root:root'
                      label 'linux&&docker'
                }}
                        steps {
							script{
							if (params.Build_Customerhub) {
								echo "Build CustomerHub"
                                                       sh "echo $EXECUTOR_NUMBER"
                                      dir('tetrapak-customerhub') {
                                                sh "npm install --prefix ui.dev/src"
                                                sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public install -Pminify -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                               // sh "cp $workspace/tetrapak-customerhub/complete/target/tetrapak-customerhub.complete-1.0.0-DEV${BUILD_NUMBER}.zip /app/build-area/releases/DEVBUILD"
					      // def workspace = pwd()
                                               sh 'ls'
                                               sh 'ls ui.dev/src/coverage'
                                               sh 'echo $workspace'		
                                               sh 'cp -r ui.dev/src/coverage /root/reports'	
                                                            }
							}
                                  }
                        }
                    }

					stage ('Build-PublicWeb') {
                        steps {
							script{
							if (params.Build_Publicweb) {
								echo "Build PublicWeb"
                                                         dir('tetrapak-publicweb'){ 
                                sh "npm install --prefix ui.dev/src"
                                sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public install -Pminify -Dbuildversion=1.0.0-${build_id_number} dependency:tree -Dverbose"
                               // sh "cp $workspace/tetrapak-publicweb/complete/target/tetrapak-publicweb.complete-1.0.0-${build_id_number}.zip /app/build-area/releases/DEVBUILD"
								}
							}}
                        }
                    }


				
                    stage ( 'Karma, Pa11y, Zap Tools Execution') {
                        steps {
                                script {
						if (!params.Tools_Execution) {
                                                        echo "Skipping Tools Execution"
							}
						else {
							if (params.Build_Customerhub) {
                                                        echo "Publising karma Test Report- CustomerHub"
                                                        sh 'echo "Karma Report"'
                                                        def reportname = "Karma Report - CustomerHub"
                                                        sh 'ls -la reports/coverage'
                                                       // sh 'ls tetrapak-customerhub/releases'
                                                       // sh 'cp -r ${karmapath_cuhu} releases'
                                                       // sh 'chmod 755 -R reports' 
							sh 'cp -r reports/coverage .'
                                                        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'coverage', reportFiles: 'index.html', reportName: 'KarmaReport-CustomerHub', reportTitles: ''])
                                                        // sh 'cp -r /app/build-area/releases/coverage/index.html /app/splunk-output/karmajson/customerhub'
														
														
                                                         echo "Starting pa11y test Run on CustomerHub Urls"
                                                         reportname = "Pa11y Report - CustomerHub"
                                                         sh 'chmod 777 Devops/PallyReporting.sh'
                                                         sh 'Devops/PallyReporting.sh'
                                                         sh 'cp Devops/PallyReport.html PallyReport_CustomerHub.html' 
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'PallyReport_CustomerHub.html', reportName: 'PallyReport-CustomerHub', reportTitles: ''])
							 echo "Starting Zap Test Run- CustomerHub"
                                                         sh 'docker rm zap'
                                                         sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true'  
								sleep 20
								echo "Starting ZAP test Run on CustomerHub Urls"
                                                         sh 'docker exec zap zap-cli spider ${test_url_pally_zap_cuhu}'
                                                         sh 'docker exec zap zap-cli report -f html -o "zap_CustomerHub.html"'
							 sh 'docker cp zap:zap/zap_CustomerHub.html .'
							 sh 'docker stop zap'
							 sh 'docker rm zap' 
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'zap_CustomerHub.html', reportName: 'zap Report', reportTitles: ''])            
														  sh 'cp -r ./zap_CustomerHub.html /app/splunk-output/zap/customerhub'
														  }
														  
														  if (params.Build_Publicweb) {
														echo "Publising karma Test Report- PublicWeb"
														sh 'echo "Karma Report"'
														sh 'cp -r ${karmapath_pw} /app/build-area/releases'
														sh 'cp -r /app/build-area/releases/coverage/index.html .'
														publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: '/app/build-area/releases/coverage', reportFiles: 'index.html', reportName: 'Karma Report', reportTitles: ''])
														sh 'cp -r /app/build-area/releases/coverage/index.html /app/splunk-output/karmajson/publicweb'
														
														
														 echo "Starting pa11y test Run on PublicWeb Urls"
														 sh 'chmod 777 Devops/PallyReportPubWeb.sh'
														 sh './Devops/PallyReportPubWeb.sh'
														 publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'PallyReporPublicWeb.html', reportName: 'Pally Report', reportTitles: ''])
														 echo "Starting Zap Test Run- PublicWeb"
														  sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw \
														 -i owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  \
														  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true \
														  -config api.disablekey=true'  
														 
														 echo "Starting ZAP test Run on PublicWeb Urls"
														  sh 'docker exec zap zap-cli spider ${test_url_pally_zap_pw}'
														  sh 'docker exec zap zap-cli report -f html -o "zap_PublicWeb.html"'
														  sh 'docker cp zap:zap/zap_PublicWeb.html .'
														  sh 'docker stop zap'
														  sh 'docker rm zap' 
														  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'zap_PublicWeb.html', reportName: 'zap Report', reportTitles: ''])            
														  sh 'cp -r ./zap_PublicWeb.html /app/splunk-output/zap/customerhub'
														  }
														  
														  
														  }
								}
						}
				}
														
														stage ( 'Sitespeed Execution on all platforms - CustomerHub') {
													       steps {
														     script {
														if (!params.Tools_Execution) {
                                                        echo "Skipping Sitespeed Execution for Desktop"
                                                        }
                                                        else {
														if (params.Build_Customerhub) {
                                                        echo "Starting Sitespeed-Desktop Test Run for CustomerHub"
                                                         sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io ${test_url_cuhu} -b firefox --outputFolder sitespeed_desktop'
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_desktop', reportFiles: 'index.html', reportName: 'sitespeed Report Desktop', reportTitles: ''])
                                                         sh 'cp -r ./sitespeed_desktop/index.html /app/splunk-output/sitespeeddesktop'
                                                         
														 echo "Starting Sitespeed-Mobile Test Run for CustomerHub"
                                                         sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --mobile ${test_url_cuhu} -b firefox --outputFolder sitespeed_mobile'
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_mobile', reportFiles: 'index.html', reportName: 'sitespeed Report Mobile', reportTitles: ''])
														 sh 'cp -r ./sitespeed_mobile/index.html /app/splunk-output/sitespeedmobile'
                                                         
														 echo "Starting Sitespeed-IPAD Test Run for CustomerHub"
                                                          sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --browsertime.viewPort 400x400 --browsertime.userAgent "Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10" ${test_url_cuhu} -b firefox --outputFolder sitespeed_ipad'
                                                          publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_ipad', reportFiles: 'index.html', reportName: 'sitespeed Report IPad', reportTitles: ''])
                                                          sh 'cp -r ./sitespeed_ipad/index.html /app/splunk-output/sitespeedipad'
														  }
														  
														  if (params.Build_Publicweb) {
                                                        echo "Starting Sitespeed-Desktop Test Run for PublicWeb"
                                                         sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io ${test_url_pw} -b firefox --outputFolder sitespeed_desktop'
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_desktop', reportFiles: 'index.html', reportName: 'sitespeed Report Desktop', reportTitles: ''])
                                                         sh 'cp -r ./sitespeed_desktop/index.html /app/splunk-output/sitespeeddesktop'
                                                       
													   echo "Starting Sitespeed-Mobile Test Run for PublicWeb"
                                                         sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --mobile ${test_url_pw} -b firefox --outputFolder sitespeed_mobile'
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_mobile', reportFiles: 'index.html', reportName: 'sitespeed Report Mobile', reportTitles: ''])
														 sh 'cp -r ./sitespeed_mobile/index.html /app/splunk-output/sitespeedmobile'
                                                        
														echo "Starting Sitespeed-IPAD Test Run for PublicWeb"
                                                          sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --browsertime.viewPort 400x400 --browsertime.userAgent "Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10" ${test_url_pw} -b firefox --outputFolder sitespeed_ipad'
                                                          publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_ipad', reportFiles: 'index.html', reportName: 'sitespeed Report IPad', reportTitles: ''])
                                                          sh 'cp -r ./sitespeed_ipad/index.html /app/splunk-output/sitespeedipad'
														  }
														  
														  
													  
														  }
														  }
														  }
														  }
														  }
post {
      success {
      emailext subject: "SUCCESS: Job '${env.JOB_NAME}'",
      body: '''${DEFAULT_CONTENT}''',
	  to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'
}
      failure {
      emailext subject: "FAILURE: Job '${env.JOB_NAME}'",
      body: '''${DEFAULT_CONTENT}''',
	  to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'
}
  always {
    build 'Tetra_Dev_Clear_httpd_Cache'
	build 'Tetra-Splunk-All-Tools'
    } 
	} 
}
