

- Subindo stack
cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/
docker-compose up


- Buildando aplicação e validando se está tudo ok

cd /home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app
mvn clean package


- Subindo aplicação:
sh start.sh


- Acessando:
192.168.0.113:8080/topicos
http://192.168.0.113:8080/topicos/1

curl -v http://192.168.0.113:8080/topicos/1
curl -v http://192.168.0.113:8080/topicos/2
curl -v http://192.168.0.113:8080/topicos/3