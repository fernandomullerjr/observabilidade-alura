

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