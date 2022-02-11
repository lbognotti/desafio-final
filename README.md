# Desafio Final - W4/G8

# Sobre o Projeto

O objetivo deste projeto final é implementar uma API REST (com um total de #6 Requisitos à serem atendidos) com o desafio de acondicionar mercadorias que os
Vendedores como supermercados, empresas do setor alimentício, atacadistas, etc, afim de 
acondicioná-los em locais corretos de acordo com as exigências para cada tipo diferente de produtos, 
neste caso, produtos que necessitam serem armazenado em zonas frigoríficas diferentes e diante disto aplicar 
os conteúdos trabalhados durante o BOOTCAMP MELI.

# Ferramentas Utilizadas

- Java
- Spring boot
- Maven
- Lombok
- Mockito

# Servidor do PostgreSQL

Está sendo usado o Docker Compose para automatizar a inicialização de serviços em ambiente de desenvolvimento. Para subir um container PostgreSQL em background use o comando:

docker-compose up -d db

# Executar aplicação

Para executar o projeto em desenvolvimento através da IDE IntelliJ Idea é necessário definir duas variáveis de ambiente: SCOPE=local e APPLICATION=api

Isso pode ser feito nas configurações de execução da aplicação:

Ao lado do botão de build (ícone de martelo) no canto superior direito da janela da IDE, clique em "Application" e depois em "Editar configurações" (ou "Edit configurations");

Na janela que abrir, vá na seção "Environment" e adicione as variáveis SCOPE e APPLICATION no campo "Environment variables".

# Prefixo global

Todos os endpoints estão acessíveis com o prefixo /api/v1 na URI.

# endpoints API

<b>Requisito #1</b><br>
<b>User Story</b> - O Representante insere um lote de produtos no armazém de distribuição para registrar a existência de estoque.</br>
<b>POST e PUT - fresh-products/inboundorder/</b>

POST JSON

```sh
{
	"inboundOrder": {
		"orderNumber": 1,
		"orderDate": "2022-02-13T12:28:38.954Z",
		"section": {
			"sectionId": 1,
			"warehouseId": 1
		},
		"batchStock": [
			{
				"number": 1,
				"productId": 1,
				"currentTemperature": 22.2,
				"maximumTemperature": 33.3,
				"minimumTemperature": 11.1,
				"initialQuantity": 20,
				"currentQuantity": 20,
				"manufacturingDate": "2022-02-03T12:28:38.954Z",
				"manufacturingTime": "2022-02-03T12:28:38.954Z",
				"dueDate": "2022-02-03T12:28:38.954Z"
			},
			{
				"number": 2,
				"productId": 3,
				"currentTemperature": 32.2,
				"maximumTemperature": 71.3,
				"minimumTemperature": 10.1,
				"initialQuantity": 10,
				"currentQuantity": 5,
				"manufacturingDate": "2022-02-03T12:28:38.954Z",
				"manufacturingTime": "2022-02-03T12:28:38.954Z",
				"dueDate": "2022-02-03T12:28:38.954Z"
			}
		]
	}
}
```

<b>Requisito #2</b><br>
<b>User Story</b> - Comprador quer adicionar produtos ao carrinho de compras do Marketplace para comprá-los, se assim desejar.

<b>GET /fresh-products/<br></b>
Veja uma lista de produtos e se ela não existir retorna uma um "404 Not Found".<br>

<b>GET /fresh-products/list? querytype=[categoría producto]<br></b>
Veja uma lista de produtos por categoria.<br>
category:<br>
FS = Fresco<br>
RF = Refrigerado<br>
FF = Congelado<br>
Se a lista não existir, ela deve retornar um "404 Not Found".<br>

<b>POST /fresh-products/orders/<br></b>
Veja uma lista de produtos que compõem o PurchaseOrder. <br>
Calcula preço no final e devolva-o juntamente com o código de status "201 CREATED".<br>
Se não houver estoque de um produto,retorna um erro por produto.<br>
```sh
{
    "date": "2022-02-05T12:00:00.000Z",
    "buyerId": 1,
    "orderStatus": "OPEN",
    "products": [
        {
            "salesAdId": 1,
            "quantity": 1
        },
        {
            "salesAdId": 2,
            "quantity": 1
        }
    ]
}
```
<b>GET /fresh-products/orders/ querytype=[idOrder]</b><br>
Veja o pedido com produtos.<br>

<b>PUT /fresh-products/orders/ query param=[idOrder]</b>
Modifica o pedido existente.
```sh
{
    "date": "2022-02-05T12:00:00.000Z",
    "buyerId": 1,
    "products": [
        {
            "id": 3,
            "quantity": 2,
            "salesAdId": 1
        },
        {
            "id": 5,
            "quantity": 1,
            "salesAdId": 2
        }
    ]
}
```

<b>Requisito #3</b>
<b>User Story</b> - Representante pode consultar um produto em stock no armazém para saber a sua localização num setor e os diferentes lotes onde se encontra.

<b>GET fresh-products/list? querytype=[idProducto]<br></b>
Veja uma lista de produtos com todos os lotes onde aparece.<br>
Se a lista não existir, ela deve retornar um “404 Not Found”.

<b>GET /fresh-products/list? querytype=[idProducto] querytype=[L]<br></b>
Veja uma lista de produtos com todos os lotes onde aparece.<br>
Ordenados por:<br>
L = ordenado por lote<br>
C = ordenado por quantidade atual<br>
F = ordenado por data de vencimento<br>

<b>Requisito #4</b>
<b>User Story</b> - Representante pode consultar um produto em todos os armazéns para saber o estoque em cada armazém do referido produto.

<b>GET /fresh-products/warehouse/ querytype=id product]<br></b>
Obter a quantidade total de produtos por armazém.<br>
Se o produto não existe em nenhum armazém, você deve retornar um "404 Not Found".

<b>Requisito #5</b>
<b>User Story</b> - O representante quer poder consultar os produtos em estoque que estão prestes a expirar no almoxarifado, a fim de aplicar alguma ação comercial com eles.

<b>GET /fresh-products/due-date/ queryparam=[number of days] queryparam=[section]<br></b>
Obtem todos os lotes armazenados em um setor de um armazém ordenados por sua data de vencimento.

<b>GET fresh-products/due-date/list? queryparam=[number of days] queryparam=[category] queryparam=[asc]<br></b>
Obtem uma lista dos lotes pedidos por prazo de validade, que pertencem a uma determinada categoria de produto.<br>
<b>Categorias:</b>
FS = Fresco
RF = Refrigerado
FF = Congelado

<b>Requisito #6</b>
<b>User Story Sugerida</b> - O Vendedor precisa listar todos os produtos com validade entre 30 e 60 dias ou, outra sugestão de intervalo de dias afim de fazer uma promoção de produtos com lotes próximos a data de validade.

<b>User Story Sugerida</b> - O Vendedor obtem lista de anúncios de produtos com data de validade em determinado intervalo de dias (mínimo de 30 dias), lista esses anúncios e faz uma atualização de valor de produtos gerando uma promoção pontual.

<b>User Story Sugerida</b> - O Vendedor obtem uma lista ordenada de anúncios por data de vencimento de produtos e as datas que forem menor ou igual a 21 dias ele altera o status do anúncio para desativado.

# endpoints Sugeridos Requisito #6

# Testes Unitários
```sh
class BuyerServiceTest
@Test
    shouldReturnABueyrWhenFindMethodPass
    //Deve Rertornar um Comprador

    shouldReturnApiExceptionWhenFindMethodFails
    //Deve Retornar uma Exception Quando Método de Retornar Comprador Falhar.

    shouldReturnTheListOfItemCartsWhenSuccess
    //Deve Retornar uma Lista de Itens do Carrinho de Compras.

class SalesAdServiceTest
@Test
    shouldReturnSalesAdWhenFindOnePass
    //Deve Retornar um Anuncio
  
    shouldReturnSalesAdWhenFindOneFails
    //Deve Retornar uma Exception Quando Anúncio Não é Encontrado.
 
    shouldReturnsTheSumOfPrices
    //Deve Retornar A Soma dos Valores de uma Lista de Anúncios.
  
    shouldReturnTrueWhenAllIdsExists
    //Retorna Verdadeiro Quanto Encontra Uma Lista de Anúncios.

    shouldReturnFalseWhenOneIdNotExists
    //Deve Retornar False Quando Não Encontra Um Id De Anúncios Em Uma Lista

class ProductServiceTest
@Test
    deveRetornarListaOrdenadaPorBatchstock
    //Deve Retornar Lista Ordenada Por Batchstock

    deveRetornarListaOrdenadaPorQuantidade
    //Deve Retornar Lista Ordenada Por Quantidade
 
    deveRetornarListaOrdenadaPorDataDeValidade
    //Deve Retornar Lista Ordenada Por Data De Validade
```
   
# Autores

- <strong>Everson Okuhara</strong> - Desenvolvedor e Documentação
- <strong>Lucas Matos</strong> - Desenvolvedor
- <strong>Ronaldd Pinho</strong> - Desenvolvedor
- <strong>Tiago Wolsen</strong> - Desenvolvedor
- <strong>Vinicius Feitoza</strong> - Desenvolvedor e Documentação

# Expressões de Gratidão

Agradecemos todo o suporte neste desafio aos membros da DH @Kenyo @Mauri @Joice @Michele @Betania e @Michelle e também o pessoal do Meli 🙂 <br>
Agracedimento especial a cada membro do grupo G8 que dedicaram e deram o seu máximo neste desafio! 🤝
