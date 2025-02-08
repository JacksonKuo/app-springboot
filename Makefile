# Description: Makefile for the springboot application

# Don't forget to export the environment variables secrets
# Don't forget to run redis

.PHONY: test build docker cluster secrets deploy

test:
	./gradlew test --info -Dspring.profiles.active=local

build:
	./gradlew build -Dspring.profiles.active=localk8

docker:
	docker build -t springboot --build-arg BASE_IMAGE="openjdk:17-jdk-slim" .

cluster:
	k3d cluster create local-cluster --port 8087:8087@loadbalancer

secrets:
	kubectl delete secret hcaptcha-secret --ignore-not-found
	kubectl create secret generic hcaptcha-secret --from-literal=HCAPTCHA_SECRET="$(HCAPTCHA_SECRET)"
	kubectl delete secret verify-service-sid --ignore-not-found
	kubectl create secret generic verify-service-sid --from-literal=VERIFY_SERVICE_SID="$(VERIFY_SERVICE_SID)"
	kubectl delete secret twilio-account-sid --ignore-not-found
	kubectl create secret generic twilio-account-sid --from-literal=TWILIO_ACCOUNT_SID="$(TWILIO_ACCOUNT_SID)"
	kubectl delete secret twilio-auth-token --ignore-not-found
	kubectl create secret generic twilio-auth-token --from-literal=TWILIO_AUTH_TOKEN="$(TWILIO_AUTH_TOKEN)"

secrets-check:
	kubectl get secrets
	kubectl get secret hcaptcha-secret -o yaml
	kubectl describe secret hcaptcha-secret
	#echo zz | base64 --decode

deploy:
	k3d image import springboot --cluster local-cluster
	helm install springboot ./springboot-chart -f ./springboot-chart/values-local.yaml

restart:
	kubectl rollout restart deployment springboot-app

log:
	kubectl describe pod -l app=springboot
	kubectl logs -l app=springboot --tail=100

uninstall:
	k3d cluster delete local-cluster

all: test build docker cluster secrets deploy
