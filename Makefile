run_proxy:
	javac ./Project/Proxy/*.java -d ./bin
run_gui:
	javac ./Project/Gui/Main.java -d ./bin
	javac ./Project/Gui/Handlers/*.java -d ./bin
	javac ./Project/Gui/Components/*.java -d ./bin
	java -classpath ./bin Project.Gui.Main