


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 003 - Aula 02 Subindo a stack com API e Prometheus"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Subindo a stack com API e Prometheus

# Transcrição

[00:00] Seja bem-vindo à primeira aula do Capítulo 2. Nesse capítulo, vamos consumir as métricas com o Prometheus, porém, nessa aula 1, temos uma mudança na nossa stack.

[00:17] Lembra que, na última aula do Capítulo I (uma aula bem rápida), nós preparamos a aplicação para ela ser conteinerizada? Nós mudamos a chamada do Redis e a chamada do MySQL para o nome dos contêineres.

[00:35] Tendo feito isso, posso fechar o Eclipse, a IDE. Atualmente, a aplicação está rodando – na verdade, a aplicação não está mais rodando, já parei a minha aplicação, mas ainda estou com esse endpoint sendo exibido.

[00:56] O que vamos fazer? Vamos dar uma olhada no arquivo docker-compose, que está disponibilizado na plataforma. Você tem que pegar esse arquivo, a aplicação já foi "buildada" com essas alterações.

[01:13] Vou abrir o docker_compose.ymal (vim docker-compose.yaml), vou colocar aqui a numeração das linhas e vamos falar um pouco desse arquivo antes de subir a stack. O que foi mudado aqui? As redes internas do Docker.

[01:32] Agora eu tenho uma rede chamada database, que é interna; tenho uma rede chamada cache, que também é interna; uma rede chamada api, que é interna; e tenho uma rede chamada monit, que, no momento, está interna, porque vamos utilizar a interface web do Prometheus para entender como funciona a linguagem PromQL e compor as nossas primeiras métricas.

[01:56] Futuramente, ela vai também ser uma rede interna porque Prometheus server não precisa estar exposto. E a rede proxy, que é a rede que vai conduzir as requisições à aplicação, requisições externas.

[02:15] Os clientes vão consumir essa aplicação, mas, antes de chegar na aplicação, eles serão filtrados por um proxy, e é esse proxy que estará exposto, é um nginx.

[02:30] O Prometheus vai consumir a aplicação por dentro, então, ele não vai passar pelo proxy, mas os clientes vão passar. O que mudou nos serviços? O serviço do Redis está aqui, não mudou basicamente nada, exceto a rede. A network dele agora é a cache. Ele continua somente com o expose: 6379.

[02:53] Na verdade, isso mudou. Antes, ele estava fazendo bind de portas, estava com o ports, agora é o expose, ele só expõe a porta do contêiner, a 6379, e isso somente de forma interna, só dentro do Docker.

[03:10] Aqui, temos o mysql-forum-api que também teve mudanças, agora ele não tem mais ports, só tem o expose, então a porta 3306 do MySQL só é visível dentro do Docker e para quem tem acesso à rede database.

[03:29] Está na rede database e continua com a mesma dependência, que o redis, o redis tem que subir primeiro. Agora eu tenho a aplicação que vai ser "buildada", é a nossa app-forum-api.

[03:44] Ele vai fazer um build, cujo contexto é o diretório app. Ele vai encontrar um dockerfile dentro do diretório app e vai fazer o build, gerando a imagem app-forum-api.

[04:00] O nome do contêiner vai ser app-forum-api. O que mais tem aqui? A aplicação está em comunicação direta com três redes: a rede api, a rede database e a rede cache. Ela precisa falar com o MySQL e precisa falar com o redis.

[04:19] Quais são as dependências desse contêiner? O MySQL, simples assim, ele precisa do MySQL para poder consultar a base de dados; e o MySQL, por sua vez, depende do Redis.

[04:34] Então, tem uma cadeia de dependências que precisa ser cumprida, e ele tem o healthcheck. Nós passamos um curl, que vai internamente, com o contêiner app-forum-api:8080/, lá no actuator/health.

[04:47] Se ele retornar 200, está certo. Aqui está o timeout e as tentativas. Aqui está o contêiner do proxy, que é um nginx, ele tem diversas rotas que eu vou mostrar para vocês de forma rápida.

[05:05] Basicamente, esse vai fazer o bind de porta na porta 80 da sua máquina. A partir desse momento, as requisições vão ser para localhost, quando quisermos acessar a aplicação de forma externa. Ele pertence à rede proxy e api e depende do app-forum-api para subir.

[05:12] Por último, temos o contêiner do Prometheus. O nome será prometheus-forum-api; a imagem é o prom/prometheus:latest, então é a última versão, a mais atual; o nome do contêiner é prometheus-forum-api; e aqui os volumes do Prometheus.

[05:45] Você vai notar que, nesse pacote do Capítulo 2, existem algumas mudanças, ele é diferente do pacote do Capítulo 1, ele tem uma pasta chamada prometheus com um subdiretório prometheus_data com o arquivo Prometheus.yml.

[06:04] Assim como ele tem um diretório nginx que também tem um arquivo chamado nginx.conf e proxy.conf. Aqui, estão as configurações do Prometheus para sua subida. Tenho um arquivo de configuração que, se você notar, vai ser retirado do diretório prometheus/prometheus.yml e vai ser colocado no local em que estamos alimentando o config.file como parâmetro.

[06:32] Esse arquivo tem as configurações do Prometheus. Aqui, temos o path do TSDB, do storage dele, que também é um volume que vai sair da nossa máquina.

[06:45] Temos aqui as bibliotecas que o Prometheus vai utilizar; a porta que ele vai fazer bind, a 9090; e a rede que o Prometheus faz parte que, nesse caso, é a monit. Ele vai estar na monit. Vou, inclusive, fazer um adendo – esse adendo já vai estar no seu arquivo, não se preocupe.

[07:12] Que é a rede api para ele poder falar com a API, a rede api interna. Está feita a configuração, vou salvar, esse arquivo já vai estar assim para você. Agora podemos dar um docker-compose up -d, vou subir em modo daemon para não ocupar a tela.

[07:41] Vai demorar um pouco porque ele vai ter que descarregar a imagem do Spring e "buildar" o contêiner, conforme a configuração do Docker Compose, descarregar as outras imagens, subir todas elas e recriar tanto o contêiner do Redis e do MySQL quanto da mudança de rede.

[08:06] Está aí, já criou a stack, aparentemente subiu, vamos validar isso para garantir que está tudo certo. Agora, qual é a mudança? Vou em “http://localhost/topicos”, vamos ver se ele trouxe os tópicos para mim.

[08:28] O proxy está funcionando, está aqui, “/topicos”. Eu posso agora ir em “http://localhost/topicos/2”, trouxe para mim, a aplicação está funcionando. Qual é a mudança aqui? Quando quisermos acessar o endpoint de métricas, vamos acessar o “http://localhost/metrics”.

[08:55] Aqui, nós caímos nas métricas do Prometheus. Se quisermos acessar outros endpoints que são expostos pela aplicação através do Actuator, podemos também acessar o “http://localhost/health”, que vai estar aqui e o “http://localhost/info”, que também está disponível para nós.

[09:18] Onde que está essa configuração? Vou minimizar o browser e vou te mostrar rapidamente. Se dermos um ls, você vai ver que eu tenho o nginx, e aqui eu tenho o nginx.conf, que tem a configuração de redirecionamento.

[09:45] Não precisa se preocupar com isso, é só para você saber onde está. Existe essa configuração de proxy_pass que faz o redirecionamento das requisições.

[09:59] A configuração de proxy não tem nada de interessante para você, pelo menos nesse momento, isso não deve fazer muito sentido, mas é legal você entender como funciona essa configuração.

[10:12] Você vai notar que o proxy só serve para acessar a aplicação e os endpoints da aplicação de forma externa. Ele não acessa o Prometheus. O Prometheus em si está agora em “http://localhost:9090”.

[10:35] O Prometheus entrou no modo escuro, deixa eu mudar e diminuir o zoom, porque está meio estranho. Vamos falar sobre o Prometheus agora já na próxima aula.

[10:50] Na próxima aula, começamos a trabalhar diretamente com o Prometheus, fazendo uma navegação, entendendo a interface dos Prometheus e os recursos que ela vai nos entregar.

[11:05] É isso, nos vemos na próxima aula.


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Subindo a stack com API e Prometheus

 
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
docker-compose up




[01:13] Vou abrir o docker_compose.ymal (vim docker-compose.yaml), vou colocar aqui a numeração das linhas e vamos falar um pouco desse arquivo antes de subir a stack. O que foi mudado aqui? As redes internas do Docker.

[01:32] Agora eu tenho uma rede chamada database, que é interna; tenho uma rede chamada cache, que também é interna; uma rede chamada api, que é interna; e tenho uma rede chamada monit, que, no momento, está interna, porque vamos utilizar a interface web do Prometheus para entender como funciona a linguagem PromQL e compor as nossas primeiras métricas.

[01:56] Futuramente, ela vai também ser uma rede interna porque Prometheus server não precisa estar exposto. E a rede proxy, que é a rede que vai conduzir as requisições à aplicação, requisições externas.

- Docker-compose novo, com as coisas novas:
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/Materiais-aulas/aula_04/conteudo_04/docker-compose.yaml





- ANTES:

~~~~yaml
version: '3'

networks:
  local:

services:
  redis-forum-api:
    image: redis
    container_name: redis-forum-api
    restart: unless-stopped
    ports:
      - 6379:6379
    networks:
      - local

  mysql-forum-api:
    image: mysql:5.7
    container_name: mysql-forum-api
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: 'forum'
      MYSQL_USER: 'forum'
      MYSQL_PASSWORD: 'Bk55yc1u0eiqga6e'
      MYSQL_RANDOM_ROOT_PASSWORD: 'yes'
      MYSQL_ROOT_HOST: '%'
    volumes:
      - ./mysql:/docker-entrypoint-initdb.d
    ports:
      - 3306:3306
    networks:
      - local
    depends_on:
      - redis-forum-api
~~~~



- DEPOIS:

~~~~yaml
version: '3'

networks:
  database:
    internal: true
  cache:
    internal: true
  api:
    internal: true
  monit:
  proxy:

services:
  redis-forum-api:
    image: redis
    container_name: redis-forum-api
    restart: unless-stopped
    expose:
      - 6379
    networks:
      - cache

  mysql-forum-api:
    image: mysql:5.7
    container_name: mysql-forum-api
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: 'forum'
      MYSQL_USER: 'forum'
      MYSQL_PASSWORD: 'Bk55yc1u0eiqga6e'
      MYSQL_RANDOM_ROOT_PASSWORD: 'yes'
      MYSQL_ROOT_HOST: '%'
    volumes:
      - ./mysql:/docker-entrypoint-initdb.d
    expose:
      - 3306
    networks:
      - database
    depends_on:
      - redis-forum-api

  app-forum-api:
    build:
      context: ./app/
      dockerfile: Dockerfile
    image: app-forum-api
    container_name: app-forum-api
    restart: unless-stopped
    networks:
      - api
      - database
      - cache
    depends_on:
      - mysql-forum-api
    healthcheck:
      test: "curl -sS http://app-forum-api:8080/actuator/health"
      interval: 1s
      timeout: 30s
      retries: 60
  
  proxy-forum-api:
    image: nginx
    container_name: proxy-forum-api
    restart: unless-stopped
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/proxy.conf:/etc/nginx/conf.d/proxy.conf
    ports:
      - 80:80
    networks:
      - proxy
      - api
    depends_on:
      - app-forum-api

  prometheus-forum-api:
    image: prom/prometheus:latest
    container_name: prometheus-forum-api
    restart: unless-stopped
    volumes:
      - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
      - ./prometheus/prometheus_data:/prometheus
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--storage.tsdb.path=/prometheus'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--web.enable-lifecycle'
    ports:
      - 9090:9090
    networks:
      - monit
      - api
    depends_on:
      - proxy-forum-api

  client-forum-api:
    build:
      context: ./client/
      dockerfile: Dockerfile
    image: client-forum-api
    container_name: client-forum-api
    restart: unless-stopped
    networks:
      - proxy
    depends_on:
      - proxy-forum-api
~~~~














# IMPORTANTE
- Copiar os arquivos da pasta da aula_04, para dentro da pasta prometheus-grafana:
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/Materiais-aulas/aula_04/conteudo_04
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ ls
app  docker-compose.yaml  mysql
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ ls
app  client  docker-compose.yaml  mysql  nginx  prometheus
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$
~~~~







- Subindo stack
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
docker-compose up -d

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ docker-compose up -d
Creating network "prometheus-grafana_cache" with the default driver
Creating network "prometheus-grafana_database" with the default driver
Creating network "prometheus-grafana_api" with the default driver
Creating network "prometheus-grafana_proxy" with the default driver
Creating network "prometheus-grafana_monit" with the default driver
Building app-forum-api
Sending build context to Docker daemon  59.91MB
Step 1/6 : FROM openjdk:8-jdk-alpine
8-jdk-alpine: Pulling from library/openjdk
e7c96db7181b: Pull complete
f910a506b6cb: Pull complete
c2274a1a0e27: Pull complete
Digest: sha256:94792824df2df33402f201713f932b58cb9de94a0cd524164a0f2283343547b3
Status: Downloaded newer image for openjdk:8-jdk-alpine
 ---> a3562aa0b991
Step 2/6 : RUN addgroup -S spring && adduser -S spring -G spring
 ---> Running in 6802f9ce76f9
Removing intermediate container 6802f9ce76f9
 ---> 3d488c2b2200
Step 3/6 : USER spring:spring
 ---> Running in 02603b1dcaf8
Removing intermediate container 02603b1dcaf8
 ---> d88563c4142c
Step 4/6 : ARG JAR_FILE=target/*jar
 ---> Running in 5230c859744f
Removing intermediate container 5230c859744f
 ---> a55870ed6004
Step 5/6 : COPY ${JAR_FILE} app.jar
 ---> 139ab8599a00
Step 6/6 : ENTRYPOINT ["java","-Xms128M","-Xmx128M","-XX:PermSize=64m","-XX:MaxPermSize=128m","-Dspring.profiles.active=prod","-jar","/app.jar"]
 ---> Running in f4160b6659e4
Removing intermediate container f4160b6659e4
 ---> f68dc86d8333
Successfully built f68dc86d8333
Successfully tagged app-forum-api:latest
WARNING: Image for service app-forum-api was built because it did not already exist. To rebuild this image you must use `docker-compose build` or                                         `docker-compose up --build`.
Pulling proxy-forum-api (nginx:)...
latest: Pulling from library/nginx
e9995326b091: Already exists
71689475aec2: Pull complete
f88a23025338: Pull complete
0df440342e26: Pull complete
eef26ceb3309: Pull complete
8e3ed6a9e43a: Pull complete
Digest: sha256:943c25b4b66b332184d5ba6bb18234273551593016c0e0ae906bab111548239f
Status: Downloaded newer image for nginx:latest
Pulling prometheus-forum-api (prom/prometheus:latest)...
latest: Pulling from prom/prometheus
f5b7ce95afea: Pull complete
38a813b53864: Pull complete
a56778b6a658: Pull complete
442ae8ee6995: Pull complete
3b776dfacb2b: Pull complete
3b0bc18d3c30: Pull complete
e878f3f67dd3: Pull complete
3d39cb8fed51: Pull complete
52d3ef0644f1: Pull complete
56926bea1dc6: Pull complete
fe7520032892: Pull complete
c83493a50a72: Pull complete
Digest: sha256:dd692d1e1388dff116fddcda24ff7b7761b953aec6ed01a8281d572748b548a4
Status: Downloaded newer image for prom/prometheus:latest
Building client-forum-api
Sending build context to Docker daemon  4.096kB
Step 1/5 : FROM debian
latest: Pulling from library/debian
17c9e6141fdb: Pull complete
Digest: sha256:001ceb2477bc7817eb9addbdbf9b958237b4eb9db80415ad321b96af76252184
Status: Downloaded newer image for debian:latest
 ---> d8cacd17cfdc
Step 2/5 : USER root
 ---> Running in 11130486e216
Removing intermediate container 11130486e216
 ---> ac3609232120
Step 3/5 : COPY ./client.sh /scripts/client.sh
 ---> 78692f0da08c
Step 4/5 : RUN apt update &&         apt install curl -y &&         chmod +x /scripts/client.sh
 ---> Running in 2b29fae2d430

WARNING: apt does not have a stable CLI interface. Use with caution in scripts.

Get:1 http://deb.debian.org/debian bullseye InRelease [116 kB]
Get:2 http://deb.debian.org/debian-security bullseye-security InRelease [48.4 kB]
Get:3 http://deb.debian.org/debian bullseye-updates InRelease [44.1 kB]
Get:4 http://deb.debian.org/debian bullseye/main amd64 Packages [8184 kB]
Get:5 http://deb.debian.org/debian-security bullseye-security/main amd64 Packages [194 kB]
Get:6 http://deb.debian.org/debian bullseye-updates/main amd64 Packages [14.6 kB]
Fetched 8601 kB in 3s (2808 kB/s)
Reading package lists...
Building dependency tree...
Reading state information...
1 package can be upgraded. Run 'apt list --upgradable' to see it.

WARNING: apt does not have a stable CLI interface. Use with caution in scripts.

Reading package lists...
Building dependency tree...
Reading state information...
The following additional packages will be installed:
  ca-certificates libbrotli1 libcurl4 libldap-2.4-2 libldap-common
  libnghttp2-14 libpsl5 librtmp1 libsasl2-2 libsasl2-modules
  libsasl2-modules-db libssh2-1 openssl publicsuffix
Suggested packages:
  libsasl2-modules-gssapi-mit | libsasl2-modules-gssapi-heimdal
  libsasl2-modules-ldap libsasl2-modules-otp libsasl2-modules-sql
The following NEW packages will be installed:
  ca-certificates curl libbrotli1 libcurl4 libldap-2.4-2 libldap-common
  libnghttp2-14 libpsl5 librtmp1 libsasl2-2 libsasl2-modules
  libsasl2-modules-db libssh2-1 openssl publicsuffix
0 upgraded, 15 newly installed, 0 to remove and 1 not upgraded.
Need to get 2990 kB of archives.
After this operation, 6254 kB of additional disk space will be used.
Get:1 http://deb.debian.org/debian bullseye/main amd64 openssl amd64 1.1.1n-0+deb11u3 [853 kB]
Get:2 http://deb.debian.org/debian bullseye/main amd64 ca-certificates all 20210119 [158 kB]
Get:3 http://deb.debian.org/debian bullseye/main amd64 libbrotli1 amd64 1.0.9-2+b2 [279 kB]
Get:4 http://deb.debian.org/debian bullseye/main amd64 libsasl2-modules-db amd64 2.1.27+dfsg-2.1+deb11u1 [69.1 kB]
Get:5 http://deb.debian.org/debian bullseye/main amd64 libsasl2-2 amd64 2.1.27+dfsg-2.1+deb11u1 [106 kB]
Get:6 http://deb.debian.org/debian bullseye/main amd64 libldap-2.4-2 amd64 2.4.57+dfsg-3+deb11u1 [232 kB]
Get:7 http://deb.debian.org/debian bullseye/main amd64 libnghttp2-14 amd64 1.43.0-1 [77.1 kB]
Get:8 http://deb.debian.org/debian bullseye/main amd64 libpsl5 amd64 0.21.0-1.2 [57.3 kB]
Get:9 http://deb.debian.org/debian bullseye/main amd64 librtmp1 amd64 2.4+20151223.gitfa8646d.1-2+b2 [60.8 kB]
Get:10 http://deb.debian.org/debian bullseye/main amd64 libssh2-1 amd64 1.9.0-2 [156 kB]
Get:11 http://deb.debian.org/debian bullseye/main amd64 libcurl4 amd64 7.74.0-1.3+deb11u3 [345 kB]
Get:12 http://deb.debian.org/debian bullseye/main amd64 curl amd64 7.74.0-1.3+deb11u3 [269 kB]
Get:13 http://deb.debian.org/debian bullseye/main amd64 libldap-common all 2.4.57+dfsg-3+deb11u1 [95.8 kB]
Get:14 http://deb.debian.org/debian bullseye/main amd64 libsasl2-modules amd64 2.1.27+dfsg-2.1+deb11u1 [104 kB]
Get:15 http://deb.debian.org/debian bullseye/main amd64 publicsuffix all 20220811.1734-0+deb11u1 [127 kB]
debconf: delaying package configuration, since apt-utils is not installed
Fetched 2990 kB in 0s (7724 kB/s)
Selecting previously unselected package openssl.
(Reading database ... 6661 files and directories currently installed.)
Preparing to unpack .../00-openssl_1.1.1n-0+deb11u3_amd64.deb ...
Unpacking openssl (1.1.1n-0+deb11u3) ...
Selecting previously unselected package ca-certificates.
Preparing to unpack .../01-ca-certificates_20210119_all.deb ...
Unpacking ca-certificates (20210119) ...
Selecting previously unselected package libbrotli1:amd64.
Preparing to unpack .../02-libbrotli1_1.0.9-2+b2_amd64.deb ...
Unpacking libbrotli1:amd64 (1.0.9-2+b2) ...
Selecting previously unselected package libsasl2-modules-db:amd64.
Preparing to unpack .../03-libsasl2-modules-db_2.1.27+dfsg-2.1+deb11u1_amd64.deb ...
Unpacking libsasl2-modules-db:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Selecting previously unselected package libsasl2-2:amd64.
Preparing to unpack .../04-libsasl2-2_2.1.27+dfsg-2.1+deb11u1_amd64.deb ...
Unpacking libsasl2-2:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Selecting previously unselected package libldap-2.4-2:amd64.
Preparing to unpack .../05-libldap-2.4-2_2.4.57+dfsg-3+deb11u1_amd64.deb ...
Unpacking libldap-2.4-2:amd64 (2.4.57+dfsg-3+deb11u1) ...
Selecting previously unselected package libnghttp2-14:amd64.
Preparing to unpack .../06-libnghttp2-14_1.43.0-1_amd64.deb ...
Unpacking libnghttp2-14:amd64 (1.43.0-1) ...
Selecting previously unselected package libpsl5:amd64.
Preparing to unpack .../07-libpsl5_0.21.0-1.2_amd64.deb ...
Unpacking libpsl5:amd64 (0.21.0-1.2) ...
Selecting previously unselected package librtmp1:amd64.
Preparing to unpack .../08-librtmp1_2.4+20151223.gitfa8646d.1-2+b2_amd64.deb ...
Unpacking librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2+b2) ...
Selecting previously unselected package libssh2-1:amd64.
Preparing to unpack .../09-libssh2-1_1.9.0-2_amd64.deb ...
Unpacking libssh2-1:amd64 (1.9.0-2) ...
Selecting previously unselected package libcurl4:amd64.
Preparing to unpack .../10-libcurl4_7.74.0-1.3+deb11u3_amd64.deb ...
Unpacking libcurl4:amd64 (7.74.0-1.3+deb11u3) ...
Selecting previously unselected package curl.
Preparing to unpack .../11-curl_7.74.0-1.3+deb11u3_amd64.deb ...
Unpacking curl (7.74.0-1.3+deb11u3) ...
Selecting previously unselected package libldap-common.
Preparing to unpack .../12-libldap-common_2.4.57+dfsg-3+deb11u1_all.deb ...
Unpacking libldap-common (2.4.57+dfsg-3+deb11u1) ...
Selecting previously unselected package libsasl2-modules:amd64.
Preparing to unpack .../13-libsasl2-modules_2.1.27+dfsg-2.1+deb11u1_amd64.deb ...
Unpacking libsasl2-modules:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Selecting previously unselected package publicsuffix.
Preparing to unpack .../14-publicsuffix_20220811.1734-0+deb11u1_all.deb ...
Unpacking publicsuffix (20220811.1734-0+deb11u1) ...
Setting up libpsl5:amd64 (0.21.0-1.2) ...
Setting up libbrotli1:amd64 (1.0.9-2+b2) ...
Setting up libsasl2-modules:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Setting up libnghttp2-14:amd64 (1.43.0-1) ...
Setting up libldap-common (2.4.57+dfsg-3+deb11u1) ...
Setting up libsasl2-modules-db:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Setting up librtmp1:amd64 (2.4+20151223.gitfa8646d.1-2+b2) ...
Setting up libsasl2-2:amd64 (2.1.27+dfsg-2.1+deb11u1) ...
Setting up libssh2-1:amd64 (1.9.0-2) ...
Setting up openssl (1.1.1n-0+deb11u3) ...
Setting up publicsuffix (20220811.1734-0+deb11u1) ...
Setting up libldap-2.4-2:amd64 (2.4.57+dfsg-3+deb11u1) ...
Setting up ca-certificates (20210119) ...
debconf: unable to initialize frontend: Dialog
debconf: (TERM is not set, so the dialog frontend is not usable.)
debconf: falling back to frontend: Readline
debconf: unable to initialize frontend: Readline
debconf: (Can t locate Term/ReadLine.pm in @INC (you may need to install the Term::ReadLine module) (@INC contains: /etc/perl /usr/local/lib/x86_64-linux-gnu/perl/5.32.1 /usr/local/share/perl/5.32.1 /usr/lib/x86_64-linux-gnu/perl5/5.32 /usr/share/perl5 /usr/lib/x86_64-linux-gnu/perl-base /usr/lib/x86_64-linux-gnu/perl/5.32 /usr/share/perl/5.32 /usr/local/lib/site_perl) at /usr/share/perl5/Debconf/FrontEnd/Readline.pm line 7.)
debconf: falling back to frontend: Teletype
Updating certificates in /etc/ssl/certs...
129 added, 0 removed; done.
Setting up libcurl4:amd64 (7.74.0-1.3+deb11u3) ...
Setting up curl (7.74.0-1.3+deb11u3) ...
Processing triggers for libc-bin (2.31-13+deb11u5) ...
Processing triggers for ca-certificates (20210119) ...
Updating certificates in /etc/ssl/certs...
0 added, 0 removed; done.
Running hooks in /etc/ca-certificates/update.d...
done.
Removing intermediate container 2b29fae2d430
 ---> c8fc6cd6514b
Step 5/5 : ENTRYPOINT ["/scripts/client.sh"]
 ---> Running in 501ad1d6fd90
Removing intermediate container 501ad1d6fd90
 ---> d3f4714067de
Successfully built d3f4714067de
Successfully tagged client-forum-api:latest
WARNING: Image for service client-forum-api was built because it did not already exist. To rebuild this image you must use `docker-compose build` or `docker-compose up --build`.
Recreating redis-forum-api ... done
Recreating mysql-forum-api ... done
Creating app-forum-api     ... done
Creating proxy-forum-api   ... done
Creating client-forum-api     ... done
Creating prometheus-forum-api ... done
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$


fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE       COMMAND                  CREATED      STATUS       PORTS                                                  NAMES
ccdde684f777   mysql:5.7   "docker-entrypoint.s…"   8 days ago   Up 2 hours   0.0.0.0:3306->3306/tcp, :::3306->3306/tcp, 33060/tcp   mysql-forum-api
e976f206e53f   redis       "docker-entrypoint.s…"   8 days ago   Up 2 hours   0.0.0.0:6379->6379/tcp, :::6379->6379/tcp              redis-forum-api
fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED          STATUS                             PORTS                               NAMES
83e10d4ed708   prom/prometheus:latest   "/bin/prometheus --c…"   36 seconds ago   Restarting (2) 1 second ago                                            prometheus-forum-api
ee9f972a9709   client-forum-api         "/scripts/client.sh"     36 seconds ago   Up 35 seconds                                                          client-forum-api
64fdc02e7306   nginx                    "/docker-entrypoint.…"   37 seconds ago   Up 36 seconds                      0.0.0.0:80->80/tcp, :::80->80/tcp   proxy-forum-api
2aa62dd5f35b   app-forum-api            "java -Xms128M -Xmx1…"   38 seconds ago   Up 37 seconds (health: starting)                                       app-forum-api
05941ffc7974   mysql:5.7                "docker-entrypoint.s…"   39 seconds ago   Up 37 seconds                                                          mysql-forum-api
2c025f59b117   redis                    "docker-entrypoint.s…"   41 seconds ago   Up 40 seconds                                                          redis-forum-api
fernando@debian10x64:~$

~~~~









- Efetuando curl para o ip do host:

<http://192.168.0.113/>

fernando@debian10x64:~$ curl http://192.168.0.113/
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
fernando@debian10x64:~$






- Efetuando curl para o ip do host, no path tópicos:

<http://192.168.0.113/topicos>

~~~~bash
fernando@debian10x64:~$ curl http://192.168.0.113/topicos
{"content":[{"id":3,"titulo":"Duvida 3","mensagem":"Tag HTML","dataCriacao":"2019-05-05T17:00:00"},{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T16:00:00"},{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T15:00:00"}],"pageable":{"sort":{"sorted":true,"unsorted":false,"empty":false},"pageNumber":0,"pageSize":10,"offset":0,"paged":true,"unpaged":false},"totalPages":1,"totalElements":3,"last":true,"sort":{"sorted":true,"unsorted":false,"empty":false},"first":true,"numberOfElements":3,"size":10,"number":0,"empty":false}
fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$
~~~~





- Efetuando curl para o ip do host, no path tópicos2:

<http://192.168.0.113/topicos/2>
curl http://192.168.0.113/topicos/2

~~~~bash
fernando@debian10x64:~$ curl http://192.168.0.113/topicos/2
{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T19:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$
fernando@debian10x64:~$
~~~~




- Efetuando curl no path do Prometheus, com as métricas:
http://192.168.0.113/metrics



- Efetuando curl no path do Health:
http://192.168.0.113/health



- Efetuando curl no path do info:
http://192.168.0.113/info




- Página do Prometheus não abre:
http://192.168.0.113:9090/


- Verificado que o Container do Prometheus tá com status "Restarting (2) 52 seconds ago":

~~~~bash
fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED          STATUS                          PORTS                               NAMES
83e10d4ed708   prom/prometheus:latest   "/bin/prometheus --c…"   11 minutes ago   Restarting (2) 52 seconds ago                                       prometheus-forum-api
ee9f972a9709   client-forum-api         "/scripts/client.sh"     11 minutes ago   Up 11 minutes                                                       client-forum-api
64fdc02e7306   nginx                    "/docker-entrypoint.…"   11 minutes ago   Up 11 minutes                   0.0.0.0:80->80/tcp, :::80->80/tcp   proxy-forum-api
2aa62dd5f35b   app-forum-api            "java -Xms128M -Xmx1…"   11 minutes ago   Up 11 minutes (unhealthy)                                           app-forum-api
05941ffc7974   mysql:5.7                "docker-entrypoint.s…"   11 minutes ago   Up 11 minutes                                                       mysql-forum-api
2c025f59b117   redis                    "docker-entrypoint.s…"   11 minutes ago   Up 11 minutes                                                       redis-forum-api
fernando@debian10x64:~$
~~~~



- Erros nos logs do container:

~~~~bash
ts=2022-11-15T02:53:32.251Z caller=main.go:564 level=info vm_limits="(soft=unlimited, hard=unlimited)"
ts=2022-11-15T02:53:32.251Z caller=query_logger.go:91 level=error component=activeQueryTracker msg="Error opening query log file" file=/prometheus/queries.active err="open /prometheus/queries.active: permission denied"
panic: Unable to create mmap-ed active query log

goroutine 1 [running]:
github.com/prometheus/prometheus/promql.NewActiveQueryTracker({0x7ffe26e49ef7, 0xb}, 0x14, {0x3b92b80, 0xc0009356d0})
        /app/promql/query_logger.go:121 +0x3cd
main.main()
        /app/cmd/prometheus/main.go:618 +0x6973
fernando@debian10x64:~$
~~~~



- ERRO:
Error opening query log file file=/prometheus/queries.active err=open /prometheus/queries.active: permission denied




- SOLUÇÃO:
<https://github.com/prometheus/prometheus/issues/5976>
user: "1000:1000"

In order to fix the issue, you need to set UID/GID in the container:

> echo $UID
1000

Define in docker-compose:

~~~~yaml
# docker-compose.yml

 services:
  prometheus:
    image: prom/prometheus
    user: "1000:1000"
~~~~


Or in Dockerfile:

~~~~Dockerfile
# Dockerfile

USER 1000:1000
~~~~




- Subindo stack
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
docker-compose up -d


~~~~bash
fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED          STATUS                      PORTS                                       NAMES
07aae7905cd9   prom/prometheus:latest   "/bin/prometheus --c…"   5 seconds ago    Up 4 seconds                0.0.0.0:9090->9090/tcp, :::9090->9090/tcp   prometheus-forum-api
ee9f972a9709   client-forum-api         "/scripts/client.sh"     18 minutes ago   Up 18 minutes                                                           client-forum-api
64fdc02e7306   nginx                    "/docker-entrypoint.…"   18 minutes ago   Up 18 minutes               0.0.0.0:80->80/tcp, :::80->80/tcp           proxy-forum-api
2aa62dd5f35b   app-forum-api            "java -Xms128M -Xmx1…"   18 minutes ago   Up 18 minutes (unhealthy)                                               app-forum-api
05941ffc7974   mysql:5.7                "docker-entrypoint.s…"   18 minutes ago   Up 18 minutes                                                           mysql-forum-api
2c025f59b117   redis                    "docker-entrypoint.s…"   18 minutes ago   Up 18 minutes                                                           redis-forum-api
fernando@debian10x64:~$
~~~~

docker container exec -ti prometheus-forum-api sh

fernando@debian10x64:~$ docker container exec -ti prometheus-forum-api sh
/prometheus $
/prometheus $
/prometheus $ id
uid=1000 gid=1000
/prometheus $










- Página do Prometheus:
http://192.168.0.113:9090/


# PENDENTE:
- Tratar erro na página do Prom
Warning: Error fetching server time: Detected 284.9360001087189 seconds time difference between your browser and the server. Prometheus relies on accurate time and time drift might cause unexpected query results.





Warning: Error fetching server time: Detected 286.87800002098083 seconds time difference between your browser and the server. Prometheus relies on accurate time and time drift might cause unexpected query results.

