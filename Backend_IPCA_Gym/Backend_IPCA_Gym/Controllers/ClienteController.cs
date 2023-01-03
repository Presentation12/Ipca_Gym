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
using Microsoft.AspNetCore.Authorization;

namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade Cliente
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class ClienteController : Controller
    {
        private readonly IConfiguration _configuration;
        /// <summary>
        /// Construtor do controlador cliente
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public ClienteController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar os clientes da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de clientes em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um cliente através do seu id
        /// </summary>
        /// <param name="targetID">ID do cliente que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a cliente em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Cliente")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo cliente
        /// </summary>
        /// <param name="newCliente">Dados do novo cliente a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin")]
        public async Task<IActionResult> Post([FromBody] Cliente newCliente)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.PostLogic(sqlDataSource, newCliente);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um cliente através do id e novos dados
        /// </summary>
        /// <param name="cliente">Cliente com dados alterados</param>
        /// <param name="targetID">ID da Cliente pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> Patch([FromBody] Cliente cliente, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.PatchLogic(sqlDataSource, cliente, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um cliente da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID do cliente pretendida para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        // Tentativa de enviar mail com password ao user
        /*
        [HttpGet("RecoverPass/{targetID}")]
        public async Task<IActionResult> RecoverPassword(int targetID, ClienteLogic ClienteLogic)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            await ClienteLogic.SendPasswordByEmail(sqlDataSource, targetID);

            return Ok();
        }
        */

        /// <summary>
        /// Método http para recuperar a palavra passe de um cliente
        /// </summary>
        /// <param name="mail">Mail do cliente que se pretende recuperar a password</param>
        /// <param name="password">Nova password</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpPatch("recoverpass")]
        public async Task<IActionResult> RecoverPassword(string mail, string password)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se quem executa request é quem possui o mail

            Response response = await ClienteLogic.RecoverPasswordLogic(mail, password, sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para executar o login do user
        /// </summary>
        /// <param name="conta">Dados da conta para fazer login</param>
        /// <returns>Resposta do request que contém a sua mensagem, código e a token de sessão do cliente em formato json</returns>
        [HttpPost("login")]
        public async Task<IActionResult> Login(LoginCliente conta)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClienteLogic.LoginLogic(sqlDataSource, conta, _configuration);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http para remover um cliente
        /// </summary>
        /// <param name="targetID">ID do cliente que se pretende remover</param>
        /// <returns>Resposta do request que contém a sua mensagem e o seu código em formato json</returns>
        [HttpDelete("remove/cliente/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> DeleteCliente(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Verificar se Gerente e Funcionario pertencem ao ginasio ao qual o cliente pertence

            Response response = await ClienteLogic.DeleteClienteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion
    }
}
