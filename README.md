### XDS gRPC
* https://istio.io/v1.12/blog/2021/proxyless-grpc/
* https://github.com/grpc/grpc-java/tree/master/examples/example-xds

### How to build app's Docker image into Minikube
* https://minikube.sigs.k8s.io/docs/handbook/pushing/#1-pushing-directly-to-the-in-cluster-docker-daemon-docker-env

### Istio setup
* ./bin/istioctl install --set profile=minimal
* kubectl label namespace default istio-injection=enabled --overwrite

2nd command is a fix for container injection ...
