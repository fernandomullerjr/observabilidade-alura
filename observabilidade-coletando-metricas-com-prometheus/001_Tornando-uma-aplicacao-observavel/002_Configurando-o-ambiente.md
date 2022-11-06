
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula 02 - Configurando o ambiente"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 - Configurando o ambiente

Transcrição
[00:00] Seja bem-vindo à primeira aula do capítulo 1. Nessa aula, vamos configurar o ambiente e eu também vou explicar algumas peculiaridades desse ambiente para vocês.

[00:16] Vou começar importando o projeto. Estou com o Eclipse aberto, ele é uma dependência para que você siga o capítulo 1 sem nenhum problema, mas isso não significa que você não possa utilizar outra IDE. Pode utilizar outra IDE, eu estou usando o Eclipse simplesmente porque ele tem a funcionalidade de trazer as dependências que eu preciso para essa aplicação, como o Maven.

[00:41] Se você está fazendo isso em outra IDE, pode seguir com ela, mas eu recomendo que você utilize o Eclipse. No painel à esquerda, vou em “Import Projects...” e vou procurar por um projeto Maven já existente.

[00:56] Vou em “Next > Browse...”. Em “Browse”, vou encontrar o conteúdo descompactado do pacote (prometheus-grafana) que está disponível na plataforma – que você já deve ter feito download e descompactado.

[01:12] Por conveniência, estou usando o path do workdir do Eclipse. Você não é obrigado a fazer isso, pode utilizar outro lugar, não vai gerar problema, mas estou fazendo dessa forma.

[01:28] Dentro do prometheus-grafana, você vai no subdiretório app, em que existe um arquivo com o XML que o Eclipse vai reconhecer automaticamente como sendo o arquivo de um projeto.

[01:43] Dar “Open”, está aqui, vou dar “Finish” e ele vai importar o projeto para mim. Essa é a nossa API, a “forum”. Então, o primeiro passo foi feito com êxito, agora vamos para o segundo passo.

[02:58] Vou abrir o meu Terminal, estou usando o Linux. Isso é agnóstico, você pode usar em qualquer sistema operacional. As dependências, você vai cumprir e instalar em qualquer sistema operacional, seja Windows, Linux ou MacOS.

[02:15] Vou entrar onde a aplicação está descompactada, onde está toda a stack. Aqui, você vai encontrar o diretório mysql, o docker-compose e o app. Esse grafana e o nginx você não terá eles nesse momento, não se preocupe com isso, é só lixo residual que ficou nesse pacote que estou utilizando.

[02:43] O que vai está na plataforma e você baixou não tem esse conteúdo, não se preocupe com isso. Vamos começar olhando para o conteúdo que está dentro de mysql. É um script SQL e esse script vai ser executado quando o contêiner do MySQL subir para popular a base.

[03:09] Basicamente, essa é a funcionalidade dele. Agora vamos dar uma olhada no docker-compose. É esse cara que vai subir a stack que vamos utilizar.

[03:21] Eu tenho dois serviços aqui, o redis-forum-api e o mysql-forum-api. Esses dois contêineres vão fazer bind de porta com a sua máquina. Se você tiver uma instância local rodando MySQL, vai gerar conflito.

[03:41] Se você tiver o serviço MySQL configurado, derruba ele. Se for qualquer serviço ocupando a porta 3306 TCP, você deve derrubar esse serviço para que esse bind ocorra sem gerar nenhum problema.

[03:57] A mesma coisa para o Redis, que vai rodar na 6379, a porta padrão do Redis fazendo bind para a 6379 da sua máquina. Ele vai estar em uma rede local do Docker Compose, em uma rede do Docker, mas ele vai "bindar" na sua porta.

[04:15] Aqui tem as configurações do MySQL e a dependência dele. Ele precisa que o Redis suba primeiro para que ele possa subir depois. Essas são as dependências básicas de comunicação dessa API, ela depende disso, por isso você tem essa stack no capitulo 1 disponibilizada para você.

[04:41] Só um detalhe, vamos fazer uma equivalência aqui. Antes de subir a stack, é importante que você tenha o Docker instalado. Vou rodar um docker --version só para fazermos uma equivalência de versão.

[04:58] "Docker version 20.10.7, built 20.10.7-0ubuntu5-18.04.04.3" - essa é a versão do Docker que estou utilizando e também tem a versão do docker-compose --version: "docker-compose version 1.29.2, built 5becea4c". É importante que você tenha uma versão igual ou superior à minha. Se for muito superior, se tiver mudado alguma coisa, você pode encontrar algum conflito, mas aí você pode comunicar o fórum ou pode fazer downgrade da versão e resolver o problema.

[05:23] Sem crise, não é nada grave, bem simples de resolver e difícil de acontecer essa quebra de compatibilidade. Temos que ter o Java, vou dar um java -version só para verificarmos.

[05:39] A versão 1.8, é disso que você precisa. Tendo feito isso, eu posso subir a stack. docker-compose. Eu não vou subir em daemon agora, eu vou subir prendendo o meu terminal para que você possa acompanhar a subida.

[06:03] Para você, isso vai demorar um pouco mais. Tive um erro aqui, esse erro é até previsível, eu não subi o serviço do Docker. Sem o serviço do Docker, logicamente, o Docker Compose não vai conseguir subir os contêineres.

[06:20] Então, vamos lá, sudo systemctl start docker docker.socket. No Windows, o processo é outro, ele tem, inclusive, um cliente de interface gráfica que facilita a sua vida. Você vai lá e dá um start no serviço.

[06:47] Vou entrar com a minha senha e agora é só aguardar o serviço subir para subir a stack. Vamos aguardar um pouco, às vezes demora mesmo. Vou dar uma olhada no status do serviço, com sudo systemctl status docker docker.socket.

[07:09] Verificar se subiu com êxito. Preciso esperar um pouco. Está aí, “active running”, subiu tudo e agora posso dar o meu docker-compose up. Ele vai criar as redes, vai criar os contêineres.

[07:30] No caso de vocês, o processo de criação dos contêineres vai demorar um pouco mais porque o Docker vai fazer download do Redis na última versão e do MySQL 5.7, das imagens base para poder rodar os contêineres.

[07:48] Está aqui, ele está subindo ainda, não concluiu a subida, é só esperar um pouco. Com isso, temos a base necessária para poder rodar a nossa aplicação.

[08:05] Está terminando aqui. Só esperar, subiu, deu até o versionamento aqui e ele está esperando por conexões. Eu vou abrir o meu Terminal e vou entrar em app/, com cd app/. Em app/, vou rodar o comando mvn clean package.

[08:43] É o Maven, estou chamando o Maven para fazer um build da aplicação e verificar se está tudo certo com esse pacote que eu baixei. Você pode fazer isso lá pelo Eclipse também, ele tem essa funcionalidade, mas eu prefiro fazer aqui no Terminal.

[09:05] Ele vai executar uma série de testes da aplicação e, se tudo passar, se estiver tudo certo, ele vai concluir esse build e vai gerar um artefato JAR. Passou, ele rodou quatro testes, nenhuma falha, nenhum erro, passou tudo, "buildou" com sucesso.

[09:22] Tendo feito isso, vou no Eclipse e tenho um script aqui chamado start.sh. Basicamente, é só executar esse script para que a aplicação possa subir. Esse capítulo 1 vai ser bem básico porque vamos mexer no código da aplicação, pouca coisa, mas vamos mexer.

[09:49] Para não termos que ficar "rebuildando" o contêiner toda hora, a aplicação vai estar assim. Quando chegarmos no status final da aplicação com ela instrumentada, com o application properties devidamente configurado, com tudo certo, ela vai virar um contêiner e vai subir diretamente via Docker Compose.

[10:09] Aí você não vai ter esse trabalho. Eu subi a aplicação, ela está aqui. Agora, vou no Firefox e vou em “localhost:8080/topicos”. Está aqui, está rodando, essa é a nossa API.

[10:34] Ela responde por “tópicos” e também aceita um ID específico em “tópicos”. Lá na base, aquele script SQL populou três IDs lá dentro. Tenho “1”, “2” e “3”, são endpoints.

[10:52] Para cada inserção que ocorrer de tópico dessa aplicação, vai acabar gerando um endpoint com um número daquele ID que você consegue requisitar.

[11:03] Então, está aqui, configuramos a stack, já tem tudo para partirmos para a próxima aula. Aí entra o nosso problema. Se olharmos para essa aplicação hoje, ela não está instrumentada, ela não tem observabilidade. Então, eu não sei como está o funcionamento dela de fato.

[11:27] Ela está respondendo, mas quanto ela está consumindo de CPU? Qual o tempo de resposta que os meus clientes estão tendo? Quanto ela está usando de memória? Como está a conexão com a base de dados? Qual a duração de uma requisição? Estou tendo algum erro? Tem algum cliente com erro?

[11:47] Tudo isso nós não sabemos, nós estamos cegos. Então, vamos fazer a primeira parte da camada de observabilidade para sairmos do escuro e entendermos o funcionamento da aplicação na próxima aula, iniciando pelo Actuator.

[12:02] É isso, até a próxima aula.






# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 - Configurando o ambiente

<https://linuxize.com/post/how-to-install-apache-maven-on-debian-10/>

sudo apt update
sudo apt install maven
mvn -version

fernando@debian10x64:~/cursos$ mvn -version
Apache Maven 3.6.0
Maven home: /usr/share/maven
Java version: 11.0.16, vendor: Debian, runtime: /usr/lib/jvm/java-11-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.19.0-17-amd64", arch: "amd64", family: "unix"
fernando@debian10x64:~/cursos$



- Instalando o "Extension Pack for Java" no VSCODE

- Garantir que as portas 3306 e 6379 não estejam sendo usadas na VM, porque o projeto vai usar elas no Docker


- VERSÕES
validar versões do Docker, Docker-compose, Java

[04:58] "Docker version 20.10.7, built 20.10.7-0ubuntu5-18.04.04.3" - essa é a versão do Docker que estou utilizando e também tem a versão do docker-compose --version: "docker-compose version 1.29.2, built 5becea4c". É importante que você tenha uma versão igual ou superior à minha. Se for muito superior, se tiver mudado alguma coisa, você pode encontrar algum conflito, mas aí você pode comunicar o fórum ou pode fazer downgrade da versão e resolver o problema.

$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ docker -v
Docker version 20.10.10, build b485636
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ docker-compose --version
docker-compose version 1.29.2, build 5becea4c
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$

fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$ java -version
openjdk version "11.0.16" 2022-07-19
OpenJDK Runtime Environment (build 11.0.16+8-post-Debian-1deb10u1)
OpenJDK 64-Bit Server VM (build 11.0.16+8-post-Debian-1deb10u1, mixed mode, sharing)
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana$



- Subindo stack de MYSQL e REDIS:

docker-compose up

~~~~bash
mysql-forum-api    | 2022-11-06T16:49:35.792599Z 0 [Warning] Insecure configuration for --pid-file: Location '/var/run/mysqld' in the path is accessible to all OS users. Consider choosing a different directory.
mysql-forum-api    | 2022-11-06T16:49:35.805938Z 0 [Note] Event Scheduler: Loaded 0 events
mysql-forum-api    | 2022-11-06T16:49:35.806357Z 0 [Note] mysqld: ready for connections.
mysql-forum-api    | Version: '5.7.40'  socket: '/var/run/mysqld/mysqld.sock'  port: 3306  MySQL Community Server (GPL)
~~~~





cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana


[08:05] Está terminando aqui. Só esperar, subiu, deu até o versionamento aqui e ele está esperando por conexões. Eu vou abrir o meu Terminal e vou entrar em app/, com cd app/. Em app/, vou rodar o comando mvn clean package.




- Buildando aplicação e validando se está tudo ok

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


[09:05] Ele vai executar uma série de testes da aplicação e, se tudo passar, se estiver tudo certo, ele vai concluir esse build e vai gerar um artefato JAR. Passou, ele rodou quatro testes, nenhuma falha, nenhum erro, passou tudo, "buildou" com sucesso.




- Local

~~~~bash
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  56.468 s
[INFO] Finished at: 2022-11-06T13:52:44-03:00
[INFO] ------------------------------------------------------------------------
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$
~~~~








- Terminado o build, vamos subir a aplicação com o Script start.sh

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$ ls
Dockerfile  mvnw  mvnw.cmd  pom.xml  src  start.sh  target
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$ cat start.sh
#!/bin/bash

java -jar -Xms128M -Xmx128M -XX:PermSize=64m -XX:MaxPermSize=128m -Dspring.profiles.active=prod target/forum.jarfernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$
~~~~










~~~~bash
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$ sh start.sh
OpenJDK 64-Bit Server VM warning: Ignoring option PermSize; support was removed in 8.0
OpenJDK 64-Bit Server VM warning: Ignoring option MaxPermSize; support was removed in 8.0

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.1.RELEASE)

2022-11-06 13:57:23.181  INFO 33137 --- [           main] br.com.alura.forum.ForumApplication      : Starting ForumApplication v0.0.1-SNAPSHOT on debian10x64 with PID 33137 (/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/target/forum.jar started by fernando in /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app)
2022-11-06 13:57:23.185  INFO 33137 --- [           main] br.com.alura.forum.ForumApplication      : The following profiles are active: prod
2022-11-06 13:57:24.930  INFO 33137 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode!
2022-11-06 13:57:24.933  INFO 33137 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFERRED mode.

2022-11-06 13:57:29.876  INFO 33137 --- [           main] pertySourcedRequestMappingHandlerMapping : Mapped URL path [/v2/api-docs] onto method [springfox.documentation.swagger2.web.Swagger2Controller#getDocumentation(String, HttpServletRequest)]
2022-11-06 13:57:30.842  INFO 33137 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-11-06 13:57:30.844  INFO 33137 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Context refreshed
2022-11-06 13:57:30.849  INFO 33137 --- [         task-1] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2022-11-06 13:57:30.866  INFO 33137 --- [         task-1] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2022-11-06 13:57:30.896  INFO 33137 --- [           main] d.s.w.p.DocumentationPluginsBootstrapper : Found 1 custom documentation plugin(s)
2022-11-06 13:57:30.976  INFO 33137 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2022-11-06 13:57:31.379  INFO 33137 --- [           main] DeferredRepositoryInitializationListener : Triggering deferred initialization of Spring Data repositories…
2022-11-06 13:57:31.987  INFO 33137 --- [           main] DeferredRepositoryInitializationListener : Spring Data repositories initialized!
2022-11-06 13:57:31.999  INFO 33137 --- [           main] br.com.alura.forum.ForumApplication      : Started ForumApplication in 9.6 seconds (JVM running for 10.281)
~~~~














- Acessando


<192.168.0.113:8080/topicos>
192.168.0.113:8080/topicos


	
content	
0	
id	3
titulo	"Duvida 3"
mensagem	"Tag HTML"
dataCriacao	"2019-05-05T17:00:00"
1	
id	2
titulo	"Duvida 2"
mensagem	"Projeto nao compila"
dataCriacao	"2019-05-05T16:00:00"
2	
id	1
titulo	"Duvida 1"
mensagem	"Erro ao criar projeto"
dataCriacao	"2019-05-05T15:00:00"
pageable	
sort	
sorted	true
unsorted	false
empty	false
offset	0
pageNumber	0
pageSize	10
paged	true
unpaged	false
totalPages	1
totalElements	3
last	true
size	10
number	0
sort	
sorted	true
unsorted	false
empty	false
first	true
numberOfElements	3
empty	false










- Também é possível acessar os tópicos por id

<http://192.168.0.113:8080/topicos/1>
http://192.168.0.113:8080/topicos/1

fernando@debian10x64:~$ curl -v http://192.168.0.113:8080/topicos/1
* Expire in 0 ms for 6 (transfer 0x5641825e1fb0)
*   Trying 192.168.0.113...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x5641825e1fb0)
* Connected to 192.168.0.113 (192.168.0.113) port 8080 (#0)
> GET /topicos/1 HTTP/1.1
> Host: 192.168.0.113:8080
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sun, 06 Nov 2022 17:06:01 GMT
<
* Connection #0 to host 192.168.0.113 left intact
{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T15:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$




fernando@debian10x64:~$ curl -v http://192.168.0.113:8080/topicos/2
* Expire in 0 ms for 6 (transfer 0x5601f9512fb0)
*   Trying 192.168.0.113...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x5601f9512fb0)
* Connected to 192.168.0.113 (192.168.0.113) port 8080 (#0)
> GET /topicos/2 HTTP/1.1
> Host: 192.168.0.113:8080
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sun, 06 Nov 2022 17:09:01 GMT
<
* Connection #0 to host 192.168.0.113 left intact
{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T16:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$
fernando@debian10x64:~$ curl -v http://192.168.0.113:8080/topicos/3
* Expire in 0 ms for 6 (transfer 0x5650b17b0fb0)
*   Trying 192.168.0.113...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x5650b17b0fb0)
* Connected to 192.168.0.113 (192.168.0.113) port 8080 (#0)
> GET /topicos/3 HTTP/1.1
> Host: 192.168.0.113:8080
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sun, 06 Nov 2022 17:09:03 GMT
<
* Connection #0 to host 192.168.0.113 left intact
{"id":3,"titulo":"Duvida 3","mensagem":"Tag HTML","dataCriacao":"2019-05-05T17:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~$
