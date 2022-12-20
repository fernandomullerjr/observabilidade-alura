# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso monitoramento-prometheus-grafana-alertmanager - Modulo 002 - Aula 01 Métricas Logged Users e Auth Errors "
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 01 Métricas Logged Users e Auth Errors


# Transcrição
[00:00] Vamos dar sequência no nosso curso, agora vamos trabalhar criando mais dois painéis.

[00:10] Vamos em “Add panel”. Agora vamos trabalhar com uma métrica que diz respeito ao número de usuários autenticados no último minuto.

[00:25] No campo "metrics browser", vou colocar user, já conseguimos buscar, vai ser auth_user_success_total. No caso, se eu colocar essa métrica aqui, ela já vai me trazer um valor, só que não é o valor que eu quero demonstrar no meu dash, eu quero um painel com números.

[00:48] O que eu preciso aqui? Eu quero olhar para o último minuto, não quero olhar a totalidade, não quero olhar um contador que sempre vai estar sendo incrementado, porque ele não vai refletir a realidade da minha API.

[01:03] Alguns usuários já não vão estar mais logados, então quero pegar somente a autenticação do último minuto para entender quantos usuários estão autenticados naquele minuto.

[01:15] Para esse caso, é interessante, primeiro, se eu definir o meu range time, eu não vou ter informações sendo plotadas aqui. Por quê? Porque eu preciso de um dado Scalar ou de um instant vector.

[01:31] Quando eu trabalho com um tempo especificado na minha consulta, eu formo um range vector e um range vector não tem como formar um gráfico. Então, eu vou ter que trabalhar com uma função.

[01:46] Vou utilizar o increase porque quero a taxa de crescimento dessa métrica no último minuto, increase(auth_user_success_total[1m]). Você pode se perguntar: “Você vai ter que utilizar o sum para fazer uma agregação?”, não é necessário nesse caso, o valor vai ser o mesmo, porque estou trabalhando apenas com uma série temporal, a partir do momento que uso o increase.

[02:09] Então, eu não preciso fazer uma agregação nesse caso. Está aqui, a visualização que eu vou escolher é “Stat” também. Estou com “9.82”, a taxa de crescimento de autenticação foi essa.

[02:28] É um número quebrado, vou arredondar isso. O título que vamos usar é “USERS LOGGED”, “usuários logados”. A descrição é “Usuários logados no último minuto”.

[02:50] O cálculo vai ser o último valor não nulo. O campo é numérico. A coloraçãom vou tirar o “Graph mode” para ficar só o número. Em “Standard options”, a unidade que vamos utilizar é uma unidade “Short” mesmo.

[03:14] Se eu quiser tirar um número decimal, eu posso colocar um valor “0” e ele vai arredondar. Essa métrica é uma aproximação do que temos de usuários autenticados no último minuto.

[03:28] Não é o valor exato do cálculo que o Prometheus faz olhando a taxa de crescimento do último minuto porque ele se baseia em uma série temporal, então o dado sempre vai ser um número quebrado, sempre vai ser um float.

[03:46] Não tenho “Threshold” aqui, vou manter um verde mais escuro. Então, já temos um painel com o número de usuários logados no último minuto. Vou formatá-lo, vou diminuir, está muito grande.

[04:09] Assim está mais interessante, pronto. Vamos adicionar mais um painel. Esse painel vai ser uma métrica relacionada aos erros de autenticação, é auth_user_error_total.

[04:23] Temos o mesmo paradigma de antes, eu quero pegar o meu último minuto e vou trabalhar com a minha taxa de crescimento, vai ser increase para [1m] e vamos seguir com o mesmo trabalho que fizemos na métrica anterior.

[04:47] Vai ser uma visualização “Stat”, o título do painel vou colocar como “AUTH ERRORS”, vou colocar como “Erros de autenticação no último minuto”, essa é a descrição. Quando alguém posicionar o mouse sobre a métrica, você vai ver a descrição, então é legal para alguém recém-chegado, que não entende a composição do dash, para se encontrar.

[05:19] O cálculo vai ser em cima do último valor não nulo; vou tirar a coloração gráfica para manter só o número; na unidade, vou trabalhar também com o “Short” – se eu colocar “None”, ele não muda nada, com valor de “String”, ele iria colocar todo o número completo, o que não é interessante, então vamos manter o “Short”.

[05:52] Vou tirar a casa decimal e aqui, sim, vou colocar um “Threshold”. Vamos imaginar que, em 1 minuto, se tivermos 50 erros de autenticação, teremos um problema. Como não vamos simular 50 erros de autenticação em 1 minuto, eu vou colocar aqui que 5 erros de autenticação é uma situação um pouco estranha.

[06:26] Vou abrir de novo o “Threshold”. E que 10 erros de autenticação já significa que talvez exista um problema no nosso database, porque a aplicação tem uma regra de negócio que vai validar o que o usuário enviou com os dados que estão no database, então ela vai fazer uma consulta para devolver um token para esse usuário.

[06:52] Vou deixar o “Threshold” dessa forma e está feito. Agora, já temos quatro painéis, são bem simples. deixa só eu dar uma reforçada nessa cor verde e trabalhar com um verde mais escuro.

[07:12] Até dado momento, está tudo tranquilo, já vamos começar a trabalhar com algumas coisas mais complexas, mas, por hora, vamos seguindo dessa maneira.

[07:22] Na próxima aula, já vamos trabalhar com dois outros painéis: um que reflete as informações de log, o log level, e outro que vai olhar para o pool de conexões da JDBC. Nos vemos na próxima aula.











# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Aula 01 Métricas Logged Users e Auth Errors

[00:00] Vamos dar sequência no nosso curso, agora vamos trabalhar criando mais dois painéis.

[00:10] Vamos em “Add panel”. Agora vamos trabalhar com uma métrica que diz respeito ao número de usuários autenticados no último minuto.

[00:25] No campo "metrics browser", vou colocar user, já conseguimos buscar, vai ser auth_user_success_total. No caso, se eu colocar essa métrica aqui, ela já vai me trazer um valor, só que não é o valor que eu quero demonstrar no meu dash, eu quero um painel com números.

auth_user_success_total




- Criado post na Alura:
https://cursos.alura.com.br/forum/topico-metricas-logged-users-e-auth-errors-metrics-broser-nao-exibe-auth_user_success_total-262187
<https://cursos.alura.com.br/forum/topico-metricas-logged-users-e-auth-errors-metrics-broser-nao-exibe-auth_user_success_total-262187>

Oi,
estou com problemas no módulo   002 - Aula 01 Métricas Logged Users e Auth Errors.

Aos 25segundos de aula, o professor fala sobre a métrica auth_user_success_total
Porém ao digitar ela o Metrics Browser não encontra ela.
Colocando apenas "user", também não acha nada relacionado.

O que pode estar ocorrendo?