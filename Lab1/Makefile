SRC = $(wildcard src/*.java)
BIN = bin


all:
	@javac -d $(BIN) $(SRC)

clean:
	@rm -rf $(BIN)

run: all
	@echo "RUNNING PROGRAM"
	@echo "----------------------"
	@java -cp $(BIN) Main
	@echo "----------------------"
	@echo "RUN COMPLETED"
	@rm -rf $(BIN)

.PHONY: all clean run