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
    /// Controlador da entidade marcação
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class MarcacaoController : Controller
    {
        private readonly IConfiguration _configuration;

        /// <summary>
        /// Construtor do controlador marcação
        /// </summary>
        /// <param name="configuration">Dependency Injection</param>
        public MarcacaoController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        #region DEFAULT REQUESTS

        /// <summary>
        /// Método http get para retornar as marcações da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de marcações em formato Json</returns>
        [HttpGet, Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar uma marcação através do seu id
        /// </summary>
        /// <param name="targetID">ID da marcação que é pretendida ser retornada</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a marcação em formato Json</returns>
        [HttpGet("{targetID}"), Authorize(Roles = "Admin")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de uma nova marcação
        /// </summary>
        /// <param name="newMarcacao">Dados da nova marcação a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost, Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> Post([FromBody] Marcacao newMarcacao)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.PostLogic(sqlDataSource, newMarcacao);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração dos dados de uma marcação através do id e novos dados
        /// </summary>
        /// <param name="marcacao">marcação com dados alterados</param>
        /// <param name="targetID">ID da marcação pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> Patch([FromBody] Marcacao marcacao, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.PatchLogic(sqlDataSource, marcacao, targetID);

            //Verificar se a Marcacao é do cliente que faz o request
            //Verificar se a Marcacao é do funcionario que faz o request
            //Verificar se a Marcacao é de um funcionario do ginasio ao qual o gerente que faz o request pertence

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover uma marcação da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da marcação pretendida para ser removida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}"), Authorize(Roles = ("Admin"))]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método http get para retornar todos as marcações de um funcionário através do seu id de funcionário na base de dados
        /// </summary>
        /// <param name="targetID">ID do funcionário ao qual pertencem as marcações a ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a marcação em formato Json</returns>
        [HttpGet("funcionario/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario")]
        public async Task<IActionResult> GetAllByFuncionarioID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.GetAllByFuncionarioIDLogic(sqlDataSource, targetID);

            //Se for um funcionario, no front-end mandar o ID do user que faz request
            //Se for de um gerente, certificar que esse funcionario pertence ao ginasio

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);
            
            return new JsonResult(response);
        }

        /// <summary>
        /// Método http get para retornar todos as marcações de um cliente através do seu id de cliente na base de dados
        /// </summary>
        /// <param name="targetID">ID do cliente ao qual pertencem as marcações a ser retornado</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a marcação em formato Json</returns>
        [HttpGet("cliente/{targetID}"), Authorize(Roles = "Admin, Cliente")]
        public async Task<IActionResult> GetAllByClienteID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.GetAllByClienteIDLogic(sqlDataSource, targetID);

            //Se for um funcionario, no front-end mandar o ID do user que faz request
            //Se for de um gerente, certificar que esse cliente pertence ao ginasio

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http post para inserção de uma nova marcação na base de dados com o uso das regras de negócio
        /// </summary>
        /// <param name="newMarcacao">Dados da nova marcação a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost("new/marcacao"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> PostChecked([FromBody] Marcacao newMarcacao)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.PostCheckedLogic(sqlDataSource, newMarcacao);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração do estado de uma marcação através do id e novos dados
        /// </summary>
        /// <param name="marcacao">marcação com dados alterados</param>
        /// <param name="targetID">ID da marcação pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("cancelamento/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> PatchCancelMarcacao([FromBody] Marcacao marcacao, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.PatchCancelMarcacaoLogic(sqlDataSource, marcacao, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http patch para alteração da data de uma marcação através do id e novos dados
        /// </summary>
        /// <param name="marcacao">marcação com dados alterados</param>
        /// <param name="targetID">ID da marcação pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("remarcacao/{targetID}"), Authorize(Roles = "Admin, Gerente, Funcionario, Cliente")]
        public async Task<IActionResult> PatchRescheduleMarcacao([FromBody] Marcacao marcacao, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await MarcacaoLogic.PatchRescheduleMarcacaoLogic(sqlDataSource, marcacao, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        #endregion
    }
}
