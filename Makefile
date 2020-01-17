

all: 
	javac -classpath '/Applications/apache-tomcat-9.0.11/lib/servlet-api.jar' -d ../WEB-INF/classes *.java

clean:
	rm -f *.class