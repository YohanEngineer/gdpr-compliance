# GDPR-COMPLIANCE-APP

# TO DO

- [ ] Add method to get the hashed data to stay in the dataframe
- [ ] Use spray-json to convert json schema to case class
- [ ] Add unit test
- [ ] Use Timestamp instead of Date for the last column 
- [ ] Test multiple hash and multiple delete
- [ ] Update the README 
- [ ] Add Github Action to build the project with sbt assembly

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

gdpr-compliance is a spark-scala tool that enable a client to hash or delete personnal information detained in a csv stored in HDFS.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation

To install the app you need to :

- install JDK8 and SBT
- clone the project
- package the application

```java
java -jar gdpr-compliance-assembly-0.1.0-SNAPSHOT.jar
```

## Usage

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

## Project status

This project is a school project. We are currently working on it until the 5th of december.
