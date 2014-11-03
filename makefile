#Billy Barbaro

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	UninitializedObjectException.java \
	SocialNetworkObject.java \
	SocialNetworkUtility.java \
	SocialNetworkStatus.java \
	User.java \
	Friend.java \
	Link.java \
	SocialNetwork.java

default: classes

classes: $(CLASSES:.java=.class)

TESTS = ./Tests/UserTester.java \
	./Tests/FriendTester.java \
	./Tests/LinkTester.java \
	./Tests/SocialNetworkTester.java

test:
	javac -cp ./Tests/junit-4.10.jar:. $(TESTS)
	java -cp ./Tests/junit-4.10.jar:Tests:. org.junit.runner.JUnitCore $(notdir $(TESTS:.java=))


clean:
	$(RM) *.class
	$(RM) ./Tests/*.class