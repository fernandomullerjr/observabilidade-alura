
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 004 - Aula 02 A anatomia de uma Métrica"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 A anatomia de uma Métrica

Transcrição
[00:00] Vamos dar sequência no nosso curso. Agora que já temos o consumidor da nossa API, vamos conseguir ter uma visibilidade melhor sobre as métricas, mas, para que possamos realmente fazer bom uso do que estamos fazendo, é muito importante que você entenda como é uma métrica para o Prometheus.

[00:26] No browser, vamos no “localhost/metrics”, vou atualizar, você vai ver que muita métrica foi gerada por conta daquele client que nós rodamos. Inclusive, alguns erros 500 vão se detectados, porque o script começou a ser executado pelo client antes de o MySQL estar realmente atendendo às requisições, então gerou uma quantidade de erros 500 em /topicos.

[01:03] Foi só durante a subida, agora está certo. Você vai notar o seguinte – vou diminuir para você identificar cada métrica em uma linha única –, os valores estão aumentando porque a aplicação está sendo consumida.

[01:25] Aqui tem um /topicos/{id}. Se descermos um pouco, vamos ver que /topicos está aqui; temos também as métricas de usuários, então, até dado momento, eu tenho 70 autenticações com sucesso.

[01:47] Tenho também autenticações com erro, 38 até agora. Voltando ao que interessa, se olharmos para uma métrica, vou procurar pela métrica de log, que talvez seja a mais simples de explicar no momento.

[02:08] Vamos pegar essa métrica aqui, logback_events_total. Toda métrica tem três componentes básicos. O primeiro é o metric name, é o nome da métrica. Aqui, o metric name que nós temos é o logback_events_total. Se eu executo essa consulta no Prometheus, eu tenho alguns retornos.

[02:34] Se eu pegar um desses retornos específicos - o primeiro, por exemplo - e fazer essa consulta, eu tenho só o retorno dessa consulta específica com esses atributos. Vamos entender o que são esses atributos e o que é o retorno da consulta.

[02:56] Então, tenho o metric name, que nesse caso é o logback_events_total, e logo após o metric name tenho labels, rótulos. Esses rótulos vão identificar qual é a série temporal que você quer consultar.

[03:15] Até agora vimos duas entidades aqui: metric name e labels. Podem existir vários labels configurados, eles vão estar nessa configuração similar a uma variável, de chave-valor, {application=”app-forum-api”,instance="app-forum-api:8080",job-"app-forum-api",level="debug"}.

[03:39] À direita dos labels, eu tenho o sample, que é o resultado dessa consulta, que está aqui, 0. Se eu mudar alguns desses labels, por exemplo, eu não quero olhar o level debug do log, eu quero olhar o info: logback_events_total{application=”app-forum-api”,instance="app-forum-api:8080",job-"app-forum-api",level="info"}. Vamos fazer a consulta dele, eu tenho 43 como retorno, meu sample como 43.

[04:04] Já entendemos que existe metric name, que existe label e que existe o sample, que é o resultado da consulta sobre aquela métrica com aquele metric name e aqueles labels específicos.

[04:21] Isso está fácil de entender. Agora nós entramos no ponto de quais são os tipos de dados que uma métrica pode ter – qual tipo de dado é aquela métrica? No Prometheus, temos quatro tipos de dados.

[04:40] Temos o instant vector, que é um vetor instantâneo; temos o range vector, que é um vetor de uma série temporal; temos um dado do tipo Scalar, que é um float simples, um ponto flutuante; e temos o spring, que basicamente não é utilizado, a própria documentação do Prometheus diz isso, que não é comumente utilizado.

[05:10] Vamos só focar nos três tipos de dados mais utilizados no Prometheus e são eles que vamos utilizar até o último momento desse curso. O que é um instant vector? É um vetor instantâneo, esse vetor está sendo exibido agora para vocês na tela, ao executar logback_events_total.

[05:30] Quando eu olho para essa métrica, logback_events_total, ela me retorna um vetor. Esse vetor está aqui embaixo, cada uma dessas linhas seria um índice desse vetor. Eu teria o índice 0, 1, 2, 3 e 4. Vamos simplificar dessa maneira.

[05:52] Cada um desses elementos que estão dentro desse vetor é uma série temporal. Eu tenho aqui 5 séries temporais que estão armazenadas nessa métrica logback_events_total e, quando eu faço uma consulta para essa métrica, esse vetor retorna para mim.

[06:20] O que gera esses vetores e como eu faço a distinção entre cada um? Através de levels, são os labels que vão fazer essa distinção. Se olharmos para essa série temporal específica, vou pegar novamente o debug, você vai notar o seguinte.

[06:45] Qual é a diferença de uma para outra? É o level. Essa métrica está relacionada ao log, e o log tem níveis de logs específicos que a JVM consegue externalizar através do Actuator e o Prometheus acaba fazendo a interface através do Micrometer.

[07:07] Nós conseguimos ter acesso à essa informação através de uma métrica com um label de level distinto. Então, tivemos, na subida da aplicação, uma quantidade de info, uma quantidade de erros e uma quantidade de warnings. Não tivemos log level de debug aqui, nem de trace.

[07:30] Isso é um instant vector, um vetor de tempo. Se você olhar em “Evaluation time”, você vai ver o momento da consulta. Então, falamos sobre o primeiro tipo de dado que é o instant vector.

[07:48] Agora, vamos falar sobre o range vector. Para chegarmos nesse ponto do range vector, eu vou tirar tudo e colocar 1 minuto,[1m], entre colchetes - logback_events_total[1m]. A partir do momento que eu faço isso, estou trabalhando com o range vector.

[08:12] Notem que teve uma transformação aqui, eu estou olhando justamente para esse timestamp, posso tirá-lo e refazer a consulta, vai dar na mesma. O que estou trazendo é o último minuto dentro desse timestamp.

[08:32] Esse foi o timestamp de execução da consulta, eu quero o último minuto. No último minuto, eu tenho essas informações que voltaram. Notem que o range vector não seria uma matriz, não temos um vetor multidimensional, mas temos, em cada série temporal, um “vetor” que traz um valor específico a cada scrape time do Prometheus.

[09:08] Vamos lembrar a nossa configuração. Já vimos isso em uma aula anterior. Se eu abrir aqui e for em “Configuration”, você vai ver que o nosso job app-forum-api tem o scrape de 5s. Então, a cada 5 segundos o Prometheus vai lá e bate no endpoint para trazer as métricas.

[09:34] Justamente aqui, em 1 minuto, nós tivemos essa quantidade aqui, 12 consultas, cada uma a 5 segundos, o que equivale a 60 segundos de trabalho do Prometheus buscando métricas.

[09:56] Alguns estão com valores e outros não, não está dando para pegarmos uma mudança aqui nesses valores, mas normalmente vamos enxergar mudanças.

[10:04] Aqui tem uma notação meio estranha, que é o unix timestamp. Se você quiser entender o que é isso, no Linux é fácil – se você estiver no Windows, deve ter uma forma fácil de você fazer no PowerShell, ou você vai no browser e dá uma "googlada" “unix timestamp” que você vai encontrar um jeito de converter.

[10:23] Vou te mostrar, só para você entender um pouco mais. Se eu rodar um date -d e colocar o timestamp, o Linux converte para mim e eu vejo que essa consulta rodou às 23:39, está certo, com o horário configurado.

[10:46] Se eu quiser ir um pouco mais além, só para confirmar para você, eu posso declarar uma variável que vai ser um array, e vou colocar quatro valores para ficar mais simples. Tenho um, dois, três e, por fim, o quarto.

[11:26] Declarei esse array, se eu fizer um laço, vou fazer for i in e vou iterar esse array. Se você não souber Shell, não tem problema, isso é só para você ter a noção de como isso está vinculado ao scrape time.

[11:59] Vou fazer um laço, um for que vai varrer esse array, vai varrer cada um desses valores, temos quatro valores, índice 0, 1, 2 e 3. A cada iteração, ele vai encapsular na variável i, em um timestamp específico, e vai converter através do date.

[12:18] Está aí. se você notar, a cada cinco segundos, 02, 07, 12 e 17, ele trouxe um valor para mim, ele fez um scrape e trouxe. Então, quando falamos em range vector, estamos falando de um range de tempo dentro de uma série temporal.

[12:44] Eu posso facilitar um pouco isso, posso olhar um intervalo específico? Posso, através de uma subquery. Eu quero olhar os últimos 5 minutos e quero ver apenas o último minuto. Está aqui, consigo fazer isso: logback_events_total[5m:1m].

[13:02] Um minuto ficou muita coisa, quero olhar os 30 segundos. Também consigo: logback_events_total[5m:30s]. Então, dentro dos últimos 5 minutos, eu olhei os 30 segundos.

[13:18] É bem simples de se trabalhar, vamos ter funções que vamos ver mais a frente que vão aproveitar melhor o range vector. Tem um detalhe, estamos vendo uma saída que é tabulada, e se quisermos ir para gráfico?

[13:34] Não é possível. Se você ler isso, você vai ver que “houve um erro executando a consulta, o tipo ‘range vector’ é inválido para esse tipo de consulta, tente com um Scalar ou um vetor instantâneo”.

[13:51] Isso significa o quê? Que eu não posso formar um gráfico no Prometheus quando eu tiver uma saída que possui mais de um valor. Ou ela tem que ser um instant vector ou um dado Scalar. O dado Scalar, só lembrando, é um float.

[14:10] Vou executar dessa maneira, logback_events_total. Eu tive esse retorno, ele formou um gráfico para mim, está bem simples, mas formou o gráfico. Aqui eu tenho uma consulta que traz para mim um instant vector. Sendo um instant vector ou sendo um dado Scalar, eu consigo retorno.

[14:40] Voltando, o que faltou é falarmos sobre o Scalar. O dado Scalar é, basicamente, um float que vou encontrar. Posso encontrar qualquer um aqui, por exemplo, deixa eu olhar para alguma coisa de conexões.

[15:12] Tenho um dado que vai me retornar um valor que é um float simples e que vai poder ser visualizado. Se eu for para o modo gráfico, eu consigo enxergar também um gráfico.

[15:35] Não vamos falar de string porque isso não cabe dentro do nosso conteúdo e dificilmente você vai ver algum caso de uso para string envolvendo o Prometheus. Isso pode ser encontrado na própria documentação.

[15:51] Se procurarmos aqui – deixa eu ver no “Help”, que já vem direto para cá –, se eu vir na parte básica de consulta, você vai ver o tipo de dado que o Prometheus usa, como ele lida com string, como ele lida com float, como ele trabalha com instant vector e como ele trabalha com range vector.

[16:20] Eu aconselho que você aprofunde mais utilizando essa documentação, ela vai estar na plataforma, vamos estender esse assunto até o final do curso, só que não vamos esgotá-lo. Então, acho muito interessante você consumir a documentação e aprofundar um pouco mais.

[16:37] Agora que entendemos a anatomia de uma métrica, entendemos os três componentes – metric name, label e o retorno da consulta que é o sample.

[16:52] E entendemos também os tipos de dados que o Prometheus utiliza, já estamos com o caminho aberto para a próxima aula, em que vamos aprofundar um pouco nesse assunto, vamos falar sobre os tipos de métricas que o Prometheus utiliza.

[17:13] Te vejo na próxima aula, até mais.




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 A anatomia de uma Métrica


[00:00] Vamos dar sequência no nosso curso. Agora que já temos o consumidor da nossa API, vamos conseguir ter uma visibilidade melhor sobre as métricas, mas, para que possamos realmente fazer bom uso do que estamos fazendo, é muito importante que você entenda como é uma métrica para o Prometheus.

[00:26] No browser, vamos no “localhost/metrics”, vou atualizar, você vai ver que muita métrica foi gerada por conta daquele client que nós rodamos. Inclusive, alguns erros 500 vão se detectados, porque o script começou a ser executado pelo client antes de o MySQL estar realmente atendendo às requisições, então gerou uma quantidade de erros 500 em /topicos.

http://192.168.0.113/metrics
<http://192.168.0.113/metrics>


~~~~bash

http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.05",} 1506.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.1",} 1508.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.2",} 1509.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.3",} 1510.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="+Inf",} 1510.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",} 1510.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",} 14.169847161
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.05",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.1",} 129.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.2",} 535.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.3",} 540.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="+Inf",} 540.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",} 540.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",} 61.545931534
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.05",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.1",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.2",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.3",} 1.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="+Inf",} 1.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 1.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.177473069
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.05",} 812.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.1",} 812.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.2",} 813.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.3",} 814.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="+Inf",} 815.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 815.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 7.88043136
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.05",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.1",} 33.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.2",} 147.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.3",} 147.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="+Inf",} 148.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",} 148.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",} 19.355592757
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.05",} 2801.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.1",} 2805.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.2",} 2806.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.3",} 2806.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="+Inf",} 2807.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",} 2807.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",} 18.363357789
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.05",} 89.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.1",} 89.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.2",} 89.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.3",} 89.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="+Inf",} 89.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",} 89.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",} 0.533675901
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",} 0.027069389
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",} 0.156620146
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.177473069
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",} 0.030214606
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",} 0.143336765
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",} 0.006528223
http_server_requests_seconds_max{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",} 0.005589456
~~~~




- Métricas de logs:

~~~~bash
logback_events_total{application="app-forum-api",level="info",} 28.0
logback_events_total{application="app-forum-api",level="trace",} 0.0
logback_events_total{application="app-forum-api",level="warn",} 2.0
logback_events_total{application="app-forum-api",level="error",} 0.0
logback_events_total{application="app-forum-api",level="debug",} 0.0
~~~~


- Especifico, obtido via GUI do Prometheus:
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="debug"}


- Metric name:
logback_events_total


- Levels:
application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="debug"

- Sample é o resultado da consulta:
0


[03:39] À direita dos labels, eu tenho o sample, que é o resultado dessa consulta, que está aqui, 0. Se eu mudar alguns desses labels, por exemplo, eu não quero olhar o level debug do log, eu quero olhar o info: logback_events_total{application=”app-forum-api”,instance="app-forum-api:8080",job-"app-forum-api",level="info"}. Vamos fazer a consulta dele, eu tenho 43 como retorno, meu sample como 43.

[04:04] Já entendemos que existe metric name, que existe label e que existe o sample, que é o resultado da consulta sobre aquela métrica com aquele metric name e aqueles labels específicos.








[04:21] Isso está fácil de entender. Agora nós entramos no ponto de quais são os tipos de dados que uma métrica pode ter – qual tipo de dado é aquela métrica? No Prometheus, temos quatro tipos de dados.

[04:40] Temos o instant vector, que é um vetor instantâneo; 
temos o range vector, que é um vetor de uma série temporal; 
temos um dado do tipo Scalar, que é um float simples, um ponto flutuante; 
e temos o spring, que basicamente não é utilizado, a própria documentação do Prometheus diz isso, que não é comumente utilizado.


instant vector, que é um vetor instantâneo; 
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="debug"}








[07:48] Agora, vamos falar sobre o range vector. Para chegarmos nesse ponto do range vector, eu vou tirar tudo e colocar 1 minuto,[1m], entre colchetes - logback_events_total[1m]. A partir do momento que eu faço isso, estou trabalhando com o range vector.

logback_events_total[1m]


logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="debug"}
	0 @1668824257.369
0 @1668824262.368
0 @1668824267.369
0 @1668824272.369
0 @1668824277.366
0 @1668824282.369
0 @1668824287.37
0 @1668824292.369
0 @1668824297.369
0 @1668824302.369
0 @1668824307.366
0 @1668824312.366
 
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="error"}
	0 @1668824257.369
0 @1668824262.368
0 @1668824267.369
0 @1668824272.369
0 @1668824277.366
0 @1668824282.369
0 @1668824287.37
0 @1668824292.369
0 @1668824297.369
0 @1668824302.369
0 @1668824307.366
0 @1668824312.366
 
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="info"}
	28 @1668824257.369
28 @1668824262.368
28 @1668824267.369
28 @1668824272.369
28 @1668824277.366
28 @1668824282.369
28 @1668824287.37
28 @1668824292.369
28 @1668824297.369
28 @1668824302.369
28 @1668824307.366
28 @1668824312.366
 
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="trace"}
	0 @1668824257.369
0 @1668824262.368
0 @1668824267.369
0 @1668824272.369
0 @1668824277.366
0 @1668824282.369
0 @1668824287.37
0 @1668824292.369
0 @1668824297.369
0 @1668824302.369
0 @1668824307.366
0 @1668824312.366
 
logback_events_total{application="app-forum-api", instance="app-forum-api:8080", job="app-forum-api", level="warn"}
	2 @1668824257.369
2 @1668824262.368
2 @1668824267.369
2 @1668824272.369
2 @1668824277.366
2 @1668824282.369
2 @1668824287.37
2 @1668824292.369
2 @1668824297.369
2 @1668824302.369
2 @1668824307.366
2 @1668824312.366

[08:12] Notem que teve uma transformação aqui, eu estou olhando justamente para esse timestamp, posso tirá-lo e refazer a consulta, vai dar na mesma. O que estou trazendo é o último minuto dentro desse timestamp.

[08:32] Esse foi o timestamp de execução da consulta, eu quero o último minuto. No último minuto, eu tenho essas informações que voltaram. Notem que o range vector não seria uma matriz, não temos um vetor multidimensional, mas temos, em cada série temporal, um “vetor” que traz um valor específico a cada scrape time do Prometheus.

[09:08] Vamos lembrar a nossa configuração. Já vimos isso em uma aula anterior. Se eu abrir aqui e for em “Configuration”, você vai ver que o nosso job app-forum-api tem o scrape de 5s. Então, a cada 5 segundos o Prometheus vai lá e bate no endpoint para trazer as métricas.

[09:34] Justamente aqui, em 1 minuto, nós tivemos essa quantidade aqui, 12 consultas, cada uma a 5 segundos, o que equivale a 60 segundos de trabalho do Prometheus buscando métricas.

[09:56] Alguns estão com valores e outros não, não está dando para pegarmos uma mudança aqui nesses valores, mas normalmente vamos enxergar mudanças.

[10:04] Aqui tem uma notação meio estranha, que é o unix timestamp. Se você quiser entender o que é isso, no Linux é fácil – se você estiver no Windows, deve ter uma forma fácil de você fazer no PowerShell, ou você vai no browser e dá uma "googlada" “unix timestamp” que você vai encontrar um jeito de converter.







- O Linux consegue converter o valor do timestamp em Data:

~~~~bash
fernando@debian10x64:~$ date -d @1668824257.369
Fri 18 Nov 2022 11:17:37 PM -03
fernando@debian10x64:~$
~~~~








[10:46] Se eu quiser ir um pouco mais além, só para confirmar para você, eu posso declarar uma variável que vai ser um array, e vou colocar quatro valores para ficar mais simples. Tenho um, dois, três e, por fim, o quarto.

[11:26] Declarei esse array, se eu fizer um laço, vou fazer for i in e vou iterar esse array. Se você não souber Shell, não tem problema, isso é só para você ter a noção de como isso está vinculado ao scrape time.

[11:59] Vou fazer um laço, um for que vai varrer esse array, vai varrer cada um desses valores, temos quatro valores, índice 0, 1, 2 e 3. A cada iteração, ele vai encapsular na variável i, em um timestamp específico, e vai converter através do date.

array=( @1668824257.369 @1668824262.368 @1668824267.369 @1668824272.369 )
for i in "${array[@]}" ; do date -d $i ; done


[12:18] Está aí. se você notar, a cada cinco segundos, 02, 07, 12 e 17, ele trouxe um valor para mim, ele fez um scrape e trouxe. Então, quando falamos em range vector, estamos falando de um range de tempo dentro de uma série temporal.

~~~~bash
fernando@debian10x64:~$ for i in "${array[@]}" ; do date -d $i ; done
Fri 18 Nov 2022 11:17:37 PM -03
Fri 18 Nov 2022 11:17:42 PM -03
Fri 18 Nov 2022 11:17:47 PM -03
Fri 18 Nov 2022 11:17:52 PM -03
fernando@debian10x64:~$
~~~~



- Range de tempo / Range Vector numa série temporal:
logback_events_total[1m]






[12:44] Eu posso facilitar um pouco isso, posso olhar um intervalo específico? Posso, através de uma subquery. Eu quero olhar os últimos 5 minutos e quero ver apenas o último minuto. Está aqui, consigo fazer isso: logback_events_total[5m:1m].
 
 logback_events_total[5m:1m]




[13:02] Um minuto ficou muita coisa, quero olhar os 30 segundos. Também consigo: logback_events_total[5m:30s]. Então, dentro dos últimos 5 minutos, eu olhei os 30 segundos.

logback_events_total[5m:30s]





- Ao clicar em "Graph" na console do Prometheus:
Error executing query: invalid expression type "range vector" for range query, must be Scalar or instant Vector



hikaricp_connections_idle{application="app-forum-api",pool="HikariPool-1",} 10.0






<https://prometheus.io/docs/prometheus/latest/querying/basics/>
Expression language data types

In Prometheus's expression language, an expression or sub-expression can evaluate to one of four types:

    Instant vector - a set of time series containing a single sample for each time series, all sharing the same timestamp
    Range vector - a set of time series containing a range of data points over time for each time series
    Scalar - a simple numeric floating point value
    String - a simple string value; currently unused







# PENDENTE
- Reforçar aos 9:15
- Ler DOC do Prometheus:
    <https://prometheus.io/docs/prometheus/latest/querying/basics/>