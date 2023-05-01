# PROBLEMA

- Verificado que o painel está trazendo métricas do Actuator.
/actuator/health GET 200
	3.39 ms	1.19 s	72.5 ms	7.47 ms
/actuator/prometheus GET 200
	7.45 ms	51.5 ms	14.6 ms	9.91 ms



- Temos que ajustar a Query, para negar este path.


- Query editada:

~~~~bash
http_server_requests_seconds_max{application="$application", instance="$instance", job="app-forum-api", status="200", uri!="/actuator/prometheus"}
~~~~

- Na Query acima, só pegava os Actuator com path Prometheus







# SOLUÇÃO

- Necessário utilizar wildcard com REGEX no Prometheus, para filtrar todos que tem /actuator

- Exemplo de REGEX:
topic!~".+confluent.+"

- REGEX editado:
uri!~"/actuator.+"

- Query editada:

~~~~bash
http_server_requests_seconds_max{application="$application", instance="$instance", job="app-forum-api", status="200", uri!~"/actuator.+"}
~~~~

- Trouxe apenas os paths desejados:

/topicos GET 200
	2.86 ms	1.89 s	33.1 ms	4.09 ms
/topicos/{id} GET 200
	7.75 ms	47.4 ms	19.8 ms	27.4 ms
/auth POST 200
	112 ms	395 ms	185 ms	202 ms