
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula 02 - Configurando o ambiente"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 - Configurando o ambiente

Transcrição
[00:00] Seja bem-vindo à primeira aula do capítulo 1. Nessa aula, vamos configurar o ambiente e eu também vou explicar algumas peculiaridades desse ambiente para vocês.

[00:16] Vou começar importando o projeto. Estou com o Eclipse aberto, ele é uma dependência para que você siga o capítulo 1 sem nenhum problema, mas isso não significa que você não possa utilizar outra IDE. Pode utilizar outra IDE, eu estou usando o Eclipse simplesmente porque ele tem a funcionalidade de trazer as dependências que eu preciso para essa aplicação, como o Maven.

[00:41] Se você está fazendo isso em outra IDE, pode seguir com ela, mas eu recomendo que você utilize o Eclipse. No painel à esquerda, vou em “Import Projects...” e vou procurar por um projeto Maven já existente.

[00:56] Vou em “Next > Browse...”. Em “Browse”, vou encontrar o conteúdo descompactado do pacote (prometheus-grafana) que está disponível na plataforma – que você já deve ter feito download e descompactado.

[01:12] Por conveniência, estou usando o path do workdir do Eclipse. Você não é obrigado a fazer isso, pode utilizar outro lugar, não vai gerar problema, mas estou fazendo dessa forma.

[01:28] Dentro do prometheus-grafana, você vai no subdiretório app, em que existe um arquivo com o XML que o Eclipse vai reconhecer automaticamente como sendo o arquivo de um projeto.

[01:43] Dar “Open”, está aqui, vou dar “Finish” e ele vai importar o projeto para mim. Essa é a nossa API, a “forum”. Então, o primeiro passo foi feito com êxito, agora vamos para o segundo passo.

[02:58] Vou abrir o meu Terminal, estou usando o Linux. Isso é agnóstico, você pode usar em qualquer sistema operacional. As dependências, você vai cumprir e instalar em qualquer sistema operacional, seja Windows, Linux ou MacOS.

[02:15] Vou entrar onde a aplicação está descompactada, onde está toda a stack. Aqui, você vai encontrar o diretório mysql, o docker-compose e o app. Esse grafana e o nginx você não terá eles nesse momento, não se preocupe com isso, é só lixo residual que ficou nesse pacote que estou utilizando.

[02:43] O que vai está na plataforma e você baixou não tem esse conteúdo, não se preocupe com isso. Vamos começar olhando para o conteúdo que está dentro de mysql. É um script SQL e esse script vai ser executado quando o contêiner do MySQL subir para popular a base.

[03:09] Basicamente, essa é a funcionalidade dele. Agora vamos dar uma olhada no docker-compose. É esse cara que vai subir a stack que vamos utilizar.

[03:21] Eu tenho dois serviços aqui, o redis-forum-api e o mysql-forum-api. Esses dois contêineres vão fazer bind de porta com a sua máquina. Se você tiver uma instância local rodando MySQL, vai gerar conflito.

[03:41] Se você tiver o serviço MySQL configurado, derruba ele. Se for qualquer serviço ocupando a porta 3306 TCP, você deve derrubar esse serviço para que esse bind ocorra sem gerar nenhum problema.

[03:57] A mesma coisa para o Redis, que vai rodar na 6379, a porta padrão do Redis fazendo bind para a 6379 da sua máquina. Ele vai estar em uma rede local do Docker Compose, em uma rede do Docker, mas ele vai "bindar" na sua porta.

[04:15] Aqui tem as configurações do MySQL e a dependência dele. Ele precisa que o Redis suba primeiro para que ele possa subir depois. Essas são as dependências básicas de comunicação dessa API, ela depende disso, por isso você tem essa stack no capitulo 1 disponibilizada para você.

[04:41] Só um detalhe, vamos fazer uma equivalência aqui. Antes de subir a stack, é importante que você tenha o Docker instalado. Vou rodar um docker --version só para fazermos uma equivalência de versão.

[04:58] "Docker version 20.10.7, built 20.10.7-0ubuntu5-18.04.04.3" - essa é a versão do Docker que estou utilizando e também tem a versão do docker-compose --version: "docker-compose version 1.29.2, built 5becea4c". É importante que você tenha uma versão igual ou superior à minha. Se for muito superior, se tiver mudado alguma coisa, você pode encontrar algum conflito, mas aí você pode comunicar o fórum ou pode fazer downgrade da versão e resolver o problema.

[05:23] Sem crise, não é nada grave, bem simples de resolver e difícil de acontecer essa quebra de compatibilidade. Temos que ter o Java, vou dar um java -version só para verificarmos.

[05:39] A versão 1.8, é disso que você precisa. Tendo feito isso, eu posso subir a stack. docker-compose. Eu não vou subir em daemon agora, eu vou subir prendendo o meu terminal para que você possa acompanhar a subida.

[06:03] Para você, isso vai demorar um pouco mais. Tive um erro aqui, esse erro é até previsível, eu não subi o serviço do Docker. Sem o serviço do Docker, logicamente, o Docker Compose não vai conseguir subir os contêineres.

[06:20] Então, vamos lá, sudo systemctl start docker docker.socket. No Windows, o processo é outro, ele tem, inclusive, um cliente de interface gráfica que facilita a sua vida. Você vai lá e dá um start no serviço.

[06:47] Vou entrar com a minha senha e agora é só aguardar o serviço subir para subir a stack. Vamos aguardar um pouco, às vezes demora mesmo. Vou dar uma olhada no status do serviço, com sudo systemctl status docker docker.socket.

[07:09] Verificar se subiu com êxito. Preciso esperar um pouco. Está aí, “active running”, subiu tudo e agora posso dar o meu docker-compose up. Ele vai criar as redes, vai criar os contêineres.

[07:30] No caso de vocês, o processo de criação dos contêineres vai demorar um pouco mais porque o Docker vai fazer download do Redis na última versão e do MySQL 5.7, das imagens base para poder rodar os contêineres.

[07:48] Está aqui, ele está subindo ainda, não concluiu a subida, é só esperar um pouco. Com isso, temos a base necessária para poder rodar a nossa aplicação.

[08:05] Está terminando aqui. Só esperar, subiu, deu até o versionamento aqui e ele está esperando por conexões. Eu vou abrir o meu Terminal e vou entrar em app/, com cd app/. Em app/, vou rodar o comando mvn clean package.

[08:43] É o Maven, estou chamando o Maven para fazer um build da aplicação e verificar se está tudo certo com esse pacote que eu baixei. Você pode fazer isso lá pelo Eclipse também, ele tem essa funcionalidade, mas eu prefiro fazer aqui no Terminal.

[09:05] Ele vai executar uma série de testes da aplicação e, se tudo passar, se estiver tudo certo, ele vai concluir esse build e vai gerar um artefato JAR. Passou, ele rodou quatro testes, nenhuma falha, nenhum erro, passou tudo, "buildou" com sucesso.

[09:22] Tendo feito isso, vou no Eclipse e tenho um script aqui chamado start.sh. Basicamente, é só executar esse script para que a aplicação possa subir. Esse capítulo 1 vai ser bem básico porque vamos mexer no código da aplicação, pouca coisa, mas vamos mexer.

[09:49] Para não termos que ficar "rebuildando" o contêiner toda hora, a aplicação vai estar assim. Quando chegarmos no status final da aplicação com ela instrumentada, com o application properties devidamente configurado, com tudo certo, ela vai virar um contêiner e vai subir diretamente via Docker Compose.

[10:09] Aí você não vai ter esse trabalho. Eu subi a aplicação, ela está aqui. Agora, vou no Firefox e vou em “localhost:8080/topicos”. Está aqui, está rodando, essa é a nossa API.

[10:34] Ela responde por “tópicos” e também aceita um ID específico em “tópicos”. Lá na base, aquele script SQL populou três IDs lá dentro. Tenho “1”, “2” e “3”, são endpoints.

[10:52] Para cada inserção que ocorrer de tópico dessa aplicação, vai acabar gerando um endpoint com um número daquele ID que você consegue requisitar.

[11:03] Então, está aqui, configuramos a stack, já tem tudo para partirmos para a próxima aula. Aí entra o nosso problema. Se olharmos para essa aplicação hoje, ela não está instrumentada, ela não tem observabilidade. Então, eu não sei como está o funcionamento dela de fato.

[11:27] Ela está respondendo, mas quanto ela está consumindo de CPU? Qual o tempo de resposta que os meus clientes estão tendo? Quanto ela está usando de memória? Como está a conexão com a base de dados? Qual a duração de uma requisição? Estou tendo algum erro? Tem algum cliente com erro?

[11:47] Tudo isso nós não sabemos, nós estamos cegos. Então, vamos fazer a primeira parte da camada de observabilidade para sairmos do escuro e entendermos o funcionamento da aplicação na próxima aula, iniciando pelo Actuator.

[12:02] É isso, até a próxima aula.






# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 - Configurando o ambiente

<https://linuxize.com/post/how-to-install-apache-maven-on-debian-10/>

sudo apt update
sudo apt install maven
mvn -version

fernando@debian10x64:~/cursos$ mvn -version
Apache Maven 3.6.0
Maven home: /usr/share/maven
Java version: 11.0.16, vendor: Debian, runtime: /usr/lib/jvm/java-11-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.19.0-17-amd64", arch: "amd64", family: "unix"
fernando@debian10x64:~/cursos$
