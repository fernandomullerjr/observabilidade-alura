# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula 01 Métricas Logged Users e Auth Errors "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 01 Métricas Logged Users e Auth Errors


# Transcrição
[00:00] Vamos dar sequência no nosso curso, agora vamos trabalhar criando mais dois painéis.

[00:10] Vamos em “Add panel”. Agora vamos trabalhar com uma métrica que diz respeito ao número de usuários autenticados no último minuto.

[00:25] No campo "metrics browser", vou colocar user, já conseguimos buscar, vai ser auth_user_success_total. No caso, se eu colocar essa métrica aqui, ela já vai me trazer um valor, só que não é o valor que eu quero demonstrar no meu dash, eu quero um painel com números.

[00:48] O que eu preciso aqui? Eu quero olhar para o último minuto, não quero olhar a totalidade, não quero olhar um contador que sempre vai estar sendo incrementado, porque ele não vai refletir a realidade da minha API.

[01:03] Alguns usuários já não vão estar mais logados, então quero pegar somente a autenticação do último minuto para entender quantos usuários estão autenticados naquele minuto.

[01:15] Para esse caso, é interessante, primeiro, se eu definir o meu range time, eu não vou ter informações sendo plotadas aqui. Por quê? Porque eu preciso de um dado Scalar ou de um instant vector.

[01:31] Quando eu trabalho com um tempo especificado na minha consulta, eu formo um range vector e um range vector não tem como formar um gráfico. Então, eu vou ter que trabalhar com uma função.

[01:46] Vou utilizar o increase porque quero a taxa de crescimento dessa métrica no último minuto, increase(auth_user_success_total[1m]). Você pode se perguntar: “Você vai ter que utilizar o sum para fazer uma agregação?”, não é necessário nesse caso, o valor vai ser o mesmo, porque estou trabalhando apenas com uma série temporal, a partir do momento que uso o increase.

[02:09] Então, eu não preciso fazer uma agregação nesse caso. Está aqui, a visualização que eu vou escolher é “Stat” também. Estou com “9.82”, a taxa de crescimento de autenticação foi essa.

[02:28] É um número quebrado, vou arredondar isso. O título que vamos usar é “USERS LOGGED”, “usuários logados”. A descrição é “Usuários logados no último minuto”.

[02:50] O cálculo vai ser o último valor não nulo. O campo é numérico. A coloraçãom vou tirar o “Graph mode” para ficar só o número. Em “Standard options”, a unidade que vamos utilizar é uma unidade “Short” mesmo.

[03:14] Se eu quiser tirar um número decimal, eu posso colocar um valor “0” e ele vai arredondar. Essa métrica é uma aproximação do que temos de usuários autenticados no último minuto.

[03:28] Não é o valor exato do cálculo que o Prometheus faz olhando a taxa de crescimento do último minuto porque ele se baseia em uma série temporal, então o dado sempre vai ser um número quebrado, sempre vai ser um float.

[03:46] Não tenho “Threshold” aqui, vou manter um verde mais escuro. Então, já temos um painel com o número de usuários logados no último minuto. Vou formatá-lo, vou diminuir, está muito grande.

[04:09] Assim está mais interessante, pronto. Vamos adicionar mais um painel. Esse painel vai ser uma métrica relacionada aos erros de autenticação, é auth_user_error_total.

[04:23] Temos o mesmo paradigma de antes, eu quero pegar o meu último minuto e vou trabalhar com a minha taxa de crescimento, vai ser increase para [1m] e vamos seguir com o mesmo trabalho que fizemos na métrica anterior.

[04:47] Vai ser uma visualização “Stat”, o título do painel vou colocar como “AUTH ERRORS”, vou colocar como “Erros de autenticação no último minuto”, essa é a descrição. Quando alguém posicionar o mouse sobre a métrica, você vai ver a descrição, então é legal para alguém recém-chegado, que não entende a composição do dash, para se encontrar.

[05:19] O cálculo vai ser em cima do último valor não nulo; vou tirar a coloração gráfica para manter só o número; na unidade, vou trabalhar também com o “Short” – se eu colocar “None”, ele não muda nada, com valor de “String”, ele iria colocar todo o número completo, o que não é interessante, então vamos manter o “Short”.

[05:52] Vou tirar a casa decimal e aqui, sim, vou colocar um “Threshold”. Vamos imaginar que, em 1 minuto, se tivermos 50 erros de autenticação, teremos um problema. Como não vamos simular 50 erros de autenticação em 1 minuto, eu vou colocar aqui que 5 erros de autenticação é uma situação um pouco estranha.

[06:26] Vou abrir de novo o “Threshold”. E que 10 erros de autenticação já significa que talvez exista um problema no nosso database, porque a aplicação tem uma regra de negócio que vai validar o que o usuário enviou com os dados que estão no database, então ela vai fazer uma consulta para devolver um token para esse usuário.

[06:52] Vou deixar o “Threshold” dessa forma e está feito. Agora, já temos quatro painéis, são bem simples. deixa só eu dar uma reforçada nessa cor verde e trabalhar com um verde mais escuro.

[07:12] Até dado momento, está tudo tranquilo, já vamos começar a trabalhar com algumas coisas mais complexas, mas, por hora, vamos seguindo dessa maneira.

[07:22] Na próxima aula, já vamos trabalhar com dois outros painéis: um que reflete as informações de log, o log level, e outro que vai olhar para o pool de conexões da JDBC. Nos vemos na próxima aula.











# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 01 Métricas Logged Users e Auth Errors

[00:00] Vamos dar sequência no nosso curso, agora vamos trabalhar criando mais dois painéis.

[00:10] Vamos em “Add panel”. Agora vamos trabalhar com uma métrica que diz respeito ao número de usuários autenticados no último minuto.

[00:25] No campo "metrics browser", vou colocar user, já conseguimos buscar, vai ser auth_user_success_total. No caso, se eu colocar essa métrica aqui, ela já vai me trazer um valor, só que não é o valor que eu quero demonstrar no meu dash, eu quero um painel com números.

auth_user_success_total



- Subindo aplicação:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
sh start.sh


- Criado post na Alura:
https://cursos.alura.com.br/forum/topico-metricas-logged-users-e-auth-errors-metrics-broser-nao-exibe-auth_user_success_total-262187
<https://cursos.alura.com.br/forum/topico-metricas-logged-users-e-auth-errors-metrics-broser-nao-exibe-auth_user_success_total-262187>

Oi,
estou com problemas no módulo   002 - Aula 01 Métricas Logged Users e Auth Errors.

Aos 25segundos de aula, o professor fala sobre a métrica auth_user_success_total
Porém ao digitar ela o Metrics Browser não encontra ela.
Colocando apenas "user", também não acha nada relacionado.

O que pode estar ocorrendo?




- Métrica não existe na página de métricas do Prometheus:
auth_user_success_total
http://192.168.92.129/metrics
<http://192.168.92.129/metrics>



# PENDENTE
- Resolver problema, porque o Metrics Browser do Grafana não exibe a métrica auth_user_success_total.
- Verificar retorno no Fórum e no Discord da Alura.
- Verificar porque o endpoint do Actuator não tá funcionando:
    http://192.168.92.129:8080/actuator
    necessário subir aplicação, para o Actuator subir
- Verificar se o Prometheus tá com problema em fazer o Scrape, pois não tem essa métrica na página de métricas do Prometheus:
    http://192.168.92.129/metrics
    idéia do professor:
    https://cursos.alura.com.br/forum/topico-metrica-http_server_requests_seconds_bucket-nao-encontrada-232308
- Revisar a aula:
    /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/002_Metricas-default-e-personalizadas/002_Metricas-personalizadas.md





# #############################################################################################################################################################
# Dia 20/12/2022

ao Buildar e Subir a aplicação

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


- Subindo aplicação:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
sh start.sh

E tentar acessar via 
http://192.168.92.129:8080/topicos/1
http://192.168.92.129:8080/topicos/

No navegador surgem os erros:
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.
Tue Dec 20 13:32:43 BRT 2022
There was an unexpected error (type=Internal Server Error, status=500).


E na console do terminal surgem os erros:

2022-12-20 13:38:25.250  WARN 38987 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2022-12-20 13:38:25.250 ERROR 38987 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2022-12-20 13:38:25.253 ERROR 38987 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection] with root cause

2022-12-20 13:32:43.574 ERROR 38987 --- [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.data.redis.RedisConnectionFailureException: Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to redis-forum-api:6379] with root cause




Verificado que a stack subida era antiga, do curso1
subindo stack do curso2

E na console do terminal, onde foi subida a aplicação via start.sh, surgem os erros:

2022-12-20 13:53:09.092  WARN 66193 --- [nio-8080-exec-9] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2022-12-20 13:53:09.092 ERROR 66193 --- [nio-8080-exec-9] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2022-12-20 13:53:09.094 ERROR 66193 --- [nio-8080-exec-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection] with root cause

2022-12-20 14:00:31.795  WARN 66193 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2022-12-20 14:00:31.795 ERROR 66193 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2022-12-20 14:00:31.798 ERROR 66193 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection] with root cause

2022-12-20 14:20:43.339  INFO 123081 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 4412 ms
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.


2022-12-20 14:21:02.350 ERROR 123081 --- [         task-1] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Exception during pool initialization.

com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure




- Container do MYSQL acho que não está bindando a porta 3306 a principio:

fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$ ss -tulp | grep 3306
fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$

fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$ docker ps | grep mysql
4a0bab2605d6   mysql:5.7                "docker-entrypoint.s…"   44 minutes ago   Up 3 minutes                                                            mysql-forum-api
fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$



- Exemplo antigo, que tem a porta bindada:

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



- Troquei
de:
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
para:
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

nos arquivos
application-prod.properties

- Seguiu com erro:

2022-12-20 14:50:00.236  WARN 47380 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2022-12-20 14:50:00.237 ERROR 47380 --- [nio-8080-exec-1] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2022-12-20 14:50:00.269 ERROR 47380 --- [nio-8080-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection] with root cause


- Efetuando rollback


- Adicionando ao Dockerfile do Container do app
/home/fernando/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app/Dockerfile
RUN apk update
RUN apk upgrade
RUN apk --no-cache add curl
RUN apk add busybox-extras


git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula 01 Métricas Logged Users e Auth Errors- TSHOOT "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$ docker-compose up -d --build
Building app-forum-api


/ $ telnet mysql-forum-api 3306
J
5.7.40fkk d*}8NH}EbBmysql_native_passwordxterm-256color^C
Console escape. Commands are:

 l      go to line mode
 c      go to character mode
 z      suspend telnet
 e      exit telnet
/ $



fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$ docker network ls
NETWORK ID     NAME                       DRIVER    SCOPE
d9b75a5b78b1   bridge                     bridge    local
2a5bf18701f7   conteudo_01_api            bridge    local
ded955dd913f   conteudo_01_cache          bridge    local
e9be77774a1e   conteudo_01_database       bridge    local
270e555a64cb   conteudo_01_monit          bridge    local
297584849e73   conteudo_01_proxy          bridge    local
4ea8eec63a81   host                       host      local
3ae049e03484   minikube                   bridge    local
cc85819ababc   none                       null      local
487fed780399   prometheus-grafana_local   bridge    local
fernando@debian10x64:~/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app$




/home/fernando/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01/app/src/main/resources/application-prod.properties
# datasource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://mysql-forum-api:3306/forum
spring.datasource.username=forum
spring.datasource.password=Bk55yc1u0eiqga6e




# PENDENTE
- Testar comunicação do Container do app com o mysql, foi instalado o curl e telnet no Container do App. Revisar networks e config do application-prod.
- Resolver problema, porque o Metrics Browser do Grafana não exibe a métrica auth_user_success_total.
- Verificar retorno no Fórum e no Discord da Alura.
- Verificar porque o endpoint do Actuator não tá funcionando:
    http://192.168.92.129:8080/actuator
    necessário subir aplicação, para o Actuator subir
- Verificar se o Prometheus tá com problema em fazer o Scrape, pois não tem essa métrica na página de métricas do Prometheus:
    http://192.168.92.129/metrics
    idéia do professor:
    https://cursos.alura.com.br/forum/topico-metrica-http_server_requests_seconds_bucket-nao-encontrada-232308
- Revisar a aula:
    /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/002_Metricas-default-e-personalizadas/002_Metricas-personalizadas.md







# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
## Dia 25/12/2022


- TSHOOT sobre o porque da métrica "auth_user_success_total" não aparecer no Metrics Browser do Grafana.
continuando


2022-12-25 13:50:59.766  WARN 69771 --- [nio-8080-exec-5] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 0, SQLState: 08S01
2022-12-25 13:50:59.767 ERROR 69771 --- [nio-8080-exec-5] o.h.engine.jdbc.spi.SqlExceptionHelper   : Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.
2022-12-25 13:50:59.775 ERROR 69771 --- [nio-8080-exec-5] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection] with root cause



- Containers OK

fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED      STATUS                    PORTS                                       NAMES
8f3f8f25614d   app-forum-api            "java -Xms128M -Xmx1…"   4 days ago   Up 59 minutes (healthy)                                               app-forum-api
944639fff4dc   client-forum-api         "/scripts/client.sh"     4 days ago   Up 59 minutes                                                         client-forum-api
5321d2036731   grafana/grafana          "/run.sh"                4 days ago   Up 59 minutes             0.0.0.0:3000->3000/tcp, :::3000->3000/tcp   grafana-forum-api
aae714aa787e   prom/prometheus:latest   "/bin/prometheus --c…"   4 days ago   Up 59 minutes             0.0.0.0:9090->9090/tcp, :::9090->9090/tcp   prometheus-forum-api
4ce737afcedd   nginx                    "/docker-entrypoint.…"   4 days ago   Up 59 minutes             0.0.0.0:80->80/tcp, :::80->80/tcp           proxy-forum-api
1f327e29cf39   mysql:5.7                "docker-entrypoint.s…"   4 days ago   Up 59 minutes                                                         mysql-forum-api
b837c42537de   redis                    "docker-entrypoint.s…"   4 days ago   Up 59 minutes                                                         redis-forum-api
fernando@debian10x64:~$



http://192.168.92.129:80/topicos
http://192.168.92.129:80/topicos/1


curl -v http://192.168.92.129:80/topicos
curl -v http://192.168.92.129:80/topicos/1

fernando@debian10x64:~$ curl -v http://192.168.92.129:80/topicos
* Expire in 0 ms for 6 (transfer 0x55fc12415fb0)
*   Trying 192.168.92.129...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55fc12415fb0)
* Connected to 192.168.92.129 (192.168.92.129) port 80 (#0)
> GET /topicos HTTP/1.1
> Host: 192.168.92.129
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 25 Dec 2022 17:04:40 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
<
* Connection #0 to host 192.168.92.129 left intact
{"content":[{"id":3,"titulo":"Duvida 3","mensagem":"Tag HTML","dataCriacao":"2019-05-05T20:00:00"},{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T19:00:00"},{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T18:00:00"}],"pageable":{"sort":{"unsorted":false,"sorted":true,"empty":false},"pageNumber":0,"pageSize":10,"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":1,"totalElements":3,"sort":{"unsorted":false,"sorted":true,"empty":false},"first":true,"numberOfElements":3,"size":10,"number":0,"empty":false}fernando@debian10x64:~$ curl -v http://192.168.92.129:80/topicos/1
* Expire in 0 ms for 6 (transfer 0x562e76100fb0)
*   Trying 192.168.92.129...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x562e76100fb0)
* Connected to 192.168.92.129 (192.168.92.129) port 80 (#0)
> GET /topicos/1 HTTP/1.1
> Host: 192.168.92.129
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 25 Dec 2022 17:04:40 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
<
* Connection #0 to host 192.168.92.129 left intact
{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T18:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$
fernando@debian10x64:~$



- RESOLVIDO
Problema de não conseguir acessar os endpoints da API foi resolvido.
Eu estava tentando acessar eles usando a porta 8080, que é usada na parte inicial do curso, quando a aplicação é iniciada via script, ainda não tem um Container.
Como estou na fase onde a aplicação já tem um Container, verifiquei que esse container usa a porta 80 ao invés da 8080, então só ajustei a porta e consegui acessar os endpoints da minha API de tópicos.






# PENDENTE
- Testar comunicação do Container do app com o mysql, foi instalado o curl e telnet no Container do App. Revisar networks e config do application-prod.
- Resolver problema, porque o Metrics Browser do Grafana não exibe a métrica auth_user_success_total.
- Verificar retorno no Fórum e no Discord da Alura.
- Verificar porque o endpoint do Actuator não tá funcionando:
    http://192.168.92.129:8080/actuator
    necessário subir aplicação, para o Actuator subir
- Verificar se o Prometheus tá com problema em fazer o Scrape, pois não tem essa métrica na página de métricas do Prometheus:
    http://192.168.92.129/metrics
    idéia do professor:
    https://cursos.alura.com.br/forum/topico-metrica-http_server_requests_seconds_bucket-nao-encontrada-232308
- Revisar a aula:
    /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/002_Metricas-default-e-personalizadas/002_Metricas-personalizadas.md





- Acessei algumas vezes os Endpoints da API de tópicos.
- Agora temos métricas de "auth_user_success_total" na página:
http://192.168.92.129/metrics
<http://192.168.92.129/metrics>

- No dashboard do Prometheus, ao fazer uma query:
auth_user_success_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api"}
	580





RESOLVIDO.


Meu container do app estava com status  (health: starting):

```
fernando@debian10x64:~$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED          STATUS                             PORTS                               NAMES
83e10d4ed708   prom/prometheus:latest   "/bin/prometheus --c…"   36 seconds ago   Restarting (2) 1 second ago                                            prometheus-forum-api
ee9f972a9709   client-forum-api         "/scripts/client.sh"     36 seconds ago   Up 35 seconds                                                          client-forum-api
64fdc02e7306   nginx                    "/docker-entrypoint.…"   37 seconds ago   Up 36 seconds                      0.0.0.0:80->80/tcp, :::80->80/tcp   proxy-forum-api
2aa62dd5f35b   app-forum-api            "java -Xms128M -Xmx1…"   38 seconds ago   Up 37 seconds (health: starting)                                       app-forum-api
05941ffc7974   mysql:5.7                "docker-entrypoint.s…"   39 seconds ago   Up 37 seconds                                                          mysql-forum-api
2c025f59b117   redis                    "docker-entrypoint.s…"   41 seconds ago   Up 40 seconds                                                          redis-forum-api
fernando@debian10x64:~$

```


E eu tinha aqueles problemas para acessar os endpoints da API de tópicos.
Problema de não conseguir acessar os endpoints da API foi resolvido.
Eu estava tentando acessar eles usando a porta 8080, que é usada na parte inicial do curso, quando a aplicação é iniciada via script, ainda não tem um Container.
Como estou na fase onde a aplicação já tem um Container, verifiquei que esse container usa a porta 80 ao invés da 8080, então só ajustei a porta e consegui acessar os endpoints da minha API de tópicos.

Endpoints OK:

```

fernando@debian10x64:~$ curl -v http://192.168.92.129:80/topicos
* Expire in 0 ms for 6 (transfer 0x55fc12415fb0)
*   Trying 192.168.92.129...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55fc12415fb0)
* Connected to 192.168.92.129 (192.168.92.129) port 80 (#0)
> GET /topicos HTTP/1.1
> Host: 192.168.92.129
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 25 Dec 2022 17:04:40 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
<
* Connection #0 to host 192.168.92.129 left intact
{"content":[{"id":3,"titulo":"Duvida 3","mensagem":"Tag HTML","dataCriacao":"2019-05-05T20:00:00"},{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T19:00:00"},{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T18:00:00"}],"pageable":{"sort":{"unsorted":false,"sorted":true,"empty":false},"pageNumber":0,"pageSize":10,"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":1,"totalElements":3,"sort":{"unsorted":false,"sorted":true,"empty":false},"first":true,"numberOfElements":3,"size":10,"number":0,"empty":false}fernando@debian10x64:~$ curl -v http://192.168.92.129:80/topicos/1
* Expire in 0 ms for 6 (transfer 0x562e76100fb0)
*   Trying 192.168.92.129...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x562e76100fb0)
* Connected to 192.168.92.129 (192.168.92.129) port 80 (#0)
> GET /topicos/1 HTTP/1.1
> Host: 192.168.92.129
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 25 Dec 2022 17:04:40 GMT
< Content-Type: application/json
< Transfer-Encoding: chunked
< Connection: keep-alive
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
<
* Connection #0 to host 192.168.92.129 left intact
{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T18:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$

```



Sobre a métrica "auth_user_success_total" que eu não encontrava no Grafana:
agora que consegui resolver os problemas com os Endpoints da API, acessei eles algumas vezes, consequentemente, gerou métricas de "auth_user_success_total" no meu endpoint de métricas:
http://192.168.92.129/metrics

Fui verificar no Grafana, quando adiciono um panel e tento buscar pela métrica "auth_user_success_total" no Metrics Browser, agora consigo encontrar ela!
Tudo certo.










# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
## Retomando o curso

[00:00] Vamos dar sequência no nosso curso, agora vamos trabalhar criando mais dois painéis.

[00:10] Vamos em “Add panel”. Agora vamos trabalhar com uma métrica que diz respeito ao número de usuários autenticados no último minuto.

[00:25] No campo "metrics browser", vou colocar user, já conseguimos buscar, vai ser auth_user_success_total. No caso, se eu colocar essa métrica aqui, ela já vai me trazer um valor, só que não é o valor que eu quero demonstrar no meu dash, eu quero um painel com números.

[00:48] O que eu preciso aqui? Eu quero olhar para o último minuto, não quero olhar a totalidade, não quero olhar um contador que sempre vai estar sendo incrementado, porque ele não vai refletir a realidade da minha API.

[01:03] Alguns usuários já não vão estar mais logados, então quero pegar somente a autenticação do último minuto para entender quantos usuários estão autenticados naquele minuto.






[01:15] Para esse caso, é interessante, primeiro, se eu definir o meu range time, eu não vou ter informações sendo plotadas aqui. Por quê? Porque eu preciso de um dado Scalar ou de um instant vector.

[01:31] Quando eu trabalho com um tempo especificado na minha consulta, eu formo um range vector e um range vector não tem como formar um gráfico. Então, eu vou ter que trabalhar com uma função.

- Tentando dessa forma:
auth_user_success_total[1m]

- Apresenta o erro:
    bad_data: invalid expression type "range vector" for range query, must be Scalar or instant Vector: details: 


[01:46] Vou utilizar o increase porque quero a taxa de crescimento dessa métrica no último minuto, increase(auth_user_success_total[1m]). Você pode se perguntar: “Você vai ter que utilizar o sum para fazer uma agregação?”, não é necessário nesse caso, o valor vai ser o mesmo, porque estou trabalhando apenas com uma série temporal, a partir do momento que uso o increase.

- Necessário usar a função increase:
increase(auth_user_success_total[1m])