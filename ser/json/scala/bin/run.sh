#!/bin/bash

scala -cp target/ser-jackson-example_2.11-1.0.0.jar:`cat bin/classPath.txt` org.ebay.datameta.ser.jackson.fasterxml.JacksonTestApp

