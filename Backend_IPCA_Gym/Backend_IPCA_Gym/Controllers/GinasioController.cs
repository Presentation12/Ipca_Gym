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
    /// Controlador da entidade ginásio
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class GinasioController : ControllerBase
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador ginásio
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public GinasioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar os ginásios da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de ginásios em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await GinasioLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um ginásio através do seu id
        /// </summary>
        /// <param name="targetID">ID da ginásio que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o ginásio em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await GinasioLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo ginásio
        /// </summary>
        /// <param name="newGinasio">Dados do novo ginásio a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin")]
        public async Task<IActionResult> Post([FromBody] Ginasio newGinasio)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await GinasioLogic.PostLogic(sqlDataSource, newGinasio);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um ginásio através do id e novos dados
        /// </summary>
        /// <param name="ginasio">ginásio com dados alterados</param>
        /// <param name="targetID">ID do ginásio pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente")]
        public async Task<IActionResult> Patch([FromBody] Ginasio ginasio, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await GinasioLogic.PatchLogic(sqlDataSource, ginasio, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um ginásio da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID do ginásio pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin, Gerente, FuncionarioC")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await GinasioLogic.DeleteLogic(sqlDataSource, targetID);

            if(response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
