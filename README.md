# cs7is3-ir-indi

the individual assignment for CS7IS3-Information Retrieval.

## IMPORTANT

the host: `54.224.88.97`.

a account with sudo permit is ready for TA: `user4ta / HXfEmkrh`.

the project is under the home directory of the user.

project root: `~/cs7is3-indi`.

the directory structure:

```txt
user4ta@ip-172-31-35-243:~/cs7is3-indi$ tree .
.
├── QRelsCorrectedforTRECeval
├── README.md
├── cran
│   ├── cranqrel
│   └── cranqrel.readme
├── data
│   ├── result_EnglishAnalyzer.gnuplot
│   ├── result_EnglishAnalyzer.png
│   ├── result_SimpleAnalyzer.gnuplot
│   ├── result_SimpleAnalyzer.png
│   ├── result_WhitespaceAnalyzer.gnuplot
│   └── result_WhitespaceAnalyzer.png
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── life
│   │   │       └── tannineo
│   │   │           └── cs7is3
│   │   │               └── indi
│   │   │                   ├── App.java
│   │   │                   └── EnumTag.java
│   │   └── resources
│   │       └── cran
│   │           ├── cran.all.1400
│   │           └── cran.qry
│   └── test
│       └── java
│           └── life
│               └── tannineo
│                   └── cs7is3
│                       └── indi
│                           └── AppTest.java
├── trec_eval
└── trec_eval-9.0.7.tar.gz
```

## Gernerated Data

There are already generated data exists in `./data` folder.

Also with the `*.gnuplot` file.

## Compile

run `mvn package`, a jar file will be generated as `./target/cs7is3-indi-1.0.0-SNAPSHOT.jar`.

## Run

`java -jar target/cs7is3-indi-1.0.0-SNAPSHOT.jar`, it will perform `3 x 3 = 9`, 9 strategies to index and search the Cranfield data.

## TREC_Eval

A compiled `trec_eval` is in the project root folder.

The file called `QRelsCorrectedforTRECeval` is also in the project root folder.

Once the program get compiled and runned. Run a command like this to see the evaluation result.

```sh
./trec_eval QRelsCorrectedforTRECeval data/result_WhitespaceAnalyzer_LMDirichletSimilarity
```
