


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 003 - Aula 02 Subindo a stack com API e Prometheus"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Subindo a stack com API e Prometheus

# Transcrição

[00:00] Seja bem-vindo à primeira aula do Capítulo 2. Nesse capítulo, vamos consumir as métricas com o Prometheus, porém, nessa aula 1, temos uma mudança na nossa stack.

[00:17] Lembra que, na última aula do Capítulo I (uma aula bem rápida), nós preparamos a aplicação para ela ser conteinerizada? Nós mudamos a chamada do Redis e a chamada do MySQL para o nome dos contêineres.

[00:35] Tendo feito isso, posso fechar o Eclipse, a IDE. Atualmente, a aplicação está rodando – na verdade, a aplicação não está mais rodando, já parei a minha aplicação, mas ainda estou com esse endpoint sendo exibido.

[00:56] O que vamos fazer? Vamos dar uma olhada no arquivo docker-compose, que está disponibilizado na plataforma. Você tem que pegar esse arquivo, a aplicação já foi "buildada" com essas alterações.

[01:13] Vou abrir o docker_compose.ymal (vim docker-compose.yaml), vou colocar aqui a numeração das linhas e vamos falar um pouco desse arquivo antes de subir a stack. O que foi mudado aqui? As redes internas do Docker.

[01:32] Agora eu tenho uma rede chamada database, que é interna; tenho uma rede chamada cache, que também é interna; uma rede chamada api, que é interna; e tenho uma rede chamada monit, que, no momento, está interna, porque vamos utilizar a interface web do Prometheus para entender como funciona a linguagem PromQL e compor as nossas primeiras métricas.

[01:56] Futuramente, ela vai também ser uma rede interna porque Prometheus server não precisa estar exposto. E a rede proxy, que é a rede que vai conduzir as requisições à aplicação, requisições externas.

[02:15] Os clientes vão consumir essa aplicação, mas, antes de chegar na aplicação, eles serão filtrados por um proxy, e é esse proxy que estará exposto, é um nginx.

[02:30] O Prometheus vai consumir a aplicação por dentro, então, ele não vai passar pelo proxy, mas os clientes vão passar. O que mudou nos serviços? O serviço do Redis está aqui, não mudou basicamente nada, exceto a rede. A network dele agora é a cache. Ele continua somente com o expose: 6379.

[02:53] Na verdade, isso mudou. Antes, ele estava fazendo bind de portas, estava com o ports, agora é o expose, ele só expõe a porta do contêiner, a 6379, e isso somente de forma interna, só dentro do Docker.

[03:10] Aqui, temos o mysql-forum-api que também teve mudanças, agora ele não tem mais ports, só tem o expose, então a porta 3306 do MySQL só é visível dentro do Docker e para quem tem acesso à rede database.

[03:29] Está na rede database e continua com a mesma dependência, que o redis, o redis tem que subir primeiro. Agora eu tenho a aplicação que vai ser "buildada", é a nossa app-forum-api.

[03:44] Ele vai fazer um build, cujo contexto é o diretório app. Ele vai encontrar um dockerfile dentro do diretório app e vai fazer o build, gerando a imagem app-forum-api.

[04:00] O nome do contêiner vai ser app-forum-api. O que mais tem aqui? A aplicação está em comunicação direta com três redes: a rede api, a rede database e a rede cache. Ela precisa falar com o MySQL e precisa falar com o redis.

[04:19] Quais são as dependências desse contêiner? O MySQL, simples assim, ele precisa do MySQL para poder consultar a base de dados; e o MySQL, por sua vez, depende do Redis.

[04:34] Então, tem uma cadeia de dependências que precisa ser cumprida, e ele tem o healthcheck. Nós passamos um curl, que vai internamente, com o contêiner app-forum-api:8080/, lá no actuator/health.

[04:47] Se ele retornar 200, está certo. Aqui está o timeout e as tentativas. Aqui está o contêiner do proxy, que é um nginx, ele tem diversas rotas que eu vou mostrar para vocês de forma rápida.

[05:05] Basicamente, esse vai fazer o bind de porta na porta 80 da sua máquina. A partir desse momento, as requisições vão ser para localhost, quando quisermos acessar a aplicação de forma externa. Ele pertence à rede proxy e api e depende do app-forum-api para subir.

[05:12] Por último, temos o contêiner do Prometheus. O nome será prometheus-forum-api; a imagem é o prom/prometheus:latest, então é a última versão, a mais atual; o nome do contêiner é prometheus-forum-api; e aqui os volumes do Prometheus.

[05:45] Você vai notar que, nesse pacote do Capítulo 2, existem algumas mudanças, ele é diferente do pacote do Capítulo 1, ele tem uma pasta chamada prometheus com um subdiretório prometheus_data com o arquivo Prometheus.yml.

[06:04] Assim como ele tem um diretório nginx que também tem um arquivo chamado nginx.conf e proxy.conf. Aqui, estão as configurações do Prometheus para sua subida. Tenho um arquivo de configuração que, se você notar, vai ser retirado do diretório prometheus/prometheus.yml e vai ser colocado no local em que estamos alimentando o config.file como parâmetro.

[06:32] Esse arquivo tem as configurações do Prometheus. Aqui, temos o path do TSDB, do storage dele, que também é um volume que vai sair da nossa máquina.

[06:45] Temos aqui as bibliotecas que o Prometheus vai utilizar; a porta que ele vai fazer bind, a 9090; e a rede que o Prometheus faz parte que, nesse caso, é a monit. Ele vai estar na monit. Vou, inclusive, fazer um adendo – esse adendo já vai estar no seu arquivo, não se preocupe.

[07:12] Que é a rede api para ele poder falar com a API, a rede api interna. Está feita a configuração, vou salvar, esse arquivo já vai estar assim para você. Agora podemos dar um docker-compose up -d, vou subir em modo daemon para não ocupar a tela.

[07:41] Vai demorar um pouco porque ele vai ter que descarregar a imagem do Spring e "buildar" o contêiner, conforme a configuração do Docker Compose, descarregar as outras imagens, subir todas elas e recriar tanto o contêiner do Redis e do MySQL quanto da mudança de rede.

[08:06] Está aí, já criou a stack, aparentemente subiu, vamos validar isso para garantir que está tudo certo. Agora, qual é a mudança? Vou em “http://localhost/topicos”, vamos ver se ele trouxe os tópicos para mim.

[08:28] O proxy está funcionando, está aqui, “/topicos”. Eu posso agora ir em “http://localhost/topicos/2”, trouxe para mim, a aplicação está funcionando. Qual é a mudança aqui? Quando quisermos acessar o endpoint de métricas, vamos acessar o “http://localhost/metrics”.

[08:55] Aqui, nós caímos nas métricas do Prometheus. Se quisermos acessar outros endpoints que são expostos pela aplicação através do Actuator, podemos também acessar o “http://localhost/health”, que vai estar aqui e o “http://localhost/info”, que também está disponível para nós.

[09:18] Onde que está essa configuração? Vou minimizar o browser e vou te mostrar rapidamente. Se dermos um ls, você vai ver que eu tenho o nginx, e aqui eu tenho o nginx.conf, que tem a configuração de redirecionamento.

[09:45] Não precisa se preocupar com isso, é só para você saber onde está. Existe essa configuração de proxy_pass que faz o redirecionamento das requisições.

[09:59] A configuração de proxy não tem nada de interessante para você, pelo menos nesse momento, isso não deve fazer muito sentido, mas é legal você entender como funciona essa configuração.

[10:12] Você vai notar que o proxy só serve para acessar a aplicação e os endpoints da aplicação de forma externa. Ele não acessa o Prometheus. O Prometheus em si está agora em “http://localhost:9090”.

[10:35] O Prometheus entrou no modo escuro, deixa eu mudar e diminuir o zoom, porque está meio estranho. Vamos falar sobre o Prometheus agora já na próxima aula.

[10:50] Na próxima aula, começamos a trabalhar diretamente com o Prometheus, fazendo uma navegação, entendendo a interface dos Prometheus e os recursos que ela vai nos entregar.

[11:05] É isso, nos vemos na próxima aula.


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Subindo a stack com API e Prometheus