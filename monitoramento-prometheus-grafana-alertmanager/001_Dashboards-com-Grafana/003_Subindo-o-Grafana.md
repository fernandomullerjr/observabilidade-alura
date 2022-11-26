

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 001 - Aula 03 Subindo o Grafana  "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status



# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 03 Subindo o Grafana


# Transcrição

[00:00] Vamos dar sequência ao nosso curso. Na aula de hoje vamos subir o Grafana. Esse processo é bem simples, já vai ter um pacote para você na plataforma com todo o conteúdo feito de subida do Grafana.

[00:21] Vamos fazer algumas configurações após a subida do contêiner e essa você realmente vai precisar executar. Eu aconselho que você utilize o que já está feito para não perder tempo em erros de digitação com relação à sintaxe do YML no Docker Compose.

[00:40] Mas se você quiser digitar e fazer tudo na mão, é muito legal também, pode seguir que vai dar tudo certo.

[00:48] O primeiro ponto, estou no meu workdir, estou no path em que está o meu Docker Compose. Vou criar um diretório chamado grafana. Esse diretório, essa pasta vai servir como um volume para o contêiner, e vai ser onde o Grafana armazena suas informações.

[01:13] Após criar esse diretório, vou aplicar um chmod para mudar as permissões dele para 777, então chmod 777 grafana, acabei digitando sem colocar o Grafana.

[01:33] Quando fazemos isso, damos esse chmod, nós estamos atribuindo permissões muito abertas, muito inseguras para esse diretório. Porém, estamos falando somente do diretório grafana.

[01:48] Se eu olhar dentro do diretório grafana, ele está vazio, não tem nada. Se eu olhar para o diretório em si, vocês vão ver que qualquer um pode executar, escrever e ler o diretório.

[02:08] Por que eu tenho que fazer isso? Quando o contêiner do Grafana subir, ele vai ter um usuário próprio que vai ter que usar esse diretório porque ele vai ser montado como volume dentro do contêiner e, se ele não tiver permissões para entrar nesse diretório e escrever, o contêiner não vai subir.

[02:28] É apenas isso. Tendo feito isso, vamos abrir o nosso docker.compose.yml. Abrindo-o, o que acontece? Nós temos essa configuração, essa é a configuração atual que você tem.

[02:53] Você tem o contêiner do prometheus e você tem o contêiner do client que depende do proxy – o prometheus depende do proxy e o client depende do prometheus. Temos essa dependência intercalada de contêineres.

[03:14] Vamos respeitar isso para o Grafana. Vou pegar a cola que eu já fiz para não gastar tempo digitando tudo isso. Vou pegar esse conteúdo e vou colar entre o conteúdo do prometheus e do client. O bloco de código vai entrar aqui.

[03:52] Para facilitar, vou colocar a numeração aqui. Esse bloco de código eu coloquei da linha 97 até a linha 108. O que ele diz? Que vamos ter um service, que no caso um contêiner chamado grafana-forum-api; ele vai trabalhar com a imagem corrente mais atual do Grafana; o nome do contêiner vai ser grafana-forum-api; ele vai ter como volume esse diretório grafana que eu criei e apliquei aquela permissão; e ele vai ser montado dentro do contêiner /var/lib/grafana.

[04:33] O esquema de restart é igual ao dos outros contêineres. Se derrubarmos esse contêiner, ele não sobe sozinho, isso é proposital. Se tivermos uma falha, não vamos entrar em loop e ficar tentando subir o serviço, e também, em casos em que derrubemos um contêiner para forçar uma situação de erro, ele não vai subir no automático.

[04:54] Aqui está a porta, o Grafana roda na porta 3000 TCP e vai fazer bind de porta para o seu docker host, para a sua máquina. Então, em “localhost:3000”, você vai acessar o Grafana.

[05:09] Ele está na network: monit, que é a mesma do Prometheus, e ele depende do prometheus-forum-api. Essa é a configuração do Docker Compose para subir essa stack.

[05:23] A alteração a mais que eu vou fazer é na dependência do client. O client vai subir após o grafana agora. É bom porque dá um tempo para o client enviar as requisições para a API.

[05:38] O client fala com a API através do proxy, ele envia requisições, só que ele sobe antes que o MySQL esteja devidamente configurado. O que acontece?

[05:52] No esquema de dependências, se olharmos aqui, você vai ver que temos o redis subindo primeiro; o mysql sobe depois do redis e depende do redis para subir; o app depende do mysql, por sua vez, para subir – aqui está a dependência dele, mysql-forum-api.

[06:16] O proxy depende do app para subir, da API; e o prometheus depende do proxy. Agora, o grafana depende do prometheus e o client depende do grafana.

[06:31] Sem essa configuração, do jeito que estava antes, apesar do contêiner do mysql subir, ele ainda não estava com a base configurada e o client já enviava requisições, o que resultava em alguns erros 500 que vocês podem observar nas métricas anteriores que nós verificamos.

[06:52] Inclusive, você vai conseguir verificar nas suas métricas que isso vai acontecer. Aqui, nós coibimos um pouco dessa falha porque o contêiner do client só vai começar a disparar suas requisições depois que o grafana tiver subido e isso dá um tempo a mais para o MySQL se organizar e estar bem das pernas nessa hora.

[07:13] Vou salvar o conteúdo e vou rodar o comando docker-compose up -d para ele subir em modo daemon. Ele vai baixar a imagem do Grafana, vai fazer todo esse processo conhecido, você já viu isso sobre contêineres.

[07:33] Ele vai subir toda a stack naquela ordem de dependências que está no Docker Compose e, por último, ele vai subir o Grafana. É só aguardar um pouco, ele já está na fase de criação, já baixou as layers do Grafana bem rapidamente e agora está subindo a stack.

[07:56] Para validar isso, vou abrir o browser. Enquanto isso, ele está subindo a stack, está criando o prometheus normalmente e agora está indo para o grafana.

[08:26] Meu browser vai aparecer a qualquer momento aqui. Pronto, já está subindo o client, o grafana já está certo e agora ele está criando o client. Vamos esperar acabar para podermos acessar a nossa aplicação e verificar como está a questão da observabilidade.

[08:58] Em primeiro ponto, vou no “localhost/metrics” para que possamos verificar – não subiu, deixa eu dar uma olhada em como está a situação. Vou atualizar, subiu. Deu aquele “Bad Getway” porque estava processando.

[09:29] Agora está certo, era só o contêiner subindo. Aqui, o erro 500 que eu falei que conseguíamos observá-lo. Por que continuamos observando o erro 500? Estamos usando um volume no Prometheus e o TSDB está sendo armazenado, então temos essa referência histórica.

[09:47] Se eu procurar por requisições 200, eu tenho o /actuator/prometheus e agora o meu client já começa a atingir o /auth, /topicos e /topicos/{id} está em algum lugar também – não estou enxergando, deixa eu atualizar mais uma vez porque ele também vai bater em /topicos/{id} daqui a pouco.

[10:20] Já estamos consumindo a aplicação, isso já está sendo refletido nas métricas. Se eu acessar o “localhost:9090”, eu vou cair na interface do Prometheus lá no "Graph”. Aqui, é o Expression Browser, é o navegador de expressão. Vamos agora para o Grafana. O Grafana vai estar em “localhost:3000”, como falei anteriormente.

[10:49] No primeiro acesso ao Grafana, o login é “admin” e senha “admin”, ele vem default com essa configuração. Autentica e, assim que você autenticar, ele vai pedir para você criar um novo password, uma nova senha. Vou colocar “Alura”.

[11:14] Vou salvar e pronto, autenticamos no Grafana. Essa é a interface do Grafana. A primeira coisa que vamos fazer agora que o Grafana subiu é configurar um data source.

[11:28] Isso é rápido, vamos no painel à esquerda, no símbolo da engrenagem, em “Configuration > Data sources”. O que é um data source? É uma origem de dados do Grafana. Ele pode ter diversos data sources, pode ser o Splunk, pode ser o CloudWatch, pode ser o Prometheus etc.

[11:51] Vamos adicionar no “Data sources”, o primeiro da lista já é o Prometheus. Vou selecioná-lo, o “Name” está “Prometheus”, vou colocar que ele está em “http://prometheus-forum-api:9090” TCP.

[12:13] Esse é o endereço porque ele vai falar com o contêiner. Se você colocar “localhost”, o Grafana vai achar que ele é o próprio endpoint de métricas do Prometheus, e não é.

[12:26] Aqui você não mexe em mais nenhuma opção, vem e roda um “Save & test”. Se estiver certo, se você conseguir falar com esse endpoint, ele vai ter um resultado igual ao meu e vai estar configurado.

[12:41] Está certo aqui. Se eu procurar em “Data sources”, vou ver que já tenho o Prometheus configurado. A outra configuração rápida que vamos fazer é, no painel à esquerda, no símbolo do “+”. Vamos entrar em “Folder”.

[12:59] Vamos criar uma pasta e toda a nossa configuração de dashboard vai ficar nessa pasta. O nome da pasta vai ser “forum-api”, vou criar a pasta e posso criar o meu dashboard dentro dessa pasta.

[13:18] Vou em “Create dashboard > Dashboard settings” e no nome do dashboard vou colocar “dash-forum-api”. Não vou colocar descrição, nada disso. Vou dar um “save”, vou salvar com as tags e pronto, está feito.

[13:45] Essa é a subida do Grafana e a configuração mais básica, que é a seleção de um data source e a criação de uma pasta em que o nosso dash será configurado.

[13:57] Na próxima aula, vamos trabalhar com as variáveis que vamos utilizar no Grafana. É isso, te vejo na próxima aula.