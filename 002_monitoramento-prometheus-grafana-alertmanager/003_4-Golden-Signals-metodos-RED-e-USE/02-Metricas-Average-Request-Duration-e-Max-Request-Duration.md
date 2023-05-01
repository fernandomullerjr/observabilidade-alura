

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 003 - Aula 02 Métricas Average Request Duration e Max Request Duration"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas Average Request Duration e Max Request Duration

# Transcrição

Transcrição
[00:00] Vamos dar continuidade ao nosso conteúdo. Nessa aula, vamos falar sobre a duração de requisições.

[00:12] Vou trabalhar criando um novo painel e, nesse painel, vamos trabalhar com a nossa métrica conhecida, http_server_requests_seconds_sum.

[00:30] Vou, como de praxe, definir o labels, tem {application="$application" instance="$instance", job="app-forum-api"}. Nesse caso, vou trabalhar também com o status="200" e com uma uri="/topicos".

[01:08] Agora tenho um instant vector voltando para mim, vou transformá-lo em um range vector porque quero olhar para o último minuto, [1m], o que logicamente tira a nossa visualização gráfica, mas vou fazer o rate e obter a taxa dessa métrica.

[01:31] Chegamos nesse ponto, só que, para que eu consiga entender a minha média de duração, eu tenho que fazer uma operação aritmética que consiste em uma divisão para uma métrica muito parecida, que é a count.

[01:51] Vou pegar a agregação, a soma de todos os segundos, todas as requisições com o status 200 e vou dividir pelo número de requisições contadas utilizando o mesmo time range de 1 minuto. rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m]).

[02:15] Aqui, vou poder colocar na legenda a {{uri}} que foi utilizada; o {{method}}; e o status não precisa, porque já está ali, é 200, mas vou colocar aqui {{status}}, só por colocar mesmo.

[02:43] Está aqui /topicos, vou adicionar mais uma consulta. Temos também o /topicos/{id}, então vou colocar aqui /topicos/(id) e aqui embaixo a mesma coisa, tem que colocar nas duas métricas, rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/tópicos{id}"}[1])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api" status="200", uri="/tópicos/{id}"}[1]).

[03:12] A mesma coisa na legenda, {{uri}}{{method}}{{status}}. Vou trabalhar da mesma forma adicionando o auth, o endpoint de autenticação.

[03:40] Está aqui o /auth. Vou copiar porque estou com preguiça de digitar de novo toda a legenda e está feito, temos os três endpoints já aqui.

[04:07] O título vou colocar como “AVERAGE REQUEST DURATION", vou colocar a descrição como “Duração média de requisições no último minuto". Vamos trabalhar na legenda, vai ser uma legenda em formato de tabela.

[04:44] Em valores, vou colocar o mínimo, o máximo, o valor médio e o último coletado. Está quase concluído, vou colocar a opacidade 10, vou trabalhar com gradiente, tirar a pontuação.

[05:20] Em unidade, novamente vamos trabalhar com “Time > Seconds". Está construído o nosso painel. Vou descer esse painel para cá e vou colocá-lo do lado do contador de requisições, do nosso “Request Count".

Gráfico *Average Request Duration*. O eixo x mostra a passagem do tempo, de 1 em 1 minuto. O eixo y mostra os valores 0s, 50ms e 100ms. A linha azul (que representa /auth) se mantém estável ligeiramente abaixo de 100ms. As linhas amarela e verde  (/topicos/{id} e /topicos, respectivamente) se mantém estáveis ligeiramente acima de 0s.

[05:54] Agora vamos partir para o nosso último painel. Vamos em “Adicionar painel" e, nesse caso, vamos estar lidando com a duração máxima de uma requisição. Então, a métrica é o http_server_requests_seconds_max.

[06:19] Vou colocar os nosso labels application="$application", instance="$instance", job="app-forum-api". O que mais vamos colocar aqui? Cada um dos endpoints que vamos olhar.

[06:55] Vai ficar uma métrica muito similar ao que trabalhamos anteriormente, tem o status="200" – eu poderia não trabalhar com o status para termos um volume maior de informação, mas, nesse caso, o nosso foco é naquilo que está realmente sendo respondido para o cliente com sucesso.

[07:20] Vou trabalhar com uma uri="/topicos". Nesse caso, o que podemos fazer é colocar um time range específico, vou colocar [1m], e podemos trabalhar especificamente com a taxa de crescimento que tivemos no último minuto, increase.

[07:55] Dando uma olhada, não tive um retorno justamente por essa métrica ser uma métrica de contagem. Vamos dar uma olhada nela, o job está errado, app-forum-api. Conseguimos ter um retorno.

[08:23] Se eu transformo isso em um range vector, eu não tenho retorno, posso trabalhar com increase para ver a taxa de crescimento que eu tive, porém, a informação que tenho aqui não é muito interessante para mim.

[08:46] Vou tirar esse increase e vou dar uma olhada por consulta dentro dessa métrica. Vou tirar o /topicos para termos uma métrica geral, não precisa ter o /topicos, vou olhar para todos os endpoints, vai ser mais fácil fazer dessa maneira.

[09:16] Na legenda, podemos colocar a {{uri}} {{method}} {{status}}. Vou colocar como título “MAX REQUEST DURATION", e a descrição será “Duração máxima de uma requisição".

[10:00] A legenda vou colocar também como tabela, e nos valores, vamos colocar o mínimo, o máximo, a média e o último valor não nulo.

[10:25] Vou fazer a mesma configuração de sempre, opaco com gradiente, sem os pontos, não vou mexer nos eixos X ou Y. Na unidade, vou pôr “Tempo" e vou "setar" “Segundos".

[10:49] Basicamente, é isso. O “Max Request Duration" está feito, vou trazê-lo para cá e adequar o tamanho.

[11:14] Ficou pequeno porque ele está pegando o /actuator/prometheus, então vamos voltar e fazer uma pequena correção. Vou fazer um seletor de negação, uri!="/actuator/prometheus".

[11:39] Nesse caso, precisamos disso porque no “Average request duration" estamos especificando os endpoints e aqui não, então o /actuator/prometheus acabava entrando no meio dessa seara.

[11:57] Concluímos a parte RED da nossa API. Ainda vamos ter uma abordagem ao método USE, mas bem reduzida. Só vamos olhar para CPU e memória, uma vez que estamos lidando com uma aplicação conteinerizada.

[12:18] Se fossemos olhar para a parte de rede e para a parte de disco, teríamos que estar olhando para um escopo maior, teríamos que estar acima do nível do contêiner.

[12:28] Ainda vamos ter uma abordagem que vai ser sobre CPU e sobre memória. Fazemos isso na próxima aula. Por hoje, paramos por aqui. Te vejo na próxima aula.








# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas Average Request Duration e Max Request Duration


- Adicionar 1 painel


[00:00] Vamos dar continuidade ao nosso conteúdo. Nessa aula, vamos falar sobre a duração de requisições.

[00:12] Vou trabalhar criando um novo painel e, nesse painel, vamos trabalhar com a nossa métrica conhecida, http_server_requests_seconds_sum.

[00:30] Vou, como de praxe, definir o labels, tem {application="$application" instance="$instance", job="app-forum-api"}. Nesse caso, vou trabalhar também com o status="200" e com uma uri="/topicos".

[01:08] Agora tenho um instant vector voltando para mim, vou transformá-lo em um range vector porque quero olhar para o último minuto, [1m], o que logicamente tira a nossa visualização gráfica, mas vou fazer o rate e obter a taxa dessa métrica.

[01:31] Chegamos nesse ponto, só que, para que eu consiga entender a minha média de duração, eu tenho que fazer uma operação aritmética que consiste em uma divisão para uma métrica muito parecida, que é a count.

[01:51] Vou pegar a agregação, a soma de todos os segundos, todas as requisições com o status 200 e vou dividir pelo número de requisições contadas utilizando o mesmo time range de 1 minuto. rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m]).




- Exemplo de uma OK, para pegar a sintaxe:

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m]) / rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])
~~~~



## Query A
- QUERY EDITADA:

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m]) / rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos"} [1m])
~~~~





- Editar a legenda

[02:15] Aqui, vou poder colocar na legenda a {{uri}} que foi utilizada; o {{method}}; e o status não precisa, porque já está ali, é 200, mas vou colocar aqui {{status}}, só por colocar mesmo.

[02:43] Está aqui /topicos, vou adicionar mais uma consulta. Temos também o /topicos/{id}, então vou colocar aqui /topicos/(id) e aqui embaixo a mesma coisa, tem que colocar nas duas métricas, rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/tópicos{id}"}[1])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api" status="200", uri="/tópicos/{id}"}[1]).

[03:12] A mesma coisa na legenda, {{uri}}{{method}}{{status}}. Vou trabalhar da mesma forma adicionando o auth, o endpoint de autenticação.

- Legenda
{{uri}} {{method}} {{status}}


- Adicionar mais 1 query do  [02:43]
nesta vamos usar o id no path do topicos:
/topicos/{id}

- Query original

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/tópicos{id}"}[1])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api" status="200", uri="/tópicos/{id}"}[1]).
~~~~


- QUERY EDITADA:

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/tópicos{id}"}[1])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api" status="200", uri="/tópicos/{id}"}[1])
~~~~

- Legenda:
{{uri}} {{method}} {{status}}


- Erro na query
bad_data: 1:155: parse error: missing unit character in duration: details: 



- QUERY EDITADA, v2:

adicionada a letra "m" ao lado dos números 1, para ficar 1m entre os colchetes

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/tópicos{id}"}[1m])/rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api" status="200", uri="/tópicos/{id}"}[1m])
~~~~




- Novo erro:
bad_data: 1:280: parse error: unexpected identifier "status" in label matching, expected "," or "}": details: 


## Query B
- QUERY EDITADA, v3:

tirando acento da palavra topicos
adicionada virgula antes do status na segunda linha
ADICIONADA barra apos topicos

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos/{id}"}[1m]) / rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/topicos/{id}"}[1m])
~~~~

- Legenda:
{{uri}} {{method}} {{status}}

- Ficou ok agora!



# CONTINUA EM

[03:12] A mesma coisa na legenda, {{uri}}{{method}}{{status}}. Vou trabalhar da mesma forma adicionando o auth, o endpoint de autenticação.

[03:40] Está aqui o /auth. Vou copiar porque estou com preguiça de digitar de novo toda a legenda e está feito, temos os três endpoints já aqui.



# PENDENTE
- Seguir em 03:35 de video da aula.


## Query C

Endpoint de autenticação, auth

- QUERY EDITADA:

~~~~bash
rate(http_server_requests_seconds_sum{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/auth"}[1m]) / rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", status="200", uri="/auth"}[1m])
~~~~

- Legenda:
{{uri}} {{method}} {{status}}