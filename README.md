# Jeauty

Command line Java code formatter.

[![Build Status](https://api.travis-ci.org/enr/jeauty.png?branch=master)](https://travis-ci.org/enr/jeauty)

**Jeauty is a tool that modifies your files. Use it at your own risk. (In particular, commit or back up changes before usage.)**

There is not yet a distribution.

Build the tool:

	./gradlew installDist

Run:

	./build/install/jeauty/bin/jeauty --help

Get code analysis (will be written in `build/reports`):

	./gradlew reports
