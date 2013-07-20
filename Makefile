rootdir = .

libs = $(rootdir)/lib/junit-4.5.jar

CLASSPATH = $(rootdir)/src:$(libs):$(rootdir)/tests

sourcelist = $(shell find $(rootdir) -name '*.java' | sed "s,[.]/,,g")

docdir = ./docs 

default: all 

all:
	@javac -cp $(CLASSPATH) $(sourcelist) 

	@javadoc -d $(docdir) -linksource $(sourcelist)

	@echo "java -cp" $(CLASSPATH) "org.junit.runner.JUnitCore " $(tests) > runTests
	@chmod +x runTests

clean:
	@if [ -d $(docdir) ]; then rm -r $(docdir); fi;

	@-find $(rootdir) \( -name "*~" -o -name "*.class" -o -name "runTests" \) -exec rm '{}' \;