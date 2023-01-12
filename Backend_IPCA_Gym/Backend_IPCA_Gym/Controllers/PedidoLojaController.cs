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
    /// Controlador da entidade PedidoLoja
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoLojaController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador PedidoLoja
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public PedidoLojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar os pedidos de loja da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de pedidos de loja em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um pedido de loja através do seu id
        /// </summary>
        /// <param name="targetID1">ID do pedido de loja que é pretendido ser retornado</param>
        /// <param name="targetID2">ID do produto de loja que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a PedidoLoja em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetByID(int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.GetByIDLogic(sqlDataSource, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo pedido de loja
        /// </summary>
        /// <param name="newPedidoLoja">Dados do novo PedidoLoja a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin")]
        public async Task<IActionResult> Post([FromBody] PedidoLoja newPedidoLoja)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.PostLogic(sqlDataSource, newPedidoLoja);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um pedido de loja através do id e novos dados
        /// </summary>
        /// <param name="pedidoLoja">PedidoLoja com dados alterados</param>
        /// <param name="targetID1">ID do pedido de loja que é pretendido ser retornado</param>
        /// <param name="targetID2">ID do produto de loja que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Patch([FromBody] PedidoLoja pedidoLoja, int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.PatchLogic(sqlDataSource, pedidoLoja, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um pedido de loja da base de dados através do seu id
        /// </summary>
        /// <param name="targetID1">ID do pedido de loja que é pretendido ser retornado</param>
        /// <param name="targetID2">ID do produto de loja que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID1, int targetID2)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.DeleteLogic(sqlDataSource, targetID1, targetID2);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para retornar os produtos de um pedido da base de dados
        /// </summary>
        /// <param name="targetID">ID do pedido que é pretendido ser retornado os produtos</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de planos de treino em formato Json</returns>
        [HttpGet("Pedido/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> GetAllByPedidoIDID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.GetAllByPedidoIDLogic(sqlDataSource, targetID);

            //Se for cliente, tem de ser o seu pedido

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover todos os produtos de um pedido da base de dados através do seu id de pedido
        /// </summary>
        /// <param name="targetID">ID do pedido que é pretendido ser remover os produtos</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("Pedido/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> DeletePedidoLoja(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.DeletePedidoLogic(sqlDataSource, targetID);

            //Verificar que os produtos sao do ginasio de quem esta a executar o request

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo pedido de loja com verificação de stock e manipulação dele
        /// </summary>
        /// <param name="newPedidoLoja">Dados do novo PedidoLoja a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost("PedidoChecked"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> PostPedidoChecked([FromBody] PedidoLoja newPedidoLoja)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLojaLogic.PostPedidoCheckedLogic(sqlDataSource, newPedidoLoja);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion
    }
}
