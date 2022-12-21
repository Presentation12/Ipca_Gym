﻿using Microsoft.AspNetCore.Mvc;
using LayerBOL.Models;
using LayerBLL.Utils;
using LayerBLL.Logics;

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
        /// Retorna todos as atividades presentes na base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de atividades em formato Json</returns>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetAllLogic(sqlDataSource);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);
                
            return new JsonResult(response);
        }

        /// <summary>
        /// Retorna uma atividade com o id passado por parâmetro
        /// </summary>
        /// <param name="targetID">ID da atividade que é pretendida ser retornada</param>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a Atividade em formato Json</returns>
        [HttpGet("{targetID}")]
        public async Task<IActionResult> GetByID(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.GetByIDLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Criação de uma nova Atividade
        /// </summary>
        /// <param name="newAtividade">Dados da nova Atividade a ser inserida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Atividade newAtividade)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.PostLogic(sqlDataSource, newAtividade);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }

        /// <summary>
        /// Fazer atualização dos dados de uma Atividade
        /// </summary>
        /// <param name="atividade">Atividade com dados alterados</param>
        /// <param name="targetID">ID da Atividade pretendida para alterar os dados</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] Atividade atividade, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.PatchLogic(sqlDataSource, atividade, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Remover uma Atividade da base de dados
        /// </summary>
        /// <param name="targetID">ID da Atividade pretendida para ser removida</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await AtividadeLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
