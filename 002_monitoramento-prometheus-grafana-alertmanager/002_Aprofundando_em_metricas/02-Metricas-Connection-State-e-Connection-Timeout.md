
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula 02 Métricas Connection State e Connection Timeout "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas Connection State e Connection Timeout

# Transcrição
[00:00] Vamos dar sequência no nosso curso. Agora vamos criar mais dois painéis. O primeiro vai ser o estado e o número das conexões com a base de dados.

[00:18] Vou adicionar um painel e a métrica que vamos trabalhar aqui é a hikaricp_connections_active, essa vai ser a primeira métrica. Esse painel vai ter três métricas ao todo.

[00:36] Aqui, vou trabalhar com os labels {application=”$application”, instance=”$instance”, job=”app-forum-api” e, além disso, vamos configurar o nosso pool=”$pool”}.

[01:20] É legal você olhar para a parte superior esquerda porque você consegue verificar, na parte esquerda superior, as variáveis junto com o labels. Temos essa métrica e essa métrica traz, basicamente, o número de conexões que estão em estado ativo.

[01:48] Temos a primeira, aqui, vou colocar como legenda "active", esse é o estado ativo. Vou copiar e vamos adicionar uma nova query que, ao invés de olhar para o active, vai olhar para o idle, hikaricp_connections_idle.

[02:17] Então, é idle, já temos as conexões em estado idle, em estado de espera. Vou colocar como "idle" a legenda e vamos adicionar outra query que vai ser o estado pendente, o "pending".

[02:40] Todas essas métricas existem na JVM. Já entrou o pending em estado pendente, temos três métricas sendo "plotadas" nesse gráfico, três séries temporais distintas.

[02:58] Vou manter o “Time series”, vou colocar o título como “CONNECTION STATE” e descrição como “Estado das conexões com o database”.

[03:23] Descendo, vou colocar a legenda em um formato tabular. Posso colocá-la do lado direito, fica legal também, vamos fazer uma experiência com isso. Nos valores da legenda, vou colocar o mínimo, o máximo, o último e o total – apesar de o total ser totalmente irrelevante para nós porque ele só vai fazer um contador.

[04:03] Vou até tirar o total para não gerar confusão. Então, mínimo, máximo e último. Aqui, vou trabalhar com a opacidade, vou deixar em 10. Eu gosto dessa configuração, você não precisa fazer assim. Se você achar outro jeito mais interessante, tudo bem, isso é só o layout mesmo, o que é importa é a métrica e o gráfico sendo "plotado".

[04:28] Aqui, vou colocar uma opacidade no gradiente; vou tirar os pontos, não gosto dos pontos; não vou trabalhar no “Eixo X” ou “Eixo Y”; a unidade não vai fazer diferença, vou deixar “Short” porque são números inteiros.

[04:54] Não vou trabalhar com “Threshold”, é um gráfico. Está feito esse painel. Ele é bem interessante para nós. Tenho o “CONNECTION STATE” criado, vou descê-lo e diminuí-lo. Está bagunçado, mas já vamos corrigir, vai ficar legal.

Gráfico "*Connection state*". O eixo X mostra a passagem do tempo, de minuto em minuto, o eixo Y apresenta os valores 0, 5 e 10. A linha amarela (que representa "*idle*") é estável no número 10, exceto uma leve queda perto de 23:25. A linha verde ("*active*") está estável em 0, exceto por um leve aumento, também perto de 23:21. A linha azul ("*pending*") permanece no 0 por todo o período mostrado no gráfico.

[05:37] Está o “CONNECTION STATE”, vamos criar mais um painel. Vou adicionar mais um painel e esse painel vai ser o “Connection Timeout”, vai ser o número de conexões que estão em timeout.

[05:56] Vou colocar o hikaricp_connections_timeout_total, essa métrica. Vou trabalhar com {application=”$application”, instance=”$instance”, job=”app-forum-api”.

[06:28] É isso, não tem muito o que fazer aqui, e o pool, não posso me esquecer, que é a nossa variável pool=”$pool”}. Eu tenho essa informação, só que vou trabalhar novamente com um “Stat” aqui.

[06:51] Vou colocar “DB TIMEOUT” como título e, para descrição, “Conexões com o database em timeout”. Você pode colocar uma descrição melhor se você quiser, não tem problema.

[07:24] Aqui, é um contador. Estamos pegando o timeout_total, ele traz o total para nós, isso não é legal, então vamos fazer uma mudança nisso, vou colocar um range time de [1m] e vou trabalhar com o increase.

[07:47] Só para pegar a taxa de crescimento dele por minuto. Vai estar “0” porque não estamos com o timeout em nenhuma conexão. Vamos trabalhar dessa forma.

[08:02] Aqui, em “Color mode”, vou tirar o “Graph”; em “Unidade”, novamente estamos trabalhando com o número inteiro, então não tem o que mudar. Tendo 5 conexões de timeout em um minuto – até menos –, nós podemos considerar que temos um problema.

[08:32] Vou deixar 5 aqui e está certo. Está feito esse “DB Timeout”, o nome ficou estranho, vou tentar aumentar, “DB CONNECTION TIMEOUT”.

[08:54] Voltando aqui, tenho esse painel que foi criado. Posso trabalhar com ele, agora vou descer, posso diminuir um pouco aqui para ficar mais interessante, só para deixarmos o layout mais enxuto.

*Layout* com todos os gráficos criados dentro de API BASIC. Temos "Uptime", "Start Time", "Users Logged", "Auth Errors", "JDBC pool", "DB connection timeout", "Connection state" e "Warn and Error log".

[09:30] Tenho o meu “ERROR LOG” aqui, vou editá-lo e vou mudar a disposição das estatísticas dele. Vamos agora colocar a prova isso. Vou parar o MySQL e vou causar o caos completo porque vou derrubar o Redis. Vamos ver o que vai acontecer.

[10:17] O “JDBC Pool” caiu na hora, essa métrica é alimentada por scrape. Como ela é um counter, ele traz na hora a informação para nós. “DB Connection Timeout” também vai ser alimentado, mas está por minuto, então é só nós esperarmos um pouco para ver a mudança dele.

[10:35] Aqui, já começamos a ver que as conexões que estavam em idle já baixaram, as pendentes já tiveram um crescimento – tem uma pendente – e agora o “DB Connection Timeout” já começa a ser alterado.

[10:54] Vou até fazer uma edição nele, na parte de decimal, vou deixar como “0” só para eu ter um número mais interessante a ser exibido e não gerar dúvida para vocês.

[11:10] Então, já está com duas conexões em “Timeout” e já temos os warns com 19, o total de warns que tivemos na aplicação até agora, desde a subida, foi 73; temos 24 erros; temos “Auth errors” 0, porque ninguém consegue tentar autenticar; nenhum usuário logado; e o pool de conexões JDBC está lá embaixo.

[11:41] Está aqui a prova de que a nossa métrica funciona bem. Só mais um ajuste em “DB Connection Timeout”, vou no “Threshold”, vou subi-lo, já sei que, se eu tiver “2” conexões ganhando timeout, eu estou com problema dentro desse nosso cenário que é um laboratório.

[12:03] Aqui é mais para entendermos e termos o primeiro contato com a composição de um dashboard. Vou agora subir a aplicação, acho que está de bom tamanho o nosso teste, já validamos, está tudo certo.

[12:19] Vou começar subindo o mysql e agora vou subir o redis. Pronto, agora tudo vai voltar ao normal, a aplicação vai se restabelecer e nós encerramos esse capítulo por aqui, com a composição desses primeiros painéis.

[12:44] Fizemos o basic, mas, no próximo capítulo, já vamos estar lidando com métricas que correspondem à experiência do usuário final. Vamos falar um pouco de golden signals, vamos falar de uma metodologia chamada RED e de outra metodologia chamada USE, mas o nosso foco será na metodologia RED que é direcionada à experiência do usuário final.

[13:13] Então, é isso, nos vemos na próxima aula.








# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas Connection State e Connection Timeout


[00:00] Vamos dar sequência no nosso curso. Agora vamos criar mais dois painéis. O primeiro vai ser o estado e o número das conexões com a base de dados.

[00:18] Vou adicionar um painel e a métrica que vamos trabalhar aqui é a hikaricp_connections_active, essa vai ser a primeira métrica. Esse painel vai ter três métricas ao todo.

[00:36] Aqui, vou trabalhar com os labels {application=”$application”, instance=”$instance”, job=”app-forum-api” e, além disso, vamos configurar o nosso pool=”$pool”}.

[01:20] É legal você olhar para a parte superior esquerda porque você consegue verificar, na parte esquerda superior, as variáveis junto com o labels. Temos essa métrica e essa métrica traz, basicamente, o número de conexões que estão em estado ativo.


- Adicionar um painel

- Métrica que traz as conexões ativas:
hikaricp_connections_active{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}

- Legend:
active


[01:48] Temos a primeira, aqui, vou colocar como legenda "active", esse é o estado ativo. Vou copiar e vamos adicionar uma nova query que, ao invés de olhar para o active, vai olhar para o idle, hikaricp_connections_idle.

[02:17] Então, é idle, já temos as conexões em estado idle, em estado de espera. Vou colocar como "idle" a legenda e vamos adicionar outra query que vai ser o estado pendente, o "pending".


- Métrica que traz as conexões idle:
hikaricp_connections_idle{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}

- Legend:
idle


- Métrica que traz as conexões pending:
hikaricp_connections_pending{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}

- Legend:
pending

- Tipo do gráfico:
Time series

- Título do painel:
CONNECTION STATE

- Descrição:
Estado das conexões com o database

- Legend mode:
Table

- Placement:
Right

- Values
Select values or calculations to show in legend
min, max, last *

- Unidade:
short

- Remover o Threshold vermelho.

[02:40] Todas essas métricas existem na JVM. Já entrou o pending em estado pendente, temos três métricas sendo "plotadas" nesse gráfico, três séries temporais distintas.

[02:58] Vou manter o “Time series”, vou colocar o título como “CONNECTION STATE” e descrição como “Estado das conexões com o database”.

[03:23] Descendo, vou colocar a legenda em um formato tabular. Posso colocá-la do lado direito, fica legal também, vamos fazer uma experiência com isso. Nos valores da legenda, vou colocar o mínimo, o máximo, o último e o total – apesar de o total ser totalmente irrelevante para nós porque ele só vai fazer um contador.

[04:03] Vou até tirar o total para não gerar confusão. Então, mínimo, máximo e último. Aqui, vou trabalhar com a opacidade, vou deixar em 10. Eu gosto dessa configuração, você não precisa fazer assim. Se você achar outro jeito mais interessante, tudo bem, isso é só o layout mesmo, o que é importa é a métrica e o gráfico sendo "plotado".


[04:28] Aqui, vou colocar uma opacidade no gradiente; vou tirar os pontos, não gosto dos pontos; não vou trabalhar no “Eixo X” ou “Eixo Y”; a unidade não vai fazer diferença, vou deixar “Short” porque são números inteiros.

[04:54] Não vou trabalhar com “Threshold”, é um gráfico. Está feito esse painel. Ele é bem interessante para nós. Tenho o “CONNECTION STATE” criado, vou descê-lo e diminuí-lo. Está bagunçado, mas já vamos corrigir, vai ficar legal.

Gráfico "*Connection state*". O eixo X mostra a passagem do tempo, de minuto em minuto, o eixo Y apresenta os valores 0, 5 e 10. A linha amarela (que representa "*idle*") é estável no número 10, exceto uma leve queda perto de 23:25. A linha verde ("*active*") está estável em 0, exceto por um leve aumento, também perto de 23:21. A linha azul ("*pending*") permanece no 0 por todo o período mostrado no gráfico.

[05:37] Está o “CONNECTION STATE”, vamos criar mais um painel. Vou adicionar mais um painel e esse painel vai ser o “Connection Timeout”, vai ser o número de conexões que estão em timeout.

[05:56] Vou colocar o hikaricp_connections_timeout_total, essa métrica. Vou trabalhar com {application=”$application”, instance=”$instance”, job=”app-forum-api”.

[06:28] É isso, não tem muito o que fazer aqui, e o pool, não posso me esquecer, que é a nossa variável pool=”$pool”}. Eu tenho essa informação, só que vou trabalhar novamente com um “Stat” aqui.

[06:51] Vou colocar “DB TIMEOUT” como título e, para descrição, “Conexões com o database em timeout”. Você pode colocar uma descrição melhor se você quiser, não tem problema.

[07:24] Aqui, é um contador. Estamos pegando o timeout_total, ele traz o total para nós, isso não é legal, então vamos fazer uma mudança nisso, vou colocar um range time de [1m] e vou trabalhar com o increase.

[07:47] Só para pegar a taxa de crescimento dele por minuto. Vai estar “0” porque não estamos com o timeout em nenhuma conexão. Vamos trabalhar dessa forma.

[08:02] Aqui, em “Color mode”, vou tirar o “Graph”; em “Unidade”, novamente estamos trabalhando com o número inteiro, então não tem o que mudar. Tendo 5 conexões de timeout em um minuto – até menos –, nós podemos considerar que temos um problema.




- Métrica que traz as conexões pending:
hikaricp_connections_pending{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}

hikaricp_connections_timeout_total{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}


- Tipo de gráfico/painel:
Stat

- Nome do painel:
DB TIMEOUT

- Descrição:
Conexões com o database em timeout

- Adicionar uma função increase:
increase(hikaricp_connections_timeout_total{application="$application", instance="$instance", job="app-forum-api", pool="$pool"}[1m])

- Graph mode:
None

- Unit:
short
pois estamos trabalhando com número inteiro

- Threshold
em vermelho, colocar 5, que é indicativo de problemas

- Nome do painel, mudar para:
DB CONNECTION TIMEOUT






- Simulando erros no JDBC:
docker container stop mysql-forum-api
docker container stop redis-forum-api

- Alterando o Threshold do painel "DB CONNECTION TIMEOUT", pois 2 timeouts já são indicativos de problemas:
2



- Subindo devolta:
docker container start mysql-forum-api
docker container start redis-forum-api



- Métricas normalizando!
- Métricas normalizando!
- Métricas normalizando!