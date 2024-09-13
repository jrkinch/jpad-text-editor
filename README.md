<h1>jpad text editor</h1>
Project of a text editor clone using Java.<br><br>

Simple text editor prototype that has tabs.<br>


<h2>Installation:</h2>
1) Make sure to have the latest java install.<br>
&emsp;- https://www.java.com/en/download/
	

<h2>Getting Started:</h2>
Steps for creating .jar file:<br>
1) Run the <code>javac -d bin src/JPad.java</code> command in the project folder or 'run_compile_java.bat' file in the scripts folder to compile java.<br>
2) Run the <code>jar cfvm build/JPad.jar resources/docs/manifest.txt bin/*.class resources</code> command in the project folder or 'run_create_jar.bat' file in the scripts folder to create the .jar file.<br>


<h2>Running:</h2>
1) Run the executable jar file using the <code>java -jar build/JPad.jar</code> command in the project folder or 'run_jar.bat' file in the scripts folder.<br>
&emsp;- can also run .jar directly from the build folder.<br>

> [!NOTE]
> Can also run using the java code:<br>
> <code>java src/Jpad.java</code> command in the project folder or 'run_java_code.bat' file in the scripts folder to run the java code.<br>


<h2>Troubleshooting:</h2>
1) jarfix from Johann N. Löfflmann fixes some path issues.<br>
&emsp;- https://johann.loefflmann.net/jarfix<br>
2) Conflicting java version on your computer can cause issues. There is a uninstall tool from Java.<br>
&emsp;- https://www.java.com/en/download/help/testvm.xml<br><br>


> [!TIP]
> Ran into an issue where .jar file wouldn't run or do anything.<br>
> These are the steps I took to fix the issue:<br>
> 1) The jarfix program still wouldn't run the .jar file but now got 'a java exception has occurred' error.<br>
> 2) Used the uninstall tool as I had multiple versions of Java.<br>
> 3) Reinstalled latest version but still getting same 'a java exception has occurred' error.<br>
> 4) Restarted computer and now able to use .jar file to run program.<br>
