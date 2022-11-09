
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula 04 Expondo métricas para o Prometheus"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Expondo métricas para o Prometheus




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Transcrição

[00:00] Vamos dar prosseguimento nas nossas aulas. Na última aula, nós fizemos a configuração do Actuator e conseguimos externalizar as métricas da JVM.

[00:14] Essas métricas ajudam bastante, mas não são o que nós realmente queremos, nós precisamos que as métricas estejam legíveis para o Prometheus. Para conseguirmos fazer isso, temos que trabalhar com o Micrometer.

[00:33] Basicamente, ele vai ser a nossa fachada de métricas, levando as métricas em um formato legível para o Prometheus.

[00:41] Para fazer isso, é bem simples, basta você dar uma pesquisada em “Micrometer”. Para mim, veio como primeiro resultado no Google. Entrando no site, vou em “Documentação > Instalação”. Na “Instalação”, eu tenho o formato para o Gradle – não é o que nos atende, eu vou utilizar o bloco para o Maven.

[01:06] Você vai copiar esse bloco e, de posse desse conteúdo copiado, vamos no pom.xml adicionar essa dependência. Está aqui, pronto, está copiado. Basta salvar e agora vem uma alteração no application.prod.properties.

[01:33] O que vai ser necessário? Colocar o endpoint Prometheus, que nós não colocamos na última aula. Logo após metrics, vai colocar uma vírgula e vai colocar aqui prometheus: management.endpoints.web.exposure.include=health,info,metrics,prometheus.

[01:50] Vamos expor o endpoint prometheus e, além disso, precisamos de uma configuração adicional. Vou colar aqui essa configuração, vocês vão ter esse arquivo já feito, então não precisam se preocupar em digitar tudo isso, mas é muito importante que vocês entendam cada uma dessas linhas.

[02:15] Primeiro, vamos habilitar as métricas da JVM, tem aqui management.metrics.enable.jvm=true; tem aqui também o management.metrics.export.prometheus.enabled=true. Estamos habilitando o export das métricas para o Prometheus.

[02:34] Temos aqui uma métrica específica relacionada à SLA, que não vou explicar agora, porque ela terá uma aula só para ela, mas já vamos deixar configurado para não termos que voltar nesse arquivo no futuro.

[02:51] Aqui também tenho uma configuração que é de tagueamento. Quando externalizarmos uma métrica, essa métrica vai ser exibida com um tagueamento. Nesse caso, ela vai ter a tag app-forum-api.

[03:16] Por que isso? Quando tivermos N aplicações rodando, algumas métricas vão ser iguais. Por exemplo, a métrica de tempo de resposta pode ter o mesmo nome, mas posso querer olhar mais de uma aplicação, aí a tag entra nesse ponto, porque eu posso selecionar aquela métrica com uma tag específica correspondente à aplicação que eu quero avaliar.

[03:44] Está aqui esse bloco de configurações, você salva aí. Vamos para o Terminal e eu vou fazer o build dessa nova versão. Enquanto ele vai "buildando", podemos dar uma olhada no Actuator. Notem que são esses endpoints que nós possuímos.

[04:05] Não tem nada relacionado ao Prometheus – temos métricas, mas não temos o Prometheus. Vou voltar, esperar terminar o build. Concluiu com sucesso, nenhum erro.

[04:24] Posso voltar aqui, deixa eu só ver se a aplicação está rodando. Está rodando, vou dar um “Ctrl + C”, pronto. Vamos no start.sh e vamos rodar a aplicação.

[04:41] É só deixar a aplicação subir agora. Uma coisa importante, que eu gostaria que vocês fizessem, é dar uma lida na documentação do Actuator. Aqui, no Actuator, você pode ver a parte de “Logging”, que é exposta, e tem o log level, que é exposto.

[05:00] Tem a parte de métricas. Já veio o Micrometer aqui e tem a parte específica do Prometheus. É bem legal. E também dar uma olhada na documentação do Micrometer.

[05:17] É bem importante que você entenda isso para que você possa levar para outras aplicações com o conhecimento de causa interessante. Deixa eu voltar no build, vamos ver a aplicação, se ela subiu.

[05:35] Está aqui, está "startada", vamos voltar para o browser. No browser, vou atualizar o Actuator e agora já tem o endpoint actuator-prometheus. Clicando nele, nós encontramos as métricas no formato esperado pelo Prometheus.

[05:56] Se você notar, tem bastante coisa, é muita métrica, e a maior parte delas vai ser útil para nós na nossa implantação, no acompanhamento do funcionamento da nossa API. Alguma aqui não é útil? Na verdade, nenhuma é inútil, todas elas são úteis.

[06:17] Algumas vamos acabar desprezando quando estamos rodando a nossa aplicação em um contêiner, então não são tão necessárias para nós quanto o número de núcleos de uma CPU ou coisas do tipo.

[06:31] Mas está aqui, conseguimos expor essas métricas em um formato legível para o Prometheus e agora podemos entender o que são essas métricas e ao que cada uma delas corresponde.

[06:46] Pelo menos, as que são interessantes para a nossa aplicação e que vamos utilizar futuramente configurando dashboards e alertas em cima de valores específicos, de momentos específicos dessas métricas.

[07:02] Essa aula se encerra aqui e, na aula que vem, vamos entender as métricas da JVM para que você saiba qual métrica vai ser utilizada para um tipo específico de observabilidade e monitoramento. Até a próxima.