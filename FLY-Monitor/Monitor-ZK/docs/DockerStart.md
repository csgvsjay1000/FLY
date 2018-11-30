# zk服务启动

docker run -d -p 10100:10100 --name=zk_dubbo_kafka_10100 -it -v /root/csg/dockerContainer/zk/10100:/FLY/10100 cc9cafa53651 /bin/bash