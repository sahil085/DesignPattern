    Required Technology stack::
	1. Java 8
	2. Tomcat 8

    Edit Run/Debug Configurations
	Add new Tomcate server Configuration -> 
	Name -> bluebell
	Application Server -> Tomcat
	Open browser -> after launch url -> http://localhost:8080/bluebell-client/
	Set VM options ->   -Dspring.profiles.active=development 
	Set JRE, http port
	
    Deployment Tab Setting::
    add artifact 'bluebell-360:war' with application context '/bluebell'
    add exteral sourced 'bluebell-client' with application context '/bluebell-client'

    In bb-app.js : 
	add email and employeeCode to your own in sfAuthUrl variable's value
	
	Take dump from bluebell@qa3.intelligrape.net:~/dbDumps folder and restore it into local db named bluebell
	 change one of the EMPLOYEE table entries email id to your own to be able to login.

