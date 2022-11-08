
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula 03 Externalizando métricas com Actuator"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 03 Externalizando métricas com Actuator


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Transcrição
[00:00] Sejam bem-vindos à nossa aula 2 do curso. Agora, vamos configurar o Actuator e enfim tornar a nossa aplicação observável – ou, pelo menos, iniciar esse trajeto.

[00:16] Aqui foi onde paramos na aula anterior, nós conseguimos implantar o ambiente e subir a aplicação. A primeira coisa que eu peço a você é que você dê uma olhada na documentação do Actuator. É só você digitar no Google “Actuator” e você vai encontrar em um dos primeiros resultados a documentação do Spring dizendo como você vai habilitar o Actuator.

[00:48] O Actuator entra como uma dependência. No nosso caso, estamos utilizando o Maven. Vou copiar essa dependência. Se estiver usando Gradle, é outra forma de implantação, mas, no nosso caso, é o Maven.

[01:08] Vou copiar isso, vou abrir o Eclipse e, no Eclipse, vou abrir o arquivo pom.xml. Tem diversas dependências configuradas aqui, você deve encontrar algum espaço para colocar essa configuração, eu vou colocar aqui.

[01:45] Vou manter a indentação e está certo. Tenho essa dependência já configurada, vou salvar e, no automático, o Maven já vai descarregar essa dependência através do Eclipse.

[02:08] O processo é bem simples. Tendo feito isso, é necessário que façamos alguns ajustes na nossa aplicação. Nesse caso, não vamos mexer no código da aplicação agora. No painel à esquerda, vamos em src/main/resources, vamos no application-prod.properties e aqui tem diversas configurações.

[02:32] Tem a porta em que a aplicação sobre, tem a configuração do Redis, tem a configuração do MySQL, tem o JPA para conexão e tem o token jwt, porque essa aplicação usa um token.

[02:47] Para algumas ações de excluir um tópico, criar um tópico, é necessário ter um token que é gerado através de uma autenticação. Estamos falando de uma API Rest.

[03:02] O que vou fazer agora? Deixa eu pegar uma cola para não ter que digitar tudo isso. Em qualquer espaço que você encontrar, pode colocar actuator. A primeira linha que eu inseri aqui foi o management.endpoint.health.show-details.

[03:23] Está como always, então vai mostrar todos os detalhes, e aqui eu tenho o web.exposure. Aqui está um asterisco; se eu deixar com asterisco, ele vai basicamente colocar todas as informações relacionadas a JVM e tudo que estiver relacionado à aplicação, ao gerenciamento da aplicação pela JVM.

[03:46] Isso, para mim, não vai ser muito interessante – você pode até fazer esse teste. Quem eu vou disponibilizar é um endpoint chamado health, um endpoint chamado info e um endpoint chamado metrics. Então: management.endpoints.web.exposure.include=health,info,metrics.

[04:01] Esses três estão de bom tamanho, são o que nós precisamos. O nosso foco vai estar sobre o metrics, mas o health é legal se você tem um endpoint que pode ser usado para health check, dependendo de onde você estiver rodando a sua aplicação.

[04:21] E o info é legal porque você consegue ter as informações dessa aplicação, informações interessantes para você, não para o público externo. health, info e metrics são internos, não devem ser expostos publicamente.

[04:42] Essa aplicação, quando estiver rodando na stack do Docker Compose, vai estar com esses endpoints fechados. Para acessarmos essa aplicação, vamos ter que passar por um proxy que também vai ser implantado no próximo capítulo.

[05:00] Tendo cumprido esses pré-requisitos, eu vou para o Terminal. A aplicação está rodando, nós a subimos pelo Eclipse. Vou fechar essa janela e, como já estou no diretório da aplicação, vou rodar um mvn clean package para "rebuildar" essa aplicação.

[05:23] O processo é bem simples. Como eu disse na aula anterior, você pode fazer isso pelo Eclipse, mas eu prefiro fazer aqui. Nos contêineres, não é necessário mexer. Eu não subi em modo daemon, então ele está trazendo o status dos contêineres.

[05:41] Vou dar um zoom no Terminal em que a aplicação está sendo "buildada". Rodou direito, vamos agora para o Eclipse. No Eclipse, vou no start.sh novamente e vou rodar a aplicação.

[06:03] Vamos deixá-la subir aqui. Se você olhar, essa linha da inicialização, só para você ter uma ideia, de repente você está no Windows e o bin/bash não vai funcionar para você.

[06:16] Você pode rodar isso na linha de comando do cmd do próprio Windows, contanto que você esteja posicionado no path em que está o app. Você tem que estar dentro do diretório app, aí você pode chamar o Java dessa maneira que ele vai encontrar, dentro de target, o forum.

[06:37] Você só vai ter que fazer isso no Windows, colocar uma contrabarra (target\forum) e não a barra (/), porque o Windows não entende a barra que, no Linux, faz distinção entre um diretório e outro. No MacOS, isso não muda nada.

[06:54] Voltando, a aplicação subiu, foi "startada" e, por fim, eu tenho “topicos”, tenho “http://localhost:8080/topicos/” e um ID qualquer. Agora tenho “http://localhst:8080/actuator”. Aqui está o Actuator expondo para nós health. Aqui tenho health mais um path específico, mas ele é herdado desse health aqui que foi configurado no application properties.

[07:38] Está aqui, eu tenho esse endpoint health, tenho o endpoint health-path que veio herdado do health, tenho o info, e tenho o metrics. Tenho o metrics com uma métrica específica, o metric name, e tenho o metrics puro.

[07:59] Então, o que vou fazer aqui? Vou abrir o health, vamos dar uma olhada no health. Está aqui, esse é o health que pode ser usado como health check, então status: UP. Ele olha, inclusive, a comunicação que a aplicação depende, no caso, o My SQL, está conectado.

[08:16] Olha o espaço em disco. Tem o redis também UP. Então, esse health é importantíssimo para a questão de health check e para você entender, em um momento imediato, se alguma dependência está com problema.

[08:35] Se ele não conectar com o Redis, vai bater aqui; se não conectar com o MySQL, vai bater aqui também, porque isso não depende da aplicação conectar no banco para ser exibido.

[08:48] Já demos uma olhada no health, vamos dar uma olhada no info, o que eu encontro dentro do info. O app, qual é o nome, a descrição, a versão, como ele está codificado e a versão do Java. Isso é bem legal.

[09:06] O que nós conseguimos, na parte mais importante, em metrics, o que conseguimos ver? Muita coisa. Aqui tenho a exposição de todas as métricas da JVM. Se você verificar, você tem o uso de CPU, você tem o log no logback, você tem a questão de threads, de memória, você consegue encontrar o garbage collector.

[09:39] Você consegue pegar o pool de conexões da JDBC com o banco, conexões pendentes, com timeout, utilizadas, tempo de criação, enfim, diversas métricas.

[09:55] Essas métricas, se eu pegar o nome desse, eu consigo chegar nesse endpoint com o nome de uma métrica naquela métrica específica. Por exemplo, vou pegar o hikaricp.connections, vou chamar esse endpoint, que é o mesmo. Vou colocar o “http://localhost:8080/actuator/metrics/hikaricp.connections” e está aqui, o valor é 10.

[10:24] Então, eu consigo chegar nessas métricas. Nós conseguimos colocar as métricas da JVM aqui, elas estão expostas, porém, não estão no formato esperado. Nós não conseguimos identificar e usar essas métricas através do Prometheus, que é o nosso objetivo.

[10:47] Então, na próxima aula, vamos configurar o Micrometer e ele vai fazer esse meio de campo, vai tornar essas métricas legíveis para o Prometheus. Te vejo na próxima aula.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 03 Externalizando métricas com Actuator


- Ativando o Actuator num projeto baseado em Maven
<https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/production-ready-enabling.html>

To add the actuator to a Maven based project, add the following ‘Starter’ dependency:

~~~~java
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-actuator</artifactId>
	</dependency>
</dependencies>
~~~~


- Editar o arquivo pom.xml
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml
colar o conteúdo do xml acima




- Editar o arquivo "application-prod.properties"
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/resources/application-prod.properties

[02:32] Tem a porta em que a aplicação sobre, tem a configuração do Redis, tem a configuração do MySQL, tem o JPA para conexão e tem o token jwt, porque essa aplicação usa um token.

[02:47] Para algumas ações de excluir um tópico, criar um tópico, é necessário ter um token que é gerado através de uma autenticação. Estamos falando de uma API Rest.


[03:02] O que vou fazer agora? Deixa eu pegar uma cola para não ter que digitar tudo isso. Em qualquer espaço que você encontrar, pode colocar actuator. A primeira linha que eu inseri aqui foi o management.endpoint.health.show-details.


O que será adicionado:

~~~~conf
# actuator
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=health,info,metrics
~~~~



- ReBuildando a aplicação e validando se está tudo ok:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package

deu erro

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$ cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$ mvn clean package
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.google.inject.internal.cglib.core.$ReflectUtils$1 (file:/usr/share/maven/lib/guice.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of com.google.inject.internal.cglib.core.$ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
[INFO] Scanning for projects...
[ERROR] [ERROR] Some problems were encountered while processing the POMs:
[ERROR] Malformed POM /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml: Unrecognised tag: 'dependencies' (position: START_TAG seen ...</dependency>\n\n\t\t<dependencies>... @94:17)  @ /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml, line 94, column 17
 @
[ERROR] The build could not read 1 project -> [Help 1]
[ERROR]
[ERROR]   The project br.com.alura:forum:0.0.1-SNAPSHOT (/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml) has 1 error
[ERROR]     Malformed POM /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml: Unrecognised tag: 'dependencies' (position: START_TAG seen ...</dependency>\n\n\t\t<dependencies>... @94:17)  @ /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml, line 94, column 17 -> [Help 2]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/ProjectBuildingException
[ERROR] [Help 2] http://cwiki.apache.org/confluence/display/MAVEN/ModelParseException
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$

~~~~




- Ajustando o pom.xml

- ReBuildando a aplicação e validando se está tudo ok:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package

buildou corretamente

~~~~bash

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
[INFO] Total time:  40.761 s
[INFO] Finished at: 2022-11-07T22:03:33-03:00
[INFO] ------------------------------------------------------------------------
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$

~~~~



- Rodar a aplicação novamente:
 sh start.sh





- Agora é possível acessar o Actuator:
http://192.168.0.113:8080/actuator

	
_links	
self	
href	"http://192.168.0.113:8080/actuator"
templated	false
health	
href	"http://192.168.0.113:8080/actuator/health"
templated	false
health-path	
href	"http://192.168.0.113:8080/actuator/health/{*path}"
templated	true
info	
href	"http://192.168.0.113:8080/actuator/info"
templated	false
metrics	
href	"http://192.168.0.113:8080/actuator/metrics"
templated	false
metrics-requiredMetricName	
href	"http://192.168.0.113:8080/actuator/metrics/{requiredMetricName}"
templated	true