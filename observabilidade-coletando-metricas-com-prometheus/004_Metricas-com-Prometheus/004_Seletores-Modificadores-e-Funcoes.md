
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