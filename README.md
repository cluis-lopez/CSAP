# CSAP
Test and load Oracle and mysql databases with simple java

The application is made of three different program sets:

- GeneraBBDD: the scripts and Java programs to create and feed the different databse tables. After compiling the Java programs and install the selected databse engine (either Oracle or Mysql), run create_mysql <number of customers> (or create_oracle <number of customers> to initialize the tables

- 2tier: stress the database in a client/server environment. Run these programs in a computer other than the server where your database engine is installed. The two Java programs you may use are:
    - QueryDebug: to check the connectivity to the database
    - MultiThread: to stress the database simulating a number of concurrent users doing simultaneous transactions against the database           engine. This utility creates one Java thread per simulated concurrent user so be careful with your kernel and network config when         running this app
    
- WebApp: distributed as a war file to deploy under Tomcat (works with Weblogic as well). You may do the same transactions as with the 2tier programs but using a web application server instead. To stress both engines (the database and the application server) you must use a load injector like The Grinder, Apache JMeter or Loadrunner if you're a rich guy
