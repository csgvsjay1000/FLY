# zk服务启动

docker run -d -p 10300:10300 --name=ivg-redis_10300 -it -v /root/csg/dockerContainer/redis/10300:/usr/local/src/10300 9b953bbaaaea /bin/bash