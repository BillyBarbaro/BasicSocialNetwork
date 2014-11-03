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

TESTS = UserTester.java \
	FriendTester.java \
	LinkTester.java \
	SocialNetworkTester.java

test:
	javac -cp junit-4.10.jar:. $(TESTS)
	java -cp junit-4.10.jar:. org.junit.runner.JUnitCore $(TESTS:.java=)


clean:
	$(RM) *.class