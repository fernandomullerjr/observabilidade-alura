

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula  04 Métricas Total Requests e Response Time"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Métricas Total Requests e Response Time

# Transcrição
[00:00] Vamos iniciar o nosso capítulo. A primeira coisa que vou fazer é recolher essa row, vamos criar uma nova row, vou editar o nome dela que vai ser “API RED”. Dar um “Update” e está certo, configurada.

[00:26] Vamos começar com a criação do nosso primeiro painel, que vai refletir o total de requisições por segundo. Vamos em “Add new panel” e vamos trabalhar com a métrica http_server_requests_seconds_count.

[00:48] Vamos ver a contagem dessas requisições e vamos definir os nossos labels: {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[01:12] Até o momento, está certo, está aqui o número de requisições que estamos recebendo. Se queremos olhar somente para o cliente, podemos retirar, por exemplo, o /actuator/prometheus.

[01:30] Vou trabalhar com um seletor de diferença, vou pegar o label uri e vou colocar o seletor de diferença, uri!=”/actuator/Prometheus”.

[01:48] Já demos uma enxugada e agora tem um detalhe: eu quero olhar para um período de tempo específico, vou definir como [1m], quero ver o número de requisições do meu último minuto.

[02:03] Se eu colocar o time range, eu vou quebrar o meu gráfico, porque, para que eu possa "plotar" um gráfico, eu tenho que ter como retorno um dado Scalar ou um instant vector, não um range vector.

[02:16] Para eu fazer essa mudança, vou trabalhar com a nossa conhecida função increase e vou ter a taxa de crescimento do último minuto. Tenho essa taxa de crescimento do último minuto para diversos endpoints.

[02:38] Só que eu quero o total, quero pegar o total de requisições que a minha aplicação está recebendo. Posso fazer uma agregação, trabalhando com o sum. Tenho o número que está em torno de 76.4 requisições no último minuto.

[03:05] Um ponto importante é a visualização. Eu vou transformar isso, em vez de “Time series”, vou por em “Stat”. Tenho 76.4 requisições. Vou colocar aqui de título “TOTAL REQUESTS”, por segundo.

[03:46] E descrição vou colocar “Número de requisições” – na verdade, eu olhar para segundos, mas vou ver o espaço de 1 minuto –, “Número de requisições no último minuto” para a descrição.

[04:10] Vou olhar o último valor não nulo; na parte de coloração, vou tirar a coloração gráfica, quero ficar só com o número; na unidade, vou trabalhar com uma unidade que é um número inteiro, vou colocar “Short”.

[04:32] Não tem mais o que fazer aqui. Agora, tem um ponto interessante. Vamos imaginar que não tem nenhum cliente acessando a nossa aplicação. Vamos ficar com 0 aqui, não vai ter nenhum número.

[04:46] Só que, se eu retirar essa negação para o /actuator/prometheus, como eu sei que tenho o scrape time de 5 segundos e estou olhando 1 minuto, eu vou ter que ter, pelo menos, 30 requisições aqui.

[05:05] Então, vou deixar um 30 dentro do meu “Threshold” como laranja, porque sabemos que, se tivermos somente 30 requisições em 1 minuto, vamos estar lidando com um problema.

[05:18] Vou colocar o “Basic”, que seria o 0, vazio, e vou adicionar 60 requisições como um padrão aceitável. Então, está configurado o “Total Requests” para nós.

[05:36] Vou diminuí-lo um pouco e vou colocá-lo dentro do RED. Vamos partir agora para o nosso segundo painel. Vamos em “Add new panel” e agora vamos trabalhar com response time.

[05:54] A métrica que vou utilizar é o http_server_requests_seconds_sum. Na verdade, vou utilizar duas métricas, vou pegar a soma de todos os segundos e vou dividir pelo número de requisições total.

[06:14] Assim, vou conseguir, através disso, trabalhando com taxas, chegar no meu tempo de resposta. Tenho a métrica http_sever_requests_seconds_sum, vou colocar aqui as labels {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[06:55] Posso tirar o /actuator/prometheus ou mantê-lo. Vou tirar e colocar o seletor de negação, uri!=”/actuator/Prometheus”. Tenho agora essa métrica sendo "plotada" para mim.

[07:23] Porém, eu quero também olhar para um espaço de tempo não muito longo, então vou colocar [1m]. Logicamente, vou ter que trabalhar com uma função aqui. Vamos trabalhar com uma função nova chamada rate.

[07:43] O rate traz a taxa do meu último minuto, ele faz uma conversão desse valor para a taxa. Tenho aqui essa média, essa taxa que está relacionada a essas requisições.

[07:58] Vou trabalhar com uma passagem por referência de label na legenda. Olha a legenda como está, uma bagunça, muita coisa, vem todos os labels aqui.

[08:10] Vou colocar a {{uri}}, vou colocar o método que foi usado {{method}}, “Get” ou “Post”, e vou colocar também o {{status}}, que é o código HTTP de retorno. Ficou legal agora, ficou mais simples de entender. A legenda fica assim: {{uri}}{{method}}{{status}}.

[08:34] Só que eu ainda não tenho o que preciso. Presta bem atenção, vou copiar tudo isso para não ter que digitar tudo de novo, mudando só a métrica, porque a métrica que vou usar para fazer a operação matemática não é a requests_seconds_sum, é a requests_seconds_count.

[08:56] Vou fazer uma divisão, vou colar a métrica que eu copiei e vou mudar só o final, ao invés de sum, vou utilizar o count, http_server_requests_seconds_count. Então fica assim: rate(http_server_requests_seconds_sum{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri!=”/actuator/Prometheus”}[1m])/rate(http_server_requests_seconds_count{application=”$application”, instance=”$instance”, job=”app-forum-api”, uri!=”/actuator/Prometheus”}[1m]).

[9:06] Está feita. Através dessa expressão, eu consigo saber o tempo de resposta dos meus endpoints.Vou colocar aqui de título “RESPONSE TIME” e, na descrição, “Tempo de resposta no último minuto”. Vou trabalhar na legenda, eu quero uma visualização em forma de tabela para ela ficar mais organizada.

[09:45] Quero colocar essa tabela não embaixo, mas na direita, e agora vou selecionar alguns valores que vão aparecer na legenda para não termos só o gráfico como referência.

[09:57] Vou colocar o valor mínimo, vou colocar o valor máximo, posso colocar a média de valores – se ficar muita coisa, nós tiramos – e vou colocar o último. Então, tenho o mínimo, o máximo, a média e o último valor.

[10:20] Vou trabalhar agora no design da métrica. Vou colocar opacidade de nível 10, vou fazer um gradiente e vou tirar os pontos. Você pode manter o layout que você quiser, é a gosto.

[10:40] Descendo, em “Standard options”, na “Unidade”, vou trabalhar com a unidade “Time” e vou "setar" como “Seconds”. Ele já fez a configuração aqui de segundos para mim.

[11:04] Eu posso configurar a casa decimal. Se alguém achar estranho o que é 2.73 milissegundos, se você colocar 0 casas decimais, ele vai arredondar isso e você vai ter essa resposta. Porém, com o arredondamento, os dados não são tão precisos, então eu não aconselho fazer isso.

[11:26] Vamos manter desse jeito. Não temos “Threshold” para a questão do tempo de resposta, então vou deixar desse jeito. Então, já temos uma métrica com o tempo de resposta dos endpoints da nossa API.

[11:48] Tem aqui com o status 500, não está tendo nenhum erro 500, então está certo; 404 está aqui; 200, temos o mínimo, 1.42, e 1.94 para o máximo; e por aí vai.

[12:07] Tem uma diferença legal. Quando eu consulto /topicos, eu consulto o Redis. A API fala com o Redis e traz do cache essa consulta. Então, você vai ver que ela é bem mais rápida do que o /topicos/(id), que vai no MySQL, por isso tem essa diferenciação.

[12:40] O POST também vai no MySQL e tem uma lógica de autenticação, tem uma regra de negócio, então ele sempre vai ser mais demorado.

[12:51] Está aqui o nosso “Response time”, depois nós formatamos isso de um jeito melhor, não vai ficar tão grande assim, vai ficar um pouco menor. Formatamos isso na próxima aula com a criação dos novos painéis.

[13:08] Na próxima aula, vamos trabalhar com error rate e com requisições por segundo, com o número de requisições por segundo. Nos vemos na próxima aula.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Métricas Total Requests e Response Time


[00:00] Vamos iniciar o nosso capítulo. A primeira coisa que vou fazer é recolher essa row, vamos criar uma nova row, vou editar o nome dela que vai ser “API RED”. Dar um “Update” e está certo, configurada.

[00:26] Vamos começar com a criação do nosso primeiro painel, que vai refletir o total de requisições por segundo. Vamos em “Add new panel” e vamos trabalhar com a métrica http_server_requests_seconds_count.

[00:48] Vamos ver a contagem dessas requisições e vamos definir os nossos labels: {application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[01:12] Até o momento, está certo, está aqui o número de requisições que estamos recebendo. Se queremos olhar somente para o cliente, podemos retirar, por exemplo, o /actuator/prometheus.


- Adicionar uma Row

- Nome da Row
API RED

- Metrics Browser
http_server_requests_seconds_count

sum(rate(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus", status="400"}[5m]))

colocando assim vai quebrar o meu gráfico, porque, para que eu possa "plotar" um gráfico, eu tenho que ter como retorno um dado Scalar ou um instant vector, não um range vector:
http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m]


[01:30] Vou trabalhar com um seletor de diferença, vou pegar o label uri e vou colocar o seletor de diferença, uri!=”/actuator/Prometheus”.

[01:48] Já demos uma enxugada e agora tem um detalhe: eu quero olhar para um período de tempo específico, vou definir como [1m], quero ver o número de requisições do meu último minuto.

[02:03] Se eu colocar o time range, eu vou quebrar o meu gráfico, porque, para que eu possa "plotar" um gráfico, eu tenho que ter como retorno um dado Scalar ou um instant vector, não um range vector.

[02:16] Para eu fazer essa mudança, vou trabalhar com a nossa conhecida função increase e vou ter a taxa de crescimento do último minuto. Tenho essa taxa de crescimento do último minuto para diversos endpoints.

[02:38] Só que eu quero o total, quero pegar o total de requisições que a minha aplicação está recebendo. Posso fazer uma agregação, trabalhando com o sum. Tenho o número que está em torno de 76.4 requisições no último minuto.

[03:05] Um ponto importante é a visualização. Eu vou transformar isso, em vez de “Time series”, vou por em “Stat”. Tenho 76.4 requisições. Vou colocar aqui de título “TOTAL REQUESTS”, por segundo.

- Para eu fazer essa mudança, vou trabalhar com a nossa conhecida função increase e vou ter a taxa de crescimento do último minuto. Tenho essa taxa de crescimento do último minuto para diversos endpoints.:
increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])

- Alguns resultados/legendas:
{application="app-forum-api", exception="CannotCreateTransactionException", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SERVER_ERROR", status="500", uri="/topicos/{id}"}
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SERVER_ERROR", status="503", uri="/actuator/health"}
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/actuator/health"}


- Fazendo uma agregação usando o "sum":
sum(increase(http_server_requests_seconds_count{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m]))

- Com a agregação do "sum", é trazido 1 valor apenas, ao invés de vários.

- Tipo de gráfico:
Stat

- Nome do painel:
TOTAL REQUESTS

- Descrição:
Número de requisições no último minuto

- Graph mode:
none

- Unit:
short


- Threshold:
30, cor laranja
60, cor verde

[03:46] E descrição vou colocar “Número de requisições” – na verdade, eu olhar para segundos, mas vou ver o espaço de 1 minuto –, “Número de requisições no último minuto” para a descrição.

[04:10] Vou olhar o último valor não nulo; na parte de coloração, vou tirar a coloração gráfica, quero ficar só com o número; na unidade, vou trabalhar com uma unidade que é um número inteiro, vou colocar “Short”.

[04:32] Não tem mais o que fazer aqui. Agora, tem um ponto interessante. Vamos imaginar que não tem nenhum cliente acessando a nossa aplicação. Vamos ficar com 0 aqui, não vai ter nenhum número.

[04:46] Só que, se eu retirar essa negação para o /actuator/prometheus, como eu sei que tenho o scrape time de 5 segundos e estou olhando 1 minuto, eu vou ter que ter, pelo menos, 30 requisições aqui.

[05:05] Então, vou deixar um 30 dentro do meu “Threshold” como laranja, porque sabemos que, se tivermos somente 30 requisições em 1 minuto, vamos estar lidando com um problema.

[05:18] Vou colocar o “Basic”, que seria o 0, vazio, e vou adicionar 60 requisições como um padrão aceitável. Então, está configurado o “Total Requests” para nós.

[05:36] Vou diminuí-lo um pouco e vou colocá-lo dentro do RED. Vamos partir agora para o nosso segundo painel. Vamos em “Add new panel” e agora vamos trabalhar com response time.

[05:54] A métrica que vou utilizar é o http_server_requests_seconds_sum. Na verdade, vou utilizar duas métricas, vou pegar a soma de todos os segundos e vou dividir pelo número de requisições total.

[06:14] Assim, vou conseguir, através disso, trabalhando com taxas, chegar no meu tempo de resposta. Tenho a métrica http_sever_requests_seconds_sum, vou colocar aqui as labels {application=”$application”, instance=”$instance”, job=”app-forum-api”}.


- Continua em 5:49
no próximo painel do Row "API RED".