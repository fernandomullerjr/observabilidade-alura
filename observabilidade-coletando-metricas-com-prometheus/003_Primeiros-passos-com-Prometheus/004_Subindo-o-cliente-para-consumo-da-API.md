

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 003 - Aula 04 Subindo o cliente para consumo da API"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Subindo o cliente para consumo da API

# Transcrição
[00:00] Vamos dar sequência no nosso curso. Essa seria a aula de anatomia de uma métrica, porém, temos um pequeno problema. Nós já temos a nossa API, nós possuímos o nosso database, o nosso cache e o nosso proxy, porém, não temos um client, um consumidor dessa API.

[00:25] E isso nos leva a um problema porque não temos as métricas sendo alimentadas através desse consumo. Nós poderíamos utilizar algum recurso, como a extensão de um browser, para fazer requisições de tempos em tempos, ou seja, atualizações daquela página em que estamos consumindo o endpoint.

[00:47] Ou poderíamos continuar utilizando o Postman, só que essas duas soluções trariam uma carga de processamento que não é interessante, nesse momento, para a nossa infraestrutura que suporta todo o curso.

[01:02] Então, a solução é a seguinte: vamos rodar um contêiner bem simples e esse contêiner vai consumir a nossa aplicação em todos os endpoints que ela possui e, inclusive, gerar alguns erros de tempo em tempo para que tenhamos todas as métricas devidamente alimentadas.

[01:23] Para fazer isso, eu quero que você acesse a pasta em que está toda a configuração – no meu caso, está no workdir do Eclipse, eu estou no Linux, e aqui tem dentro tem um diretório chamado client.

[01:43] Eu chamo de “diretório”, se você está no Windows, você está mais habituado a ouvir “pasta”, mas já faz esse link, quando eu chamar diretório, é pasta.

[01:53] Vamos entrar no client, nesse diretório, e aqui dentro eu tenho um Dockerfile e um script Shell, um script em linguagem de Terminal do Linux. Não se preocupa porque o contêiner é Linux, então é esse script mesmo que tem que rodar, sem nenhuma modificação.

[02:13] Vamos conhecer esse Dockerfile para que você possa entender o que vamos fazer. Nesse Dockerfile, vamos gerar um contêiner derivado da imagem do debian, da última versão da imagem, por isso que não estou com tagueamento de versão para ele.

[02:29] O usuário que vai executar vai ser o root. Eu não estou preocupado com segurança nesse momento, então vai ser o root mesmo que vai executar. E o que eu vou fazer aqui? Vou fazer uma cópia daquele script client.sh para dentro desse path /scripts/client.sh.

[02:47] Depois disso, eu vou rodar, através do RUN, um contêiner, que é o apt update que vai atualizar a base índice de pacotes do Debian. O Linux funciona por pacotes e sempre vai haver um software que faz a gestão de pacotes.

[03:06] No caso do Debian, em alto nível, é o apt – Debian e derivados, como o Ubuntu. Vamos rodar o apt update que vai sincronizar o que eu tenho de referência de pacotes com o que existe na internet, deixar isso atualizado e depois eu vou instalar um software chamado curl.

[03:25] O curl vai fazer o papel do browser, é ele que vai fazer a requisição para a nossa API. Ele é o client que nós vamos usar. E depois disso vou dar um comando chamado chmod (change mode) que altera as permissões desse script, delegando para ele o bit de execução, então ele vai virar um executável.

[03:45] Aqui tem o entrypoint do contêiner que é a chamada que é executada assim que o contêiner sobe, o objetivo de vida desse contêiner vai estar no entrypoint eu é a execução desse script client.sh.

[04:02] É bem simples essa configuração. Está aqui o Dockerfile e vamos dar uma olhada no script, no client.sh. Esse script tem uma variável chamada host, essa variável recebe o nome do contêiner proxy, o proxy-forum-api.

[04:26] A configuração dessa variável é o nome do contêiner proxy e ele tem um laço infinito, um while. Esse while é infinito porque ele está avaliando a condição de true, e true é um dado booleano que sempre vai ser verdade. Então, isso nunca vai parar de ser executado, é um laço de repetição infinito.

[04:45] Aqui, eu declaro três variáveis. A primeira variável vai conter um número aleatório de 1 a 3; a cada execução do while, a cada iteração, essa variável vai ser alimentada com uma configuração de um valor inteiro variando de 1 a 3.

[05:04] A segunda variável vai fazer a mesma coisa, mas vai ter um valor de 1 a 100 a cada iteração. E a terceira variável, chamada temp, também vai receber um valor aleatório, porém, um valor menor que 1, então ela vai receber um float que está entre 0 e 1.

[05:28] Para que isso? Para que tenhamos, primeiro, um tempo a cada iteração que vai ser inferior a 1 segundo; depois disso, para que possamos atingir um dos endpoints que já está configurado, que é o ID de um tópico que sempre vai variar de 1 a 3.

[05:50] A cada iteração desse laço, eu vou atingir um endpoint, o endpoint /topicos, em um ID específico que vai variar de 1 a 3 a cada execução que eu entrar, na condição específica de fazer uma requisição para ele.

[06:08] Quanto ao outro número, é ele que segue com a lógica de negócio principal desse script simples. Basicamente, o que eu faço aqui é uma estrutura condicional de if/else. Esse if vai avaliar se a variável numb, que recebe um valor de 1 a 100, é menor ou igual a 50.

[06:33] Se ela for menor ou igual a 50, ele vai fazer uma requisição para /topicos, ele vai atingir o proxy-forum-api em /topicos; se for maior ou igual a 51 e menor ou igual que 80, vamos mandar uma requisição para /topicos em um ID que vai estar variando de 1 a 3 a cada iteração.

[07:05] Caso o numb esteja maior ou igual a 81 e menor ou igual que 90, fazemos uma autenticação com sucesso. Agora, se numb for maior ou igual que 91 e menor ou igual que 95, nós falhamos uma autenticação; se esse valor estiver acima de 95, ou seja, se for de 96 até 100, nós enviamos uma requisição para um ID que não existe e isso gera um erro também.

[07:42] Então, a condição mais simples de ser atingida é uma requisição para /topicos. A segunda condição mais fácil de ser atingida é a que envia uma requisição para um ID que vai variar de 1 a 3 em /topicos.

[08:00] A nossa terceira condição, que é um pouco mais difícil de ser alcançada, é uma autenticação com sucesso. Depois, os erros têm um percentual bem pequeno de chance de ocorrer e eles vão estar alimentando métricas relacionadas à falha de autenticação e as chamadas a endpoints que não existem, que não vão ser encontrados.

[08:30] Essa configuração desse script, eu não vou me alongar na explicação dele, mas, basicamente, se você ficar interessado por isso, você pode dar uma olhada no curso de Linux, entender um pouco mais. Aqui na Alura, temos um conteúdo muito bom sobre Linux, e com certeza isso vai agregar bastante para você.

[08:50] Para você entender o que é o random, é um pseudodispositivo, e toda essa lógica que foi feita nesse script bem simples. Vou fechar aqui. Basicamente, esse Dockerfile vai pegar esse script, vai configurá-lo, com permissões de execução, vai sanar as dependências instalando o curl, e vai chamar esse script no entrypoint de execução.

[09:17] Saindo desse escopo, temos o nosso Dockerfile. Abrindo esse Dockerfile, você vai notar que o seu está com o último bloco comentado. Vamos descomentar esse bloco e eu vou te explicar o que esse bloco está fazendo.

[09:38] Esse server que vai ser gerado no Docker Compose se chama client-forum-api, ele vai fazer um build. O contexto desse build é no diretório client, e lá ele vai encontrar um Dockerfile. Você já sabe o que o Dockerfile faz e o que o script faz também.

[10:00] Ele vai gerar uma imagem chamada client-forum-api e um contêiner derivado dessa imagem chamado client-forum-api também. Se esse contêiner for derrubado, ele não vai subir.

[10:15] Todos os contêineres estão com essa configuração porque, em dado momento, vamos realmente derrubar alguma coisa para verificar como que as métricas vão ser alimentadas e, principalmente, para testar os alertas que serão feitos mais para frente.

[10:31] Ele está dentro da rede proxy e qual é a dependência dele? O prometheus-forum-api, o contêiner do Prometheus. Por que ele depende do Prometheus? Porque temos uma cadeia de dependências que permite somente que um contêiner suba após o sucesso do outro.

[10:48] Isso começa desde o primeiro service que possuímos aqui, o redis. O redis não depende de ninguém, ele tem que subir; após o redis, tem o mysql, que depende do redis; depois, tem o redis-forum-api, que depende do mysql; depois, o proxy, que depende do app; e o prometheus, que depende do proxy; e, por último, o client, que depende do prometheus.

[11:16] Por hora, essa configuração vai nos auxiliar, porque esse script vai ser executado nesse contêiner e ele, logicamente, vai fazer muitas requisições para a nossa aplicação sem trazer uma carga de CPU ou de consumo de memória notável para a sua infraestrutura, para a sua máquina pessoal, que é o host que está rodando essa stack.

[11:44] Vamos rodar o comando docker-compose up -d para subir, modo daemon. Vai demorar um pouco porque ele vai agora "buildar" a imagem, mas não é algo muito demorado, é rápido.

[12:05] Ele vai fazer o update da base índice de pacotes; vai instalar o software que vamos utilizar, o curl; depois, vai rodar as permissões e vai subir a stack. Não é nada muito complexo, já está quase chegando no fim, está configurando a parte do curl de certificados.

[12:27] Agora pronto, já começou a criar a stack novamente e vai subir todo mundo. Depois disso, já vamos ter um client consumindo a nossa aplicação e, por fim, vamos ter dados relevantes nas nossas métricas, vamos ter, enfim, insumos para podermos identificar melhor a funcionalidade de cada métrica.

[12:55] Já subiu, já está tudo certo, vamos para a próxima aula, e aí sim vamos falar sobre a anatomia de uma métrica. Te vejo na próxima aula.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Subindo o cliente para consumo da API