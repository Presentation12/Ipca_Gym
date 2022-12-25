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
    /// Controlador da entidade plano nutricional
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class PlanoNutricionalController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador plano nutricional
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public PlanoNutricionalController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar os planos nutricionais da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de planos nutricionais em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar uma plano nutricional através do seu id
        /// </summary>
        /// <param name="targetID">ID do plano nutricional que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o plano nutricional em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo plano nutricional
        /// </summary>
        /// <param name="newPlanoNutricional">Dados do novo plano nutricional a ser inserido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> Post([FromBody] PlanoNutricional newPlanoNutricional)
        {
            // No novo plano é passado o ID da pessoa logada(funcionario ou gerente)
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.PostLogic(sqlDataSource, newPlanoNutricional);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um plano nutricional através do id e novos dados
        /// </summary>
        /// <param name="planoNutricional">plano nutricional com dados alterados</param>
        /// <param name="targetID">ID da plano nutricional pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> Patch([FromBody] PlanoNutricional planoNutricional, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Certificar que o plano a ser modificado pertence ao gym associado a quem fez o request

            Response response = await PlanoNutricionalLogic.PatchLogic(sqlDataSource, planoNutricional, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um plano nutricional da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da plano nutricional pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Certificar que o plano a ser apagado pertence ao gym associado a quem fez o request

            Response response = await PlanoNutricionalLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para retornar os planos de nutrição de um ginásio da base de dados
        /// </summary>
        /// <param name="targetID">ID do ginásio que é pretendido ser retornado os planos de nutrição</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de planos de nutrição em formato Json</returns>
        [HttpGet("Ginasio/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetAllByGinasioID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.GetAllByGinasioIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um plano nutricional da base de dados através do seu id e suas dependencias
        /// </summary>
        /// <param name="targetID">ID da plano nutricional pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("Delete/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> DeleteChecked(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");

            //Certificar que o plano a ser apagado pertence ao gym associado a quem fez o request

            Response response = await PlanoNutricionalLogic.DeleteCheckedLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion
    }
}
