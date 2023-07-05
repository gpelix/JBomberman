EXEC = jbomberman
NATIVE_IMAGE = ~/opt/graalvm-ce-java17-22.3.1/bin/native-image

PHONY += all
all: fatJar docs

PHONY += build
build:
	./gradlew build

PHONY += run
run:
	./gradlew run

PHONY += clean
clean:
	rm -rf build/ .gradle/ bin/

PHONY += fatJar
fatJar:
	./gradlew customFatJar

PHONY += native
native: fatJar
	$(NATIVE_IMAGE) -jar build/libs/jbomberman-1.0.jar $(EXEC)

PHONY += docs
docs:
	./gradlew javadoc

.PHONY: $(PHONY)

# vim: set ts=8 sw=8 tw=0 noet :
