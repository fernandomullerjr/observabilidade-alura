


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 002 - Aula 01 Métricas da JVM"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 01 Métricas da JVM




Transcrição

[00:00] Sejam bem-vindos a mais uma aula do nosso curso. Nessa aula, eu vou falar especificamente sobre as métricas da JVM. Essas métricas estão sendo externalizadas através do Micrometer e o endpoint em que vamos consumir essas métricas ficam em “localhost:8080/actuator/prometheus”.

[00:27] Isso é só por enquanto. Lá na frente, vamos acessar isso através de outro endpoint porque vamos passar por um proxy. O Prometheus não vai passar pelo proxy, ele vai de forma interna, mas quando formos visualizar, a visualização será outra.

[00:43] No momento, vamos manter assim. Vamos dar uma olhada para entender quais informações nós conseguimos coletar. Vou dar um zoom para ficar um pouco mais legível para vocês.

[00:58] Temos aqui a parte de utilização de memória com essa métrica jvm_memory_used_bytes. É muito legal porque você consegue verificar como está a alocação de memória heap – o que está em área heap e o que está em área nonheap.

[01:17] Além disso, não vou falar de todas as métricas, vou falar só das mais importantes para nós e de outras que podem ser interessantes para você, mas não de todas.

[01:30] Aqui, temos uma métrica logback_events_total. Essa métrica, basicamente, está relacionada àquele tipo de evento registrado no log. Você consegue verificar info, trace, warn, error e debug. Aqui, você pode notar que temos 28 incidências de info, temos 2 warns, que estão aparecendo no log, então é uma métrica importante.

[01:59] Além disso, temos aqui o hikaricp – a minha pronúncia provavelmente não é correta, mas vou usar esse nome mesmo – que é o monitoramento das conexões da aplicação com a base de dados.

[02:16] Se olharmos aqui, essa métrica são as conexões ativas que vamos encontrar. No momento, não estamos com nenhuma sendo exibida. Vou até dar uma consultada. Sempre que você atualizar, a métrica vai mudar o valor se a sua aplicação estiver sendo consumida.

[02:42] Esse endpoint não é dinâmico, você tem que atualizá-lo, você tem que consultá-lo. Você faz uma consulta e ele fica com o resultado estático para você, você tem que consultá-lo novamente para ter uma atualização.

[02:58] Quem cuida dessa função é o Prometheus porque ele tem um tempo de consulta, que chamamos de scrape time. Ele acaba fazendo esse papel para nós, mas, aqui, você tem que fazer na mão se você quiser ver uma atualização de métrica.

[03:16] Olhando aqui, temos a hikaricp_connections, o número mínimo, essa métrica está relacionada ao número mínimo de conexões, que é o pool de conexões que é aberto quando a aplicação sobe.

[03:28] E aqui é o valor 10, é o default. Mais métricas relacionadas à conexão. Aqui são os segundos utilizados para fazer a conexão. Descendo um pouco, o pool de conexões e ter uma informação importante. Lembra daquela tag que nós colocamos lá no application properties? Ela está refletida aqui.

[03:56] Temos aqui o application=”app-forum-api”. Andando um pouco mais sobre essas métricas, está aqui process_files_open_files, 66; descendo um pouco mais, uma métrica bem legal para você entender como o garbage collector está fazendo o trabalho dele, o tempo de uma execução entre outra do garbage collector.

[04:25] Além disso, temos aqui process_cpu_usage, temos a utilização de CPU por conta do processo que está em execução pela aplicação. Andando um pouco mais, temos mais uma métrica relacionada à memória. Aqui, no caso, é a alocação nas áreas de memória pela JVM.

[04:52] Descendo um pouco mais, temos o tempo que demorou para que a conexão fosse iniciada com a base de dados; temos também métricas relacionadas ao estado das threads – isso é bem legal, você consegue ver quais estão em execução, quais estão em espera, quais foram terminadas, quais estão em time-waiting, blocked e new.

[05:22] São informações bem legais e vamos conseguir lá na frente, no Grafana, por exemplo, pegar um dashboard já feito que vai trazer toda uma visibilidade interessante sobre as métricas da JVM.

[05:37] Mesmo que não abordemos todas essas métricas porque sairíamos do foco da nossa proposta, nós conseguimos ter um dash já pronto que vai olhar para todas essas métricas.

[05:52] Agora vou diminuir um pouco para ficar um pouco mais simples para explicar. Lembra da métrica de SLA que nós colocamos – que, na verdade, nós configuramos via application properties? Na verdade, o que nós configuramos foi a exibição da métrica.

[06:10] Você vai notar que é o http_server_requests_seconds_bucket. Existe um bucket que vai alocar esses valores para uma determinada contagem. Temos aqui de 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além. Passou de 500 milissegundos, temos um problema; de 1 segundo em diante a coisa está bem crítica em termos de resposta para a nossa API.

[06:45] Está aqui uma métrica que é importantíssima para entendermos como está o desempenho da nossa aplicação e a experiência do usuário final ao consumi-la, porque você não vai querer que a sua API demore mais de um segundo para responder.

[07:02] Na verdade, vamos estabelecer valores bem interessantes. De 300 milissegundos para cima, já um caso a se preocupar. Isso vai acontecer para todos os endpoints, por isso que eu tenho aqui 403, então alguma consulta já bateu 403.

[07:30] Temos como status 200 do actuator-prometheus. Temos para o /tópicos o status 200 também. Se, porventura, algo ficar indisponível e tivermos um 500 aqui, vai "plotar" essa métrica, ela vai ser exibida relacionada ao status de erro 500.

[07:51] Agora, só para vocês entenderem como isso é legal, vou fazer uma consulta, vou colocar um ID de tópico. Vamos olhar o que acontece agora quando eu atualizo.

[08:05] Notem que, de imediato, vai aparecer uma métrica topicos com id. É uma coisa que é dinâmica a criação da métrica por conta da externalização dessas informações via Actuator e a utilização da interface do Micrometer.

[08:29] Descendo aqui, saindo um pouco desse escopo, temos as classes da JVM, aqui o timeout de conexão com o database, uma métrica bem importante para entendermos, no momento de um incidente, se a nossa aplicação não está se comunicando com a base de dados e se isso foi o causador do nosso problema.

[08:55] O tempo de criação de uma conexão, esse é o tempo máximo, no caso. Aqui em cima tem a métrica que não é do tempo máximo, é do tempo normal de criação de conexão. Descendo um pouco, deixa eu encontrar outra métrica interessante.

[09:16] Utilização de CPU, está aqui a utilização de CPU. Aqui, descendo um pouco mais, jdbc_connections. Deixa eu ver se acho uma de CPU. Olha aqui, tem a de CPU, tem a de processo. Queria encontrar outra de CPU que é a contagem.

[09:42] Essa daqui, system_cpu_count, não vai ser tão interessante quando nós estivermos rodando a nossa aplicação em um modelo distribuído porque, basicamente, ela traz a contagem de núcleos de CPU, mas está valendo, é uma métrica também.

[09:56] Eu não vou me alongar muito nesse assunto, a documentação dessas métricas está naqueles links que eu passei nas aulas anteriores que contemplam o Actuator e o Prometheus.

[10:11] Mas é interessante que saibamos a riqueza das informações que são exibidas aqui para que possamos realmente observar o que é importante para o nosso caso.

[10:22] Pode ser que você tenha um caso diferente desse que nós vamos enfrentar nessa aplicação, então pode ser interessante para você olhar para outras métricas no futuro e você já sabe onde encontrá-las e como aprofundar no que realmente são essas métricas através da documentação.

[10:42] É isso, nos vemos na próxima aula em que vamos começar a trabalhar um assunto que é muito importante e que não existe nessas métricas. Nós não temos nenhuma métrica personalizada, nenhuma métrica que contemple uma regra de negócio, e essas métricas são de extrema importância para qualquer aplicação.

[11:05] Então, vamos entender como é o processo de instrumentação e de utilização de uma métrica personalizada. Nos vemos na próxima aula.
