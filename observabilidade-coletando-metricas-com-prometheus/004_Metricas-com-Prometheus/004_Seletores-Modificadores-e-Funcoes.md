
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 004 - Aula  04 Seletores, Modificadores e Funções"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status



# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Seletores, Modificadores e Funções

004_Seletores-Modificadores-e-Funcoes.md

# Transcrição

[00:00] Vamos dar continuidade ao nosso curso. Na aula anterior, nós falamos sobre tipos de métricas. Agora, já vamos entrar em outro assunto que está relacionado a seletores, agregadores e funções. Vamos ter uma prévia sobre isso para você entender como você pode manipular uma métrica e obter o resultado que você precisa.

[00:29] Para começarmos a falar desse assunto, vou trabalhar com essa métrica que é o http_server_requests_seconds_count. Eu tenho essa métrica, se eu executar a consulta no Prometheus, ele vai me trazer esse instant vector que tem várias séries temporais, mas vou fazer uma filtragem.

[00:49] Vou colocar o application="app-forum-api", vou colocar qual é o método que vamos olhar, method="GET". Vamos também fazer uma verificação no status="200", requisições que retornaram sucesso. Está de bom tamanho, por hora, vamos executar, http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200"}.

[01:25] Executando essa consulta, o nosso retorno trouxe para nós um endpoint chamado actuator/prometheus; trouxe também o /topicos e o topicos/{id}. O actuator/prometheus não é um endpoint consumido pelos nossos usuários, então ele não nos interessa nessa visualização.

[01:49] O que vamos fazer? Vamos trabalhar com um seletor de negação. Vou colocar na uri tudo que for diferente de actuator/prometheus, http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200",uri!="/actuator/prometheus}. Melhorou um pouco a situação porque tive agora o /topicos e o /topicos{id} retornando para mim a contagem de requisições.

[02:12] Só que existe também uma autenticação que ocorre nessa aplicação e ela é através do método “POST”, então eu preciso mudar o meu seletor para que consiga obter esse resultado.

[02:26] Para fazer isso, é bem simples. Quando vou trabalhar com o "ou" lógico aqui dentro do Prometheus, da linguagem PromQL, eu tenho que mudar o meu seletor e usar esse seletor com acento que, basicamente, é um seletor que suporta uma espécie de regex.

[02:45] Vou colocar method=~"GET|POST" e vou executar. Legal, tenho aqui esse retorno que é o método POST com sucesso, status="200", em /auth.

[03:03] Isso são seletores. Vamos imaginar a seguinte situação, vamos imaginar que você tem uma aplicação, você tem uma API e ela retorna outros códigos além do 200, ela retorna códigos da família do 200 também. Como vamos resolver isso?

[03:23] Vamos trabalhar novamente com o nosso seletor com acento. Ao invés de usarmos 200, vamos usar um coringa, que é o ponto. O ponto é um coringa que equivale a qualquer caractere – como no Unix ou Linux, você utiliza o asterisco.

[03:42] Está aqui, status="2..". Você vê que não mudou nada a consulta está certa. Vamos expandir isso, vamos imaginar que, de repente, a sua API está por detrás de um CDN, tem um redirecionamento, um proxy, então tem também a família 300. Vamos colocar status="2..|3..".

[04:05] Até agora, o que fizemos? Estamos trabalhando com os métodos GET e POST, estamos olhando para status 200 ou 300, estamos olhando para qualquer URI que não seja /actuator/prometheus.

[04:22] Agora vamos imaginar no caso em que você quer simplesmente pegar os erros. Eu preciso saber o que não está dando certo, o que podemos mudar? Vamos no seletor e vamos criar um seletor de negação que suporta o nosso regex: status!~"2..|3..".

[04:44] Vou trabalhar com esse seletor e aqui tenho o retorno dos erros que captei na minha aplicação. Teve erro 500, 404, 400 e mais outro 500 aqui embaixo. Dessa forma, conseguimos trabalhar dentro de uma métrica fazendo uma consulta customizada para pegarmos o resultado que é realmente importante para nós.

[05:14] Saindo dessa questão de seletores, um detalhe, eu posso também fazer uma aglutinação de resultados. Se eu olhar para o “Evaluation time”, esse é o timestamp da consulta que eu fiz, estou pegando essa informação com uma restrição de tempo.

[05:46] Vamos imaginar que eu quero olhar o que aconteceu, em termos de número, no último 1 minuto. Vou trabalhar com o modificador offset e vou setar 1m, http_server_requests_seconds_count{application="app-forum-api",method=~"GET|POST",status!~"2..|3..",uri!="/actuator/prometheus} offset 1m

[06:02] Vou executar, ele pegou o resultado do último minuto. Estamos fazendo uma observação, em uma série temporal, de 1 minuto. Saindo desse escopo, se você quiser dar uma olhada, vai em “Help”. Eu vou deixar o link da documentação.

[06:28] Mas, basicamente, esse conteúdo está em “Querying > Basics”. Se procurarmos aqui, o modificador offset, está tudo na documentação e eu vou deixar o link para vocês.

[06:55] Uma coisa que é bem legal é começarmos a trabalhar com funções. Nós já entendemos o que são seletores, já trabalhamos com um modificador para fazer uma aglutinação, porém, podemos ir além disso e trabalhar com funções que, basicamente, é o recurso que mais vamos utilizar daqui para frente na hora de compor as nossas métricas.

[07:19] Vamos imaginar o seguinte, eu preciso saber a taxa de crescimento de uma métrica específica. Vamos entender que essa métrica está relacionada ao número de requisições. Então, o que eu vou fazer?

[07:34] Não me importa qual é o método, qual é o verbo HTTP, e não me importa qual é o status. Tenho http_server_requests_seconds_count{application=”app-forum-api", uri!=”/actuator/prometheus”}.

[07:56] Tenho esse resultado, porém, vamos imaginar que eu quero olhar 1 minuto, vamos olhar para [1m]. O que eu vou ter é um range vector que vai trazer para mim um resultado correspondente a cada scrape que o Prometheus executou no endpoint.

[08:24] Eu não tenho, de forma visível, qual foi a taxa de crescimento dessas séries temporais em 1 minuto, eu só tenho os valores divididos. Para facilitar a nossa vida, podemos utilizar a função increase.

[08:43] Com a função increase, eu pego a taxa de crescimento que eu tive no meu último minuto. É interessante entender que eu ainda continuo tendo um instant vector de retorno, increase( http_server_requests_seconds_count{application=”app-forum-api", uri!=”/actuator/prometheus”}[1m]).

[09:00] Basicamente, no último minuto, o que eu tive de taxa de crescimento em /tópicos foi 41 requisições; em tópicos/(id), 22 – estou fazendo um arredondamento meio brusco, mas dá para entender; 7 requisições em auth; não tive nenhum erro 500 em /topicos, tive 404 em tópicos/(id) e nenhum erro 500 em /topicos.

[09:33] Agora, vamos imaginar o seguinte: eu estou precisando saber qual o foi o número de requisições que eu tive, e o increase está trazendo para mim essa taxa de crescimento, mas não está somando esse valor.

[09:48] Eu preciso ter uma agregação de conteúdo para que eu tenha o total. Vamos trabalhar com o sum, que é um agregador. Vou utilizar o sum para pegar o resultado do increase e trazer o número específico de requisições do último minuto, que foram 76 requisições.

[10:14] Inclusive, eu consigo ir para a parte gráfica e ter a visibilidade de uma métrica já "plotada" para mim. É bem interessante entender como podemos fazer agregações e ter o resultado de uma métrica, bem conciso.

[10:35] Além disso, vamos imaginar que estamos na seguinte situação: vamos mudar isso para [5m] e agora, diferente de obter a taxa de crescimento, vamos imaginar que eu preciso saber quantas requisições por segundo eu recebi nos últimos 5 minutos.

[11:01] Se eu executar a consulta, vai vir um range vector gigantesco com esse espaço de 5 minutos, uma vez que cada scrape que nós temos é de 5 em 5 segundos.

[11:14] Vem muita informação, mas eu preciso saber por segundo. Como vou trabalhar com isso? Vou utilizar a função irate. Com a função irate, eu consigo pegar o número de requisições por segundo que eu tive em cada um desses endpoints e me vem esse resultado aqui.

[11:40] O irate basicamente olha os dois últimos data points coletados em relação ao momento da sua consulta. Então, não é uma função que eu vou utilizar para criar uma métrica para a SLI, por exemplo.

[12:00] Não, porque é uma função para eu saber, em um momento exato, em um trecho curto de observabilidade, um dado que eu preciso. No nosso caso, o número de requisições.

[12:14] Se eu levar isso para a parte gráfica, está aqui, ele coloca cada um dos endpoints que ele conseguiu coletar e temos esse retorno já "plotado" em forma de gráfico. Eu poderia fazer uma agregação utilizando sum? Poderia, eu posso agregar esse resultado. Está aqui.

[12:37] Basicamente, se eu olhar para esse resultado, ele vai dizer que, por segundo, eu tive uma média de 1.2 requisições a cada segundo; no gráfico, quando fazemos a agregação, o resultado é esse.

[12:54] Não vou esgotar o assunto de funções agora, vamos lidar com funções até a última aula e vamos explorar melhor isso no próximo capítulo. Porém, no próximo capítulo, vamos subir o Grafana, vamos configurar o Grafana e já começar a construir o nosso dashboard para a nossa API.

[13:15] No decorrer dessa construção, vamos amadurecer ainda mais na linguagem PromQL. Te vejo na próxima aula.





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 04 Seletores, Modificadores e Funções



[00:00] Vamos dar continuidade ao nosso curso. Na aula anterior, nós falamos sobre tipos de métricas. Agora, já vamos entrar em outro assunto que está relacionado a seletores, agregadores e funções. Vamos ter uma prévia sobre isso para você entender como você pode manipular uma métrica e obter o resultado que você precisa.

[00:29] Para começarmos a falar desse assunto, vou trabalhar com essa métrica que é o http_server_requests_seconds_count. Eu tenho essa métrica, se eu executar a consulta no Prometheus, ele vai me trazer esse instant vector que tem várias séries temporais, mas vou fazer uma filtragem.


http_server_requests_seconds_count

- Se eu executar a consulta no Prometheus, ele vai me trazer esse instant vector que tem várias séries temporais:

http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	149
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/actuator/prometheus"}
	1193
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	4110
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	2152
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	240
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	763





# ##############################################################################################################################################################
# Seletores

[00:49] Vou colocar o application="app-forum-api", vou colocar qual é o método que vamos olhar, method="GET". Vamos também fazer uma verificação no status="200", requisições que retornaram sucesso. Está de bom tamanho, por hora, vamos executar, http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200"}.

- Consulta:
http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200"}

- Resultado:
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/actuator/prometheus"}
	1215
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	4178
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	2196


[01:25] Executando essa consulta, o nosso retorno trouxe para nós um endpoint chamado actuator/prometheus; trouxe também o /topicos e o topicos/{id}. O actuator/prometheus não é um endpoint consumido pelos nossos usuários, então ele não nos interessa nessa visualização.

[01:49] O que vamos fazer? Vamos trabalhar com um seletor de negação. Vou colocar na uri tudo que for diferente de actuator/prometheus, http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200",uri!="/actuator/prometheus"}. Melhorou um pouco a situação porque tive agora o /topicos e o /topicos{id} retornando para mim a contagem de requisições.

- Seletor de negação:
!=

- Consulta:
http_server_requests_seconds_count{application="app-forum-api",method="GET",status="200",uri!="/actuator/prometheus"}

- Resultado:
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	4422
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	2331











# Time series Selectors
Instant vector selectors
<https://prometheus.io/docs/prometheus/latest/querying/basics/>
Instant vector selectors allow the selection of a set of time series and a single sample value for each at a given timestamp (instant): in the simplest form, only a metric name is specified. This results in an instant vector containing elements for all time series that have this metric name.

This example selects all time series that have the http_requests_total metric name:

    http_requests_total

It is possible to filter these time series further by appending a comma separated list of label matchers in curly braces ({}).

This example selects only those time series with the http_requests_total metric name that also have the job label set to prometheus and their group label set to canary:

    http_requests_total{job="prometheus",group="canary"}

It is also possible to negatively match a label value, or to match label values against regular expressions. The following label matching operators exist:

    =: Select labels that are exactly equal to the provided string.
    !=: Select labels that are not equal to the provided string.
    =~: Select labels that regex-match the provided string.
    !~: Select labels that do not regex-match the provided string.

Regex matches are fully anchored. A match of env=~"foo" is treated as env=~"^foo$".

For example, this selects all http_requests_total time series for staging, testing, and development environments and HTTP methods other than GET.

    http_requests_total{environment=~"staging|testing|development",method!="GET"}











[02:12] Só que existe também uma autenticação que ocorre nessa aplicação e ela é através do método “POST”, então eu preciso mudar o meu seletor para que consiga obter esse resultado.

[02:26] Para fazer isso, é bem simples. Quando vou trabalhar com o "ou" lógico aqui dentro do Prometheus, da linguagem PromQL, eu tenho que mudar o meu seletor e usar esse seletor com acento que, basicamente, é um seletor que suporta uma espécie de regex.

[02:45] Vou colocar method=~"GET|POST" e vou executar. Legal, tenho aqui esse retorno que é o método POST com sucesso, status="200", em /auth.

- Iremos usar esse seletor, que faz o match do Regex:
=~: Select labels that regex-match the provided string.

- Trocando:
de:
method="GET"
para:
method=~"GET|POST"

- Consulta:
http_server_requests_seconds_count{application="app-forum-api",method=~"GET|POST",status="200",uri!="/actuator/prometheus"}

- Resultado:
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	4659
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	2456
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	861









[03:23] Vamos trabalhar novamente com o nosso seletor com acento. Ao invés de usarmos 200, vamos usar um coringa, que é o ponto. O ponto é um coringa que equivale a qualquer caractere – como no Unix ou Linux, você utiliza o asterisco.

[03:42] Está aqui, status="2..". Você vê que não mudou nada a consulta está certa. Vamos expandir isso, vamos imaginar que, de repente, a sua API está por detrás de um CDN, tem um redirecionamento, um proxy, então tem também a família 300. Vamos colocar status="2..|3..".

- Iremos usar esse seletor, que faz o match do Regex:
=~: Select labels that regex-match the provided string.

- Trocando:
de:
status="200"
para:
status=~"2..|3.."

- Consulta:
http_server_requests_seconds_count{application="app-forum-api",method=~"GET|POST",status=~"2..|3..",uri!="/actuator/prometheus"}

- Resultado:
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	6035
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	3158
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	1126








[04:22] Agora vamos imaginar no caso em que você quer simplesmente pegar os erros. Eu preciso saber o que não está dando certo, o que podemos mudar? Vamos no seletor e vamos criar um seletor de negação que suporta o nosso regex: status!~"2..|3..".

[04:44] Vou trabalhar com esse seletor e aqui tenho o retorno dos erros que captei na minha aplicação. Teve erro 500, 404, 400 e mais outro 500 aqui embaixo. Dessa forma, conseguimos trabalhar dentro de uma métrica fazendo uma consulta customizada para pegarmos o resultado que é realmente importante para nós.

- Trocando:
de:
status="2..|3.."
para:
status!~"2..|3.."

- Consulta:
http_server_requests_seconds_count{application="app-forum-api",method=~"GET|POST",status!~"2..|3..",uri!="/actuator/prometheus"}

- Resultado:
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	239
http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	342










# offset

[05:14] Saindo dessa questão de seletores, um detalhe, eu posso também fazer uma aglutinação de resultados. Se eu olhar para o “Evaluation time”, esse é o timestamp da consulta que eu fiz, estou pegando essa informação com uma restrição de tempo.

[05:46] Vamos imaginar que eu quero olhar o que aconteceu, em termos de número, no último 1 minuto. Vou trabalhar com o modificador offset e vou setar 1m, http_server_requests_seconds_count{application="app-forum-api",method=~"GET|POST",status!~"2..|3..",uri!="/actuator/prometheus} offset 1m

[06:02] Vou executar, ele pegou o resultado do último minuto. Estamos fazendo uma observação, em uma série temporal, de 1 minuto. Saindo desse escopo, se você quiser dar uma olhada, vai em “Help”. Eu vou deixar o link da documentação.

[06:28] Mas, basicamente, esse conteúdo está em “Querying > Basics”. Se procurarmos aqui, o modificador offset, está tudo na documentação e eu vou deixar o link para vocês.





# Offset modifier

<https://prometheus.io/docs/prometheus/latest/querying/basics/#offset-modifier>

The offset modifier allows changing the time offset for individual instant and range vectors in a query.

For example, the following expression returns the value of http_requests_total 5 minutes in the past relative to the current query evaluation time:

	http_requests_total offset 5m

Note that the offset modifier always needs to follow the selector immediately, i.e. the following would be correct:

	sum(http_requests_total{method="GET"} offset 5m) // GOOD.

While the following would be incorrect:

	sum(http_requests_total{method="GET"}) offset 5m // INVALID.

The same works for range vectors. This returns the 5-minute rate that http_requests_total had a week ago:

	rate(http_requests_total[5m] offset 1w)

For comparisons with temporal shifts forward in time, a negative offset can be specified:

	rate(http_requests_total[5m] offset -1w)

Note that this allows a query to look ahead of its evaluation time.





# TRADUÇÃO
# modificador de deslocamento

O modificador de deslocamento permite alterar o deslocamento de tempo para vetores instantâneos e de intervalo individuais em uma consulta.

Por exemplo, a seguinte expressão retorna o valor de http_requests_total 5 minutos no passado em relação ao tempo de avaliação da consulta atual:

	http_requests_deslocamento total 5m

Observe que o modificador de deslocamento sempre precisa seguir o seletor imediatamente, ou seja, o seguinte estaria correto:

	sum(http_requests_total{method="GET"} offset 5m) // BOM.

Enquanto o seguinte seria incorreto:

	sum(http_requests_total{method="GET"}) offset 5m // INVÁLIDO.

O mesmo funciona para vetores de alcance. Isso retorna a taxa de 5 minutos que http_requests_total tinha uma semana atrás:

	rate(http_requests_total[5m] offset 1w)

Para comparações com deslocamentos temporais para frente no tempo, um deslocamento negativo pode ser especificado:

	rate(http_requests_total[5m] offset -1w)

Observe que isso permite que uma consulta olhe antes de seu tempo de avaliação.
























[06:55] Uma coisa que é bem legal é começarmos a trabalhar com funções. Nós já entendemos o que são seletores, já trabalhamos com um modificador para fazer uma aglutinação, porém, podemos ir além disso e trabalhar com funções que, basicamente, é o recurso que mais vamos utilizar daqui para frente na hora de compor as nossas métricas.

[07:19] Vamos imaginar o seguinte, eu preciso saber a taxa de crescimento de uma métrica específica. Vamos entender que essa métrica está relacionada ao número de requisições. Então, o que eu vou fazer?

[07:34] Não me importa qual é o método, qual é o verbo HTTP, e não me importa qual é o status. Tenho http_server_requests_seconds_count{application=”app-forum-api", uri!=”/actuator/prometheus”}.

[07:56] Tenho esse resultado, porém, vamos imaginar que eu quero olhar 1 minuto, vamos olhar para [1m]. O que eu vou ter é um range vector que vai trazer para mim um resultado correspondente a cada scrape que o Prometheus executou no endpoint.

[08:24] Eu não tenho, de forma visível, qual foi a taxa de crescimento dessas séries temporais em 1 minuto, eu só tenho os valores divididos. Para facilitar a nossa vida, podemos utilizar a função increase.

[08:43] Com a função increase, eu pego a taxa de crescimento que eu tive no meu último minuto. É interessante entender que eu ainda continuo tendo um instant vector de retorno, increase( http_server_requests_seconds_count{application=”app-forum-api", uri!=”/actuator/prometheus”}[1m]).

[09:00] Basicamente, no último minuto, o que eu tive de taxa de crescimento em /tópicos foi 41 requisições; em tópicos/(id), 22 – estou fazendo um arredondamento meio brusco, mas dá para entender; 7 requisições em auth; não tive nenhum erro 500 em /topicos, tive 404 em tópicos/(id) e nenhum erro 500 em /topicos.


# FUNÇÃO
# increase

<https://prometheus.io/docs/prometheus/latest/querying/functions/#increase>

increase( http_server_requests_seconds_count{application=”app-forum-api", uri!=”/actuator/prometheus”}[1m]).

CORRIGIDA:
increase( http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[1m])


Resultado:
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	0
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	39.269157349331884
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	22.907008453776932
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	3.2724297791109906
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	9.817289337332971


# increase()

<https://prometheus.io/docs/prometheus/latest/querying/functions/#increase>

increase(v range-vector) calculates the increase in the time series in the range vector. Breaks in monotonicity (such as counter resets due to target restarts) are automatically adjusted for. The increase is extrapolated to cover the full time range as specified in the range vector selector, so that it is possible to get a non-integer result even if a counter increases only by integer increments.

The following example expression returns the number of HTTP requests as measured over the last 5 minutes, per time series in the range vector:

	increase(http_requests_total{job="api-server"}[5m])

increase acts on native histograms by calculating a new histogram where each compononent (sum and count of observations, buckets) is the increase between the respective component in the first and last native histogram in v. However, each element in v that contains a mix of float and native histogram samples within the range, will be missing from the result vector.

increase should only be used with counters and native histograms where the components behave like counters. It is syntactic sugar for rate(v) multiplied by the number of seconds under the specified time range window, and should be used primarily for human readability. Use rate in recording rules so that increases are tracked consistently on a per-second basis.
irate() 





[09:33] Agora, vamos imaginar o seguinte: eu estou precisando saber qual o foi o número de requisições que eu tive, e o increase está trazendo para mim essa taxa de crescimento, mas não está somando esse valor.

[09:48] Eu preciso ter uma agregação de conteúdo para que eu tenha o total. Vamos trabalhar com o sum, que é um agregador. Vou utilizar o sum para pegar o resultado do increase e trazer o número específico de requisições do último minuto, que foram 76 requisições.

[10:14] Inclusive, eu consigo ir para a parte gráfica e ter a visibilidade de uma métrica já "plotada" para mim. É bem interessante entender como podemos fazer agregações e ter o resultado de uma métrica, bem conciso.

increase( http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[1m])


{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	2.1819371965743586
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	32.72905794861538
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	33.82002654690256
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	1.0909685982871793
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	6.545811589723076



sum(increase( http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[1m]))
{}
	76.36363636363635










[10:35] Além disso, vamos imaginar que estamos na seguinte situação: vamos mudar isso para [5m] e agora, diferente de obter a taxa de crescimento, vamos imaginar que eu preciso saber quantas requisições por segundo eu recebi nos últimos 5 minutos.

[11:01] Se eu executar a consulta, vai vir um range vector gigantesco com esse espaço de 5 minutos, uma vez que cada scrape que nós temos é de 5 em 5 segundos.

http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[5m]

http_server_requests_seconds_count{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	129 @1669168252.371
129 @1669168257.367
129 @1669168262.369
129 @1669168267.369
130 @1669168272.367
130 @1669168277.369
130 @1669168282.369
130 @1669168287.367
130 @1669168292.367
130 @1669168297.367
131 @1669168302.37
131 @1669168307.367
131 @1669168312.367
131 @1669168317.367
131 @1669168322.373
131 @1669168327.367
131 @1669168332.37
131 @1669168337.367
132 @1669168342.367
132 @1669168347.367
133 @1669168352.367
133 @1669168357.367

[11:14] Vem muita informação, mas eu preciso saber por segundo. Como vou trabalhar com isso? Vou utilizar a função irate. Com a função irate, eu consigo pegar o número de requisições por segundo que eu tive em cada um desses endpoints e me vem esse resultado aqui.

[11:40] O irate basicamente olha os dois últimos data points coletados em relação ao momento da sua consulta. Então, não é uma função que eu vou utilizar para criar uma métrica para a SLI, por exemplo.

- Consulta:
	irate(http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[5m])

- Resultado:
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="CLIENT_ERROR", status="404", uri="/topicos/{id}"}
	0
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos"}
	0.6004803843074459
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="GET", outcome="SUCCESS", status="200", uri="/topicos/{id}"}
	0.4003202562049639
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="CLIENT_ERROR", status="400", uri="/auth"}
	0.20016012810248196
{application="app-forum-api", exception="None", instance="app-forum-api:8080", job="app-forum-api", method="POST", outcome="SUCCESS", status="200", uri="/auth"}
	0.20016012810248196


- É possível agregar o irate, usando o sum:
	sum(irate(http_server_requests_seconds_count{application="app-forum-api", uri!="/actuator/prometheus"}[5m]))

- Resultado:
{}
	1.4



#  irate()

<https://prometheus.io/docs/prometheus/latest/querying/functions/#irate>

irate(v range-vector) calculates the per-second instant rate of increase of the time series in the range vector. This is based on the last two data points. Breaks in monotonicity (such as counter resets due to target restarts) are automatically adjusted for.

The following example expression returns the per-second rate of HTTP requests looking up to 5 minutes back for the two most recent data points, per time series in the range vector:

	irate(http_requests_total{job="api-server"}[5m])

irate should only be used when graphing volatile, fast-moving counters. Use rate for alerts and slow-moving counters, as brief changes in the rate can reset the FOR clause and graphs consisting entirely of rare spikes are hard to read.

Note that when combining irate() with an aggregation operator (e.g. sum()) or a function aggregating over time (any function ending in _over_time), always take a irate() first, then aggregate. Otherwise irate() cannot detect counter resets when your target restarts.

