pipeline {
    agent any
    parameters {
        booleanParam defaultValue: false, description: 'Please check in case you want to build Commons Module', name: 'Build_Commons'
        booleanParam defaultValue: true, description: 'Please check in case you want to build Customer Hub Module', name: 'Build_Customerhub'
        booleanParam defaultValue: false, description: 'Please check in case you want to build Public Web Module', name: 'Build_Publicweb'
        booleanParam defaultValue: false, description: 'Please uncheck in case you do not want to perform sonaranalysys', name: 'Sonar_Analysis'
        booleanParam defaultValue: false, description: 'Please uncheck in case you do not want to execute the pipeline with all Tools', name: 'Tools_Execution'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }
    environment {
        sonar_url = "https://sonarcloud.io"
        login_token = "2354fbb990d5494aad3c578f2c9dd65147d01e02"
        author_url = "http://13.69.79.81:4502"
        publish_url = "http://13.69.73.197:4503"
        package_name = "tetrapak-complete-package"
        test_url_cuhu = "https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/dashboard.html https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/financials.html"
        test_url_pw = "http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en/innovations.html http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en.html http://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en/solutions.html"
        test_url_pally_zap_cuhu = "https://tetrapak-dev64a.adobecqms.net/content/tetrapak/customerhubtools/global/en/dashboard.html"
        test_url_pally_zap_pw = "https://tetrapak-dev64a.adobecqms.net/content/tetrapak/public-web/global/en.html"
        karmapath_cuhu = "${env.WORKSPACE}/tetrapak-customerhub/ui.dev/src/coverage"
        karmapath_pw = "${env.WORKSPACE}/tetrapak-publicweb/ui.dev/src/coverage"
        build_id_number = ""
    }
    stages {
        stage('init-build-Number') {
            steps {
                script {//sh "wget https://tetrapak-dev64a.adobecqms.net"
                    def now = new Date()
                    def formattedDate
                    formattedDate = now.format("yyyyMMddHHmm")
                    build_id_number = formattedDate
                    echo "build_id_number = ${build_id_number}"
                    //  sh 'Devops/deldocker.sh '
                }
            }
        }
        stage('Build-SonarAnalysis') {
            agent {
                dockerfile {
                    args "-v ${env.HOME}/.m2:/root/.m2 --tmpfs /.npm -u root:root"
                    label 'linux&&docker'
                }
            }
            steps {
                script {
                    if (params.Build_Commons) {
                        echo "Build Commons"
                        sh "echo $HOME"
                        sh 'pwd'
                        dir('tetrapak-commons') {
                            sh "npm install --prefix ui.dev/src"
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'tetrapak-artifactory-publish-creds', usernameVariable: 'artifactuser', passwordVariable: 'artifactpassword']])
                                    {
                                        sh "mvn clean install -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  deploy -Pminify -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                    }
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for commons module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.branch=tetrapack-commons  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}"
                            }
                        }
                    }
                }
                script {
                    if (params.Build_Customerhub) {
                        echo "Build CustomerHub"
                        sh "echo $EXECUTOR_NUMBER"
                        sh 'ls /root/.m2'
                        dir('tetrapak-customerhub') {
                            sh "npm install --prefix ui.dev/src"
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'tetrapak-artifactory-publish-creds', usernameVariable: 'artifactuser', passwordVariable: 'artifactpassword']])
                                    {
                                        sh "mvn clean -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  deploy -Pminify -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                    }
                            sh 'cp -r ui.dev/src/coverage /root/customerhub'
                            sh 'ls /root/customerhub'
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for customerhub module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.projectKey=tetrapak-smartsales_cfe-tetrapak  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}"
                            }
                        }
                    }
                }
                script {
                    if (params.Build_Publicweb) {
                        echo "Build PublicWeb"
                        dir('tetrapak-publicweb') {
                            sh "npm install --prefix ui.dev/src"
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'tetrapak-artifactory-publish-creds', usernameVariable: 'artifactuser', passwordVariable: 'artifactpassword']])
                                    {
                                        sh "mvn clean -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent -Padobe-public -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  deploy -Pminify -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                    }
                            sh 'cp -r ui.dev/src/coverage /root/publicweb'
                            sh 'ls /root/publicweb'
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for Publicweb module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.branch=tetrapack-Publicweb  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}"
                            }
                        }
                    }
                }
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
                                             //	sh 'ls -la report/coverage'
                                             	// sh 'ls tetrapak-customerhub/releases'
                                             	// sh 'cp -r ${karmapath_cuhu} releases'
                                              	// sh 'chmod 755 -R reports' 
						sh 'ls -la report' 
                                                sh 'cp -r report/customerhub .'
                                                sh 'ls -la'  
                                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'customerhub/coverage', reportFiles: 'index.html', reportName: 'KarmaReport-CustomerHub', reportTitles: ''])
                                                // sh 'cp -r /app/build-area/releases/coverage/index.html /app/splunk-output/karmajson/customerhub'
														
														
                                                echo "Starting pa11y test Run on CustomerHub Urls"
                                                reportname = "Pa11y Report - CustomerHub"
                                                sh 'chmod 777 Devops/PallyReportCuhu.sh'
                                                sh 'Devops/PallyReportCuhu.sh'
												sh 'mkdir pally-customerHub'
                                                sh 'mv PallyReportCuhu.html pally-customerHub/PallyReportCuhu.html' 
                                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'pally-customerHub', reportFiles: 'PallyReportCuhu.html', reportName: 'PallyReportCuhu', reportTitles: ''])
		
                				echo "Starting Zap Test Run- CustomerHub"
                				sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 -e LANG=C.UTF-8 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true'  
						sleep 20
						echo "Starting ZAP test Run on CustomerHub Urls"
                				sh 'docker exec  zap zap-cli spider ${test_url_pally_zap_cuhu}'
                				sh 'docker exec  zap zap-cli report -f html -o "zap_CustomerHub.html"'
								sh 'mkdir zap-customerHub'
						sh 'docker cp zap:zap/zap_CustomerHub.html zap-customerHub'
						sh 'docker stop zap'
						sh 'docker rm zap' 
                				publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'zap-customerHub', reportFiles: 'zap_CustomerHub.html', reportName: 'ZAPReport-CustomerHub', reportTitles: ''])            
						// sh 'cp -r ./zap_CustomerHub.html /app/splunk-output/zap/customerhub'
	    }
														  
						if (params.Build_Publicweb) {
					   	echo "Publising karma Test Report- PublicWeb"
					     	sh 'echo "Karma Report"'
					      	//sh 'cp -r ${karmapath_pw} /app/build-area/releases'
					       	//sh 'cp -r /app/build-area/releases/coverage/index.html .'
                                                sh 'cp -r reports/publicweb .' 
					       	publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'publicweb/coverage', reportFiles: 'index.html', reportName: 'KarmaReport-Publicweb', reportTitles: ''])
						//sh 'cp -r /app/build-area/releases/coverage/index.html /app/splunk-output/karmajson/publicweb'
						sh 'ls -la'
                                                echo "Starting pa11y test Run on PublicWeb Urls"
						sh 'chmod 777 Devops/PallyReportPubWeb.sh'
						sh './Devops/PallyReportPubWeb.sh'
						sh 'mkdir pally-publicWeb'
                        sh 'mv PallyReportPublicWeb.html pally-publicWeb/PallyReportPublicWeb.html' 
						publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'pally-publicWeb', reportFiles: 'PallyReportPublicWeb.html', reportName: 'PallyReport-PublicWeb', reportTitles: ''])
		  
                 				echo "Starting Zap Test Run- PublicWeb"
                  	   	                sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30  -e LANG=C.UTF-8  --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw -i owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true'  
                  	   		//sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 -e LANG=C.UTF-8 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw -i owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true'  
						sleep 20						
						echo "Starting ZAP test Run on PublicWeb Urls"
						sh 'docker exec  zap zap-cli spider ${test_url_pally_zap_pw}'
					//	sh 'docker exec  zap zap-cli spider ${test_url_pally_zap_cuhu}'
						sh 'docker exec  zap zap-cli report -f html -o "zap_PublicWeb.html"'
						sh 'mkdir zap-publicWeb'
						sh 'docker cp zap:zap/zap_PublicWeb.html zap-publicWeb'
						sh 'docker stop zap'
						sh 'docker rm zap' 
						publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'zap-publicWeb', reportFiles: 'zap_PublicWeb.html', reportName: 'ZAPReport-PublicWeb', reportTitles: ''])   
					//	sh 'cp -r ./zap_PublicWeb.html /app/splunk-output/zap/customerhub'
									}
														  
														  
						}
					}
				}
				}

        stage('Sitespeed Execution on all platforms - CustomerHub') {
            steps {
                script {
                    if (!params.Tools_Execution) {
                        echo "Skipping Sitespeed Execution for Desktop"
                    } else {
                        if (params.Build_Customerhub) {
                            echo "Starting Sitespeed-Desktop Test Run for CustomerHub"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io ${test_url_cuhu} -b firefox --outputFolder sitespeed_desktop'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_desktop', reportFiles: 'index.html', reportName: 'SitespeedReport-CustomerHub-Desktop', reportTitles: ''])
                            //sh 'cp -r ./sitespeed_desktop/index.html /app/splunk-output/sitespeeddesktop'

                            echo "Starting Sitespeed-Mobile Test Run for CustomerHub"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --mobile ${test_url_cuhu} -b firefox --outputFolder sitespeed_mobile'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_mobile', reportFiles: 'index.html', reportName: 'SitespeedReport-CustomerHub-Mobile', reportTitles: ''])
                            //sh 'cp -r ./sitespeed_mobile/index.html /app/splunk-output/sitespeedmobile'

                            echo "Starting Sitespeed-IPAD Test Run for CustomerHub"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --browsertime.viewPort 400x400 --browsertime.userAgent "Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10" ${test_url_cuhu} -b firefox --outputFolder sitespeed_ipad'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_ipad', reportFiles: 'index.html', reportName: 'SitespeedReport-CustomerHub-IPad', reportTitles: ''])
                            //sh 'cp -r ./sitespeed_ipad/index.html /app/splunk-output/sitespeedipad'
                        }

                        if (params.Build_Publicweb) {
                            echo "Starting Sitespeed-Desktop Test Run for PublicWeb"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io ${test_url_pw} -b firefox --outputFolder sitespeed_desktop'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_desktop', reportFiles: 'index.html', reportName: 'SitespeedReport-PublicWeb-Desktop', reportTitles: ''])
                            //sh 'cp -r ./sitespeed_desktop/index.html /app/splunk-output/sitespeeddesktop'

                            echo "Starting Sitespeed-Mobile Test Run for PublicWeb"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --mobile ${test_url_pw} -b firefox --outputFolder sitespeed_mobile'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_mobile', reportFiles: 'index.html', reportName: 'SitespeedReport-PublicWeb-Mobile', reportTitles: ''])
                            //sh 'cp -r ./sitespeed_mobile/index.html /app/splunk-output/sitespeedmobile'

                            echo "Starting Sitespeed-IPAD Test Run for PublicWeb"
                            sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --rm -v "$(pwd)":/sitespeed.io sitespeedio/sitespeed.io --browsertime.viewPort 400x400 --browsertime.userAgent "Mozilla/5.0(iPad; U; CPU iPhone OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B314 Safari/531.21.10" ${test_url_pw} -b firefox --outputFolder sitespeed_ipad'
                            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'sitespeed_ipad', reportFiles: 'index.html', reportName: 'SitespeedReport-PublicWeb-IPad', reportTitles: ''])
                            // sh 'cp -r ./sitespeed_ipad/index.html /app/splunk-output/sitespeedipad'
                        }

                    }
                }
            }
        }

    }
/** post {success {emailext subject: "SUCCESS: Job '${env.JOB_NAME}'",
 body: '''${DEFAULT_CONTENT}''',
 to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'}failure {emailext subject: "FAILURE: Job '${env.JOB_NAME}'",
 body: '''${DEFAULT_CONTENT}''',
 to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'}//always {//  build 'Tetra_Dev_Clear_httpd_Cache'
 // 	build 'Tetra-Splunk-All-Tools'
 //  }} **/
}
