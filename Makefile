PHONY += all
all:
	./gradlew build

PHONY += run
run:
	./gradlew run

PHONY += clean
clean:
	rm -rf build/ .gradle/

PHONY += native
native:
	./gradlew customFatJar


.PHONY: $(PHONY)
