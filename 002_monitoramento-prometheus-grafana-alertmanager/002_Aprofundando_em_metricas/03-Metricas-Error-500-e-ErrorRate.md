

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula 03 Métricas Error 500 e Error Rate "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Métricas Error 500 e Error Rate

# Transcrição
[00:00] Vamos dar sequência no nosso curso. Nessa aula, a nossa missão é configurar dois painéis: um painel com o número de erros 500 que estão ocorrendo na aplicação em um período de tempo e outro painel que vai ter a nossa taxa de erros, não só focada em erro 500.

[00:24] Vamos começar. Vou adicionar um painel, a métrica que vou utilizar é a http_server_requests_seconds_count. Aqui, vou colocar alguns labels, vou colocar o {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[00:59] Agora, vou fazer um seletor de negação para negar tudo que for uri!=”/actuator/prometheus”. Por que vou fazer isso? Vamos imaginar uma situação que vai causar um erro 500 para nós: o database caiu.

[01:20] O database caiu, vamos ter uma taxa de erros 500, só que o /actuator/prometheus não vai ficar fora, ele vai continuar retornando 200, e como eu quero olhar especificamente para erro 500, eu tenho que tirar esse endpoint.

[01:37] O status=”500”. Então, se eu colocar aqui, tem uma quantidade de erros 500 que estou tendo no momento, é até grande, então, aparentemente, estou com alguma coisa derrubada. Deixa eu entender.

[02:00] Já "startei" o redis e o mysql, mas ainda tem uma quantidade de erros 500 sendo exibida aqui. O que eu posso fazer aqui, além disso, para facilitar a nossa vida e compor uma métrica que realmente funcione?

[02:22] Eu preciso olhar para um espaço de tempo e vou olhar para o último minuto. Automaticamente, quando eu faço isso, eu não tenho mais uma visualização gráfica, porque eu acabei de criar um range vector e preciso de um dado Scalar ou de um instant vector.

[02:47] O que vou trabalhar aqui é a taxa de crescimento. Então, vou pegar a taxa de crescimento, o increase, do último minuto, e agora tenho uma métrica que reflete a realidade. Não tem nenhum erro 500 acontecendo no último minuto.

[03:07] Porém, se vocês notarem, eu tenho duas séries temporais sendo exibidas, eu tenho esses dois time series aqui. Por que tenho dois? Porque tenho exceções diferentes.

[03:20] Aqui são os labels, Exception é um label, não estamos usando. Eu tenho um que é CannotCreateTransationException e outro que é QueryTimeoutException.

[03:33] O que eu preciso fazer para ter um número inteiro único que traga para mim, que reflita a quantidade de erros 500? Eu tenho que fazer uma agregação. Então, vou trabalhar com o sum para fazer essa agregação e transformar isso em uma métrica só.

[03:52] Eu não tenho nenhuma informação sendo "plotada", é óbvio, porque não tenho nenhum erro 500. Se eu mudar isso para status=”200”, vou ter a visualização de um número. Ele não me retornou nada, é um bug do Grafana.

[04:10] Vou copiar essa consulta, vou sair, vou remover esse dash – isso está acontecendo frequentemente nessa versão – vou adicionar um novo painel, vou colar a nova consulta e está aí.

[04:32] Está aqui a informação, essa é com erro “200 OK”, vou mudar para 500, vai chegar a nível 0 porque não tem erros 500 no momento.

[04:46] A legenda, não vou trabalhar porque a visualização que vamos usar é do tipo “Stat”. Vou colocar aqui “ERROR 500” como título, “Número de erros 500 no último minuto” como descrição.

[05:12] Descendo aqui, o cálculo vai ser em cima do último valor não nulo; o tipo de campo é numérico mesmo; em “Color mode”, vou tirar o "Graph" para focar só no número.

[05:27] Na unidade, vamos trabalhar com “Short” ou “None”, tanto faz; no “Threshold”, vou colocar que, se em um minuto eu tiver 3 erros 500, eu tenho um problema, vou tratar dessa forma. Vou colocar aqui verde escuro e está feito.

[05:55] Já temos o nosso painel de erro 500. Vou arrastá-lo para cá, vai ficar aqui do lado, e vamos agora criar o outro painel. Esse outro painel também vai trabalhar com a métrica http_server_requests_seconds_count.

[06:16] Vou trabalhar com os labels application=”$application”, instance=”$instance”, job=”app-forum-api”. Vou fazer aquele seletor de uri, quero pegar erros que são refletidos para os meus usuários, então vou por o uri!=”/actuator/prometheus” e vou colocar o status=”500”. Aqui, quero especificamente o erro 500.

[07:08] Entro nesse estado novamente. O que mais vou fazer? Vou colocar um time range de [5m]. Como quero avaliar a minha taxa de erros, não é uma coisa imediata, eu quero olhar para a minha faixa temporal um pouco maior.

[07:27] Quando faço isso, obviamente, perco a visualização. Agora, posso trabalhar com a minha função rate para pegar essa taxa. Aqui, o rate nos últimos cinco minutos, 0. Vou fazer a agregação para que eu tenha apenas um valor e não tenha séries temporais distintas.

[07:56] Isso está me trazendo um valor que não posso tratar como uma taxa de erros, porque preciso comparar o meu número de erros com o total de requisições que estou tendo. Para fazer essa comparação, tenho que fazer uma operação aritmética de divisão.

[08:14] Vou pegar essa métrica aqui, coloquei a barra de divisão, /, vou colar essa métrica, mas vou tirar o status=500. Fica assim: sum(rate(http_server_requests_seconds_count{application=”$application”,instance=”$instance,job=”app-forum-api”,uri!=”/actuator/prometheus”,status=”500”}[5m]))/sum(rate(http_server_requests_seconds_count{application=”$application”,instance=”$instance,job=”app-forum-api”,uri!=”/actuator/prometheus”}[5m]))

[8:25] Basicamente, tenho que pegar o meu número de eventos com o status específico – nesse caso, 500 – e dividir pelo total de eventos, sem discriminar o status. Aí eu consigo ter uma taxa de quanto aquele evento ocorreu em um espaço de tempo.

[08:49] Então, está feito para erro 500, vou colocar na legenda “500”. Não posso fazer passagem por referência de label porque estou trabalhando com agregação. Vou copiar essa métrica, vou adicionar uma outra query.

[09:04] Agora, para essa outra query, o status vai ser 400. 400 já estou tendo alguns nos últimos 5 minutos. Vou adicionar, no lado inferior esquerdo, mais uma consulta e vou colocar o 404.

[09:27] No formato da legenda “404”, está feito. Agora, vou colocar o título “ERROR RATE” e a descrição vai ser “Taxa de erros nos últimos 5 minutos”. Descendo, a legenda vou colocar como “Tabela”; vou manter embaixo mesmo; e os valores de legenda vou colocar o mínimo, o máximo, a média, o último valor não nulo e posso colocar o total também.

[10:19] Agora, vamos trabalhar no layout dessa métrica. Vou colocar opacidade nível 10 – estamos trabalhando com “Time series”, atenção quanto a isso –, vou colocar um gradiente e vou tirar os pontos, “Show points: Never”, não quero exibição de pontos.

[10:44] Descendo, a unidade, em “Standard options”, a unidade que queremos é a “Porcentagem”, vou escolher o “Percent (0-100)”, fica mais interessante. Realmente é dessa forma que estamos, em 0.0250%.

[11:07] Se eu mudar isso e colocar esse outro tipo de porcentagem, “Percent (0.0-1-0)”, ficou melhor, “2.50%”. Vamos seguir assim e ver se esse é o formato que vai nos atender melhor.

[11:23] O outro está realmente ficando muito desproporcional. Agora, não mais o que mexer aqui, não tem “Threshold”. O que vou fazer é uma alteração: o 500 vou colocar vermelho, o 400 vai ficar laranja, e vou colocar o 404 como amarelo.

Gráfico "*Error rate*". O eixo X mostra a passagem do tempo, de 30 em 30 segundos, de 23:00:00 a 23:04:30. O eixo Y mostra os valores 0% e 2,50%. A linha vermelha (referente a erro 500) está em 0% por todo o período. A linha amarela (erro 404) começa perto de 2,5% às 23:00, desce próximo de 23:02 e depois volta a subir. A linha laranja (erro 400) está relativamente estável perto de 2,5%.

[11:51] Voltando para o nosso dash, está aqui o nosso “Error rate”, vamos descê-lo e colocá-lo aqui. Vou diminuir um pouco esse “Total Requests”, está muito grande, o “Error 500” também está muito grande.

[12:11] Agora posso abaixar e ficou em uma proporção bem legal. Então, esse é o nosso “Error Rate”, atualmente estamos com 3.20% de erros 400; estamos com 2.67% de erros 404, e 0% de erros 500 nos últimos 5 minutos.

[12:43] Essa aula, finalizamos aqui. Na próxima aula, vamos configurar mais dois painéis. Nos vemos na próxima aula.







# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Métricas Error 500 e Error Rate


[00:00] Vamos dar sequência no nosso curso. Nessa aula, a nossa missão é configurar dois painéis: um painel com o número de erros 500 que estão ocorrendo na aplicação em um período de tempo e outro painel que vai ter a nossa taxa de erros, não só focada em erro 500.

[00:24] Vamos começar. Vou adicionar um painel, a métrica que vou utilizar é a http_server_requests_seconds_count. Aqui, vou colocar alguns labels, vou colocar o {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[00:59] Agora, vou fazer um seletor de negação para negar tudo que for uri!=”/actuator/prometheus”. Por que vou fazer isso? Vamos imaginar uma situação que vai causar um erro 500 para nós: o database caiu.

[01:20] O database caiu, vamos ter uma taxa de erros 500, só que o /actuator/prometheus não vai ficar fora, ele vai continuar retornando 200, e como eu quero olhar especificamente para erro 500, eu tenho que tirar esse endpoint.

[01:37] O status=”500”. Então, se eu colocar aqui, tem uma quantidade de erros 500 que estou tendo no momento, é até grande, então, aparentemente, estou com alguma coisa derrubada. Deixa eu entender.


- Adicionar um painel

- Colocar no Metrics Browser:
http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}

- Erro no painel
"No data"



- Tentando consultar via Prometheus:

http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	10
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/actuator/health"}
	512
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/actuator/prometheus"}
	117
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	387
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	231
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	20
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}

Não tem ocorrencia de código 500, por isso o erro de "No data" no painel.


- Simulando erros no JDBC:
docker container stop mysql-forum-api
docker container stop redis-forum-api

- Subindo devolta:
docker container start mysql-forum-api
docker container start redis-forum-api



[02:00] Já "startei" o redis e o mysql, mas ainda tem uma quantidade de erros 500 sendo exibida aqui. O que eu posso fazer aqui, além disso, para facilitar a nossa vida e compor uma métrica que realmente funcione?

[02:22] Eu preciso olhar para um espaço de tempo e vou olhar para o último minuto. Automaticamente, quando eu faço isso, eu não tenho mais uma visualização gráfica, porque eu acabei de criar um range vector e preciso de um dado Scalar ou de um instant vector.


- Colocar no Metrics Browser a métrica com um espaço de tempo de 1 minuto, último minuto:
http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[1m]

- Gráfico some ao definir o espaço de tempo.



[02:47] O que vou trabalhar aqui é a taxa de crescimento. Então, vou pegar a taxa de crescimento, o increase, do último minuto, e agora tenho uma métrica que reflete a realidade. Não tem nenhum erro 500 acontecendo no último minuto.

- Colocar no Metrics Browser a métrica com um espaço de tempo de 1 minuto e usando a função increase
increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[1m])


[03:07] Porém, se vocês notarem, eu tenho duas séries temporais sendo exibidas, eu tenho esses dois time series aqui. Por que tenho dois? Porque tenho exceções diferentes.

[03:20] Aqui são os labels, Exception é um label, não estamos usando. Eu tenho um que é CannotCreateTransationException e outro que é QueryTimeoutException.





[03:33] O que eu preciso fazer para ter um número inteiro único que traga para mim, que reflita a quantidade de erros 500? Eu tenho que fazer uma agregação. Então, vou trabalhar com o sum para fazer essa agregação e transformar isso em uma métrica só.

- Colocar no Metrics Browser a métrica com uma agregação, espaço de tempo de 1 minuto e usando a função increase:
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[1m]))

- Mudando o código da métrica, para trazer os códigos 200 ao invés dos 500:
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="200"}[1m]))

[03:52] Eu não tenho nenhuma informação sendo "plotada", é óbvio, porque não tenho nenhum erro 500. Se eu mudar isso para status=”200”, vou ter a visualização de um número. Ele não me retornou nada, é um bug do Grafana.

[04:10] Vou copiar essa consulta, vou sair, vou remover esse dash – isso está acontecendo frequentemente nessa versão – vou adicionar um novo painel, vou colar a nova consulta e está aí.

[04:32] Está aqui a informação, essa é com erro “200 OK”, vou mudar para 500, vai chegar a nível 0 porque não tem erros 500 no momento.

[04:46] A legenda, não vou trabalhar porque a visualização que vamos usar é do tipo “Stat”. Vou colocar aqui “ERROR 500” como título, “Número de erros 500 no último minuto” como descrição.

[05:12] Descendo aqui, o cálculo vai ser em cima do último valor não nulo; o tipo de campo é numérico mesmo; em “Color mode”, vou tirar o "Graph" para focar só no número.

[05:27] Na unidade, vamos trabalhar com “Short” ou “None”, tanto faz; no “Threshold”, vou colocar que, se em um minuto eu tiver 3 erros 500, eu tenho um problema, vou tratar dessa forma. Vou colocar aqui verde escuro e está feito.

- Voltar para 500 o status no label da métrica:
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[1m]))

- Tipo do gráfico:
Stat

- Nome do painel:
ERROR 500

- Descrição:
Número de erros 500 no último minuto

- Graph mode:
none

- Unit:
short

- Threshold:
vermelho = 3




[05:55] Já temos o nosso painel de erro 500. Vou arrastá-lo para cá, vai ficar aqui do lado, e vamos agora criar o outro painel. Esse outro painel também vai trabalhar com a métrica http_server_requests_seconds_count.

[06:16] Vou trabalhar com os labels application=”$application”, instance=”$instance”, job=”app-forum-api”. Vou fazer aquele seletor de uri, quero pegar erros que são refletidos para os meus usuários, então vou por o uri!=”/actuator/prometheus” e vou colocar o status=”500”. Aqui, quero especificamente o erro 500.

[07:08] Entro nesse estado novamente. O que mais vou fazer? Vou colocar um time range de [5m]. Como quero avaliar a minha taxa de erros, não é uma coisa imediata, eu quero olhar para a minha faixa temporal um pouco maior.


- Voltar para 500 o status no label da métrica:
sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[5m]))


[07:27] Quando faço isso, obviamente, perco a visualização. Agora, posso trabalhar com a minha função rate para pegar essa taxa. Aqui, o rate nos últimos cinco minutos, 0. Vou fazer a agregação para que eu tenha apenas um valor e não tenha séries temporais distintas.

[07:56] Isso está me trazendo um valor que não posso tratar como uma taxa de erros, porque preciso comparar o meu número de erros com o total de requisições que estou tendo. Para fazer essa comparação, tenho que fazer uma operação aritmética de divisão.

[08:14] Vou pegar essa métrica aqui, coloquei a barra de divisão, /, vou colar essa métrica, mas vou tirar o status=500. Fica assim: sum(rate(http_server_requests_seconds_count{application=”$application”,instance=”$instance,job=”app-forum-api”,uri!=”/actuator/prometheus”,status=”500”}[5m]))/sum(rate(http_server_requests_seconds_count{application=”$application”,instance=”$instance,job=”app-forum-api”,uri!=”/actuator/prometheus”}[5m]))


- Editado, adicionando barra e removendo status 500 do lado direito da fórmula:
sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="500"}[5m])) / sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[5m]))

- Legend:
500

[8:25] Basicamente, tenho que pegar o meu número de eventos com o status específico – nesse caso, 500 – e dividir pelo total de eventos, sem discriminar o status. Aí eu consigo ter uma taxa de quanto aquele evento ocorreu em um espaço de tempo.

[08:49] Então, está feito para erro 500, vou colocar na legenda “500”. Não posso fazer passagem por referência de label porque estou trabalhando com agregação. Vou copiar essa métrica, vou adicionar uma outra query.

[09:04] Agora, para essa outra query, o status vai ser 400. 400 já estou tendo alguns nos últimos 5 minutos. Vou adicionar, no lado inferior esquerdo, mais uma consulta e vou colocar o 404.

- Adicionar mais uma Query
sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="400"}[5m])) / sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[5m]))

- Legend:
400

- Adicionar mais uma Query
sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="404"}[5m])) / sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[5m]))

- Legend:
404

[09:27] No formato da legenda “404”, está feito. Agora, vou colocar o título “ERROR RATE” e a descrição vai ser “Taxa de erros nos últimos 5 minutos”. Descendo, a legenda vou colocar como “Tabela”; vou manter embaixo mesmo; e os valores de legenda vou colocar o mínimo, o máximo, a média, o último valor não nulo e posso colocar o total também.

- Titulo do painel:
ERROR RATE

- Descrição:
Taxa de erros nos últimos 5 minutos

- Legend Mode:
Table

- Legend values:
min
max
média
last *
Total

[10:19] Agora, vamos trabalhar no layout dessa métrica. Vou colocar opacidade nível 10 – estamos trabalhando com “Time series”, atenção quanto a isso –, vou colocar um gradiente e vou tirar os pontos, “Show points: Never”, não quero exibição de pontos.

- Opacidade:
10

- Show points: 
Never

- Tipo de gráfico:
 Time series


[10:44] Descendo, a unidade, em “Standard options”, a unidade que queremos é a “Porcentagem”, vou escolher o “Percent (0-100)”, fica mais interessante. Realmente é dessa forma que estamos, em 0.0250%.

[11:07] Se eu mudar isso e colocar esse outro tipo de porcentagem, “Percent (0.0-1-0)”, ficou melhor, “2.50%”. Vamos seguir assim e ver se esse é o formato que vai nos atender melhor.

- Unidade:
Percent (0.0-1.0)

- Mudar as cores das legendas:
vermelho - 500
laranja - 400
amarelo - 404

[11:23] O outro está realmente ficando muito desproporcional. Agora, não mais o que mexer aqui, não tem “Threshold”. O que vou fazer é uma alteração: o 500 vou colocar vermelho, o 400 vai ficar laranja, e vou colocar o 404 como amarelo.

Gráfico "*Error rate*". O eixo X mostra a passagem do tempo, de 30 em 30 segundos, de 23:00:00 a 23:04:30. O eixo Y mostra os valores 0% e 2,50%. A linha vermelha (referente a erro 500) está em 0% por todo o período. A linha amarela (erro 404) começa perto de 2,5% às 23:00, desce próximo de 23:02 e depois volta a subir. A linha laranja (erro 400) está relativamente estável perto de 2,5%.

[11:51] Voltando para o nosso dash, está aqui o nosso “Error rate”, vamos descê-lo e colocá-lo aqui. Vou diminuir um pouco esse “Total Requests”, está muito grande, o “Error 500” também está muito grande.

[12:11] Agora posso abaixar e ficou em uma proporção bem legal. Então, esse é o nosso “Error Rate”, atualmente estamos com 3.20% de erros 400; estamos com 2.67% de erros 404, e 0% de erros 500 nos últimos 5 minutos.

[12:43] Essa aula, finalizamos aqui. Na próxima aula, vamos configurar mais dois painéis. Nos vemos na próxima aula.