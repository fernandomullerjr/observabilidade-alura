
### Subindo stack - Curso2

cd /home/fernando/cursos/sre-alura/002_monitoramento-prometheus-grafana-alertmanager/materiais_aulas/aula_01/conteudo_01
docker-compose up -d


### Grafana

- Grafana acessível:
http://192.168.0.110:3000
<http://192.168.0.110:3000>

- Usuário e senha padrão
admin
admin

- Definida nova senha:
nemsei90

- Usuário e senha
admin
nemsei90

- Grafana precisa de ajuste nas permissões:
sudo chmod 777 -R grafana/
sudo chmod 777 -R Grafana/


### Métricas

- Endpoint das métricas:
http://192.168.0.110/metrics
<http://192.168.0.110/metrics>





### Endpoints

- Acessando:
192.168.0.110:8080/topicos
http://192.168.0.110:80/topicos/1

curl -v http://192.168.0.110:80/topicos/1
curl -v http://192.168.0.110:80/topicos/2
curl -v http://192.168.0.110:80/topicos/3

- Agora é possível acessar o Actuator:
http://192.168.0.110:8080/actuator

- Acessando o Health:
http://192.168.0.110:8080/actuator/health

- Acessando o Info:
http://192.168.0.110:8080/actuator/info

- Acessando as métricas em Metrics:
http://192.168.0.110:8080/actuator/metrics





### Outros comandos úteis

docker container start mysql-forum-api
docker container start redis-forum-api


- Simulando erros no JDBC:
docker container stop mysql-forum-api
docker container start mysql-forum-api


- Conectando no Container do Prometheus:
docker container exec -ti prometheus-forum-api sh

- Logs nos Containers:

docker logs grafana-forum-api -f --tail 22
docker logs app-forum-api -f --tail 22
docker logs prometheus-forum-api -f --tail 22
docker logs proxy-forum-api -f --tail 22
docker logs client-forum-api -f --tail 22