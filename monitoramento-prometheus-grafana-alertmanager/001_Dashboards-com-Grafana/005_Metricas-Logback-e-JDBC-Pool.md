

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 001 - Aula 05 Métricas Logback e JDBC Pool  "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status



# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
#  05 Métricas Logback e JDBC Pool


# Transcrição

[00:00] Vamos dar sequência ao nosso conteúdo. Nessa aula, vamos começar a trabalhar com uma métrica que diz respeito aos níveis de log que estão sendo refletidos nos eventos da nossa aplicação.

[00:18] No Grafana, no canto direito superior, vamos adicionar um painel. A métrica que vamos olhar aqui é a logback_events_total. Vamos colocar alguns labels, os labels que vou utilizar são: {application=”$application”, instance=“$instance”, job=”app-forum-api”}.

[01:12] Eu tenho aqui o logback_events_total e ele vai trazer diversas séries temporais, é um instant vector e traz diversas séries temporais, não somente uma. E onde entra a distinção entre uma série temporal e outra?

[01:29] É o level, é o log level. Se formos olhar no "localhost/metrics", “Ctrl + F”, “level”, vocês vão ver que tem o info, trace, warn, error e debug. Aqui também conseguimos ver essa informação.

[01:52] Então, quais são os eventos que são interessantes para olharmos aqui? Na minha opinião, warn e error. Vamos começar pelo warn. Vou colocar mais um label, level="warn". No momento, eu tenho 90 warns, só que estou olhando uma série temporal inteira não é isso que eu quero.

[02:14] Como vou olhar para o log, quero olhar os últimos cinco minutos para eu ter um nível de informação interessante. Vou trabalhar novamente com a função increase, vou pegar os [5m]. increase(logback_events_total{application=”$application”, instance=“$instance”, job=”app-forum-api,level="warn”}[5m]), nos últimos cinco minutos, tive 0 informação.

[02:35] Aqui, posso trabalhar com sum para fazer uma agregação dos valores que vou ter nesses cinco minutos. O que vou colocar em “Legend” e “Format”? Não vamos mais trabalhar com “Stat”.

[02:52] Na legenda, posso colocar qualquer string, posso colocar "warn", aí vai mudar aqui, está vendo? No painel, embaixo tem um "warn" que eu coloquei na legenda. Só que eu posso fazer uma passagem de label por referência, o que é mais legal.

[03:12] Posso passar o label {{level}}. Esse {{level}} tem que ser passado entre chaves duplas, como se fosse uma passagem de variável por referência. Ele não refletiu para mim, vou ver se coloquei alguma coisa diferente no meu {{level}}.

[03:36] É por causa da agregação. Se eu tirar o sum, ele coloca o level para mim. Realmente acho que é interessante nós fazermos uma agregação aqui, mas vamos seguir assim, depois nós mudamos.

[03:55] Está aqui, nós já temos os 5 minutos e vamos manter como uma métrica, como um gráfico. O título que vou colocar é “WARN & ERROR LOG”. Na descrição, vou colocar “Warning e erros logados nos últimos 5 minutos”.

[04:40] Já está com título e informação, porém, nós só temos uma métrica e ela está relacionada a warn, eu preciso adicionar mais uma. Você pega o scroll, desce um pouco e você vai ver, no canto inferior esquerdo, que tem um plus e um “Query”, é para adicionar uma consulta a mais nesse painel.

[05:04] Clica nele, ele vai abrir o “B” – antes tínhamos o “A” –, é só copiar e colarmos e mudarmos o level; vou colocar como level="error". Pronto, agora já temos o level="warn" e "error" aqui.

[05:27] Estão os dois aqui, os dois estão zerados, não teve nenhuma alteração. Agora nós voltamos para o lado direito para trabalharmos nesse painel.

[05:40] Antes disso, acho que é interessante agitarmos um pouco as coisas, então vou derrubar o MySQL para vermos essa métrica sendo alimentada, no Terminal,docker container stop mysql-forum-api.

[06:02] Vamos derrubar, pronto, já derrubou o contêiner, e vamos trabalhando aqui. Na “Legenda”, muda um pouco as coisas, nós trabalhamos com “Time Series” aqui, então é um gráfico.

[06:25] Vou trabalhar na legenda em forma tabular, como uma tabela, “Legend mode: Table”. Vou manter aqui embaixo mesmo e aqui, nos valores, é que é legal. Eu posso colocar o valor “Min” – isso vai acontecer para as duas métricas –, posso colocar o valor “Max”, a média, o último valor não nulo e o total.

[07:21] No momento, dei uma atualizada e está aqui, temos 12 warnings e 3 erros. Vou trabalhar colocando uma opacidade de nível “10” nessa linha – no “Graph styles”.

[07:48] No gradiente, também vou trabalhar com opacidade, vai dar esse efeito que acho legal. Vou arrancar os pontos, em “Show points” vou colocar “Never”.

[08:00] Em “Eixo Y” e “Eixo X”, eu não vou mexer. Na “Unidade”, é interessante entendermos que estamos trabalhando com um número inteiro, então vou colocar “Short”.

[08:15] Aqui, não estamos olhando uma taxa, então pode ser um valor sem casa decimal. Basicamente, essa é a informação que eu tenho. Vamos imaginar agora, vou trabalhar com sum para ver se temos alguma mudança de resultado. Estou no warn.

[08:34] Trabalhei aqui, tivemos uma mudança. Ela pode ter sido causada porque eu acabei atualizando a consulta, mas vou manter a minha agregação. Não mudou nada depois, então, na prática, nós continuamos não precisando fazer agregação, mas vou manter a consulta dessa forma.

[09:00] Temos um quadro bem interessante com os valores, não preciso de todos deles, então vou retirá-los. Só para confirmar, estamos com o mínimo, o máximo, a média – não preciso da média –, com o último e com o total. Só isso já está interessante para trabalharmos.

[09:25] Quando eu trabalho com o sum, eu perco a anotação da minha variável, então, teria que colocar a minha legenda, por exemplo, warn e, embaixo no B, error.

[09:42] Pronto, ele volta. O que eu vou mudar é o seguinte, clicando nas legendas do gráfico: vou colocar o error em vermelho e o warn em amarelo. Só para fazermos o tira-teima do sum, vou fazer uma consulta agora.

[10:06] Vou arrancar o sum e tenho uma mudança no “Total”, que está em “220” para o warn; se eu volto o sum, temos uma diferenciação. Essa diferenciação pode ser causada tanto pelo scrape quanto pelo tempo, só que aqui, nesse caso, vemos que o ideal realmente é trabalhar com uma agregação de valor.

Gráfico de *warns* (em amarelo) e *erros* (em vermelho). O eixo x apresenta o tempo, de 30 em 30 segundos; o eixo y mostra a quantidade de ocorrências - há apenas o valor 0 e o valor 20. À medida que o tempo passa, os *warns* e *errors* sobem, se aproximando de 20.

[10:38] Vamos voltar. Arrastá-lo para API BASIC. Já estamos com esse painel relacionado a error bem configurado. Vou deixá-lo do lado e vamos partir para a criação do painel que está relacionado ao pool de conexões JDBC.

[10:59] Vamos adicionar um novo painel. Essa métrica que vamos utilizar aqui é o hikaricp_connections. No automático, ela já traz, como qualquer outra métrica, um valor. Vou "setar" os meus labels hikaricp_connections{application=”$application”, instance=”$instance”.

[11:29] Não é obrigatório colocar os labels, mas é legal você se adaptar a isso para quando você estiver configurando um painel que esteja relacionado a alguma aplicação que está em produção, você vai ter que trabalhar com label porque você não vai olhar só para uma instância, só para uma aplicação, você vai olhar para diversas. Então, é legal você já se adaptar a isso.

[11:54] O job="app-forum-api”. Já está configurado, mas faltou uma variável que é o pool="$pool" que nós já configuramos.

[12:22] Vamos trabalhar novamente com o “Stat” nesse caso. Ele está batendo que está em “0” o pool de conexões, é lógico, nós derrubamos a base de dados. O título, vou colocar como “JDBC POOL”.

[12:41] Para a descrição, vou colocar “Pool de conexões JDBC”. Vamos trabalhar com o último valor; o campo é numérico; vou tirar a coloração gráfica dele; na “unidade”, vamos trabalhar com “Short”; não tenho número decimal, nem vou me atrever a colocar, porque não estamos trabalhando com time range, então não é necessário.

[13:15] O “Base” vai ficar em vermelho e o normal em verde. Vou colocar o número default do pool do JDBC que é “10”, em verde. Está feito, vamos voltar e vamos fazer um teste rápido.

[13:36] Está aqui o pool, vamos subir o MySQL agora. Vamos colocar um docker container start mysql-forum-api. Subiu. No canto direito superior, vamos atualizar – deixa só ele fazer um scrape para vermos o valor mudando aqui.

[14:05] Está aí, “JDCB POOL: 10”, o pool de conexões já foi restabelecido. Vamos ver aqui que os logs relacionados a error e a warn vão começar a diminuir drasticamente, vai começar a ter usuário logado e vai começar a ter erro de autenticação, porque é o usuário errando a senha.

[14:29] É isso, nós encerramos essa aula. Na próxima aula, voltamos para finalizar esse capítulo. Já vamos, na próxima aula, configurar dois painéis: um que vai ser o estado e o número das conexões e um que vai olhar o timeout de conexões com a base de dados.

[14:51] É isso, nos vemos na próxima aula.




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
#  05 Métricas Logback e JDBC Pool



[00:00] Vamos dar sequência ao nosso conteúdo. Nessa aula, vamos começar a trabalhar com uma métrica que diz respeito aos níveis de log que estão sendo refletidos nos eventos da nossa aplicação.

[00:18] No Grafana, no canto direito superior, vamos adicionar um painel. A métrica que vamos olhar aqui é a logback_events_total. Vamos colocar alguns labels, os labels que vou utilizar são: {application=”$application”, instance=“$instance”, job=”app-forum-api”}.

[01:12] Eu tenho aqui o logback_events_total e ele vai trazer diversas séries temporais, é um instant vector e traz diversas séries temporais, não somente uma. E onde entra a distinção entre uma série temporal e outra?

[01:29] É o level, é o log level. Se formos olhar no "localhost/metrics", “Ctrl + F”, “level”, vocês vão ver que tem o info, trace, warn, error e debug. Aqui também conseguimos ver essa informação.

[01:52] Então, quais são os eventos que são interessantes para olharmos aqui? Na minha opinião, warn e error. Vamos começar pelo warn. Vou colocar mais um label, level="warn". No momento, eu tenho 90 warns, só que estou olhando uma série temporal inteira não é isso que eu quero.

[02:14] Como vou olhar para o log, quero olhar os últimos cinco minutos para eu ter um nível de informação interessante. Vou trabalhar novamente com a função increase, vou pegar os [5m]. increase(logback_events_total{application=”$application”, instance=“$instance”, job=”app-forum-api,level="warn”}[5m]), nos últimos cinco minutos, tive 0 informação.

[02:35] Aqui, posso trabalhar com sum para fazer uma agregação dos valores que vou ter nesses cinco minutos. O que vou colocar em “Legend” e “Format”? Não vamos mais trabalhar com “Stat”.

[02:52] Na legenda, posso colocar qualquer string, posso colocar "warn", aí vai mudar aqui, está vendo? No painel, embaixo tem um "warn" que eu coloquei na legenda. Só que eu posso fazer uma passagem de label por referência, o que é mais legal.

[03:12] Posso passar o label {{level}}. Esse {{level}} tem que ser passado entre chaves duplas, como se fosse uma passagem de variável por referência. Ele não refletiu para mim, vou ver se coloquei alguma coisa diferente no meu {{level}}.

- Code:
increase(logback_events_total{application="$application", instance="$instance", job="app-forum-api", level="warn"}[5m])

- Legend:
{{level}}


- Panel options
Title
    WARN & ERROR LOG
Description
    Warnings e erros logados nos últimos 5 minutos



- Clicar no "+query", para adicionar uma outra métrica.
- Colocar esta métrica, com o level error, ao invés de warn, para diferenciar:
    increase(logback_events_total{application="$application", instance="$instance", job="app-forum-api", level="error"}[5m])

- Legend:
{{level}}





[03:36] É por causa da agregação. Se eu tirar o sum, ele coloca o level para mim. Realmente acho que é interessante nós fazermos uma agregação aqui, mas vamos seguir assim, depois nós mudamos.

[03:55] Está aqui, nós já temos os 5 minutos e vamos manter como uma métrica, como um gráfico. O título que vou colocar é “WARN & ERROR LOG”. Na descrição, vou colocar “Warning e erros logados nos últimos 5 minutos”.

[04:40] Já está com título e informação, porém, nós só temos uma métrica e ela está relacionada a warn, eu preciso adicionar mais uma. Você pega o scroll, desce um pouco e você vai ver, no canto inferior esquerdo, que tem um plus e um “Query”, é para adicionar uma consulta a mais nesse painel.

[05:04] Clica nele, ele vai abrir o “B” – antes tínhamos o “A” –, é só copiar e colarmos e mudarmos o level; vou colocar como level="error". Pronto, agora já temos o level="warn" e "error" aqui.

[05:27] Estão os dois aqui, os dois estão zerados, não teve nenhuma alteração. Agora nós voltamos para o lado direito para trabalharmos nesse painel.

[05:40] Antes disso, acho que é interessante agitarmos um pouco as coisas, então vou derrubar o MySQL para vermos essa métrica sendo alimentada, no Terminal,docker container stop mysql-forum-api.

[06:02] Vamos derrubar, pronto, já derrubou o contêiner, e vamos trabalhando aqui. Na “Legenda”, muda um pouco as coisas, nós trabalhamos com “Time Series” aqui, então é um gráfico.


docker container stop mysql-forum-api


- Mudar o "Legend Mode" para "Table".

- Legend
Mode
    Table


- O ""Legend Mode" para "Table" faz que os valores da legenda fiquem numa tabela mais fácil de visualizar.


- Selecionar os "Values", para exibir diferentes cálculos das métricas:
    Values
    Select values or calculations to show in legend
    Min, Max, Mean, Last *, Total



- Definir a opacidade em:
    Fill opacity
    Coloquei 10

- No "Gradient Mode", colocar "Opacity" também.

- Em 'Show points", marcar a opção "Never".


[06:25] Vou trabalhar na legenda em forma tabular, como uma tabela, “Legend mode: Table”. Vou manter aqui embaixo mesmo e aqui, nos valores, é que é legal. Eu posso colocar o valor “Min” – isso vai acontecer para as duas métricas –, posso colocar o valor “Max”, a média, o último valor não nulo e o total.

[07:21] No momento, dei uma atualizada e está aqui, temos 12 warnings e 3 erros. Vou trabalhar colocando uma opacidade de nível “10” nessa linha – no “Graph styles”.

[07:48] No gradiente, também vou trabalhar com opacidade, vai dar esse efeito que acho legal. Vou arrancar os pontos, em “Show points” vou colocar “Never”.

[08:00] Em “Eixo Y” e “Eixo X”, eu não vou mexer. Na “Unidade”, é interessante entendermos que estamos trabalhando com um número inteiro, então vou colocar “Short”.

[08:15] Aqui, não estamos olhando uma taxa, então pode ser um valor sem casa decimal. Basicamente, essa é a informação que eu tenho. Vamos imaginar agora, vou trabalhar com sum para ver se temos alguma mudança de resultado. Estou no warn.

[08:34] Trabalhei aqui, tivemos uma mudança. Ela pode ter sido causada porque eu acabei atualizando a consulta, mas vou manter a minha agregação. Não mudou nada depois, então, na prática, nós continuamos não precisando fazer agregação, mas vou manter a consulta dessa forma.

[09:00] Temos um quadro bem interessante com os valores, não preciso de todos deles, então vou retirá-los. Só para confirmar, estamos com o mínimo, o máximo, a média – não preciso da média –, com o último e com o total. Só isso já está interessante para trabalharmos.

[09:25] Quando eu trabalho com o sum, eu perco a anotação da minha variável, então, teria que colocar a minha legenda, por exemplo, warn e, embaixo no B, error.

[09:42] Pronto, ele volta. O que eu vou mudar é o seguinte, clicando nas legendas do gráfico: vou colocar o error em vermelho e o warn em amarelo. Só para fazermos o tira-teima do sum, vou fazer uma consulta agora.

[10:06] Vou arrancar o sum e tenho uma mudança no “Total”, que está em “220” para o warn; se eu volto o sum, temos uma diferenciação. Essa diferenciação pode ser causada tanto pelo scrape quanto pelo tempo, só que aqui, nesse caso, vemos que o ideal realmente é trabalhar com uma agregação de valor.

Gráfico de *warns* (em amarelo) e *erros* (em vermelho). O eixo x apresenta o tempo, de 30 em 30 segundos; o eixo y mostra a quantidade de ocorrências - há apenas o valor 0 e o valor 20. À medida que o tempo passa, os *warns* e *errors* sobem, se aproximando de 20.




- Colocando a agregação do sum agora:

- Code:

sum(increase(logback_events_total{application="$application", instance="$instance", job="app-forum-api", level="error"}[5m]))

sum(increase(logback_events_total{application="$application", instance="$instance", job="app-forum-api", level="warn"}[5m]))


- Legend:
{{level}}



- Observação:
Quando utilizada a agregação do sum, é perdido o valor da variável level(warn, error, etc) na legenda do painel.















[10:38] Vamos voltar. Arrastá-lo para API BASIC. Já estamos com esse painel relacionado a error bem configurado. Vou deixá-lo do lado e vamos partir para a criação do painel que está relacionado ao pool de conexões JDBC.

[10:59] Vamos adicionar um novo painel. Essa métrica que vamos utilizar aqui é o hikaricp_connections. No automático, ela já traz, como qualquer outra métrica, um valor. Vou "setar" os meus labels hikaricp_connections{application=”$application”, instance=”$instance”.

[11:29] Não é obrigatório colocar os labels, mas é legal você se adaptar a isso para quando você estiver configurando um painel que esteja relacionado a alguma aplicação que está em produção, você vai ter que trabalhar com label porque você não vai olhar só para uma instância, só para uma aplicação, você vai olhar para diversas. Então, é legal você já se adaptar a isso.

[11:54] O job="app-forum-api”. Já está configurado, mas faltou uma variável que é o pool="$pool" que nós já configuramos.

[12:22] Vamos trabalhar novamente com o “Stat” nesse caso. Ele está batendo que está em “0” o pool de conexões, é lógico, nós derrubamos a base de dados. O título, vou colocar como “JDBC POOL”.

[12:41] Para a descrição, vou colocar “Pool de conexões JDBC”. Vamos trabalhar com o último valor; o campo é numérico; vou tirar a coloração gráfica dele; na “unidade”, vamos trabalhar com “Short”; não tenho número decimal, nem vou me atrever a colocar, porque não estamos trabalhando com time range, então não é necessário.

[13:15] O “Base” vai ficar em vermelho e o normal em verde. Vou colocar o número default do pool do JDBC que é “10”, em verde. Está feito, vamos voltar e vamos fazer um teste rápido.

[13:36] Está aqui o pool, vamos subir o MySQL agora. Vamos colocar um docker container start mysql-forum-api. Subiu. No canto direito superior, vamos atualizar – deixa só ele fazer um scrape para vermos o valor mudando aqui.

[14:05] Está aí, “JDCB POOL: 10”, o pool de conexões já foi restabelecido. Vamos ver aqui que os logs relacionados a error e a warn vão começar a diminuir drasticamente, vai começar a ter usuário logado e vai começar a ter erro de autenticação, porque é o usuário errando a senha.

[14:29] É isso, nós encerramos essa aula. Na próxima aula, voltamos para finalizar esse capítulo. Já vamos, na próxima aula, configurar dois painéis: um que vai ser o estado e o número das conexões e um que vai olhar o timeout de conexões com a base de dados.

[14:51] É isso, nos vemos na próxima aula.



# CONTINUA EM
11:01