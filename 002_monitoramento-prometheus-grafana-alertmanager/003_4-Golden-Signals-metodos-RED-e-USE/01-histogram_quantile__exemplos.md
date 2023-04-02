
Se formos olhar para o “localhost/metrics” e procurarmos por essa métrica http_server_requests_seconds_bucket, temos buckets com valores maiores ou iguais que 50 milissegundos, 100, 200, 300, 500, 1 segundo e ao infinito e além, de 1 segundo para cima.
Então, eu quero entender onde que 99% das minhas requisições se enquadram, em qual bucket elas estão caindo. 

http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.05",} 1859.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.1",} 1861.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.2",} 1862.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.3",} 1862.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="0.5",} 1863.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="1.0",} 1863.0
http_server_requests_seconds_bucket{application="app-forum-api",exception="None",method="GET",outcome="SUCCESS",status="200",uri="/topicos/{id}",le="+Inf",} 1863.0



- Adicionar mais uma Query, agora para descobrir as que se enquadram em 90% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.90, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~


- Adicionar mais uma Query, agora para descobrir as que se enquadram em 75% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.75, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~



- Adicionar mais uma Query, agora para descobrir as que se enquadram em 50% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.50, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~



- Adicionar mais uma Query, agora para descobrir as que se enquadram em 25% dos casos:
em Metrics Browser:

~~~~bash
histogram_quantile(0.25, sum(rate(http_server_requests_seconds_bucket{application="$application", instance="$instance", job="app-forum-api", uri!="/actuator/prometheus"}[1m])) by (le))
~~~~
