JCC = javac
SRC_DIR = src
default:
	$(JCC)  $(SRC_DIR)/* -d ./
run_Server:
	java Server
run_Player:
	java Player
clean:
	rm *.class
