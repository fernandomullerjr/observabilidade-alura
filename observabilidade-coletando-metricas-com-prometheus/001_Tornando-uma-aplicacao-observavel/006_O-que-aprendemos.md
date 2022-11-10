


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 001 - Aula  06 O que aprendemos?"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 06 O que aprendemos?





Transcrição

[0:00] Esta aqui é a aula de conclusão do capítulo, em que vamos apenas recapitular o que foi feito, para sabermos o que, de fato, aprendemos.

[0:11] Começamos com a configuração do ambiente, pegando o pacote da aplicação na plataforma, criando o projeto e abrindo-o na nossa IDE. Além disso, fizemos a externalização das métricas com o Actuator, que é uma biblioteca do Spring. Essas métricas foram expostas em um endpoint e são métricas default da JVM.

[0:42] Ademais, trabalhamos com o Micrometer, fazendo a transição dessas métricas para outro endpoint com um formato legível para o Prometheus.

[0:54] Esses foram os passos que vimos nesse capítulo. Deixamos tudo preparado para seguirmos para o capítulo 2, cumprimos toda a nossa missão. Então, nos vemos na próxima aula.
