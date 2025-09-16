# Prometheus installation

To install Prometheus we used the kube-prometheus operator. Since there are some values that we need to customize the CLI tools `jsonnet` and `jsonnet-bundler` are needed. 

```shell
go install github.com/google/jsonnet/cmd/jsonnet@latest
go install github.com/jsonnet-bundler/jsonnet-bundler/cmd/jb@latest
go install github.com/brancz/gojsontoyaml@latest
```

The following commands are them needed to initialize the project:

```shell
mkdir -p env/develop/prometheus; cd env/develop/prometheus 
jb init  # Creates the initial/empty `jsonnetfile.json`
# Install the kube-prometheus dependency
jb install github.com/prometheus-operator/kube-prometheus/jsonnet/kube-prometheus@main # Creates `vendor/` & `jsonnetfile.lock.json`, and fills in `jsonnetfile.json`

wget https://raw.githubusercontent.com/prometheus-operator/kube-prometheus/main/example.jsonnet -O example.jsonnet
wget https://raw.githubusercontent.com/prometheus-operator/kube-prometheus/main/build.sh -O build.sh
chmod +x build.sh
```

## Configuration 

The Configuration is done in the `prometheus.jsonnet` file. The lines added are: 

```jsonnet
{ 
    values+:: {
        common+: {
            namespace: 'monitoring',
        },
        prometheus+: {
            namespaces: [],
        }
    }
}
```

By adding there values the prometheus stack runs in the monitoring namespace and is not constrained to it. 

To install the monitoring stack the following commands can be used:

```bash
./build.sh 
kubectl apply --server-side -f manifests/setup
# Wait for the resources
kubectl apply -f manifests
```

This installs Prometheus and Grafana.
