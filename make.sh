#!/bin/bash
javac *.java
rmic Server
rmic Client
rmiregistry
