

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 003 - Aula 03 Conhecendo as configurações do Prometheus"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Conhecendo as configurações do Prometheus

# Transcrição

[00:00] Sejam bem-vindos à segunda aula do Capítulo 2 do nosso curso. Na aula anterior, nós subimos a stack com a API "dockerizada" e o Prometheus. Agora, vamos entender um pouco melhor essa interface web do Prometheus e entender a configuração que está por trás disso.

[00:25] Para você acessar o Prometheus, é “http://localhost:9090”. Acessando o Prometheus, você vai cair diretamente aqui, nessa tela, e vai ver que tem aqui o histórico, que pode ser habilitado para consultas; a utilização do tempo local, você pode habilitá-lo ou manter o padrão.

[00:49] Você vê que existem, nesse painel, algumas opções. Aqui, você encontra basicamente o tempo em que você vai executar uma consulta, então você pode definir o tempo em que você quer rodar aquela consulta.

[01:07] Existe o modo tabulado de exibição e o modo gráfico, em que nós vamos acabar vendo um gráfico mesmo. Vamos entender essas opções de cima. Também tem a opção de adicionar painéis.

[01:25] Futuramente, não vamos trabalhar com essa interface, porque ela é muito simples para o propósito que nós temos, mas ela é extremamente funcional para você validar uma consulta, para você construir uma métrica, fazer um indicador.

[01:44] Além de ter alguns outros recursos que vamos utilizar na hora de verificar se temos uma regra de alerta funcional, na hora de verificar se o service discovery está olhando para todas as aplicações que nós configuramos etc.

[02:04] Clicando em “Alerts”, não temos nenhum alerta configurado ainda, isso vamos fazer mais para frente, em outro capítulo, quando trabalharmos o Alertmanager.

[02:15] Em “Graph”, nós voltamos para o mesmo lugar em que estávamos anteriormente; em “Status”, temos algumas informações legais, como runtime e informação de build – não vou aprofundar muito nisso, é só mesmo para você entender o que é cada uma dessas opções.

[02:35] Aqui está o status do TSBD, que é o Time Series Database. Você pode dar uma olhada com mais calma e entender essa configuração, vou deixar um link da documentação. No momento, isso não vai fazer diferença para nós.

[02:54] Aqui estão as flags de linha de comando que você pode usar para subir o Prometheus na hora da execução dele. Nós usamos algumas naquele Docker Compose, que vimos na aula de introdução a esse capítulo.

[03:09] Se você não viu essa aula, volta lá e dá uma olhada. Você vai ver que aqui tem diversas flags que podem ser utilizadas na hora de subir o serviço.

[03:18] Além disso, aqui está a “Configuração”. Nessa configuração, vamos olhar o arquivo que gerou essa configuração. Essa configuração está sendo derivada de um arquivo, já vamos olhar para ele, mas é legal você entender que essa configuração você vai poder validar aqui.

[03:41] Você vem em “Status > Configuration” e você vai ver essa configuração. Em “Regras”, não tem nenhuma regra habilitada, não temos o que exibir.

[03:57] Quando você vem em “Targets”, você pode ver quais são os targets que o Prometheus está olhando, quais são os alvos de coleta. Temos aqui o contêiner “app-forum-api-8080/actuator/prometheus”, esse é o endpoint que o Micrometer disponibilizou para nós com as métricas traduzidas em um formato legível para o Prometheus.

[04:26] Aqui, temos um endpoint que é o próprio Prometheus, ele olha para ele mesmo. O Prometheus monitora ele mesmo e "plota" essas métricas. Se colocarmos aqui “localhost:9090” – o Prometheus roda na porta “9090” TCP – e colocarmos um “localhost:9090/metrics”, vamos pegar as métricas do próprio Prometheus.

[04:56] Como é uma aplicação feita em Go, você vai ver muitas métricas relacionadas à execução do Go. Tem bastante métricas, eu não vou entrar no mérito das métricas do Prometheus, mas é interessante você entender que você pode, em algum momento, ter uma curiosidade do funcionamento do Prometheus, de algum aspecto que parece que não está interessante e olhar as métricas dele.

[05:31] Deixa eu tirar esse zoom, aqui estão os targets. Se você for em “Unhealthy”, não tem nenhum. Basicamente, está tudo certo. Aqui, o “Service Discovery”. Para onde estamos olhando no momento, é o “app-forum-api” e para o próprio “prometheus-forum-api”.

[05:51] Aqui, temos o “Help”, que nos leva direto para a documentação do Prometheus, que é muito legal. Vou abrir em outra aba, e temos a versão clássica da interface dele.

[06:04] Eu não vou trabalhar nessa versão clássica, vou para o novo modelo, até para que você esteja atualizado com a última versão gráfica da interface do Prometheus, não é interessante utilizarmos a antiga, porque versões futuras do Prometheus vão descontinuar esse tipo de visualização.

[06:38] Agora que já falamos um pouco da interface gráfica do Prometheus – só ressaltar que é aqui que vamos fazer as consultas das nossas métricas. Nesse capítulo, vamos focar nessa visualização e, principalmente, aqui. Basicamente, vamos estar centralizando tudo em “Graph”.

[07:03] Vamos dar uma olhada aqui, deixa eu encontrar o meu Terminal. Estávamos falando do ngnix, na aula de apresentação, está aqui o proxy_pass configurado. Agora, vamos subir o nível e vamos entrar no prometheus, cd ../prometheus/

[07:24] Aqui no prometheus, tem uma coisa que eu jamais faria em produção, mas, nesse ambiente "conteinerizado", foi feito. O diretório prometheus_data está com permissão 777. O diretório somente dentro dele não está com 777, mas, se eu olhar só para o diretório, você vai ver que ele está como 777.

[07:54] Por quê? Porque ele está no path do meu usuário no Linux, e não é o mesmo usuário de execução do Prometheus, que logicamente não é o root. Então, ele não tinha permissão para acessar esse compartilhamento.

[08:11] Para esse diretório específico, eu liberei um chmod 777 prometheus_data, foi o que eu fiz para que o contêiner funcionasse direito, por isso a coloração estranha no diretório.

[08:30] Isso é uma peculiaridade do Linux. No Windows, provavelmente você vai ter que olhar esse diretório e ir nas propriedades da pasta, “Segurança > Segurança Avançada” e mudar as permissões para outros, permitindo que qualquer usuário que tenha acesso a essa pasta possa acessá-la e escrever nela.

[08:58] Dificilmente você vai precisar fazer alguma mudança porque estamos falando de uma "conteinerização" em Linux. Enfim, seguindo, além desse diretório, temos o mais importante da aula que é o prometheus.yml.

[09:15] Esse arquivo de configuração gerou aquela configuração que nós vimos lá na interface web. Aqui, o que é importante? scrape_interval, que é uma característica global nessa configuração e em qualquer outra.

[09:30] O que muda aqui? Eu defini como 5s. A regra é que, para qualquer configuração de scrape – scrape é o tempo que o Prometheus demora para fazer uma consulta em um endpoint de métrica, então, scrape_interval, scrape_time, enfim, é o tempo do Prometheus entre uma consulta e outra em um endpoint de métricas.

[09:56] Eu coloquei 5s, isso está global. Eu posso mudar isso? Posso, é só fazer uma configuração diferente em algum job. Como é a divisão disso? Eu tenho aqui scrape_interval, que é uma característica global nessa configuração, e tenho scrape_configs, que são as configurações de scrape personalizadas.

[10:20] Se eu quiser sobrescrever isso e mudar o valor em alguma scrape_config, eu tenho que trabalhar dentro do escopo de um job. Tenho aqui job_name: prometheus-forum-api, que é aquela consulta que o Prometheus faz nele mesmo.

[10:37] Aqui, o meu scrape_interval é de 15s; o timeout de 10s. Então, ele está sobrescrevendo a configuração global aqui. Qual é o metrics_path que ele olha, qual o path de métricas? É o /metrics.

[10:55] Qual é o esquema? http, o protocolo HTTP. As configurações estáticas são o target dele, que é o prometheus-forum-api:9090. Importante, tem que ter o IP, o FQDN da máquina ou o hostname, a porta TCP, onde o Prometheus está rodando.

[11:17] Aqui, em metrics_path, o path, o endpoint em que a métrica está. É legal que uma informação complementa a outra. Aqui, tenho outro job_name que, nesse caso, é o app-forum-api.

[11:38] O metrics_path dele é em /actuator/prometheus e a static_configs tem um target que é o app-forum-api:8080. Se formos avaliar, você vai ver que esse job app-forum-api está com o scrape_interval de 5s, está herdando a característica da configuração global.

[12:04] Não estou sobrescrevendo, acho que 5 segundos é o ideal nesse caso para olharmos para uma API, mas você pode customizar isso. Fiz uma pequena divisão no arquivo para ele ficar legível, não iria causar nenhum erro, mas estou retornando-o para o formato normal.

[12:25] Isso é a nossa introdução à configuração do Prometheus. Vamos ver tópicos mais avançados até o fim do curso, mas, no momento, isso é necessário para que você possa entender o funcionamento dele.

[12:39] Por último, vamos só dar uma olhada no Docker Compose para você entender que, no momento de subida do contêiner, nós estamos utilizando esses arquivos.

[12:55] O local exato da configuração do prometheus.yml, do arquivo, é em /etc/prometheus/prometheus.yml, e aqui é onde está o TSDB, é um diretório que está saindo da nossa máquina, é aquele diretório que eu tive que dar uma permissão muito aberta para que o usuário do Prometheus pudesse manipulá-lo dentro do contêiner.

[13:20] Aqui estão as flags. Lembra das flags que vimos na interface web? Então, aqui tem algumas flags que estão sendo utilizadas que podem ser encontradas na interface web dele.

[13:35] Então, é isso, foi só uma navegação pelo Prometheus, entendendo algumas configurações dele. Vamos aprofundando, vamos trabalhando mais nisso e olhando para o que interessa no decorrer do curso.

[13:54] Na próxima aula, já vamos lidar com outro assunto, que é a anatomia de uma métrica. Vamos começar a fazer nossas consultas e é de extrema importância que você entenda o que é cada campo em uma métrica e como isso pode ser manipulado a seu favor na hora de obter uma informação importante.

[14:18] É isso, nos vemos na próxima aula.




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Conhecendo as configurações do Prometheus


http://192.168.0.113:9090/graph
<http://192.168.0.113:9090/graph>





- TARGETS

[03:57] Quando você vem em “Targets”, você pode ver quais são os targets que o Prometheus está olhando, quais são os alvos de coleta. Temos aqui o contêiner “app-forum-api-8080/actuator/prometheus”, esse é o endpoint que o Micrometer disponibilizou para nós com as métricas traduzidas em um formato legível para o Prometheus.

Targets
app-forum-api (1/1 up)
Endpoint	State	Labels	Last Scrape	Scrape Duration	Error
http://app-forum-api:8080/actuator/prometheus	UP	
instance="app-forum-api:8080"job="app-forum-api"
	29.865s ago	
2.778ms
	
prometheus-forum-api (1/1 up)
Endpoint	State	Labels	Last Scrape	Scrape Duration	Error
http://prometheus-forum-api:9090/metrics	UP	
instance="prometheus-forum-api:9090"job="prometheus-forum-api"
	30.917s ago	
9.127ms
	








- Métricas do próprio Prometheus

http://192.168.0.113:9090/metrics
<http://192.168.0.113:9090/metrics>

go_gc_duration_seconds{quantile="0"} 4.8571e-05
go_gc_duration_seconds{quantile="0.25"} 0.000170094
go_gc_duration_seconds{quantile="0.5"} 0.000261857
go_gc_duration_seconds{quantile="0.75"} 0.000439591
go_gc_duration_seconds{quantile="1"} 0.003070204
go_gc_duration_seconds_sum 0.020735225

prometheus_http_request_duration_seconds_count{handler="/-/ready"} 2
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="0.1"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="0.2"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="0.4"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="1"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="3"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="8"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="20"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="60"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="120"} 1
prometheus_http_request_duration_seconds_bucket{handler="/api/v1/alertmanagers",le="+Inf"} 1
prometheus_http_request_duration_seconds_sum{handler="/api/v1/alertmanagers"} 0.000856258

[04:56] Como é uma aplicação feita em Go, você vai ver muitas métricas relacionadas à execução do Go. Tem bastante métricas, eu não vou entrar no mérito das métricas do Prometheus, mas é interessante você entender que você pode, em algum momento, ter uma curiosidade do funcionamento do Prometheus, de algum aspecto que parece que não está interessante e olhar as métricas dele.














cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus

~~~~bash
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus# ls -lhasp
total 16K
4.0K drwxr-xr-x 3 fernando fernando 4.0K Nov 14 23:32 ./
4.0K drwxr-xr-x 7 fernando fernando 4.0K Nov 14 23:33 ../
4.0K drwxr-xr-x 5 fernando fernando 4.0K Nov 15 11:36 prometheus_data/
4.0K -rw-r--r-- 1 fernando fernando  346 Nov 14 23:32 prometheus.yml
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus# ^C
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus# pwd
/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus#
~~~~

[07:03] Vamos dar uma olhada aqui, deixa eu encontrar o meu Terminal. Estávamos falando do ngnix, na aula de apresentação, está aqui o proxy_pass configurado. Agora, vamos subir o nível e vamos entrar no prometheus, cd ../prometheus/

[07:24] Aqui no prometheus, tem uma coisa que eu jamais faria em produção, mas, nesse ambiente "conteinerizado", foi feito. O diretório prometheus_data está com permissão 777. O diretório somente dentro dele não está com 777, mas, se eu olhar só para o diretório, você vai ver que ele está como 777.

[07:54] Por quê? Porque ele está no path do meu usuário no Linux, e não é o mesmo usuário de execução do Prometheus, que logicamente não é o root. Então, ele não tinha permissão para acessar esse compartilhamento.

[08:11] Para esse diretório específico, eu liberei um chmod 777 prometheus_data, foi o que eu fiz para que o contêiner funcionasse direito, por isso a coloração estranha no diretório.


chmod 777 prometheus_data/

~~~~bash
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus# ls -lhasp
total 16K
4.0K drwxr-xr-x 3 fernando fernando 4.0K Nov 14 23:32 ./
4.0K drwxr-xr-x 7 fernando fernando 4.0K Nov 14 23:33 ../
4.0K drwxrwxrwx 5 fernando fernando 4.0K Nov 15 11:36 prometheus_data/
4.0K -rw-r--r-- 1 fernando fernando  346 Nov 14 23:32 prometheus.yml
root@debian10x64:/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/prometheus#
~~~~
