#!/bin/bash

cd ../pg

kubectl delete -f service.yaml
kubectl delete -f deploy.yaml
kubectl delete -f pv-pvc.yaml
kubectl delete -f config.yaml