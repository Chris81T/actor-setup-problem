actor-setup-problem
===================

This is a simple workaround ( combined of an early shiro project and a real project - the same structure as in the real project is given, but not the real logic) sample to show, that this app deployed on a JBoss AS 7.1 Application Server wont run properly. The problem is, that a initially setup message will is not received by the according actor. Also for a simple test simple stdout println's are used. Normally you should use a logger. This here should only reproduce the error. Also for creating this sample project some code parts are a bit hacked - don't worry about it. Thanks :) 

Sometimes it is needed to restart the server to reproduce the error. Remember: If you use the Jetty, then everything works fine. You have to use the JBoss AS 7.1 app-server! 

If everything works fine, you should see after the login during every page request a slided message, that the setup message is received. So the actor will perform showing a green panel with 'SETUP MESSAGE RECEIVED : )' 

How it works: 
TheSnippet will setup TheActor during every request - requirements are an available session and a logged in user - on the available session instance the method setupComet will be invoked ( using the sendCometActorMessage also wont work )

How to run it:

- The project is build with maven.
- Go into the folder and call "mvn package" or "mvn install" (if you want to place the app into your local repository)

- Then you can call "mvn jboss-as:run". This will simply download the JBoss AS 7.1.1 app-server and place it into your local repository ( if you already have a JBoss AS 7.1.1 reachable on localhost, you can skip this step ) and will finally deploy the application. After that you should go to the page: http://localhost:8080/actor-setup-problem-0.0.1-SNAPSHOT/

- If everything looks fine ( you will see the green panel above every time on different browsers ), please abort with [Ctrl]+[c] and call the "mvn jboss-as:run" again. Someday you
should not see the green panel. Then no setup message is received inside the actor (you wont see the stdout logs). 


