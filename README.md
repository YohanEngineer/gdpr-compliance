# GDPR-COMPLIANCE-APP

# TO DO

- [ ] Use spray-json to convert json schema to case class
- [ ] Add unit test
- [ ] Update the README

## Description

<b> gdpr-compliance </b> is a spark-scala tool that enable a client to hash or delete personal information detained in a csv stored in HDFS.

## Visuals

WE NEED TO ADD SOME VISUALS 


## Installation

To install the application you need to :

- install JDK8 and SBT
- clone the project
- package the application by running the following command in the root of the project

```
sbt assembly
```

## Usage

To see all the actions available use this command 

```
java -jar gdpr-compliance.jar
```

### Hash data of a client

```
java -jar gdpr-compliance.jar -h <id>
```

### Hash data of multiple clients

```
java -jar gdpr-compliance.jar -hm <id1>,<id2>,...
```

### Delete data of a client

```
java -jar gdpr-compliance.jar -d <id>
```

### Delete data of multiple client

```
java -jar gdpr-compliance.jar -dm <id1>,<id2>,...
```


## Authors and acknowledgment

This project was developped entirely by Yohan B (https://github.com/YohanEngineer) and Selim B (https://github.com/Selim-web) but we were helped by the fabulous tool named internet :earth_africa: 

### Useful resources that helped us 

- https://medium.com/analytics-vidhya/spark-encrypt-columns-for-pii-gdpr-compliance-and-security-3bf17bf59636
- https://sparkbyexamples.com/spark/different-ways-to-create-a-spark-dataframe/
- https://medium.com/codex/how-to-easily-test-spark-dataframe-transformations-3b8cc160a705
- https://regex101.com/r/FKV8ot/1
- https://docs.scala-lang.org/tour/regular-expression-patterns.html
- https://docs.scala-lang.org/getting-started/intellij-track/testing-scala-in-intellij-with-scalatest.html
- https://docs.github.com/en/actions/quickstart


## Project status

This project is a school project. We are currently working on it until the 5th of december.
