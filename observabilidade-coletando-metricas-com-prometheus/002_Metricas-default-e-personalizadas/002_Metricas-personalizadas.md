


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# push

git status
git add .
git commit -m "Curso SRE Alura - Modulo 002 - Aula 02 Métricas personalizadas"
eval $(ssh-agent -s)
ssh-add /home/fernando/.ssh/chave-debian10-github
git push
git status




# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas personalizadas


# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# Transcrição

[00:00] Sejam bem-vindos a mais uma aula do nosso curso. Nessa aula, vamos dar sequência ao assunto que foi tratado no término da aula anterior. O problema é o seguinte: temos basicamente todas as métricas da JVM sendo expostas, porém, nós não temos nenhuma métrica que corresponda a alguma regra de negócio da nossa aplicação, e temos alguns processos que precisam ser mapeados.

[00:30] Hoje, não temos o controle sobre o número de usuários autenticados e as tentativas de autenticação com erro. Vamos trabalhar em cima disso e gerar uma métrica para autenticação com sucesso e outra para erros de autenticação. Vamos lá.

[00:49] Vamos abrir a IDE, vamos para o Eclipse. No Eclipse, vamos na nossa aplicação, expandir o main/java e vamos abrir o pacote forum.controller.

[01:05] Vamos trabalhar no controller. Dentro do controller, temos uma classe que é a AutenticacaoController.java que faz o controle de autenticação. Basicamente, o que essa classe faz – vou explicar de forma bem resumida – é, se você passa um usuário e uma senha correta, ela vai te retornar um token; caso contrário, ela te retorna um erro já tratado.

[01:35] Esse é o básico dessa classe para não termos que mergulhar em cima da lógica feita nessa aplicação. Então, o que precisamos aqui? Primeiro, precisamos de umas dependências, mas essas dependências vão ser cumpridas conforme formos criando o método que vamos utilizar.

[01:57] Eu vou começar definindo dois atributos. Eles vão se chamar authUserSuccess e authUserErrors. Ao inserir esses dois atributos, você vai ver que o Eclipse mostra um problema.

[02:44] O que é esse problema? Temos que importar uma biblioteca, temos que importar uma dependência para que isso funcione. Presta bem atenção aqui porque eu vou trabalhar com o Counter vindo do io.micrometer.core.instrument.

[03:02] Importando isso, você pode expandir que você vai ver que a dependência foi importada e não temos mais aquela sinalização de erro. Agora, vou criar o método aqui. Esse método vou copiar de uma cola porque já o fiz.

[03:22] Vou liberar esse código na plataforma para você baixar e não ter que digitar. Se você não é um desenvolvedor Java, pode ser meio complexo de você digitar isso e entender. Não seria interessante porque o nosso foco não é codar em Java, mas trabalhar com a parte de observabilidade usando o Prometheus e o Grafana.

[03:45] Então, vou colar aqui o meu método. Está batendo um erro porque está sem o s. Vamos entender isso, note que também está pedindo mais uma dependência. Eu sempre vou trabalhar com o io.micrometer.core.instrument.

[04:06] Problema resolvido, está tudo certo, agora vamos entender isso. Eu tenho um método, os parâmetros que ele necessita é o MeterRegistry e o registry. Aqui, eu tenho uma propriedade que é o authUserSuccess, que eu já instanciei aqui em cima, o atributo.

[04:23] Basicamente, o que ela faz? Ela cria uma métrica chamada auth_user_success com uma descrição ”usuarios autenticados” e anexa isso no registry, então ela registra essa métrica para mim.

[04:42] Aqui, eu tenho também o meu atributo authUserErrors e ele faz exatamente a mesma coisa, Counter.Builder, cria o auth_user_error, cria a descrição ”erros de login” e faz o registro dessa métrica.

[05:03] Tendo feito isso, já estamos trabalhando com uma métrica que é do tipo Counter. Uma métrica do tipo Counter significa que é uma métrica incremental, ela vai crescer gradativamente, conforme um evento específico for disparado.

[05:22] Que evento é esse que vai trabalhar com o crescimento dessa métrica, que vai alimentar a configuração dela? Se olharmos aqui, temos o ResponseEntity que retorna um TokenDto.

[05:37] Esse token é o token de autenticação. Estamos falando de uma API Rest, ela não guarda o estado e o processo de autenticação corresponde à requisição de um front-end ou de um app que vai trazer um usuário e uma senha que vão chegar nessa API.

[05:59] Ela vai encaminhar essa requisição para a lógica de validação que ela possui, consultar o banco, e, se estiver tudo certo, ela retorna um token para esse usuário continuar executando suas ações autenticado através da API. Então, essa é a lógica.

[06:19] Aqui, temos um bloco try/catch. Dentro desse bloco, eu tenho, logicamente no try, a minha primeira opção é a de sucesso. Vou basicamente trabalhar com o incremento desse valor.

[06:29] Deixa eu pegar a minha opção authUserSuccess primeiro. No meu bloco try, vou incrementar essa chamada authUserSuccess.increment(). Bem simples, a mesma coisa vou fazer com Errors authUserErrors.increment().

[07:22] Feito. Tendo feito essa configuração, salva o código e já podemos fazer o build desse novo artefato. Vou abrir o meu Terminal, a aplicação está rodando, vou parar a aplicação.

[07:43] Só um detalhe, eu parei a aplicação, mas é legal darmos uma olhada. Vou dar um “Ctrl + F”, se eu procurar por user, não tem nada relacionado a user nas métricas que estávamos olhando na aula passada.

[08:01] Vou abrir outra aba do Terminal, deixa eu minimizar tudo para ficar mais legível. Aqui, vou rodar o mvn clean package, vou criar um novo artefato JAR com essa mudança de código.

[08:19] É só aguardar um pouco, o processo é rápido e já vamos poder testar e subir a aplicação. Eu estou trabalhando, logicamente, com o Redis e o MySQL já rodando lá no contêiner, porque estou dando sequência à aula anterior e o ambiente está de pé.

[08:39] Terminou o processo, vamos para a IDE e vamos subir a aplicação. Só aguardar alguns segundos e pronto, subiu a aplicação. Vamos voltar para o browser, vamos atualizar e vamos procurar por user agora. Aqui, auth_user_success_total usuarios autenticados, é uma métrica do tipo counter, e está aqui, não tem nenhum usuário autenticado. Vamos para o error.

[09:22] Vou colocar “user” para ele continuar buscando. Está aqui, auth_user_error_total erros de login, também do tipo counter, e está aqui a métrica auth_user_error 0.0. Nenhum erro e nenhum usuário autenticado.

[09:45] Agora vem a parte interessante, nós precisamos de algum software para testar isso. Vou utilizar o Postman, acho muito interessante que você instale o Postman. É bem simples utilizá-lo, é só seguir os passos que eu vou fazer para fazermos a autenticação, o erro de autenticação e validarmos essa métrica.

[10:08] Com o Postman aberto, você vai fazer uma requisição. Essa requisição é “Post”, você define como “Post”, vamos fazer para “localhost:8080/auth”. Aqui, em “Parâmetros”, não entra nada. Deixa eu só verificar uma pequena coisa aqui.

[10:39] Em “Authorization”, entra um “Bearer Token”, então o “Type” é “Bearer Token”. Em “Header”, é importante você colocar o “Content Type”, que é “application/json”, senão não vai funcionar. E no “Body” você vai colocar esse JSON aqui:

{
    "email": "moderador@email.com",
    "senha": "123456"
}

[11:08] Isso é o que você precisa configurar no Postman para fazer essa requisição. Não tem mais nenhuma outra configuração e eu posso disparar aqui, pressionando "Send".

[11:20] O retorno da API para mim é esse, ele me retorna o token do tipo "Bearer". Então, eu tenho agora um usuário autenticado. Se eu voltar para as nossas métricas e procurar por user, usuários com sucesso, tem 0.

[11:41] Lembra o que eu falei na aula anterior de que esse endpoint não é dinâmico, ele não se atualiza sozinho, é por consulta. Então, vou atualizar e está aqui, agora já temos um 1 usuário autenticado.

[11:57] Vamos mandar mais algumas autenticações, vou mandar algumas, não estou nem contando quantas estou fazendo, só para termos algum número. Vamos voltar lá agora, vou atualizar e está aqui, 13 usuários autenticados. Tivemos 13 autenticações.

[12:25] Não está fazendo distinção se é o usuário X ou Y, é só o número de autenticações válidas. Tendo feito isso, vou colocar um caractere a mais para essa autenticação resultar em um erro:

{
    "email": "moderador@email.com",
    "senha": "1234567"
}`

[13:38] Você vê aqui, o status é “200 OK”. O legal é que, antes de fazermos a falha, se você procurar, você vai ver que o /auth já foi mapeado na nossa outra métrica que avalia a questão de SLA, ela verifica o tempo de resposta, a duração do processamento dessa requisição pela nossa API.

[13:08] Vamos provocar o erro agora. eu já inseri um outro caractere aqui e, quando eu enviar, o meu retorno vai ser um “400 Bad Request”. Vou enviar alguns, pronto.

[13:25] Vamos voltar nas nossas métricas, vamos olhar essa outra métrica de usuário. Aqui é o total de usuários autenticados e aqui estão os erros. Não tem nenhum erro porque eu ainda não atualizei depois das tentativas.

[13:42] Agora vou atualizar, deixa eu encontrar de novo o erro, aqui é com sucesso. Está aqui, por incrível que pareça, também bateu 13, foi uma coincidência mesmo, vou mandar mais três – um, dois e três.

[14:04] Ele vai ter que ir para 16, vamos voltar lá. A não ser que eu tenha colocado o mesmo nome, está certo, então realmente foi uma coincidência. Vou atualizar, agora estou com 16 erros e vamos procurar o sucesso. O sucesso continua com 13.

[14:35] Nós criamos uma métrica personalizada, agora podemos mensurar quantos usuários autenticaram em um período, é isso que vamos fazer quando realmente trabalharmos com essa métrica, porque vamos olhar uma série temporal.

[14:49] Vamos entender quantos usuários autenticaram no último minuto e quantos erros de autenticação ocorreram também no último minuto ou nos últimos N minutos.

[15:00] Esse era o propósito dessa aula, porém, vamos um pouco mais além, porque vou fazer uma pequena mudança na próxima aula. Ainda vamos ter uma aula e essa aula vai ser para empacotarmos essa aplicação em um contêiner.

[15:19] Vamos "dockerizar" essa aplicação, agora que ela já tem uma métrica personalizada, para que tenhamos mais facilidade e daqui para frente possamos olhar somente para o Prometheus, para o Grafana e para o Alert Manager.

[15:35] Vamos parar de mexer no código da aplicação aqui, vou deixar a documentação relacionada às métricas do Prometheus. Vou até pegar aqui, você vai ter acesso à essa documentação na própria plataforma.

[15:53] Eu recomendo que você leia com atenção, que você estude essa documentação para você implementar outras métricas nessa aplicação e, futuramente, levar isso para uma aplicação sua.

[16:05] É isso, nos vemos na próxima aula em que vamos "conteinerizar" essa aplicação. 





# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# ##############################################################################################################################################################
# 02 Métricas personalizadas


[00:49] Vamos abrir a IDE, vamos para o Eclipse. No Eclipse, vamos na nossa aplicação, expandir o main/java e vamos abrir o pacote forum.controller.


[01:05] Vamos trabalhar no controller. Dentro do controller, temos uma classe que é a AutenticacaoController.java que faz o controle de autenticação. Basicamente, o que essa classe faz – vou explicar de forma bem resumida – é, se você passa um usuário e uma senha correta, ela vai te retornar um token; caso contrário, ela te retorna um erro já tratado.

/home/fernando/cursos/sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/java/br/com/alura/forum/controller/AutenticacaoController.java

sre-alura/observabilidade-coletando-metricas-com-prometheus/prometheus-grafana/app/src/main/java/br/com/alura/forum/controller/AutenticacaoController.java



- ANTES:

~~~~java
package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication); 		
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}

		
	}
}
~~~~







- Adicionar as linhas

	Counter authUserSuccess;
	Counter authUserErrors;




- DEPOIS

~~~~java
package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication); 		
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
			
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}

		
	}
}

~~~~










[01:57] Eu vou começar definindo dois atributos. Eles vão se chamar authUserSuccess e authUserErrors. Ao inserir esses dois atributos, você vai ver que o Eclipse mostra um problema.

[02:44] O que é esse problema? Temos que importar uma biblioteca, temos que importar uma dependência para que isso funcione. Presta bem atenção aqui porque eu vou trabalhar com o Counter vindo do io.micrometer.core.instrument.




- Adicionar o import do Counter
~~~~java
import io.micrometer.core.instrument.Counter;
~~~~