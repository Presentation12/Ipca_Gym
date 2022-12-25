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
    /// Controlador da entidade HorarioFuncionario
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class HorarioFuncionarioController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador HorarioFuncionario
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public HorarioFuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar os horários (diário) do funcionário da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de horários (diário) do funcionário em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar um horário (dia) do funcionário através do seu id
        /// </summary>
        /// <param name="targetID">ID do horário (dia) do funcionário que é pretendido ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e o horário (dia) do funcionário em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de um novo horário (dia) do funcionário
        /// </summary>
        /// <param name="newHorarioFuncionario">Dados do novo horário (dia) do funcionário a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin")]
        public async Task<IActionResult> Post([FromBody] HorarioFuncionario newHorarioFuncionario)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.PostLogic(sqlDataSource, newHorarioFuncionario);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de um horário (dia) do funcionário através do id e novos dados
        /// </summary>
        /// <param name="horarioFuncionario">Horário (dia) do funcionário com dados alterados</param>
        /// <param name="targetID">ID do Horário (dia) do funcionário pretendido para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Patch([FromBody] HorarioFuncionario horarioFuncionario, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.PatchLogic(sqlDataSource, horarioFuncionario, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um horário (dia) do funcionário da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID do horário (dia) do funcionário pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para leitura dos dados de todos os horários de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="targetID">ID do funcionário que é pretendido ser retornado os horários</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e os horários (dia) do funcionário em formato Json</returns>
        [HttpGet("funcionario/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetAllByFuncionarioID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await HorarioFuncionarioLogic.GetAllByFuncionarioIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion
    }
}
