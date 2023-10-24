# You need to login into the Docker Registry first by using `docker login`
export IMAGE_NAME_GLOBAL=tdchien1982/jenkins:04.jenkins-ci-java-hello-world-1.0
echo "Build Docker Image for Java HelloWorld: $IMAGE_NAME_GLOBAL"
uname -a
echo Check Docker Version
docker version

echo "Remove the old image (if exists) $IMAGE_NAME_GLOBAL"
docker stop java-hello-world-demo
docker rm java-hello-world-demo
docker image rm $IMAGE_NAME_GLOBAL --force

echo "Create Docker Image for Java Project: $IMAGE_NAME_GLOBAL"
docker build ./hello-world-app -t "$IMAGE_NAME_GLOBAL"

echo Push new image to Docker Registry
docker login
docker push "$IMAGE_NAME_GLOBAL"

echo "Run App using Docker on localhost"
docker run -p 9200:8100 --name java-hello-world-demo -d --env PORT=8100 "$IMAGE_NAME_GLOBAL"