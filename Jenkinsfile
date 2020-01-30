pipeline {
	agent {
		dockerfile {
			args  '-v "$HOME/.m2":/.m2 --tmpfs /.npm -u root:root'
			label 'linux&&docker'
		}
	}
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
        }

        stages {

                stage ('Initialize') {
                        steps {
                                sh      '''
                                        echo "PATH = ${PATH}"
                                        echo "M2_HOME = ${M2_HOME}"
                                        '''
            }
        }
		stage('init-build-Number'){
			steps{
				script{
					def now = new Date()
					def formattedDate
                    formattedDate = now.format("yyyyMMddHHmm")
                    build_id_number = formattedDate
                    echo "build_id_number = ${build_id_number}"
				}
			}
		}


                stage ('Build-Commons') {
                        steps {
                             script {
                                 if (params.Build_Commons) {
                                     echo "Build Commons"
                                     dir('tetrapak-customerhub') {
                                        sh "npm install --prefix ui.dev/src"
                                        sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dbuildversion=1.0.0-DEV${BUILD_NUMBER}"
                                        sh "cp $workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-${build_id_number}.zip /app/build-area/releases/DEVBUILD"
                                                                 }

                                                           }
                                     }
                               }
                                         }




					stage ('Build-CustomerHub') {
                        steps {
							script{
							if (params.Build_Customerhub) {
								echo "Build CustomerHub"
                                sh "npm install --prefix $workspace/tetrapak-customerhub/ui.dev/src"
                                sh "mvn -f $workspace/tetrapak-customerhub/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Pminify -Dbuildversion=1.0.0-${build_id_number} dependency:tree -Dverbose"
                                sh "cp $workspace/tetrapak-customerhub/complete/target/tetrapak-customerhub.complete-1.0.0-${build_id_number}.zip /app/build-area/releases/DEVBUILD"
								}
							}
                        }
                    }

					stage ('Build-PublicWeb') {
                        steps {
							script{
							if (params.Build_Publicweb) {
								echo "Build PublicWeb"
                                sh "npm install --prefix $workspace/tetrapak-publicweb/ui.dev/src"
                                sh "mvn -f $workspace/tetrapak-publicweb/pom.xml clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Pminify -Dbuildversion=1.0.0-${build_id_number} dependency:tree -Dverbose"
                                sh "cp $workspace/tetrapak-publicweb/complete/target/tetrapak-publicweb.complete-1.0.0-${build_id_number}.zip /app/build-area/releases/DEVBUILD"
								}
							}
                        }
                    }


                    stage ('Sonar_JS') {
                        steps {
                                script {
                                        if (!params.Tools_Execution) {
                                                echo "Skipping Sonar execution for JS profile"
                                }
                                        else {

					                            if (params.Build_Commons)
												{
												 echo "Running Sonar for JS profile on Project - Commons"
                                                 sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.exclusions=$workspace/**/ui.dev/src/source/scripts/utils/logger.js -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=JS -Dbuildversion=${build_id_number}"
												}
												  if (params.Build_Customerhub)
												{
												 echo "Running Sonar for JS profile on Project - Customerhub"
                                                 sh "mvn -f $workspace/tetrapak-customerhub/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.exclusions=$workspace/**/ui.dev/src/source/scripts/utils/logger.js -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-customerhub -Dsonar.branch=JS -Dbuildversion=${build_id_number}"
												}
												  if (params.Build_Publicweb)
												{
												 echo "Running Sonar for JS profile on Project - PublicWeb"
                                                 sh "mvn -f $workspace/tetrapak-publicweb/pom.xml -e -B sonar:sonar  -Dsonar.language=js -Dsonar.exclusions=$workspace/**/ui.dev/src/source/scripts/utils/logger.js -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-publicweb -Dsonar.branch=JS -Dbuildversion=${build_id_number}"
												}
                                        }
                                }
                        }
                    }

				stage ('Sonar_CSS') {
                        steps {
                                script {
                                        if (!params.Tools_Execution) {
                                                echo "Skipping Sonar execution for CSS profile"
                                        }
                                        else{
											   if (params.Build_Commons)
												{
												echo "Running Sonar for CSS profile on Project - Commons"
                                                sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=css  -Dsonar.exclusions=$workspace/**/ui.dev/src/app/jcr_root/apps/settings/wcm/designs/commons/clientlibs/vendor.publish/css/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=CSS -Dbuildversion=${build_id_number}"
												}
												if (params.Build_Customerhub)
												{
												echo "Running Sonar for CSS profile on Project - Customerhub"
                                                sh "mvn -f $workspace/tetrapak-customerhub/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.exclusions=$workspace/**/ui.dev/src/app/jcr_root/apps/settings/wcm/designs/customerhub/clientlibs/vendor.publish/css/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-customerhub -Dsonar.branch=CSS -Dbuildversion=${build_id_number}"
												}
												if (params.Build_Publicweb)
												{
												echo "Running Sonar for CSS profile on Project - PublicWeb"
                                                sh "mvn -f $workspace/tetrapak-publicweb/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.exclusions=$workspace/**/ui.dev/src/app/jcr_root/apps/settings/wcm/designs/publicweb/clientlibs/vendor.publish/css/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-publicweb -Dsonar.branch=CSS -Dbuildversion=${build_id_number}"
												}
                                        }

                                }
                        }
                }

                stage ('Sonar_JAVA') {
                        steps {
                                script {
                                        if (!params.Tools_Execution) {
                                                echo "Skipping Sonar execution for JAVA profile"
                                        }
                                        else
                                        {
                                                if (params.Build_Commons)
												{
												echo "Running Sonar for JAVA profile on Project - Commons"
                                                sh "mvn -f $workspace/tetrapak-commons/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.inclusions=**/src/main/java/com/tetrapak/commons/core/**/*,**/integration/**/*,**/it.launcher/**/*,**/ui.apps/**/*,**/models/**/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-commons -Dsonar.branch=JAVA -Dbuildversion=${build_id_number}"
												}
												if (params.Build_Customerhub)
												{
												echo "Running Sonar for JAVA profile on Project - Customerhub"
                                                sh "mvn -f $workspace/tetrapak-customerhub/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.inclusions=**/src/main/java/com/tetrapak/customerhub/core/**/*,**/integration/**/*,**/it.launcher/**/*,**/ui.apps/**/*,**/models/**/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-customerhub -Dsonar.branch=JAVA -Dbuildversion=${build_id_number}"
												}
												if (params.Build_Publicweb)
												{
												echo "Running Sonar for JAVA profile on Project - PublicWeb"
                                                sh "mvn -f $workspace/tetrapak-publicweb/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.inclusions=**/src/main/java/com/tetrapak/customerhub/core/**/*,**/integration/**/*,**/it.launcher/**/*,**/ui.apps/**/*,**/models/**/* -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=tetrapak-publicweb -Dsonar.branch=JAVA -Dbuildversion=${build_id_number}"
												}
                                        }
                                }
                        }
                }

				stage ('Deployment Author and Publish Parallel') {
                        steps {
                                parallel (
                                        "Author Deployment" : {
												script {
														if (params.Build_Commons) {
                                                echo "Uninstalling Old Commons Package on author for Commons"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-commons.complete'"
                                                sleep 20
                                                echo "Deleting Old Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-commons.complete'"
                                                sleep 10
                                                echo "Uploading New Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-${build_id_number}.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Commons Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-commons.complete'"
                                                sleep 20
												}
													if (params.Build_Customerhub) {
                                                echo "Uninstalling Old Package on author for customerhub"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-customerhub.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-customerhub.complete'"
                                                sleep 10
                                                echo "Uploading New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-customerhub/complete/target/tetrapak-customerhub.complete-1.0.0-${build_id_number}.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-customerhub.complete'"
												sleep 20
												}

													if (params.Build_Publicweb) {
                                                 echo "Uninstalling Old Package on author -- PublicWeb"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-publicweb.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-publicweb.complete'"
                                                sleep 10
                                                echo "Uploading New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-publicweb.complete -F file=@$workspace/tetrapak-publicweb/complete/target/tetrapak-publicweb.complete-1.0.0-${build_id_number}.zip -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on author"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${author_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-publicweb.complete'"
												}
                                        }},
                                        "Publish_Deployment" : {
										    script {
														if (params.Build_Commons) {
                                                echo "Uninstalling Old Commons Package on publish for commons"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-commons.complete'"
                                                sleep 20
                                                echo "Deleting Old Commons Package  on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-commons.complete'"
                                                sleep 10
                                                echo "Uploading New Commons Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-commons/complete/target/tetrapak-commons.complete-1.0.0-${build_id_number}.zip -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Commons Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-commons.complete'"
                                                sleep 20
												}
													if (params.Build_Customerhub) {
                                                echo "Uninstalling Old Package on publish for customerhub"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-customerhub.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-customerhub.complete'"
                                                sleep 10
                                                echo "Uploading New Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-customerhub.complete -F file=@$workspace/tetrapak-customerhub/complete/target/tetrapak-customerhub.complete-1.0.0-${build_id_number}.zip -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-customerhub.complete'"
												sleep 20
												}

													if (params.Build_Publicweb) {
												echo "Uninstalling Old Package on publish -- PublicWeb"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=tetrapak-publicweb.complete'"
                                                sleep 20
                                                echo "Deleting Old Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=tetrapak-publicweb.complete'"
                                                sleep 10
                                                echo "Uploading New Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F name=tetrapak-publicweb.complete -F file=@$workspace/tetrapak-publicweb/complete/target/tetrapak-publicweb.complete-1.0.0-${build_id_number}.zip -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=upload' --verbose"
                                                sleep 10
                                                echo "Installing New Package on publish"
                                                sh "curl -u admin:Oa=]2Z7u#w@Mkojms*V=mj\\>a -F force=true '${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=tetrapak-publicweb.complete'"
												}
                                            } 
									    }
                                )
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
                                                        sh 'cp -r ${karmapath_cuhu} /app/build-area/releases'
														sh 'cp -r /app/build-area/releases/coverage/index.html .'
                                                        publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: '/app/build-area/releases/coverage', reportFiles: 'index.html', reportName: 'Karma Report', reportTitles: ''])
                                                        sh 'cp -r /app/build-area/releases/coverage/index.html /app/splunk-output/karmajson/customerhub'
														
														
                                                         echo "Starting pa11y test Run on CustomerHub Urls"
                                                         sh 'chmod 777 Devops/PallyReporting.sh'
                                                         sh './Devops/PallyReporting.sh'
                                                         publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'PallyReport_CustomerHub.html', reportName: 'Pally Report', reportTitles: ''])
														 echo "Starting Zap Test Run- CustomerHub"
                                                          sh 'docker run --add-host tetrapak-dev64a.dev.adobecqms.net:104.46.45.30 --detach --name zap -u zap -v "$(pwd)/reports":/zap/reports/:rw \
                                                         -i owasp/zap2docker-stable zap.sh -daemon -host 0.0.0.0  \
                                                          -config api.addrs.addr.name=.* -config api.addrs.addr.regex=true \
                                                          -config api.disablekey=true'  
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
