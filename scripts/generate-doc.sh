#!/usr/bin/env bash

./sbt "project sea-docs" paradox
./scripts/publish-doc.sh
