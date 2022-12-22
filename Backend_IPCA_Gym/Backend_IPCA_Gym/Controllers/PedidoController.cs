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


namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade pedido
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class PedidoController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador pedido
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public PedidoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar os pedidos da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de pedidos em formato Json</returns>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um pedido através do seu id
        /// </summary>
        /// <param name="targetID">ID do pedido que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a pedido em formato Json</returns>
        [HttpGet("{targetID}")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo pedido
        /// </summary>
        /// <param name="newPedido">Dados do novo pedido a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Pedido newPedido)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLogic.PostLogic(sqlDataSource, newPedido);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um pedido através do id e novos dados
        /// </summary>
        /// <param name="pedido">pedido com dados alterados</param>
        /// <param name="targetID">ID do pedido pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] Pedido pedido, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLogic.PatchLogic(sqlDataSource, pedido, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um pedido da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID do pedido pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PedidoLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
