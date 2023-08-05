# Db2 plugin for Dremio

We develop this plugin to support IBM DB2 as source, starting at Dremio `1.4.4`  and today to `24.1.0` 
The plugin was tested and used in production for many years.

Starting with 24.0 version dremio officially supports db2 but not works as espected and have many errors that cause pushdowns failed
and or generate worn or too expensive pushdown.

In this repo ARP yml was fined to works well with latest version.

## Environment

- Dremio Community support version 20.0+
- Java 8 (tested with temurin 8)

## Goals

- [x] support Dremio 24.1.0
- [ ] Fix decimal precision when use CTA

## How to build
```bash
./gradlew jar
```

## Notes

1. Conflicts with official Dremio IBM db2 plugin, official plugin must not be enabled while using this plugin

