#!/bin/bash

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:01", "name":"default-flow1", "cookie":"0",
"priority":"32768", "in_port":"1","active":"true",
"actions":"output=2"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:01", "name":"default-flow2", "cookie":"0",
"priority":"32768", "in_port":"2","active":"true",
"actions":"output=1"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:02", "name":"default-flow1", "cookie":"0",
"priority":"32768", "in_port":"1","active":"true",
"actions":"output=2"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:02", "name":"default-flow2", "cookie":"0",
"priority":"32768", "in_port":"2","active":"true",
"actions":"output=1"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:03", "name":"default-flow1", "cookie":"0",
"priority":"32768", "in_port":"1","active":"true",
"actions":"output=2"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:03", "name":"default-flow2", "cookie":"0",
"priority":"32768", "in_port":"2","active":"true",
"actions":"output=1"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:04", "name":"default-flow1", "cookie":"0",
"priority":"32768", "in_port":"1","active":"true",
"actions":"output=2"}' http://10.0.2.15:8080/wm/staticflowpusher/json

curl -X POST -d '{"switch":"00:00:00:00:00:00:00:04", "name":"default-flow2", "cookie":"0",
"priority":"32768", "in_port":"2","active":"true",
"actions":"output=1"}' http://10.0.2.15:8080/wm/staticflowpusher/json