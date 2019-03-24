# faire-backend

[![Build Status](https://travis-ci.org/luanpotter/faire.svg?branch=new-stuff)](https://travis-ci.org/luanpotter/faire)

This is a solution I made for the Faire Backend Exercise.

*new-stuff*: this branch has more stuff that I was able to do in the time until the interview; mainly I intend to add more tests to reach more edge cases now.

## Setup

Use Maven 3.6+ or more and Java 8.

Go to the root folder and run:

```bash
./cmds/build.sh
```

This will download all dependencies using maven an make sure everything is fine.

If you are using Intellij IDEA, you should consider installing the [Lombok plugin](https://projectlombok.org/setup/intellij).

## Running

To run, use the following command:

```bash
./cmds/run.sh <api_key>
```

Where `<api_key>` is the API key you must provide to access the API (you need to attain one to use this).

The tests will mock all requests so the key won't be neessary.

## Contributing

Any help is appreciated! Comment, suggestions, issues, PR's! Give us a star to help!

To open a PR, create a fork and then a feature branch from the master branch of this repo.
