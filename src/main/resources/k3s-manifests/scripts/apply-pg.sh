#!/bin/bash

cd ../pg

kubectl apply -f config.yaml
kubectl apply -f pv-pvc.yaml
kubectl apply -f deploy.yaml
kubectl apply -f service.yaml