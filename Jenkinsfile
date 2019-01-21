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
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=css -Dsonar.sources='./' -Dsonar.exclusions=**/tests/**,**/breadcrumb/**,**/clientlibs/** -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak-${params.CHOICE} -Dsonar.branch=CSSBranch -Dbuildversion=${BUILD_NUMBER}"
                }
            }
		stage ('Sonar_JAVA') {
			steps {
			    

			    sh "mvn -f $workspace/${params.CHOICE}/pom.xml -e -B sonar:sonar  -Dsonar.language=java -Dsonar.sources='./' -Dsonar.exclusions=**/tests/**,**/customerhub/**,**/inegration/**,**/launcher/**,**/models/**,**/schedulers/** -Dsonar.host.url=${sonar_url} -Dsonar.login='admin' -Dsonar.password='admin' -Dsonar.projectKey=Tetrapak-${params.CHOICE} -Dsonar.branch=JAVABranch -Dbuildversion=${BUILD_NUMBER}"
                }
            }	
		stage ('Author_Deployment') {
		    steps {
			echo "Uninstalling Old Package"
			sh 'curl -u admin:admin -F force=true "${author_url}/crx/packmgr/service.jsp?cmd=uninst&name=${package_name}"'
			echo "Removing Old Package"
			sh 'curl -u admin:admin -F force=true "${author_url}/crx/packmgr/service.jsp?cmd=rm&name=${package_name}"'
			echo "Uploading New Package"
			sh 'curl -u admin:admin -F name=${params.CHOICE}.complete -F file=@$workspace/${params.CHOICE}/complete/target/${params.CHOICE}.complete-1.0.0-SNAPSHOT.zip -F force=true "${author_url}/crx/packmgr/service.jsp?cmd=upload" --verbose'
			echo "Installing New Package"
			sh 'curl -u admin:admin -F force=true "${author_url}/crx/packmgr/service.jsp?cmd=inst&name=${params.CHOICE}.complete"'
			}
		    }

		stage ('Publish_Deployment') {
		    steps {
			echo "Uninstalling Old Package"
			sh 'curl -u admin:admin -F force=true "${publish_url}/crx/packmgr/service.jsp?cmd=uninst&name=${package_name}"'
			echo "Removing Old Package"
			sh 'curl -u admin:admin -F force=true "${publish_url}/crx/packmgr/service.jsp?cmd=rm&name=${package_name}"'
			echo "Uploading New Package"
			sh 'curl -u admin:admin -F name=${params.CHOICE}.complete -F file=@$workspace/${params.CHOICE}/complete/target/${params.CHOICE}.complete-1.0.0-SNAPSHOT.zip -F force=true "${publish_url}/crx/packmgr/service.jsp?cmd=upload" --verbose'
			echo "Installing New Package"
			sh 'curl -u admin:admin -F force=true "${publish_url}/crx/packmgr/service.jsp?cmd=inst&name=${params.CHOICE}.complete"'
			}
		    }  
		       
		stage ('pa11y') {
		    steps {
			    echo "Starting pa11y test Run"
				sh 'pa11y -r html --standard WCAG2AA -i "notice;warning" -i "WCAG2AA.Principle1.Guideline1_1.1_1_1.H67.2;WCAG2AA.Principle1.Guideline1_1.1_1_1.G94.Image;WCAG2AA.Principle1.Guideline1_1.1_1_1.H24;WCAG2AA.Principle1.Guideline1_1.1_1_1.H24.2;WCAG2AA.Principle1.Guideline1_1.1_1_1.G73,G74;WCAG2AA.Principle1.Guideline1_1.1_1_1.H2.EG5;WCAG2AA.Principle1.Guideline1_1.1_1_1.H2.EG3;WCAG2AA.Principle1.Guideline1_1.1_1_1.G94,G92.Object;WCAG2AA.Principle1.Guideline1_2.1_2_4.G9,G87,G93;WCAG2AA.Principle1.Guideline1_2.1_2_2.G87,G93;WCAG2AA.Principle1.Guideline1_2.1_2_5.G78,G173,G8;WCAG2AA.Principle1.Guideline1_3.1_3_1.H48.1;WCAG2AA.Principle1.Guideline1_3.1_3_1.H48.2;WCAG2AA.Principle1.Guideline1_3.1_3_2.G57;WCAG2AA.Principle1.Guideline1_4.1_4_2.F23;WCAG2AA.Principle1.Guideline1_4.1_4_1.G14,G182;WCAG2AA.Principle1.Guideline1_4.1_4_3.G18.Fail;WCAG2AA.Principle1.Guideline1_4.1_4_3.G145.Fail;WCAG2AA.Principle1.Guideline1_4.1_4_3.G18;WCAG2AA.Principle1.Guideline1_4.1_4_3.G145;WCAG2AA.Principle1.Guideline1_4.1_4_3.F24.BGColour;WCAG2AA.Principle1.Guideline1_4.1_4_3.F24.FGColour;WCAG2AA.Principle1.Guideline1_4.1_4_4.G142;WCAG2AA.Principle1.Guideline1_4.1_4_5.G140,C22,C30.AALevel;WCAG2AA.Principle3.Guideline3_2.3_2_2.H32.2;WCAG2AA.Principle3.Guideline3_2.3_2_3.G61;WCAG2AA.Principle3.Guideline3_2.3_2_4.G197;WCAG2AA.Principle3.Guideline3_3.3_3_1.G83,G84,G85;WCAG2AA.Principle4.Guideline4_1.4_1_2.H91.InputRange.Name;WCAG2AA.Principle4.Guideline4_1.4_1_2.H91.A.NoContent" ${test_url} > pa11y.html | echo 0'
				
				publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: '', reportFiles: 'pa11y.html', reportName: 'pally Report', reportTitles: ''])

				
			      }
		                }
						
			stage ('sitespeed') {
				steps {
					echo "Starting Sitespeed Test Run"
					sh 'sitespeed.io -b chrome --browsertime.xvfb true -d 1 --outputFolder sitespeed ${test_url}'
					publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'sitespeed', reportFiles: 'index.html', reportName: 'sitespeed Report', reportTitles: ''])																   

					
						}
								}
			stage ('zap') {
				steps {
					echo "Starting Zap Test Run"
					sh 'zap-cli --zap-path /opt/ZAP_2.6.0 start -o "-config api.key=12345"'
					sh 'zap-cli --api-key 12345 spider ${test_url}'
					sh 'zap-cli --api-key 12345 report -f html -o "zap.html"'
					sh 'zap-cli --api-key 12345 shutdown'
					publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: '', reportFiles: 'zap.html', reportName: 'zap Report', reportTitles: ''])
						}
							}
							
            }						
		}
		