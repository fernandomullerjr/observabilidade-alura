Definir quais métricas monitorar sempre foi um grande desafio, de fato, uma grande quantidade das “coisas” monitoradas nunca são realmente utilizadas por terem pouca ou nenhuma utilidade para entender o comportamento dos sistemas. Atualmente os métodos mais amplamente aceitos para definir quais métricas devem ser monitoradas são o USE (Utilization, Saturation e Error) e o RED (Request Rate, Error Rate e Duration of request). Enquanto o USE tem foco no recurso (ex: cpu, memória, serviços A, serviço B), o RED, que segundo o próprio criador Tom Wilkie, é baseado no que ele aprendeu como SRE no Google, tem foco na chamado do serviço (request) e normalmente é representado através de histogramas. 


# USE 
(Utilization, Saturation e Error)
 
# RED 
(Request Rate, Error Rate e Duration of request)