# SpringBoot-2.7.5--Spring-Security-

# docker
## mariadb
```bash
# 컨테이너 최초 시작
docker run --name gg24 -p 3306:3306 -v /Users/sukjineee/docker/mariadb:/var/lib/mysql -e MARIADB_ROOT_PASSWORD=gg24 -d mariadb:latest;
# 컨테이너 시작
docker start gg24;
# 컨테이너 중지
docker stop gg24;

# 터미널 접속
docker exec -it gg24 /bin/bash;
# db 접속
mariadb -u root -p;
# db 생성
create database gg24;
use gg24;
# user 생성, 전체권한 부여
grant all privileges on gg24.* to gg24@'%' identified by 'gg24';
flush privileges;
```