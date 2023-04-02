

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 003 - Aula 01 Métricas Latency Average e Request Count"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 01 Métricas Latency Average e Request Count

# Transcrição
[00:00] Vamos dar sequência ao nosso curso. Nessa aula, vamos trabalhar com uma métrica bem importante, vamos começar a olhar para quantile, vamos começar a entender quanto porcento das nossas requisições estão em 100 milissegundos, 200, 300, e por aí vai.

[00:29] Vou adicionar um novo painel e a métrica que vamos trabalhar é http_server_requests_seconds_bucket e vou colocar os labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[01:06] O que mais entrar aqui em termos de label? Vamos fazer um seletor. Não quero olhar para o /actuator/prometheus, então coloco uri!=”/actuator/prometheus” e quero pegar uma faixa de tempo, vou pegar [1m] e vou trabalhar com o rate para me trazer a taxa em 1 minuto.

[01:40] Automaticamente, ele vai me trazer diversos resultados. Eu não tenho instant vector quando trabalho com 1 minuto, eu tenho diversos valores. Na verdade, agora tive um instant vector e vou trabalhá-lo fazendo uma agregação, porque são muitos valores e eu quero totalizar isso.

[02:08] Até aqui, nada mudou, estamos dentro da nossa zona de conforto, estamos trabalhando com o agregador sum, com o rate e temos uma faixa de tempo. O que vai mudar aqui?

[02:20] Vamos trabalhar com a função histogram_quantile. Essa função recebe como parâmetro um número percentual, 0.99, você coloca uma vírgula e ela recebe como segundo parâmetro o resultado do sum que escrevemos há pouco.

[02:43] Vou usar uma cláusula, vou usar um by e vou fazer less or equal, le. Ficou assim: histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri!=”/actuator/prometheus}[1m])) by (le)). Cometi algum erro, deixa eu entender onde está o meu erro.

[03:16] Foi aqui, histagram_quantile, escrevi errado o nome da função. O que estamos fazendo aqui? Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.

[03:34] Se formos olhar para o “localhost/metrics” e procurarmos por essa métrica http_server_requests_seconds_bucket, temos buckets com valores maiores ou iguais que 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além, de 1 segundo para cima.

[04:00] Então, eu quero entender onde que 99% das minhas requisições se enquadram, em qual bucket elas estão caindo. Vou copiar a minha consulta e vou colocar na legenda “99%”.

[04:31] Vou adicionar mais uma consulta e quero agora olhar 90%, 0.90, quero saber se tem uma diferenciação de tempo. Em outra consulta, vou colocar aqui 0.75, na legenda vou colocar também 75%. Também vou colocar 50%, 0.50. E, por último, vou colocar 25%, 0.25.

[05:21] Vou conseguir obter essa informação através desse painel. Vou chamar de “LATENCY AVERAGE” e vou colocar aqui na descrição “Latência média por minuto”.

[05:48] Na legenda, vou colocar em formado de tabela, vou colocar à direita; e, em valores, vou colocar o último valor coletado, acho que é o suficiente.

[06:04] Vou trabalhar a opacidade, vou colocar um gradiente, vou tirar os pontos e agora, descendo, em unidade, vou ter que trabalhar com a unidade de tempo, “Time”, e segundos, “seconds”, que aí tenho essa anotação interessante de milissegundos.

[06:30] Está aqui, já temos o nosso painel de latência. Vou colocar esse painel aqui, expandi-lo, ficou bem interessante. Vou deixá-lo assim, vou diminuir um pouco esses outros para tentar igualar no tamanho. Pronto, assim está bem interessante.

[07:11] Já temos aqui a nossa latência sendo registrada. Agora, sabemos que 99% das nossas requisições estão iguais ou menores a 131 milissegundos. Sabemos que 90% está igual ou menor que 73 milissegundos, e assim sucessivamente, é só seguir essa linha de raciocínio.

Página do Grafana, mostrando a *row* API RED. Há 5 indicadores: *Total Requests*, *Error 500*, *Error rate*, *Latency average* e *Response time*.

[07:38] Isso é muito importante para entendermos que estamos dentro de um objetivo de nível de serviço. Essa métrica, sendo melhor trabalhada, não olhando só para o último minuto, pode ser muito importante para você formar uma SLI.

[07:55] Agora, vamos trabalhar na composição de mais uma métrica, que é a contagem de requisições. Quero saber quantas requisições estou recebendo em cada endpoint.

[08:08] Vou trabalhar com http_requests_seconds_count, vou trabalhar com os meus labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[08:40] Agora, vou colocar um seletor porque quero olhar para endpoints específicos. Quero olhar para uri=”/topicos”} e quero pegar, dentro de /topicos, o meu último minuto, [1m].

[08:55] O que vou fazer aqui é olhar a taxa de crescimento, então vou usar o increase para pegar essa taxa de crescimento. Aqui, tenho dois valores retornados, porque também tenho exception voltando aqui. Na verdade, tenho SERVER_ERROR que foi o 500 quando bateu.

[09:17] Eu posso fazer uma agregação para juntar tudo isso utilizando o sum e ter uma linha só dentro dessa minha métrica. Vou colocar aqui /topicos como legenda. Vou copiar essa query, vou formar uma segunda query em que vou colocar uri="/topicos/(id)". Na legenda, vou colocar “/topicos/(id)”.

[09:58] Agora, vou também criar uma nova query e trabalhar com o uri="/auth", o endpoint de autenticação, sum(increase(http_requests_seconds_count{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri=”/auth”} [1m]). Feito, agora vamos trabalhar em como esse painel vai ficar, como vai ser a formatação dele.

[10:22] Vou colocar o título como “REQUEST COUNT”. Descrição será “Número de requisições por endpoint no último minuto”. Vou ter a visualização em forma tabular, a legenda vai ficar embaixo mesmo.

[11:02] Aqui, vou colocar o valor mínimo, o valor máximo, a média e o último valor não nulo. Agora, na parte de formatação, vou seguir o padrão de sempre – opacidade, gradiente, vou tirar os pontos, e é isso.

[11:30] Na parte de unidade, não tenho que mudar, porque preciso de um número inteiro, que é o número de requisições que eu sofri no último minuto. Então, está concluído.

Gráfico "*Request Count*". O eixo x mostra a passagem de tempo de 1 em 1 minuto, o eixo y apresenta os valores 20 e 40. A linha verde (referente a /topicos) oscila próximo do valor 40. A linha amarela (/topicos/(id)) oscila próximo de 20. A linha azul (/auth) oscila abaixo de 20.

[11:46] Vamos olhar para esse painel. Vou trazê-lo para baixo e vou aumentar o tamanho dele para não ter que usar scroll. Está correto.

[12:06] Encerramos essa aula aqui, nós configuramos a nossa média de latência, o Latency Average, trabalhamos com Request Count e, na próxima aula, vamos trabalhar com a duração das requisições.

[12:21] Vamos criar mais dois painéis: um com a média de duração de requisição e outro com a duração máxima. Nos vemos na próxima aula.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 01 Métricas Latency Average e Request Count

# Transcrição
[00:00] Vamos dar sequência ao nosso curso. Nessa aula, vamos trabalhar com uma métrica bem importante, vamos começar a olhar para quantile, vamos começar a entender quanto porcento das nossas requisições estão em 100 milissegundos, 200, 300, e por aí vai.

[00:29] Vou adicionar um novo painel e a métrica que vamos trabalhar é http_server_requests_seconds_bucket e vou colocar os labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[01:06] O que mais entrar aqui em termos de label? Vamos fazer um seletor. Não quero olhar para o /actuator/prometheus, então coloco uri!=”/actuator/prometheus” e quero pegar uma faixa de tempo, vou pegar [1m] e vou trabalhar com o rate para me trazer a taxa em 1 minuto.

[01:40] Automaticamente, ele vai me trazer diversos resultados. Eu não tenho instant vector quando trabalho com 1 minuto, eu tenho diversos valores. Na verdade, agora tive um instant vector e vou trabalhá-lo fazendo uma agregação, porque são muitos valores e eu quero totalizar isso.

[02:08] Até aqui, nada mudou, estamos dentro da nossa zona de conforto, estamos trabalhando com o agregador sum, com o rate e temos uma faixa de tempo. O que vai mudar aqui?

- Iremos usar a métrica:
http_server_requests_seconds_bucket

- Metrics Browser:
sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m]))




[02:20] Vamos trabalhar com a função histogram_quantile. Essa função recebe como parâmetro um número percentual, 0.99, você coloca uma vírgula e ela recebe como segundo parâmetro o resultado do sum que escrevemos há pouco.

[02:43] Vou usar uma cláusula, vou usar um by e vou fazer less or equal, le. Ficou assim: histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri!=”/actuator/prometheus}[1m])) by (le)). Cometi algum erro, deixa eu entender onde está o meu erro.

- Exemplo do professor:
histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri!=”/actuator/prometheus}[1m])) by (le))

- Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.
    usando a função histagram_quantile
- Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.
    usando a função histagram_quantile
- Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.
    usando a função histagram_quantile
- Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.

- Metrics Browser editado:
histogram_quantile(0.99, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))


[03:16] Foi aqui, histagram_quantile, escrevi errado o nome da função. O que estamos fazendo aqui? Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.

[03:34] Se formos olhar para o “localhost/metrics” e procurarmos por essa métrica http_server_requests_seconds_bucket, temos buckets com valores maiores ou iguais que 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além, de 1 segundo para cima.

https://prometheus.io/docs/prometheus/latest/querying/functions/#histogram_quantile
<https://prometheus.io/docs/prometheus/latest/querying/functions/#histogram_quantile>

[03:16] Foi aqui, histagram_quantile, escrevi errado o nome da função. O que estamos fazendo aqui? Eu quero descobrir em qual bucket 99% das minhas requisições se enquadram.

[03:34] Se formos olhar para o “localhost/metrics” e procurarmos por essa métrica http_server_requests_seconds_bucket, temos buckets com valores maiores ou iguais que 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além, de 1 segundo para cima.

[04:00] Então, eu quero entender onde que 99% das minhas requisições se enquadram, em qual bucket elas estão caindo. Vou copiar a minha consulta e vou colocar na legenda “99%”.


- Endpoint das métricas:
http://192.168.92.129/metrics
<http://192.168.92.129/metrics>

Se formos olhar para o “localhost/metrics” e procurarmos por essa métrica http_server_requests_seconds_bucket, temos buckets com valores maiores ou iguais que 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além, de 1 segundo para cima.
Então, eu quero entender onde que 99% das minhas requisições se enquadram, em qual bucket elas estão caindo. 

http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.05",} 1859.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.1",} 1861.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.2",} 1862.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.3",} 1862.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.5",} 1863.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="1.0",} 1863.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="+Inf",} 1863.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",} 1863.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",} 14.3191498
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.05",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.1",} 157.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.2",} 639.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.3",} 644.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="0.5",} 645.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="1.0",} 646.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",le="+Inf",} 646.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",} 646.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="POST",outcome="SUCCESS",status="200",uri="/auth",} 74.01994496
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.05",} 4431.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.1",} 4432.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.2",} 4433.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.3",} 4434.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="0.5",} 4434.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="1.0",} 4435.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",le="+Inf",} 4435.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 4435.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 14.18113426
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.05",} 1001.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.1",} 1001.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.2",} 1002.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.3",} 1002.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="0.5",} 1002.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="1.0",} 1002.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/prometheus",le="+Inf",} 1002.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.05",} 0.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.1",} 51.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.2",} 187.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.3",} 188.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="0.5",} 188.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="1.0",} 188.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",le="+Inf",} 188.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",} 188.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="POST",outcome="CLIENT_ERROR",status="400",uri="/auth",} 21.626248505
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.05",} 3466.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.1",} 3467.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.2",} 3468.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.3",} 3468.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="0.5",} 3468.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="1.0",} 3468.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",le="+Inf",} 3468.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",} 3468.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos",} 10.282188146
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.05",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.1",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.2",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.3",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="0.5",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="1.0",} 143.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",le="+Inf",} 143.0
http_server_requests_seconds_count{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",} 143.0
http_server_requests_seconds_sum{application="app-forum-api",exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/topicos/{id}",} 0.744249603



- Vou copiar a minha consulta e vou colocar na legenda “99%”.


- Adicionar mais uma Query, agora para descobrir as que se enquadram em 90% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.90, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~


- Adicionar mais uma Query, agora para descobrir as que se enquadram em 75% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.75, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~



- Adicionar mais uma Query, agora para descobrir as que se enquadram em 50% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.50, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~



- Adicionar mais uma Query, agora para descobrir as que se enquadram em 25% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.25, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~



[04:00] Então, eu quero entender onde que 99% das minhas requisições se enquadram, em qual bucket elas estão caindo. Vou copiar a minha consulta e vou colocar na legenda “99%”.

[04:31] Vou adicionar mais uma consulta e quero agora olhar 90%, 0.90, quero saber se tem uma diferenciação de tempo. Em outra consulta, vou colocar aqui 0.75, na legenda vou colocar também 75%. Também vou colocar 50%, 0.50. E, por último, vou colocar 25%, 0.25.

[05:21] Vou conseguir obter essa informação através desse painel. Vou chamar de “LATENCY AVERAGE” e vou colocar aqui na descrição “Latência média por minuto”.

- Nome do painel:
LATENCY AVERAGE

[05:48] Na legenda, vou colocar em formado de tabela, vou colocar à direita; e, em valores, vou colocar o último valor coletado, acho que é o suficiente.

[06:04] Vou trabalhar a opacidade, vou colocar um gradiente, vou tirar os pontos e agora, descendo, em unidade, vou ter que trabalhar com a unidade de tempo, “Time”, e segundos, “seconds”, que aí tenho essa anotação interessante de milissegundos.

[06:30] Está aqui, já temos o nosso painel de latência. Vou colocar esse painel aqui, expandi-lo, ficou bem interessante. Vou deixá-lo assim, vou diminuir um pouco esses outros para tentar igualar no tamanho. Pronto, assim está bem interessante.

[07:11] Já temos aqui a nossa latência sendo registrada. Agora, sabemos que 99% das nossas requisições estão iguais ou menores a 131 milissegundos. Sabemos que 90% está igual ou menor que 73 milissegundos, e assim sucessivamente, é só seguir essa linha de raciocínio.

Página do Grafana, mostrando a *row* API RED. Há 5 indicadores: *Total Requests*, *Error 500*, *Error rate*, *Latency average* e *Response time*.







# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 
# Dia 01/04/2023

- Subindo stack da aula, usando stack do Curso2:
cd /home/fernando/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01
docker-compose up -d

- Grafana acessível:
http://192.168.0.110:3000
<http://192.168.0.110:3000>

- Usuário e senha
admin
nemsei90



- Endpoint das métricas:
http://192.168.0.110/metrics





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# # PENDENTE

- Ver mais sobre histogram_quantile. Usar ChatGPT para auxiliar no entendimento.
site abaixo tem exemplos:
https://blog.cisne.dev/montando-5-graficos-com-uma-metrica-do-prometheus/
<https://blog.cisne.dev/montando-5-graficos-com-uma-metrica-do-prometheus/>

- Organizar Favoritos






# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 
# Dia 02/04/2023


4:47


[05:21] Vou conseguir obter essa informação através desse painel. Vou chamar de “LATENCY AVERAGE” e vou colocar aqui na descrição “Latência média por minuto”.

[05:48] Na legenda, vou colocar em formado de tabela, vou colocar à direita; e, em valores, vou colocar o último valor coletado, acho que é o suficiente.
5:48

- Editando o painel "Latency Average":
colocando a descrição:
Latencia média por minuto



- Ajustando o formato do Painel

editando as opções da legenda:

Mode:
Table

Placement:
Right

Values:
Last *
esta opção vai trazer o último valor existente



- Ajustando demais opções do Painel:

Fill opacity:
70

Gradient Mode:
Opacity

Show points:
Never



- Ajustando Standard Options
no campo Unit, vamos usar unidade de tempo, Time, segundos
esta opção vai trazer a notação em milisegundos, bem interessante

Unit:
Time > seconds(s)





Save dashboard
dash-forum-api

Ajustado o painel Latency Average. Ajustada opacidade, legenda em formato tabela e medida em segundos.






[06:30] Está aqui, já temos o nosso painel de latência. Vou colocar esse painel aqui, expandi-lo, ficou bem interessante. Vou deixá-lo assim, vou diminuir um pouco esses outros para tentar igualar no tamanho. Pronto, assim está bem interessante.

[07:11] Já temos aqui a nossa latência sendo registrada. Agora, sabemos que 99% das nossas requisições estão iguais ou menores a 131 milissegundos. Sabemos que 90% está igual ou menor que 73 milissegundos, e assim sucessivamente, é só seguir essa linha de raciocínio.

- No meu caso, 99% das requisições estão iguais ou menos a 173 milisegundos, e assim por diante:

	Last *
99%
	173 ms
90%
	47.6 ms
75%
	39.7 ms
50%
	26.5 ms
25%
	13.2 ms








[07:38] Isso é muito importante para entendermos que estamos dentro de um objetivo de nível de serviço. Essa métrica, sendo melhor trabalhada, não olhando só para o último minuto, pode ser muito importante para você formar uma SLI.

[07:55] Agora, vamos trabalhar na composição de mais uma métrica, que é a contagem de requisições. Quero saber quantas requisições estou recebendo em cada endpoint.

[08:08] Vou trabalhar com http_requests_seconds_count, vou trabalhar com os meus labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[08:08] Vou trabalhar com http_requests_seconds_count, vou trabalhar com os meus labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.


[08:40] Agora, vou colocar um seletor porque quero olhar para endpoints específicos. Quero olhar para uri=”/topicos”} e quero pegar, dentro de /topicos, o meu último minuto, [1m].

[08:55] O que vou fazer aqui é olhar a taxa de crescimento, então vou usar o increase para pegar essa taxa de crescimento. Aqui, tenho dois valores retornados, porque também tenho exception voltando aqui. Na verdade, tenho SERVER_ERROR que foi o 500 quando bateu.

[09:17] Eu posso fazer uma agregação para juntar tudo isso utilizando o sum e ter uma linha só dentro dessa minha métrica. Vou colocar aqui /topicos como legenda. Vou copiar essa query, vou formar uma segunda query em que vou colocar uri="/topicos/(id)". Na legenda, vou colocar “/topicos/(id)”.

- Criar um novo painel
usar a métrica:
    http_requests_seconds_count
adicionar os labels de sempre
    {application="$application", instance="$instance", job="app-forum-api", "}
Quero olhar para uri=”/topicos”} e quero pegar, dentro de /topicos, o meu último minuto, [1m].
    uri="/topicos"}[1m]
O que vou fazer aqui é olhar a taxa de crescimento, então vou usar o increase para pegar essa taxa de crescimento
    increase

- Ficando assim a query:

~~~~bash
increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m])
~~~~




- Usando opção "explain" no Query Editor:

http_requests_seconds_count {application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}
Fetch all series matching metric name and label filters.

increase(<expr>[1m])

Calculates the increase in the time series in the range vector. Breaks in monotonicity (such as counter resets due to target restarts) are automatically adjusted for. The increase is extrapolated to cover the full time range as specified in the range vector selector, so that it is possible to get a non-integer result even if a counter increases only by integer increments.








- TSHOOT
porque esta métrica não está mostrando nada no painel do Prometheus?

increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m])

A métrica que você está tentando visualizar no painel do Prometheus é um contador que conta a quantidade de requisições HTTP para uma determinada URI em um período de tempo específico.

A expressão que você usou na consulta Prometheus é increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m]), que utiliza a função increase() para calcular a taxa de aumento do contador no último minuto.

Se essa métrica não está aparecendo no painel do Prometheus, pode ser devido a alguns motivos, como:

    O nome da métrica ou as tags usadas na consulta estão incorretos
    O período de tempo especificado na consulta não está cobrindo a janela de tempo em que os dados estão sendo coletados
    O coletor de métricas não está configurado corretamente ou não está coletando os dados de forma adequada
    A instância ou job que você está consultando não está gerando nenhum dado para essa métrica

Para verificar se a métrica está sendo coletada corretamente, você pode verificar o status do coletor de métricas que está sendo usado para coletar esses dados, bem como as configurações e tags usadas na definição da métrica.

Além disso, você pode tentar simplificar a expressão Prometheus e remover as tags application e instance para ver se a métrica está sendo coletada de forma geral. Por exemplo, você pode usar a expressão increase(http_requests_seconds_count{job="app-forum-api", uri="/topicos"}[1m]) para contar o número de requisições HTTP para a URI "/topicos" no último minuto.





sum(increase(http_requests_seconds_count{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri=”/auth”} [1m])


increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m])

sum(increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"} [1m]))



- Painel só apresenta "No data" ao formar as queries:
No data



- Erros ao tentar curl para os endpoints:
talvez não tenha dados devido isto

~~~~bash
fernando@debian10x64:~$
fernando@debian10x64:~$ curl -v http://192.168.0.110:8080/topicos/1
* Expire in 0 ms for 6 (transfer 0x55da35580fb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55da35580fb0)
* connect to 192.168.0.110 port 8080 failed: Connection refused
* Failed to connect to 192.168.0.110 port 8080: Connection refused
* Closing connection 0
curl: (7) Failed to connect to 192.168.0.110 port 8080: Connection refused
fernando@debian10x64:~$ curl -v http://192.168.0.110:8080/topicos/2
* Expire in 0 ms for 6 (transfer 0x55faf8570fb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55faf8570fb0)
* connect to 192.168.0.110 port 8080 failed: Connection refused
* Failed to connect to 192.168.0.110 port 8080: Connection refused
* Closing connection 0
curl: (7) Failed to connect to 192.168.0.110 port 8080: Connection refused
fernando@debian10x64:~$ curl -v http://192.168.0.110:8080/topicos/3
* Expire in 0 ms for 6 (transfer 0x55839a734fb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55839a734fb0)
* connect to 192.168.0.110 port 8080 failed: Connection refused
* Failed to connect to 192.168.0.110 port 8080: Connection refused
* Closing connection 0
curl: (7) Failed to connect to 192.168.0.110 port 8080: Connection refused
fernando@debian10x64:~$
~~~~




- Logs do container do Grafana:

docker logs grafana-forum-api -f --tail 22

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$ docker logs grafana-forum-api -f --tail 22
logger=tsdb.prometheus t=2023-04-02T18:16:29.979803687Z level=error msg="Range query failed" query="sum(increase(http_requests_seconds_count{application=\"app-forum-api\", instance=\"app-forum-api:8080\", job=\"app-forum-api\", uri=\"/topicos\"} [1m])" error="bad_data: 1:144: parse error: unclosed left parenthesis" detail=
logger=context userId=1 orgId=1 uname=admin t=2023-04-02T18:16:29.980055383Z level=info msg="Request Completed" method=POST path=/api/ds/query status=400 remote_addr=192.168.0.109 time_ms=3 duration=3.151656ms size=97 referer="http://192.168.0.110:3000/d/M7JCg3d4k/dash-forum-api?editPanel=32&orgId=1" handler=/api/ds/query
logger=tsdb.prometheus t=2023-04-02T18:16:30.03557203Z level=error msg="Range query failed" query="sum(increase(http_requests_seconds_count{application=\"app-forum-api\", instance=\"app-forum-api:8080\", job=\"app-forum-api\", uri=\"/topicos\"} [1m])" error="bad_data: 1:144: parse error: unclosed left parenthesis" detail=
logger=context userId=1 orgId=1 uname=admin t=2023-04-02T18:16:30.035690158Z level=info msg="Request Completed" method=POST path=/api/ds/query status=400 remote_addr=192.168.0.109 time_ms=2 duration=2.223961ms size=97 referer="http://192.168.0.110:3000/d/M7JCg3d4k/dash-forum-api?editPanel=32&orgId=1" handler=/api/ds/query
logger=cleanup t=2023-04-02T18:19:32.703753424Z level=info msg="Completed cleanup jobs" duration=6.072401ms
logger=cleanup t=2023-04-02T18:29:32.706275917Z level=info msg="Completed cleanup jobs" duration=9.387226ms
logger=cleanup t=2023-04-02T18:39:32.704497226Z level=info msg="Completed cleanup jobs" duration=6.75785ms
logger=cleanup t=2023-04-02T18:49:32.700962832Z level=info msg="Completed cleanup jobs" duration=4.026247ms
logger=cleanup t=2023-04-02T18:59:32.700277835Z level=info msg="Completed cleanup jobs" duration=3.973002ms
logger=cleanup t=2023-04-02T19:09:32.700074294Z level=info msg="Completed cleanup jobs" duration=4.026259ms
logger=cleanup t=2023-04-02T19:19:32.70027464Z level=info msg="Completed cleanup jobs" duration=3.528801ms
logger=cleanup t=2023-04-02T19:29:32.700090801Z level=info msg="Completed cleanup jobs" duration=3.510509ms
logger=cleanup t=2023-04-02T19:39:32.70015702Z level=info msg="Completed cleanup jobs" duration=3.679948ms
logger=cleanup t=2023-04-02T19:49:32.701690257Z level=info msg="Completed cleanup jobs" duration=4.088924ms
logger=cleanup t=2023-04-02T19:59:32.702344599Z level=info msg="Completed cleanup jobs" duration=4.754899ms
logger=cleanup t=2023-04-02T20:09:32.701702697Z level=info msg="Completed cleanup jobs" duration=4.047531ms
logger=cleanup t=2023-04-02T20:19:32.700146109Z level=info msg="Completed cleanup jobs" duration=3.750179ms
logger=cleanup t=2023-04-02T20:29:32.70510394Z level=info msg="Completed cleanup jobs" duration=7.910009ms
logger=cleanup t=2023-04-02T20:39:32.700449568Z level=info msg="Completed cleanup jobs" duration=4.077806ms
logger=cleanup t=2023-04-02T20:49:32.702951587Z level=info msg="Completed cleanup jobs" duration=4.970326ms
logger=cleanup t=2023-04-02T20:59:32.701660236Z level=info msg="Completed cleanup jobs" duration=4.597479ms
logger=cleanup t=2023-04-02T21:09:32.701370875Z level=info msg="Completed cleanup jobs" duration=4.440952ms
~~~~







docker logs app-forum-api -f --tail 22


docker logs client-forum-api -f --tail 22


docker logs client-forum-api -f --tail 22
docker logs prometheus-forum-api -f --tail 22
docker logs proxy-forum-api -f --tail 22
docker logs client-forum-api -f --tail 22



- Logs no Container Proxy:

docker logs proxy-forum-api -f --tail 22

~~~~bash
fernando@debian10x64:~$ docker logs proxy-forum-api -f --tail 22
/docker-entrypoint.sh: Launching /docker-entrypoint.d/30-tune-worker-processes.sh
/docker-entrypoint.sh: Configuration complete; ready for start up
2023/04/02 16:29:31 [error] 30#30: *1 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "POST /auth HTTP/1.1", upstream: "http://172.21.0.2:8080/auth", host: "proxy-forum-api"
2023/04/02 16:29:32 [error] 30#30: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/2 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/2", host: "proxy-forum-api"
2023/04/02 16:29:33 [error] 30#30: *5 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/3 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/3", host: "proxy-forum-api"
2023/04/02 16:29:34 [error] 30#30: *7 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "POST /auth HTTP/1.1", upstream: "http://172.21.0.2:8080/auth", host: "proxy-forum-api"
2023/04/02 16:29:35 [error] 30#30: *9 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/1 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/1", host: "proxy-forum-api"
2023/04/02 16:29:35 [error] 30#30: *11 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/1 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/1", host: "proxy-forum-api"
2023/04/02 16:29:36 [error] 30#30: *13 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:37 [error] 30#30: *15 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:38 [error] 30#30: *17 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:38 [error] 30#30: *19 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:39 [error] 30#30: *21 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/3 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/3", host: "proxy-forum-api"
2023/04/02 16:29:40 [error] 30#30: *23 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:41 [error] 30#30: *25 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:42 [error] 30#30: *27 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:42 [error] 30#30: *29 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/3 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/3", host: "proxy-forum-api"
2023/04/02 16:29:43 [error] 30#30: *31 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:44 [error] 30#30: *33 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:45 [error] 30#30: *35 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/2 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/2", host: "proxy-forum-api"
2023/04/02 16:29:45 [error] 30#30: *37 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos", host: "proxy-forum-api"
2023/04/02 16:29:46 [error] 30#30: *39 connect() failed (111: Connection refused) while connecting to upstream, client: 172.22.0.3, server: _, request: "GET /topicos/1 HTTP/1.1", upstream: "http://172.21.0.2:8080/topicos/1", host: "proxy-forum-api"
^C
fernando@debian10x64:~$
~~~~












- Erro durante curl era devido a porta 8080, projeto atual escuta na porta 80, conforme container do Proxy:

~~~~bash
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$ curl -v http://192.168.0.110:80/topicos/1
* Expire in 0 ms for 6 (transfer 0x563e3dde8fb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x563e3dde8fb0)
* Connected to 192.168.0.110 (192.168.0.110) port 80 (#0)
> GET /topicos/1 HTTP/1.1
> Host: 192.168.0.110
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 02 Apr 2023 21:31:07 GMT
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
* Connection #0 to host 192.168.0.110 left intact
{"id":1,"titulo":"Duvida 1","mensagem":"Erro ao criar projeto","dataCriacao":"2019-05-05T18:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$ curl -v http://192.168.0.110:80/topicos/2
* Expire in 0 ms for 6 (transfer 0x5642c14fdfb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x5642c14fdfb0)
* Connected to 192.168.0.110 (192.168.0.110) port 80 (#0)
> GET /topicos/2 HTTP/1.1
> Host: 192.168.0.110
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 02 Apr 2023 21:31:07 GMT
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
* Connection #0 to host 192.168.0.110 left intact
{"id":2,"titulo":"Duvida 2","mensagem":"Projeto nao compila","dataCriacao":"2019-05-05T19:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$ curl -v http://192.168.0.110:80/topicos/3
* Expire in 0 ms for 6 (transfer 0x55b76de0afb0)
*   Trying 192.168.0.110...
* TCP_NODELAY set
* Expire in 200 ms for 4 (transfer 0x55b76de0afb0)
* Connected to 192.168.0.110 (192.168.0.110) port 80 (#0)
> GET /topicos/3 HTTP/1.1
> Host: 192.168.0.110
> User-Agent: curl/7.64.0
> Accept: */*
>
< HTTP/1.1 200
< Server: nginx
< Date: Sun, 02 Apr 2023 21:31:08 GMT
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
* Connection #0 to host 192.168.0.110 left intact
{"id":3,"titulo":"Duvida 3","mensagem":"Tag HTML","dataCriacao":"2019-05-05T20:00:00","nomeAutor":"Aluno","status":"NAO_RESPONDIDO","respostas":[]}fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$ docker ps
CONTAINER ID   IMAGE                    COMMAND                  CREATED       STATUS                 PORTS                                       NAMES
2810038281ef   client-forum-api         "/scripts/client.sh"     5 hours ago   Up 5 hours                                                         client-forum-api
57527b3d5197   grafana/grafana          "/run.sh"                5 hours ago   Up 5 hours             0.0.0.0:3000->3000/tcp, :::3000->3000/tcp   grafana-forum-api
2fbf7bb4b469   prom/prometheus:latest   "/bin/prometheus --c…"   5 hours ago   Up 5 hours             0.0.0.0:9090->9090/tcp, :::9090->9090/tcp   prometheus-forum-api
025c15588abe   nginx                    "/docker-entrypoint.…"   5 hours ago   Up 5 hours             0.0.0.0:80->80/tcp, :::80->80/tcp           proxy-forum-api
86dd81e3535b   app-forum-api            "java -Xms128M -Xmx1…"   5 hours ago   Up 5 hours (healthy)                                               app-forum-api
0f4fb1da5331   mysql:5.7                "docker-entrypoint.s…"   5 hours ago   Up 5 hours                                                         mysql-forum-api
5c685991d5cd   redis                    "docker-entrypoint.s…"   5 hours ago   Up 5 hours                                                         redis-forum-api
fernando@debian10x64:~/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01$
~~~~






- Query segue com "No Data" no Prometheus.

~~~~bash
Painel do Prometheus segue acusando "No data". 

Se o painel do Prometheus está acusando "No data", pode haver alguns motivos pelos quais os dados não estão sendo exibidos corretamente. Algumas possíveis causas e soluções incluem:

    Não há dados para exibir: Verifique se os dados estão sendo coletados e se estão disponíveis para consulta. Você pode verificar isso executando uma consulta manualmente no Prometheus, usando a interface de consulta (Query) do Prometheus.

    A consulta (Query) está incorreta: Verifique se a consulta Prometheus que você está usando no painel está correta. Certifique-se de que a consulta está retornando resultados e que não há erros de sintaxe na consulta.

    O tempo especificado na consulta não corresponde aos dados coletados: Verifique se o período de tempo que você está especificando na consulta Prometheus corresponde ao período de tempo em que os dados foram coletados. Certifique-se de que os dados que você está tentando visualizar não estão fora do período de tempo que você especificou.

    A fonte de dados (Data source) está incorreta ou não foi configurada corretamente: Verifique se a fonte de dados que você está usando no painel está configurada corretamente e está apontando para o endereço correto do Prometheus. Certifique-se também de que o tipo de consulta que você está usando no painel corresponde ao tipo de dados que você está tentando visualizar.

    A métrica ou as tags usadas na consulta estão incorretas: Verifique se o nome da métrica e as tags que você está usando na consulta Prometheus estão corretas. Certifique-se de que as tags estão sendo usadas corretamente na consulta e que estão retornando resultados.

    O painel está configurado incorretamente: Verifique se o painel está configurado corretamente e se todas as opções e configurações estão corretas. Certifique-se de que o painel está usando a fonte de dados correta e que todas as consultas estão configuradas corretamente.

Espero que essas dicas ajudem você a resolver o problema de "No data" no painel do Prometheus. Se você ainda estiver enfrentando problemas, tente revisar sua configuração do Prometheus e do painel em busca de outras possíveis causas de problemas.
~~~~


porque esta métrica não está mostrando nada no painel do Prometheus?

increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m])

A métrica que você está tentando visualizar no painel do Prometheus é um contador que conta a quantidade de requisições HTTP para uma determinada URI em um período de tempo específico.

A expressão que você usou na consulta Prometheus é increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m]), que utiliza a função increase() para calcular a taxa de aumento do contador no último minuto.

Se essa métrica não está aparecendo no painel do Prometheus, pode ser devido a alguns motivos, como:

    O nome da métrica ou as tags usadas na consulta estão incorretos
    O período de tempo especificado na consulta não está cobrindo a janela de tempo em que os dados estão sendo coletados
    O coletor de métricas não está configurado corretamente ou não está coletando os dados de forma adequada
    A instância ou job que você está consultando não está gerando nenhum dado para essa métrica

Para verificar se a métrica está sendo coletada corretamente, você pode verificar o status do coletor de métricas que está sendo usado para coletar esses dados, bem como as configurações e tags usadas na definição da métrica.

Além disso, você pode tentar simplificar a expressão Prometheus e remover as tags application e instance para ver se a métrica está sendo coletada de forma geral. Por exemplo, você pode usar a expressão increase(http_requests_seconds_count{job="app-forum-api", uri="/topicos"}[1m]) para contar o número de requisições HTTP para a URI "/topicos" no último minuto.





- Exemplo de uma OK
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m]) / rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])


- Editada:
increase(http_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m])

- Não funcionou.


- Verificando o Endpoint de métricas:
http://192.168.0.110/metrics
Não consta esta métrica:
http_requests_seconds_count


- Nome da métrica estava errado.
DE:
http_requests_seconds_count
PARA:
http_server_requests_seconds_count








### Retomando



[07:38] Isso é muito importante para entendermos que estamos dentro de um objetivo de nível de serviço. Essa métrica, sendo melhor trabalhada, não olhando só para o último minuto, pode ser muito importante para você formar uma SLI.

[07:55] Agora, vamos trabalhar na composição de mais uma métrica, que é a contagem de requisições. Quero saber quantas requisições estou recebendo em cada endpoint.

[08:08] Vou trabalhar com http_requests_seconds_count, vou trabalhar com os meus labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[08:08] Vou trabalhar com http_requests_seconds_count, vou trabalhar com os meus labels de sempre, {application=”$application”, instance=”$instance”, job=”app-forum-api”}.


[08:40] Agora, vou colocar um seletor porque quero olhar para endpoints específicos. Quero olhar para uri=”/topicos”} e quero pegar, dentro de /topicos, o meu último minuto, [1m].

[08:55] O que vou fazer aqui é olhar a taxa de crescimento, então vou usar o increase para pegar essa taxa de crescimento. Aqui, tenho dois valores retornados, porque também tenho exception voltando aqui. Na verdade, tenho SERVER_ERROR que foi o 500 quando bateu.

[09:17] Eu posso fazer uma agregação para juntar tudo isso utilizando o sum e ter uma linha só dentro dessa minha métrica. Vou colocar aqui /topicos como legenda. Vou copiar essa query, vou formar uma segunda query em que vou colocar uri="/topicos/(id)". Na legenda, vou colocar “/topicos/(id)”.

- Criar um novo painel
usar a métrica:
    http_server_requests_seconds_count
adicionar os labels de sempre
    {application="$application", instance="$instance", job="app-forum-api", "}
Quero olhar para uri=”/topicos”} e quero pegar, dentro de /topicos, o meu último minuto, [1m].
    uri="/topicos"}[1m]
O que vou fazer aqui é olhar a taxa de crescimento, então vou usar o increase para pegar essa taxa de crescimento
    increase
Eu posso fazer uma agregação para juntar tudo isso utilizando o sum e ter uma linha só dentro dessa minha métrica
    sum


- Ficando assim a query:

~~~~bash
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos"}[1m]))
~~~~

- Legenda
/topicos



- Adicionar uma segunda query, usando {id} após /topicos:

~~~~bash
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/topicos/{id}"}[1m]))
~~~~

- Legenda
/topicos/{id}



- Adicionar outra query, usando o path de autenticação /auth:

~~~~bash
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri="/auth"}[1m]))
~~~~

- Legenda
/auth






[09:58] Agora, vou também criar uma nova query e trabalhar com o uri="/auth", o endpoint de autenticação, sum(increase(http_requests_seconds_count{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri=”/auth”} [1m]). Feito, agora vamos trabalhar em como esse painel vai ficar, como vai ser a formatação dele.

[10:22] Vou colocar o título como “REQUEST COUNT”. Descrição será “Número de requisições por endpoint no último minuto”. Vou ter a visualização em forma tabular, a legenda vai ficar embaixo mesmo.

[11:02] Aqui, vou colocar o valor mínimo, o valor máximo, a média e o último valor não nulo. Agora, na parte de formatação, vou seguir o padrão de sempre – opacidade, gradiente, vou tirar os pontos, e é isso.

[11:30] Na parte de unidade, não tenho que mudar, porque preciso de um número inteiro, que é o número de requisições que eu sofri no último minuto. Então, está concluído.

Gráfico "*Request Count*". O eixo x mostra a passagem de tempo de 1 em 1 minuto, o eixo y apresenta os valores 20 e 40. A linha verde (referente a /topicos) oscila próximo do valor 40. A linha amarela (/topicos/(id)) oscila próximo de 20. A linha azul (/auth) oscila abaixo de 20.

[11:46] Vamos olhar para esse painel. Vou trazê-lo para baixo e vou aumentar o tamanho dele para não ter que usar scroll. Está correto.

[12:06] Encerramos essa aula aqui, nós configuramos a nossa média de latência, o Latency Average, trabalhamos com Request Count e, na próxima aula, vamos trabalhar com a duração das requisições.

[12:21] Vamos criar mais dois painéis: um com a média de duração de requisição e outro com a duração máxima. Nos vemos na próxima aula.


- Criando novo painel:

Nome:
REQUEST COUNT

descrição:
Número de requisições por endpoint no último minuto.

Legenda:
visualização em forma tabular, a legenda vai ficar embaixo mesmo.

Legend values:
vou colocar o valor mínimo, o valor máximo, a média e o último valor não nulo.

Formatação:
opacidade, gradiente, vou tirar os pontos

Unit:
Na parte de unidade, não tenho que mudar, porque preciso de um número inteiro, que é o número de requisições que eu sofri no último minuto. Então, está concluído.