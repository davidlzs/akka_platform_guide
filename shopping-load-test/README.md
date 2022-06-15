Gatling-gRPC demo in sbt
=========================

A simple project forked from
[Gatling's SBT plugin demo](https://github.com/gatling/gatling-sbt-plugin-demo)
to show how to use Gatling's SBT plugin to run Gatling-gRPC simulations.

Only the integration test is changed to a test with Gatling-gRPC.

This project uses SBT 1, which is available [here](https://www.scala-sbt.org/download.html).

## Try on your own

You are suggested to follow the
[step by step guide](https://medium.com/@georgeleung_7777/a-demo-of-gatling-grpc-bc92158ca808)
to reach the state in this repository.

Refer to this repository only when you get stuck.

Get the project
---------------

```bash
$ git clone https://github.com/phiSgr/gatling-grpc-sbt-demo.git && cd gatling-grpc-sbt-demo
```

Start SBT
---------
```bash
$ sbt
```

Run the gRPC test
-------------------

```sbtshell
> gatling-it:test
```

or in bash
```bash
$ sbt gatling-it:test
```


# Notes

Start the project
------------------
Create a new directory: `shopping-load-test`, copy and paste the build.sbt into the newly created directory.

The in the IntelliJ `sbt` side menu, add the sbt project.

Copy and paste the `build.properties`, `plugins.sbt`, and `scalapb.sbt` into the `project` sub folder.

Create `src` folder, and copy all the source files to the `src` fodler.


Run only specific simulation
-----------------

```bash
sbt "gatling-it:testOnly computerdatabase.BasicShoppingCartItSimulation"
```