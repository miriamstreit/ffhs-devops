# Kubernetes Deployment setup

This setup defines a Kubernetes cluster for the full-stack application Brew-Buddy with frontend, backend, and database components. It includes deployments, services, ingress configurations, and persistent storage for PostgreSQL.

## Short intro into what is Kubernetes and why all files are needed

- Ingress
Manages external HTTP/HTTPS access to services in the cluster, routing traffic based on hostnames or paths.

- Service
Exposes a set of Pods as a single endpoint, enabling stable communication and load balancing.

- Deployment
Manages the desired state of application Pods, handling rollouts, updates, and scaling.

- Volume
Provides storage for data in containers, ensuring persistence across container restarts.

- PersistentVolumeClaim (PVC)
A request for storage that binds to a PersistentVolume, providing persistent storage for applications.

## Backend Deployment

Defines a deployment for the backend application, exposing it via a service and an ingress route under the path `/api`. 
The image name is required and the dummy variable `BACKEND_IMAGE` will be replaced by Kustomize with the current image.

Deployment configuration:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
        - name: backend
          image: BACKEND_IMAGE
          ports:
            - containerPort: 8080
          envFrom:
            - configMapRef:
                name: backend-config
            - secretRef:
                name: backend-secret
```

Service configuration:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: backend-service
spec:
  selector:
    app: backend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
```

The host of the Ingress will be replaced using Kustomize and contains the hostname. 
Ingress configuration:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: backend-ingress
spec:
  rules:
    - host: DUMMY_HOST
      http:
        paths:
          - pathType: Prefix
            path: "/api"
            backend:
              service:
                name: backend-service
                port:
                  number: 80
```

## Frontend Deployment

Defines a deployment for the frontend application, exposing it via a service and an ingress route under the root path `/`.
The replacement of the image works the same as for the backend.

Deployment configuration:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
        - name: frontend
          image: FRONTEND_IMAGE
          ports:
            - containerPort: 80
          envFrom:
            - configMapRef:
                name: frontend-config
```

Service configuration:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: frontend-service
spec:
  selector:
    app: frontend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80

```

Ingress configuration:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: frontend-ingress
spec:
  rules:
    - host: DUMMY_HOST
      http:
        paths:
          - pathType: Prefix
            path: "/"
            backend:
              service:
                name: frontend-service
                port:
                  number: 80
```

## Database Deployment

Defines a deployment for the PostgreSQL database, exposing it via a service. It also includes a persistent volume claim for data storage.
The image is specified statically as the Postgres image won't be updated as often as the application images.

Deployment configuration:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: db-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: db
  template:
    metadata:
      labels:
        app: db
    spec:
      containers:
        - name: postgres
          image: postgres:15
          ports:
            - containerPort: 5432
          envFrom:
            - configMapRef:
                name: postgres-config
            - secretRef:
                name: postgres-secret
          volumeMounts:
            - mountPath: /var/lib/postgresql/data
              name: postgresdata
      volumes:
        - name: postgresdata
          persistentVolumeClaim:
            claimName: postgres-volume-claim
```

Persistent volume configuration:

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: postgres-volume
  labels:
    app: db
spec:
  storageClassName: ""
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteMany
  hostPath:
    path: /data/postgresql
```

Persistent volume claim configuration:

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-volume-claim
  labels:
    app: db
spec:
  storageClassName: ""
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 10Gi
```

Service configuration:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: db-service
spec:
  selector:
    app: db
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
```

## Kustomization

The Kubernetes resources are organized using a Kustomization file to manage the deployment, service, and ingress configurations for the backend, frontend, and database components. The Kustomization only specifies which files will be considered when compiling the complete Kubernetes manifest.

Kustomize is used to dynamically change the contents of the Kubernetes manifests file. In our project, the files are organized as follows:

The base files live within the `k8s-deploy` folder. The content of these files will be overridden using the specifications of the specific environments. 
For this reason, the stage specific files live within their respective folders `develop`, `release` and `production`.

Kustomization file:

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - backend-deployment.yml
  - backend-service.yml
  - backend-ingress.yml
  - postgres-deployment.yml
  - postgres-service.yml
  - postgres-persistentvolume.yml
  - postgres-pvc.yml
  - frontend-deployment.yml
  - frontend-service.yml
  - frontend-ingress.yml
```

The template file references the base folder as a resource onto which it will be applied. The namespace will be set using the branch name in the pipeline,
the images are set using their name and the build ID of the pipeline as a tag. The hostname will be set using a Kustomize patch. The ConfigMap and the Secret 
objects are created using `configMapGenerator` and `secretGenerator`. These contain the names of the resources and their values as literals. In a productive
setup however, passwords should not be stored plain in these files.

kustomization-template.yml File

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - ../k8s-deploy

namespace: ${BRANCH_NAME}

images:
  - name: BACKEND_IMAGE
    newName: sebastianschuum/schuum-backend
    newTag: "${BUILD_ID}"
  - name: FRONTEND_IMAGE
    newName: sebastianschuum/schuum-frontend
    newTag: "${BUILD_ID}"

patches:
  - target:
      kind: Ingress
    path: ingress-set-hostname.yml

configMapGenerator:
  - name: backend-config
    literals:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db-service:5432/schuum
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
  - name: frontend-config
    literals:
      - VITE_API_URL="http://schuumapp-develop.sw3.ch/api"
  - name: postgres-config
    literals:
      - POSTGRES_DB=schuum

secretGenerator:
  - name: backend-secret
    literals:
      - SPRING_DATASOURCE_USERNAME=schuumadmin
      - SPRING_DATASOURCE_PASSWORD=i<3Beer
  - name: postgres-secret
    literals:
      - POSTGRES_USER=schuumadmin
      - POSTGRES_PASSWORD=i<3Beer

```

## Rollout using Jenkins

This stage will only run on the branches `develop`, `release` and `main`. Based on the branch name, the correct Kustomization directory is entered
and the commands are executed within this path. Using `envsubst` the variables set in the pipeline environment will be applied to the Kustomization template file, 
and the Kustomization template then builds the final manifests which will be applied with the `kubectl` command.

```groovy
stage('Deploy to k8s') {
  when {
    anyOf {
      branch 'develop'
      branch 'release'
      branch 'main'
    }
  }
  steps {
    dir("env/${BRANCH_NAME}") {
      sh '${JENKINS_HOME}/.kube/kubectl get nodes'
      sh '${JENKINS_HOME}/.kube/envsubst < kustomization-template.yml > kustomization.yml'
      sh '${JENKINS_HOME}/.kube/kustomize build . | ${JENKINS_HOME}/.kube/kubectl apply -f -'
    }
  }
}
```

## Environment changes

To deploy the YAML manifests to the server minikube was installed with the docker driver and added to the same docker network as the jenkins server, so that jenkins can communicate with the kubeapi. Additionally the nginx-ingress plugin was installed to allow access. 
