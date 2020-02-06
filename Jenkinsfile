//#!/usr/bin/env groovy
//@Library('smartsales-jenkins-library')
pipeline {
    agent any
    parameters {
        booleanParam defaultValue: true, description: 'Please check in case you want to build Commons Module', name: 'Build_Commons'
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
        author_url = "https://author-tetrapak-dev64a.adobecqms.net"
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
                    echo "build_id_number = ${build_id_number}-SNAPSHOT"
                    //  sh 'Devops/deldocker.sh '
                    sh "dig +short myip.opendns.com @resolver1.opendns.com"
                   // sh 'ls -R ${env.HOME}/.m2'
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
                        sh "rm -r /root/.m2/tetrapak*"
                        dir('tetrapak-commons') {
                            sh "npm install --prefix ui.dev/src"
                            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'tetrapak-artifactory-publish-creds', usernameVariable: 'artifactuser', passwordVariable: 'artifactpassword']])
                                    {
                                        sh "curl -u ${artifactuser}:${artifactpassword} -XDELETE https://tetrapak.jfrog.io/tetrapak/libs-snapshot-local/tetrapak"
                                        sh "curl -u ${artifactuser}:${artifactpassword} -XDELETE https://tetrapak.jfrog.io/tetrapak/libs-snapshot-local/tetrapak-publicweb"
                                        sh "curl -u ${artifactuser}:${artifactpassword} -XDELETE https://tetrapak.jfrog.io/tetrapak/libs-release-local/tetrapak-customerhub"
                                        sh "curl -u ${artifactuser}:${artifactpassword} -XDELETE https://tetrapak.jfrog.io/tetrapak/libs-release-local/terrapak"
                                        sh "curl -u ${artifactuser}:${artifactpassword} -XDELETE https://tetrapak.jfrog.io/tetrapak/libs-release-local/tetrapak-publicweb"
                                        sh "mvn clean install -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent  -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  install deploy -Pminify -Dbuildversion=1.0.0-DEV${build_id_number}-SNAPSHOT"
                                    }
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for commons module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.branch=tetrapack-commons  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}-SNAPSHOT"
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
                                        sh "mvn clean -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent  -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  install deploy -Pminify -Dbuildversion=1.0.0-DEV${build_id_number}-SNAPSHOT"
                                    }
                            sh 'cp -r ui.dev/src/coverage /root/customerhub'
                            sh 'ls /root/customerhub'
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for customerhub module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.branch=tetrapack-customerhub  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}-SNAPSHOT"
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
                                        sh "mvn clean -s settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent  -Dartuser=${artifactuser} -Dartpassword=${artifactpassword}  install deploy -Pminify -Dbuildversion=1.0.0-DEV${build_id_number}-SNAPSHOT"
                                    }
                            sh 'cp -r ui.dev/src/coverage /root/publicweb'
                            sh 'ls /root/publicweb'
                            if (!params.Sonar_Analysis) {
                                echo "Skipping Sonar execution for Publicweb module"
                            } else {
                                sh "mvn -e -B sonar:sonar -Dsonar.organization=tetrapak-smartsales   -Dsonar.host.url=${sonar_url} -Dsonar.buildbreaker.skip=true -Dsonar.login=${login_token} -Dsonar.branch=tetrapack-Publicweb  -Dsonar.languages=java,js,css -Dbuildversion=${build_id_number}-SNAPSHOT"
                            }
                        }
                    }
                }
            }
        }
		
		stage ('Author Deployment and Replication to Publish')
				{
                        steps {
									script {
											if (params.Build_Commons) 
												{
                                                echo "Uninstalling Old Commons Package on author for Commons"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-commons.complete'"
                                                sleep 20
                                                echo "Deleting Old Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-commons.complete'"
                                                sleep 10
                                                echo "Uploading New Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-commons.complete'"
                                                sleep 20
												
                                                echo "Deactivating Old Commons Package on publish for commons"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak -F cmd=deactivate '${author_url}/bin/replicate.json'"
                                                sleep 20
                                                echo "Activating new Commons Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak/tetrapak-commons.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F cmd=activate '${author_url}/bin/replicate.json'"
                                                sleep 10
												 
												}
												
												if (params.Build_Customerhub) 
												{
                                                echo "Uninstalling Old Package on author for customerhub"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-customerhub.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-customerhub.complete'"
                                                sleep 10
                                                echo "Uploading New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-customerhub/complete/target/tetrapak-customerhub.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-customerhub.complete'"
												sleep 20
												
												echo "Deactivating Old Customerhub Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak-customerhub -F cmd=deactivate '${author_url}/bin/replicate.json'"
                                                sleep 20
                                                echo "Activating new Customerhub Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak-customerhub/tetrapak-customerhub.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F cmd=activate '${author_url}/bin/replicate.json'"
                                                sleep 10
												}

												if (params.Build_Publicweb) 
												{
                                                echo "Uninstalling Old Package on author -- PublicWeb"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-publicweb.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-publicweb.complete'"
                                                sleep 10
                                                echo "Uploading New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-publicweb.complete -F file=@$workspace/tetrapak-publicweb/complete/target/tetrapak-publicweb.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-publicweb.complete'"
												sleep 20
												
												echo "Deactivating Old PublicWeb Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak-publicweb -F cmd=deactivate '${author_url}/bin/replicate.json'"
                                                sleep 20
                                                echo "Activating new PublicWeb Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F path=/etc/packages/tetrapak-publicweb/tetrapak-publicweb.complete-1.0.0-${build_id_number}-SNAPSHOT.zip -F cmd=activate '${author_url}/bin/replicate.json'"
                                                sleep 10
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
												sh 'mkdir -p pally-customerHub'
                                                sh 'mv PallyReportCuhu.html pally-customerHub/PallyReportCuhu.html' 
                                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'pally-customerHub', reportFiles: 'PallyReportCuhu.html', reportName: 'PallyReportCuhu', reportTitles: ''])
		
                				echo "Starting Zap Test Run- CustomerHub"
                				sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 -e LANG=C.UTF-8 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true -config api.disablekey=true'  
						sleep 20
						echo "Starting ZAP test Run on CustomerHub Urls"
                				sh 'docker exec  zap zap-cli spider ${test_url_pally_zap_cuhu}'
                				sh 'docker exec  zap zap-cli report -f html -o "zap_CustomerHub.html"'
								sh 'mkdir -p zap-customerHub'
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
						sh 'mkdir -p pally-publicWeb'
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
						sh 'mkdir -p zap-publicWeb'
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
	//sendNotifications(currentBuild.currentResult)
    }
/** post {success {emailext subject: "SUCCESS: Job '${env.JOB_NAME}'",
 body: '''${DEFAULT_CONTENT}''',
 to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'}failure {emailext subject: "FAILURE: Job '${env.JOB_NAME}'",
 body: '''${DEFAULT_CONTENT}''',
 to: 'nitin.kumar1@publicissapient.com, rajeev.duggal@publicissapient.com, sachin.singh1@publicissapient.com'}//always {//  build 'Tetra_Dev_Clear_httpd_Cache'
 // 	build 'Tetra-Splunk-All-Tools'
 //  }} **/
}
