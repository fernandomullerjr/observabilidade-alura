

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 001 - Aula 04 Variáveis e métricas Uptime e Start Time  "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status



# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Variáveis e métricas Uptime e Start Time

# Transcrição

[00:00] Vamos dar sequência no nosso curso. Agora que já temos uma pasta e já temos o nosso dash criado, vamos começar a criação dos painéis. Antes disso, vamos iniciar o uso de um recurso bem interessante do Grafana, que são as variáveis.

[00:22] Por que usar variáveis no Grafana? Vamos imaginar que a sua API foi "deployada" em um ambiente distribuído. Você teria que ter uma métrica para cada instância em que sua API fosse "deployada". Isso não seria nada interessante.

[00:42] Com uma variável, você pode simplesmente alimentar essa variável com valores que estão relacionados às instâncias que estão suportando a sua API.

[00:55] Além disso, podemos conter uma métrica que possa atender mais de uma aplicação. Então, quando utilizamos variáveis, nós tornamos as nossas métricas dinâmicas, o que fica mais interessante.

[01:11] Para configurar a variável, é bem simples. No Grafana, no canto superior direito, vamos em “Dashboard settings > Variables” e vamos adicionar a nossa primeira variável. Tem o nome da variável, vou chamá-la de “application”. O type da variável é como ela é configurada.

[01:35] Essa variável vai ser configurada como uma “Query”, uma consulta. Vamos fazer uma consulta no endpoint e a configuração dessa variável vai ser herdada de uma configuração já estabelecida lá no endpoint de métricas.

[01:51] Existem outros tipos também, poderíamos fazer “Customizada”, “Constante” etc. Nesse momento, vamos trabalhar com o tipo “Query”. O label é o rótulo da variável, vai ficar como “application” também.

[02:07] Aqui, nas opções de query, o data source é o “Prometheus”, vamos buscar lá no endpoint que está configurado nesse data source e a query que vamos fazer é bem simples.

[02:21] O primeiro ponto para entender é que vamos trabalhar com um label_value. Então, entre parênteses, dizemos qual é a configuração de label que vai ser encontrada no endpoint de métricas e que vai alimentar a nossa variável application.

[02:43] Nesse caso, é o valor (application). Então fica label_value(application) Quando estávamos mexendo no application properties, nós definimos uma property chamada application que tem como valor app-forum-api. É essa.

[03:00] Fazendo isso, automaticamente o Grafana já exibe para nós um preview “app-forum-api”. Simples assim, está feito, vou clicar em “Update” no final da página e temos aqui a primeira variável configurada.

[03:17] À direita da variável, com um símbolo de atenção, fica um aviso de que essa variável não foi referenciada por nenhuma outra variável ou dashboard. É questão de tempo, vamos criar outra variável que vai referenciar essa variável e esse aviso vai sumir. Ele não é um erro, é normal.

[03:36] Vamos criar uma próxima variável que vai se chamar “instance”. Também vai ser um tipo “Query” e vai ter o label “instance” também. Ela vai usar como “data source” o “Prometheus”.

[03:52] A query que vamos fazer vai ser label_values() e aqui, dentro dos parênteses de label_values(), vamos pegar uma métrica que é o jvm – deu um erro porque eu tirei o cursor daqui –, vamos trabalhar com essa métrica, (jvm_classes_loaded_classes).

[04:16] Dentro dessa métrica é que vamos configurar a variável “instance”. Ela trabalha buscando por application. Dentro de application, vamos ter application. Por fim, fora dessa configuração de métrica, nós declaramos o instance, que vai ser alimentado. Então a query fica label_values(jvm_classes_loaded_classes{application="$application"}, instance).

[04:46] Automaticamente, o preview já traz para nós “app-forum-api:8080”. 8080 é a porta TCP que a aplicação está rodando e ela está rodando na instância de nome “app-forum-api” – lembrando que esse é o nome do contêiner.

[05:03] Tendo feito isso, vou clicar em “Update”. Já temos agora a variável “application” e a “instance” configuradas. Vou criar mais uma variável. O nome dessa variável vai ser “pool”, ela vai ter como label “pool” também.

[05:21] O tipo é consulta (Query) e aqui é a métrica que vamos usar para buscar essa variável. A métrica que vamos usar – deixa eu configurar o label_values(hikaricp_connections{instance="$instance", application="$application"}, pool), essa é a métrica, com os labels instance e application.

[05:50] Ela vai pegar o valor do pool de conexões, esse é o pool da JDBC, é o hikari pool cp. Automaticamente, já aparece aqui “HikariPool-1”. Então, estão configuradas as nossas variáveis, vou dar um “Update”.

[06:12] Ela não é referenciada por outra variável, é por isso que esse aviso fica feito aqui. Vou salvar o dashboard com essa mudança e vou colocar “variáveis”.

[06:24] Está salvo, vamos voltar. Já temos aqui “application”, “instance” e “pool” de conexões que vamos utilizar. Agora, vamos ao canto direito superior, clicar em "Add Panel" e adicionar uma nova linha, uma “New row”.

[06:40] Clicarei em "Row Title". Vou chamar a linha de “API BASIC”. É dentro dessa row que vamos criar os painéis desse capítulo 3. Vamos começar já criando um painel bem simples que, basicamente, vai ser uma métrica que traz para nós o uptime da nossa API.

[07:08] Na verdade, ela traz o uptime do processo. No painel inferior, em "metrics browser", vou rodar process_uptime_seconds. Se olharmos aqui, ele vai trazer um dado para nós que não parece muito interessante, mas vamos formatar isso.

[07:28] Eu vou trabalhar com o label application. Quem é o application? Eu posso preencher direto aqui, mas é legal trabalharmos com as nossas variáveis.

[07:40] Porque, se em algum momento eu tiver outro application que essa métrica possa ser utilizada, a variável vai ter mais de um valor, é só fazer um ajuste nela e conseguimos trabalhar com ela como se ela fosse um vetor, conseguimos pegar diversos índices.

[08:00] Vai ter o application e o instance. Poderia colocar outro label que é muito importante, que é o job. Esse eu acabei não criando variável, app-forum-api, esse é o job. Fica process_uptime_seconds{application="$application",instance="$instance",job="app-forum-api"}.

[08:20] Já temos essa métrica. Na legenda, não tem o que colocar, vamos entender. Sob "Panel Title", tenho o painel em que a métrica vai ser exibida para mim. No painel abaixo, eu tenho o data source e as opções de query – inclusive, posso inspecionar a minha query.

[08:41] Ainda no painel inferior, tenho o browser de métricas que eu coloco a minha métrica e ela vai gerar o painel aqui, a informação que vai ser exibida no painel. Mais abaixo, posso colocar uma legenda, o formato que estou utilizando, e tenho outras opções, como o “Min step”, “Resolution”, enfim, não vamos lidar com essas configurações agora.

[09:08] O que importa para nós é configurar, do lado direito superior, qual é o tipo de visualização. Temos diversos tipos de visualizações. Para essa, vamos utilizar o “Stat”. Ele já trouxe um número interessante para nós. Vou chamar, no campo para título que apareceu à direita, de “UPTIME” e vou colocar como descrição “API uptime”.

[09:35] Essa é a descrição. Quando coloco uma descrição, quando alguém colocar o mouse sobre o painel, ele vai ver a descrição. Agora, vamos trabalhar aqui.

[09:47] Descendo em “Opções de valores”, temos a forma de cálculo do valor que vai ser exibido. Vamos pegar o último valor não nulo, é esse “Last” e o tipo de campo é “Numérico”.

[10:03] Aqui, temos o modo de coloração. Se eu colocar em “None”, ele vai ficar sem cor; em “Value”, ele colore o valor; e em “Background”, é o fundo. Vou manter em “Value”.

[10:15] “Graph mode” é essa colação de métrica que fica aqui, eu não vou utilizar, eu quero só o número. Em "Text Size", tamanho de texto, eu não vou mexer para não alterar essa configuração.

[10:31] Nas opções padrões, nas “Standard options”, temos o tipo de unidade. A unidade para esse valor é “Time” e, nesse caso, uma medida de uptime que eu acho interessante que se parece muito quando você está olhando o uptime, por exemplo, de um sistema operacional, é a duração em hora, minuto e segundo.

[10:54] Está aqui, a aplicação está de pé há 4 horas, 46 minutos e 15 segundos. Descendo, temos o “Threshold” que não vai ser utilizado, vou colocar essa métrica em cor azul.

[11:12] Está feito, está aqui o “UPTIME”, vou arrastá-lo para dentro da “API BASIC”. Já temos o primeiro painel com o uptime da aplicação, vamos criar outro.

[11:27] Esse agora que vamos criar ele vai utilizar a métrica process_start_time_seconds. Vamos pegar o momento que o processo foi "startado". Temos o uptime da nossa API, mas temos o start time de processo que pode mudar se "restartarmos" a nossa aplicação.

[11:55] Isso não vai influenciar no uptime, são dois valores distintos. Vamos trabalhar com os labels process start_time_seconds{application=”$application”, instance=”$instance”, job=”app-forum-api”}.

[12:24] Depois, nós configuramos uma variável como job para não termos que ficar fazendo isso todas as vezes, para simplesmente colocarmos job=job para caso tenhamos mais de um job e possamos utilizar a variável como um recurso válido.

[12:39] Tendo colocado essa métrica, temos um valor que não fica interessante em forma de gráfico. Vamos mudar a nossa visualização também para um formato “Stat”. Temos um Unix timestamp, temos uma numeração que é difícil de entender, vamos ter que fazer uma conversão rápida nela.

[13:00] Vou por como título desse painel “START TIME”. Na descrição, vou colocar “Hora da inicialização da API”. Agora, no “Cálculo”, vamos usar o último valor; no campo numérico não vamos mudar nada; o que vou fazer é que o “Graph mode” eu vou retirar para ele não ficar com aquela linha.

[13:32] Na “Unidade”, vamos ter uma diferença. Nós trabalhamos com “Time” na unidade anterior, aqui, vou trabalhar com “Date & Time” e vou colocar “Datetime local”.

[13:48] Ele apareceu com um formato que tem o mês, dia, ano e hora. Não é muito interessante. Deixa eu procurar outro que não tenha toda essa informação. O "Datetime ISO", nenhum deles está vindo da forma que eu gostaria.

[14:10] Quando escolhemos “Datetime ISO (No date if today)”, ele não traz para nós o ano, o mês e o dia, mas aqui está com um bug que ele está trazendo, então para vocês é possível que não apareça dessa forma que está aqui.

[14:25] Vou acabar colocando o default, o “Datetime ISO”. Aqui, em “Threshold”, vou retirar, vou colocar uma cor azul. Ele está aqui “1970-01-19” porque é o Unix timestamp. Vou multiplicar por 1000 (* 1000) e acabo com esse problema.

[14:58] Inclusive, sumiu tudo. Fazendo essa conversão, está aqui, só não está no fuso adequado. Vou tirar do “ISO”, vou colocar o “Datetime local”, e ainda está incorreto, não era para ele estar trazendo esse horário, tem alguma coisa de questão de horário do meu contêiner que está incorreta.

[15:24] Não vou me preocupar com isso agora, depois isso vai ser corrigido quando eu subir a stack novamente. Não vou investigar isso agora para não perdermos tempo, o que importa é que o “Start time” está aqui, também vou arrastar para "API BASIC".

[15:37] Na verdade, está certo o “Start time”, está completamente certo, foi a hora que eu subi a minha API, eu subi às 05:48 da tarde mesmo, não tem nada errado, está correto.

[15:50] Aqui, já temos dois painéis configurados, estamos trabalhando com variáveis e abrimos caminho para a próxima aula que vamos estar trabalhando com uma métrica de usuários autenticados e erros de autenticação.

[16:07] Te vejo na próxima aula.


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Variáveis e métricas Uptime e Start Time


[00:00] Vamos dar sequência no nosso curso. Agora que já temos uma pasta e já temos o nosso dash criado, vamos começar a criação dos painéis. Antes disso, vamos iniciar o uso de um recurso bem interessante do Grafana, que são as variáveis.

[00:22] Por que usar variáveis no Grafana? Vamos imaginar que a sua API foi "deployada" em um ambiente distribuído. Você teria que ter uma métrica para cada instância em que sua API fosse "deployada". Isso não seria nada interessante.

[00:42] Com uma variável, você pode simplesmente alimentar essa variável com valores que estão relacionados às instâncias que estão suportando a sua API.

[00:55] Além disso, podemos conter uma métrica que possa atender mais de uma aplicação. Então, quando utilizamos variáveis, nós tornamos as nossas métricas dinâmicas, o que fica mais interessante.

[01:11] Para configurar a variável, é bem simples. No Grafana, no canto superior direito, vamos em “Dashboard settings > Variables” e vamos adicionar a nossa primeira variável. Tem o nome da variável, vou chamá-la de “application”. O type da variável é como ela é configurada.

[01:35] Essa variável vai ser configurada como uma “Query”, uma consulta. Vamos fazer uma consulta no endpoint e a configuração dessa variável vai ser herdada de uma configuração já estabelecida lá no endpoint de métricas.

[01:51] Existem outros tipos também, poderíamos fazer “Customizada”, “Constante” etc. Nesse momento, vamos trabalhar com o tipo “Query”. O label é o rótulo da variável, vai ficar como “application” também.


- Variavel:
application

- Tipo do variavel:
Query

- Label:
application


[02:07] Aqui, nas opções de query, o data source é o “Prometheus”, vamos buscar lá no endpoint que está configurado nesse data source e a query que vamos fazer é bem simples.

[02:21] O primeiro ponto para entender é que vamos trabalhar com um label_values. Então, entre parênteses, dizemos qual é a configuração de label que vai ser encontrada no endpoint de métricas e que vai alimentar a nossa variável application.

[02:43] Nesse caso, é o valor (application). Então fica label_values(application) Quando estávamos mexendo no application properties, nós definimos uma property chamada application que tem como valor app-forum-api. É essa.

label_values(application)




- Query:
label_value(application)

- Erro:
invalid parameter "match[]": 1:12: parse error: unexpected "("


- Corrigido:
faltava o s ao final, values, ao invés de value
    label_values(application)



[03:00] Fazendo isso, automaticamente o Grafana já exibe para nós um preview “app-forum-api”. Simples assim, está feito, vou clicar em “Update” no final da página e temos aqui a primeira variável configurada.

- No Grafana:

Preview of values
app-forum-api






Variables
Variables can make your dashboard more dynamic and act as global filters.
Variable	Definition	
label_values(application)


[03:17] À direita da variável, com um símbolo de atenção, fica um aviso de que essa variável não foi referenciada por nenhuma outra variável ou dashboard. É questão de tempo, vamos criar outra variável que vai referenciar essa variável e esse aviso vai sumir. Ele não é um erro, é normal.

[03:36] Vamos criar uma próxima variável que vai se chamar “instance”. Também vai ser um tipo “Query” e vai ter o label “instance” também. Ela vai usar como “data source” o “Prometheus”.

[03:52] A query que vamos fazer vai ser label_values() e aqui, dentro dos parênteses de label_values(), vamos pegar uma métrica que é o jvm – deu um erro porque eu tirei o cursor daqui –, vamos trabalhar com essa métrica, (jvm_classes_loaded_classes).

label_values(jvm_classes_loaded_classes)


[04:16] Dentro dessa métrica é que vamos configurar a variável “instance”. Ela trabalha buscando por application. Dentro de application, vamos ter application. Por fim, fora dessa configuração de métrica, nós declaramos o instance, que vai ser alimentado. Então a query fica label_values(jvm_classes_loaded_classes{application="$application"}, instance).

label_values(jvm_classes_loaded_classes{application="$application"}, instance)

[04:46] Automaticamente, o preview já traz para nós “app-forum-api:8080”. 8080 é a porta TCP que a aplicação está rodando e ela está rodando na instância de nome “app-forum-api” – lembrando que esse é o nome do contêiner.


Preview of values
app-forum-api:8080


[05:03] Tendo feito isso, vou clicar em “Update”. Já temos agora a variável “application” e a “instance” configuradas. Vou criar mais uma variável. O nome dessa variável vai ser “pool”, ela vai ter como label “pool” também.

pool


[05:21] O tipo é consulta (Query) e aqui é a métrica que vamos usar para buscar essa variável. A métrica que vamos usar – deixa eu configurar o label_values(hikaricp_connections{instance="$instance", application="$application"}, pool), essa é a métrica, com os labels instance e application.

label_values(hikaricp_connections{instance="$instance", application="$application"}, pool)


[05:50] Ela vai pegar o valor do pool de conexões, esse é o pool da JDBC, é o hikari pool cp. Automaticamente, já aparece aqui “HikariPool-1”. Então, estão configuradas as nossas variáveis, vou dar um “Update”.

Preview of values
HikariPool-1

[06:12] Ela não é referenciada por outra variável, é por isso que esse aviso fica feito aqui. Vou salvar o dashboard com essa mudança e vou colocar “variáveis”.

[06:24] Está salvo, vamos voltar. Já temos aqui “application”, “instance” e “pool” de conexões que vamos utilizar. Agora, vamos ao canto direito superior, clicar em "Add Panel" e adicionar uma nova linha, uma “New row”.