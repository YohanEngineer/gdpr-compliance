# GDPR-COMPLIANCE-APP

## Description

<b> gdpr-compliance </b> is a spark-scala tool that enable a client to hash or delete personal information detained in a csv stored in HDFS.

## Visuals

With gdpr-compliance app you can hash or delete personal data of your client with only one command line!

#### Before the hash 

![beforeHash](https://github.com/YohanEngineer/gdpr-compliance/blob/main/pictures/beforeHash.png)

#### After the hash

![afterHash](https://github.com/YohanEngineer/gdpr-compliance/blob/main/pictures/afterHash.png)

#### Before the deletion 

![beforeDelete](https://github.com/YohanEngineer/gdpr-compliance/blob/main/pictures/beforeDelete.png)

#### After the deletion

![afterDelete](https://github.com/YohanEngineer/gdpr-compliance/blob/main/pictures/AfterDelete.png)


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
spark-submit --class Main --deploy-mode cluster gdpr-compliance.jar
```

### Hash data of a client

```
spark-submit --class Main --deploy-mode cluster gdpr-compliance.jar -h <id>
```

### Hash data of multiple clients

```
spark-submit --class Main --deploy-mode cluster gdpr-compliance.jar -hm <id1>,<id2>,...
```

### Delete data of a client

```
spark-submit --class Main --deploy-mode cluster gdpr-compliance.jar -d <id>
```

### Delete data of multiple client

```
spark-submit --class Main --deploy-mode cluster gdpr-compliance.jar -dm <id1>,<id2>,...
```


## Authors and acknowledgment

This project was developped entirely by [Yohan B](https://github.com/YohanEngineer) and [Selim B](https://github.com/Selim-web) but we were helped by the fabulous tool named internet :earth_africa: 

### Useful resources that helped us 


- [Encrypt data in Spark Dataframe](https://medium.com/analytics-vidhya/spark-encrypt-columns-for-pii-gdpr-compliance-and-security-3bf17bf59636)
- [Create a Dataframe manually](https://sparkbyexamples.com/spark/different-ways-to-create-a-spark-dataframe/)
- [Test transformation on DataFrame in Scala](https://medium.com/codex/how-to-easily-test-spark-dataframe-transformations-3b8cc160a705)
- [Regex for SHA256](https://regex101.com/r/FKV8ot/1)
- [Regular expression in Scala](https://docs.scala-lang.org/tour/regular-expression-patterns.html)
- [Test in Scala](https://docs.scala-lang.org/getting-started/intellij-track/testing-scala-in-intellij-with-scalatest.html)
- [Start with Github Actions](https://docs.github.com/en/actions/quickstart)

## Project status

This project is a school project. We are currently working on it until the 5th of december.
