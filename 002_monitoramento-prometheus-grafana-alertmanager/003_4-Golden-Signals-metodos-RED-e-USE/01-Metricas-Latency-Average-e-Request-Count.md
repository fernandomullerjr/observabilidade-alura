

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 003 - Aula  01 Métricas Latency Average e Request Count"
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
# 
# PENDENTE
- Ver mais sobre histogram_quantile
https://blog.cisne.dev/montando-5-graficos-com-uma-metrica-do-prometheus/
<https://blog.cisne.dev/montando-5-graficos-com-uma-metrica-do-prometheus/>





