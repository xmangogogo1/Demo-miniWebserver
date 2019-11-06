name: Xiaoman Wang
Assignment name: PA1—-Xiaoman_Wang.tar.gz
Date: 2/21/2018



Description :

	Using unblocking socket to implement a multithread server, server socket keeping accepting new client request, once there comes one, it generate a new thread to process this request with assign this new thread the client's socket. Implement with HTTP/1.0 



submitted files:

		code:	
				/src/WebServer.java
				/src/Responce.java
				/src/Request.java
		
		pages:
				/SCU_files 
				/SCU.htm

		explanations:
				/readme.txt
				/output.pdf
				/makefile
				



Instruction of running:

	1.unzip the tar file
			
	2.go to folder:
			>> cd PA1--Xiaoman_Wang
	3.to compile:
			>> make

	4.to run the server:
			>> make run
**********
$note$ : 
**********
  	you could also self-define the port and document_root by edit the makefile under run command, and then run the command: 
			>> make run  



testing: 
	>> telnet localhost 8080
