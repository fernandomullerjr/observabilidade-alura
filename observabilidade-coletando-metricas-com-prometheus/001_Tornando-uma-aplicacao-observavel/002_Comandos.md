

- Diretório principal:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/


- Subindo stack
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
docker-compose up


- Buildando aplicação e validando se está tudo ok

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


- Subindo aplicação:
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
sh start.sh


- Acessando:
192.168.0.113:8080/topicos
http://192.168.0.113:8080/topicos/1

curl -v http://192.168.0.113:8080/topicos/1
curl -v http://192.168.0.113:8080/topicos/2
curl -v http://192.168.0.113:8080/topicos/3




- Agora é possível acessar o Actuator:
http://192.168.0.113:8080/actuator

- Acessando o Health:
http://192.168.0.113:8080/actuator/health

- Acessando o Info:
http://192.168.0.113:8080/actuator/info

- Acessando as métricas em Metrics:
http://192.168.0.113:8080/actuator/metrics





- É necessário adicionar ao Docker-compose da aula:
 sre-alura/observabilidade-coletando-metricas-com-prometheus/003_Primeiros-passos-com-Prometheus/002_Subindo-a-stack-com-API-e-Prometheus.md
- SOLUÇÃO:
<https://github.com/prometheus/prometheus/issues/5976>
user: "1000:1000"



- Conectando no Container do Prometheus:
docker container exec -ti prometheus-forum-api sh




- O Linux consegue converter o valor do timestamp em Data:

~~~~bash
fernando@debian10x64:~$ date -d @1668824257.369
Fri 18 Nov 2022 11:17:37 PM -03
fernando@debian10x64:~$
~~~~




- iterando array
array=( @1668824257.369 @1668824262.368 @1668824267.369 @1668824272.369 )
for i in "${array[@]}" ; do date -d $i ; done






- Curso2 - diretório:
cd /home/fernando/cursos/sre-alura/monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01


- Grafana precisa de ajuste nas permissões:
sudo chmod 777 -R grafana/
sudo chmod 777 -R Grafana/


- Grafana acessível:
http://192.168.0.113:3000
<http://192.168.0.113:3000>