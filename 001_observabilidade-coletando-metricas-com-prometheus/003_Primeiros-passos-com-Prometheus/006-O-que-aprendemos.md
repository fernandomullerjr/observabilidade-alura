

# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 003 - Aula 06 O que aprendemos?"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 06 O que aprendemos?


# Transcrição
[0:00] Este é o vídeo de conclusão do nosso capítulo 3. Vamos ver o que aprendemos durante essa sessão.

[0:10] Começamos subindo a nossa stack, com a nossa API e com o Prometheus, via Docker Composer. Conhecemos as configurações do Prometheus, tanto em termos de arquivos quanto em relação à navegação na sua interface.

[0:28] Também subimos um cliente (que pode ser considerado um cliente sintético) para o consumo dessa API e, logicamente, para termos uma maior proximidade com a interação de um usuário real.

[0:45] Esses foram os nossos passos no capítulo 3 e eles nos habilitaram para seguir para o capítulo seguinte. Muito obrigado, nos vemos na próxima aula.