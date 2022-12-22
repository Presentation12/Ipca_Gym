﻿using Microsoft.AspNetCore.Http;
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

        /// <summary>
        /// Método http get para retornar os planos nutricionais da base de dados
        /// </summary>
        /// <returns>Resposta do request que contém a sua mensagem, seu código e a lista de planos nutricionais em formato Json</returns>
        [HttpGet]
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
        [HttpGet("{targetID}")]
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
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] PlanoNutricional newPlanoNutricional)
        {
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
        [HttpPatch("{targetID}")]
        public async Task<IActionResult> Patch([FromBody] PlanoNutricional planoNutricional, int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.PatchLogic(sqlDataSource, planoNutricional, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);


            return new JsonResult(response);
        }

        /// <summary>
        /// Método http delete para remover um plano nutricional da base de dados através do seu id
        /// </summary>
        /// <param name="targetID">ID da plano nutricional pretendido para ser removido</param>
        /// <returns>Resposta do request que contém a sua mensagem e seu código em formato json</returns>
        [HttpDelete("{targetID}")]
        public async Task<IActionResult> Delete(int targetID)
        {
            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            Response response = await PlanoNutricionalLogic.DeleteLogic(sqlDataSource, targetID);

            if (response.StatusCode != LayerBLL.Utils.StatusCodes.SUCCESS) return StatusCode((int)response.StatusCode);

            return new JsonResult(response);
        }
    }
}
