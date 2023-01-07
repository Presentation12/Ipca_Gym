using Microsoft.AspNetCore.Mvc;
using LayerBOL.Models;
using LayerBLL.Utils;
using LayerBLL.Logics;
using Microsoft.AspNetCore.Authorization;

namespace Backend_IPCA_Gym.Controllers
{
    /// <summary>
    /// Controlador da entidade Atividade
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class AtividadeController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador Atividade
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public AtividadeController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Método http get para retornar as atividades da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de atividades em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);
            
            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar as atividades de um ginasio
        /// </summary>
        /// <param name="targetID">ID do ginasio em causa</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de atividades em formato Json</returns>
        [HttpGet("getbygym/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetbyGymAll(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetAllbyGymLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar uma atividade através do seu id
        /// </summary>
        /// <param name="targetID">ID da atividade que é pretendida ser retornada</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a Atividade em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de uma nova atividade
        /// </summary>
        /// <param name="newAtividade">Dados da nova Atividade a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> Post([FromBody] Atividade newAtividade)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.PostLogic(sqlDataSource, newAtividade);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de uma atividade através do id e novos dados
        /// </summary>
        /// <param name="atividade">Atividade com dados alterados</param>
        /// <param name="targetID">ID da Atividade pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Patch([FromBody] Atividade atividade, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.PatchLogic(sqlDataSource, atividade, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover uma atividade da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da Atividade pretendida para ser removida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para obter estatísticas de um ginásio
        /// </summary>
        /// <param name="targetID">ID do ginasio em causa</param>
        /// <returns></returns>
        [HttpGet("getgymstats/{targetID}"), Authorize(Roles = "Admin, Funcionario, Gerente")]
        public async Task<IActionResult> GetGymStats(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetGymStatsLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
