
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula 04 Expondo métricas para o Prometheus"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Expondo métricas para o Prometheus




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Transcrição

[00:00] Vamos dar prosseguimento nas nossas aulas. Na última aula, nós fizemos a configuração do Actuator e conseguimos externalizar as métricas da JVM.

[00:14] Essas métricas ajudam bastante, mas não são o que nós realmente queremos, nós precisamos que as métricas estejam legíveis para o Prometheus. Para conseguirmos fazer isso, temos que trabalhar com o Micrometer.

[00:33] Basicamente, ele vai ser a nossa fachada de métricas, levando as métricas em um formato legível para o Prometheus.

[00:41] Para fazer isso, é bem simples, basta você dar uma pesquisada em “Micrometer”. Para mim, veio como primeiro resultado no Google. Entrando no site, vou em “Documentação > Instalação”. Na “Instalação”, eu tenho o formato para o Gradle – não é o que nos atende, eu vou utilizar o bloco para o Maven.

[01:06] Você vai copiar esse bloco e, de posse desse conteúdo copiado, vamos no pom.xml adicionar essa dependência. Está aqui, pronto, está copiado. Basta salvar e agora vem uma alteração no application.prod.properties.

[01:33] O que vai ser necessário? Colocar o endpoint Prometheus, que nós não colocamos na última aula. Logo após metrics, vai colocar uma vírgula e vai colocar aqui prometheus: management.endpoints.web.exposure.include=health,info,metrics,prometheus.

[01:50] Vamos expor o endpoint prometheus e, além disso, precisamos de uma configuração adicional. Vou colar aqui essa configuração, vocês vão ter esse arquivo já feito, então não precisam se preocupar em digitar tudo isso, mas é muito importante que vocês entendam cada uma dessas linhas.

[02:15] Primeiro, vamos habilitar as métricas da JVM, tem aqui management.metrics.enable.jvm=true; tem aqui também o management.metrics.export.prometheus.enabled=true. Estamos habilitando o export das métricas para o Prometheus.

[02:34] Temos aqui uma métrica específica relacionada à SLA, que não vou explicar agora, porque ela terá uma aula só para ela, mas já vamos deixar configurado para não termos que voltar nesse arquivo no futuro.

[02:51] Aqui também tenho uma configuração que é de tagueamento. Quando externalizarmos uma métrica, essa métrica vai ser exibida com um tagueamento. Nesse caso, ela vai ter a tag app-forum-api.

[03:16] Por que isso? Quando tivermos N aplicações rodando, algumas métricas vão ser iguais. Por exemplo, a métrica de tempo de resposta pode ter o mesmo nome, mas posso querer olhar mais de uma aplicação, aí a tag entra nesse ponto, porque eu posso selecionar aquela métrica com uma tag específica correspondente à aplicação que eu quero avaliar.

[03:44] Está aqui esse bloco de configurações, você salva aí. Vamos para o Terminal e eu vou fazer o build dessa nova versão. Enquanto ele vai "buildando", podemos dar uma olhada no Actuator. Notem que são esses endpoints que nós possuímos.

[04:05] Não tem nada relacionado ao Prometheus – temos métricas, mas não temos o Prometheus. Vou voltar, esperar terminar o build. Concluiu com sucesso, nenhum erro.

[04:24] Posso voltar aqui, deixa eu só ver se a aplicação está rodando. Está rodando, vou dar um “Ctrl + C”, pronto. Vamos no start.sh e vamos rodar a aplicação.

[04:41] É só deixar a aplicação subir agora. Uma coisa importante, que eu gostaria que vocês fizessem, é dar uma lida na documentação do Actuator. Aqui, no Actuator, você pode ver a parte de “Logging”, que é exposta, e tem o log level, que é exposto.

[05:00] Tem a parte de métricas. Já veio o Micrometer aqui e tem a parte específica do Prometheus. É bem legal. E também dar uma olhada na documentação do Micrometer.

[05:17] É bem importante que você entenda isso para que você possa levar para outras aplicações com o conhecimento de causa interessante. Deixa eu voltar no build, vamos ver a aplicação, se ela subiu.

[05:35] Está aqui, está "startada", vamos voltar para o browser. No browser, vou atualizar o Actuator e agora já tem o endpoint actuator-prometheus. Clicando nele, nós encontramos as métricas no formato esperado pelo Prometheus.

[05:56] Se você notar, tem bastante coisa, é muita métrica, e a maior parte delas vai ser útil para nós na nossa implantação, no acompanhamento do funcionamento da nossa API. Alguma aqui não é útil? Na verdade, nenhuma é inútil, todas elas são úteis.

[06:17] Algumas vamos acabar desprezando quando estamos rodando a nossa aplicação em um contêiner, então não são tão necessárias para nós quanto o número de núcleos de uma CPU ou coisas do tipo.

[06:31] Mas está aqui, conseguimos expor essas métricas em um formato legível para o Prometheus e agora podemos entender o que são essas métricas e ao que cada uma delas corresponde.

[06:46] Pelo menos, as que são interessantes para a nossa aplicação e que vamos utilizar futuramente configurando dashboards e alertas em cima de valores específicos, de momentos específicos dessas métricas.

[07:02] Essa aula se encerra aqui e, na aula que vem, vamos entender as métricas da JVM para que você saiba qual métrica vai ser utilizada para um tipo específico de observabilidade e monitoramento. Até a próxima.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Expondo métricas para o Prometheus




https://micrometer.io/docs

https://micrometer.io/docs/installing




The following example adds Prometheus in Maven:

<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
  <version>${micrometer.version}</version>
</dependency>



- Editar o pom.xml e adicionar a dependencia:
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/pom.xml

<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
  <version>${micrometer.version}</version>
</dependency>




- Necessário adicionar o endpoint do Prometheus no arquivo "application-prod.properties":
sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/resources/application-prod.properties

DE:

# actuator
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=health,info,metrics

PARA:

# actuator
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=health,info,metrics,prometheus






- Necessário adicionar a Configuração do Prometheus no arquivo "application-prod.properties":
sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/resources/application-prod.properties
~~~~conf
# prometheus
management.metrics.enable.jvm=true
management.metrics.export.prometheus.enabled=true
management.metrics.distribution.sla.http.server.requests=50ms,100ms,200ms,300ms
management.metrics.tags.application=app-forum-api
~~~~




- Buildando aplicação e validando se está tudo ok

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


- Buildou:

~~~~bash

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
[INFO] Total time:  43.741 s
[INFO] Finished at: 2022-11-08T22:06:23-03:00
[INFO] ------------------------------------------------------------------------
fernando@debian10x64:~/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app$

~~~~









- Acessando o endpoint do Actuator, agora consta o Prometheus:

<http://192.168.0.113:8080/actuator>
	
_links	
self	
href	"http://192.168.0.113:8080/actuator"
templated	false
health-path	
href	"http://192.168.0.113:8080/actuator/health/{*path}"
templated	true
health	
href	"http://192.168.0.113:8080/actuator/health"
templated	false
info	
href	"http://192.168.0.113:8080/actuator/info"
templated	false
prometheus	
href	"http://192.168.0.113:8080/actuator/prometheus"
templated	false
metrics-requiredMetricName	
href	"http://192.168.0.113:8080/actuator/metrics/{requiredMetricName}"
templated	true
metrics	
href	"http://192.168.0.113:8080/actuator/metrics"
templated	false




- Clicando no Prometheus, acessando ele
<http://192.168.0.113:8080/actuator/prometheus>

ele agora traz as métricas no formato esperado

jvm_memory_max_bytes{application="app-forum-api",area="nonheap",id="Compressed Class Space",} 1.073741824E9
jvm_memory_max_bytes{application="app-forum-api",area="nonheap",id="CodeHeap 'profiled nmethods'",} 1.22908672E8
jvm_memory_max_bytes{application="app-forum-api",area="heap",id="G1 Survivor Space",} -1.0
jvm_memory_max_bytes{application="app-forum-api",area="heap",id="G1 Old Gen",} 1.34217728E8
jvm_memory_max_bytes{application="app-forum-api",area="nonheap",id="Metaspace",} -1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",le="0.05",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",le="0.1",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",le="0.2",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",le="0.3",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",le="+Inf",} 1.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="403",uri="root",} 1.0

Entre outras