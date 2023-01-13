using FluentAssertions;
using Microsoft.Extensions.Configuration;
using Microsoft.AspNetCore.Mvc;
using Backend_IPCA_Gym.Controllers;
using LayerBLL.Utils;
using LayerBOL.Models;

namespace UnitTests
{
    public class UnitTests
    {
        /// <summary>
        /// Configuração utilizada para a criação de controladores, para que seja possível fazer as function calls
        /// </summary>
        /// <returns></returns>
        public static IConfiguration InitConfiguration()
        {
            var config = new ConfigurationBuilder().AddJsonFile("C:\\TrabalhosPraticos\\Projeto_Aplicado\\ipca_gym" +
                "\\Backend_IPCA_Gym\\Backend_IPCA_Gym\\appsettings.json").AddEnvironmentVariables().Build();

            return config;
        }

        /// <summary>
        /// Teste no request de obter todos os clientes da Base de Dados
        /// Esperado:
        ///     - No caso de retornar um JsonResult: Não ser nulo e ter code 200 ou 204
        ///     - No caso de retornar um StatusCodeResult: Deve ser code 204
        /// </summary>
        [Fact]
        public async void GetAllClienteTest()
        {
            var config = InitConfiguration();
            var clienteController = new ClienteController(config);

            IActionResult requestResult = await clienteController.GetAll();

            requestResult.Should().NotBeNull();


            var statusCodeResult = requestResult as StatusCodeResult;
            if (statusCodeResult != null)
            {
                statusCodeResult.Should().NotBeNull();
                statusCodeResult.StatusCode.Should().Be(204);
            }

            var jsonResult = requestResult as JsonResult;
            if (jsonResult != null)
            {
                var response = jsonResult.Value as Response;
                if (response != null)
                {
                    response.Should().NotBeNull();
                    response.StatusCode.Should().Be(StatusCodes.SUCCESS);
                }
            }
        }

        /// <summary>
        /// Teste no request de fazer login
        /// Esperado:
        ///     - No caso de retornar um JsonResult: Não ser nulo e ter code 200 ou 204
        ///     - No caso de retornar um StatusCodeResult: Deve ser code 204
        /// Dados introduzidos:
        ///     - Primeiro InlineData com email errado
        ///     - Segundo InlineData com dados corretos
        ///     - Terceiro InlineData com password errada
        /// </summary>
        [Theory]
        [InlineData("email_incorreto", "dm")]
        [InlineData("user@gmail.com", "pass")]
        [InlineData("user@gmail.com", "pass_incorreta")]
        public async void LoginClienteTest(string mail, string password)
        {
            var config = InitConfiguration();
            var clienteController = new ClienteController(config);

            LoginCliente modelTest = new LoginCliente();

            modelTest.mail = mail;
            modelTest.password = password;

            IActionResult requestResult = await clienteController.Login(modelTest);

            requestResult.Should().NotBeNull();

            var statusCodeResult = requestResult as StatusCodeResult;
            if (statusCodeResult != null)
            {
                statusCodeResult.Should().NotBeNull();
                statusCodeResult.StatusCode.Should().Be(204);
            }

            var jsonResult = requestResult as JsonResult;
            if (jsonResult != null)
            {
                var response = jsonResult.Value as Response;
                if (response != null)
                {
                    response.Should().NotBeNull();
                    response.StatusCode.Should().Be(StatusCodes.SUCCESS);
                }
            }
        }

        /// <summary>
        /// Teste no request de fazer login
        /// Esperado:
        ///     - No caso de retornar um JsonResult: Não ser nulo e ter code 200 ou 204
        ///     - No caso de retornar um StatusCodeResult: Deve ser code 204
        /// Dados introduzidos:
        ///     - 1011 (ID anteriormente existente na Base de Dados)
        ///     - -1 (ID inexistente)
        /// </summary>
        [Theory]
        [InlineData(1011)]
        [InlineData(-1)]
        public async void DeleteClassificacaoTest(int id)
        {
            var config = InitConfiguration();
            var classficacaoController = new ClassificacaoController(config);

            IActionResult requestResult = await classficacaoController.Delete(id);

            requestResult.Should().NotBeNull();

            var statusCodeResult = requestResult as StatusCodeResult;
            if (statusCodeResult != null)
            {
                statusCodeResult.Should().NotBeNull();
                statusCodeResult.StatusCode.Should().Be(204);
            }

            var jsonResult = requestResult as JsonResult;
            if (jsonResult != null)
            {
                var response = jsonResult.Value as Response;
                if (response != null)
                {
                    response.Should().NotBeNull();
                    response.StatusCode.Should().Be(StatusCodes.SUCCESS);
                }
            }
        }
    }
}