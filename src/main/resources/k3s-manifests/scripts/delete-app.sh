#!/bin/bash

cd ../app

kubectl delete -f service.yaml
kubectl delete -f deploy.yaml
kubectl delete -f config.yaml