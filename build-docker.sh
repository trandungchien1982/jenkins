# You need to login into the Docker Registry first by using `docker login`
export IMAGE_NAME_GLOBAL=tdchien1982/jenkins:05.jenkins-ci-django-navigate-url-1.0
echo "Build Docker Image for Python NavigateUrl: $IMAGE_NAME_GLOBAL"
uname -a
echo Check Docker Version
docker version

echo "Remove the old image (if exists) $IMAGE_NAME_GLOBAL"
docker stop django-navigate-url-demo
docker rm django-navigate-url-demo
docker image rm $IMAGE_NAME_GLOBAL --force

echo "Create Docker Image for Python NavigateUrl: $IMAGE_NAME_GLOBAL"
docker build ./django-app -t "$IMAGE_NAME_GLOBAL"

echo Push new image to Docker Registry
docker login
docker push "$IMAGE_NAME_GLOBAL"

# Process the interaction with Docker Server
echo "Run App using Docker on localhost"
#docker run -p 8400:8000 --name django-navigate-url-demo -d --env PORT=8000 "$IMAGE_NAME_GLOBAL"