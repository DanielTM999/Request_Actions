# Request Actions

`Request Actions` é uma biblioteca focade em requisoções

## HttpAction

`HttpActionImpl` é uma classe que implementa a interface `HttpAction` para fornecer funcionalidades de envio de requisições HTTP (GET, POST e DELETE, PUT), tanto de forma síncrona quanto assíncrona, utilizando a API de Cliente HTTP do Java.

## Funcionalidades

### Métodos Implementados

#### GET

- `HttpRequestResult<T> get(String url)`: Envia uma requisição GET para a URL especificada.
- `HttpRequestResult<T> get(String url, String... urlParams)`: Envia uma requisição GET para a URL com parâmetros de URL.
- `HttpRequestResult<T> get(URI url)`: Envia uma requisição GET para a URI especificada.
- `HttpRequestResult<T> get(String url, Map<String, String> header)`: Envia uma requisição GET com cabeçalhos especificados.
- `Future<HttpRequestResult<T>> getAsync(String url)`: Envia uma requisição GET de forma assíncrona.

#### POST

- `HttpRequestResult<T> post(String url, String body)`: Envia uma requisição POST com um corpo de string.
- `HttpRequestResult<T> post(String url, Object body)`: Envia uma requisição POST com um corpo de objeto, que será convertido em JSON.
- `Future<HttpRequestResult<T>> postAsync(String url, String body)`: Envia uma requisição POST de forma assíncrona.

#### DELETE

- `HttpRequestResult<T> delete(String url)`: Envia uma requisição DELETE para a URL especificada.
- `Future<HttpRequestResult<T>> deleteAsync(String url)`: Envia uma requisição DELETE de forma assíncrona.

### PUT

- `HttpRequestResult<T> put(String url)`: Envia uma requisição PUT para a URL especificada.
- `HttpRequestResult<T> put(URI url)`: Envia uma requisição PUT para a URI especificada.
- `HttpRequestResult<T> put(String url, String body)`: Envia uma requisição PUT com um corpo de string.
- `HttpRequestResult<T> put(String url, Object body)`: Envia uma requisição PUT com um corpo de objeto, que será convertido em JSON.
- `Future<HttpRequestResult<T>> putAsync(String url, String body)`: Envia uma requisição PUT de forma assíncrona.


### Construtores

- `HttpActionImpl()`: Inicializa um cliente HTTP padrão com um mapeador JSON padrão.
- `HttpActionImpl(HttpClient httpClient)`: Permite a injeção de um cliente HTTP personalizado.
- `HttpActionImpl(HttpMapper httpMapper)`: Permite a injeção de um mapeador JSON personalizado.
- `HttpActionImpl(HttpClient httpClient, HttpMapper httpMapper)`: Permite a injeção de um cliente HTTP e um mapeador JSON personalizados.

## Exceções

A classe lança as seguintes exceções:

- `HttpException`: Lançada em caso de erro durante o envio da requisição.
- `HttpRuntimeException`: Lançada em operações assíncronas em caso de erro.

## Uso

Aqui está um exemplo básico de como usar a classe `HttpActionImpl`:

```java
    HttpAction httpAction = new HttpActionImpl();
    try {
        HttpActionGet actionGet = new HttpActionImpl();
        String result = actionGet.get("https://www.google.com").getBody().get();

        System.out.println(result);
    } catch (HttpException e) {
        e.printStackTrace();
    }
```

## Classe HttpRequestResult

A classe `HttpRequestResult<T>` representa o resultado de uma requisição HTTP, fornecendo métodos para acessar o status, cabeçalhos e corpo da resposta.

### Funcionalidades

- `void setMapper(HttpMapper mapper)`: Define o mapeador JSON a ser utilizado para converter o corpo da resposta.
- `int getStatusCode()`: Retorna o código de status da resposta.
- `HttpHeaderResult getHeader()`: Retorna os cabeçalhos da resposta.
- `Optional<String> getBody()`: Retorna o corpo da resposta como uma string, se presente.
- `Optional<T> getBody(Class<T> referenceToMapper)`: Retorna o corpo da resposta convertido para o tipo especificado, se presente.
- `Optional<T> getBody(HttpMapper mapper, Class<T> referenceToMapper)`: Retorna o corpo da resposta convertido para o tipo especificado usando o mapeador fornecido, se presente.
- `<S> Optional<S> ifErrorGet(Class<S> reference)`: Retorna um valor do tipo especificado em caso de erro.
- `Optional<String> ifErrorGet()`: Retorna o corpo da resposta como uma string em caso de erro, se presente.

## Eventos

### Eventos de Erro

A classe permite registrar eventos de erro utilizando o `HttpErrorEvent`. Esse evento será acionado quando ocorrer um erro durante o processamento da requisição.

- `void registerErrorEvent(HttpErrorEvent errorEvent, boolean async)`: Registra um evento de erro que será executado de forma síncrona ou assíncrona, dependendo do valor de `async`.
- `void registerErrorEvent(HttpErrorEvent errorEvent)`: Registra um evento de erro que será executado de forma síncrona.

A interface `HttpErrorEvent` contém o método:

```java
void onError(Throwable throwable, String message);
```

### Eventos de Sucesso

A classe também permite registrar eventos de sucesso utilizando o `HttpSucessEvent<T>`. Esse evento será acionado quando a requisição for bem-sucedida.

- `void registerSucessEvent(HttpSucessEvent<T> sucessEvent, boolean async)`: Registra um evento de sucesso que será executado de forma síncrona ou assíncrona, dependendo do valor de `async`.
- `void registerSucessEvent(HttpSucessEvent<T> sucessEvent)`: Registra um evento de sucesso que será executado de forma síncrona.

A interface `HttpSucessEvent<T>` contém o método:

```java
void onSucess(Optional<T> result);
```