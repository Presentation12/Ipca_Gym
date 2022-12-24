using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using LayerBOL.Models;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;
using LayerBLL.Utils;
using LayerBLL.Logics;
using Swashbuckle.AspNetCore.Annotations;
using StatusCodes = Microsoft.AspNetCore.Http.StatusCodes;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade Funcionario
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class FuncionarioController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador funcionario
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public FuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar os funcionários da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de funcionários em formato Json</returns>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um funcionário através do seu id
        /// </summary>
        /// <param name="targetID">ID do funcionário que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o funcionario em formato Json</returns>
        [HttpGet("{targetID}")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo funcionário
        /// </summary>
        /// <param name="newFuncionario">Dados do novo funcionario a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Funcionario newFuncionario)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.PostLogic(sqlDataSource, newFuncionario);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um funcionário através do id e novos dados
        /// </summary>
        /// <param name="funcionario">funcionario com dados alterados</param>
        /// <param name="targetID">ID do funcionario pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] Funcionario funcionario, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.PatchLogic(sqlDataSource, funcionario, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um funcionário da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da funcionario pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para efetuar o login de um funcionário
        /// </summary>
        /// <param name="conta">Model de login de Funcionario</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a token de sessão em formato json</returns>
        [Route("login")]
        [HttpPost]
        public async Task<IActionResult> Login([FromBody] LoginFuncionario conta)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.LoginLogic(sqlDataSource, conta, _configuration);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        [HttpPatch("recoverpass")]
        public async Task<IActionResult> RecoverPassword(int codigo, string password)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.RecoverPasswordLogic(codigo, password, sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para adicionar um novo cliente por parte do funcionario
        /// </summary>
        /// <param name="newCliente">Objeto que contém os dados do novo cliente</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPost("register/cliente")]
        public async Task<IActionResult> RegistClient([FromBody] Cliente newCliente)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.RegistClientLogic(sqlDataSource, newCliente);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para remover um cliente por parte do funcionario
        /// </summary>
        /// <param name="targetID">ID do cliente que se pretende remover</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpDelete("remove/cliente/{targetID}")]
        public async Task<IActionResult> RemoveCliente(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.RemoveClienteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar um cliente por parte do funcionario
        /// </summary>
        /// <param name="targetID">ID do funcionario a editar</param>
        /// <param name="cliente">Objeto que contém os dados atualizados do funcionário</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("edit/cliente/{targetID}")]
        public async Task<IActionResult> EditCliente(int targetID, [FromBody] Cliente cliente)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.EditClienteLogic(sqlDataSource, targetID, cliente);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar valor de stock de um produto
        /// </summary>
        /// <param name="targetID">ID do produto da Loja que se pretende alterar stock</param>
        /// <param name="quantidade">Novo valor de quantidade de stock na loja</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("changestock/{targetID}")]
        public async Task<IActionResult> EditLojaStock(int targetID, int quantidade)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.EditLojaStockLogic(sqlDataSource, quantidade, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para editar um produto
        /// </summary>
        /// <param name="produto">Objeto que contém dados atualizados do produto da Loja</param>
        /// <param name="targetID">ID do produto pretendido a mudar</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("edit/product/{targetID}")]
        public async Task<IActionResult> EditLoja(int targetID, Loja produto)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await FuncionarioLogic.EditLojaLogic(sqlDataSource, produto, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}