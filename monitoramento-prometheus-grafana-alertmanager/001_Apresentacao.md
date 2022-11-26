
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 001 - Aula  01 Apresentação "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status



# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula  01 Apresentação


# Transcrição

[00:00] Oi, pessoal, tudo bem? Sejam bem-vindos. Esse é o vídeo de apresentação do curso Monitoramento: Prometheus, Grafana e Alertmanager. Eu sou o Kleber Costa, o construtor desse curso.

[00:14] O conteúdo abordado será sobre a aplicabilidade do curso; quais são os requisitos para você fazer o curso; o que são os quatros sinais de ouro; o que são os métodos RED e USE; qual é o cenário do curso e quais são os benefícios de você fazer esse treinamento.

[00:33] Vamos para a aplicabilidade. Esse curso se destina para pessoas desenvolvedoras; pessoas de operação de infraestrutura; pessoas de DevOps e pessoas de engenharia de confiabilidade de sites.

[00:48] Seguindo, vamos entender quais são os requisitos para você fazer esse treinamento sem ter dificuldades. Você precisa ter feito o curso de Observabilidade: coletando métricas de uma aplicação com Prometheus. Por que você precisa ter feito esse curso? Nós estamos dando sequência nas implantações da stack que foi apresentada nesse primeiro curso.

[01:11] E nesse primeiro curso nós fizemos a instrumentação de uma API Spring para que ela pudesse expor as métricas default da JVM e também expor métricas personalizadas.

[01:25] Tudo isso foi feito utilizando o Actuator e, posteriormente, utilizando o Micrometer para fazer a interface dessas métricas e levá-las para um formato legível para o Prometheus.

[01:39] Então, o Prometheus foi levantado nesse curso também e houve uma introdução à linguagem PromQL, trabalhando com sua sintaxe e os tipos de métricas que o Prometheus utiliza, além de lidarmos com questões que estão relacionadas à composição das métricas.

[02:04] Então o pré-requisito é ter esse conhecimento. Se você já possui esse conhecimento, não é necessário fazer o curso. Porém, se você não entendeu algum dos termos que eu utilizei, faça o curso para você poder seguir nesse curso sem nenhum problema, dando continuidade a todas as tarefas que foram feitas no curso anterior sem ter nenhum débito técnico.

[02:31] Quais são também os outros pré-requisitos? Caso você não tenha feito o curso, mas entenda tudo que eu falei até agora, você precisa ter o Docker e o Docker Compose instalados na sua máquina. Esses são os pré-requisitos.

[02:48] Vamos falar um pouco sobre o que será abordado nesse treinamento. Falaremos sobre os Golden Signals, que são os quatro sinais de ouro.

[02:59] Eles foram definidos pelo Google, e basicamente a premissa é a seguinte: se você só puder olhar para quatro componentes, quatro indicadores da sua aplicação, do seu sistema, foque em latência, tráfego, saturação e erros.

[03:20] Entendendo que a latência está relacionada ao tempo de resposta da sua aplicação; o tráfego está relacionado ao número de requisições que a sua aplicação recebe; a saturação está relacionada ao uso excedente dos recursos que suportam a sua aplicação; e os erros falam por si só, é a quantidade de erros que você está tendo no decorrer da execução da sua aplicação, tanto em retorno para o usuário como em iteração com a infraestrutura que suporta o seu sistema.

[04:01] Esses sinais de ouro estão como uma referência no livro SRE do Google. É importantíssimo você entender esse conceito, e ao olhar para um projeto, já planejar a camada de observabilidade e monitoramento de uma forma que esses quatro sinais sejam observados. Isso é de suma importância.

[04:29] Como acabamos entrando num espectro muito abrangente quando falamos desses sinais, existem métodos que vão facilitar a observabilidade e o monitoramento em cima desses aspectos. Vamos falar dessas metodologias a seguir.

[04:46] Existe a metodologia USE, que foca na utilização de recursos, na saturação e nos erros. Ela está muito voltada para uma questão de infraestrutura, então basicamente vamos olhar a utilização de disco, como está a questão de IO, como está basicamente a nossa parte de tráfego mesmo, em termos de pacote.

[05:15] Vamos olhar para a saturação, que é o uso excessivo de recursos. Temos um planejamento para a utilização de recursos.

[05:23] Quando saímos um pouco disso, temos uma camada de saturação ocorrendo. Existe a saturação e os erros gerados com relação a toda essa comunicação de recursos em decorrência do consumo da sua aplicação pelos usuários e da forma como ela está interagindo com a infraestrutura que a suporta.

[05:47] O método USE está mais focado nisso, ele não será o nosso foco nesse treinamento, porque é muito abrangente e deveríamos ter um curso específico só para USE, se fôssemos falar sobre ele.

[06:00] Nós vamos focar no método RED, que foca na taxa de requisições, que é o rate; foca nos erros gerados por essas iterações; e foca também na duração dessas iterações, dessas requisições.

[06:17] Nós não vamos simplesmente só mergulhar no método RED e focar só nesses três aspectos. É importante você entender que esses três aspectos geram muitos desdobramentos e que nós vamos, sim, olhar para alguma coisa do método USE que complemente a utilização do método RED na nossa sequência desse treinamento, entregando realmente uma camada de observabilidade e monitoramento completa.

[06:48] O método RED, como se difere do USE, foca mais em iterações que estão relacionadas ao protocolo HTTP. Então o nosso objetivo é mensurar a experiência do usuário final.

[07:04] Então o nosso foco será esse: entender de uma forma mais próxima possível como está a satisfação de quem consome a nossa API.

[07:20] Vamos dar uma olhada agora para o cenário do curso para entender qual é a nossa missão daqui para frente.

Diagrama da jornada do usuário. À esquerda, temos um ícone de usuário que se conecta ao ícone da internet. Este último, por sua vez, está ligado ao *proxy* ("proxy-forum-api"), que tem acesso à API ("app-forum-api"), no centro do diagrama. A API está interligada com *cache* ("redis-forum-api"), com *database* ("mysql-forum-api") e com "monit", este na parte inferior do diagrama. "Monit" contém três elementos: "grafana-forum-api", "prometheus-forum-api" e "alertmanager-forum-api". Esses três ligam-se à internet (à direita do diagrama), que tem conexão com o Slack, que chega ao time de suporte. Este último tem uma ligação com "grafana-forum-api".

[17:26] Nós já falamos sobre a jornada do nosso cliente, que é um cliente sintético basicamente.

[07:34] É um container que possui um script que consome de uma forma não previsível a nossa API, gerando inclusive algumas interações com erros, o que é interessante em termos da nossa camada de observabilidade, para que ela realmente possa ser colocada à prova.

[07:53] Nós já temos então um proxy reverso, temos a nossa API, temos um cache e nós temos um banco de dados, cada um em seu respectivo container, todos rodando em uma stack do Docker Compose.

[08:07] E nós já temos o Prometheus configurado e sendo utilizado. O que nós faremos nesse curso é configurar o Grafana, configurar diversos painéis, tendo um dashboard bem abrangente com o Grafana, subir o Alertmanager e configurar a integração dele com o Slack.

[08:32] Logicamente, teremos a comunicação do Prometheus também sendo configurada com o Alertmanager. Teremos a camada de visualização gráfica utilizando o Grafana. E teremos o Slack como o endpoint de alertas.

[08:49] Basicamente, o nosso Alertmanager vai se comunicar com o Slack, passando para um canal específico todos os alertas que forem gerados através de eventos críticos no nosso ambiente.

[09:07] Então todo incidente que ocorrer vai gerar um alerta que será enviado para o Slack e visualizado por um time de suporte e também pelos desenvolvedores dessa API.

[09:23] Esse é o cenário do curso, e agora, para finalizar, vamos falar sobre os benefícios que você terá ao concluir esse treinamento em termos de conhecimento.

[09:37] A prática que nós faremos serve para trabalharmos na redução do tempo médio de detecção de incidentes, o MTTD, que é o Mean Time To Detect.

[09:49] Quando temos visibilidade sobre métricas e temos essa camada de observabilidade, nós conseguimos detectar algumas nuances dentro do nosso sistema. Então nós conseguimos pegar o início de degradações que, ao se desenvolverem, causam um incidente.

[10:09] Uma vez que conseguimos ver o início dessa degradação, nós conseguimos já iniciar uma busca pela causa raiz do incidente e resolver o problema antes que uma grande fatia de usuários seja impactada. Então também teremos uma redução do MTTR, que é o tempo médio de reparo, ou Mean Time To Repair.

[10:33] Nesses dois pontos você vai realmente ganhar esse conhecimento, terá essa agregação de conteúdo para levar para o mundo real.

[10:46] Além disso, vamos dar uma pincelada sobre um assunto que está dentro do escopo SRE, mas que tem tudo a ver com métricas. Na verdade é um assunto que é derivado de métricas também, que é o acordo de nível de serviço, SLA.

[11:03] É um documento que faz parte de uma norma ISO e que serve como uma base para que tenhamos um objetivo de nível de serviço. Basicamente, o foco será em manter a qualidade e a estabilidade do nosso serviço acima desse SLA.

[11:24] Então temos um acordo de nível de serviço, que normalmente gera algum tipo de punição caso ele não seja cumprido. O que nós faremos para manter esse SLA? Nós teremos um objetivo de nível de serviço que está acima desse SLA.

[11:40] E como entendemos que estamos dentro desse objetivo de nível de serviço? Temos que ter indicadores, e esses indicadores são considerados como o SLI, os nossos indicadores de nível de serviço, que basicamente serão compostos através de métricas.

[12:02] Dentro desse escopo daremos uma pincelada no Error Budget, que é o orçamento de erro, a margem de erro que nós temos para falhar sem culpa.

[12:13] Mas nós não vamos mergulhar de cabeça nesse assunto, porque o escopo dele é SRE, e falaremos de uma prática da engenharia em confiabilidade de sites, que é a medição, o trabalho com a observabilidade, com o monitoramento através de métricas. Mas esse não é um curso de SRE.

[12:34] Além disso, quais são os benefícios que você agregará em nível de conhecimento depois de entender esses conceitos e trabalhar com métricas? Você entenderá o que é a composição de uma baseline comportamental. Afinal de contas, uma série temporal pode te entregar isso.

[12:52] Você entenderá que as ações de escalabilidade automática e preditiva são derivadas também do uso de métricas. Daí a importância de se trabalhar com métricas.

[13:03] Também ações reativas e proativas. Ação reativa é quando existe uma reação como um evento disparado com base em algo que foi monitorado, observado. E dentro desse escopo entram os acionamentos, o call, e alertas automatizados.

[13:25] Lembrando que estamos falando de ação reativa. Um alerta, uma ação automatizada é uma reação. Quando falamos de escalabilidade preditiva, temos ações proativas. Tudo isso é gerado através do uso de métricas.

[13:47] E o ponto mais forte do curso, que é a medição da experiência do usuário. Se você não tem clientes, não tem usuários satisfeitos com a sua aplicação, ela não serve de nada. Por isso que o nosso objetivo é justamente medir a experiência do usuário o mais próximo possível. Então esse é o nosso objetivo final, e com ele nós vamos rumo à confiabilidade.

[14:15] Esse é o conteúdo que você verá nesse curso, eu espero que tenha te agradado. E espero poder agregar um pouco de conhecimento no seu caminho. Te vejo na próxima aula.