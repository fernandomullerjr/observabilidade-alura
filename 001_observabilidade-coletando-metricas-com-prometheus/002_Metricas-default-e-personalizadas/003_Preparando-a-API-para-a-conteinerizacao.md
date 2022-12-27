


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 002 - Aula 03 Preparando a API para a conteinerização"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Preparando a API para a conteinerização


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Transcrição 
Transcrição
[00:00] Essa aula vai ser rápida, é o final do capítulo 1. Vamos preparar essa aplicação para ela ser empacotada e rodar junto com a stack do Docker Compose.

[00:16] O que vamos fazer é o seguinte, vou fechar o “src/main/java” e vou no “src/main/resources”. No application.prod.properties, vou modificar o redis.host. Aqui está para localhost, mas esse contêiner se chama redis-forum-api.

[00:50] A linha que eu modifiquei foi a 5, agora vou modificar a linha 10. Em spring.datasource, vou trocar o localhost pelo mysql-forum-api.

[01:10] Bem simples, temos que fazer essa mudança porque, a partir do capítulo II, essa API vai conectar tanto no MySQL quanto no Redis por uma rede interna do Docker. Então, o nosso Docker Compose vai mudar.

[01:28] Na próxima aula, você já vai ter um Docker Compose diferente na plataforma, para baixar no capítulo 2, e esse Docker Compose já vai subir uma stack maior, ele vai subir o Redis e o MySQL internamente, o próprio Docker Compose vai fazer o build do contêiner da aplicação e, além disso, ele vai subir também o Prometheus.

[01:58] Eu vou explicar tudo isso com calma para que você possa entender o processo. Agora, com relação à aplicação, a conteinerização dela é bem simples.

[02:11] Vamos dar uma olhada nisso na próxima aula e o que precisamos mudar aqui, como pré-requisito, são apenas esses dois valores. Podemos fechar a aplicação, não vamos "startar" ela por aqui, vou ver o que está executando da stack.

[02:32] A aplicação ainda estava executando, eu não vou mais precisar, e vou "rebuildar" o pacote. Vamos criar um artefato JAR que agora já possui essas configurações, ele já está configurado para conectar no contêiner, não mais no localhost.

[02:55] Bem simples, é só esperar o build ser concluído. Está certo, foi "buildado" com sucesso. A partir de agora, já vamos trabalhar de uma forma diferente, vamos focar nas ferramentas-chaves do curso – nesse caso, o Prometheus, o Grafana e, posteriormente, o Alert Manager.

[03:21] Tendo esse artefato "buildado", é só seguir em frente e nos vemos no capítulo II. Até a próxima aula.




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Preparando a API para a conteinerização


- Necessário ajustar o arquivo "application-prod.properties":
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/resources/application-prod.properties

- Trocar o host do redis de localhost para o nome do container
- Trocar o host do MYSQL de localhost para o nome do container

de:

~~~~conf
# Redis cache config 
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379

# datasource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/forum
spring.datasource.username=forum
spring.datasource.password=Bk55yc1u0eiqga6e
~~~~



para:

~~~~conf
# Redis cache config 
spring.cache.type=redis
spring.redis.host=redis-forum-api
spring.redis.port=6379

# datasource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://mysql-forum-api:3306/forum
spring.datasource.username=forum
spring.datasource.password=Bk55yc1u0eiqga6e
~~~~




- Buildando aplicação e validando se está tudo ok

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


[02:32] A aplicação ainda estava executando, eu não vou mais precisar, e vou "rebuildar" o pacote. Vamos criar um artefato JAR que agora já possui essas configurações, ele já está configurado para conectar no contêiner, não mais no localhost.


- Buildou

~~~~bash
2022-11-13 15:48:57.524  INFO 3962 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO]
[INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ forum ---
[INFO] Building jar: /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/target/forum.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:2.3.1.RELEASE:repackage (repackage) @ forum ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  47.084 s
[INFO] Finished at: 2022-11-13T15:48:59-03:00
[INFO] ------------------------------------------------------------------------
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$

~~~~