* [Swagger-UI](http://localhost:8080/api/v1/ms17/swagger-ui/index.html)

**These additional references will help you:**

docker images ~ see all docker images<br/>
docker pull mysql:latest<br/>
docker run --name mysql -e MYSQL_ROOT_PASSWORD=2000 -d mysql:latest<br/>
docker ps<br/>
docker container ls ~ see all docker containers<br/>
docker exec -it {container_id} /bin/sh ~ use specified container<br/>
mysql -uroot -p2000 ~ connect to mysql<br/>
show databases;<br/>
use {database_name};<br/>
show tables;<br/>
select * from {table_name};<br/>

docker build -t {image_name} . ~ builds an image from the current project<br/>
docker run -d -e LOG_LEVEL=INFO --name {container_name} -p {container_port_no}:{localhost_port_no} {image_id} ~ runs project image locally<br/>

docker logs {container_id} ~ view logs of a specific container<br/>

docker compose up -d ~ run docker compose file<br/>
docker compose down ~v  shut down the containers<br/
