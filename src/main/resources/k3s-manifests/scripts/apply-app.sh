#!/bin/bash

cd ../app

kubectl apply -f config.yaml
kubectl apply -f deploy.yaml
kubectl apply -f service.yaml