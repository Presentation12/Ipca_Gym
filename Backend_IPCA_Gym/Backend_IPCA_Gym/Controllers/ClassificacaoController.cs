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
    /// Controlador da entidade Classificação
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class ClassificacaoController : Controller
    {
        private readonly IConfiguration _configuration;
        /// <summary>
        /// Construtor do controlador Classificação
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public ClassificacaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar as classificações da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de classificações em formato Json</returns>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar uma classificação através do seu id
        /// </summary>
        /// <param name="targetID">ID da classificação que é pretendida ser retornada</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a classificação em formato Json</returns>
        [HttpGet("{targetID}")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de uma nova classificação
        /// </summary>
        /// <param name="newClassificacao">Dados da nova classificação a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Classificacao newClassificacao)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.PostLogic(sqlDataSource, newClassificacao);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de uma classificação através do id e novos dados
        /// </summary>
        /// <param name="classificacao">classificação com dados alterados</param>
        /// <param name="targetID">ID da classificação pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] Classificacao classificacao, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.PatchLogic(sqlDataSource, classificacao, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover uma classificação da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da classificação pretendida para ser removida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para retornar as classificacoes de um ginásio da base de dados
        /// </summary>
        /// <param name="targetID">ID do ginásio que é pretendido ser retornado as classificacoes</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de planos de treino em formato Json</returns>
        
        [HttpGet("Ginasio/{targetID}")]
        public async Task<IActionResult> GetAllClassificationsByGinasioID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await ClassificacaoLogic.GetAllClassificationsByGinasioIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
        #endregion
    }
}

